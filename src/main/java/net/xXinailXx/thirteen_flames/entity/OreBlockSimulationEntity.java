package net.xXinailXx.thirteen_flames.entity;

import com.mojang.math.Vector3f;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class OreBlockSimulationEntity extends Entity {
    private static final EntityDataAccessor<Optional<BlockState>> BLOCK_STATE = SynchedEntityData.defineId(OreBlockSimulationEntity.class, EntityDataSerializers.BLOCK_STATE);

    public OreBlockSimulationEntity(EntityType<?> pEntityType, Level level) {
        super(pEntityType, level);
    }

    public  OreBlockSimulationEntity(Level level, BlockState state) {
        super(EntityRegistry.ORE_SIMULATION.get(), level);
        setBlockState(state);
        this.noPhysics = true;
    }

    public void tick() {
        super.tick();

        if (!level.isClientSide()) {
            this.getLevel().setBlock(this.blockPosition(), getBlockState(), 11);
        }

        if (this.level.getBlockState(this.blockPosition()).isAir()) {
            this.level.setBlock(this.blockPosition(), getBlockState(), 11);
            this.remove(RemovalReason.KILLED);
        }

//        this.move(MoverType.SELF, getDeltaMovement());
//        setDeltaMovement(getDeltaMovement().add(0, 1F, 0));
//
//        BlockState state = this.level.getBlockState(this.getOnPos());

//        if (this.level.getBlockState(getOnPos()).isAir()) {
//            this.remove(RemovalReason.KILLED);
//            level.setBlock(getOnPos(), getBlockState(), 11);
//        }
    }

    public void setBlockState(@Nullable BlockState state) {
        this.entityData.set(BLOCK_STATE, Optional.ofNullable(state));
    }

    @Nullable
    public BlockState getBlockState() {
        return this.entityData.get(BLOCK_STATE).orElse(null);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BLOCK_STATE, Optional.of(Blocks.AIR.defaultBlockState()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        setBlockState(NbtUtils.readBlockState(compound.getCompound("BlockState")));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        BlockState state = getBlockState();

        if (state != null) {
            compound.put("BlockState", NbtUtils.writeBlockState(state));
        }
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
