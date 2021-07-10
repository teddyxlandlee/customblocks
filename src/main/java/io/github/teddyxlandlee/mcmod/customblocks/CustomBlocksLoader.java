package io.github.teddyxlandlee.mcmod.customblocks;

import com.mojang.serialization.Lifecycle;
import io.github.teddyxlandlee.mcmod.customblocks.registry.JsonDataAdapter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
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

    }

    private static void registerBasicAdapter() {
        registerVanilla("block", null /*TODO*/);
    }

    /** <b>NOTE</b>: this will start with "{@code minecraft:}", not
     * "{@code customblocks:}"! */
    private static JsonDataAdapter registerVanilla(String id, JsonDataAdapter jsonDataAdapter) {
        return Registry.register(ROOT_DATA_LOADER_REGISTRY, id, jsonDataAdapter);
    }

    static {
        ROOT_DATA_LOADER_KEY = RegistryKey.ofRegistry(id("root_data_loader"));
        ROOT_DATA_LOADER_REGISTRY = new SimpleRegistry<>(ROOT_DATA_LOADER_KEY, Lifecycle.stable());
    }

    private static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
