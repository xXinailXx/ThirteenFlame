package net.xXinailXx.thirteen_flames.client.gui.button.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidgetUtils;
import net.xXinailXx.thirteen_flames.client.gui.scroll.ScrollScreen;
import net.xXinailXx.thirteen_flames.network.packet.ScrollMenuOpenPacket;
import org.zeith.hammerlib.net.Network;

import java.util.Map;

public class ButtonDecreaseEnchLevel extends AbstractWidgetUtils {
    private final ScrollScreen screen;

    public ButtonDecreaseEnchLevel(int x, int y, ScrollScreen screen) {
        super(x, y, 15, 21);
        this.screen = screen;
    }

    public void onPress() {
        Network.sendToServer(new ScrollMenuOpenPacket(this.screen.getMenu().scrollStack, 1, this.screen.getEnchLevel(), this.screen.scrollY0, this.screen.scrollY1, this.screen.getEnchSelected(), this.screen.getMenu().stack, this.screen.enchs, this.screen.possibleEnchs));
    }

    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        TextureManager manager = MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, ScrollScreen.BACKGROUNG);
        manager.bindForSetup(ScrollScreen.BACKGROUNG);
        blit(poseStack, this.x, this.y, 347, 0, 15, 21, 512, 512);

        if (this.isHovered)
            blit(poseStack, this.x, this.y, 347, 28, 15, 21, 512, 512);
    }
}
