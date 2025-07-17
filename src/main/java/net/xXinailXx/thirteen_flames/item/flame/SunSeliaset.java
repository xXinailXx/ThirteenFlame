package net.xXinailXx.thirteen_flames.item.flame;

import com.google.common.base.Suppliers;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.api.events.client.EntityInteractEvent;
import net.xXinailXx.enderdragonlib.capability.managers.CompoundManager;
import net.xXinailXx.thirteen_flames.block.StatueHandler;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.client.renderer.item.EmissiveRenderer;
import net.xXinailXx.thirteen_flames.entity.SunSeliasetEntity;
import net.xXinailXx.thirteen_flames.network.packet.FlameUpgradePacket;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class SunSeliaset extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("light", RelicAbilityEntry.builder().maxLevel(10).stat("radius", RelicAbilityStat.builder().initialValue(10, 10).thresholdValue(10, 25).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("cooldown", RelicAbilityStat.builder().initialValue(10, 10).thresholdValue(0, 10).upgradeModifier(RelicAbilityStat.Operation.ADD, -1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 10, 100)).build();
    }

    public InteractionResult useOn(UseOnContext use) {
        BlockPos pos = use.getClickedPos().above();
        Level level = use.getLevel();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = use.getItemInHand();

        if (!ResearchUtils.isItemResearched(use.getPlayer(), stack.getItem()))
            return InteractionResult.SUCCESS;

        if (state.getBlock() instanceof StatueHandler handler || state.getBlock() instanceof StatueStructureBlock structureBlock) {
            StatueBE be = state.getBlock() instanceof StatueHandler handler ? handler.getBE(pos) : ((StatueStructureBlock) state.getBlock()).getMainBlockBE(pos);

            if (be != null && be.getTimeToUpgrade() == 0) {
                RelicLevelingData data = ((FlameItemSetting) stack.getItem()).getRelicData().getLevelingData();

                if (LevelingUtils.getLevel(stack) < data.getMaxLevel()) {
                    Network.sendToServer(new FlameUpgradePacket(stack));
                    be.resetFlameUpgradeData();

                    return InteractionResult.SUCCESS;
                }
            } else {
                return InteractionResult.FAIL;
            }
        }

        SunSeliasetEntity sun = new SunSeliasetEntity(level, (int) AbilityUtils.getAbilityValue(stack, "light", "radius"), (int) AbilityUtils.getAbilityValue(stack, "light", "cooldown"), stack);
        sun.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.addFreshEntity(sun);

        CompoundManager.add(sun.getStringUUID(), stack.save(stack.getTag()));
        use.getPlayer().setItemInHand(use.getHand(), Items.AIR.getDefaultInstance());

        return InteractionResult.SUCCESS;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final Supplier<EmissiveRenderer> renderer = Suppliers.memoize(EmissiveRenderer::new);

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return (BlockEntityWithoutLevelRenderer)this.renderer.get();
            }
        });
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0, 0));
    }

    @SubscribeEvent
    public static void useSun(EntityInteractEvent event) {
        Player player = event.getEntity();

        if (player == null || player.getLevel().isClientSide)
            return;

        Entity entity = event.getTarget();

        if (!(entity instanceof SunSeliasetEntity sun))
            return;

        if (player.isShiftKeyDown() && sun.getPhase() == 0) {
            ItemStack stack = ItemStack.of(CompoundManager.get(sun.getStringUUID()));

            LevelingUtils.addExperience(stack, sun.getAddExp());
            sun.remove(Entity.RemovalReason.KILLED);
            player.addItem(stack);
        } else if (sun.getPhase() == 0) {
            sun.nextPhase();
        } else if (sun.isAnim()) {
            sun.nextPhase();
        }
    }
}
