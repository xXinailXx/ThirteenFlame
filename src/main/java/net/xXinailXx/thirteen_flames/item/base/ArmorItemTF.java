package net.xXinailXx.thirteen_flames.item.base;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.xXinailXx.thirteen_flames.utils.FireItemSetting;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ArmorItemTF extends FireItemSetting implements Wearable {
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            return ArmorItem.dispenseArmor(source, stack) ? stack : super.execute(source, stack);
        }
    };
    protected final EquipmentSlot slot;
    private final int defense;
    private final float toughness;
    protected final float knockbackResistance;
    protected final ArmorMaterial material;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    @Override
    public @org.jetbrains.annotations.Nullable RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("teleport", RelicAbilityEntry.builder().maxLevel(2).stat("range", RelicAbilityStat.builder().initialValue(1, 3).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 100)).build();
    }

    public ArmorItemTF(ArmorMaterial material, EquipmentSlot slot, Item.Properties properties) {
        super(properties.defaultDurability(material.getDurabilityForSlot(slot)));
        this.material = material;
        this.slot = slot;
        this.defense = material.getDefenseForSlot(slot);
        this.toughness = material.getToughness();
        this.knockbackResistance = material.getKnockbackResistance();
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", (double) this.defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", (double) this.toughness, AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", (double) this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }

        this.defaultModifiers = builder.build();
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    public int getEnchantmentValue() {
        return this.material.getEnchantmentValue();
    }

    public ArmorMaterial getMaterial() {
        return this.material;
    }

    public boolean isValidRepairItem(ItemStack p_40392_, ItemStack p_40393_) {
        return this.material.getRepairIngredient().test(p_40393_) || super.isValidRepairItem(p_40392_, p_40393_);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(itemstack);
        ItemStack itemstack1 = player.getItemBySlot(equipmentslot);

        if (itemstack1.isEmpty()) {
            player.setItemSlot(equipmentslot, itemstack.copy());
            if (!level.isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(this));
            }

            itemstack.setCount(0);
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == this.slot ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public int getDefense() {
        return this.defense;
    }

    public float getToughness() {
        return this.toughness;
    }

    @Nullable
    public SoundEvent getEquipSound() {
        return this.getMaterial().getEquipSound();
    }
}
