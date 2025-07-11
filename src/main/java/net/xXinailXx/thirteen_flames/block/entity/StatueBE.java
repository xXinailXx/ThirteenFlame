package net.xXinailXx.thirteen_flames.block.entity;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
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
import java.util.List;
import java.util.Map;

public class StatueBE extends BlockEntity implements IAnimatable, ITickBlockEntity {
    @Getter
    private AnimationFactory factory = new AnimationFactory(this);
    @Setter
    @Getter
    private int timeToUpgrade = ThirteenFlamesConfig.TIME_TO_FLAME_UPGRADE.get();
    @Getter
    private final Gods god;
    protected int tickCount = 0;
    @Getter
    private final boolean finished;

    public StatueBE(BlockEntityType<?> type, BlockPos pos, BlockState state, Gods god, boolean finished) {
        super(type, pos, state);
        this.god = god;
        this.finished = finished;
    }

    public void tick() {
        if (this.level == null || this.level.isClientSide)
            return;

        if (this.tickCount % 5 == 0 || this.tickCount == 0) {
            Data.StatueBuilderData.StatueBuilder builder = null;

            if (!Data.StatueBuilderData.containsStatue(this)) {
                Iterable<BlockPos> iterable;

                if (getGod().equals(Gods.GOD_PHARAOH))
                    iterable = BlockPos.betweenClosed(this.worldPosition.offset(- 2, 0, - 2), this.worldPosition.offset(2, 6, 2));
                else
                    iterable = BlockPos.betweenClosed(this.worldPosition.offset(- 1, 0, - 1), this.worldPosition.offset(1, 4, 1));

                List<BlockPos> posList = new ArrayList<>();

                for (BlockPos pos : iterable)
                    posList.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));

                builder = new Data.StatueBuilderData.StatueBuilder(posList, this.worldPosition);
                Data.StatueBuilderData.addStatue(builder);
            }

            if (builder != null && !Data.StatueBuilderData.containsStatueBE(builder)) {
                Data.StatueBuilderData.addStatueBE(builder);
            } else if (builder == null) {
                builder = Data.StatueBuilderData.getStatue(this.worldPosition);

                Data.StatueBuilderData.addStatueBE(builder);
            }
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

