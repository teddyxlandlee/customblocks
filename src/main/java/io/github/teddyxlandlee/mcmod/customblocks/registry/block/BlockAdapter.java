package io.github.teddyxlandlee.mcmod.customblocks.registry.block;

import com.google.gson.JsonObject;
import com.mojang.serialization.Lifecycle;
import io.github.teddyxlandlee.mcmod.customblocks.CustomBlocksLoader;
import io.github.teddyxlandlee.mcmod.customblocks.registry.JsonDataAdapter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Map;

/**
 * Here is a sample:<br /><pre>
 *     {
 *         "type": "minecraft:block",   // required
 *         "template": "minecraft:default",
 *         "block_states": {
 *             "type": "int",
 *             "min": 0,
 *             "max": 7
 *         },
 *         "settings": {
 *             //...
 *         }
 *     }
 * </pre>
 *
 * @see net.minecraft.block.Block
 * @see net.minecraft.block.Blocks#STONE
 * @see net.minecraft.block.Blocks#STONE_SLAB
 * @see net.minecraft.block.Blocks#STONE_STAIRS
 * @see net.minecraft.block.Blocks#ACACIA_LOG
 * @see net.minecraft.block.Blocks#ACACIA_FENCE_GATE
 * @see net.minecraft.block.Blocks#ACACIA_FENCE
 * @see JsonDataAdapter
 */
public class BlockAdapter implements JsonDataAdapter {
    @Override
    public void register(JsonObject json) {
        Identifier template0 = CustomBlocksLoader.idFromJson(json, "template", new Identifier("default"));

    }

    @Override
    public boolean verify(JsonObject json) {
        return JsonHelper.getString(json, "type", "").equals("block");
    }


}
