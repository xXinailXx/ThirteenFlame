package net.xXinailXx.thirteen_flames.item.flame;

import com.google.common.base.Suppliers;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.renderer.item.EmissiveRenderer;
import net.xXinailXx.thirteen_flames.entity.ShockwaveEntity;
import net.xXinailXx.thirteen_flames.item.base.tools.PickaxeItemTF;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.tools.ToolTierTF;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class HammerMontu extends PickaxeItemTF {
    public HammerMontu() {
        super(ToolTierTF.THIRTEEN_FLAMES, 3, -2.4f);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("ejection", RelicAbilityEntry.builder().maxLevel(15).stat("cooldown", RelicAbilityStat.builder().initialValue(56, 35).thresholdValue(15, 56).upgradeModifier(RelicAbilityStat.Operation.ADD, -2).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("radius", RelicAbilityStat.builder().initialValue(6, 11).thresholdValue(6, 19).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 1);
        }).build()).build()).ability("digging", RelicAbilityEntry.builder().maxLevel(2).stat("mining", RelicAbilityStat.builder().initialValue(1, 1).thresholdValue(1, 3).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 15, 100)).build();
    }

    @Override
    public InteractionResult useOn(UseOnContext use) {
        Player player = use.getPlayer();

        if (!AbilityUtils.isAbilityOnCooldown(use.getItemInHand(), "ejection")) {
            Level level = player.getCommandSenderWorld();

            ShockwaveEntity shockwave = new ShockwaveEntity(level, (int) AbilityUtils.getAbilityValue(use.getItemInHand(), "ejection", "radius"), 0);
            shockwave.setOwner(player);
            shockwave.setPos(use.getClickedPos().getX(), use.getClickedPos().getY(), use.getClickedPos().getZ());
            level.addFreshEntity(shockwave);

            LevelingUtils.addExperience(player, use.getItemInHand(), 5);

            player.getCooldowns().addCooldown(use.getItemInHand().getItem(), (int) (AbilityUtils.getAbilityValue(use.getItemInHand(), "ejection", "cooldown") * 20));
        }

        return super.useOn(use);
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.5, 0));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final Supplier<EmissiveRenderer> renderer = Suppliers.memoize(EmissiveRenderer::new);

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return (BlockEntityWithoutLevelRenderer)this.renderer.get();
            }
        });
    }

    @SubscribeEvent
    public static void onBlockDestroy(BlockEvent.BreakEvent event) {
        ItemStack stack = event.getPlayer().getMainHandItem();
        BlockPos pos = event.getPos();
        Level level = event.getPlayer().getLevel();
        double maxLevel = AbilityUtils.getAbilityValue(stack, "digging", "mining");

        if (stack.getItem() == ItemRegistry.HAMMER_MONTU.get()) {
            switch ((int) maxLevel) {
                case 2 -> {
                    BlockState state = level.getBlockState(pos.below());

                    if (!(state.getMaterial() == Material.BARRIER) || !(state.getMaterial() == Material.PORTAL))
                        level.destroyBlock(pos.below(), true);
                }
                case 3 -> {
                    BlockState stateAbove = level.getBlockState(pos.below());
                    BlockState stateBelow = level.getBlockState(pos.above());

                    if (!(stateAbove.getMaterial() == Material.BARRIER) || !(stateAbove.getMaterial() == Material.PORTAL) && !(stateBelow.getMaterial() == Material.BARRIER) || !(stateBelow.getMaterial() == Material.PORTAL)) {
                        level.destroyBlock(pos.below(), true);
                        level.destroyBlock(pos.above(), true);
                    }
                }
            }
        }
    }
}
