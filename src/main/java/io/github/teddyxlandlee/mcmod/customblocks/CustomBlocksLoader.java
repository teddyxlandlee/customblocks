package io.github.teddyxlandlee.mcmod.customblocks;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class CustomBlocksLoader implements ModInitializer {
    public static final RegistryKey<Registry<JsonObjectLoader<?>>> ROOT_DATA_LOADER_KEY;
    public static final Registry<JsonObjectLoader<?>> ROOT_DATA_LOADER_REGISTRY;

    public static final String MODID = "customblocks";

    public static final JsonObjectLoader<Block> BLOCK_ROOT = null;

    @Override
    public void onInitialize() {

    }

    static {
        ROOT_DATA_LOADER_KEY = RegistryKey.ofRegistry(id("root_data_loader"));
        ROOT_DATA_LOADER_REGISTRY = new SimpleRegistry<>(ROOT_DATA_LOADER_KEY, Lifecycle.stable());
    }

    private static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
