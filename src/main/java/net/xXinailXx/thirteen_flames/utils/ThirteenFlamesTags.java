package net.xXinailXx.thirteen_flames.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.xXinailXx.thirteen_flames.ThirteenFlames;

public class ThirteenFlamesTags {
    public static final TagKey<Block> NEEDS_FLAME_TOOL = tag("needs_flame_tool");

    private static TagKey<Block> tag(String name) {
        return BlockTags.create(new ResourceLocation(ThirteenFlames.MODID, name));
    }

    private static TagKey<Block> forgeTag(String name) {
        return BlockTags.create(new ResourceLocation("forge", name));
    }
}
