package net.xXinailXx.thirteen_flames.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticleRendererTypes;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;
import org.zeith.hammerlib.net.Network;

import java.awt.*;

public class SoulEntity extends ThrowableProjectile {
    private float heal;

    public SoulEntity(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    public SoulEntity(Level level, float heal) {
        this(EntityRegistry.SOUL.get(), level);
        this.heal = heal;
    }

    public void tick() {
        if (getOwner() == null)
            this.discard();

        super.tick();

        this.setDeltaMovement(this.getDeltaMovement().add(this.getOwner().position().subtract(this.position()).normalize().scale(0.1)));

        ColoredParticle.Options options = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                .color(new Color(0, 159, 191).getRGB())
                .renderType(ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                .diameter(0.15F)
                .lifetime(10)
                .scaleModifier(0.98F)
                .physical(false)
                .build());

        Network.sendToAll(new SpawnParticlePacket(options, this.position().x, this.position().y, this.position().z, 0 ,0, 0));
    }

    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        if (!entity.is(this.getOwner()))
            return;

        if (!(entity instanceof LivingEntity))
            return;

        LivingEntity living = (LivingEntity) entity;
        living.heal(this.heal);
        this.discard();
    }

    protected void defineSynchedData() {
    }
}
