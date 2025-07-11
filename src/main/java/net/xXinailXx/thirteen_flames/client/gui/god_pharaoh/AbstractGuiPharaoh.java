package net.xXinailXx.thirteen_flames.client.gui.god_pharaoh;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.sskirillss.relics.client.screen.base.IHoverableWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.button.gui.*;
import net.xXinailXx.thirteen_flames.data.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractGuiPharaoh extends Screen {
    public static final ResourceLocation BACKGROUNG = new ResourceLocation(ThirteenFlames.MODID, "textures/gui/god_faraon_background_screen.png");
    public static final ResourceLocation BACKGROUNG_CURSE = new ResourceLocation(ThirteenFlames.MODID, "textures/gui/god_faraon_background_screen_curse.png");
    protected static final IData.IGuiLevelingData guiLeveling = new Data.GuiLevelingData.Utils();
    protected static final IData.IXpScarabsData xpScarabsData = new Data.XpScarabsData.Utils();
    protected static final IData.IScarabsData scarabsData = new Data.ScarabsData.Utils();
    protected static final IData.IEffectData effectData = new Data.EffectData.Utils();
    protected final Minecraft MC = Minecraft.getInstance();
    protected final ScreenID screenID;
    public int backgroundHeight = 327;
    public int backgroundWidth = 363;
    protected static int levelGui;

    protected AbstractGuiPharaoh(ScreenID screenID) {
        super(Component.empty());
        this.screenID = screenID;
    }

    protected void init() {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        this.addRenderableWidget(new ButtonMiningOpenGui(x - 8, y + 35, screenID.equals(ScreenID.MINING)));
        this.addRenderableWidget(new ButtonCraftOpenGui(x - 8, y + 67, screenID.equals(ScreenID.CRAFT)));
        this.addRenderableWidget(new ButtonFightOpenGui(x - 8, y + 99, screenID.equals(ScreenID.FIGHT)));
        this.addRenderableWidget(new ButtonHealthOpenGui(x - 8, y + 131, screenID.equals(ScreenID.HEALTH)));
        this.addRenderableWidget(new ButtonGlobalOpenGui(x - 8, y + 163, screenID.equals(ScreenID.GLOBAL)));
        this.addRenderableWidget(new ButtonOpenPassiveSkillTree(x - 8, y + 195));

        if (!guiLeveling.isPlayerScreen(MC.player)) {
            this.addRenderableWidget(new ButtonConvernSilverGold(x + 373, y + 56));
            this.addRenderableWidget(new ButtonConvernLazotepAuriteh(x + 373, y + 100));
        }

        switch (screenID) {
            case MINING -> this.addRenderableWidget(new ButtonUpgradeMiningGui(x + 24, y + 250));
            case CRAFT -> this.addRenderableWidget(new ButtonUpgradeCraftGui(x + 24, y + 250));
            case FIGHT -> this.addRenderableWidget(new ButtonUpgradeFightGui(x + 24, y + 250));
            case HEALTH -> this.addRenderableWidget(new ButtonUpgradeHealthGui(x + 24, y + 250));
        }
    }

    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(poseStack);

        TextureManager manager = this.MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        if (effectData.isCurseKnef(MC.player)) {
            RenderSystem.setShaderTexture(0, BACKGROUNG_CURSE);
            manager.bindForSetup(BACKGROUNG_CURSE);
        } else {
            RenderSystem.setShaderTexture(0, BACKGROUNG);
            manager.bindForSetup(BACKGROUNG);
        }

        int texWidth = 512;
        int texHeight = 512;
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        blit(poseStack, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, texWidth, texHeight);

        if (effectData.isCurseKnef(MC.player))
            blit(poseStack, x + 288, y + 3, 229, 346, 80, 40, texWidth, texHeight);

        blit(poseStack, x + 305, y + 188, 168, 347, 22, 24, texWidth, texHeight);
        blit(poseStack, x + 305, y + 147, 197, 347, 22, 24, texWidth, texHeight);
        blit(poseStack, x + 301, y + 104, 163, 373, 30, 24, texWidth, texHeight);
        blit(poseStack, x + 301, y + 61, 194, 372, 30, 30, texWidth, texHeight);

        this.MC.font.draw(poseStack, String.valueOf(scarabsData.getScarabSilver(MC.player)), x + 317 - this.font.width(String.valueOf(scarabsData.getScarabSilver(MC.player))) / 2, y + 215, 0x673E09);
        this.MC.font.draw(poseStack, String.valueOf(scarabsData.getScarabGold(MC.player)), x + 317 - this.font.width(String.valueOf(scarabsData.getScarabGold(MC.player))) / 2, y + 174, 0x673E09);
        this.MC.font.draw(poseStack, String.valueOf(scarabsData.getScarabAuriteh(MC.player)), x + 317 - this.font.width(String.valueOf(scarabsData.getScarabAuriteh(MC.player))) / 2, y + 132, 0x673E09);
        this.MC.font.draw(poseStack, String.valueOf(scarabsData.getScarabLazotep(MC.player)), x + 317 - this.font.width(String.valueOf(scarabsData.getScarabLazotep(MC.player))) / 2, y + 92, 0x673E09);

        if (effectData.isCurseKnef(MC.player)) {
            poseStack.pushPose();
            poseStack.scale(1.5F, 1.5F, 1.5F);

            this.MC.font.draw(poseStack, "-" + guiLeveling.getProcentCurse(MC.player) + "%", x * 0.666F + 217 - this.font.width("-" + guiLeveling.getProcentCurse(MC.player) + "%") / 2.0F, y * 0.666F + 7, 0xFFFFFFFF);
            this.MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.curse.info"), x * 0.666F + 218 - this.font.width(Component.translatable("gui.thirteen_flames.curse.info")) / 2.0F, y * 0.666F + 16, 0xFFFFFFFF);

            poseStack.popPose();
        }

        poseStack.pushPose();
        poseStack.scale(1.5F, 1.5F, 1.5F);

        String screenName = "";

        switch (screenID) {
            case MINING -> screenName = "mining";
            case CRAFT -> screenName = "craft";
            case FIGHT -> screenName = "fight";
            case HEALTH -> screenName = "health";
            case GLOBAL -> screenName = "global";
        }

        int level = 0;

        if (levelGui == 0) {
            switch (screenID) {
                case MINING -> level = guiLeveling.getMiningLevel(MC.player);
                case CRAFT -> level = guiLeveling.getCraftLevel(MC.player);
                case FIGHT -> level = guiLeveling.getFightLevel(MC.player);
                case HEALTH -> level = guiLeveling.getHealthLevel(MC.player);
            }
        } else {
            level = levelGui;
        }

        this.MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.id." + screenName + ".info", level), x * 0.666F + 55, y * 0.666F + 27, 0x673E09);

        poseStack.popPose();
        poseStack.pushPose();

        poseStack.scale(0.65F, 0.65F, 0.65F);

        List<MutableComponent> bonusInfo = new ArrayList<>();

        boolean isMaxLevel = false;

        switch (screenID) {
            case MINING -> isMaxLevel = guiLeveling.getMiningLevel(MC.player) >= 100;
            case CRAFT -> isMaxLevel = guiLeveling.getCraftLevel(MC.player) >= 100;
            case FIGHT -> isMaxLevel = guiLeveling.getFightLevel(MC.player) >= 100;
            case HEALTH -> isMaxLevel = guiLeveling.getHealthLevel(MC.player) >= 100;
        }

        if (!screenID.equals(ScreenID.GLOBAL)) {
            bonusInfo.add(Component.translatable("gui.thirteen_flames.bonus_info"));

            MutableComponent bonus = Component.literal("        §l◆§r");

            switch (screenID) {
                case MINING -> {
                    bonusInfo.add(bonus.append(Component.translatable("gui.thirteen_flames.bonus_mining").append(guiLeveling.getMiningLevel(MC.player) + "%.")));
                    bonusInfo.add(Component.literal(" "));
                }
                case CRAFT -> {
                    bonusInfo.add(bonus.append(Component.translatable("gui.thirteen_flames.bonus_craft").append(guiLeveling.getCraftLevel(MC.player) + "%.")));
                    bonusInfo.add(Component.literal(" "));
                }
                case FIGHT -> {
                    bonusInfo.add(bonus.append(Component.translatable("gui.thirteen_flames.bonus_fight_1").append(guiLeveling.getFightLevel(MC.player) + "%.\n        §l◆§r").append(Component.translatable("gui.thirteen_flames.bonus_fight_2")).append(Math.round((float) guiLeveling.getFightLevel(MC.player) / 10) + "%.")));
                }
                case HEALTH -> {
                    bonusInfo.add(bonus.append(Component.translatable("gui.thirteen_flames.bonus_health").append(guiLeveling.getHealthLevel(MC.player) + "%.")));
                    bonusInfo.add(Component.literal(" "));
                }
            }

            if (!effectData.isCurseKnef(MC.player)) {
                bonusInfo.add(Component.literal(" "));
                bonusInfo.add(Component.literal(" "));
            }

            if (isMaxLevel)
                bonusInfo.add(Component.translatable("gui.thirteen_flames.bonus_max_level"));
            else
                bonusInfo.add(Component.translatable("gui.thirteen_flames.bonus_cost_upgrade"));

            if (!guiLeveling.isPlayerScreen(MC.player))
                bonusInfo.add(Component.translatable("gui.thirteen_flames.gui_upgrade_info"));
            else
                bonusInfo.add(Component.translatable("gui.thirteen_flames.player_gui_info"));
        } else {
            bonusInfo.add(Component.translatable("gui.thirteen_flames.global_info"));
        }

        if (screenID.equals(ScreenID.GLOBAL)) {
            if (!effectData.isCurseKnef(MC.player)) {
                bonusInfo.add(Component.literal(" "));
                bonusInfo.add(Component.literal(" "));
            }

            bonusInfo.add(Component.literal(" "));

            MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.global_scarab_info", xpScarabsData.getXpScarabSilver(MC.player), xpScarabsData.getXpScarabGold(MC.player), xpScarabsData.getXpScarabAuriteh(MC.player), xpScarabsData.getXpScarabLazotep(MC.player)), x * 1.538F + 138, y * 1.538F + 424, 4269832);
        }

        if (effectData.isCurseKnef(MC.player)) {
            if (!screenID.equals(ScreenID.GLOBAL)) {
                bonusInfo.add(Component.literal(" "));
                bonusInfo.add(Component.translatable("gui.thirteen_flames.curse_knef").withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.curse_knef"), x * 1.538F + 138, y * 1.538F + 437, 11141290);
            }
        }

        List<FormattedCharSequence> tooltip = Lists.newArrayList();

        int renderX = 0;
        int renderY = 0;

        for (MutableComponent entry : bonusInfo) {
            int entryWidth = (MC.font.width(entry)) / 2;

            if (entryWidth > renderX)
                renderX = Math.min(entryWidth, 250);

            tooltip.addAll(MC.font.split(entry, 380));
        }

        int yOff = 0;

        for (FormattedCharSequence entry : tooltip) {
            MC.font.draw(poseStack, entry, x * 1.538F + 138, y * 1.538F + 384 + yOff, 4269832);

            yOff += 9;
        }

        poseStack.popPose();

        RenderSystem.setShaderTexture(0, BACKGROUNG);
        manager.bindForSetup(BACKGROUNG);

        if (!screenID.equals(ScreenID.GLOBAL) && !isMaxLevel) {
            poseStack.pushPose();

            poseStack.scale(0.45F, 0.45F, 0.45F);

            int extraYOff = 0;

            if (!effectData.isCurseKnef(MC.player))
                extraYOff = 27;

            blit(poseStack, (int) (x * 2.222F + 567), (int) (y * 2.222F + 588 + extraYOff), 197, 347, 22, 24, texWidth, texHeight);

            poseStack.popPose();
        } else if (screenID.equals(ScreenID.GLOBAL) && guiLeveling.isPlayerScreen(MC.player)) {
            poseStack.pushPose();

            poseStack.scale(1.5F, 1.5F, 1.5F);

            this.MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.xp"), x * 0.666F + 30 - this.font.width(Component.translatable("gui.thirteen_flames.xp")) / 2, y * 0.666F + 171, 0x673E09);
            this.MC.font.draw(poseStack, Component.literal("__"), x * 0.666F + 25, y * 0.666F + 175, 0x673E09);
            this.MC.font.draw(poseStack, String.valueOf(MC.player.totalExperience), x * 0.666F + 32 - this.font.width(String.valueOf(MC.player.totalExperience)) / 2, y * 0.666F + 186, 0x673E09);

            poseStack.popPose();
        }

        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
        Iterator var33 = this.children().iterator();

        while (var33.hasNext()) {
            GuiEventListener listener = (GuiEventListener) var33.next();
            if (listener instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) listener;
                if (button.isHoveredOrFocused() && button instanceof IHoverableWidget) {
                    IHoverableWidget widget = (IHoverableWidget) button;
                    widget.onHovered(poseStack, pMouseX, pMouseY);
                }
            }
        }
    }

    public boolean isPauseScreen() {
        return false;
    }
}
