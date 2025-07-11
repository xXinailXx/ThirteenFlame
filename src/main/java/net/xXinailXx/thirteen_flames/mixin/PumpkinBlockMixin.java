package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PumpkinBlock.class)
public class PumpkinBlockMixin extends PumpkinBlock {
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    public PumpkinBlockMixin(Properties p_55284_) {
        super(p_55284_);
    }

    @Overwrite
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.canPerformAction(net.minecraftforge.common.ToolActions.SHEARS_CARVE)) {
            if (!level.isClientSide) {
                Direction direction = result.getDirection();
                Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
                level.playSound((Player)null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(pos, Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction1), 11);
                ItemEntity itementity = new ItemEntity(level, (double)pos.getX() + 0.5D + (double)direction1.getStepX() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction1.getStepZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
                itementity.setDeltaMovement(0.05D * (double)direction1.getStepX() + level.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getStepZ() + level.random.nextDouble() * 0.02D);
                level.addFreshEntity(itementity);

                if (!data.isActiveAbility(player, "scissorhands"))
                    itemstack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(hand));

                level.gameEvent(player, GameEvent.SHEAR, pos);
                player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(state, level, pos, player, hand, result);
        }
    }
}
