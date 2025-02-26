package net.xXinailXx.thirteen_flames.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityStorage;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IStaminaData;
import net.xXinailXx.thirteen_flames.data.StaminaData;

public class ThirteenFlamesCommands {
    private static final IData.IAbilitiesData abilityData = new Data.AbilitiesData.Utils();
    private static final IStaminaData staminaData = new StaminaData.Utils();
    private static final IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData();
    private static final IData.IEffectData effectData = new Data.EffectData();
    private static final IData.IXpScarabsData xpScarabsData = new Data.XpScarabsData();
    private static final IData.IScarabsData scarabsData = new Data.ScarabsData();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("thirteen_flames").requires(sender -> sender.hasPermission(2))
                .then(Commands.literal("ability")
                        .then(Commands.argument("ability_id", AbilityArgumentsType.abilityType())
                                .then(Commands.literal("lock")
                                        .executes(context -> {
                                            String ability = AbilityArgumentsType.getAbility(context, "ability_id");

                                            abilityData.setBuyAbility(ability, false);

                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.literal("buy")
                                        .executes(context -> {
                                            String ability = AbilityArgumentsType.getAbility(context, "ability_id");

                                            abilityData.setBuyAbility(ability, true);

                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.literal("active")
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(context -> {
                                                    String ability = AbilityArgumentsType.getAbility(context, "ability_id");
                                                    boolean value = BoolArgumentType.getBool(context, "value");

                                                    abilityData.setActiveAbility(ability, value);

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        ))
                                .then(Commands.literal("level")
                                        .then(Commands.argument("action", EnumArgument.enumArgument(CommandIntegerActionType.class))
                                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                            String ability = AbilityArgumentsType.getAbility(context, "ability_id");

                                                            if (!abilityData.isBuyAbility(ability)) {
                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability_lock_message"));

                                                                return Command.SINGLE_SUCCESS;
                                                            }

                                                            CommandIntegerActionType type = context.getArgument("action", CommandIntegerActionType.class);
                                                            int count = IntegerArgumentType.getInteger(context, "count");

                                                            AbilityStorage.abilities.forEach(abil -> {
                                                                if (abil.getAbilityData().getAbilityName().equals(ability)) {
                                                                    switch (type) {
                                                                        case add -> abilityData.addLevelAbility(ability, count, abil.getAbilityData().getMaxLevel());
                                                                        case set -> abilityData.setLevelAbility(ability, count);
                                                                        case take -> abilityData.addLevelAbility(ability, -count, abil.getAbilityData().getMaxLevel());
                                                                    }
                                                                }
                                                            });

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                )
                        ))
                .then(Commands.literal("curse_knef")
                        .then(Commands.argument("active", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean active = BoolArgumentType.getBool(context, "active");

                                    effectData.setCurseKnef(active);
                                    guiLevelingData.setProcentCurse(70);
                                    xpScarabsData.setXpScarabSilver(500 + guiLevelingData.getProcentCurse() * 10);
                                    xpScarabsData.setXpScarabGold(1000 + guiLevelingData.getProcentCurse() * 20);
                                    xpScarabsData.setXpScarabAuriteh(1500 + guiLevelingData.getProcentCurse() * 30);
                                    xpScarabsData.setXpScarabLazotep(2000 + guiLevelingData.getProcentCurse() * 40);

                                    return Command.SINGLE_SUCCESS;
                                })
                        ))
                .then(Commands.literal("stamina")
                        .then(Commands.literal("add")
                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            int count = IntegerArgumentType.getInteger(context, "count");

                                            staminaData.addStamina(player, count);

                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.literal("level")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                                    int count = IntegerArgumentType.getInteger(context, "count");

                                                    staminaData.addMaxStamina(player, count);

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("set")
                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            int count = IntegerArgumentType.getInteger(context, "count");

                                            staminaData.setStamina(player, count);

                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.literal("level")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                                    int count = IntegerArgumentType.getInteger(context, "count");

                                                    staminaData.setMaxStamina(player, count);

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        ))
                .then(Commands.literal("gui_leveling")
                        .then(Commands.argument("screen_type", EnumArgument.enumArgument(ScreenType.class))
                                .then(Commands.literal("add")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ScreenType type = context.getArgument("screen_type", ScreenType.class);
                                                    int count = IntegerArgumentType.getInteger(context, "count");

                                                    switch (type) {
                                                        case MINING -> guiLevelingData.addGuiMiningLevelAmount(count);
                                                        case CRAFT -> guiLevelingData.addGuiCraftLevelAmount(count);
                                                        case FIGHT -> guiLevelingData.addGuiFightLevelAmount(count);
                                                        case HEALTH -> guiLevelingData.addGuiHealthLevelAmount(count);
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                }))
                                )
                                .then(Commands.literal("set")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ScreenType type = context.getArgument("screen_type", ScreenType.class);
                                                    int count = IntegerArgumentType.getInteger(context, "count");

                                                    switch (type) {
                                                        case MINING -> guiLevelingData.setGuiMiningLevelAmount(count);
                                                        case CRAFT -> guiLevelingData.setGuiCraftLevelAmount(count);
                                                        case FIGHT -> guiLevelingData.setGuiFightLevelAmount(count);
                                                        case HEALTH -> guiLevelingData.setGuiHealthLevelAmount(count);
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                }))
                                )
                        ))
                .then(Commands.literal("xp_scarabs")
                        .then(Commands.argument("scarabs_type", EnumArgument.enumArgument(ScarabsType.class))
                                .then(Commands.literal("take")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                    int count = IntegerArgumentType.getInteger(context, "count");

                                                    switch (type) {
                                                        case SILVER -> xpScarabsData.subXpScarabsSilver(count);
                                                        case GOLD -> xpScarabsData.subXpScarabsGold(count);
                                                        case AURITEH -> xpScarabsData.subXpScarabsAuriteh(count);
                                                        case LAZOTEP -> xpScarabsData.subXpScarabsLazotep(count);
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                                .then(Commands.literal("set")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                    int count = IntegerArgumentType.getInteger(context, "count");

                                                    switch (type) {
                                                        case SILVER -> xpScarabsData.setXpScarabSilver(count);
                                                        case GOLD -> xpScarabsData.setXpScarabGold(count);
                                                        case AURITEH -> xpScarabsData.setXpScarabAuriteh(count);
                                                        case LAZOTEP -> xpScarabsData.setXpScarabLazotep(count);
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )))
                        .then(Commands.literal("reset")
                                .executes(context -> {
                                    xpScarabsData.setXpScarabSilver(500 + guiLevelingData.getProcentCurse() * 10);
                                    xpScarabsData.setXpScarabGold(1000 + guiLevelingData.getProcentCurse() * 20);
                                    xpScarabsData.setXpScarabAuriteh(1500 + guiLevelingData.getProcentCurse() * 30);
                                    xpScarabsData.setXpScarabLazotep(2000 + guiLevelingData.getProcentCurse() * 40);

                                    return Command.SINGLE_SUCCESS;
                                }))
                .then(Commands.literal("scarabs")
                        .then(Commands.argument("scarabs_type", EnumArgument.enumArgument(ScarabsType.class))
                                .then(Commands.literal("add")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                                    ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                    int count = IntegerArgumentType.getInteger(context, "count");

                                                    switch (type) {
                                                        case SILVER -> scarabsData.addScarabSilver(player, count);
                                                        case GOLD -> scarabsData.addScarabGold(count);
                                                        case AURITEH -> scarabsData.addScarabAuriteh(count);
                                                        case LAZOTEP -> scarabsData.addScarabLazotep(count);
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        ))
                                .then(Commands.literal("take")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                                    ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                    int count = IntegerArgumentType.getInteger(context, "count");

                                                    switch (type) {
                                                        case SILVER -> scarabsData.addScarabSilver(player, -count);
                                                        case GOLD -> scarabsData.addScarabGold(-count);
                                                        case AURITEH -> scarabsData.addScarabAuriteh(-count);
                                                        case LAZOTEP -> scarabsData.addScarabLazotep(-count);
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        ))
                                .then(Commands.literal("set")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                                    ScarabsType type = context.getArgument("screen_type", ScarabsType.class);
                                                    int count = IntegerArgumentType.getInteger(context, "count");

                                                    switch (type) {
                                                        case SILVER -> scarabsData.setScarabSilver(player, count);
                                                        case GOLD -> scarabsData.setScarabGold(count);
                                                        case AURITEH -> scarabsData.setScarabAuriteh(count);
                                                        case LAZOTEP -> scarabsData.setScarabLazotep(count);
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        ))
                                )
                        .then(Commands.literal("reset")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();

                                    scarabsData.resetScarabs(player);

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }

    private enum CommandIntegerActionType {
        add,
        set,
        take
    }

    private enum ScreenType {
        MINING,
        CRAFT,
        FIGHT,
        HEALTH
    }

    private enum ScarabsType {
        SILVER,
        GOLD,
        AURITEH,
        LAZOTEP
    }
}
