package net.xXinailXx.thirteen_flames.item.flame;

import com.google.common.base.Suppliers;
import it.hurts.sskirillss.relics.init.EffectRegistry;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.renderer.item.EmissiveRenderer;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.base.FlameItemSetting;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ShieldRonosa extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("spikes", RelicAbilityEntry.builder().maxLevel(10).stat("procent", RelicAbilityStat.builder().initialValue(15, 150).thresholdValue(15, 200).upgradeModifier(RelicAbilityStat.Operation.ADD, 10).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("bleeding", RelicAbilityStat.builder().initialValue(5, 15).thresholdValue(5, 20).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 10, 100)).build();
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        player.startUsingItem(hand);
        NBTUtils.setInt(stack, "use", 1);

        return InteractionResultHolder.consume(stack);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        NBTUtils.setInt(stack, "use", 0);

        return super.finishUsingItem(stack, level, entity);
    }

    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
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
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.5, 0));
    }

    @SubscribeEvent
    public static void shieldUse(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();

        if (!(living instanceof Player player))
            return;

        ItemStack stack;

        if (player.getMainHandItem().is(ItemRegistry.SHIELD_RONOSA.get()))
            stack = player.getMainHandItem();
        else if (player.getOffhandItem().is(ItemRegistry.SHIELD_RONOSA.get()))
            stack = player.getOffhandItem();
        else
            return;

        if (NBTUtils.getInt(stack, "use", 0) == 0)
            return;

        Entity entity = event.getSource().getEntity();

        if (!(entity instanceof LivingEntity))
            return;

        float damage = (float) (event.getAmount() * (AbilityUtils.getAbilityValue(stack, "spikes", "procent") * 0.01));

        entity.hurt(DamageSource.playerAttack(player), damage);

        ((LivingEntity) entity).addEffect(new MobEffectInstance(EffectRegistry.BLEEDING.get(), 60 * (int) AbilityUtils.getAbilityValue(stack, "spikes", "bleeding")));
        LevelingUtils.addExperience(stack, 3);
    }
}
