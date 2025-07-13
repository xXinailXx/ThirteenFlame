package net.xXinailXx.thirteen_flames.client.gui.button.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import daripher.skilltree.client.screen.SkillTreeScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidget;
import net.xXinailXx.thirteen_flames.client.gui.god_pharaoh.GodPharaohScreenMining;
import net.xXinailXx.thirteen_flames.data.Data;

public class ButtonOpenPassiveSkillTree extends AbstractWidget {
    public ButtonOpenPassiveSkillTree(int x, int y) {
        super(x, y, 61, 29);
    }

    public void onPress() {
        this.MC.setScreen(new SkillTreeScreen(new ResourceLocation("skilltree", "tree")));
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

        blit(poseStack, this.x, this.y, 439, 58, 59, 27, 512, 512);

        if (this.isHovered)
            blit(poseStack, this.x - 3, this.y - 3, 376, 304, 67, 36, 512, 512);
    }
}
