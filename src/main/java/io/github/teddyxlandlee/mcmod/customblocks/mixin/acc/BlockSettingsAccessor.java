package io.github.teddyxlandlee.mcmod.customblocks.mixin.acc;

import net.minecraft.block.AbstractBlock;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.Settings.class)
public interface BlockSettingsAccessor {
    @Accessor
    void setCollidable(boolean value);

    @Accessor
    void setLootTableId(Identifier value);
}
