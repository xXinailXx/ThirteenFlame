package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticleRendererTypes;
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;
import org.zeith.hammerlib.net.Network;

import java.awt.*;

public class HornWindSeliasetEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Float> EFFECTIVE = SynchedEntityData.defineId(HornWindSeliasetEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MAX_BLOCK_DISTANCE = SynchedEntityData.defineId(HornWindSeliasetEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> REVERCE = SynchedEntityData.defineId(HornWindSeliasetEntity.class, EntityDataSerializers.BOOLEAN);
    private ItemStack stack;

    public HornWindSeliasetEntity(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    public HornWindSeliasetEntity(Level level, float effective, float maxBlockDistance, boolean reverce, ItemStack stack) {
        this(EntityRegistry.HORN_WIND_SELIASET.get(), level);

        setEffective(effective);
        setMaxBD(maxBlockDistance);
        setReverce(reverce);

        this.stack = stack;
    }

    public void tick() {
        if (this.stack == null || this.stack.isEmpty() || this.getOwner() == null)
            discard();

        Vec3 motion = this.getDeltaMovement().scale(this.tickCount == 1 ? getEffective() : 1);
        super.tick();
        this.setDeltaMovement(motion);

        if (this.level.isClientSide)
            return;

        if (this.position().distanceTo(this.getOwner().getEyePosition().add(this.getOwner().getLookAngle().scale(1.5))) > getMaxBD())
            discard();

        ColoredParticle.Options particle = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                .color(new Color(255, 255, 255, 75).getRGB())
                .renderType(ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                .diameter(0.1F)
                .lifetime(1)
                .scaleModifier(1F)
                .physical(false)
                .build());

        for (int i = 0; i < 90; i++) {
            double a = 5 * i - this.tickCount * 10;
            double radius = 1 + Math.sin(Math.toRadians(this.tickCount * 20) - 90) * 0.02;

            Vec3 x = motion.normalize().x < 0.001 && motion.normalize().z < 0.001 ? motion.normalize().cross(new Vec3(1, 0, 0)).normalize().scale(radius) : motion.normalize().cross(new Vec3(0, 1, 0)).normalize().scale(radius);
            Vec3 z = motion.normalize().cross(x).normalize().scale(radius);
            Vec3 pos = this.getPosition(1).add(x.scale(Math.cos(Math.toRadians(a)))).add(z.scale(Math.sin(Math.toRadians(a))));

            Network.sendToAll(new SpawnParticlePacket(particle, pos.x, pos.y, pos.z, 0, 0, 0));
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        if (entity.getLevel().isClientSide)
            return;

        if (!(entity instanceof LivingEntity) || entity.is(getOwner()))
            return;

        Vec3 delta = this.getDeltaMovement();

        if (isReverce())
            ((LivingEntity) entity).knockback(0.4, delta.x, delta.z);
        else
            ((LivingEntity) entity).knockback(0.4, -delta.x, -delta.z);

        if (this.stack == null) {
            discard();

            return;
        }

        LevelingUtils.addExperience(this.stack, 2);
    }

    public boolean isReverce() {
        return this.entityData.get(REVERCE);
    }

    public void setReverce(boolean reverce) {
        this.entityData.set(REVERCE, reverce);
    }

    public float getEffective() {
        return this.entityData.get(EFFECTIVE);
    }

    public void setEffective(float effective) {
        this.entityData.set(EFFECTIVE, effective);
    }

    public float getMaxBD() {
        return this.entityData.get(MAX_BLOCK_DISTANCE);
    }

    public void setMaxBD(float maxBD) {
        this.entityData.set(MAX_BLOCK_DISTANCE, maxBD);
    }

    public boolean shouldRender(double x, double y, double z) {
        return false;
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setEffective(tag.getFloat("effective"));
        setMaxBD(tag.getFloat("max_bd"));
        setReverce(tag.getBoolean("reverce"));
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("effective", getEffective());
        tag.putFloat("max_bd", getMaxBD());
        tag.putBoolean("reverce", isReverce());
    }

    protected void defineSynchedData() {
        this.entityData.define(EFFECTIVE, 1F);
        this.entityData.define(MAX_BLOCK_DISTANCE, 8F);
        this.entityData.define(REVERCE, false);
    }
}
