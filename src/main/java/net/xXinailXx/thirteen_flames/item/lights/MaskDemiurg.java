package net.xXinailXx.thirteen_flames.item.lights;

import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.xXinailXx.thirteen_flames.item.base.ArmorItemTF;
import org.jetbrains.annotations.Nullable;

public class MaskDemiurg extends ArmorItemTF {
    public MaskDemiurg(ArmorMaterial material, EquipmentSlot slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("teleport", RelicAbilityEntry.builder().maxLevel(2).stat("range", RelicAbilityStat.builder().initialValue(1, 3).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 100)).build();
    }
}
