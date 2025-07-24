package net.xXinailXx.thirteen_flames.block;

import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticle;
import net.xXinailXx.enderdragonlib.client.particle.ColoredParticleRendererTypes;
import net.xXinailXx.enderdragonlib.client.particle.ParticleActions;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.enderdragonlib.utils.statues.data.StatueData;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.config.ThirteenFlamesCommonConfig;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Mod.EventBusSubscriber
public abstract class StatueHandler extends CustomStatueUtils implements IAnimatable {
    @Getter
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    protected final Gods god;

    public StatueHandler(Block block, @NotNull Gods gods) {
        super(Properties.of(Material.METAL).strength(1), block, Block.box(0, 0, 0, 48, 80, 48).move(-1, 0, -1));
        this.god = gods;
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (level.isClientSide)
            return;

        for (BlockPos pos1 : getBlockPoses(pos, false)) {
            StatueStructureBlock structureBlock = (StatueStructureBlock) this.getStructureBlock();

            level.setBlock(pos1, structureBlock.defaultBlockState(), 11);
        }

        StatueData.addStatue(new StatueData.StatueBuilder(getBlockPoses(pos, false), pos));
    }

    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        super.destroy(accessor, pos, state);

        for (BlockPos pos1 : getBlockPoses(pos, false))
            accessor.destroyBlock(pos1, false);

        StatueData.removeStatue(pos);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        super.playerDestroy(level, player, pos, state, entity, stack);

        for (BlockPos pos1 : getBlockPoses(pos, false))
            level.destroyBlock(pos1, false);

        StatueData.removeStatue(pos);
    }

    public List<BlockPos> getBlockPoses(BlockPos pos, boolean isMain) {
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 4, 1));
        List<BlockPos> posList = new ArrayList<>();

        for (BlockPos blockPos : iterable) {
            if (!isMain && blockPos.equals(pos))
                continue;

            posList.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        }

        return posList;
    }

    @Nullable
    public StatueBE getBE(BlockPos pos) {
        return (StatueBE) StatueData.getStatueBE(pos);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    public static boolean isUpgrade(ItemStack stack, Gods gods) {
        if (!ThirteenFlamesCommonConfig.FLAME_UPGRADE_REQ_TYPE.get())
            return true;

        if (!(stack.getItem() instanceof FlameItemSetting))
            return false;

        return switch (gods) {
            case KNEF -> stack.is(ItemRegistry.BLACK_ROSE.get()) || stack.is(ItemRegistry.MOON_BOW.get());
            case SELYA -> stack.is(ItemRegistry.SUN_SELIASET.get()) || stack.is(ItemRegistry.HORN_SELIASET.get());
            case MONTU -> stack.is(ItemRegistry.HAMMER_MONTU.get()) || stack.is(ItemRegistry.GLOVES_MONTU.get());
            case RONOS -> stack.is(ItemRegistry.SWORD_RONOSA.get()) || stack.is(ItemRegistry.SHIELD_RONOSA.get());
            case HET -> stack.is(ItemRegistry.SCROLL_HET.get()) || stack.is(ItemRegistry.FLIGHT_HET.get());
            case GOD_PHARAOH -> false;
        };
    }

    public static void upgrade(StatueBE be, Level level, ItemStack stack, Player player, InteractionHand hand) {
        if (be == null || !be.isFinished() || be.getTimeToUpgrade() > 0 || !isUpgrade(stack, be.getGod()))
            return;

        if (level.isClientSide)
            return;

        LevelingUtils.addExperience(stack, 600);

        ItemStack newStack = stack.copy();
        player.setItemSlot(EquipmentSlot.MAINHAND, newStack);

        ColoredParticle.Options options = new ColoredParticle.Options(ColoredParticle.Constructor.builder()
                .color(new Color(255, 140, 0).getRGB())
                .renderType(ColoredParticleRendererTypes.RENDER_LIGHT_COLOR)
                .diameter(0.2F)
                .lifetime(100)
                .scaleModifier(0.98F)
                .physical(true)
                .build());

        Vec3 center = new Vec3(player.getX(), player.getY() + 1, player.getZ());

        ParticleActions.createBall(options, center, level, 2, 0.15F);
        be.resetFlameUpgradeData();
        player.swing(hand);
    }

    @SubscribeEvent
    public static void useStatues(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (player == null || player.getLevel().isClientSide)
            return;

        Level level = event.getLevel();
        BlockState state = level.getBlockState(event.getPos());
        ItemStack stack = player.getItemInHand(event.getHand());

        if (stack.getItem() instanceof FlameItemSetting setting) {
            RelicLevelingData data = setting.getRelicData().getLevelingData();

            if (LevelingUtils.getLevel(stack) >= data.getMaxLevel())
                return;

            if (state.getBlock() instanceof StatueHandler handler)
                upgrade(handler.getBE(event.getPos()), level, stack, player, event.getHand());
            else if (state.getBlock() instanceof StatueStructureBlock structureBlock)
                upgrade(structureBlock.getMainBlockBE(event.getPos()), level, stack, player, event.getHand());
        }
    }
}
