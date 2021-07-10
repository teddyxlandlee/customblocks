package io.github.teddyxlandlee.mcmod.customblocks;

import com.google.gson.*;
import io.github.teddyxlandlee.mcmod.customblocks.extension.CustomBlocksExtension;
import io.github.teddyxlandlee.mcmod.customblocks.extension.DependencyImpl;
import io.github.teddyxlandlee.mcmod.customblocks.registry.CustomRegistryHelper;
import io.github.teddyxlandlee.mcmod.customblocks.registry.JsonDataAdapter;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.minecraft.util.JsonHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class BaseDataLoader {
    private static boolean _initialized$ = false;
    public static final BaseDataLoader INSTANCE = new BaseDataLoader();
    private BaseDataLoader() {
        synchronized (BaseDataLoader.class) {
            if (_initialized$)
                throw new UnsupportedOperationException("BaseDataLoader initialized twice");
            _initialized$ = true;
        }
    }

    public static final Logger LOGGER = LogManager.getLogger("Custom Blocks Loader");

    public static final Path DATA_PATH = FabricLoader.getInstance().getGameDir().resolve("customblocks");

    private static final int CURRENT_DATA_VERSION = 1;

    private List<CustomBlocksExtension> extensions;

    public @NotNull List<CustomBlocksExtension> getExtensions() {
        if (extensions == null) throw new UnsupportedOperationException("Access getExtensions() too early!");
        //return Collections.unmodifiableList(extensions);
        return extensions;
    }

    public void load() throws IOException, IllegalArgumentException {
        File[] files = DATA_PATH.toFile().listFiles((parentDir, filename) -> filename.endsWith(".json") || filename.endsWith("json5"));
        if (files == null) throw new IOException("Cannot load files from" + DATA_PATH);

        this.extensions = new ArrayList<CustomBlocksExtension>() {
            @Override
            public boolean add(CustomBlocksExtension added) {
                for (CustomBlocksExtension previous : this) {
                    int compareResult = added.compare(previous);
                    if (compareResult < 0) {        // keep previous
                        LOGGER.info("Extension {} is earlier than {}. Won't" +
                                "load {}.", added, previous, added);
                        return false;
                    } else if (compareResult > 0) { // keep added
                        LOGGER.info("Extension {} is later than {}. Won't load {}.",
                                added, previous, previous);
                        this.remove(previous);
                    }
                }
                return super.add(added);
            }
        };
        for (File file : files) {
            if (file.isDirectory()) continue;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            JsonObject root = JsonHelper.deserialize(reader, true);

            // Core
            String id = JsonHelper.getString(root, "id");
            if (!validId(id))
                throw new IllegalArgumentException("Invalid extension id: \"" + id + "\". " +
                        "Should only contain lowercase letters, numbers, `-` or `_`");
            int schemaVersion = JsonHelper.getInt(root, "schema");
            checkSchemaVersion(schemaVersion, id);

            Version extVersion;
            try {
                extVersion = Version.parse(JsonHelper.getString(root, "version"));
            } catch (VersionParsingException e) {
                throw new IllegalArgumentException("Illegal version for extension " + id);
            }

            JsonObject dependencies0 = JsonHelper.getObject(root, "depends", emptyObject());
            JsonObject fabricModDependencies0 = JsonHelper.getObject(dependencies0 ,"mods", null);
            JsonObject extensionDependencies0 = JsonHelper.getObject(dependencies0, "extensions", null);
            List<ModDependency>
                    fabricModDependencies = readDependencies(fabricModDependencies0),
                    extensionDependencies = readDependencies(extensionDependencies0);

            JsonArray registries0 = JsonHelper.getArray(root, "data");
            int i = 0; List<ImmutablePair<JsonObject, JsonDataAdapter>> adapters = new ArrayList<>();
            for (Iterator<JsonElement> iterator = registries0.iterator(); iterator.hasNext(); ++i) {
                JsonElement je = iterator.next();
                if (!je.isJsonObject()) {
                    LOGGER.error("Invalid registry at extension \"" + id + "\", data #" + i + "." +
                            " Will be ignored.");
                    continue;
                }
                JsonObject registry0 = je.getAsJsonObject();
                adapters.add(ImmutablePair.of(registry0, CustomRegistryHelper.fromJson(registry0, id)));
            }
            extensions.add(new CustomBlocksExtension(
                    schemaVersion, id,
                    extVersion, fabricModDependencies,
                    extensionDependencies, adapters
            ));
        }
    }

    private static void checkSchemaVersion(int version, String id) throws IllegalArgumentException {
        if (version < 1) throw new IllegalArgumentException("data version of" +
                "extension" + id + " is too low");
        if (version > CURRENT_DATA_VERSION) throw new IllegalArgumentException("unsupported data version. Use " +
                "later version of custom blocks loader.");
    }

    private static JsonObject emptyObject() { return new JsonObject(); }

    private static List<ModDependency> readDependencies(@Nullable JsonObject root)
            throws IllegalArgumentException {
        //Map<String, ModDependency> ret = new HashMap<>();
        List<ModDependency> ret = new ArrayList<>();
        if (root == null) return ret;
        for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
            List<String> matcherStringList = new ArrayList<>();

            JsonElement value = entry.getValue();
            JsonPrimitive jp;
            if (value.isJsonArray()) {
                for (JsonElement je : value.getAsJsonArray()) {
                    if (!je.isJsonPrimitive() || !(jp = je.getAsJsonPrimitive()).isString())
                        throw new IllegalArgumentException("Dependency version range array must only contain string values");
                    matcherStringList.add(jp.getAsString());
                }
            } else if (value.isJsonPrimitive() && (jp = value.getAsJsonPrimitive()).isString()) {
                matcherStringList.add(jp.getAsString());
            } else throw new IllegalArgumentException("Dependency version range must be a string or string array!");

            ret.add(new DependencyImpl(entry.getKey(), matcherStringList));
        } return ret;
    }

    private static boolean validId(String id) {
        return ID_PATTERN.matcher(id).matches();
    }

    private static final Pattern ID_PATTERN =
            Pattern.compile("^[0-9a-z_\\-]+$");
}
