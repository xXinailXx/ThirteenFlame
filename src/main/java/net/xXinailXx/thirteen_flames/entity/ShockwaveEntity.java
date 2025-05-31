package net.xXinailXx.thirteen_flames.entity;

import it.hurts.sskirillss.relics.entities.BlockSimulationEntity;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.enderdragonlib.utils.AABBUtils;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ShockwaveEntity extends ThrowableProjectile {
    private int radius;
    private int step;
    private List<BlockPos> poses = new ArrayList<>();

    public ShockwaveEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ShockwaveEntity(Level level, int radius, float damage) {
        super(EntityRegistry.SHOCKWAVE.get(), level);

        this.radius = radius;
    }

    public void tick() {
        super.tick();

        BlockPos center = this.blockPosition();
        Level level = this.getLevel();

        if (this.poses.isEmpty()) {
            for (int i = -radius; i <= radius; i++) {
                float r1 = Mth.sqrt((radius * radius - i * i));

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
                int extraY = 0;
                BlockSimulationEntity entity = new BlockSimulationEntity(level, level.getBlockState(p));

                if (!level.getBlockState(p).isAir() && level.getBlockState(p.above()).isAir()) {
                    BlockState state = level.getBlockState(p);

                    if (!state.getMaterial().blocksMotion() || level.getBlockState(p.above()).getMaterial().blocksMotion())
                        continue;

                    entity.setPos(p.getX() + 0.5F, p.getY() + 0.5F, p.getZ() + 0.5F);
                } else if (level.getBlockState(p).isAir() && level.getBlockState(p.above()).isAir()){
                    for (int i = 0; i < radius; i++) {
                        BlockPos pos = new BlockPos(p.getX(), p.getY() - extraY, p.getZ());
                        BlockState state = level.getBlockState(pos);

                        if (!state.isAir() && level.getBlockState(pos.above()).isAir()) {
                            if (!state.getMaterial().blocksMotion() || level.getBlockState(p.above()).getMaterial().blocksMotion())
                                break;

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
                            if (!state.getMaterial().blocksMotion() || level.getBlockState(pos.above()).getMaterial().blocksMotion())
                                break;

                            entity = new BlockSimulationEntity(level, state);
                            entity.setPos(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5);

                            break;
                        }

                        extraY++;
                    }
                }

                entity.setDeltaMovement(0, step * 0.02F, 0);
                level.addFreshEntity(entity);

                BlockPos spawnPos = p;
                List<Block> oreMap = new ArrayList<>();

                while (this.level.getBlockState(spawnPos).isAir())
                    spawnPos = spawnPos.below();

                for (int i = 0; i < radius; i++) {
                    BlockState state = this.level.getBlockState(spawnPos.below(i));

                    if (!state.isAir())
                        if (state.getBlock() instanceof DropExperienceBlock)
                            oreMap.add(state.getBlock());
                }

                if (oreMap.isEmpty())
                    continue;

                int count = 0;

                for (Block block : oreMap) {
                    OreBlockSimulationEntity simulation = new OreBlockSimulationEntity(level, block.defaultBlockState());

                    simulation.setPos(spawnPos.getX() + 0.5, spawnPos.getY() - count + 0.01, spawnPos.getZ() + 0.5);
                    simulation.setOreCount(oreMap.size());
                    level.addFreshEntity(simulation);
                    count++;
                }
            }

            step++;

            if (poses.isEmpty() || step >= poses.size())
                this.remove(RemovalReason.KILLED);
        } else {
            this.remove(RemovalReason.KILLED);
        }
    }

    public boolean isPushedByFluid() {
        return false;
    }

    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.radius = compound.getInt("Radius");
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Radius", this.radius);
    }

    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