        if (this.finished && MathUtils.isRandom(this.level, getGod().equals(Gods.GOD_PHARAOH) ? 20 : 15)) {
            ParticleOptions options;

            if (getGod().equals(Gods.KNEF))
                options = ParticleUtils.createKnefParticle(color, 0.25F, random.nextInt(25, 60), 0.98F);
            else
                options = ParticleUtils.createStatueParticle(color, 0.25F, getGod().equals(Gods.GOD_PHARAOH) ? random.nextInt(40, 75) : random.nextInt(25, 60), 0.98F);

            Iterable<BlockPos> iterable;

            if (getGod().equals(Gods.GOD_PHARAOH))
                iterable = BlockPos.betweenClosed(this.worldPosition.offset(-2, 0, -2), this.worldPosition.offset(2, 0, 2));
            else
                iterable = BlockPos.betweenClosed(this.worldPosition.offset(-1, 0, -1), this.worldPosition.offset(1, 0, 1));

            List<BlockPos> posList = new ArrayList<>();

            for (BlockPos pos : iterable)
                posList.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));

            BlockPos pos;
            pos = posList.get(random.nextInt(posList.size() - 1));

            Network.sendToAll(new SpawnParticlePacket(options, pos.getX() + 0.1 + random.nextInt(0, 8) * 0.1, pos.getY(), pos.getZ() + 0.1 + random.nextInt(0, 8) * 0.1, 0, random.nextInt(1, 12) * 0.01, 0));
        }

        if (this.tickCount % 5 == 0 && this.finished && !getGod().equals(Gods.GOD_PHARAOH)) {
            if (this.timeToUpgrade == 0) {
                ColoredParticle.Options particle = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                        .color(new Color(255, 140, 0).getRGB())
                        .renderType(getGod().equals(Gods.KNEF) ? ColoredParticleRendererTypes.DISABLE_RENDER_LIGHT_COLOR : ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                        .diameter(0.2F)
                        .lifetime(100)
                        .scaleModifier(0.95F)
                        .physical(false)
                        .build());

                for (int i = 0; i < 180; i++) {
                    double a = 2 * i + this.tickCount * 10;
                    double radius = 1 + Math.sin(Math.toRadians(this.tickCount * 20) - 90) * 0.02;

                    Vec3 motion = new Vec3(0, this.tickCount * 0.1, 0);
                    Vec3 x = motion.normalize().x < 0.001 && motion.normalize().z < 0.001 ? motion.normalize().cross(new Vec3(1, 0, 0)).normalize().scale(radius) : motion.normalize().cross(new Vec3(0, 1, 0)).normalize().scale(radius);
                    Vec3 z = motion.normalize().cross(x).normalize().scale(radius);
                    Vec3 pos = this.getPosition(1, new Vec3(0, (this.tickCount - 1) * 0.1, 0)).add(x.scale(Math.cos(Math.toRadians(a)))).add(z.scale(Math.sin(Math.toRadians(a))));

                    Network.sendToAll(new SpawnParticlePacket(particle, pos.x + 0.5, pos.y + 0.3, pos.z + 0.5, 0, 0.02, 0));
                }
            }

            Player player = Minecraft.getInstance().player;

            if (player != null) {
                StatueEffect effect = null;
                int amplifier = -1;

                Map<MobEffect, MobEffectInstance> map = player.getActiveEffectsMap();

                if (map != null && !map.isEmpty()) {
                    for (MobEffect mobEffect : map.keySet()) {
                        if (mobEffect instanceof StatueEffect eff && eff.getGod().equals(getGod())) {
                            effect = eff;
                            amplifier = map.get(mobEffect).getAmplifier();
                        }
                    }

                    if (effect != null) {
                        Direction direction = this.getBlockState().getValue(CustomStatueUtils.FACING);
                        BlockPos posCups = switch (direction) {
                            case SOUTH -> this.worldPosition.south(2);
                            case WEST -> this.worldPosition.west(2);
                            case EAST -> this.worldPosition.east(2);
                            default -> this.worldPosition.north(2);
                        };

                        BlockPos pos = null;

                        for (int i = 0; i <= 1; i++) {
                            BlockState state = this.level.getBlockState(i == 0 ? posCups : posCups.below());

                            if (state.is(BlockRegistry.STATUE_CUP.get())) {
                                pos = i == 0 ? posCups : posCups.below();
                                break;
                            }
                        }

                        if (pos != null) {
                            BlockPos cup1;
                            BlockPos cup3 = switch (direction) {
                                case SOUTH -> {
                                    cup1 = pos.west();
                                    yield pos.east();
                                }
                                case WEST -> {
                                    cup1 = pos.north();
                                    yield pos.south();
                                }
                                case EAST -> {
                                    cup1 = pos.south();
                                    yield pos.north();
                                }
                                default -> {
                                    cup1 = pos.east();
                                    yield pos.west();
                                }
                            };

                            if (cup1 != null && cup3 != null) {
                                if (this.level.getBlockState(cup1).is(BlockRegistry.STATUE_CUP.get()) && this.level.getBlockState(cup3).is(BlockRegistry.STATUE_CUP.get())) {
                                    ParticleUtils.spawnCupFire(level, color, cup1);

                                    if (amplifier >= 2)
                                        ParticleUtils.spawnCupFire(level, color, pos);

                                    if (amplifier >= 3)
                                        ParticleUtils.spawnCupFire(level, color, cup3);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.timeToUpgrade > 0)
            this.timeToUpgrade--;

        this.tickCount++;

        setChanged();
    }

    public final Vec3 getPosition(float v, Vec3 vec3) {
        double d0 = Mth.lerp(v, vec3.x, this.worldPosition.getX());
        double d1 = Mth.lerp(v, vec3.y, this.worldPosition.getY());
        double d2 = Mth.lerp(v, vec3.z, this.worldPosition.getZ());
        return new Vec3(d0, d1, d2);
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("tick_count", this.tickCount);
        tag.putInt("time_to_upgrade", this.timeToUpgrade);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();

        this.saveAdditional(tag);

        return tag;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);

        this.load(pkt.getTag());
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.tickCount = tag.getInt("tick_count");
        this.timeToUpgrade = tag.getInt("time_to_upgrade");
    }

    public void resetFlameUpgradeData() {
        this.timeToUpgrade = ThirteenFlamesConfig.TIME_TO_FLAME_UPGRADE.get();
    }

    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

}
