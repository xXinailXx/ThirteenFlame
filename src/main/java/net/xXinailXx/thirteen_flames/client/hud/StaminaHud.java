package net.xXinailXx.thirteen_flames.client.hud;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IStaminaData;
import net.xXinailXx.thirteen_flames.data.StaminaData;

@Mod.EventBusSubscriber(modid = ThirteenFlames.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class StaminaHud {
    private static final Minecraft MC = Minecraft.getInstance();

    public static void render(PoseStack poseStack, float partialTicks) {
        Window window = MC.getWindow();
        LocalPlayer player = MC.player;

        if (player != null && ! player.isCreative() && ! player.isSpectator()) {
            RandomSource random = player.getRandom();
            IStaminaData staminaData = new StaminaData.Utils();

            int x = window.getGuiScaledWidth() / 2 + 14;
            int y = window.getGuiScaledHeight() - 48;
            int stamina = staminaData.getStamina(player);
            int maxStamina = staminaData.getMaxStamina(player);
            int shakeTime = staminaData.getShakeTime(player);

            poseStack.pushPose();

            if (shakeTime > 0) {
                float speed = 0.05F;
                poseStack.translate((random.nextGaussian() - random.nextGaussian()) * (double) shakeTime * (double) speed, (random.nextGaussian() - random.nextGaussian()) * (double) shakeTime * (double) 0.25F * (double) speed, (random.nextGaussian() - random.nextGaussian()) * (double) shakeTime * (double) speed);
            }

            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, new ResourceLocation(ThirteenFlames.MODID, "textures/hud/stamina_icon.png"));

            Gui.blit(poseStack, x - 4, y - 1, 0.0F, 0.0F, 6, 9, 6, 9);

            RenderSystem.setShaderTexture(0, new ResourceLocation(ThirteenFlames.MODID, "textures/hud/stamina_bar.png"));

            Gui.blit(poseStack, x, y, 0.0F, 0.0F, 77, 7, 77, 7);

            float percentage = (float) stamina / ((float) maxStamina / 100.0F) / 100.0F;

            RenderSystem.setShaderTexture(0, new ResourceLocation(ThirteenFlames.MODID, "textures/hud/stamina_filler.png"));

            Gui.blit(poseStack, x, y, 0.0F, 0.0F, Math.round(77.0F * percentage), 7, 77, 7);

            RenderSystem.disableBlend();
            poseStack.popPose();
        }
    }

    @SubscribeEvent
    public static void onOverlayRegistry(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll(ThirteenFlames.MODID,
                (ForgeGui, poseStack, partialTick, screenWidth, screenHeight) -> render(poseStack, partialTick));
    }
}
