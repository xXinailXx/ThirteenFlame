package net.xXinailXx.thirteen_flames.item.flame;

import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;

public class BlackRose extends FlameItemSetting {
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("flesh", RelicAbilityEntry.builder().maxLevel(10).stat("revival", RelicAbilityStat.builder().initialValue(5.0, 10.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.5).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).build()).ability("stillbirth", RelicAbilityEntry.builder().requiredLevel(0).maxLevel(5).stat("size", RelicAbilityStat.builder().initialValue(1.0, 5.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).stat("separation", RelicAbilityStat.builder().initialValue(5.0, 10.0).upgradeModifier(RelicAbilityStat.Operation.MULTIPLY_TOTAL, 1.0).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).stat("piece", RelicAbilityStat.builder().initialValue(1.0, 5.0).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.0).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).build()).ability("bonfire", RelicAbilityEntry.builder().requiredLevel(0).maxLevel(10).stat("resist", RelicAbilityStat.builder().initialValue(0.25, 0.25).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.25).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 20, 150)).styleData( RelicStyleData.builder().borders("#32a167", "#16702e").build()).build();
    }
}
