package net.xXinailXx.thirteen_flames.item.flame;

import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.google.common.base.Suppliers;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.renderer.item.EmissiveRenderer;
import net.xXinailXx.thirteen_flames.network.packet.ScrollItemUpdatePacket;
import net.xXinailXx.thirteen_flames.network.packet.ScrollMenuOpenPacket;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ScrollHet extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("catalog", RelicAbilityEntry.builder().maxLevel(10).stat("modifier", RelicAbilityStat.builder().initialValue(2.5, 1.5).thresholdValue(0, 2.5).upgradeModifier(RelicAbilityStat.Operation.ADD, -0.1).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).ability("techn", RelicAbilityEntry.builder().maxLevel(2).stat("level", RelicAbilityStat.builder().initialValue(1, 1).thresholdValue(1, 5).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 100)).build();
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.fail(player.getOffhandItem());

        if (!ResearchUtils.isItemResearched(player, player.getItemInHand(hand).getItem()))
            return InteractionResultHolder.success(player.getItemInHand(hand));

        Network.sendToServer(new ScrollMenuOpenPacket(player.getMainHandItem(), -1, 0, 0, 0, null, ItemStack.EMPTY, new HashMap<>(), new HashMap<>()));

        return super.use(level, player, hand);
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
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.25, 0));
    }

    @SubscribeEvent
    public static void scrollEvent(InputEvent.MouseScrollingEvent event) {
        Player player = Minecraft.getInstance().player;

        if (player == null || !player.isShiftKeyDown())
            return;

        ItemStack stack = player.getMainHandItem();

        if (!ResearchUtils.isItemResearched(player, stack.getItem()))
            return;

        if (!(stack.getItem() instanceof ScrollHet))
            return;

        int scroll = event.getScrollDelta() > 0 ? 1 : -1;

        if (NBTUtils.getInt(stack, "type", 0) + scroll > 8)
            NBTUtils.setInt(stack, "type", 0);
        else if (NBTUtils.getInt(stack, "type", 0) + scroll < 0)
            NBTUtils.setInt(stack, "type", 7);
        else
            NBTUtils.setInt(stack, "type", NBTUtils.getInt(stack, "type", 0) + scroll);

        Network.sendToServer(new ScrollItemUpdatePacket(stack));

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player == null || player.level.isClientSide || player.getLevel().isClientSide)
            return;

        ItemStack mainStack = player.getMainHandItem();
        ItemStack offStack = player.getOffhandItem();
        ItemStack stack = null;

        if (mainStack.getItem() instanceof ScrollHet)
            stack = mainStack;
        else
            if (offStack.getItem() instanceof ScrollHet)
                stack = offStack;

        if (stack == null)
            return;

        if (!ResearchUtils.isItemResearched(player, stack.getItem()))
            return;

        int effectValue = (int) AbilityUtils.getAbilityValue(stack, "techn", "level");
        int scrollType = NBTUtils.getInt(stack, "type", 0);

        switch (scrollType) {
            case 0:
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 1, effectValue - 1, true, true));
                break;
            case 1:
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1, effectValue - 1, true, true));
                break;
            case 2:
                player.addEffect(new MobEffectInstance(AMEffectRegistry.KNOCKBACK_RESISTANCE.get(), 1, effectValue - 1, true, true));
                break;
            case 3:
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, effectValue - 1, true, true));
                break;
            case 4:
                player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 1, effectValue - 1, true, true));
                break;
            case 5:
                player.addEffect(new MobEffectInstance(MobEffects.JUMP, 1, effectValue - 1, true, true));
                break;
            case 6:
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1, effectValue - 1, true, true));
                break;
            default:
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, effectValue - 1, true, true));
                break;
        }
    }
}
