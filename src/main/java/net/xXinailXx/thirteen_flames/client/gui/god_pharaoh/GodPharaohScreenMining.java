package net.xXinailXx.thirteen_flames.client.gui.god_pharaoh;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.mining.*;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class GodPharaohScreenMining extends AbstractGuiPharaoh {
    public GodPharaohScreenMining() {
        super(ScreenID.MINING);
    }

    public void tick() {
        levelGui = guiLeveling.getMiningLevel(Minecraft.getInstance().player);
    }

    protected void init() {
        super.init();

        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        int X = x + 90;
        int Y = y + 71;
        int xOff = 37;
        int yOff = 37;

        this.addRenderableWidget(new MinesSolomon(X, Y));
        this.addRenderableWidget(new Archaeology(X + xOff, Y));
        this.addRenderableWidget(new GoldEltdorado(X + (xOff * 2), Y));
        this.addRenderableWidget(new GoldRush(X + (xOff * 3), Y));
        this.addRenderableWidget(new Excavations(X + (xOff * 4), Y));

        this.addRenderableWidget(new PhoenixHeat(X, Y + yOff));
        this.addRenderableWidget(new Perseverance(X + xOff, Y + yOff));
        this.addRenderableWidget(new MagomedWalks(X + (xOff * 2), Y + yOff));
        this.addRenderableWidget(new PathPlanner(X + (xOff * 3), Y + yOff));
        this.addRenderableWidget(new WonderfulGarden(X + (xOff * 4), Y + yOff));
    }
}
