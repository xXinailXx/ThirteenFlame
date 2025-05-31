package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.xXinailXx.thirteen_flames.client.progress.ProgressManager;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
public class HoeItemMixin {
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), cancellable = true)
    public void useOn(UseOnContext use, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = use.getLevel();
        Player player = use.getPlayer();

        if (!ProgressManager.isAllowUsage(use.getItemInHand())) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }

        if (data.isActiveAbility("oasis")) {
            BlockPos.betweenClosed(use.getClickedPos().north().east(), use.getClickedPos().south().west()).forEach(pos -> {
                BlockState state = level.getBlockState(pos);

                if (state.is(BlockTags.DIRT)) {
                    if (!pos.equals(use.getClickedPos()))
                        level.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), 11);
                } else if (state.is(Blocks.FARMLAND)) {
                    CropBlock block = (CropBlock) Blocks.WHEAT;

                    level.setBlock(pos.above(), block.getStateForAge(0), 2);
                    level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);

                    if (player instanceof ServerPlayer)
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, pos.relative(use.getClickedFace()), use.getItemInHand());
                }
            });

            return;
        }

        if (data.isActiveAbility("grain_grower")) {
            if (level.getBlockState(use.getClickedPos()).getBlock() instanceof BonemealableBlock) {
                BlockState state = level.getBlockState(use.getClickedPos());

                BonemealableBlock bonemealableBlock = (BonemealableBlock) state.getBlock();

                if (bonemealableBlock.isValidBonemealTarget(level, use.getClickedPos(), state, level.isClientSide)) {
                    if (!level.isClientSide) {
                        level.levelEvent(1505, use.getClickedPos(), 0);
                        bonemealableBlock.performBonemeal((ServerLevel) level, level.getRandom(), use.getClickedPos(), state);

                        ItemStack stack = use.getItemInHand();
                        int damage = 100 - (data.getLevelAbility("grain_grower") * 5);

                        stack.hurtAndBreak(damage, player, (entity) -> entity.broadcastBreakEvent(use.getHand()));
                    }
                }
            }
        }
    }
}
