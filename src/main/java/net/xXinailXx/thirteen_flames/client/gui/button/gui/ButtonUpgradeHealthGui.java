package net.xXinailXx.thirteen_flames.client.gui.button.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureManager;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidget;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.god_pharaoh.GodPharaohScreenMining;
import net.xXinailXx.thirteen_flames.data.Data;

public class ButtonUpgradeHealthGui extends AbstractWidget {
    private static final IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
    private static final IData.IScarabsData scarabsData = new Data.ScarabsData.Utils();

    public ButtonUpgradeHealthGui(int x, int y) {
        super(x, y, 46, 48);
    }

    public void onPress() {
        if (guiLevelingData.getHealthLevel(MC.player) < 100 && !guiLevelingData.isPlayerScreen(MC.player)) {
            if (scarabsData.getScarabGold(MC.player) >= 1) {
                scarabsData.addScarabGold(MC.player, -1);
                guiLevelingData.addHealthLevel(MC.player, 1);
            }
        }
    }

    public void renderButton(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        TextureManager manager = MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        if (new Data.EffectData.Utils().isCurseKnef(MC.player)) {
            RenderSystem.setShaderTexture(0, GodPharaohScreenMining.BACKGROUNG_CURSE);
            manager.bindForSetup(GodPharaohScreenMining.BACKGROUNG_CURSE);
        } else {
            RenderSystem.setShaderTexture(0, GodPharaohScreenMining.BACKGROUNG);
            manager.bindForSetup(GodPharaohScreenMining.BACKGROUNG);
        }

        if (scarabsData.getScarabGold(MC.player) >= 1 && guiLevelingData.getHealthLevel(MC.player) < 100 && !guiLevelingData.isPlayerScreen(MC.player))
            blit(poseStack, this.x, this.y, 6, 342, 46, 48, 512, 512);
        else
            blit(poseStack, this.x, this.y, 55, 342, 45, 48, 512, 512);

        if (this.isHovered)
            blit(poseStack, this.x - 1, this.y - 3, 6, 395, 51, 59, 512, 512);
    }
}
