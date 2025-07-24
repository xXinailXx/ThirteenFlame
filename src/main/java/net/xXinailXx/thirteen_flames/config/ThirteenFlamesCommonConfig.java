package net.xXinailXx.thirteen_flames.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ThirteenFlamesCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> TIME_TO_FLAME_UPGRADE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> STAMINA_ACTIVE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FLAME_UPGRADE_REQ_TYPE;

    static {
        BUILDER.push("Config for Storytelling: Thirteen Flames");

        TIME_TO_FLAME_UPGRADE = BUILDER.comment("Sets the time after which the flame can be updated.")
                .comment("Default value 240000 ticks - 10 game days.")
                .define("time_upgrade", 240000);
        STAMINA_ACTIVE = BUILDER.comment("Sets whether stamina will work. If disabled, some of God-Pharaoh's abilities will not work.")
                .define("stamina_active", true);
        FLAME_UPGRADE_REQ_TYPE = BUILDER.comment("Sets whether flame improvement will depend on fire type.")
                .define("req", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
