package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();
    @Shadow public abstract ItemStack getItem();
    @Shadow public abstract ItemEntity copy();
    @Shadow public abstract void setItem(ItemStack p_32046_);

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tick(CallbackInfo ci) {
        if (data.isActiveAbility(Minecraft.getInstance().player, "wonderful_garden")) {
            if (this.getItem().getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();

                if (block instanceof SaplingBlock) {
                    Level level = this.copy().getLevel();

                    if (level != null) {
                        BlockPos pos = this.copy().getOnPos();
                        BlockState state = level.getBlockState(pos.above());

                        if (state.is(Blocks.AIR) && (level.getBlockState(pos).is(BlockTags.DIRT) || level.getBlockState(pos).is(Blocks.FARMLAND))) {
                            this.setItem(ItemStack.EMPTY);
                            level.setBlock(pos.above(), block.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }
}
