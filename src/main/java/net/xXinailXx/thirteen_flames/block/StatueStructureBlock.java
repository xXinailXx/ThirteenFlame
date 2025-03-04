package net.xXinailXx.thirteen_flames.block;

import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.utils.statues.StatueSize3x3Util;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber
public class StatueStructureBlock extends Block {
    public StatueStructureBlock() {
        super(Properties.of(Material.BARRIER).strength(-1.0F).noCollission().noOcclusion());
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        if (!level.isClientSide)
            destroyBlock(level, pos);

        super.playerDestroy(level, player, pos, state, entity, stack);
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide)
            destroyBlock(level, pos);

        super.wasExploded(level, pos, explosion);
    }

    private void destroyBlock(Level level, BlockPos pos) {
        BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1)).iterator().forEachRemaining(pos1 -> {
            level.destroyBlock(pos1, false);
        });
    }

    @SubscribeEvent
    public static void playerUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);

        if (state.getBlock() instanceof StatueStructureBlock && !(state.getBlock() instanceof GodFaraonStructureBlock) || state.getBlock() instanceof StatueSize3x3Util) {
            Item item = event.getItemStack().getItem();

            if (item instanceof FlameItemSetting setting) {
                if (item.equals(ItemsRegistry.MOLOT_MONTU) || item.equals(ItemsRegistry.GLOVES_MONTU)) {
                    if (state.getBlock().equals(BlockRegistry.STATUE_MONTU_BLOCK))
                        LevelingUtils.addLevel(item.getDefaultInstance(), 1);
                } else if (item.equals(ItemsRegistry.SWORD_RONOSA) || item.equals(ItemsRegistry.SHIELD_RONOSA)) {
                    if (state.getBlock().equals(BlockRegistry.STATUE_RONOS_BLOCK))
                        LevelingUtils.addLevel(item.getDefaultInstance(), 1);
                } else if (item.equals(ItemsRegistry.BLACK_ROSE) || item.equals(ItemsRegistry.MOONBOW)) {
                    if (state.getBlock().equals(BlockRegistry.STATUE_KNEF_BLOCK))
                        LevelingUtils.addLevel(item.getDefaultInstance(), 1);
                } else if (item.equals(ItemsRegistry.GORN_SELEASET) || item.equals(ItemsRegistry.SUN_SELEASET)) {
                    if (state.getBlock().equals(BlockRegistry.STATUE_SELYA_BLOCK))
                        LevelingUtils.addLevel(item.getDefaultInstance(), 1);
                } else if (item.equals(ItemsRegistry.SCROLL_HET) || item.equals(ItemsRegistry.FLIGHT_HET)) {
                    if (state.getBlock().equals(BlockRegistry.STATUE_HET_BLOCK))
                        LevelingUtils.addLevel(item.getDefaultInstance(), 1);
                }
            }
        }
    }
}
