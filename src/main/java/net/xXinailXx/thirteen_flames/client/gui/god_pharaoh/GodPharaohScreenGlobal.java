package net.xXinailXx.thirteen_flames.client.gui.god_pharaoh;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.global.*;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;

@OnlyIn(Dist.CLIENT)
public class GodPharaohScreenGlobal extends AbstractGuiPharaoh {
    private final IData.IEffectData effectData = new Data.EffectData.Utils();

    public GodPharaohScreenGlobal() {
        super(ScreenID.GLOBAL);
    }

    protected void init() {
        super.init();

        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        int X = x + 90;
        int Y = y + 71;
        int xOff = 37;
        int yOff = 37;

        this.addRenderableWidget(new CoverNile(X, Y));
        this.addRenderableWidget(new Heat(X + xOff, Y));
        this.addRenderableWidget(new CombatExperience(X + (xOff * 2), Y));
        this.addRenderableWidget(new PackLeader(X + (xOff * 3), Y));
        this.addRenderableWidget(new FurySky(X + (xOff * 4), Y));

        this.addRenderableWidget(new LordElements(X, Y + yOff));

        if (effectData.isCurseKnef(MC.player))
            this.addRenderableWidget(new Recovery(X + xOff, Y + yOff));

        this.addRenderableWidget(new SacredBonds(X + (effectData.isCurseKnef(MC.player) ? (xOff * 2) : xOff), Y + yOff));
        this.addRenderableWidget(new Trader(X + (effectData.isCurseKnef(MC.player) ? (xOff * 3) : (xOff * 2)), Y + yOff));
        this.addRenderableWidget(new Scissorhands(X + (effectData.isCurseKnef(MC.player) ? (xOff * 4) : (xOff * 3)), Y + yOff));
        this.addRenderableWidget(new CoverNight(X + (effectData.isCurseKnef(MC.player) ? 0 : (xOff * 4)), Y + (effectData.isCurseKnef(MC.player) ? (yOff * 2) : yOff)));

        this.addRenderableWidget(new PharaohsWings(X + (effectData.isCurseKnef(MC.player) ? xOff : 0), Y + (yOff * 2)));
        this.addRenderableWidget(new WondereVoid(X + (effectData.isCurseKnef(MC.player) ? (xOff * 2) : xOff), Y + (yOff * 2)));
        this.addRenderableWidget(new Austerity(X + (effectData.isCurseKnef(MC.player) ? (xOff * 3) : (xOff * 2)), Y + (yOff * 2)));
        this.addRenderableWidget(new GiftGodPharaoh(X + (effectData.isCurseKnef(MC.player) ? (xOff * 4) : (xOff * 3)), Y + (yOff * 2)));
        this.addRenderableWidget(new GehennaFire(X + (effectData.isCurseKnef(MC.player) ? 0 : (xOff * 4)), Y + (effectData.isCurseKnef(MC.player) ? (yOff * 3) : (yOff * 2))));

        this.addRenderableWidget(new AdamantGaze(X + (effectData.isCurseKnef(MC.player) ? xOff : 0), Y + (yOff * 3)));
        this.addRenderableWidget(new NileFlame(X + (effectData.isCurseKnef(MC.player) ? (xOff * 2) : xOff), Y + (yOff * 3)));
    }
}

