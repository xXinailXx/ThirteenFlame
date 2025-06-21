package net.xXinailXx.thirteen_flames.client.gui.button.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidgetUtils;
import net.xXinailXx.thirteen_flames.client.gui.scroll.EnchantmentUtils;
import net.xXinailXx.thirteen_flames.client.gui.scroll.ScrollScreen;
import net.xXinailXx.thirteen_flames.network.packet.ScrollMenuOpenPacket;
import org.zeith.hammerlib.net.Network;

import java.util.HashMap;
import java.util.Map;

public class ButtonItemEnchant extends AbstractWidgetUtils {
    private final ItemStack stack;
    private final ScrollScreen screen;
    private final boolean isActive;

    public ButtonItemEnchant(int x, int y, ScrollScreen screen) {
        super(x, y, 84, 27);
        this.stack = screen.getMenu().stack;
        this.screen = screen;
        this.isActive = !screen.enchs.isEmpty();
    }

    public void onPress() {
        if (this.isActive) {
            if (this.MC.player.experienceLevel >= this.screen.getExpCost()) {
                int exp = 0;

                for (Enchantment ench : this.screen.enchs.keySet()) {
                    int level = this.screen.enchs.get(ench);

                    if (this.stack.getEnchantmentLevel(ench) > 0)
                        this.stack.getAllEnchantments().put(ench, level);
                    else
                        this.stack.enchant(ench, level);

                    exp += level;
                }

                LevelingUtils.addExperience(this.stack, exp);

                if (!this.MC.player.isCreative())
                    this.MC.player.giveExperienceLevels(-this.screen.getExpCost());
            }

            this.screen.enchs.clear();
            Network.sendToServer(new ScrollMenuOpenPacket(this.screen.getMenu().scrollStack, -1, 0, this.screen.scrollY0, this.screen.scrollY1, null, this.stack, new HashMap<>(), EnchantmentUtils.getPossibleEnch(this.stack)));
        }
    }

    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        TextureManager manager = this.MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, ScrollScreen.BACKGROUNG);
        manager.bindForSetup(ScrollScreen.BACKGROUNG);

        if (!this.isActive)
            RenderSystem.setShaderColor(0.75F, 0.75F, 0.75F, 0.75F);

        blit(poseStack, this.x, this.y, 421, 0, 84, 27, 512, 512);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        poseStack.pushPose();
        poseStack.scale(1.2F, 1.2F, 1.2F);

        this.MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.scroll.button.enchant_item_info"), this.x - 10 - MC.font.width(Component.translatable("gui.thirteen_flames.scroll.button.enchant_item_info")) / 2, this.y - 20, !this.isActive ? 0xf2d37f : 0x673E09);

        poseStack.popPose();

        RenderSystem.setShaderTexture(0, ScrollScreen.BACKGROUNG);
        manager.bindForSetup(ScrollScreen.BACKGROUNG);

        if (this.isActive && this.isHovered)
            blit(poseStack, this.x, this.y, 421, 28, 84, 27, 512, 512);
    }
}
