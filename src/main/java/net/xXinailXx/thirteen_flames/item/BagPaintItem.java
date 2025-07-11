package net.xXinailXx.thirteen_flames.item;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.StatueHandler;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.ItemSetting;
import net.xXinailXx.thirteen_flames.utils.Gods;

@Getter
@Mod.EventBusSubscriber
public class BagPaintItem extends ItemSetting {
    private final Gods god;

    public BagPaintItem(Gods god) {
        super(new Properties().tab(ThirteenFlames.ITEMS_TAB).stacksTo(1));
        this.god = god;
    }

    public static void updateStatue(BagPaintItem item, Player player, Data.StatueBuilderData.StatueBuilder builder, Direction direction, boolean shrink) {
        if (item.getGod().equals(Gods.GOD_PHARAOH)) {
            CustomStatueUtils newStatue = (CustomStatueUtils) BlockRegistry.STATUE_GOD_PHARAOH.get();

            player.getLevel().setBlock(builder.mainPos(), newStatue.defaultBlockState().setValue(CustomStatueUtils.FACING, direction), 11);
        } else {
            StatueHandler handler = (StatueHandler) player.getLevel().getBlockState(builder.mainPos()).getBlock();
            StatueHandler newHadler = null;

            switch (handler.getGod()) {
                case KNEF -> newHadler = (StatueHandler) BlockRegistry.STATUE_KNEF.get();
                case SELYA -> newHadler = (StatueHandler) BlockRegistry.STATUE_SELYA.get();
                case MONTU -> newHadler = (StatueHandler) BlockRegistry.STATUE_MONTU.get();
                case RONOS -> newHadler = (StatueHandler) BlockRegistry.STATUE_RONOS.get();
                case HET -> newHadler = (StatueHandler) BlockRegistry.STATUE_HET.get();
            }

            player.getLevel().setBlock(builder.mainPos(), newHadler.defaultBlockState().setValue(CustomStatueUtils.FACING, direction), 11);
        }

        if (shrink && !player.isCreative())
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemRegistry.BAG_PAINT.get().getDefaultInstance());
    }

    @SubscribeEvent
    public static void use(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        ItemStack stack = event.getItemStack();

        if (!(stack.getItem() instanceof BagPaintItem) || stack.is(ItemRegistry.BAG_PAINT.get()))
            return;

        Level level = player.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block.defaultBlockState().is(BlockRegistry.STATUE_CUP_UNFINISHED.get())) {
            level.setBlock(pos, BlockRegistry.STATUE_CUP.get().defaultBlockState(), 11);

            return;
        }

        Data.StatueBuilderData.StatueBuilder builder = Data.StatueBuilderData.getStatue(pos);

        if (builder == null)
            return;

        Direction direction = level.getBlockState(builder.mainPos()).getValue(CustomStatueUtils.FACING);

        if (block instanceof StatueHandler handler && handler.getBE(pos) != null && !handler.getBE(pos).isFinished() && ((BagPaintItem) stack.getItem()).getGod().equals(handler.getGod()))
            updateStatue(((BagPaintItem) stack.getItem()), player, builder, direction, true);
        else if (block instanceof StatueStructureBlock structure && structure.getMainBlockBE(pos) != null && !structure.getMainBlockBE(pos).isFinished() && ((BagPaintItem) stack.getItem()).getGod().equals(structure.getMainBlockBE(builder.mainPos()).getGod()))
            updateStatue(((BagPaintItem) stack.getItem()), player, builder, direction, true);
    }
}
