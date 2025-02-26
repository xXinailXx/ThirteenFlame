package net.xXinailXx.thirteen_flames.client.gui.button.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureManager;
import net.xXinailXx.dragonworldlib.client.utils.gui.AbstractWidgetUtils;
import net.xXinailXx.thirteen_flames.client.gui.GodFaraon.GodFaraonScreenCraft;
import net.xXinailXx.thirteen_flames.client.gui.GodFaraon.GodFaraonScreenMining;

public class ButtonCraftOpenGui extends AbstractWidgetUtils {
    public ButtonCraftOpenGui(int x, int y, boolean active) {
        super(active ? (x + 5) : x, y, 61, 29);
    }

    public void onPress() {
        this.MC.setScreen(new GodFaraonScreenCraft());
    }

    public void renderButton(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        TextureManager manager = MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, GodFaraonScreenMining.BACKGROUNG);
        manager.bindForSetup(GodFaraonScreenMining.BACKGROUNG);

        blit(poseStack, this.x, this.y, 439, 0, 59, 27, 512, 512);

        if (this.isHovered) {
            blit(poseStack, this.x - 3, this.y - 3, 376, 304, 65, 34, 512, 512);
        }
    }
}
