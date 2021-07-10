package io.github.teddyxlandlee.mcmod.customblocks.registry.block;

import io.github.teddyxlandlee.mcmod.customblocks.mixin.acc.BlockSettingsAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

/**
 * @see net.minecraft.block.Block.Settings
 */
public class BlockSettingsBuilder implements Cloneable {
    private Material material = Material.STONE;
    /** With `hit box` */
    private boolean collidable = true;
    private BlockSoundGroup soundGroup = BlockSoundGroup.STONE;
    private ToIntFunction<BlockState> luminance = state -> 0;
    private float resistance, hardness;
    private boolean toolRequired;
    private boolean randomTicks;
    /** @see net.minecraft.block.IceBlock */
    private float slipperiness = 0.6f;
    //private float velocityMultiplier, jumpVelocityMultiplier;
    @Nullable private Identifier lootTableId;
    /** Non-transparent */
    private boolean opaque = true;
    private boolean isAir;

    public Material getMaterial() {
        return material;
    }

    public BlockSettingsBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public BlockSettingsBuilder setCollidable(boolean collidable) {
        this.collidable = collidable;
        return this;
    }

    public BlockSoundGroup getSoundGroup() {
        return soundGroup;
    }

    public BlockSettingsBuilder setSoundGroup(BlockSoundGroup soundGroup) {
        this.soundGroup = soundGroup;
        return this;
    }

    public ToIntFunction<BlockState> getLuminance() {
        return luminance;
    }

    public BlockSettingsBuilder setLuminance(ToIntFunction<BlockState> luminance) {
        this.luminance = luminance;
        return this;
    }

    public float getResistance() {
        return resistance;
    }

    public BlockSettingsBuilder setResistance(float resistance) {
        this.resistance = resistance;
        return this;
    }

    public float getHardness() {
        return hardness;
    }

    public BlockSettingsBuilder setHardness(float hardness) {
        this.hardness = hardness;
        return this;
    }

    public boolean isToolRequired() {
        return toolRequired;
    }

    public BlockSettingsBuilder setToolRequired(boolean toolRequired) {
        this.toolRequired = toolRequired;
        return this;
    }

    public boolean hasRandomTicks() {
        return randomTicks;
    }

    public BlockSettingsBuilder setRandomTicks(boolean randomTicks) {
        this.randomTicks = randomTicks;
        return this;
    }

    public float getSlipperiness() {
        return slipperiness;
    }

    public BlockSettingsBuilder setSlipperiness(float slipperiness) {
        this.slipperiness = slipperiness;
        return this;
    }

    public Identifier getLootTableId() {
        return lootTableId;
    }

    public BlockSettingsBuilder setLootTableId(Identifier lootTableId) {
        this.lootTableId = lootTableId;
        return this;
    }

    public boolean isOpaque() {
        return opaque;
    }

    public BlockSettingsBuilder setOpaque(boolean opaque) {
        this.opaque = opaque;
        return this;
    }

    public boolean isAir() {
        return isAir;
    }

    public BlockSettingsBuilder setAir(boolean air) {
        isAir = air;
        return this;
    }

    BlockSettingsBuilder() {}
    public static BlockSettingsBuilder of() { return new BlockSettingsBuilder(); }

    @Override
    public BlockSettingsBuilder clone() {
        try { return (BlockSettingsBuilder) super.clone(); }
        catch (CloneNotSupportedException e) { throw new InternalError(e); }
    }

    public AbstractBlock.Settings build() {
        AbstractBlock.Settings settings = AbstractBlock.Settings.of(this.material);
        BlockSettingsAccessor acc = (BlockSettingsAccessor) settings;
        acc.setCollidable(collidable);
        if (!opaque) settings.nonOpaque();
        settings.slipperiness(slipperiness)
                .luminance(luminance)
                .strength(hardness, resistance);
        if (randomTicks) settings.ticksRandomly();
        acc.setLootTableId(lootTableId);
        if (isAir) settings.air();
        if (toolRequired) settings.requiresTool();
        return settings;
    }
}
