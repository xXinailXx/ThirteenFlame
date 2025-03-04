package net.xXinailXx.thirteen_flames.client.gui.button.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureManager;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidgetUtils;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.GodFaraon.GodFaraonScreenMining;
import net.xXinailXx.thirteen_flames.data.Data;

public class ButtonUpgradeCraftGui extends AbstractWidgetUtils {
    private static final IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData();
    private static final IData.IScarabsData scarabsData = new Data.ScarabsData();

    public ButtonUpgradeCraftGui(int x, int y) {
        super( x, y, 46, 48 );
    }

    public void onPress() {
        if (guiLevelingData.getGuiCraftLevelAmount() < 100 && !guiLevelingData.isPlayerScreen()) {
            if (scarabsData.getScarabGold() >= 1) {
                scarabsData.addScarabGold(-1);
                guiLevelingData.addGuiCraftLevelAmount(1);
            }
        }
    }

    public void renderButton(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        TextureManager manager = MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, GodFaraonScreenMining.BACKGROUNG);
        manager.bindForSetup(GodFaraonScreenMining.BACKGROUNG);

        if (scarabsData.getScarabGold() >= 1 && guiLevelingData.getGuiCraftLevelAmount() < 100 && !guiLevelingData.isPlayerScreen())
            blit(poseStack, this.x, this.y, 6, 342, 46, 48, 512, 512);
        else
            blit(poseStack, this.x, this.y, 55, 342, 45, 48, 512, 512);

        if (this.isHovered)
            blit(poseStack, this.x - 1, this.y - 3, 6, 395, 51, 59, 512, 512);
    }
}
