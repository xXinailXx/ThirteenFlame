package net.xXinailXx.thirteen_flames.network.packet;

import it.hurts.sskirillss.relics.client.screen.description.RelicDescriptionScreen;
import it.hurts.sskirillss.relics.items.relics.base.RelicItem;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.tiles.ResearchingTableTile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AddXpFlameItemPacket {
    private final BlockPos pos;

    public AddXpFlameItemPacket(BlockPos pos) {
        this.pos = pos;
    }

    public AddXpFlameItemPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();

            if (player != null) {
                Level world = player.level;
                BlockEntity blockEntity = world.getBlockEntity(this.pos);

                if (blockEntity instanceof ResearchingTableTile) {
                    ResearchingTableTile tile = (ResearchingTableTile) blockEntity;
                    ItemStack tileStack = tile.getStack();
                    Item item = tileStack.getItem();

                    if (item instanceof RelicItem) {
                        LevelingUtils.addExperience(player, tileStack, 50);

                        world.sendBlockUpdated(this.pos, world.getBlockState(this.pos), world.getBlockState(this.pos), 2);
                    }
                }
            }
        });

        return true;
    }
}
