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
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

                int oreBlock = 0;
                for (int i = 1; i < radius * 5; i++) {
                    BlockPos pos = new BlockPos(p.getX(), p.getY() + extraY - i, p.getZ());
                    BlockState state = level.getBlockState(pos);

                    if (state.getBlock() instanceof DropExperienceBlock) {
//                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);

                        OreBlockSimulationEntity simulationEntity = new OreBlockSimulationEntity(level, state);
                        entity.setPos(p.getX() + 0.5F, p.getY() + 3.5F, p.getZ() + 0.5F);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0, 1.1, 0));
                        level.addFreshEntity(simulationEntity);
                        oreBlock++;
                    }
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

//    private int radius;
//    private int step;
//    private List<BlockPos> poses = new ArrayList<>();
//
//    public ShockwaveEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//    }
//
//    public ShockwaveEntity(Level level, int radius) {
//        super(EntityRegistry.SHOCKWAVE.get(), level);
//
//        this.radius = radius;
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        BlockPos center = this.blockPosition();
//        Level level = this.getLevel();
//
//        if (poses.isEmpty()) {
//            for (int i = -(int) radius; i <= radius; i++) {
//                float r1 = Mth.sqrt((float) (radius * radius - i * i));
//
//                for (int j = -(int) r1; j <= r1; j++)
//                    poses.add(center.offset(i, 0, j));
//            }
//        }
//
//        if (!poses.isEmpty()) {
//            Entity owner = getOwner();
//
//            Vec3 centerVec = new Vec3(center.getX(), center.getY(), center.getZ());
//            List<BlockPos> closest = poses.stream().filter(p -> new Vec3(p.getX(), p.getY(), p.getZ()).distanceTo(centerVec) <= step).toList();
//            poses.removeAll(closest);
//
//            for (BlockPos p : closest) {
//                if (!level.isClientSide()) {
//                    for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, new AABB(p, p.above(3)).inflate(0.5F))) {
//                        if (owner != null && entity.getStringUUID().equals(owner.getStringUUID())) {
//                            entity.setDeltaMovement(entity.position().add(0, 1, 0).subtract(centerVec).normalize().multiply(2, 1, 2));
//                        }
//                    }
//                }
//
//                BlockState state = level.getBlockState(p);
//
//                if (state.getMaterial().blocksMotion() && !level.getBlockState(p.above()).getMaterial().blocksMotion()) {
//                    BlockSimulationEntity entity = new BlockSimulationEntity(level, state);
//                    entity.setPos((double)((float)p.getX() + 0.5F), (double)((float)p.getY() + 0.5F), (double)((float)p.getZ() + 0.5F));
//                    entity.setDeltaMovement((double)0.0F, (double)((float)this.step * 0.02F), (double)0.0F);
//                    level.addFreshEntity(entity);
//                }
//
////                BlockPos.betweenClosed(p, p.offset(0, 64, 0)).iterator().forEachRemaining(pos -> {
////                    BlockState blockState = level.getBlockState(pos);
////
////                    if (blockState.getBlock() instanceof DropExperienceBlock) {
////                        if (!level.isClientSide()) {
////                            BlockSimulationEntity entity = new BlockSimulationEntity(level, blockState);
////                            entity.setPos(pos.getX(), getBlockEmpty(pos) - 1, pos.getZ());
////
////                            while (entity.getOnPos().getY() < getBlockEmpty(pos)) {
////                                entity.setDeltaMovement(0, 0.02, 0);
////                            }
////
////                            level.addFreshEntity(entity);
////                        }
////                    }
////                });
//
////                int[] posY = IntStream.range(1, (radius * 2)).toArray();
////
////                for (int y : posY) {
////                    BlockPos posExpBlock = new BlockPos(p.getX(), p.getY() - y, p.getZ());
////                    BlockState stateExpBlock = level.getBlockState(posExpBlock);
////
////                    if (stateExpBlock.getBlock() instanceof DropExperienceBlock expBlock) {
////                        level.setBlock(posExpBlock, Blocks.AIR.defaultBlockState(), 5);
////
////                        int[] posUp = IntStream.range(p.getY() + 1, 256).toArray();
////
////                        for (int posUpY : posUp) {
////                            BlockPos posUpBlock = new BlockPos(p.getX(), p.getY() + posUpY, p.getZ());
////                            BlockState stateUpBlock = level.getBlockState(posExpBlock);
////
////                            if (stateUpBlock.getBlock() == null) {
////                                level.setBlock(posUpBlock, stateExpBlock, 5);
////                                break;
////                            }
////                        }
////                    }
////                }
//            }
//
//            step++;
//            if (poses.isEmpty() || step >= poses.size())
//                this.remove(RemovalReason.KILLED);
//        } else
//            this.remove(RemovalReason.KILLED);
//    }
//
//    private int getBlockEmpty(BlockPos pos) {
//        int posY = pos.getY();
//
//        while (!this.getLevel().getBlockState(new BlockPos(pos.getX(), pos.getY() + posY, pos.getZ())).isAir()) {
//            posY++;
//        }
//
//        return posY;
//    }
//
//    public static boolean isAlliedTo(@Nullable Entity source, @Nullable Entity target) {
//        return (source == null || target == null) || (source.isAlliedTo(target) || target.isAlliedTo(source)) || (target.getUUID().equals(source.getUUID()))
//                || (target instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null && ownable.getOwnerUUID().equals(source.getUUID()));
//    }
//
//    @Override
//    protected void defineSynchedData() {
//    }
//
//    @Override
//    protected void readAdditionalSaveData(CompoundTag compound) {
//        this.radius = compound.getInt("Radius");
//    }
//
//    @Override
//    protected void addAdditionalSaveData(CompoundTag compound) {
//        compound.putInt("Radius", this.radius);
//    }
//
//    @Nonnull
//    public Packet<?> getAddEntityPacket() {
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }
}
