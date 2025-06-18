package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.client.particles.circle.CircleTintData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;
import net.xXinailXx.thirteen_flames.init.EntitiesRegistry;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;
import net.xXinailXx.thirteen_flames.init.SoundsRegistry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoonStormcallerEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<ItemStack> BOW = SynchedEntityData.defineId(MoonStormcallerEntity.class, EntityDataSerializers.ITEM_STACK);
    public List<MoonProjectileSpecialEntity> rays = new ArrayList();
    public double rad = 0.05;
    public Vec3 prevPos1;
    public Vec3 prevPos2;
    public Vec3 prevPos3;
    public Vec3 prevPos4;
    public Vec3 pos1;
    public Vec3 pos2;
    public Vec3 pos3;
    public Vec3 pos4;
    public Vec3 shotPos;

    public MoonStormcallerEntity(EntityType<? extends MoonStormcallerEntity> type, Level world) {
        super(type, world);
    }

    public void tick() {
        Vec3 motion = this.getDeltaMovement();
        super.tick();
        this.setDeltaMovement(motion);

        if (this.shotPos == null)
            this.shotPos = this.getPosition(1);

        for(int i = 0; i < this.rays.size(); ++i) {
            double a = 360 / this.rays.size() * i - this.tickCount * 20;
            double radius = this.rad + Math.sin(Math.toRadians(this.tickCount * 20) - 90) * 0.04;

            if (i % 2 == 0)
                radius += 0.1;

            Vec3 x = motion.normalize().x < 0.001 && motion.normalize().z < 0.001 ? motion.normalize().cross(new Vec3(1, 0, 0).normalize().scale(radius)) : motion.normalize().cross(new Vec3(0, 1, 0)).normalize().scale(radius);
            Vec3 z = motion.normalize().cross(x).normalize().scale(radius);
            Vec3 pos = this.getPosition(1.0F).add(x.scale(Math.cos(Math.toRadians(a)))).add(z.scale(Math.sin(Math.toRadians(a))));

            if (i % 2 == 0) {
                pos = pos.add(motion.scale(-0.3));
            }

            ((MoonProjectileSpecialEntity)this.rays.get(i)).setPos(pos);
        }

        this.pos1 = this.position().add(new Vec3(MathUtils.randomFloat(this.random) * 0.15, MathUtils.randomFloat(this.random) * 0.15, MathUtils.randomFloat(this.random) * 0.15));
        this.pos2 = this.position().add(new Vec3(MathUtils.randomFloat(this.random) * 0.15, MathUtils.randomFloat(this.random) * 0.15, MathUtils.randomFloat(this.random) * 0.15));
        this.pos4 = this.position().add(new Vec3(MathUtils.randomFloat(this.random) * 0.15, MathUtils.randomFloat(this.random) * 0.15, MathUtils.randomFloat(this.random) * 0.15));
        this.pos3 = this.position();

        if (this.tickCount % 2 == 0 && !this.level.isClientSide) {
            ParticleActions.spawnParticleLine(this.level, new CircleTintData(new Color(0, 34, 255), 0.3F, 40, 0.91F, false), this.prevPos3 == null ? this.shotPos : this.prevPos3, this.pos3, 25, 0);
            this.prevPos1 = this.pos1;
            this.prevPos2 = this.pos2;
            this.prevPos3 = this.pos3;
            this.prevPos4 = this.pos4;
        }

        if (this.getY() > this.shotPos.y + 90) {
            if (!this.level.isClientSide()) {
                for(int i = 0; i < 120; ++i) {
                    Vec3 direction = new Vec3(1, 0, 0);
                    direction = direction.yRot((float)Math.toRadians(this.random.nextFloat() * 360)).scale(this.random.nextFloat() * 0.8F);
                    ParticleActions.spawnDirectedParticle(this.level, new CircleTintData(new Color(0, 15, 49), 4.2F, 80, 0.95F, false), this.getX(), this.getY(), this.getZ(), direction.x, MathUtils.randomFloat(this.random) * 0.1, direction.z);
                    direction = direction.yRot((float)Math.toRadians(this.random.nextFloat() * 360)).normalize().scale(this.random.nextFloat() * 0.8F);
                    ParticleActions.spawnDirectedParticle(this.level, new CircleTintData(new Color(28, 0, 27), 4.2F, 80, 0.95F, false), this.getX(), this.getY(), this.getZ(), direction.x, MathUtils.randomFloat(this.random) * 0.1, direction.z);
                }
            }

            if (this.getBow().is(ItemsRegistry.MOON_BOW.get())) {
                Entity owner = this.getOwner();

                if (owner instanceof Player) {
                    Player player = (Player)owner;
                    MoonStormEntity storm = new MoonStormEntity(EntitiesRegistry.MOON_STORM.get(), this.getLevel());
                    storm.setPos(this.getPosition(1.0F));
                    storm.setRadius((float) AbilityUtils.getAbilityValue(this.getBow(), "storm", "radius"));
                    storm.setLifeTime((int)(AbilityUtils.getAbilityValue(this.getBow(), "storm", "duration") * 20));
                    storm.setOwner(player);
                    storm.setFreq((int)Math.round(4 - (double)AbilityUtils.getAbilityPoints(this.getBow(), "storm") * 0.6));
                    storm.setBow(this.getBow());
                    storm.setDamage((float)AbilityUtils.getAbilityValue(this.getBow(), "storm", "damage") + (float)this.getBow().getEnchantmentLevel(Enchantments.POWER_ARROWS) / 2.5F);
                    storm.setHeal((float)AbilityUtils.getAbilityValue(this.getBow(), "storm", "heal") / 100.0F);
                    this.getLevel().addFreshEntity(storm);

                    if (!this.getLevel().isClientSide())
                        this.getLevel().playSound(null, this.getOwner(), AbilityUtils.getAbilityValue(this.getBow(), "storm", "radius") > 5 ? (SoundEvent) SoundsRegistry.MOON_BOW_STORM.get() : (SoundEvent) net.xXinailXx.thirteen_flames.init.SoundsRegistry.MOON_BOW_STORM_SHORT.get(), SoundSource.PLAYERS, this.random.nextFloat() * 0.6F + 1.0F, this.random.nextFloat() * 0.2F + 0.8F);
                }
            }

            this.discard();
        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
        MoonDischargeEntity discharge = new MoonDischargeEntity(EntitiesRegistry.MOON_DISCHARGE.get(), this.level);
        Vec3 pos = this.position();
        discharge.setPos(pos);
        discharge.setOwner(this.getOwner());
        discharge.shotPos = pos;
        discharge.setRadius((float)(AbilityUtils.getAbilityValue(this.getBow(), "storm", "radius") * (double)0.8F));
        discharge.setDamage((float)(AbilityUtils.getAbilityValue(this.getBow(), "storm", "damage") + (double)((float)this.getBow().getEnchantmentLevel(Enchantments.POWER_ARROWS) / 2.5F)) * 6.0F);
        discharge.shootFromRotation(this, 0.0F, -90.0F, 0.0F, 0.0F, 0.0F);
        this.level.addFreshEntity(discharge);
        this.discard();
    }

    public void onRemovedFromWorld() {
        for(Entity entity : this.rays)
            entity.discard();

        this.rays.clear();
        super.onRemovedFromWorld();
    }

    protected void onHitEntity(EntityHitResult result) {
        this.setPos(result.getEntity().getBoundingBox().getCenter());

        if (!this.level.isClientSide()) {
            MoonDischargeEntity discharge = new MoonDischargeEntity(EntitiesRegistry.MOON_DISCHARGE.get(), this.level);
            Vec3 pos = result.getEntity().getBoundingBox().getCenter();
            discharge.setPos(pos);
            discharge.setOwner(this.getOwner());
            discharge.shotPos = pos;
            discharge.setRadius((float)(AbilityUtils.getAbilityValue(this.getBow(), "storm", "radius") * 0.8F));
            discharge.setDamage((float)(AbilityUtils.getAbilityValue(this.getBow(), "storm", "damage") + (double)(this.getBow().getEnchantmentLevel(Enchantments.POWER_ARROWS) / 2.5F)) * 6.0F);
            discharge.shootFromRotation(this, 0.0F, -90.0F, 0.0F, 0.0F, 0.0F);
            this.level.addFreshEntity(discharge);
        }

        this.discard();
    }

    @SubscribeEvent
    public void onLevelUnload(PlayerEvent.PlayerLoggedOutEvent event) {
        this.discard();
    }

    public void checkDespawn() {
        if (this.tickCount > 160)
            this.discard();
    }

    public boolean canCollideWith(Entity pEntity) {
        return pEntity instanceof LivingEntity && pEntity.equals(this.getOwner());
    }

    public void setBow(ItemStack bow) {
        this.getEntityData().set(BOW, bow);
    }

    public ItemStack getBow() {
        return (ItemStack)this.getEntityData().get(BOW);
    }

    public MoonStormcallerEntity setRays(List<MoonProjectileSpecialEntity> rays) {
        this.rays = rays;
        return this;
    }

    protected void defineSynchedData() {
        this.entityData.define(BOW, ItemStack.EMPTY);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBow(ItemStack.of(compound.getCompound("bow")));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("bow", this.getBow().save(new CompoundTag()));
    }
}
