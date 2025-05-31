package net.xXinailXx.thirteen_flames.block.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticleRendererTypes;
import net.xXinailXx.enderdragonlib.interfaces.ITickBlockEntity;
import net.xXinailXx.enderdragonlib.network.packet.SpawnParticlePacket;
import net.xXinailXx.enderdragonlib.utils.MathUtils;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.thirteen_flames.config.ThirteenFlamesConfig;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.effect.StatueEffect;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.utils.Gods;
import net.xXinailXx.thirteen_flames.utils.ParticleUtils;
import org.zeith.hammerlib.net.Network;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StatueBE<T extends StatueBE> extends BlockEntity implements IAnimatable, ITickBlockEntity {
    private AnimationFactory factory = new AnimationFactory(this);
    private int timeToUpgrade;
    private final Gods god;
    protected int tickCount;
    private final boolean finished;

    public StatueBE(BlockEntityType<?> type, BlockPos pos, BlockState state, Gods god, boolean finished) {
        super(type, pos, state);
        this.god = god;
        this.finished = finished;
    }

    public void tick() {
        if (this.level == null || this.level.isClientSide)
            return;

        if (this.tickCount == 0)
            this.timeToUpgrade = ThirteenFlamesConfig.TIME_TO_FLAME_UPGRADE.get();

        if (this.tickCount % 5 == 0 && !this.level.isClientSide) {
            Iterable<BlockPos> iterable = null;

            if (getGod().equals(Gods.GOD_PHARAOH))
                iterable = BlockPos.betweenClosed(this.worldPosition.offset(-2, 0, -2), this.worldPosition.offset(2, 6, 2));
            else
                iterable = BlockPos.betweenClosed(this.worldPosition.offset(-1, 0, -1), this.worldPosition.offset(1, 4, 1));

            List<BlockPos> posList = new ArrayList<>();

            for (BlockPos pos : iterable)
                posList.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));

            Data.StatueBuilderData.StatueBuilder builder = new Data.StatueBuilderData.StatueBuilder(posList, this.worldPosition);

            if (!Data.StatueBuilderData.getStatueList().contains(builder))
                Data.StatueBuilderData.addStatue(builder, this);
        }

        RandomSource random = this.level.getRandom();
        Color color = null;

        switch (getGod()) {
            case KNEF -> color = new Color(0, 0, 0);
            case SELYA -> color = new Color(255, 203, 0);
            case MONTU -> color = new Color(121, 251, 88);
            case RONOS -> color = new Color(212, 38, 19);
            case HET -> color = new Color(255, 247, 105);
            case GOD_PHARAOH -> color = new Color(255, 152, 26);
        }

        ParticleOptions options = null;

        if (getGod().equals(Gods.KNEF))
            options = ParticleUtils.createKnefParticle(color, 0.25F, random.nextInt(25, 60), 0.98F);
        else
            options = ParticleUtils.createStatueParticle(color, 0.25F, getGod().equals(Gods.GOD_PHARAOH) ? random.nextInt(40, 75) : random.nextInt(25, 60), 0.98F);

        if (this.finished && MathUtils.isRandom(this.level, getGod().equals(Gods.GOD_PHARAOH) ? 20 : 15)) {
            Iterable<BlockPos> iterable = null;

            if (getGod().equals(Gods.GOD_PHARAOH))
                iterable = BlockPos.betweenClosed(this.worldPosition.offset(- 2, 0, - 2), this.worldPosition.offset(2, 0, 2));
            else
                iterable = BlockPos.betweenClosed(this.worldPosition.offset(- 1, 0, - 1), this.worldPosition.offset(1, 0, 1));

            List<BlockPos> posList = new ArrayList<>();

            for (BlockPos pos : iterable)
                posList.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));

            BlockPos pos = null;
            pos = posList.get(random.nextInt(posList.size() - 1));

            Network.sendToAll(new SpawnParticlePacket(options, pos.getX() + 0.1 + random.nextInt(0, 8) * 0.1, pos.getY(), pos.getZ() + 0.1 + random.nextInt(0, 8) * 0.1, 0, random.nextInt(1, 12) * 0.01, 0));
        }

        if (this.tickCount % 5 == 0 && this.finished && !getGod().equals(Gods.GOD_PHARAOH)) {
            Player player = Minecraft.getInstance().player;

            if (this.timeToUpgrade <= 0) {
                Vec3 center = new Vec3(this.worldPosition.getX() + 0.5, this.worldPosition.getY(), this.worldPosition.getZ() + 0.5);

                ColoredParticle.Options particle = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                        .color(new Color(255, 140, 0).getRGB())
                        .renderType(ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                        .diameter(0.2F)
                        .lifetime(100)
                        .scaleModifier(0.98F)
                        .physical(false)
                        .build());

                for (int i = 0; i < 360; i++) {
                    if (i % 5 == 0) {
                        double a = 72 * i - this.tickCount * 10;
                        double radius = 1 + Math.sin(Math.toRadians(this.tickCount * 20) - 90) * 0.04 + this.tickCount;

                        Vec3 x = center.normalize().x < 0.001 && center.normalize().z < 0.001 ? center.normalize().cross(new Vec3(1, 0, 0)).normalize().scale(radius) : center.normalize().cross(new Vec3(0, 1, 0).normalize().scale(radius));
                        Vec3 z = center.normalize().cross(x).normalize().scale(radius);
                        Vec3 pos = center.add(x.scale(Math.cos(Math.toRadians(a)))).add(z.scale(Math.sin(Math.toRadians(a)))).xRot(90);

                        Network.sendToAll(new SpawnParticlePacket(particle, pos.x, pos.y, pos.z, 0, 0.01, 0));
                    }
                }
            }

            if (player != null) {
                StatueEffect effect = null;
                int amplifier = 0;

                Collection<MobEffectInstance> collection = player.getActiveEffects();

                for (MobEffectInstance inst : collection) {
                    if (inst.getEffect() instanceof StatueEffect eff && eff.getGod().equals(this.god)) {
                        effect = (StatueEffect) inst.getEffect();
                        amplifier = inst.getAmplifier();
                    }
                }

                if (effect != null) {
                    Direction direction = this.getBlockState().getValue(CustomStatueUtils.FACING);
                    BlockPos posCups = null;

                    switch (direction) {
                        case NORTH:
                            posCups = this.worldPosition.north(2);
                            break;
                        case SOUTH:
                            posCups = this.worldPosition.south(2);
                            break;
                        case WEST:
                            posCups = this.worldPosition.west(2);
                            break;
                        case EAST:
                            posCups = this.worldPosition.east(2);
                            break;
                        default:
                            posCups = this.worldPosition.north(2);
                    }

                    BlockPos pos = null;

                    for (int i = 0; i <= 1; i++) {
                        BlockState state = this.level.getBlockState(i == 0 ? posCups : posCups.below());

                        if (state.is(BlockRegistry.STATUE_CUP.get())) {
                            pos = i == 0 ? posCups : posCups.below();
                            break;
                        }
                    }

                    if (pos != null) {
                        BlockPos cup1 = null;
                        BlockPos cup2 = pos;
                        BlockPos cup3 = null;

                        switch (direction) {
                            case NORTH:
                                cup1 = pos.east();
                                cup3 = pos.west();
                                break;
                            case SOUTH:
                                cup1 = pos.west();
                                cup3 = pos.east();
                                break;
                            case WEST:
                                cup1 = pos.north();
                                cup3 = pos.south();
                                break;
                            case EAST:
                                cup1 = pos.south();
                                cup3 = pos.north();
                                break;
                            default:
                                cup1 = pos.east();
                                cup3 = pos.west();
                        }

                        if (cup1 != null && cup3 != null) {
                            if (this.level.getBlockState(cup1).is(BlockRegistry.STATUE_CUP.get()) && this.level.getBlockState(cup3).is(BlockRegistry.STATUE_CUP.get())) {
                                ParticleUtils.spawnCupFire(level, color, cup1);

                                if (amplifier >= 2 && amplifier <= 256)
                                    ParticleUtils.spawnCupFire(level, color, cup2);

                                if (amplifier >= 3 && amplifier <= 256)
                                    ParticleUtils.spawnCupFire(level, color, cup3);
                            }
                        }
                    }
                }
            }
        }

        this.timeToUpgrade = 0;
        this.tickCount++;

        setChanged();
    }

    public boolean isFinished() {
        return finished;
    }

    public Gods getGod() {
        return this.god;
    }

    public int getTimeToUpgrade() {
        return this.timeToUpgrade;
    }

    public void resetFlameUpgradeData() {
        this.timeToUpgrade = ThirteenFlamesConfig.TIME_TO_FLAME_UPGRADE.get();
    }

    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<T>
                ((T) this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    public AnimationFactory getFactory() {
        return this.factory;
    }
}
