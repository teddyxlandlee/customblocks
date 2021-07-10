package io.github.teddyxlandlee.mcmod.customblocks;

import com.google.gson.JsonObject;
import com.mojang.serialization.Lifecycle;
import io.github.teddyxlandlee.mcmod.customblocks.extension.CustomBlocksExtension;
import io.github.teddyxlandlee.mcmod.customblocks.registry.JsonDataAdapter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class CustomBlocksLoader implements ModInitializer {
    public static final RegistryKey<Registry<JsonDataAdapter>> ROOT_DATA_LOADER_KEY;
    public static final Registry<JsonDataAdapter> ROOT_DATA_LOADER_REGISTRY;

    public static final String MODID = "customblocks";

    //public static final JsonObjectRegistry<Block> BLOCK_ROOT = null;

    @Override
    public void onInitialize() {
        try {
            BaseDataLoader.INSTANCE.load();
        } catch (Throwable e) {
            BaseDataLoader.LOGGER.fatal("A critical error caused while initializing Custom Blocks Loader", e);
            return;
        }
        registerBasicAdapter();

        BaseDataLoader.INSTANCE.getExtensions().forEach(CustomBlocksExtension::initialize);
    }

    private static void registerBasicAdapter() {
        registerVanilla("block", null /*TODO*/);
    }

    /** <b>NOTE</b>: this will start with "{@code minecraft:}", not
     * "{@code customblocks:}"! */
    private static void registerVanilla(String id, JsonDataAdapter jsonDataAdapter) {
        Registry.register(ROOT_DATA_LOADER_REGISTRY, id, jsonDataAdapter);
    }

    static {
        ROOT_DATA_LOADER_KEY = RegistryKey.ofRegistry(id("root_data_loader"));
        ROOT_DATA_LOADER_REGISTRY = new SimpleRegistry<>(ROOT_DATA_LOADER_KEY, Lifecycle.stable());
    }

    public static Identifier id(String path) { return new Identifier(MODID, path); }
    public static Identifier idFromJson(JsonObject json, String key, Identifier defaultValue) {
        if (JsonHelper.hasString(json, key)) {
            Identifier id = Identifier.tryParse(JsonHelper.getString(json, key));
            if (id != null) return id;
        } return defaultValue;
    }
}
