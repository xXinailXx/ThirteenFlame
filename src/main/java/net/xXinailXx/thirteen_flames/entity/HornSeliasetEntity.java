package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.init.EffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.enderdragonlib.client.glow.Beam;
import net.xXinailXx.enderdragonlib.client.glow.GlowData;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.interfaces.IGlow;
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import net.xXinailXx.enderdragonlib.utils.AABBUtils;
import net.xXinailXx.thirteen_flames.init.EntitiesRegistry;
import org.zeith.hammerlib.net.Network;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HornSeliasetEntity extends Projectile implements IAnimatable, IGlow {
    private static final EntityDataAccessor<Integer> WAVES_MAX = SynchedEntityData.defineId(HornSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WAVES = SynchedEntityData.defineId(HornSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COOLDOWN = SynchedEntityData.defineId(HornSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COOLDOWN_TIMER = SynchedEntityData.defineId(HornSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COOLDOWN_TIMER_WAVES = SynchedEntityData.defineId(HornSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> STUN = SynchedEntityData.defineId(HornSeliasetEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(HornSeliasetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ADD_EXP = SynchedEntityData.defineId(HornSeliasetEntity.class, EntityDataSerializers.INT);
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public HornSeliasetEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public HornSeliasetEntity(Level level, int waves, int cooldown, float stun) {
        this(EntitiesRegistry.HORN_SELIASET.get(), level);

        setWavesMax(waves);
        setCooldown(cooldown);
        setStun(stun);
    }

    public void tick() {
        super.tick();

        if (this.level.isClientSide)
            return;

        if (getPhase() == 1) {
            if (getWaves() >= getWavesMax()) {
                nextPhase();
                return;
            }

            if (getCooldownTimerWaves() == 0) {
                ColoredParticle.Options particle = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                        .color(new Color(255, 255, 255, 150).getRGB())
                        .diameter(0.5F)
                        .lifetime(40)
                        .physical(false)
                        .build());

                Vec3 center = this.position();

                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 360; j++)
                        Network.sendToAll(new SpawnParticlePacket(particle, center.x, center.y, center.z, Math.cos(j - i * 0.1) * 0.095, 0, Math.sin(j - i * 0.1) * 0.095));

                    for (int j = 0; j < 360; j++)
                        Network.sendToAll(new SpawnParticlePacket(ParticleTypes.CLOUD, center.x, center.y, center.z, Math.cos(j - i * 0.1) * 0.15, 0, Math.sin(j - i * 0.1) * 0.15));
                }

                List<BlockPos> poses = new ArrayList<>();
                BlockPos mainPos = this.blockPosition();

                for (int i = -8; i <= 8; i++) {
                    float r1 = Mth.sqrt((64 - i * i));

                    for (int j = -(int) r1; j <= r1; j++)
                        poses.add(mainPos.offset(i, 0, j));
                }

                Iterator<Entity> iterator = AABBUtils.getEntities(this, 8).stream().filter(entity -> !(entity.is(this.getOwner()))).iterator();
                List<Entity> entities = new ArrayList<>();

                for (Iterator<Entity> it = iterator; it.hasNext();) {
                    Entity entity = it.next();
                    entities.add(entity);
                }

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity) {
                        Vec3 delta = entity.position().subtract(this.position());

                        ((LivingEntity) entity).knockback(0.5, -delta.x, -delta.z);
                        ((LivingEntity) entity).addEffect(new MobEffectInstance(EffectRegistry.STUN.get(), (int) (getStun() * 20)));
                        setAddExp(getAddExp() + 2);
                    }
                }

                setWaves(getWaves() + 1);
                setCooldownTimerWaves(3);
            }

            if (this.tickCount % 20 == 0)
                setCooldownTimerWaves(getCooldownTimerWaves() - 1);
        }

        if (getCooldownTimer() == 0 && getPhase() == 2)
            nextPhase();
        else if (getCooldownTimer() > 0 && getPhase() == 2 && this.tickCount % 20 == 0)
            setCooldownTimer(getCooldownTimer() - 1);
    }

    public void nextPhase() {
        if (getPhase() == 0) {
            setPhase(1);
            setCooldownTimer(getCooldown());
        } else if (getPhase() == 1) {
            setPhase(2);
            setCooldownTimer(getCooldown());
            setCooldownTimerWaves(3);
        } else {
            setPhase(0);
            setCooldownTimer(getCooldown());
        }
    }

    public int getAddExp() {
        return this.getEntityData().get(ADD_EXP);
    }

    public void setAddExp(int exp) {
        this.getEntityData().set(ADD_EXP, exp);
    }

    public int getWaves() {
        return this.getEntityData().get(WAVES);
    }

    public void setWaves(int radius) {
        this.getEntityData().set(WAVES, radius);
    }

    public int getWavesMax() {
        return this.getEntityData().get(WAVES_MAX);
    }

    public void setWavesMax(int radius) {
        this.getEntityData().set(WAVES_MAX, radius);
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

    public int getCooldownTimerWaves() {
        return this.getEntityData().get(COOLDOWN_TIMER_WAVES);
    }

    public void setCooldownTimerWaves(int cooldown) {
        this.getEntityData().set(COOLDOWN_TIMER_WAVES, cooldown);
    }

    public float getStun() {
        return this.getEntityData().get(STUN);
    }

    public void setStun(float stun) {
        this.getEntityData().set(STUN, stun);
    }

    public int getPhase() {
        return this.getEntityData().get(PHASE);
    }

    public void setPhase(int phase) {
        this.getEntityData().set(PHASE, phase);
    }

    public GlowData constructGlowData() {
        GlowData data = GlowData.builder(false);

        if (getPhase() != 1)
            data.addBeam(new Beam(new Color(255, 82, 82), 16, 1, 0.75F));
        else
            data.addBeam(new Beam(new Color(114, 255, 82), 16, 1, 0.75F));

        return data;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));

        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<HornSeliasetEntity>(this, "idle_controller", 0, this::predicateIdle));
    }

    public AnimationFactory getFactory() {
        return factory;
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setWavesMax(compound.getInt("waves_max"));
        this.setWaves(compound.getInt("waves"));
        this.setCooldown(compound.getInt("cooldown"));
        this.setCooldownTimer(compound.getInt("cooldown_timer"));
        this.setCooldownTimerWaves(compound.getInt("cooldown_timer_waves"));
        this.setStun(compound.getFloat("stun"));
        this.setPhase(compound.getInt("phase"));
        this.setAddExp(compound.getInt("add_item_exp"));
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("waves_max", this.getWavesMax());
        compound.putInt("waves", this.getWaves());
        compound.putInt("cooldown", this.getCooldown());
        compound.putInt("cooldown_timer", this.getCooldownTimer());
        compound.putInt("cooldown_timer_waves", this.getCooldownTimerWaves());
        compound.putFloat("stun", this.getStun());
        compound.putInt("phase", this.getPhase());
        compound.putInt("add_item_exp", this.getAddExp());
    }

    protected void defineSynchedData() {
        this.entityData.define(WAVES_MAX, 1);
        this.entityData.define(WAVES, 0);
        this.entityData.define(COOLDOWN, 60);
        this.entityData.define(COOLDOWN_TIMER, 60);
        this.entityData.define(COOLDOWN_TIMER_WAVES, 3);
        this.entityData.define(STUN, 1F);
        this.entityData.define(PHASE, 0);
        this.entityData.define(ADD_EXP, 0);
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
