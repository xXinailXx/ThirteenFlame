package net.xXinailXx.thirteen_flames.item.lights;

import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.xXinailXx.thirteen_flames.utils.FireItemSetting;

public class StaffJodah extends FireItemSetting {
    public StaffJodah(Properties properties) {
        super( properties );
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
}
