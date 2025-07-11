package net.xXinailXx.thirteen_flames.client.gui.god_pharaoh;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.healt.*;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class GodPharaohScreenHealth extends AbstractGuiPharaoh {
    public GodPharaohScreenHealth() {
        super(ScreenID.HEALTH);
    }

    public void tick() {
        levelGui = guiLeveling.getHealthLevel(Minecraft.getInstance().player);
    }

    protected void init() {
        super.init();

        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        int X = x + 90;
        int Y = y + 71;
        int xOff = 37;
        int yOff = 37;

        this.addRenderableWidget(new Overcoming(X, Y));
        this.addRenderableWidget(new SecretSurvival(X + xOff, Y));
        this.addRenderableWidget(new DeepKinship(X + (xOff * 2), Y));
        this.addRenderableWidget(new Resistance(X + (xOff * 3), Y));
        this.addRenderableWidget(new Metabolism(X + (xOff * 4), Y));

        this.addRenderableWidget(new CelestialKinship(X, Y + yOff));
        this.addRenderableWidget(new StaminaMantra(X + xOff, Y + yOff));
        this.addRenderableWidget(new SecondWind(X + (xOff * 2), Y + yOff));
        this.addRenderableWidget(new GraceHeaven(X + (xOff * 3), Y + yOff));
        this.addRenderableWidget(new Diet(X + (xOff * 4), Y + yOff));

        this.addRenderableWidget(new ConquerorDunes(X, Y + (yOff * 2)));
        this.addRenderableWidget(new TerrestrialKinship(X + xOff, Y + (yOff * 2)));
        this.addRenderableWidget(new SwordSwallower(X + (xOff * 2), Y + (yOff * 2)));
    }
}
