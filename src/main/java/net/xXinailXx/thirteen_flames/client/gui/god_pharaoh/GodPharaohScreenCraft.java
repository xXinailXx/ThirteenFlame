package net.xXinailXx.thirteen_flames.client.gui.god_pharaoh;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.craft.*;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class GodPharaohScreenCraft extends AbstractGuiPharaoh {
    public GodPharaohScreenCraft() {
        super(ScreenID.CRAFT);
    }

    @Override
    public void tick() {
        levelGui = guiLeveling.getGuiCraftLevelAmount();
    }

    protected void init() {
        super.init();

        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        int X = x + 90;
        int Y = y + 71;
        int xOff = 37;
        int yOff = 37;

        this.addRenderableWidget(new Notches(X, Y));
        this.addRenderableWidget(new NileTide(X + xOff, Y));
        this.addRenderableWidget(new CarefulHandling(X + (xOff * 2), Y));
        this.addRenderableWidget(new FishermanOfFisherman(X + (xOff * 3), Y));
        this.addRenderableWidget(new TreasureNile(X + (xOff * 4), Y));

        this.addRenderableWidget(new ForgeLegends(X, Y + yOff));
        this.addRenderableWidget(new Oasis(X + xOff, Y + yOff));
        this.addRenderableWidget(new GrainGrower(X + (xOff * 2), Y + yOff));
    }
}
