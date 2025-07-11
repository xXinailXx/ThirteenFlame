package net.xXinailXx.thirteen_flames.item.base;

import it.hurts.sskirillss.relics.items.relics.base.RelicItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import net.xXinailXx.enderdragonlib.client.glow.Beam;
import net.xXinailXx.enderdragonlib.client.glow.GlowData;
import net.xXinailXx.enderdragonlib.interfaces.IGlow;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import org.zeith.hammerlib.util.java.tuples.Tuple3;
import oshi.util.tuples.Pair;

import java.awt.*;

public abstract class FlameItemSetting extends RelicItem implements IGlow {
    public FlameItemSetting(Properties properties) {
        super(properties.tab(ThirteenFlames.FLAME_TAB));
    }

    public FlameItemSetting() {
        this(new Properties());
    }

    public boolean isFireResistant() {
        return true;
    }

    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    public Rarity getRarity(ItemStack p_41461_) {
        return Rarity.RARE;
    }

    public int getMaxDamage(ItemStack stack) {
        short maxDurability;

        switch (stack.getRarity()) {
            case COMMON -> maxDurability = -1;
            case UNCOMMON -> maxDurability = -1;
            case RARE -> maxDurability = -1;
            case EPIC -> maxDurability = -1;
            default -> throw new IncompatibleClassChangeError();
        }

        return maxDurability;
    }

    protected abstract Pair<Tuple3<Float, Float, Float>, Vec3> beamSetting();

    public GlowData constructGlowData() {
        GlowData data = GlowData.builder(false).translateBeams(beamSetting().getB());

        if (this.getDefaultInstance().is(ItemRegistry.HAMMER_MONTU.get()) || this.getDefaultInstance().is(ItemRegistry.GLOVES_MONTU.get())) {
            data.addBeam(new Beam(new Color(255, 187, 0), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(255, 251, 0), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(162, 255, 0), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(111, 255, 0), 4, 1, 1F, beamSetting().getA()));
        } else if (this.getDefaultInstance().is(ItemRegistry.SWORD_RONOSA.get()) || this.getDefaultInstance().is(ItemRegistry.SHIELD_RONOSA.get())) {
            data.addBeam(new Beam(new Color(255, 0, 0), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(214, 3, 30), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(168, 13, 13), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(151, 2, 2), 4, 1, 1F, beamSetting().getA()));
        } else if (this.getDefaultInstance().is(ItemRegistry.MOON_BOW.get()) || this.getDefaultInstance().is(ItemRegistry.BLACK_ROSE.get())) {
            data.addBeam(new Beam(new Color(0, 224, 255), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(0, 67, 255), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(0, 34, 177), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(38, 0, 172), 4, 1, 1F, beamSetting().getA()));
        } else if (this.getDefaultInstance().is(ItemRegistry.SUN_SELIASET.get()) || this.getDefaultInstance().is(ItemRegistry.HORN_SELIASET.get())) {
            data.addBeam(new Beam(new Color(255, 145, 0), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(255, 115, 0), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(232, 81, 0), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(255, 77, 0), 4, 1, 1F, beamSetting().getA()));
        } else if (this.getDefaultInstance().is(ItemRegistry.SCROLL_HET.get()) || this.getDefaultInstance().is(ItemRegistry.FLIGHT_HET.get())) {
            data.addBeam(new Beam(new Color(253, 251, 132), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(255, 249, 65), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(255, 221, 0), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(255, 166, 0), 4, 1, 1F, beamSetting().getA()));
        } else if (this.getDefaultInstance().is(ItemRegistry.TRAVELERS_SWORD.get()) || this.getDefaultInstance().is(ItemRegistry.STAFF_JODAH.get()) || this.getDefaultInstance().is(ItemRegistry.MASK_DEMIURG.get())) {
            data.addBeam(new Beam(new Color(0, 40, 243), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(1, 17, 197), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(48, 2, 234), 4, 1, 1F, beamSetting().getA()));
            data.addBeam(new Beam(new Color(80, 2, 191), 4, 1, 1F, beamSetting().getA()));
        }

        return data;
    }
}
