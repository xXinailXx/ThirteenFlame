package net.xXinailXx.thirteen_flames.entity;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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

    public OreBlockSimulationEntity(Level level, BlockState state) {
        super(EntityRegistry.ORE_SIMULATION.get(), level);
        setBlockState(state);
        this.noPhysics = true;
    }

    public void tick() {
        super.tick();

        this.move(MoverType.SELF, getDeltaMovement());
        setDeltaMovement(0, 0.025F, 0);

        Block clickTargetBlock = null;

        if (this.tickCount == 0 && !this.level.getBlockState(this.blockPosition().above()).isAir()) {
            clickTargetBlock = this.level.getBlockState(this.blockPosition().above()).getBlock();
        }

        int oreEntity = 0;

        for (Entity entity : this.level.getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(0, 21, 0))) {
            if (entity instanceof OreBlockSimulationEntity) {
                oreEntity++;
            }
        }

        if (this.tickCount % (40 * oreEntity == 0 ? 40 : 40 * oreEntity) == 0 || (!this.level.getBlockState(this.blockPosition()).isAir() && this.tickCount > (39 * oreEntity))) {
            if (!level.isClientSide()) {
                this.level.setBlock(this.blockPosition().above(), getBlockState(), 11);
            }

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
