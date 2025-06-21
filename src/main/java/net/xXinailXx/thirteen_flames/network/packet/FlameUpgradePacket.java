package net.xXinailXx.thirteen_flames.network.packet;

import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.enderdragonlib.client.particle.*;
import net.xXinailXx.thirteen_flames.block.StatueHandler;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.PacketContext;

import java.awt.*;

public class FlameUpgradePacket implements IPacket {
    private BlockPos pos;
    private ItemStack stack;

    public FlameUpgradePacket(BlockPos pos, ItemStack stack) {
        this.pos = pos;
        this.stack = stack;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeItem(this.stack);
    }

    public void read(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.stack = buf.readItem();
    }

    public void serverExecute(PacketContext ctx) {
        ServerPlayer player = ctx.getSender();
        ServerLevel level = player.getLevel();
        BlockState state = level.getBlockState(this.pos);

        if (!(state.getBlock() instanceof StatueHandler handler || state.getBlock() instanceof StatueStructureBlock))
            return;

        Data.StatueBuilderData.StatueBuilder builder = null;

        for (Data.StatueBuilderData.StatueBuilder b : Data.StatueBuilderData.getStatueList())
            if (b.posList().contains(this.pos))
                builder = b;

        StatueBE statue = Data.StatueBuilderData.getStatueBEList().get(Data.StatueBuilderData.getStatueList().indexOf(builder));

        if (statue == null)
            return;

        if (statue.getGod().equals(Gods.GOD_PHARAOH))
            return;

        if (statue.getTimeToUpgrade() > 0)
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
                .physical(false)
                .build());

        Vec3 center = new Vec3(player.getX(), player.getY() + 1, player.getZ());

        ParticleActions.createBall(options, center, level, 2, 0.15F);
        statue.resetFlameUpgradeData();
        Data.StatueBuilderData.addStatue(builder, statue);
    }
}
