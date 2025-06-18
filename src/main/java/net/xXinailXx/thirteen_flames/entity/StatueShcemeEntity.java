package net.xXinailXx.thirteen_flames.entity;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.init.EntitiesRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;
import net.xXinailXx.thirteen_flames.utils.ParticleUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StatueShcemeEntity extends Projectile {
    private boolean isBuilded = false;
    private float progress = 0;
    private Gods god;

    public StatueShcemeEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public StatueShcemeEntity(Level level, Gods god) {
        this(EntitiesRegistry.SHCEME.get(), level);
        this.god = god;
    }

    public void tick() {
        super.tick();

        if (this.tickCount % 40 == 0 || this.tickCount == 0) {
            if (this.level.isClientSide)
                return;

            Color color = null;

            switch (getGod()) {
                case KNEF -> color = new Color(0, 0, 0);
                case SELYA -> color = new Color(242, 208, 53);
                case MONTU -> color = new Color(121, 251, 88);
                case RONOS -> color = new Color(212, 38, 19);
                case HET -> color = new Color(255, 247, 105);
                case GOD_PHARAOH -> color = new Color(255, 152, 26);
            }

            int count = 0;
            int maxCount = switch (getGod()) {
                case KNEF -> 45;
                case SELYA -> 45;
                case MONTU -> 45;
                case RONOS -> 45;
                case HET -> 45;
                case GOD_PHARAOH -> 150;
            };

            Iterable<BlockPos> iterable = null;

            if (getGod().equals(Gods.GOD_PHARAOH))
                iterable = BlockPos.betweenClosed(new BlockPos(Vec3.atCenterOf(this.blockPosition())).offset(-2, 0, -2), new BlockPos(Vec3.atCenterOf(this.blockPosition())).offset(2, 6, 2));
            else
                iterable = BlockPos.betweenClosed(new BlockPos(Vec3.atCenterOf(this.blockPosition())).offset(-1, 0, -1), new BlockPos(Vec3.atCenterOf(this.blockPosition())).offset(1, 4, 1));

            List<BlockPos> buildPosList = new ArrayList<>();

            for (BlockPos pos : iterable) {
                if (this.level.getBlockState(pos).is(Blocks.SANDSTONE)) {
                    buildPosList.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
                    count++;
                    continue;
                }

                ParticleOptions options = null;

                if (getGod().equals(Gods.KNEF))
                    options = ParticleUtils.createKnefParticle(color, 0.05F, 40, 0);
                else
                    options = ParticleUtils.createStatueParticle(color, 0.05F, 40, 0);

                ParticleActions.createBlock(options, ParticleActions.spawnSimpleParticle(new Color(239, 52, 34), 0.05F, 40, 0), Vec3.atCenterOf(pos), this.getLevel(), true);
            }

            Data.StatueBuilderData.ShcemeBuilder builder = new Data.StatueBuilderData.ShcemeBuilder(buildPosList, new BlockPos(Vec3.atCenterOf(this.blockPosition())), getGod());

            if (count == maxCount && !Data.StatueBuilderData.getShcemeBuilderList().contains(builder)) {
                Data.StatueBuilderData.addShceme(builder, this.getUUID());
                setBuilded(true);
            } else if (count != maxCount) {
                List<BlockPos> posList = new ArrayList<>();
                iterable.forEach(posList::add);

                Data.StatueBuilderData.removeShceme(new Data.StatueBuilderData.ShcemeBuilder(posList, new BlockPos(Vec3.atCenterOf(this.blockPosition())), getGod()), this.getUUID());
                setBuilded(false);
            }
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();

        if (this.level.isClientSide)
            return;

        Iterable<BlockPos> iterable = null;

        if (getGod().equals(Gods.GOD_PHARAOH))
            iterable = BlockPos.betweenClosed(new BlockPos(Vec3.atCenterOf(this.blockPosition())).offset(-2, 0, -2), new BlockPos(Vec3.atCenterOf(this.blockPosition())).offset(2, 6, 2));
        else
            iterable = BlockPos.betweenClosed(new BlockPos(Vec3.atCenterOf(this.blockPosition())).offset(-1, 0, -1), new BlockPos(Vec3.atCenterOf(this.blockPosition())).offset(1, 4, 1));

        List<BlockPos> posList = new ArrayList<>();

        iterable.forEach(posList::add);

        Data.StatueBuilderData.removeShceme(new Data.StatueBuilderData.ShcemeBuilder(posList, new BlockPos(Vec3.atCenterOf(this.blockPosition())), getGod()), this.getUUID());
    }

    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        Gods[] gods = Gods.values();
        setGod(gods[compound.getInt("god")]);
        setBuilded(compound.getBoolean("builded"));
        setProgress(compound.getFloat("progress"));
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("god", getGod().ordinal());
        compound.putBoolean("builded", isBuilded());
        compound.putFloat("progress", getProgress());
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
