package net.xXinailXx.thirteen_flames.item.lights;

import it.hurts.sskirillss.relics.client.tooltip.base.RelicStyleData;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.utils.FireItemSetting;

public class SunSeleaset extends FireItemSetting {
    private static int points;
    private static int pointsAbility;

    public SunSeleaset(Properties properties) {
        super(properties);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData( RelicAbilityData.builder().ability("blessed_light", RelicAbilityEntry.builder().maxLevel(10).stat("radius", RelicAbilityStat.builder().initialValue(10.0, 10.0).upgradeModifier(RelicAbilityStat.Operation.ADD, -1.0).formatValue((value) -> {
            return (int)MathUtils.round(value, 0);
        }).build()).stat("cooldown", RelicAbilityStat.builder().initialValue(10.0, 10.0).upgradeModifier(RelicAbilityStat.Operation.ADD, -1.0).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(200, 10, 100)).styleData( RelicStyleData.builder().borders("#40D42F", "#35D922").build()).build();
    }

    @Override
    public InteractionResult useOn(UseOnContext onContext) {
        BlockPos pos = onContext.getClickedPos();
        Level level = onContext.getLevel();
        Player player = onContext.getPlayer();
        setPoints(LevelingUtils.getPoints(this.getDefaultInstance()));
        setPointsAbility(AbilityUtils.getAbilityPoints(this.getDefaultInstance(), "blessed_light"));
        level.setBlock(pos.above(), BlockRegistry.SUN_SELIASET_BLOCK.get().defaultBlockState(), 3);
        player.setItemSlot( EquipmentSlot.MAINHAND, Items.AIR.getDefaultInstance());
        return super.useOn( onContext );
    }

    public static int getPoints() {
        return points;
    }

    public static void setPoints(int points) {
        SunSeleaset.points = points;
    }

    public static int getPointsAbility() {
        return pointsAbility;
    }

    public static void setPointsAbility(int pointsAbility) {
        SunSeleaset.pointsAbility = pointsAbility;
    }
}
