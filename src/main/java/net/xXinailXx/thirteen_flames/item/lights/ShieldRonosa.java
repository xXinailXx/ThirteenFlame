package net.xXinailXx.thirteen_flames.item.lights;

import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.xXinailXx.thirteen_flames.utils.FireItemSetting;

import javax.annotation.Nullable;
import java.util.List;

public class ShieldRonosa extends FireItemSetting {
    public static final int EFFECTIVE_BLOCK_DELAY = 5;
    public static final float MINIMUM_DURABILITY_DAMAGE = 3.0F;
    public static final String TAG_BASE_COLOR = "Base";

    public ShieldRonosa(Properties properties) {
        super( properties );
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData( RelicAbilityData.builder().ability("ejection", RelicAbilityEntry.builder().maxLevel(15).stat("cooldown", RelicAbilityStat.builder().initialValue(56.0, 35.0).upgradeModifier(RelicAbilityStat.Operation.ADD, -2.0).formatValue((value) -> {
            return (int) MathUtils.round(value, 0);
        }).build()).stat("radius", RelicAbilityStat.builder().initialValue(6.0, 11.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 1);
        }).build()).build()).ability("digging", RelicAbilityEntry.builder().maxLevel(2).stat("mining", RelicAbilityStat.builder().initialValue(1.0, 1.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 15, 100)).styleData( RelicStyleData.builder().borders("#40D42F", "#35D922").build()).build();
    }

    public String getDescriptionId(ItemStack p_43109_) {
        return BlockItem.getBlockEntityData(p_43109_) != null ? this.getDescriptionId() + "." + getColor(p_43109_).getName() : super.getDescriptionId(p_43109_);
    }

    public void appendHoverText(ItemStack p_43094_, @Nullable Level p_43095_, List<Component> p_43096_, TooltipFlag p_43097_) {
        BannerItem.appendHoverTextFromBannerBlockEntityTag(p_43094_, p_43096_);
    }

    public UseAnim getUseAnimation(ItemStack p_43105_) {
        return UseAnim.BLOCK;
    }

    public int getUseDuration(ItemStack p_43107_) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level p_43099_, Player p_43100_, InteractionHand p_43101_) {
        ItemStack itemstack = p_43100_.getItemInHand(p_43101_);
        p_43100_.startUsingItem(p_43101_);
        return InteractionResultHolder.consume(itemstack);
    }

    public static DyeColor getColor(ItemStack p_43103_) {
        CompoundTag compoundtag = BlockItem.getBlockEntityData(p_43103_);
        return compoundtag != null ? DyeColor.byId(compoundtag.getInt("Base")) : DyeColor.WHITE;
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }
}
