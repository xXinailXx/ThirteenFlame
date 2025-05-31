package net.xXinailXx.thirteen_flames.entity;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class MoonCarrierEntity extends ThrowableProjectile {
    public List<MoonProjectileEntity> rays = new ArrayList();
    public double rad = 0.06;

    public MoonCarrierEntity(EntityType<? extends MoonCarrierEntity> type, Level world) {
        super(type, world);
    }

    public void tick() {
        Vec3 motion = this.getDeltaMovement();
        super.tick();
        this.setDeltaMovement(motion);

        for(int i = 0; i < this.rays.size(); ++i) {
            double a = 360D / this.rays.size() * i - this.tickCount * 10;
            double radius = this.rad + Math.sin(Math.toRadians(this.tickCount * 20) - 90) * 0.04;

            if (i % 2 == 0 && this.rays.size() > 7) {
                radius += 0.1;

                if (i % 4 == 0 && this.rays.size() > 15)
                    radius -= 0.1;
            }

            Vec3 x = motion.normalize().x < 0.001 && motion.normalize().z < 0.001 ? motion.normalize().cross(new Vec3(1, 0, 0)).normalize().scale(radius) : motion.normalize().cross(new Vec3(0, 1, 0).normalize().scale(radius));
            Vec3 z = motion.normalize().cross(x).normalize().scale(radius);
            Vec3 pos = this.getPosition(1.0F).add(x.scale(Math.cos(Math.toRadians(a)))).add(z.scale(Math.sin(Math.toRadians(a))));

            if (i % 2 == 0) {
                pos = pos.add(motion.scale(0.3));

                if (i % 4 == 0 && this.rays.size() > 15)
                    pos = pos.subtract(motion.scale(0.3));
            }

            ((MoonProjectileEntity)this.rays.get(i)).setPos(pos);
        }

        if (!this.getLevel().isClientSide()) {
            AABB box = this.getBoundingBox().inflate(7);
            List<LivingEntity> targets = new ArrayList(this.getLevel().getEntitiesOfClass(LivingEntity.class, box, (entity) -> !entity.equals(this.getOwner()) && !(entity instanceof LocalPlayer) && entity.hasLineOfSight(this)));

            if (!targets.isEmpty()) {
                for(LivingEntity target : this.getLevel().getEntitiesOfClass(LivingEntity.class, box.move(motion.scale(20)).inflate(1), (entity) -> !entity.equals(this.getOwner()) && !(entity instanceof LocalPlayer) && entity.hasLineOfSight(this)))
                    if (!targets.contains(target))
                        targets.add(target);

                int cap = this.rays.size() / targets.size();

                if (cap == 0) {
                    for(int i = 0; i < this.rays.size(); ++i) {
                        ((MoonProjectileEntity)this.rays.get(i)).target = (LivingEntity)targets.get(i);
                        ((MoonProjectileEntity)this.rays.get(i)).setFree(true);
                    }

                    this.rays.clear();
                    this.discard();
                } else {
                    for(LivingEntity target : targets) {
                        for(int i = 0; i < cap; ++i) {
                            MoonProjectileEntity proj = (MoonProjectileEntity)this.rays.remove(0);
                            proj.target = target;
                            proj.setFree(true);
                        }
                    }

                    for(int i = 0; i < this.rays.size(); ++i) {
                        ((MoonProjectileEntity)this.rays.get(i)).target = (LivingEntity)targets.get(i);
                        ((MoonProjectileEntity)this.rays.get(i)).setFree(true);
                    }

                    this.rays.clear();
                    this.discard();
                }
            }
        }
    }

    @SubscribeEvent
    public void onLevelUnload(PlayerEvent.PlayerLoggedOutEvent event) {
        this.rays.clear();
        this.discard();
    }

    public void checkDespawn() {
        if (this.tickCount > 240 || this.rays.isEmpty()) {
            this.discard();
        }

    }

    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    public MoonCarrierEntity setRays(List<MoonProjectileEntity> rays) {
        this.rays = rays;
        return this;
    }

    protected void defineSynchedData() {}
}
