package net.xXinailXx.thirteen_flames.client.gui.button.abilities.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbilityData {
    private String abilityName;

    public static AbilityDataBuilder builder(String id) {
        AbilityDataBuilder builder = new AbilityDataBuilder();

        builder.id(id);

        return builder;
    }

/// Sets which leveling menu it will be in.
    @Builder.Default
    private AbstarctAbilityWidgets.ScreenID screenID = IAbilityData.ScreenID.MINING;
/// By setting the value to zero, when an ability is purchased, it will not require improvement.
    @Builder.Default
    private int maxLevel = 1;
/// Sets the required menu level to open the ability.
    @Builder.Default
    private int requiredLevel = 0;
/// Sets the required number of scarabs to open (the type of scarab depends on the selected menu).
    @Builder.Default
    private int requiredScarabsForOpen = 1;
/// Sets the required number of scarabs for improvement (the type of scarab depends on the selected menu).
    @Builder.Default
    private int requiredScarabsForUpgrade = 1;

    public static class AbilityDataBuilder {
        private AbilityDataBuilder id(String abilityName) {
            this.abilityName = abilityName;

            return this;
        }
    }
}
