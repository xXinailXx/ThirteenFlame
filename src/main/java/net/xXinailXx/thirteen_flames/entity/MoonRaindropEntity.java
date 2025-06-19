package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.client.particles.circle.CircleTintData;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;
import net.xXinailXx.thirteen_flames.init.SoundRegistry;

import java.awt.*;

public class MoonRaindropEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> BASE_DAMAGE = SynchedEntityData.defineId(MoonRaindropEntity.class, EntityDataSerializers.INT);
    public Vec3 movement;
    public Vec3 prevPos;
    public Color color;
    private ItemStack bow;
    private float heal;
    private float damage;

    public MoonRaindropEntity(EntityType<? extends MoonRaindropEntity> type, Level world) {
        super(type, world);
        this.bow = ItemStack.EMPTY;
        this.heal = 0.0F;
        this.damage = 0.0F;
        this.color = new Color(0, 86 - this.random.nextInt(80), 255 - this.random.nextInt(90));
    }

    public void tick() {
        this.movement = this.getDeltaMovement();
        super.tick();
        this.setDeltaMovement(this.movement);

        if (this.level.isClientSide) {
            double distance = this.position().subtract(this.prevPos == null ? this.position() : this.prevPos).length();
            ParticleActions.spawnParticleLine(this.level, new CircleTintData(this.color, 0.1F, 80, 0.85F, false), this.prevPos == null ? this.position() : this.prevPos, this.position(), (int)Math.round(distance * this.tickCount * this.tickCount / 156 + 2), 0);
        }

        this.prevPos = this.position();
    }

    protected void onHitEntity(EntityHitResult pResult) {
        Entity var3 = pResult.getEntity();

        if (var3 instanceof LivingEntity living) {
            if (this.getOwner() != null && pResult.getEntity().equals(this.getOwner())) {
                living.heal(living.getMaxHealth() * this.getHeal());

                if (!this.getBow().isEmpty())
                    LevelingUtils.addExperience(this.getBow(), Math.round(Math.min(living.getMaxHealth() * this.getHeal(), living.getMaxHealth() - living.getHealth())));
            } else {
                pResult.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), this.getDamage());
                pResult.getEntity().invulnerableTime = 0;
            }

            this.getLevel().playSound((Player)null, pResult.getEntity(), (SoundEvent) SoundRegistry.MOON_BOW_SPLASH.get(), SoundSource.PLAYERS, this.random.nextFloat() * 0.2F + 0.1F, this.random.nextFloat() * 0.6F + 0.7F);
        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
        HitResult result = this.level.clip(new ClipContext(this.position(), this.position().add((double)0.0F, (double)-5.0F, (double)0.0F), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        Vec3 loc = pResult.getLocation();
        this.getLevel().playSound((Player)null, loc.x, loc.y, loc.z, (SoundEvent) SoundRegistry.MOON_BOW_SPLASH.get(), SoundSource.PLAYERS, this.random.nextFloat() * 0.2F + 0.1F, this.random.nextFloat() * 0.6F + 0.7F);

        if (result.getType() == HitResult.Type.BLOCK) {
            ParticleActions.spawnParticleLine(this.level, new CircleTintData(this.color, 0.1F, 80, 0.9F, false), this.position(), result.getLocation(), (int)Math.round(Math.sqrt(this.position().distanceToSqr(result.getLocation())) * this.tickCount * this.tickCount / 156 + 2), 0);
            ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(72, 0, 255), 0.2F, 20, 0.65F, false), new AABB(result.getLocation(), result.getLocation()), 15, 0.1);
            ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(0, 60, 255), 0.2F, 20, 0.65F, false), new AABB(result.getLocation(), result.getLocation()), 15, 0.1);
        }

        this.discard();
    }

    public void checkDespawn() {
        if (this.tickCount > 240)
            this.discard();
    }

    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    @SubscribeEvent
    public void onLevelUnload(PlayerEvent.PlayerLoggedOutEvent event) {
        this.discard();
    }

    public void setBow(ItemStack bow) {
        this.bow = bow;
    }

    public ItemStack getBow() {
        return this.bow;
    }

    public void setHeal(float heal) {
        this.heal = heal;
    }

    public float getHeal() {
        return this.heal;
    }

    public void setDamage(float dmg) {
        this.damage = dmg;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setBaseDamage(int baseDamage) {
        this.getEntityData().set(BASE_DAMAGE, baseDamage);
    }

    public int getBaseDamage() {
        return (Integer)this.getEntityData().get(BASE_DAMAGE);
    }

    protected void defineSynchedData() {
        this.entityData.define(BASE_DAMAGE, 2);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBaseDamage(compound.getInt("base_damage"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("base_damage", this.getBaseDamage());
    }
}
