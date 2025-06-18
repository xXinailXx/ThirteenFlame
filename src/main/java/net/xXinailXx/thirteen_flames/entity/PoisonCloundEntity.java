package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.client.particles.circle.CircleTintData;
import it.hurts.sskirillss.relics.client.particles.spark.SparkTintData;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;
import net.xXinailXx.thirteen_flames.init.EffectsRegistry;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class PoisonCloundEntity extends Projectile {
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(PoisonCloundEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> APLIFIRE = SynchedEntityData.defineId(PoisonCloundEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(PoisonCloundEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(PoisonCloundEntity.class, EntityDataSerializers.FLOAT);
    private final Random random = new Random();
    private ItemStack sword;
    private int cooldown = 0;

    public PoisonCloundEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.sword = ItemStack.EMPTY;
    }

    public void tick() {
        super.tick();

        if (this.tickCount > this.getLifeTime())
            this.discard();

        float radius = this.getRadius() * (1.0F - (float)this.tickCount / (float)this.getLifeTime());
        AABB box = (new AABB(this.getPosition(1.0F), this.getPosition(1.0F))).inflate((double)radius);

        if (this.getLevel() instanceof ServerLevel) {
            ParticleActions.spawnParticleAABB(this.getLevel(), new CircleTintData(new Color(85 - this.random.nextInt(80) + this.random.nextInt(80), 255 - this.random.nextInt(160), 0), radius / 6.0F + 0.1F, 80, 0.94F, false), box, Math.round(radius * radius * 2.0F) + 1, 0.01 * (double)radius);
            ParticleActions.spawnParticleAABB(this.getLevel(), new SparkTintData(new Color(85 - this.random.nextInt(80), 255 - this.random.nextInt(160), 0), radius / 10.0F, 60), box, Math.round(radius * radius) + 1, (double)0.0F);
        }

        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, box, (entity) -> !entity.equals(this.getOwner()));

        if (this.cooldown == 0) {
            for(LivingEntity e : entities) {
                e.hurt(DamageSource.MAGIC, 2.0F + radius);
                int maxAmp = this.getAmplifire();
                int duration = this.getDuration();

                if (e.hasEffect(EffectsRegistry.POISON.get())) {
                    int appliedAmplifier = e.getEffect(EffectsRegistry.POISON.get()).getAmplifier() + 1;

                    if (appliedAmplifier <= maxAmp) {
                        e.addEffect(new MobEffectInstance(EffectsRegistry.POISON.get(), duration + appliedAmplifier * 20, appliedAmplifier, false, true, false));

                        if (this.random.nextFloat() < 0.25F)
                            LevelingUtils.addExperience(this.getSword(), 1);
                    } else {
                        e.addEffect(new MobEffectInstance(EffectsRegistry.POISON.get(), duration + maxAmp * 20, maxAmp, false, true, false));
                    }
                } else {
                    e.addEffect(new MobEffectInstance(EffectsRegistry.POISON.get(), duration, 0, false, true, false));

                    if (this.random.nextFloat() < 0.25F)
                        LevelingUtils.addExperience(this.getSword(), 1);
                }
            }

            this.cooldown = 20;
        }

        if (this.cooldown > 0) {
            --this.cooldown;
        }

    }

    public boolean canCollideWith(Entity p_20303_) {
        return false;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public void setLifeTime(int lifetime) {
        this.getEntityData().set(LIFETIME, lifetime);
    }

    public int getLifeTime() {
        return (Integer)this.getEntityData().get(LIFETIME);
    }

    public void setAmplifire(int maxAmp) {
        this.getEntityData().set(APLIFIRE, maxAmp);
    }

    public int getAmplifire() {
        return (Integer)this.getEntityData().get(APLIFIRE);
    }

    public void setDuration(int duration) {
        this.getEntityData().set(DURATION, duration);
    }

    public int getDuration() {
        return (Integer)this.getEntityData().get(DURATION);
    }

    public float getRadius() {
        return (Float)this.getEntityData().get(RADIUS);
    }

    public void setRadius(float radius) {
        this.getEntityData().set(RADIUS, radius);
    }

    public void setSword(ItemStack swort) {
        this.sword = swort;
    }

    public ItemStack getSword() {
        return this.sword;
    }

    public boolean isAlwaysTicking() {
        return true;
    }

    protected void defineSynchedData() {
        this.entityData.define(RADIUS, 5.0F);
        this.entityData.define(LIFETIME, 20);
        this.entityData.define(APLIFIRE, 0);
        this.entityData.define(DURATION, 2);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setRadius(compound.getFloat("radius"));
        this.setLifeTime(compound.getInt("lifetime"));
        this.setAmplifire(compound.getInt("maxamp"));
        this.setDuration(compound.getInt("duration"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("radius", this.getRadius());
        compound.putInt("lifetime", this.getLifeTime());
        compound.putInt("maxamp", this.getAmplifire());
        compound.putInt("duration", this.getDuration());
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
