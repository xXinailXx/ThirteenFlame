package net.xXinailXx.thirteen_flames.client.gui.button.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidget;
import net.xXinailXx.thirteen_flames.client.gui.scroll.EnchantmentUtils;
import net.xXinailXx.thirteen_flames.client.gui.scroll.ScrollScreen;
import net.xXinailXx.thirteen_flames.network.packet.ScrollMenuOpenPacket;
import org.zeith.hammerlib.net.Network;

public class ButtonEnchSelected extends AbstractWidget {
    private final ScrollScreen screen;
    private final EnchantmentUtils.Ench ench;
    private boolean isActive;
    private final int oY;

    public ButtonEnchSelected(int x, int y, int oY, EnchantmentUtils.Ench ench, ScrollScreen screen) {
        super(x, y, 60, 21);
        this.oY = oY;
        this.ench = ench;
        this.screen = screen;
        this.isActive = screen.getEnchSelected() == null ? false : screen.equalsEnch(this.ench);
    }

    public void onPress() {
        if (this.isHovered && !this.isActive) {
            if (this.ench.getMinLevel() <= 0)
                this.ench.setMinLevel(1);

            Network.sendToServer(new ScrollMenuOpenPacket(this.screen.getMenu().scrollStack, 3, this.ench.getMinLevel(), this.screen.scrollY0, this.screen.scrollY1, this.ench, this.screen.getMenu().stack, this.screen.enchs, this.screen.possibleEnchs));
        }
    }

    public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
        if (this.visible) {
            if (this.y >= this.oY + 22 && this.y <= this.oY + 110) {
                this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            } else if (this.y == this.oY + 11) {
                if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.oY + 22 && mouseY <= this.oY + 33)
                    this.isHovered = true;
                else
                    this.isHovered = false;
            } else if (this.y == this.oY + 121) {
                if (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.oY + 121 && mouseY <= this.oY + 132)
                    this.isHovered = true;
                else
                    this.isHovered = false;
            } else {
                this.isHovered = false;
            }

            this.renderButton(stack, mouseX, mouseY, partialTick);
        }
    }

    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        TextureManager manager = MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, ScrollScreen.BACKGROUNG);
        manager.bindForSetup(ScrollScreen.BACKGROUNG);

        if (this.isActive)
            RenderSystem.setShaderColor(0.75F, 0.75F, 0.75F, 0.75F);

        if (this.y < this.oY + 11 || this.y > this.oY + 121)
            return;
        else if (this.y == this.oY + 11)
            blit(poseStack, this.x, this.y + 11, 331, 62, 59, 11, 512, 512);
        else if (this.y == this.oY + 121)
            blit(poseStack, this.x, this.y, 331, 51, 59, 11, 512, 512);
        else
            blit(poseStack, this.x, this.y, 331, 51, 59, 22, 512, 512);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        if (this.y >= this.oY + 22 && this.y <= this.oY + 110) {
            String nameEnch = Component.translatable(this.ench.getEnch().getDescriptionId()).getString();

            if (nameEnch.length() > 5 && this.ench.getEnch().getMaxLevel() > 1)
                nameEnch = nameEnch.substring(0, 5).concat("...").concat(Component.translatable("enchantment.level." + this.ench.getMinLevel()).getString());
            else if (nameEnch.length() > 5)
                nameEnch = nameEnch.substring(0, 5).concat("...");
            else if (nameEnch.length() <= 5 && this.ench.getEnch().getMaxLevel() > 1)
                nameEnch = nameEnch.concat(" ").concat(Component.translatable("enchantment.level." + this.ench.getMinLevel()).getString());

            MC.font.draw(poseStack, nameEnch, this.x + 29 - MC.font.width(nameEnch) / 2, this.y + 7, this.isActive ? 0xf2d37f : 0x673E09);
        }

        RenderSystem.setShaderTexture(0, ScrollScreen.BACKGROUNG);
        manager.bindForSetup(ScrollScreen.BACKGROUNG);

        if (this.isHovered)
            if (this.y >= this.oY + 22 && this.y <= this.oY + 110)
                blit(poseStack, this.x, this.y, 331, 74, 59, 22, 512, 512);
            else if (this.y == this.oY + 11)
                blit(poseStack, this.x, this.y + 11, 331, 85, 59, 11, 512, 512);
            else if (this.y == this.oY + 121)
                blit(poseStack, this.x, this.y, 331, 74, 59, 11, 512, 512);
    }
}
