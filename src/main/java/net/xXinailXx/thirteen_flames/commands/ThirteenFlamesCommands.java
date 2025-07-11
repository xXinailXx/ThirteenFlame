package net.xXinailXx.thirteen_flames.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import daripher.skilltree.capability.skill.IPlayerSkills;
import daripher.skilltree.capability.skill.PlayerSkillsProvider;
import daripher.skilltree.network.NetworkDispatcher;
import daripher.skilltree.network.message.SyncPlayerSkillsMessage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.command.EnumArgument;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityStorage;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.IAbilityData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.IStaminaData;
import net.xXinailXx.thirteen_flames.data.StaminaData;
import net.xXinailXx.thirteen_flames.item.NarsafarItem;
import net.xXinailXx.thirteen_flames.utils.ScarabsType;

import java.util.Locale;

public class ThirteenFlamesCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("thirteen_flames").requires(sender -> sender.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.literal("ability")
                                .then(Commands.argument("ability_id", AbilityArgumentsType.abilityType())
                                        .then(Commands.literal("lock")
                                                .then(Commands.argument("value", BoolArgumentType.bool())
                                                        .executes(context -> {
                                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IAbilitiesData data = new Data.AbilitiesData.Utils();
                                                                String ability = AbilityArgumentsType.getAbility(context, "ability_id");
                                                                boolean value = BoolArgumentType.getBool(context, "value");

                                                                data.setLockAbility(player, ability, value);
                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability.set_lock_message", player.getGameProfile().getName(), ability, value));

                                                                return Command.SINGLE_SUCCESS;
                                                        })))
                                        .then(Commands.literal("buy")
                                                .then(Commands.argument("value", BoolArgumentType.bool())
                                                        .executes(context -> {
                                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IAbilitiesData data = new Data.AbilitiesData.Utils();
                                                                String ability = AbilityArgumentsType.getAbility(context, "ability_id");
                                                                boolean value = BoolArgumentType.getBool(context, "value");

                                                                data.setBuyAbility(player, ability, value);
                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability.set_buy_message", player.getGameProfile().getName(), ability, value));

                                                                return Command.SINGLE_SUCCESS;
                                                        })))
                                        .then(Commands.literal("active")
                                                .then(Commands.argument("value", BoolArgumentType.bool())
                                                        .executes(context -> {
                                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IAbilitiesData data = new Data.AbilitiesData.Utils();
                                                                String ability = AbilityArgumentsType.getAbility(context, "ability_id");
                                                                boolean value = BoolArgumentType.getBool(context, "value");

                                                                data.setActiveAbility(player, ability, value);
                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability.set_active_message", player.getGameProfile().getName(), ability, value));

                                                                return Command.SINGLE_SUCCESS;
                                                        })))
                                        .then(Commands.literal("level")
                                                .then(Commands.argument("action", EnumArgument.enumArgument(CommandIntegerActionType.class))
                                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                                 .executes(context -> {
                                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                        IData.IAbilitiesData data = new Data.AbilitiesData.Utils();
                                                                        String ability = AbilityArgumentsType.getAbility(context, "ability_id");

                                                                        if (!data.isBuyAbility(player, ability) || data.isLockAbility(player, ability)) {
                                                                            context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability.level_action_error", player.getGameProfile().getName(), ability));
                                                                            return Command.SINGLE_SUCCESS;
                                                                        }

                                                                        CommandIntegerActionType type = context.getArgument("action", CommandIntegerActionType.class);
                                                                        int count = IntegerArgumentType.getInteger(context, "count");

                                                                        switch (type) {
                                                                            case add -> data.addLevelAbility(player, ability, count);
                                                                            case set -> data.setLevelAbility(player, ability, count);
                                                                            case take -> data.addLevelAbility(player, ability, -count);
                                                                        }

                                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".ability.level_action_" + type.name() + "_message", player.getGameProfile().getName(), ability, count));

                                                                        return Command.SINGLE_SUCCESS;
                                                                 }))))))
                        .then(Commands.literal("curse_knef")
                                .then(Commands.literal("active")
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(context -> {
                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                        IData.IEffectData effectData = new Data.EffectData.Utils();
                                                        boolean active = BoolArgumentType.getBool(context, "value");

                                                        effectData.setCurseKnef(player, active);
                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".curse_knef_set_active_message", player.getGameProfile().getName(), active));

                                                        return Command.SINGLE_SUCCESS;
                                                })))
                                .then(Commands.literal("set")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                        IData.IEffectData effectData = new Data.EffectData.Utils();
                                                        IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
                                                        IData.IAbilitiesData abilitiesData = new Data.AbilitiesData.Utils();
                                                        int count = Mth.clamp(IntegerArgumentType.getInteger(context, "count"), 0, 70);

                                                        if (count % 5 != 0)
                                                            count -= count % 5;

                                                        if (count == 0) {
                                                            if (effectData.isCurseKnef(player)) {
                                                                effectData.setCurseKnef(player, false);
                                                                guiLevelingData.setProcentCurse(player, 0);
                                                            }
                                                        } else {
                                                            if (!effectData.isCurseKnef(player)) {
                                                                effectData.setCurseKnef(player, true);
                                                            }

                                                            guiLevelingData.setProcentCurse(player, count);

                                                            if (count < 70) {
                                                                abilitiesData.setBuyAbility(player, "recovery", true);
                                                                abilitiesData.setActiveAbility(player, "recovery", true);
                                                                abilitiesData.setLevelAbility(player, "recovery", (70 - count) / 5);
                                                            }
                                                        }

                                                        return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("stamina")
                                .then(Commands.literal("add")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                        int count = IntegerArgumentType.getInteger(context, "count");

                                                        new StaminaData.Utils().addStamina(player, count);
                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".stamina.add_message", player.getGameProfile().getName(), count));

                                                        return Command.SINGLE_SUCCESS;
                                                }))
                                        .then(Commands.literal("full")
                                                .executes(context -> {
                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                        IStaminaData data = new StaminaData.Utils();

                                                        data.setStamina(player, data.getMaxStamina(player));
                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".stamina.full_message", player.getGameProfile().getName()));

                                                        return Command.SINGLE_SUCCESS;
                                                })))
                                .then(Commands.literal("take")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                        int count = IntegerArgumentType.getInteger(context, "count");

                                                        new StaminaData.Utils().addStamina(player, -count);
                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".stamina.take_message", player.getGameProfile().getName(), count));

                                                        return Command.SINGLE_SUCCESS;
                                                })))
                                .then(Commands.literal("set")
                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                        int count = IntegerArgumentType.getInteger(context, "count");

                                                        new StaminaData.Utils().setStamina(player, count);
                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".stamina.set_message", player.getGameProfile().getName(), count));

                                                        return Command.SINGLE_SUCCESS;
                                                }))
                                        .then(Commands.literal("full")
                                                .executes(context -> {
                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                        IStaminaData data = new StaminaData.Utils();

                                                        data.setStamina(player, data.getMaxStamina(player));
                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".stamina.full_message", player.getGameProfile().getName()));

                                                        return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("gui_leveling")
                                .then(Commands.argument("screen_type", EnumArgument.enumArgument(ScreenType.class))
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
                                                                ScreenType type = context.getArgument("screen_type", ScreenType.class);
                                                                int count = IntegerArgumentType.getInteger(context, "count");

                                                                switch (type) {
                                                                    case mining -> guiLevelingData.addMiningLevel(player, count);
                                                                    case craft -> guiLevelingData.addCraftLevel(player, count);
                                                                    case fight -> guiLevelingData.addFightLevel(player, count);
                                                                    case health -> guiLevelingData.addHealthLevel(player, count);
                                                                }

                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".gui_leveling.add_message", player.getGameProfile().getName(), count, type.name()));

                                                                return Command.SINGLE_SUCCESS;
                                                        })))
                                                .then(Commands.literal("take")
                                                        .then(Commands.argument("count", IntegerArgumentType.integer())
                                                                .executes(context -> {
                                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                        IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
                                                                        ScreenType type = context.getArgument("screen_type", ScreenType.class);
                                                                        int count = IntegerArgumentType.getInteger(context, "count");

                                                                        switch (type) {
                                                                            case mining -> guiLevelingData.setMiningLevel(player, Math.max(0, guiLevelingData.getMiningLevel(player) - count));
                                                                            case craft -> guiLevelingData.setCraftLevel(player, Math.max(0, guiLevelingData.getCraftLevel(player) - count));
                                                                            case fight -> guiLevelingData.setFightLevel(player, Math.max(0, guiLevelingData.getFightLevel(player) - count));
                                                                            case health -> guiLevelingData.setHealthLevel(player, Math.max(0, guiLevelingData.getHealthLevel(player) - count));
                                                                        }

                                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".gui_leveling.take_message", player.getGameProfile().getName(), count, type.name()));

                                                                        return Command.SINGLE_SUCCESS;
                                                                })))
                                        .then(Commands.literal("set")
                                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
                                                                ScreenType type = context.getArgument("screen_type", ScreenType.class);
                                                                int count = IntegerArgumentType.getInteger(context, "count");

                                                                switch (type) {
                                                                    case mining -> guiLevelingData.setMiningLevel(player, count);
                                                                    case craft -> guiLevelingData.setCraftLevel(player, count);
                                                                    case fight -> guiLevelingData.setFightLevel(player, count);
                                                                    case health -> guiLevelingData.setHealthLevel(player, count);
                                                                }

                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".gui_leveling.set_message", player.getGameProfile().getName(), count, type.name()));

                                                                return Command.SINGLE_SUCCESS;
                                                        })))))
                        .then(Commands.literal("xp_scarabs")
                                .then(Commands.argument("scarabs_type", EnumArgument.enumArgument(ScarabsType.class))
                                        .then(Commands.literal("take")
                                                 .then(Commands.argument("count", IntegerArgumentType.integer())
                                                         .executes(context -> {
                                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IXpScarabsData xpScarabsData = new Data.XpScarabsData.Utils();
                                                                ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                                int count = IntegerArgumentType.getInteger(context, "count");

                                                                switch (type) {
                                                                    case SILVER -> xpScarabsData.subXpScarabsSilver(player, count);
                                                                    case GOLD -> xpScarabsData.subXpScarabsGold(player, count);
                                                                    case AURITEH -> xpScarabsData.subXpScarabsAuriteh(player, count);
                                                                    case LAZOTEP -> xpScarabsData.subXpScarabsLazotep(player, count);
                                                                }

                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".xp_scarabs.take_message", player.getGameProfile().getName(), count, type.name().toLowerCase(Locale.ROOT)));

                                                                return Command.SINGLE_SUCCESS;
                                                         })))
                                        .then(Commands.literal("set")
                                                 .then(Commands.argument("count", IntegerArgumentType.integer())
                                                         .executes(context -> {
                                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IXpScarabsData xpScarabsData = new Data.XpScarabsData.Utils();
                                                                ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                                int count = IntegerArgumentType.getInteger(context, "count");

                                                                switch (type) {
                                                                    case SILVER -> xpScarabsData.setXpScarabSilver(player, count);
                                                                    case GOLD -> xpScarabsData.setXpScarabGold(player, count);
                                                                    case AURITEH -> xpScarabsData.setXpScarabAuriteh(player, count);
                                                                    case LAZOTEP -> xpScarabsData.setXpScarabLazotep(player, count);
                                                                }

                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".xp_scarabs.set_message", player.getGameProfile().getName(), count, type.name().toLowerCase(Locale.ROOT)));

                                                                return Command.SINGLE_SUCCESS;
                                                         })))))
                        .then(Commands.literal("scarabs")
                                .then(Commands.argument("scarabs_type", EnumArgument.enumArgument(ScarabsType.class))
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IScarabsData scarabsData = new Data.ScarabsData.Utils();
                                                                ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                                int count = IntegerArgumentType.getInteger(context, "count");

                                                                switch (type) {
                                                                    case SILVER:
                                                                        IPlayerSkills skillsData = PlayerSkillsProvider.get(player);

                                                                        skillsData.grantSkillPoints(count);
                                                                        NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerSkillsMessage(player));
                                                                        break;
                                                                    case GOLD:
                                                                        scarabsData.addScarabGold(player, count);
                                                                        break;
                                                                    case AURITEH:
                                                                        scarabsData.addScarabAuriteh(player, count);
                                                                        break;
                                                                    case LAZOTEP:
                                                                        scarabsData.addScarabLazotep(player, count);
                                                                        break;
                                                                }

                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".scarabs.add_message", player.getGameProfile().getName(), count, type.name().toLowerCase(Locale.ROOT)));

                                                                return Command.SINGLE_SUCCESS;
                                                        })))
                                        .then(Commands.literal("take")
                                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IScarabsData scarabsData = new Data.ScarabsData.Utils();
                                                                ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                                int count = IntegerArgumentType.getInteger(context, "count");

                                                                switch (type) {
                                                                    case SILVER:
                                                                        IPlayerSkills skillsData = PlayerSkillsProvider.get(player);

                                                                        skillsData.grantSkillPoints(-count);
                                                                        NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerSkillsMessage(player));
                                                                        break;
                                                                    case GOLD:
                                                                        scarabsData.addScarabGold(player, -count);
                                                                        break;
                                                                    case AURITEH:
                                                                        scarabsData.addScarabAuriteh(player, -count);
                                                                        break;
                                                                    case LAZOTEP:
                                                                        scarabsData.addScarabLazotep(player, -count);
                                                                        break;
                                                                }

                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".scarabs.take_message", player.getGameProfile().getName(), count, type.name().toLowerCase(Locale.ROOT)));

                                                                return Command.SINGLE_SUCCESS;
                                                        })))
                                        .then(Commands.literal("set")
                                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                IData.IScarabsData scarabsData = new Data.ScarabsData.Utils();
                                                                ScarabsType type = context.getArgument("scarabs_type", ScarabsType.class);
                                                                int count = IntegerArgumentType.getInteger(context, "count");

                                                                switch (type) {
                                                                    case SILVER:
                                                                        IPlayerSkills skillsData = PlayerSkillsProvider.get(player);

                                                                        skillsData.setSkillPoints(Math.max(0, count));
                                                                        NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerSkillsMessage(player));
                                                                        break;
                                                                    case GOLD:
                                                                        scarabsData.setScarabGold(player, count);
                                                                        break;
                                                                    case AURITEH:
                                                                        scarabsData.setScarabAuriteh(player, count);
                                                                        break;
                                                                    case LAZOTEP:
                                                                        scarabsData.setScarabLazotep(player, count);
                                                                        break;
                                                                }

                                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".scarabs.set_message", player.getGameProfile().getName(), count, type.name().toLowerCase(Locale.ROOT)));

                                                                return Command.SINGLE_SUCCESS;
                                                        }))))
                                .then(Commands.literal("reset")
                                        .executes(context -> {
                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                new Data.ScarabsData.Utils().resetScarabs(player);

                                                context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".scarabs.reset_message", player.getGameProfile().getName()));

                                                return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("nar-safar")
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .then(Commands.argument("gender", StringArgumentType.string())
                                                .then(Commands.argument("profession", StringArgumentType.string())
                                                        .then(Commands.argument("owner", StringArgumentType.string())
                                                                .then(Commands.argument("series_int", IntegerArgumentType.integer())
                                                                        .then(Commands.argument("series_str", StringArgumentType.string())
                                                                                .executes(context -> {
                                                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                                                        String name = StringArgumentType.getString(context, "name");
                                                                                        String gender = StringArgumentType.getString(context, "gender");
                                                                                        String profession = StringArgumentType.getString(context, "profession");
                                                                                        String owner = StringArgumentType.getString(context, "owner");
                                                                                        int series_int = IntegerArgumentType.getInteger(context, "series_int");
                                                                                        String series_str = StringArgumentType.getString(context, "series_str");

                                                                                        if (series_int < 0) {
                                                                                            context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".nar-safar.series_int_error_0"));
                                                                                            return 0;
                                                                                        } else if (series_int > 999) {
                                                                                            context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".nar-safar.series_int_error_1"));
                                                                                            return 0;
                                                                                        }

                                                                                        ItemStack stack = NarsafarItem.createNarsafar(name, gender, profession, owner, series_int, series_str);

                                                                                        if (player.getInventory().getFreeSlot() == -1)
                                                                                            player.drop(stack, false);
                                                                                        else
                                                                                            player.addItem(stack);

                                                                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable("command." + ThirteenFlames.MODID + ".nar-safar.create_message", player.getGameProfile().getName()));

                                                                                        return Command.SINGLE_SUCCESS;
                                                                                })
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static IAbilityData getAbilityData(String abilityName) {
        for (IAbilityData data : AbilityStorage.ABILITIES.keySet())
            if (data.getAbilityData().equals(abilityName))
                return data;

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
