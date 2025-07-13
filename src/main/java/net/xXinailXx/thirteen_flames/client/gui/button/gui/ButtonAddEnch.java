package net.xXinailXx.thirteen_flames.client.gui.button.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidget;
import net.xXinailXx.thirteen_flames.client.gui.scroll.ScrollScreen;
import net.xXinailXx.thirteen_flames.network.packet.ScrollMenuOpenPacket;
import org.zeith.hammerlib.net.Network;

public class ButtonAddEnch extends AbstractWidget {
    private final ScrollScreen screen;
    private final boolean isActive;

    public ButtonAddEnch(int x, int y, ScrollScreen screen) {
        super(x, y, 59, 21);
        this.screen = screen;
        this.isActive = screen.getEnchSelectedInLists() == 0 && (screen.getEnchSelected() != null && screen.getEnchLevel() > 0);
    }

    public void onPress() {
        if (this.isActive)
            Network.sendToServer(new ScrollMenuOpenPacket(this.screen.getMenu().scrollStack, 2, this.screen.getEnchLevel(), this.screen.scrollY0, this.screen.scrollY1, this.screen.getEnchSelected(), this.screen.getMenu().stack, this.screen.enchs, this.screen.possibleEnchs));
    }

    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        TextureManager manager = MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, ScrollScreen.BACKGROUNG);
        manager.bindForSetup(ScrollScreen.BACKGROUNG);

        if (!this.isActive)
            RenderSystem.setShaderColor(0.75F, 0.75F, 0.75F, 0.75F);

        blit(poseStack, this.x, this.y, 362, 0, 59, 21, 512, 512);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        if (this.isActive && this.isHovered)
            blit(poseStack, this.x, this.y, 362, 28, 59, 21, 512, 512);

        MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.scroll.button.add_enchant_info"), this.x + 29 - MC.font.width(Component.translatable("gui.thirteen_flames.scroll.button.add_enchant_info")) / 2, this.y + 7, !this.isActive ? 0xf2d37f : 0x673E09);
    }
}
