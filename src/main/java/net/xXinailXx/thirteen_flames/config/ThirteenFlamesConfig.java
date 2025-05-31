package net.xXinailXx.thirteen_flames.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ThirteenFlamesConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> TIME_TO_FLAME_UPGRADE;
    public static final ForgeConfigSpec.ConfigValue<String> NARSAFAR_GENDER;
    public static final ForgeConfigSpec.ConfigValue<String> NARSAFAR_PROFFESION;
    public static final ForgeConfigSpec.ConfigValue<String> NARSAFAR_OWNER;
    public static final ForgeConfigSpec.ConfigValue<Integer> NARSAFAR_SERIES_INT;
    public static final ForgeConfigSpec.ConfigValue<String> NARSAFAR_SERIES_STRING;

    static {
        BUILDER.push("Config for Storytelling: Thirteen Flames");

        TIME_TO_FLAME_UPGRADE = BUILDER.comment("Sets the time after which the flame can be updated.")
                .comment("Default value 240000 ticks - 10 game days.")
                .define("time_upgrade", 240000);
        NARSAFAR_GENDER = BUILDER.comment("Specify the gender that will be written in narsafar.")
                .comment("It is recommended to write in abbreviated form.")
                .define("gender", "none");
        NARSAFAR_PROFFESION = BUILDER.comment("Specify the profession that will be written in narsafar.")
                .define("profession", "none");
        NARSAFAR_OWNER = BUILDER.comment("Please indicate your owner which will be written in narsafar.")
                .define("owner", "none");
        NARSAFAR_SERIES_INT = BUILDER.comment("Please provide the number that will be written in the narsafar.")
                .defineInRange("series_int", 993, 100, 999);
        NARSAFAR_SERIES_STRING = BUILDER.comment("Specify the series that will be written in narsafar.")
                .define("series_str", "JDH");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
