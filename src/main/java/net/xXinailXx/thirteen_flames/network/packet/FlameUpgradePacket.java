package net.xXinailXx.thirteen_flames.network.packet;

import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.enderdragonlib.client.particle.*;
import net.xXinailXx.enderdragonlib.utils.statues.data.StatueData;
import net.xXinailXx.thirteen_flames.block.StatueHandler;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.MainThreaded;
import org.zeith.hammerlib.net.PacketContext;

import java.awt.*;

@MainThreaded
public class FlameUpgradePacket implements IPacket {
    private ItemStack stack;
    private BlockPos pos;

    public FlameUpgradePacket(ItemStack stack, BlockPos pos) {
        this.stack = stack;
        this.pos = pos;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
        buf.writeBlockPos(this.pos);
    }

    public void read(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
        this.pos = buf.readBlockPos();
    }

    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();
        ServerLevel level = player.getLevel();

        if (level.getBlockState(this.pos).getBlock() instanceof StatueStructureBlock)
            this.pos = StatueData.getStatue(this.pos).mainPos();

        StatueBE be = (StatueBE) level.getChunkAt(this.pos).getBlockEntity(this.pos);

        if (be == null || !be.isFinished() || be.getTimeToUpgrade() > 0 || !StatueHandler.isUpgrade(this.stack, be.getGod()))
            return;

        LevelingUtils.addExperience(this.stack, 600);

        ItemStack newStack = this.stack.copy();
        player.setItemSlot(EquipmentSlot.MAINHAND, newStack);

        ColoredParticle.Options options = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                .color(new Color(255, 140, 0).getRGB())
                .renderType(ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                .diameter(0.2F)
                .lifetime(100)
                .scaleModifier(0.98F)
                .physical(true)
                .build());

        Vec3 center = new Vec3(player.getX(), player.getY() + 1, player.getZ());

        ParticleActions.createBall(options, center, level, 2, 0.15F);
        be.resetFlameUpgradeData();
    }
}
