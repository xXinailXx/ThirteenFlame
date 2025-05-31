package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.client.particles.circle.CircleTintData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoonProjectileSpecialEntity extends ThrowableProjectile {
    public Vec3 prevPos;
    public Color color = new Color(255, 100, 255);

    public MoonProjectileSpecialEntity(EntityType<? extends MoonProjectileSpecialEntity> type, Level world) {
        super(type, world);
    }

    public void tick() {
        if (this.getOwner() == null) {
            this.discard();
        }

        Vec3 motion = this.getDeltaMovement();
        super.tick();
        this.setDeltaMovement(motion);

        if (!this.level.isClientSide)
            ParticleActions.spawnParticleLine(this.level, new CircleTintData(this.color, 0.1F, 40, 0.92F, false), this.prevPos == null ? this.position() : this.prevPos, this.position(), 25, 0);

        this.prevPos = this.position();
    }

    public void checkDespawn() {
        if (this.tickCount > 240)
            this.discard();
    }

    public static java.util.List<MoonProjectileSpecialEntity> makeList(int count, Level level, Entity owner, Vec3 center, Vec3 move) {
        List<MoonProjectileSpecialEntity> list = new ArrayList();

        for(int i = 0; i < count; ++i) {
            MoonProjectileSpecialEntity proj = new MoonProjectileSpecialEntity(EntityRegistry.MOON_PROJECTILE_SPECIAL.get(), level);
            proj.setOwner(owner);
            proj.setPos(center);
            proj.setDeltaMovement(move);
            proj.color = new Color(255 - proj.random.nextInt(100), 100, 255 - proj.random.nextInt(10));
            list.add(proj);
        }

        return list;
    }

    public boolean canCollideWith(Entity pEntity) {
        return pEntity instanceof LivingEntity;
    }

    @SubscribeEvent
    public void onLevelUnload(PlayerEvent.PlayerLoggedOutEvent event) {
        this.discard();
    }

    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    public boolean shouldRender(double p_20296_, double p_20297_, double p_20298_) {
        return false;
    }
}
