package net.xXinailXx.thirteen_flames.block;

import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xXinailXx.dragonworldlib.utils.statues.data.HorizontalDirectionalBlockData;
import net.xXinailXx.thirteen_flames.item.lights.SunSeleaset;
import net.xXinailXx.thirteen_flames.block.entity.SunSeliasetBlockEntity;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;

import javax.annotation.Nullable;

public class SunSeliasetBlock extends HorizontalDirectionalBlockData {
    public static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 9.51, 9.5, 9.51).move(0.205, 0.09, 0.205);

    public SunSeliasetBlock() {
        super( Properties.of( Material.BARRIER).strength(-1.0F).noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegistry.SUN_SELIASET_BLOCK_ENTITY.get().create(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (SunSeliasetBlockEntity.isOpportunityPickUp()) {
            if (player.isCrouching()) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                player.addItem( ItemsRegistry.SUN_SELEASET.get().getDefaultInstance());
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof SunSeleaset) {
                    LevelingUtils.setPoints( stack, SunSeleaset.getPoints() );
                    AbilityUtils.setAbilityPoints( stack, "blessed_light", SunSeleaset.getPointsAbility() );
                }
            }
        } else {
            SunSeliasetBlockEntity.startAnimation();
        }

        return super.use( state, level, pos, player, hand, result );
    }
}
