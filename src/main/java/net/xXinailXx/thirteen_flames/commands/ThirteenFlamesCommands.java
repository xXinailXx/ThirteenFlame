package net.xXinailXx.thirteen_flames.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import daripher.skilltree.capability.skill.IPlayerSkills;
import daripher.skilltree.capability.skill.PlayerSkillsProvider;
import daripher.skilltree.network.NetworkDispatcher;
import daripher.skilltree.network.message.SyncPlayerSkillsMessage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.command.EnumArgument;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityStorage;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.IAbilityData;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IStaminaData;
import net.xXinailXx.thirteen_flames.data.StaminaData;
import net.xXinailXx.thirteen_flames.utils.ScarabsType;

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
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(context -> {
                                                       String ability = AbilityArgumentsType.getAbility(context, "ability_id");
                                                       boolean value = BoolArgumentType.getBool(context, "value");

                                                       abilityData.setLockAbility(ability, value);
                                                       context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability_set_lock_message", value));

                                                       return Command.SINGLE_SUCCESS;
                                                })
                                        ))
                                .then(Commands.literal("buy")
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(context -> {
                                                        String ability = AbilityArgumentsType.getAbility(context, "ability_id");
                                                        boolean value = BoolArgumentType.getBool(context, "value");

                                                        abilityData.setBuyAbility(ability, value);
                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability_set_buy_message", value));

                                                        return Command.SINGLE_SUCCESS;
                                                })
                                        ))
                                .then(Commands.literal("active")
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(context -> {
                                                        String ability = AbilityArgumentsType.getAbility(context, "ability_id");
                                                        boolean value = BoolArgumentType.getBool(context, "value");

                                                        abilityData.setActiveAbility(ability, value);
                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability_set_active_message", value));

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

                                                                switch (type) {
                                                                    case add -> abilityData.addLevelAbility(ability, count);
                                                                    case set -> abilityData.setLevelAbility(ability, count);
                                                                    case take -> abilityData.addLevelAbility(ability, -count);
                                                                }

                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability_" + type.name() + "_message", count));

                                                                return Command.SINGLE_SUCCESS;
                                                         })
                                                )
                                        )
                                )))
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
                                        })))
                        .then(Commands.literal("set")
                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                        .executes(context -> {
                                                ServerPlayer player = context.getSource().getPlayerOrException();
                                                int count = IntegerArgumentType.getInteger(context, "count");

                                                staminaData.setStamina(player, count);

                                                return Command.SINGLE_SUCCESS;
                                        })
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
                                                            case mining -> guiLevelingData.addGuiMiningLevelAmount(count);
                                                            case craft -> guiLevelingData.addGuiCraftLevelAmount(count);
                                                            case fight -> guiLevelingData.addGuiFightLevelAmount(count);
                                                            case health -> guiLevelingData.addGuiHealthLevelAmount(count);
                                                        }

                                                        return Command.SINGLE_SUCCESS;
                                                })
                                        ))
                                .then(Commands.literal("set")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                        ScreenType type = context.getArgument("screen_type", ScreenType.class);
                                                        int count = IntegerArgumentType.getInteger(context, "count");

                                                        switch (type) {
                                                            case mining -> guiLevelingData.setGuiMiningLevelAmount(count);
                                                            case craft -> guiLevelingData.setGuiCraftLevelAmount(count);
                                                            case fight -> guiLevelingData.setGuiFightLevelAmount(count);
                                                            case health -> guiLevelingData.setGuiHealthLevelAmount(count);
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
                .then(Commands.literal("scarabs")
                        .then(Commands.argument("scarabs_type", EnumArgument.enumArgument(ScarabsType.class))
                                .then(Commands.literal("add")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                        ServerPlayer player = context.getSource().getPlayerOrException();
                                                        ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                        int count = IntegerArgumentType.getInteger(context, "count");

                                                        switch (type) {
                                                            case SILVER:
                                                                IPlayerSkills skillsData = PlayerSkillsProvider.get(player);

                                                                skillsData.grantSkillPoints(count);
                                                                NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerSkillsMessage(player));
                                                                break;
                                                            case GOLD:
                                                                scarabsData.addScarabGold(count);
                                                                break;
                                                            case AURITEH:
                                                                scarabsData.addScarabAuriteh(count);
                                                                break;
                                                            case LAZOTEP:
                                                                scarabsData.addScarabLazotep(count);
                                                                break;
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
                                                            case SILVER:
                                                                IPlayerSkills skillsData = PlayerSkillsProvider.get(player);

                                                                skillsData.grantSkillPoints(-count);
                                                                NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerSkillsMessage(player));
                                                                break;
                                                            case GOLD:
                                                                scarabsData.addScarabGold(-count);
                                                                break;
                                                            case AURITEH:
                                                                scarabsData.addScarabAuriteh(-count);
                                                                break;
                                                            case LAZOTEP:
                                                                scarabsData.addScarabLazotep(-count);
                                                                break;
                                                        }

                                                        return Command.SINGLE_SUCCESS;
                                                })
                                        ))
                                .then(Commands.literal("set")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                        ServerPlayer player = context.getSource().getPlayerOrException();
                                                        ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                        int count = IntegerArgumentType.getInteger(context, "count");

                                                        switch (type) {
                                                            case SILVER:
                                                                IPlayerSkills skillsData = PlayerSkillsProvider.get(player);

                                                                skillsData.setSkillPoints(Math.max(0, count));
                                                                NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerSkillsMessage(player));
                                                                break;
                                                            case GOLD:
                                                                scarabsData.setScarabGold(count);
                                                                break;
                                                            case AURITEH:
                                                                scarabsData.setScarabAuriteh(count);
                                                                break;
                                                            case LAZOTEP:
                                                                scarabsData.setScarabLazotep(count);
                                                                break;
                                                        }

                                                        return Command.SINGLE_SUCCESS;
                                                })
                                        )))
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

    private static IAbilityData getAbilityData(String abilityName) {
        for (IAbilityData data : AbilityStorage.ABILITIES.keySet()) {
            if (data.getAbilityData().equals(abilityName))
                return data;
        }

        return null;
    }

    private enum CommandIntegerActionType {
        add,
        set,
        take
    }

    private enum ScreenType {
        mining,
        craft,
        fight,
        health
    }
}
