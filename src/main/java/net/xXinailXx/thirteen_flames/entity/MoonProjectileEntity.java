package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.client.particles.circle.CircleTintData;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;
import net.xXinailXx.thirteen_flames.init.SoundRegistry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoonProjectileEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> POWER_ENCH = SynchedEntityData.defineId(MoonProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BASE_DAMAGE = SynchedEntityData.defineId(MoonProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARTICLE_COUNT = SynchedEntityData.defineId(MoonProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FREE = SynchedEntityData.defineId(MoonProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    private final Random random1 = new Random();
    public Vec3 prevPos;
    public LivingEntity target;
    public Color color;
    @Getter
    @Setter
    private ItemStack bow;

    public MoonProjectileEntity(EntityType<? extends MoonProjectileEntity> type, Level world) {
        super(type, world);
        this.bow = ItemStack.EMPTY;
        this.color = new Color(0, 246 - this.random.nextInt(100), 255 - this.random.nextInt(120));
    }

    public void tick() {
        if (this.getOwner() == null)
            this.discard();

        Vec3 motion = this.getDeltaMovement();
        super.tick();
        this.setDeltaMovement(motion);

        if (!this.level.isClientSide)
            ParticleActions.spawnParticleLine(this.level, new CircleTintData(this.color, 0.1F, 35, 0.89F, false), this.prevPos == null ? this.position() : this.prevPos, this.position(), this.getParticleCount(), 0);

        if (this.isFree()) {
            if (this.target != null && this.target.hasLineOfSight(this) && this.target.isAlive()) {
                this.setDeltaMovement(this.getDeltaMovement().add(this.target.getBoundingBox().getCenter().subtract(this.position()).normalize().scale(0.1F)));
            } else if (this.target == null || !this.target.isAlive()) {
                java.util.List<LivingEntity> targets = new ArrayList(this.getLevel().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(7), (entity) -> !entity.equals(this.getOwner()) && !(entity instanceof LocalPlayer) && entity.hasLineOfSight(this)));

                if (!targets.isEmpty())
                    this.target = targets.get(this.random1.nextInt(targets.size()));
            }
        }

        this.prevPos = this.position();
    }

    protected void onHitEntity(EntityHitResult pResult) {
        if (pResult.getEntity() != this.getOwner()) {
            if (!this.bow.isEmpty())
                LevelingUtils.addExperience(this.bow, 1);

            if (!this.level.isClientSide()) {
                ((ServerLevel)this.level).sendParticles(new CircleTintData(new Color(0, 196, 255), 0.2F, 10, 0.55F, false), this.getX(), this.getY(), this.getZ(), 10, 0, 0, 0, 0.1);
                ((ServerLevel)this.level).sendParticles(new CircleTintData(new Color(0, 60, 255), 0.2F, 10, 0.55F, false), this.getX(), this.getY(), this.getZ(), 10, 0, 0, 0, 0.1);

                pResult.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), this.getBaseDamage() + this.getPowerEnch() / 2.0F);
                pResult.getEntity().invulnerableTime = 0;
                float vol = (float) (10 / this.getOwner().distanceToSqr(this.position()));
                this.getLevel().playSound(null, this.getOwner() == null ? this : this.getOwner(), SoundRegistry.MOON_BOW_SPLASH.get(), SoundSource.PLAYERS, this.random.nextFloat() * 0.05F * vol + vol, this.random.nextFloat() * 0.1F + 0.6F);
                this.discard();
            }
        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
        HitResult result = this.level.clip(new ClipContext(this.position(), this.position().add(this.getDeltaMovement().normalize()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        if (!this.level.isClientSide()) {
            ParticleActions.spawnParticleLine(this.level, new CircleTintData(this.color, 0.1F, 80, 0.9F, false), this.position(), result.getLocation(), (int)Math.round(Math.sqrt(this.position().distanceToSqr(result.getLocation())) * 10), 0);
            ((ServerLevel)this.level).sendParticles(new CircleTintData(new Color(0, 196, 255), 0.2F, 10, 0.55F, false), result.getLocation().x(), result.getLocation().y(), result.getLocation().z(), 10, 0, 0, 0, 0.1);
            ((ServerLevel)this.level).sendParticles(new CircleTintData(new Color(0, 60, 255), 0.2F, 10, 0.55F, false), result.getLocation().x(), result.getLocation().y(), result.getLocation().z(), 10, 0, 0, 0, 0.1);
        }

        float vol = this.getOwner() == null ? 10.0F : (float)(10 / this.getOwner().distanceToSqr(this.position()));
        this.getLevel().playSound(null, this.getOwner() == null ? this : this.getOwner(), SoundRegistry.MOON_BOW_SPLASH.get(), SoundSource.PLAYERS, this.random.nextFloat() * 0.05F * vol + vol, this.random.nextFloat() * 0.1F + 0.6F);
        this.discard();
    }

    public void checkDespawn() {
        if (this.tickCount > 240) {
            this.discard();
        }
    }

    public static java.util.List<MoonProjectileEntity> makeList(int count, Level level, Entity owner, Vec3 center, Vec3 move, int powerEnch, ItemStack bow) {
        List<MoonProjectileEntity> list = new ArrayList();

        for(int i = 0; i < count; ++i) {
            MoonProjectileEntity proj = new MoonProjectileEntity(EntityRegistry.MOON_PROJECTILE.get(), level);
            proj.setOwner(owner);
            proj.setPos(center);
            proj.setDeltaMovement(move);
            proj.color = new Color(0, 246 - proj.random.nextInt(160), 255 - proj.random.nextInt(120));
            proj.setPowerEnch(powerEnch);
            proj.setBow(bow);
            proj.setParticleCount(i % 2 == 0 && count > 7 ? (i % 4 == 0 && count > 15 ? 3 : 12) : (count <= 7 ? 12 : 6));
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

    public void setPowerEnch(int powerEnch) {
        this.getEntityData().set(POWER_ENCH, powerEnch);
    }

    public int getPowerEnch() {
        return this.getEntityData().get(POWER_ENCH);
    }

    public void setBaseDamage(int baseDmg) {
        this.getEntityData().set(BASE_DAMAGE, baseDmg);
    }

    public int getBaseDamage() {
        return this.getEntityData().get(BASE_DAMAGE);
    }

    public void setParticleCount(int particles) {
        this.getEntityData().set(PARTICLE_COUNT, particles);
    }

    public int getParticleCount() {
        return this.getEntityData().get(PARTICLE_COUNT);
    }

    public boolean isFree() {
        return this.getEntityData().get(FREE);
    }

    public void setFree(boolean free) {
        this.getEntityData().set(FREE, free);
    }

    protected void defineSynchedData() {
        this.entityData.define(POWER_ENCH, 0);
        this.entityData.define(BASE_DAMAGE, 2);
        this.entityData.define(PARTICLE_COUNT, 12);
        this.entityData.define(FREE, false);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setPowerEnch(compound.getInt("power_ench"));
        this.setBaseDamage(compound.getInt("base_damge"));
        this.setParticleCount(compound.getInt("particles"));
        this.setFree(compound.getBoolean("free"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("power_ench", this.getPowerEnch());
        compound.putInt("base_damage", this.getBaseDamage());
        compound.putInt("particles", this.getParticleCount());
        compound.putBoolean("free", this.isFree());
    }

    public boolean shouldRender(double p_20296_, double p_20297_, double p_20298_) {
        return false;
    }
}
