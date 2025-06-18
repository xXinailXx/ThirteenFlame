package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.client.particles.circle.CircleTintData;
import it.hurts.sskirillss.relics.client.particles.spark.SparkTintData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;
import net.xXinailXx.thirteen_flames.init.EntitiesRegistry;
import net.xXinailXx.thirteen_flames.init.SoundsRegistry;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MoonStormEntity extends Projectile {
    Color[] colors = new Color[]{new Color(0, 7, 9), new Color(0, 0, 16), new Color(0, 7, 14), new Color(14, 3, 14)};
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(MoonStormEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(MoonStormEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(MoonStormEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEAL = SynchedEntityData.defineId(MoonStormEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> FREQ = SynchedEntityData.defineId(MoonStormEntity.class, EntityDataSerializers.INT);
    private ItemStack bow;
    private double r;
    double a;
    double b;
    float radius;
    LinkedList<MoonStormEntity.DelayedRunnable> taskQueue;

    public MoonStormEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.bow = ItemStack.EMPTY;
        this.r = 1;
        this.a = 30;
        this.b = 0;
        this.taskQueue = new LinkedList();
    }

    public boolean isAlwaysTicking() {
        return true;
    }

    public void tick() {
        super.tick();

        if (!this.level.isClientSide() && !this.taskQueue.isEmpty() && ((MoonStormEntity.DelayedRunnable)this.taskQueue.getFirst()).startedAt + ((MoonStormEntity.DelayedRunnable)this.taskQueue.getFirst()).delay <= this.tickCount)
            ((MoonStormEntity.DelayedRunnable)this.taskQueue.pop()).runnable.run();

        this.radius = this.getRadius();
        int lifetime = this.getLifeTime();
        int freq = this.getFreq();

        if (this.tickCount > lifetime)
            this.discard();

        for(int i = 0; i < this.r; ++i) {
            Vec3 direction = new Vec3(1, 0, 0);
            direction = direction.yRot((float)Math.toRadians(this.random.nextFloat() * 360)).scale(MathUtils.randomFloat(this.random));
            double x = MathUtils.randomFloat(this.random) * this.r;
            double z = MathUtils.randomFloat(this.random) * Math.sqrt(this.r * this.r - x * x);
            this.getLevel().addParticle(new CircleTintData(this.colors[this.random.nextInt(this.colors.length)], (float)(1 + this.r / 2), 80, 0.96F, false), true, this.getX() + x, this.getY() + (double)MathUtils.randomFloat(this.random) * this.r / 10, this.getZ() + z, direction.x * 0.46, direction.y * 0.1, direction.z * 0.46);
        }

        this.drawFrame();

        if (this.radius >= 10)
            this.drawInnerCircles();

        if (this.radius >= 13)
            this.drawLine();

        if (this.radius >= 16)
            this.drawDiamond();

        if (this.radius >= 19)
            this.drawCross();

        if (this.r < (double)this.radius * 1.2)
            this.r += ((double)this.radius * 1.2 - 1) / 120;

        if (this.tickCount > 130) {
            AABB box = this.getBoundingBox().inflate(this.radius).inflate(0, 50, 0).move(0, -50 ,0);
            List<LivingEntity> targets = new ArrayList(this.getLevel().getEntitiesOfClass(LivingEntity.class, box, (entity) -> !entity.equals(this.getOwner())));

            if (this.tickCount % freq == 0) {
                MoonRaindropEntity drop = new MoonRaindropEntity(EntitiesRegistry.MOON_RAINDROP.get(), this.getLevel());
                Vec3 pos = this.getPosition(1).add(MathUtils.randomFloat(this.random) * this.radius, -1, MathUtils.randomFloat(this.random) * this.radius);

                if (this.random.nextFloat() < 0.2 && !targets.isEmpty()) {
                    LivingEntity target = (LivingEntity)targets.get(this.random.nextInt(targets.size()));
                    pos = target.getPosition(1).add(0, this.getY() - target.getY() - 1, 0);
                }

                drop.setPos(pos);
                drop.setDeltaMovement(0, -3, 0);
                drop.setOwner(this.getOwner());
                drop.setBow(this.getBow());
                drop.setHeal(this.getHeal());
                drop.setDamage(this.getDamage());
                this.getLevel().addFreshEntity(drop);
                ParticleActions.spawnParticleEntity(new CircleTintData(new Color(0, 128, 255), 0.2F, 15, 0.83F, false), drop, 15, 0.1);
                HitResult result = this.level.clip(new ClipContext(pos, pos.add(0, -160.0F, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                Vec3 vec3;

                if (result.getType() == HitResult.Type.BLOCK)
                    vec3 = result.getLocation();
                else
                    vec3 = pos.add(0, -102, 0);

                float vol = (float)(20 / (this.getOwner() != null ? this.getOwner().distanceToSqr(pos.subtract(0, pos.y() - this.getOwner().getY(), 0)) : 20));
                this.getLevel().playSound(null, vec3.x, vec3.y, vec3.z, SoundsRegistry.MOON_BOW_RAIN.get(), SoundSource.PLAYERS, this.random.nextFloat() * 0.15F * vol + vol, this.random.nextFloat() * 0.6F + 0.7F);
            }

            if (this.tickCount % (35L + Math.round((freq * freq) * (freq / 1.9))) == 0L) {
                Vec3 pos = this.getPosition(1).add(MathUtils.randomFloat(this.random) * this.radius, -1, (MathUtils.randomFloat(this.random) * this.radius));
                Vec3 finalEndpos;

                if (!targets.isEmpty()) {
                    LivingEntity target = (LivingEntity)targets.get(this.random.nextInt(targets.size()));
                    pos = target.getPosition(1).add(0, this.getY() - target.getY(), 0);
                    finalEndpos = target.getPosition(1);
                } else {
                    HitResult result = this.level.clip(new ClipContext(pos, pos.add(0, -160, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

                    if (result.getType() == HitResult.Type.BLOCK)
                        finalEndpos = result.getLocation();
                    else
                        finalEndpos = pos.add(0, -102, 0);
                }

                if (!this.level.isClientSide()) {
                    Vec3 finalPos = pos;
                    this.taskQueue.add(new MoonStormEntity.DelayedRunnable(() -> this.drawThinLightning(this.getLevel(), finalPos, finalEndpos, 8, 0.45, 0.55F, new Color(187, 145, 255), 14), this.tickCount, 1));
                    this.taskQueue.add(new MoonStormEntity.DelayedRunnable(() -> this.drawThinLightning(this.getLevel(), finalPos, finalEndpos, 20, 0.55, 0.4F, new Color(127, 117, 255), 13), this.tickCount, 2));
                    this.taskQueue.add(new MoonStormEntity.DelayedRunnable(() -> this.drawThinLightning(this.getLevel(), finalPos, finalEndpos, 20, 0.55, 0.35F, new Color(154, 96, 255), 13), this.tickCount, 3));
                    this.taskQueue.add(new MoonStormEntity.DelayedRunnable(() -> this.drawThinLightning(this.getLevel(), finalPos, finalEndpos, 16, 0.55, 0.3F, new Color(128, 86, 255), 14), this.tickCount, 4));
                    float vol = (float)(20 / (this.getOwner() != null ? this.getOwner().distanceToSqr(finalEndpos) : 20));

                    if (this.getOwner() == null) {
                        this.getLevel().playSound((Player)null, finalEndpos.x, finalEndpos.y, finalEndpos.z, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, vol, this.random.nextFloat() * 0.2F + 0.3F);
                        this.getLevel().playSound((Player)null, finalEndpos.x, finalEndpos.y, finalEndpos.z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, vol, this.random.nextFloat() * 0.3F + 1.5F);
                    } else {
                        this.getLevel().playSound((Player)null, this.getOwner(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, vol, this.random.nextFloat() * 0.2F + 0.3F);
                        this.getLevel().playSound((Player)null, this.getOwner(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, vol, this.random.nextFloat() * 0.3F + 1.5F);
                    }

                    for(LivingEntity e : this.getLevel().getEntitiesOfClass(LivingEntity.class, (new AABB(finalEndpos, finalEndpos)).inflate(2.2, (double)4.0F, 2.2), (entity) -> !entity.equals(this.getOwner()) && !(entity instanceof LocalPlayer)))
                        e.hurt(DamageSource.thrown(this, this.getOwner()), this.getDamage() * 5.0F);
                }
            }
        }

    }

    public void drawThinLightning(Level level, Vec3 start, Vec3 end, int segments, double jag, float d, Color color, int particleCount) {
        Vec3 straightPos = start;
        Vec3 prevPos = start;
        ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(230, 175, 255), 0.6F, 15, 0.68F, false), new AABB(start, start), 10, 0.2);
        ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(230, 175, 255), 0.3F, 30, 0.82F, false), new AABB(start, start), 10, 0.15);
        double length = end.subtract(start).scale(1 / (double)segments).y();

        for(int i = 0; i < segments; ++i) {
            straightPos = straightPos.add(end.subtract(start).scale(1 / (double)segments));
            Vec3 pos = straightPos.add(new Vec3(MathUtils.randomFloat(this.random) * jag, 0, MathUtils.randomFloat(this.random) * jag));

            if (i == segments - 1)
                pos = end;

            ParticleActions.spawnParticleLine(level, new CircleTintData(color, d, 30, 0.89F, false), prevPos, pos, (int)Math.round(-length * particleCount * (0.2 + i * i / (segments - 1) / (segments - 1)) * 0.8), 0);
            prevPos = pos;
        }

        ParticleActions.spawnParticleAABB(this.level, new SparkTintData(new Color(179, 190, 255), 0.4F, 50), new AABB(end, end), 10, 0.15);
        ParticleActions.spawnParticleAABB(this.level, new SparkTintData(new Color(242, 208, 255), 0.4F, 50), new AABB(end, end), 10, 0.15);
        ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(0, 89, 255), 0.4F, 30, 0.8F, false), new AABB(end, end), 8, 0.15);
        ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(221, 117, 255), 0.4F, 30, 0.8F, false), new AABB(end, end), 8, 0.15);
    }

    public void drawThinHorizontalLightning(Level level, Vec3 start, Vec3 end, int segments, double jag, float d, Color color, boolean doStartBurst) {
        Vec3 straightPos = start;
        Vec3 prevPos = start;

        if (doStartBurst) {
            ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(230, 175, 255), 0.6F, 15, 0.68F, false), new AABB(start, start), 10, 0.2);
            ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(230, 175, 255), 0.3F, 30, 0.82F, false), new AABB(start, start), 10, 0.15);
        }

        double length = end.subtract(start).scale((double) 1 / segments).length();

        for(int i = 0; i < segments; ++i) {
            straightPos = straightPos.add(end.subtract(start).scale((double) 1 / segments));
            Vec3 pos = straightPos.add(new Vec3(MathUtils.randomFloat(this.random) * jag, 0, MathUtils.randomFloat(this.random) * jag));

            if (i == segments - 1)
                pos = end;

            ParticleActions.spawnParticleLine(level, new CircleTintData(color, d, 50, 0.9F, false), prevPos, pos, (int)Math.round(length * (double)8.0F), 0);
            prevPos = pos;
        }

        ParticleActions.spawnParticleAABB(this.level, new SparkTintData(new Color(179, 190, 255), 0.4F, 50), new AABB(end, end), 10, 0.1);
        ParticleActions.spawnParticleAABB(this.level, new SparkTintData(new Color(242, 208, 255), 0.4F, 50), new AABB(end, end), 10, 0.1);
        ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(0, 89, 255), 0.4F, 30, 0.8F, false), new AABB(end, end), 10, 0.1);
        ParticleActions.spawnParticleAABB(this.level, new CircleTintData(new Color(221, 117, 255), 0.4F, 30, 0.8F, false), new AABB(end, end), 10, 0.1);
    }

    public void drawFrame() {
        this.a = (double)30.0F;

        for(int i = 0; i < 80; ++i) {
            if (i + 40 <= this.tickCount) {
                Vec3 pos = this.getPosition(1).subtract(new Vec3(0, 0, -2)).add((new Vec3((this.radius + 7), 0, 0)).yRot((float)Math.toRadians(this.a)));
                this.level.addParticle(new CircleTintData(new Color(0, 21, 255), (float)(this.radius / 20) * (1 - (float)((i - 40) * (i - 40)) / 1600) + 0.1F, 2, 0.99F, false), true, pos.x(), pos.y() - 2 - (this.radius / 6.0F), pos.z(), 0, 0, 0);
                pos = this.getPosition(1).subtract(new Vec3(0, 0, 2)).add((new Vec3((this.radius + 7), 0, 0)).yRot((float)Math.toRadians(-this.a)));
                this.level.addParticle(new CircleTintData(new Color(0, 21, 255), (float)(this.radius / 20) * (1 - (float)((i - 40) * (i - 40)) / 1600) + 0.1F, 2, 0.98F, false), true, pos.x(), pos.y() - 2 - (this.radius / 6.0F), pos.z(), 0, 0, 0);
                ++this.a;
            }
        }
    }

    public void drawLine() {
        this.a = 0;

        for(int i = 0; i < 90; ++i) {
            if (i + 120 <= this.tickCount * 2) {
                Vec3 pos = this.getPosition(1).add(new Vec3((-this.radius) * 1.6 + this.a, 0, 0));
                this.level.addParticle(new CircleTintData(new Color(0, 81, 255), (float)(this.radius / 20) * (1 - (float) ((i - 45) * (i - 45)) / 2025) + 0.1F, 2, 0.99F, false), true, pos.x(), pos.y() - 1 - (this.radius / 6.0F), pos.z(), 0, 0, 0);
                this.a += this.radius * 1.6 / 45;
            }
        }
    }

    public void drawInnerCircles() {
        this.a = 0;

        for(int i = 0; i < 40; ++i) {
            if (i + 70 <= this.tickCount) {
                Vec3 pos = this.getPosition(1).add(new Vec3(0.2, 0, 0)).add((new Vec3(this.radius / 2, 0, 0)).yRot((float)Math.toRadians(this.a)));
                this.level.addParticle(new CircleTintData(new Color(0, 172, 201), (float)((this.radius / 20) * ((float) ((i - 80) * (i - 80) - 1) / 6400) + 0.05F), 2, 0.99F, false), true, pos.x(), pos.y() - 1 - (this.radius / 6.0F), pos.z(), 0, 0, 0);
                pos = this.getPosition(1).add(new Vec3(0.2, 0, 0)).add((new Vec3(this.radius / 2, 0, 0)).yRot((float)Math.toRadians(-this.a)));
                this.level.addParticle(new CircleTintData(new Color(0, 172, 201), (float)(this.radius / (double)20) * ((float) ((i - 80) * (i - 80) - 1) / 6400) + 0.05F, 2, 0.98F, false), true, pos.x(), pos.y() - 1 - (this.radius / 6.0F), pos.z(), 0, 0, 0);
                this.a += (double)2;
            }
        }

        this.b = 0;

        for(int i = 0; i < 45; ++i) {
            if (i + 70 <= this.tickCount) {
                Vec3 pos = this.getPosition(1).add((new Vec3((-this.radius) / 1.7, 0, 0)).yRot((float)Math.toRadians(this.b)));
                this.level.addParticle(new CircleTintData(new Color(0, 159, 185), (float)(this.radius / 20) * ((float)((i - 90) * (i - 90) - 1) / 8100) + 0.1F, 2, 0.99F, false), true, pos.x(), pos.y() - 1 - (this.radius / 6.0F), pos.z(), 0, 0, 0);

                if (i >= 43)
                    this.level.addParticle(new CircleTintData(new Color(0, 159, 185), (float)(this.radius / 20) * ((float)((i - 90) * (i - 90) - 1) / 8100) + 0.1F, 2, 0.99F, false), true, pos.x() + 0.05, pos.y() - 1 - (this.radius / 6.0F), pos.z() + 0.1, 0, 0, 0);

                if (i == 44)
                    this.level.addParticle(new CircleTintData(new Color(0, 159, 185), (float)(this.radius / 20) * ((float)((i - 90) * (i - 90) - 1) / 8100) + 0.1F, 2, 0.99F, false), true, pos.x(), pos.y() - 1 - (this.radius / 6.0F), pos.z() + 0.3, 0, 0, 0);

                pos = this.getPosition(1).add((new Vec3((-this.radius) / 1.7, 0, 0)).yRot((float)Math.toRadians(-this.b)));
                this.level.addParticle(new CircleTintData(new Color(0, 159, 185), (float)(this.radius / 20) * ((float)((i - 90) * (i - 90) - 1) / 8100) + 0.1F, 2, 0.98F, false), true, pos.x(), pos.y() - 1 - (this.radius / 6.0F), pos.z(), 0, 0, 0);

                if (i >= 43)
                    this.level.addParticle(new CircleTintData(new Color(0, 159, 185), (float)(this.radius / (20) * ((float)((i - 90) * (i - 90) - 1) / 8100) + 0.1F), 2, 0.99F, false), true, pos.x() + 0.05, pos.y() - 1 - (this.radius / 6.0F), pos.z() - 0.1, 0, 0, 0);

                if (i == 44)
                    this.level.addParticle(new CircleTintData(new Color(0, 159, 185), (float)(this.radius / 20) * ((float)((i - 90) * (i - 90) - 1) / 8100) + 0.1F, 2, 0.99F, false), true, pos.x(), pos.y() - 1 - (this.radius / 6.0F), pos.z() - 0.3, 0, 0, 0);

                this.b += 2;
            }
        }
    }

    public void drawDiamond() {
        this.a = 0;
        this.b = 0;

        for(int i = 0; i < 20; ++i) {
            if (i + 80 <= this.tickCount) {
                Vec3 pos = this.getPosition(1).add(new Vec3(this.b, 0, this.radius / 3 - this.a));
                this.level.addParticle(new CircleTintData(new Color(0, 81, 255), (float)(this.radius / 40) * (1 - (float)((i - 20) * (i - 20)) / 400) + 0.05F, 2, 0.99F, false), true, pos.x(), pos.y() - 1 - this.radius / 6.0F, pos.z(), 0, 0, 0);
                pos = this.getPosition(1).add(new Vec3(-this.b, 0, this.radius / 3 - this.a));
                this.level.addParticle(new CircleTintData(new Color(0, 81, 255), (float)(this.radius / 40) * (1 - (float)((i - 20) * (i - 20)) / 400) + 0.05F, 2, 0.98F, false), true, pos.x(), pos.y() - 1 - this.radius / 6.0F, pos.z(), 0, 0, 0);
                pos = this.getPosition(1).add(new Vec3(this.b, 0, (double)(-this.radius / 3) + this.a));
                this.level.addParticle(new CircleTintData(new Color(0, 81, 255), (float)(this.radius / 40) * (1 - (float)((i - 20) * (i - 20)) / 400) + 0.05F, 2, 0.98F, false), true, pos.x(), pos.y() - 1 - this.radius / 6.0F, pos.z(), 0, 0, 0);
                pos = this.getPosition(1).add(new Vec3(-this.b, 0, (double)(-this.radius / 3) + this.a));
                this.level.addParticle(new CircleTintData(new Color(0, 81, 255), (float)(this.radius / 40) * (1 - (float)((i - 20) * (i - 20)) / 400) + 0.05F, 2, 0.98F, false), true, pos.x(), pos.y() - 1 - this.radius / 6.0F, pos.z(), 0, 0, 0);
                this.a += this.radius / 60;
                this.b += this.radius / 120;
            }
        }

    }

    public void drawCross() {
        this.a = 0;
        this.b = 0;

        for(int i = 0; i < 20; ++i) {
            if (i + 1200 <= this.tickCount * 10) {
                Vec3 pos = this.getPosition(1).add(new Vec3(this.b, 0, -this.a));
                this.level.addParticle(new CircleTintData(new Color(222, 127, 255), (float)(this.radius / 40) * ((float)((i - 20) * (i - 20) - 1) / 400) + 0.05F, 2, 0.99F, false), true, pos.x(), pos.y() - 1.5F - this.radius / 6, pos.z(), 0, 0, 0);
                pos = this.getPosition(1).add(new Vec3(-this.b, 0, -this.a));
                this.level.addParticle(new CircleTintData(new Color(222, 127, 255), (float)(this.radius / 40) * ((float)((i - 20) * (i - 20) - 1) / 400) + 0.05F, 2, 0.98F, false), true, pos.x(), pos.y() - 1.5F - this.radius / 6, pos.z(), 0, 0, 0);
                pos = this.getPosition(1).add(new Vec3(this.b, 0, this.a));
                this.level.addParticle(new CircleTintData(new Color(222, 127, 255), (float)(this.radius / 40) * ((float)((i - 20) * (i - 20) - 1) / 400) + 0.05F, 2, 0.98F, false), true, pos.x(), pos.y() - 1.5F - this.radius / 6, pos.z(), 0, 0, 0);
                pos = this.getPosition(1).add(new Vec3(-this.b, 0, this.a));
                this.level.addParticle(new CircleTintData(new Color(222, 127, 255), (float)(this.radius / 40) * ((float)((i - 20) * (i - 20) - 1) / 400) + 0.05F, 2, 0.98F, false), true, pos.x(), pos.y() - 1.5F - this.radius / 6, pos.z(), 0, 0, 0);
                this.a += (double)(this.radius / 60);
                this.b += (double)(this.radius / 120);
            }
        }

    }

    public void setLifeTime(int lifetime) {
        this.getEntityData().set(LIFETIME, lifetime);
    }

    public int getLifeTime() {
        return this.getEntityData().get(LIFETIME);
    }

    public float getRadius() {
        return this.getEntityData().get(RADIUS);
    }

    public void setRadius(float radius) {
        this.getEntityData().set(RADIUS, radius);
    }

    public float getDamage() {
        return this.getEntityData().get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.getEntityData().set(DAMAGE, damage);
    }

    public float getHeal() {
        return this.getEntityData().get(HEAL);
    }

    public void setHeal(float heal) {
        this.getEntityData().set(HEAL, heal);
    }

    public void setFreq(int freq) {
        this.getEntityData().set(FREQ, freq);
    }

    public int getFreq() {
        return this.getEntityData().get(FREQ);
    }

    public void setBow(ItemStack bow) {
        this.bow = bow;
    }

    public ItemStack getBow() {
        return this.bow;
    }

    protected void defineSynchedData() {
        this.entityData.define(RADIUS, 5.0F);
        this.entityData.define(FREQ, 5);
        this.entityData.define(LIFETIME, 100);
        this.entityData.define(DAMAGE, 8.0F);
        this.entityData.define(HEAL, 1.0F);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setRadius(compound.getFloat("radius"));
        this.setDamage(compound.getFloat("damage"));
        this.setHeal(compound.getFloat("heal"));
        this.setFreq(compound.getInt("freq"));
        this.setLifeTime(compound.getInt("lifetime"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("radius", this.getRadius());
        compound.putFloat("damage", this.getDamage());
        compound.putFloat("heal", this.getHeal());
        compound.putInt("freq", this.getFreq());
        compound.putInt("lifetime", this.getLifeTime());
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private class DelayedRunnable {
        Runnable runnable;
        int startedAt;
        int delay;

        DelayedRunnable(Runnable runnable, int startedAt, int delay) {
            this.runnable = runnable;
            this.startedAt = startedAt;
            this.delay = delay;
        }
    }
}
