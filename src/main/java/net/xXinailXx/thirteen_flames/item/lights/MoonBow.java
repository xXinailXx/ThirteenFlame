package net.xXinailXx.thirteen_flames.item.lights;

import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.xXinailXx.thirteen_flames.utils.FireItemSetting;

public class MoonBow extends FireItemSetting {
    public MoonBow(Properties properties) {
        super( properties );
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("shot", RelicAbilityEntry.builder().maxLevel(10).stat("rays", RelicAbilityStat.builder().initialValue((double)3.0F, (double)3.0F).thresholdValue((double)3.0F, (double)23.0F).upgradeModifier(RelicAbilityStat.Operation.ADD, (double)2.0F).formatValue((x) -> {
            return (int) MathUtils.round(x, 1);
        }).build()).stat("dmg", RelicAbilityStat.builder().initialValue((double)3.0F, (double)4.0F).thresholdValue((double)3.0F, (double)5.0F).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.1).formatValue((x) -> {
            return MathUtils.round(x, 1);
        }).build()).stat("drain", RelicAbilityStat.builder().initialValue((double)0.25F, 0.2).thresholdValue(0.05, (double)0.25F).upgradeModifier(RelicAbilityStat.Operation.ADD, -0.015).formatValue((x) -> {
            return MathUtils.round(x * (double)100.0F, 1);
        }).build()).build()).ability("swim", RelicAbilityEntry.builder().maxLevel(5).stat("speed", RelicAbilityStat.builder().initialValue((double)4.0F, (double)6.0F).thresholdValue((double)4.0F, (double)11.0F).upgradeModifier(RelicAbilityStat.Operation.ADD, (double)1.0F).formatValue((x) -> {
            return MathUtils.round(x, 1);
        }).build()).stat("dmg", RelicAbilityStat.builder().initialValue((double)6.0F, (double)8.0F).thresholdValue((double)6.0F, (double)20.0F).upgradeModifier(RelicAbilityStat.Operation.ADD, 2.4).formatValue((x) -> {
            return MathUtils.round(x, 1);
        }).build()).build()).ability("storm", RelicAbilityEntry.builder().requiredLevel(10).maxLevel(5).stat("radius", RelicAbilityStat.builder().initialValue((double)4.0F, (double)5.0F).thresholdValue((double)4.0F, (double)20.0F).upgradeModifier(RelicAbilityStat.Operation.ADD, (double)3.0F).formatValue((x) -> {
            return MathUtils.round(x, 1);
        }).build()).stat("dur", RelicAbilityStat.builder().initialValue((double)11.0F, (double)16.0F).thresholdValue((double)11.0F, (double)32.0F).upgradeModifier(RelicAbilityStat.Operation.ADD, (double)4.0F).formatValue((x) -> {
            return (int)MathUtils.round(x, 1);
        }).build()).stat("dmg", RelicAbilityStat.builder().initialValue((double)6.0F, (double)8.0F).thresholdValue((double)6.0F, (double)13.0F).upgradeModifier(RelicAbilityStat.Operation.ADD, (double)1.0F).formatValue((x) -> {
            return (int)MathUtils.round(x, 1);
        }).build()).stat("heal", RelicAbilityStat.builder().initialValue((double)2.0F, (double)3.0F).thresholdValue((double)2.0F, (double)10.0F).upgradeModifier(RelicAbilityStat.Operation.ADD, 1.4).formatValue((x) -> {
            return MathUtils.round(x, 1);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 20, 100)).styleData(RelicStyleData.builder().borders("#fffd75", "#ffbe00").build()).build();
    }
}
