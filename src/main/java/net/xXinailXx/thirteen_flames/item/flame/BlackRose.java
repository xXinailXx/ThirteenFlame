package net.xXinailXx.thirteen_flames.item.flame;

import it.hurts.sskirillss.relics.api.events.common.ContainerSlotClickEvent;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.ResearchUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.entity.LivingFleshEntity;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

@Mod.EventBusSubscriber
public class BlackRose extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("flesh", RelicAbilityEntry.builder().maxLevel(10).stat("revival", RelicAbilityStat.builder().initialValue(5, 10).thresholdValue(5, 50).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int) MathUtils.round(value, 0);
        }).build()).build()).ability("stillbirth", RelicAbilityEntry.builder().maxLevel(5).stat("size", RelicAbilityStat.builder().initialValue(1, 1).thresholdValue(1, 6).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int) MathUtils.round(value, 0);
        }).build()).stat("separation", RelicAbilityStat.builder().initialValue(5, 10).thresholdValue(5, 50).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int) MathUtils.round(value, 0);
        }).build()).stat("piece", RelicAbilityStat.builder().initialValue(1, 5).thresholdValue(1, 10).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int) MathUtils.round(value, 0);
        }).build()).build()).ability("bonfire", RelicAbilityEntry.builder().maxLevel(10).stat("resist", RelicAbilityStat.builder().initialValue(0.25, 0.25).thresholdValue(0.25, 3.5).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.25).formatValue((value) -> {
            return MathUtils.round(value, 2);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 20, 150)).build();
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (entity.tickCount % 5 == 0 && NBTUtils.getInt(stack, "bone", 0) > 0) {
            int fire_type = NBTUtils.getInt(stack, "fire_type", 0);

            if (fire_type + 1 > 8)
                NBTUtils.setInt(stack, "fire_type", 1);
            else
                NBTUtils.setInt(stack, "fire_type", fire_type += 1);
        } else if (NBTUtils.getInt(stack, "bone", 0) == 0) {
            if (NBTUtils.getInt(stack, "fire_type", 0) > 0)
                NBTUtils.setInt(stack, "fire_type", 0);
        }

        super.inventoryTick(stack, level, entity, slot, isSelected);
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0.5, 0));
    }

    @SubscribeEvent
    public static void entityDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof LivingFleshEntity)
            return;

        Entity source = event.getSource().getEntity();

        if (!(source instanceof Player player)) {
            return;
        }

        for (int i = 0; i < 8; i++) {
            ItemStack stack = player.getInventory().items.get(i);

            if (stack.getItem() instanceof BlackRose) {
                if (!ResearchUtils.isItemResearched(player, stack.getItem()))
                    return;

                if (!net.xXinailXx.enderdragonlib.utils.MathUtils.isRandom(player.getLevel(), (int) AbilityUtils.getAbilityValue(stack, "flesh", "revival")))
                    return;

                player.sendSystemMessage(Component.literal(String.valueOf(AbilityUtils.getAbilityValue(stack, "stillbirth", "size"))));

                LivingFleshEntity entity = new LivingFleshEntity(player.getLevel(), (int) AbilityUtils.getAbilityValue(stack, "stillbirth", "size"), (int) AbilityUtils.getAbilityValue(stack, "stillbirth", "separation"), (int) AbilityUtils.getAbilityValue(stack, "stillbirth", "piece"), stack);
                entity.setPos(event.getEntity().position());
                player.getLevel().addFreshEntity(entity);

                LevelingUtils.addExperience(player, stack, 10);

                return;
            }
        }
    }

    @SubscribeEvent
    public static void playerHurt(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();

        if (!(entity instanceof Player player))
            return;

        if (player.isCreative() || player.getLevel().isClientSide)
            return;

        for (int i = 0; i < 8; i++) {
            ItemStack stack = player.getInventory().items.get(i);

            if (stack.getItem() instanceof BlackRose && NBTUtils.getInt(stack, "bone", 0) > 0) {
                double resist = AbilityUtils.getAbilityValue(stack, "bonfire", "resist");
                int boneCount = NBTUtils.getInt(stack, "bone", 0);
                float amount = event.getAmount();
                float newDamage = 0;

                if (boneCount >= amount * 0.5 / resist) {
                    newDamage = (float) (amount * 0.5);
                    NBTUtils.setInt(stack, "bone", (int) (boneCount - (amount * 0.5 / resist)));
                } else {
                    newDamage = (float) (boneCount * resist);
                    NBTUtils.setInt(stack, "bone", 0);
                }

                LevelingUtils.addExperience((Entity) player, stack, (int) newDamage / 2);

                player.heal(newDamage);
            }
        }
    }

    @SubscribeEvent
    public static void slotClick(ContainerSlotClickEvent event) {
        if (event.getAction() != ClickAction.PRIMARY)
            return;

        Player player = event.getEntity();

        ItemStack heldStack = event.getHeldStack();
        ItemStack slotStack = event.getSlotStack();

        if (!(slotStack.getItem() instanceof BlackRose))
            return;

        if (!(heldStack.is(Items.BONE)))
            return;

        int time = ForgeHooks.getBurnTime(heldStack, RecipeType.SMELTING) / 20;
        int count = heldStack.getCount();
        int boneCount = NBTUtils.getInt(slotStack, "bone", 0);

        if (boneCount + count >= 64) {
            NBTUtils.setInt(slotStack, "bone", 64);
            heldStack.shrink(64 - boneCount);
        } else {
            NBTUtils.setInt(slotStack, "bone", boneCount + count);
            heldStack.shrink(count);
        }

        LevelingUtils.addExperience(player, slotStack, count);

        player.playSound(SoundEvents.BLAZE_SHOOT, 0.75F, 2F / (time * 0.025F));

        event.setCanceled(true);
    }
}
