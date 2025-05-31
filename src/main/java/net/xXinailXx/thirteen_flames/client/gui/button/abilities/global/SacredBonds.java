package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class SacredBonds extends AbstarctAbilityWidgets {
    public SacredBonds(int x, int y) {
        super(x, y, 6 + (effectData.isCurseKnef() ? 1 : 0));
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("sacred_bonds").screenID(ScreenID.GLOBAL).build();
    }

    @SubscribeEvent
    public static void tameAnimal(PlayerInteractEvent.EntityInteract event) {
        if (data.isActiveAbility("sacred_bonds")) {
            if (event.getTarget() instanceof TamableAnimal animal) {
                if (!animal.isTame()) {
                    Level level = event.getLevel();

                    animal.tame(event.getEntity());

                    for (int i = 0; i < 7; ++ i) {
                        double d0 = level.random.nextGaussian() * 0.02D;
                        double d1 = level.random.nextGaussian() * 0.02D;
                        double d2 = level.random.nextGaussian() * 0.02D;
                        level.addParticle(ParticleTypes.HEART, event.getTarget().getRandomX(1.0D), event.getTarget().getRandomY() + 0.5D, event.getTarget().getRandomZ(1.0D), d0, d1, d2);
                    }
                }
            }
        }
    }
}
