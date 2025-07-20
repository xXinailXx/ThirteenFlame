package net.xXinailXx.thirteen_flames.client.gui.button.abilities.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.enderdragonlib.utils.AABBUtils;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;

import java.util.Iterator;
import java.util.List;

public class AbilityUtils {
    private static final IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    public static void breakBlock(Player player, Level level, BlockPos pos, int range) {
        if (player.getDirection() == Direction.NORTH || player.getDirection() == Direction.SOUTH) {
            Iterator iterator = BlockPos.betweenClosed(pos.offset(-range, -range, 0), pos.offset(range, range, 0)).iterator();

            while (iterator.hasNext()) {
                BlockPos targetPos = (BlockPos) iterator.next();
                BlockState blockstate = level.getBlockState( targetPos );
                Block block = blockstate.getBlock();
                if ((blockstate.getBlock() != Blocks.BEDROCK || block != Blocks.BARRIER || !(block instanceof CommandBlock)))
                    level.destroyBlock(targetPos, true);
            }
        } else if (player.getDirection() == Direction.EAST || player.getDirection() == Direction.WEST) {
            Iterator iterator = BlockPos.betweenClosed(pos.offset(0, -range, -range), pos.offset(0, range, range)).iterator();
            while (iterator.hasNext()) {
                BlockPos targetPos = (BlockPos) iterator.next();
                BlockState blockstate = level.getBlockState( targetPos );
                Block block = blockstate.getBlock();
                if ((blockstate.getBlock() != Blocks.BEDROCK || block != Blocks.BARRIER || ! (block instanceof CommandBlock)))
                    level.destroyBlock(targetPos, true);
            }
        } else if (player.getDirection() == Direction.UP || player.getDirection() == Direction.DOWN) {
            Iterator iterator = BlockPos.betweenClosed(pos.offset(-range, 0, -range), pos.offset(range, 0, range)).iterator();
            while (iterator.hasNext()) {
                BlockPos targetPos = (BlockPos) iterator.next();
                BlockState blockstate = level.getBlockState( targetPos );
                Block block = blockstate.getBlock();
                if ((blockstate.getBlock() != Blocks.BEDROCK || block != Blocks.BARRIER || ! (block instanceof CommandBlock)))
                    level.destroyBlock(targetPos, true);
            }
        }
    }

    public static List<LivingEntity> getEntities(LivingEntity entity, double radius) {
        if (entity == null || entity.getLevel() == null)
            return List.of();

        return AABBUtils.getEntities(LivingEntity.class, entity, radius);
    }

    public static PlayerPosYState playerLevelSea(Player player) {
        if (player == null) {
            return PlayerPosYState.NONE;
        }

        if (data.isActiveAbility(player, "deep_kinship") && !data.isActiveAbility(player, "celestial_kinship") && !data.isActiveAbility(player, "terrestrial_kinship")) {
            if (player.getY() < -60)
                return PlayerPosYState.BELOW_SEA;
        } else if (data.isActiveAbility(player, "celestial_kinship") && !data.isActiveAbility(player, "deep_kinship") && !data.isActiveAbility(player, "terrestrial_kinship")) {
            if (player.getY() >= -60 && player.getY() <= 60)
                return PlayerPosYState.LEVEL_SEA;
        } else if (data.isActiveAbility(player, "terrestrial_kinship") && !data.isActiveAbility(player, "deep_kinship") && !data.isActiveAbility(player, "celestial_kinship")) {
            if (player.getY() > 60)
                return PlayerPosYState.ABOVE_SEA;
        }

        return PlayerPosYState.NONE;
    }

    public enum PlayerPosYState {
        ABOVE_SEA,
        LEVEL_SEA,
        BELOW_SEA,
        NONE
    }
}
