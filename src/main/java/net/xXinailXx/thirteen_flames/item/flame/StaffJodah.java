package net.xXinailXx.thirteen_flames.item.flame;

import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.thirteen_flames.item.base.tools.SwordItemTF;
import net.xXinailXx.thirteen_flames.item.base.tools.ToolTierTF;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

public class StaffJodah extends SwordItemTF {
    public StaffJodah() {
        super(ToolTierTF.THIRTEEN_FLAMES, 3, -1.4f);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("rage", RelicAbilityEntry.builder().maxLevel(10).stat("procent", RelicAbilityStat.builder().initialValue(1, 10).thresholdValue(1, 50).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("heal", RelicAbilityStat.builder().initialValue(5, 25).thresholdValue(5, 85).upgradeModifier(RelicAbilityStat.Operation.ADD, 5).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).build()).ability("backlight", RelicAbilityEntry.builder().maxLevel(5).stat("durability", RelicAbilityStat.builder().initialValue(0.25, 0.75).upgradeModifier(RelicAbilityStat.Operation.ADD, 0.25).formatValue((value) -> {
            return MathUtils.round(value, 2);
        }).build()).stat("cooldown", RelicAbilityStat.builder().initialValue(60, 40).thresholdValue(60, 10).upgradeModifier(RelicAbilityStat.Operation.ADD, -5).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 10, 200)).build();
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 1, 0));
    }
}
