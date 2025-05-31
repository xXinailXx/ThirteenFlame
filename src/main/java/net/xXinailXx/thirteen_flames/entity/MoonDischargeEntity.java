package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.client.particles.circle.CircleTintData;
import it.hurts.sskirillss.relics.client.particles.spark.SparkTintData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoonDischargeEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(MoonDischargeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DMG = SynchedEntityData.defineId(MoonDischargeEntity.class, EntityDataSerializers.FLOAT);
    private boolean isExploding = false;
    public Vec3 shotPos;

    public MoonDischargeEntity(EntityType<? extends MoonDischargeEntity> type, Level world) {
        super(type, world);
    }

    public void tick() {
        Vec3 motion = new Vec3((double)0.0F, 0.6, (double)0.0F);

        super.tick();

        this.setDeltaMovement(!this.isExploding ? motion : Vec3.ZERO);
        if (this.shotPos == null)
            this.shotPos = this.position();

        if (!this.level.isClientSide)
            this.spark(2, 0.04, 0.4F);

        if (this.tickCount > 10)
            this.isExploding = true;
    }

    private void spark(int count, double speed, float diametr) {
        ParticleActions.spawnParticleEntity(new CircleTintData(new Color(175, 117, 245), diametr, 45, 0.9F, false), this, count, speed);
        ParticleActions.spawnParticleEntity(new CircleTintData(new Color(10, 46, 203), diametr, 45, 0.9F, false), this, count, speed);
        ParticleActions.spawnParticleEntity(new SparkTintData(new Color(115, 110, 255), diametr, 50), this, count, speed);
        ParticleActions.spawnParticleEntity(new SparkTintData(new Color(245, 152, 255), diametr, 50), this, count, speed);
        this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.AZALEA_FALL, SoundSource.MASTER, 0.5F, 1.4F + this.random.nextFloat() * 1.6F);
    }

    public void onRemovedFromWorld() {
        this.spark(10, 0.04, 0.15F);
        this.spark(15, 0.01, 0.15F);
        AABB box = this.getBoundingBox().inflate(this.getRadius()).move(0, (-this.getRadius()) * 0.5F, 0).expandTowards(0, -3, 0);

        for(Object obj : new ArrayList(this.getLevel().getEntitiesOfClass(LivingEntity.class, box, (entity) -> !entity.equals(this.getOwner()) && entity.hasLineOfSight(this)))) {
            LivingEntity living = (LivingEntity) obj;
            Vec3 start = this.position();
            Vec3 end = living.getBoundingBox().getCenter();

            if (!this.level.isClientSide()) {
                this.drawJaggedLightning(this.level, start, end, 3, 0.2, 0.15F, new Color(187, 145, 255), true);
                this.drawJaggedLightning(this.level, start, end, 3, 0.2, 0.15F, new Color(222, 127, 255), true);
                this.drawJaggedLightning(this.level, start, end, 3, 0.2, 0.25F, new Color(154, 96, 255), true);
                living.hurt(DamageSource.thrown(this, this.getOwner()), this.getDamage());
            }

            this.getLevel().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1.0F, this.random.nextFloat() * 1.4F + 0.3F);
            this.getLevel().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.03F, this.random.nextFloat() * 1.3F + 0.5F);
            AABB secondaryBox = (new AABB(end, end)).inflate(this.getRadius() * 0.5F).move(0, this.getRadius() * 0.25F, 0);
            List<LivingEntity> secondaryTargets = new ArrayList(this.getLevel().getEntitiesOfClass(LivingEntity.class, secondaryBox, (entity) -> !entity.equals(this.getOwner()) && !entity.equals(obj)));

            if (!secondaryTargets.isEmpty()) {
                LivingEntity secTarget = (LivingEntity)secondaryTargets.get(this.random.nextInt(secondaryTargets.size()));

                if (!this.level.isClientSide()) {
                    AABB box2 = secTarget.getBoundingBox();
                    Vec3 end2 = box2.getCenter().add((double) MathUtils.randomFloat(this.random) * box2.getXsize() * 0.4, (double)MathUtils.randomFloat(this.random) * box2.getYsize() * 0.4, (double)MathUtils.randomFloat(this.random) * box2.getZsize() * 0.4);
                    this.drawJaggedLightning(this.level, end, end2, 3, 0.3, 0.15F, new Color(222, 127, 255), true);
                    end2 = box2.getCenter().add((double)MathUtils.randomFloat(this.random) * box2.getXsize() * 0.4, (double)MathUtils.randomFloat(this.random) * box2.getYsize() * 0.4, (double)MathUtils.randomFloat(this.random) * box2.getZsize() * 0.4);
                    this.drawJaggedLightning(this.level, end, end2, 3, 0.3, 0.25F, new Color(154, 96, 255), true);
                    secTarget.hurt(DamageSource.thrown(this, this.getOwner()), this.getDamage());
                }
            }
        }

        super.onRemovedFromWorld();
    }

    public void drawJaggedLightning(Level level, Vec3 start, Vec3 end, int sliceIterations, double maxJagMultiplier, float d, Color color, boolean doStartBurst) {
        if (doStartBurst) {
            ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(230, 175, 255), 0.6F, 15, 0.68F, false), new AABB(start, start), 10, 0.2);
            ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(230, 175, 255), 0.3F, 30, 0.82F, false), new AABB(start, start), 10, 0.15);
        }

        ParticleActions.spawnRandomJaggedParticleLine(level, start, end, maxJagMultiplier, new CircleTintData(color, d, 35, 0.9F, false), 16, sliceIterations);
        ParticleActions.spawnParticleAABB(this.level, new SparkTintData(new Color(179, 190, 255), 0.4F, 50), new AABB(end, end), 10, 0.06);
        ParticleActions.spawnParticleAABB(this.level, new SparkTintData(new Color(242, 208, 255), 0.4F, 50), new AABB(end, end), 10, 0.06);
        ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(0, 89, 255), 0.4F, 30, 0.8F, false), new AABB(end, end), 10, 0.08);
        ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(221, 117, 255), 0.4F, 30, 0.8F, false), new AABB(end, end), 10, 0.08);
    }

    public void checkDespawn() {
        if (this.tickCount > 15)
            this.discard();
    }

    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    public float getRadius() {
        return (Float)this.getEntityData().get(RADIUS);
    }

    public void setRadius(float radius) {
        this.getEntityData().set(RADIUS, radius);
    }

    public float getDamage() {
        return (Float)this.getEntityData().get(DMG);
    }

    public void setDamage(float damage) {
        this.getEntityData().set(DMG, damage);
    }

    protected void defineSynchedData() {
        this.entityData.define(RADIUS, 5.0F);
        this.entityData.define(DMG, 5.0F);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setRadius(compound.getFloat("radius"));
        this.setDamage(compound.getFloat("damage"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("radius", this.getRadius());
        compound.putFloat("damage", this.getDamage());
    }
}
