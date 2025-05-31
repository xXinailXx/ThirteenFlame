package net.xXinailXx.thirteen_flames.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticleRendererTypes;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;

import java.awt.*;

public class ParticleUtils {
    public static void spawnCupFire(Level level, Color color, BlockPos pos) {
        ColoredParticle.Options particle = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                .color(color.getRGB())
                .renderType(color.equals(new Color(0, 0, 0)) ? ColoredParticleRendererTypes.DISABLE_RENDER_LIGHT_COLOR : ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                .diameter(0.2F)
                .lifetime(50)
                .physical(false)
                .runnableType(ParticleMoveRunnableType.CUP_FIRE)
                .build());

        Vec3 vec3 = new Vec3(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);

        AABB box = new AABB(vec3.add(-0.1, 0, -0.1), vec3.add(0.2, 0, 0.2));

        double deltaX = box.getXsize() / 2;
        double deltaY = box.getYsize() / 2;
        double deltaZ = box.getZsize() / 2;

        ParticleActions.spawnParticles(level, particle, vec3.x, vec3.y, vec3.z, 10, deltaX, deltaY, deltaZ, 0.003);
    }

    public static ColoredParticle.Options createStatueParticle(Color color, float diameter, int lifetime, float scaleModifier) {
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
