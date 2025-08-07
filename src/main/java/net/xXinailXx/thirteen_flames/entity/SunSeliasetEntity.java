package net.xXinailXx.thirteen_flames.entity;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.enderdragonlib.client.glow.Beam;
import net.xXinailXx.enderdragonlib.client.glow.GlowData;
import net.xXinailXx.enderdragonlib.interfaces.IGlow;
import net.xXinailXx.enderdragonlib.utils.AABBUtils;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.awt.*;

public class SunSeliasetEntity extends Projectile implements IAnimatable, IGlow {
    private static final EntityDataAccessor<Integer> RADIUS = SynchedEntityData.defineId(SunSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COOLDOWN = SynchedEntityData.defineId(SunSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COOLDOWN_TIMER = SynchedEntityData.defineId(SunSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIM_TIME = SynchedEntityData.defineId(SunSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(SunSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ADD_EXP = SynchedEntityData.defineId(SunSeliasetEntity.class, EntityDataSerializers.INT);
    @Getter
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean isAnim = false;

    public SunSeliasetEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public SunSeliasetEntity(Level level, int radius, int cooldown) {
        this(EntityRegistry.SUN_SELIASET.get(), level);

        setRadius(radius);
        setCooldown(cooldown);
    }

    public void tick() {
        super.tick();

        if (this.level.isClientSide)
            return;

        if (getPhase() == 1)
            setAnim(true);

        if (getCooldownTimer() == 0 && getPhase() == 1) {
            for (BlockPos pos : AABBUtils.getBlockPoses(this, getRadius())) {
                BlockPos pos1 = new BlockPos(pos.getX(), pos.getY(), pos.getZ());

                BlockState state = this.level.getBlockState(pos1);

                if (state.getBlock() instanceof CropBlock block) {
                    if (block.isValidBonemealTarget(this.level, pos, state, this.level.isClientSide) && block.isBonemealSuccess(this.level, this.level.getRandom(), pos, state)) {
                        block.performBonemeal((ServerLevel) this.level, this.level.getRandom(), pos1, state);
                        setAddExp(getAddExp() + 2);
                    }
                }
            }

            for (Monster entity : AABBUtils.getEntities(Monster.class, this, getRadius())) {
                entity.setSecondsOnFire(8);

                setAddExp(getAddExp() + 2);
            }

            setCooldownTimer(getCooldown());
        }

        if (getAnimTime() == 0 && getPhase() == 2)
            setPhase(0);

        if (getPhase() > 0)
            if (getAnimTime() >= 0 && getAnimTime() < 30 && getPhase() == 1)
                setAnimTime(getAnimTime() + 1);
            else if (getAnimTime() > 0 && getAnimTime() <= 31 && getPhase() == 2)
                setAnimTime(getAnimTime() - 1);

        if (this.tickCount % 20 == 0)
            if (getPhase() == 1 && getCooldownTimer() > 0)
                setCooldownTimer(getCooldownTimer() - 1);
    }

    public void nextPhase() {
        if (getPhase() == 0) {
            setPhase(1);
            setCooldownTimer(getCooldown());
            setAnimTime(1);
            setAnim(true);
        } else if (getPhase() == 1) {
            setAnim(false);
            setAnimTime(31);
            setPhase(2);
        } else {
            setPhase(0);
            setAnimTime(0);
        }
    }

    public GlowData constructGlowData() {
        if (getPhase() == 0)
            return GlowData.builder().build();

        return GlowData.builder()
                .customRenderer()
                .addBeam(6, new Beam(new Color(255, 253, 165), 1, (stack, partialTicks, number) -> stack))
                .addBeam(6, new Beam(new Color(244, 243, 144), 1, (stack, partialTicks, number) -> stack))
                .build();
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if (getPhase() == 0)
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));

        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateAnim(AnimationEvent<E> event) {
        if (getPhase() == 1)
            event.getController().setAnimation(new AnimationBuilder().addAnimation("anim", true));
        else if (getPhase() == 2)
            event.getController().setAnimation(new AnimationBuilder().addAnimation("close", false));

        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "idle_controller", 30, this::predicateIdle));
        animationData.addAnimationController(new AnimationController<>(this, "move_controller", 30, this::predicateAnim));
    }

    public boolean isAnim() {
        return isAnim;
    }

    public void setAnim(boolean anim) {
        isAnim = anim;
    }

    public int getAddExp() {
        return this.getEntityData().get(ADD_EXP);
    }

    public void setAddExp(int exp) {
        this.getEntityData().set(ADD_EXP, exp);
    }

    public int getAnimTime() {
        return this.getEntityData().get(ANIM_TIME);
    }

    public void setAnimTime(int animTime) {
        this.getEntityData().set(ANIM_TIME, animTime);
    }

    public int getRadius() {
        return this.getEntityData().get(RADIUS);
    }

    public void setRadius(int radius) {
        this.getEntityData().set(RADIUS, radius);
    }

    public int getCooldown() {
        return this.getEntityData().get(COOLDOWN);
    }

    public void setCooldown(int cooldown) {
        if (cooldown < 0)
            cooldown = 0;
        else if (cooldown > 10)
            cooldown = 10;

        this.getEntityData().set(COOLDOWN, cooldown);
    }

    public int getCooldownTimer() {
        return this.getEntityData().get(COOLDOWN_TIMER);
    }

    public void setCooldownTimer(int cooldown) {
        this.getEntityData().set(COOLDOWN_TIMER, cooldown);
    }

    public int getPhase() {
        return this.getEntityData().get(PHASE);
    }

    public void setPhase(int phase) {
        this.getEntityData().set(PHASE, phase);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setRadius(compound.getInt("radius"));
        this.setCooldown(compound.getInt("cooldown"));
        this.setCooldownTimer(compound.getInt("cooldown_timer"));
        this.setPhase(compound.getInt("phase"));
        this.setAnimTime(compound.getInt("open_anim_time"));
        this.setAddExp(compound.getInt("add_item_exp"));
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("radius", this.getRadius());
        compound.putInt("cooldown", this.getCooldown());
        compound.putInt("cooldown_timer", this.getCooldownTimer());
        compound.putInt("phase", this.getPhase());
        compound.putInt("open_anim_time", this.getAnimTime());
        compound.putInt("add_item_exp", this.getAddExp());
    }

    protected void defineSynchedData() {
        this.entityData.define(RADIUS, 10);
        this.entityData.define(COOLDOWN, 10);
        this.entityData.define(COOLDOWN_TIMER, 10);
        this.entityData.define(PHASE, 0);
        this.entityData.define(ANIM_TIME, 0);
        this.entityData.define(ADD_EXP, 0);
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
