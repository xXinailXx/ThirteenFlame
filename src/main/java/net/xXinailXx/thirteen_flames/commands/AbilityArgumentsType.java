package net.xXinailXx.thirteen_flames.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class AbilityArgumentsType implements ArgumentType<String> {
    public static AbilityArgumentsType abilityType() {
        return new AbilityArgumentsType();
    }

    public static String getAbility(final CommandContext<?> context, final String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }
}
