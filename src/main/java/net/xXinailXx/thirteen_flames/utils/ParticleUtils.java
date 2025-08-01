package net.xXinailXx.thirteen_flames.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticleRendererTypes;
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import org.zeith.hammerlib.net.Network;

import java.awt.*;

public class ParticleUtils {
    public static void spawnCupFire(Level level, Color color, BlockPos pos) {
        Player player = Minecraft.getInstance().player;

        if (player == null)
            return;

        ColoredParticle.Options options = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                .color(color.getRGB())
                .renderType(color.equals(new Color(0, 0, 0)) ? ColoredParticleRendererTypes.DISABLE_RENDER_LIGHT_COLOR : ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                .diameter(0.2F)
                .lifetime(50)
                .physical(false)
                .build());

        Vec3 vec3 = new Vec3(pos.getX() + 0.5F, pos.getY() + 0.95F, pos.getZ() + 0.5F);

        AABB box = new AABB(vec3.add(-0.1, 0, -0.1), vec3.add(0.2, 0, 0.2));

        double deltaX = box.getXsize() / 2;
        double deltaY = box.getYsize() / 2 + 0.1;
        double deltaZ = box.getZsize() / 2;

        for(int i = 0; i < 15; ++i) {
            double d1 = level.random.nextGaussian() * deltaX;
            double d3 = level.random.nextGaussian() * deltaY;
            double d5 = level.random.nextGaussian() * deltaZ;
            double d6 = level.random.nextGaussian() * 0.003;
            double d7 = level.random.nextGaussian() * 0.003;
            double d8 = level.random.nextGaussian() * 0.003;

            player.level.addParticle(options, vec3.x + d1, vec3.y + d3, vec3.z + d5, d6, d7 + 0.02, d8);
        }
    }

    public static ColoredParticle.Options createOtherGodsParticle(Color color, float diameter, int lifetime, float scaleModifier) {
        return new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                .color(color.getRGB())
                .renderType(ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                .diameter(diameter)
                .lifetime(lifetime)
                .scaleModifier(scaleModifier)
                .physical(false)
                .build());
    }

    public static ColoredParticle.Options createKnefParticle(Color color, float diameter, int lifetime, float scaleModifier) {
        return new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                .color(color.getRGB())
                .renderType(ColoredParticleRendererTypes.DISABLE_RENDER_LIGHT_COLOR)
                .diameter(diameter)
                .lifetime(lifetime)
                .scaleModifier(scaleModifier)
                .physical(false)
                .build());
    }
}
