package net.xXinailXx.thirteen_flames.client.gui.god_pharaoh;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight.*;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class GodPharaohScreenFight extends AbstractGuiPharaoh {
    public GodPharaohScreenFight() {
        super(ScreenID.FIGHT);
    }

    @Override
    public void tick() {
        levelGui = guiLeveling.getGuiFightLevelAmount();
    }

    protected void init() {
        super.init();

        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        int X = x + 90;
        int Y = y + 71;
        int xOff = 37;
        int yOff = 37;

        this.addRenderableWidget(new CobraPoison(X, Y));
        this.addRenderableWidget(new StrikingZeal(X + xOff, Y));
        this.addRenderableWidget(new ArrowAnubis(X + (xOff * 2), Y));
        this.addRenderableWidget(new SethStrength(X + (xOff * 3), Y));
        this.addRenderableWidget(new FlowingSand(X + (xOff * 4), Y));

        this.addRenderableWidget(new FangFrost(X, Y + yOff));
        this.addRenderableWidget(new DivineVeil(X + xOff, Y + yOff));
        this.addRenderableWidget(new Abundance(X + (xOff * 2), Y + yOff));
        this.addRenderableWidget(new EgyptianStrength(X + (xOff * 3), Y + yOff));
        this.addRenderableWidget(new Retribution(X + (xOff * 4), Y + yOff));

        this.addRenderableWidget(new PharaohStrength(X, Y + (yOff * 2)));
        this.addRenderableWidget(new ExcisionFlesh(X + xOff, Y + (yOff * 2)));
        this.addRenderableWidget(new HeavyHand(X + (xOff * 2), Y + (yOff * 2)));
        this.addRenderableWidget(new DesertWind(X + (xOff * 3), Y + (yOff * 2)));
        this.addRenderableWidget(new Confidence(X + (xOff * 4), Y + (yOff * 2)));
    }
}
