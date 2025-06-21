package net.xXinailXx.thirteen_flames.item.flame;

import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityData;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityEntry;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicAbilityStat;
import it.hurts.sskirillss.relics.items.relics.base.data.leveling.RelicLevelingData;
import it.hurts.sskirillss.relics.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.init.KeyBindingRegistry;
import net.xXinailXx.thirteen_flames.item.base.ArmorItemTF;
import net.xXinailXx.thirteen_flames.network.packet.UseMaskDemiurgPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

@Mod.EventBusSubscriber
public class MaskDemiurg extends ArmorItemTF {
    public MaskDemiurg() {
        super(ArmorMaterials.NETHERITE, EquipmentSlot.HEAD);
    }

    public RelicData getRelicData() {
        return RelicData.builder().abilityData(RelicAbilityData.builder().ability("dematerialization", RelicAbilityEntry.builder().maxLevel(10).stat("time", RelicAbilityStat.builder().initialValue(1, 6).thresholdValue(1, 20).upgradeModifier(RelicAbilityStat.Operation.ADD, 1).formatValue((value) -> {
            return MathUtils.round(value, 1);
        }).build()).stat("cooldown", RelicAbilityStat.builder().initialValue(60, 30).thresholdValue(5, 60).upgradeModifier(RelicAbilityStat.Operation.ADD, -2).formatValue((value) -> {
            return MathUtils.round(value, 0);
        }).build()).build()).build()).levelingData(new RelicLevelingData(100, 10, 100)).build();
    }

    protected Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting() {
        return new Pair<>(new Tuple3<>(1F, 1F, 1F), new Vec3(0, 0, 0));
    }

    @SubscribeEvent
    public static void useMaskDemiurg(InputEvent event) {
        if (Minecraft.getInstance().player == null)
            return;

        if (KeyBindingRegistry.USE_MASK.isDown())
            Network.sendToServer(new UseMaskDemiurgPacket());
    }
}
