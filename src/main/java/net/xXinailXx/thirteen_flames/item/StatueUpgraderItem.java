package net.xXinailXx.thirteen_flames.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
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
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.ItemSetting;
import net.xXinailXx.thirteen_flames.network.packet.CreativeUpdateStatuePacket;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.net.Network;

import java.util.List;

@Mod.EventBusSubscriber
public class StatueUpgraderItem extends ItemSetting {
    public StatueUpgraderItem() {
        super(new Properties().tab(ThirteenFlames.ITEMS_TAB).stacksTo(1));
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.add(Component.translatable("item." + ThirteenFlames.MODID + ".statue_upgrader.tooltip"));
    }

    private static BagPaintItem getStack(Gods gods) {
        return switch (gods) {
            case KNEF -> (BagPaintItem) ItemRegistry.BAG_PAINT_KNEF.get();
            case SELYA -> (BagPaintItem) ItemRegistry.BAG_PAINT_SELYA.get();
            case MONTU -> (BagPaintItem) ItemRegistry.BAG_PAINT_MONTU.get();
            case RONOS -> (BagPaintItem) ItemRegistry.BAG_PAINT_RONOS.get();
            case HET -> (BagPaintItem) ItemRegistry.BAG_PAINT_HET.get();
            case GOD_PHARAOH -> (BagPaintItem) ItemRegistry.BAG_PAINT_GOD_PHARAOH.get();
        };
    }

    @SubscribeEvent
    public static void use(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        ItemStack stack = event.getItemStack();

        if (!stack.is(ItemRegistry.STATUE_UPGRADER.get()))
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

        if (block instanceof StatueHandler handler && handler.getBE(pos) != null)
            if (!handler.getBE(pos).isFinished())
                BagPaintItem.updateStatue(getStack(handler.getGod()), player, builder, direction, true);
            else
                Network.sendToServer(new CreativeUpdateStatuePacket(pos));
        else if (block instanceof StatueStructureBlock structure && structure.getMainBlockBE(pos) != null)
            if (!structure.getMainBlockBE(pos).isFinished())
                BagPaintItem.updateStatue(getStack(structure.getMainBlockBE(pos).getGod()), player, builder, direction, true);
            else
                Network.sendToServer(new CreativeUpdateStatuePacket(pos));
    }
}
