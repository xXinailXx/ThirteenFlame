package net.xXinailXx.thirteen_flames.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class OreBlockSimulationEntity extends Entity {
    private static final EntityDataAccessor<Optional<BlockState>> BLOCK_STATE = SynchedEntityData.defineId(OreBlockSimulationEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Integer> ORE_COUNT = SynchedEntityData.defineId(OreBlockSimulationEntity.class, EntityDataSerializers.INT);

    public OreBlockSimulationEntity(EntityType<?> pEntityType, Level level) {
        super(pEntityType, level);
    }

    public OreBlockSimulationEntity(Level level, BlockState state) {
        super(EntityRegistry.ORE_SIMULATION.get(), level);
        setBlockState(state);
        this.noPhysics = true;
    }

    public void tick() {
        super.tick();

        this.move(MoverType.SELF, getDeltaMovement());
        setDeltaMovement(0, 0.05F, 0);

        if (this.tickCount % (20 * getOreCount() == 0 ? 20 : 20 * getOreCount()) == 0) {
            if (!level.isClientSide())
                this.level.setBlock(new BlockPos(this.position()).above(), getBlockState(), 11);

            this.remove(RemovalReason.KILLED);
        }
    }

    public void setBlockState(@Nullable BlockState state) {
        this.entityData.set(BLOCK_STATE, Optional.ofNullable(state));
    }

    @Nullable
    public BlockState getBlockState() {
        return this.entityData.get(BLOCK_STATE).orElse(null);
    }

    public void setOreCount(int count) {
        this.entityData.set(ORE_COUNT, count);
    }

    public int getOreCount() {
        return this.entityData.get(ORE_COUNT);
    }

    protected void defineSynchedData() {
        this.entityData.define(BLOCK_STATE, Optional.of(Blocks.AIR.defaultBlockState()));
        this.entityData.define(ORE_COUNT, 1);
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        setBlockState(NbtUtils.readBlockState(compound.getCompound("BlockState")));
        setOreCount(compound.getInt("ore_count"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        BlockState state = getBlockState();

        if (state != null) {
            compound.put("BlockState", NbtUtils.writeBlockState(state));
        }

        compound.putInt("ore_count", getOreCount());
    }

    public boolean isPushedByFluid() {
        return false;
    }

    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
