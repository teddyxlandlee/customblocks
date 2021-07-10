package io.github.teddyxlandlee.mcmod.customblocks.extension;

import com.google.gson.JsonObject;
import io.github.teddyxlandlee.mcmod.customblocks.BaseDataLoader;
import io.github.teddyxlandlee.mcmod.customblocks.registry.JsonDataAdapter;
import net.fabricmc.loader.api.*;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static io.github.teddyxlandlee.mcmod.customblocks.BaseDataLoader.LOGGER;

public class CustomBlocksExtension {
    //public Map<Registry<?>, ?> map = new HashMap<>();
    private final int schemaVersion;

    private final String id;
    private final Version extVersion;

    private final List<ModDependency> fabricModDependency = new ArrayList<>();
    private final List<ModDependency> extensionDependency = new ArrayList<>();
    private final List<ImmutablePair<JsonObject, JsonDataAdapter>> registryFutures = new LinkedList<>();

    public CustomBlocksExtension(int schemaVersion,
                                 String id,
                                 Version extVersion,
                                 Collection<ModDependency> modDependencies,
                                 Collection<ModDependency> extensionDependencies,
                                 Collection<ImmutablePair<JsonObject, JsonDataAdapter>> registries) {
        this.schemaVersion = schemaVersion;
        this.id = id;
        this.extVersion = extVersion;
        this.fabricModDependency.addAll(modDependencies);
        this.extensionDependency.addAll(extensionDependencies);
        this.registryFutures.addAll(registries);
        checkSemVer();

    }

    /** <b>NOTICE</b>: Verified as default. */
    public void initialize() {
        // Handle Dependencies
        Collection<ModContainer> modContainers = FabricLoader.getInstance().getAllMods();
        for (ModDependency dep1 : fabricModDependency) {
            if (!containsMod(modContainers, dep1)) {
                breakInit(dep1);
                return;
            }
        }
        List<CustomBlocksExtension> extensions = BaseDataLoader.INSTANCE.getExtensions();
        for (ModDependency dep1 : extensionDependency) {
            if (!containsExtension(extensions, dep1)) {
                breakInit(dep1);
                return;
            }
        }

        for (ImmutablePair<JsonObject, JsonDataAdapter> registryFuture : registryFutures) {
            registryFuture.getRight().register(registryFuture.getLeft());
        }
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public String getId() {
        return id;
    }

    public Version getExtVersion() {
        return extVersion;
    }

    public List<ModDependency> getFabricModDependency() {
        return fabricModDependency;
    }

    public List<ModDependency> getExtensionDependency() {
        return extensionDependency;
    }

    public List<ImmutablePair<JsonObject, JsonDataAdapter>> getRegistryFutures() {
        return registryFutures;
    }

    private void checkSemVer() {
        if (!(this.extVersion instanceof SemanticVersion)) {
            BaseDataLoader.LOGGER.warn("Extension `{}` does not respect SemVer. Version comparison is" +
                    "limited.", this);
        }
    }

    private void breakInit(ModDependency dep1) {
        LOGGER.error("Extension {} requires fabric mod `{}`, which is missing!",
                this, dep1);
        LOGGER.warn("Ignoring initialization of {}", this);
        BaseDataLoader.INSTANCE.getExtensions().remove(this);
    }

     /*
     KEEP_THIS  = 1,
     KEEP_OTHER = -1,
     KEEP_ALL   = 0;
     */

    public int compare(@NotNull CustomBlocksExtension o) {
        if (!this.id.equals(o.id)) return 0;
        Version selfVer  = this.extVersion,
                otherVer = o.extVersion;
        int compare;
        if (selfVer instanceof SemanticVersion &&
            otherVer instanceof SemanticVersion) {
            compare = ((SemanticVersion) selfVer).compareTo(((SemanticVersion) otherVer));
        } else {
            compare = selfVer.getFriendlyString().compareTo(otherVer.getFriendlyString());
        }
        if (compare == 0) return -1;
        else return compare;
    }

    @Override
    public String toString() {
        return id + '@' + extVersion;
    }

    private static boolean containsMod(Collection<ModContainer> modContainers, ModDependency singleDependency) {
        for (ModContainer modContainer : modContainers) {
            ModMetadata metadata = modContainer.getMetadata();
            if (metadata.getId().equals(singleDependency.getModId())) {
                if (singleDependency.matches(metadata.getVersion()))
                    return true;
            }
        } return false;
    }

    private static boolean containsExtension(Collection<CustomBlocksExtension> extensions, ModDependency singleDependency) {
        for (CustomBlocksExtension ext : extensions) {
            if (ext.id.equals(singleDependency.getModId())) {
                if (singleDependency.matches(ext.extVersion))
                    return true;
            }
        } return false;
    }
}
