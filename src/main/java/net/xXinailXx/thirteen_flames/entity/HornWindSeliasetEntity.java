package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
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
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import net.xXinailXx.thirteen_flames.init.EntitiesRegistry;
import org.zeith.hammerlib.net.Network;

import java.awt.*;

public class HornWindSeliasetEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Float> EFFECTIVE = SynchedEntityData.defineId(HornWindSeliasetEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MAX_BLOCK_DISTANCE = SynchedEntityData.defineId(HornWindSeliasetEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> REVERCE = SynchedEntityData.defineId(HornWindSeliasetEntity.class, EntityDataSerializers.BOOLEAN);
    private Vec3 startPos;
    private ItemStack stack;

    public HornWindSeliasetEntity(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    public HornWindSeliasetEntity(Level level, float effective, float maxBlockDistance, Vec3 startPos, boolean reverce, ItemStack stack) {
        this(EntitiesRegistry.HORN_WIND_SELIASET.get(), level);

        setEffective(effective);
        setMaxBD(maxBlockDistance);
        setStartPos(startPos);
        setReverce(reverce);
        this.stack = stack;
    }

    public void tick() {
        if (this.stack == null || this.stack.isEmpty())
            discard();

        Vec3 motion = this.getDeltaMovement().scale(this.tickCount == 1 ? getEffective() : 1);
        super.tick();
        this.setDeltaMovement(motion);

        if (this.level.isClientSide)
            return;

        if (this.position().distanceTo(this.startPos) >= getMaxBD())
            discard();

        ColoredParticle.Options particle = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                .color(new Color(255, 255, 255, 51).getRGB())
                .diameter(0.1F)
                .lifetime(1)
                .scaleModifier(1F)
                .physical(false)
                .build());

        for (int i = 0; i < 90; i++) {
            double a = 5 * i - this.tickCount * 10;
            double radius = 1 + Math.sin(Math.toRadians(this.tickCount * 20) - 90) * 0.02;

            Vec3 x = motion.normalize().cross(new Vec3(1, 0, 0)).normalize().scale(radius);
            Vec3 z = motion.normalize().cross(x).normalize().scale(radius);
            Vec3 pos = this.getPosition(1F).add(x.scale(Math.cos(Math.toRadians(a)))).add(z.scale(Math.sin(Math.toRadians(a))));

            Network.sendToAll(new SpawnParticlePacket(particle, pos.x, pos.y, pos.z, 0, 0, 0));
        }
    }

    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        if (!(entity instanceof LivingEntity) || entity.is(getOwner()))
            return;

        Vec3 delta = this.getDeltaMovement();

        if (isReverce())
            ((LivingEntity) entity).knockback(0.4, delta.x, delta.z);
        else
            ((LivingEntity) entity).knockback(0.4, -delta.x, -delta.z);

        LevelingUtils.addExperience(this.stack, 2);
    }

    public Vec3 getStartPos() {
        return startPos;
    }

    public void setStartPos(Vec3 startPos) {
        this.startPos = startPos;
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
        setStartPos(new Vec3(tag.getDouble("s_x"), tag.getDouble("s_y"), tag.getDouble("s_z")));
        setReverce(tag.getBoolean("reverce"));
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("effective", getEffective());
        tag.putFloat("max_bd", getMaxBD());
        tag.putDouble("s_x", getStartPos().x);
        tag.putDouble("s_y", getStartPos().y);
        tag.putDouble("s_z", getStartPos().z);
        tag.putBoolean("reverce", isReverce());
    }

    protected void defineSynchedData() {
        this.entityData.define(EFFECTIVE, 1F);
        this.entityData.define(MAX_BLOCK_DISTANCE, 8F);
        this.entityData.define(REVERCE, false);
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
