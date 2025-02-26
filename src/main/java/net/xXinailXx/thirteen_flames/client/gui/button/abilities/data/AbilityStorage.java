package net.xXinailXx.thirteen_flames.client.gui.button.abilities.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityStorage {
    public static final Map<IAbilityData, AbilityData> ABILITY = new HashMap<>();
    public static List<AbstarctAbilityWidgets> abilities = getAbilitiesList();

    private static @NotNull List<AbstarctAbilityWidgets> getAbilitiesList() {
        List<AbstarctAbilityWidgets> cache = new ArrayList<>();

        ABILITY.keySet().forEach(iAbility -> {
            AbstarctAbilityWidgets widgets = (AbstarctAbilityWidgets) iAbility;
            cache.add(widgets);
        });

        return cache;
    }
}
