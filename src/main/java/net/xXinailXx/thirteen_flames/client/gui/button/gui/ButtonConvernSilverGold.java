package net.xXinailXx.thirteen_flames.client.gui.button.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidget;
import net.xXinailXx.thirteen_flames.client.gui.god_pharaoh.GodPharaohScreenMining;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;

public class ButtonConvernSilverGold extends AbstractWidget {
    private static final IData.IScarabsData scarabsData = new Data.ScarabsData.Utils();

    public ButtonConvernSilverGold(int x, int y) {
        super(x, y, 93, 36);
    }

    public void onPress() {
        if (MC.player == null)
            return;

        if (scarabsData.getScarabSilver(MC.player) >= 3) {
            scarabsData.addScarabSilver(MC.player, -3);
            scarabsData.addScarabGold(MC.player, 1);
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

        blit(poseStack, this.x, this.y, 376, 87, 93, 36, 512, 512);

        if (this.isHovered)
            blit(poseStack, this.x - 3, this.y - 3, 376, 256, 99, 42, 512, 512);

        MC.font.draw(poseStack, Component.literal("x3"), this.x + 32, this.y + 23, 0x673E09);
        MC.font.draw(poseStack, Component.literal("x1"), this.x + 79, this.y + 23, 0x673E09);
    }
}
