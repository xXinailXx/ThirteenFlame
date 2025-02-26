package net.xXinailXx.thirteen_flames.entity.client.armor;

import net.xXinailXx.thirteen_flames.item.MaskSalmana;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MaskSalmanaRender extends GeoArmorRenderer<MaskSalmana> {
    public MaskSalmanaRender() {
        super(new MaskSalmanaModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftBootBone = "armorRightBoot";
    }
}
