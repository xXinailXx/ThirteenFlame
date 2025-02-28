package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.entities.BlockSimulationEntity;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ShockwaveEntity extends ThrowableProjectile {
    private int radius;
    private float damage;
    private int step;
    private List<BlockPos> poses = new ArrayList<>();

    public ShockwaveEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ShockwaveEntity(Level level, int radius, float damage) {
        super(EntityRegistry.SHOCKWAVE.get(), level);

        this.radius = radius;
        this.damage = damage;
    }

    @Override
    public void tick() {
        super.tick();

        BlockPos center = this.blockPosition();
        Level level = this.getLevel();

        if (poses.isEmpty()) {
            for (int i = -(int) radius; i <= radius; i++) {
                float r1 = Mth.sqrt((float) (radius * radius - i * i));

                for (int j = -(int) r1; j <= r1; j++)
                    poses.add(center.offset(i, 0, j));
            }
        }

        if (!poses.isEmpty()) {
            Entity owner = getOwner();
            Vec3 centerVec = new Vec3(center.getX(), center.getY(), center.getZ());
            List<BlockPos> closest = poses.stream().filter(p -> new Vec3(p.getX(), p.getY(), p.getZ()).distanceTo(centerVec) <= step).toList();
            poses.removeAll(closest);

            for (BlockPos p : closest) {
                if (!level.isClientSide()) {
                    float damage = radius * this.damage / step;

                    for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, new AABB(p, p.above(3)).inflate(0.5F))) {
                        if (owner != null && entity.getStringUUID().equals(owner.getStringUUID())) {
                            continue;
                        }

                        if (this.getOwner() instanceof Player player) {
                            entity.hurt(DamageSource.playerAttack(player), damage);
                        } else {
                            entity.hurt(DamageSource.MAGIC, damage);
                        }

                        entity.setDeltaMovement(entity.position().add(0, 1, 0).subtract(centerVec).normalize().multiply(2, 1, 2));
                    }
                }

                int extraY = 0;
                BlockSimulationEntity entity = new BlockSimulationEntity(level, level.getBlockState(p));

                if (!level.getBlockState(p).isAir() && level.getBlockState(p.above()).isAir()) {
                    BlockState state = level.getBlockState(p);

                    if (!state.getMaterial().blocksMotion() || level.getBlockState(p.above()).getMaterial().blocksMotion()) {
                        continue;
                    }

                    entity.setPos(p.getX() + 0.5F, p.getY() + 0.5F, p.getZ() + 0.5F);
                } else if (level.getBlockState(p).isAir() && level.getBlockState(p.above()).isAir()){
                    for (int i = 0; i < radius; i++) {
                        BlockPos pos = new BlockPos(p.getX(), p.getY() - extraY, p.getZ());
                        BlockState state = level.getBlockState(pos);

                        if (!state.isAir() && level.getBlockState(pos.above()).isAir()) {
                            if (!state.getMaterial().blocksMotion() || level.getBlockState(p.above()).getMaterial().blocksMotion()) {
                                break;
                            }

                            entity = new BlockSimulationEntity(level, state);
                            entity.setPos(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
                            break;
                        } else {
                            extraY++;
                        }
                    }
                } else {
                    for (int i = 0; i < radius; i++) {
                        BlockPos pos = new BlockPos(p.getX(), p.getY() + extraY, p.getZ());
                        BlockState state = level.getBlockState(pos);

                        if (!state.isAir() && level.getBlockState(pos.above()).isAir()) {
                            if (!state.getMaterial().blocksMotion() || level.getBlockState(pos.above()).getMaterial().blocksMotion()) {
                                break;
                            }

                            entity = new BlockSimulationEntity(level, state);
                            entity.setPos(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5);

                            break;
                        }

                        extraY++;
                    }
                }

                entity.setDeltaMovement(0, step * 0.02F, 0);
                level.addFreshEntity(entity);

                List<BlockState> oreBlocks = new ArrayList<>();
                for (int i = 1; i < radius; i++) {
                    BlockPos pos = new BlockPos(p.getX(), p.getY() + extraY - i, p.getZ());
                    BlockState state = level.getBlockState(pos);

                    if (state.getBlock() instanceof DropExperienceBlock) {
                        oreBlocks.add(state);
                    }
                }

                final int orecCount = oreBlocks.size();
                for (int i = 0; i < oreBlocks.size(); i++) {
                    BlockState state = oreBlocks.get(i);

                    OreBlockSimulationEntity simulationEntity = new OreBlockSimulationEntity(level, state);
                    simulationEntity.setPos(p.getX() + 0.5F, p.getY() - i + 0.01F, p.getZ() + 0.5F);
                    level.addFreshEntity(simulationEntity);
                }
            }

            step++;

            if (poses.isEmpty() || step >= poses.size())
                this.remove(RemovalReason.KILLED);
        } else
            this.remove(RemovalReason.KILLED);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.radius = compound.getInt("Radius");
        this.damage = compound.getFloat("Damage");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Radius", this.radius);
        compound.putFloat("Damage", this.damage);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
