package net.xXinailXx.thirteen_flames.client.gui.GodFaraon;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.xXinailXx.enderdragonlib.interfaces.IHoveredWidget;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.IAbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.gui.*;
import net.xXinailXx.thirteen_flames.data.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractGuiFaraon extends Screen {
    public static final ResourceLocation BACKGROUNG = new ResourceLocation( ThirteenFlames.MODID, "textures/gui/god_faraon_background_screen.png");
    public static final ResourceLocation BACKGROUNG_CURSE = new ResourceLocation( ThirteenFlames.MODID, "textures/gui/god_faraon_background_screen_curse.png");
    protected static final IData.IGuiLevelingData guiLeveling = new Data.GuiLevelingData();
    protected static final IData.IXpScarabsData xpScarabsData = new Data.XpScarabsData();
    protected static final IData.IScarabsData scarabsData = new Data.ScarabsData();
    protected static final IData.IEffectData effectData = new Data.EffectData();
    protected final Minecraft MC = Minecraft.getInstance();
    protected final IAbilityData.ScreenID screenID;
    public int backgroundHeight = 327;
    public int backgroundWidth = 363;
    protected static int levelGui;
    protected Player player = MC.player;

    protected AbstractGuiFaraon(IAbilityData.ScreenID screenID) {
        super(Component.empty());
        this.screenID = screenID;
    }

    @Override
    public void tick() {
        this.player = minecraft.player;
    }

    protected void init() {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        this.addRenderableWidget(new ButtonMiningOpenGui(x - 8, y + 35, screenID.equals(IAbilityData.ScreenID.MINING)));
        this.addRenderableWidget(new ButtonCraftOpenGui(x - 8, y + 67, screenID.equals(IAbilityData.ScreenID.CRAFT)));
        this.addRenderableWidget(new ButtonFightOpenGui(x - 8, y + 99, screenID.equals(IAbilityData.ScreenID.FIGHT)));
        this.addRenderableWidget(new ButtonHealthOpenGui(x - 8, y + 131, screenID.equals(IAbilityData.ScreenID.HEALTH)));
        this.addRenderableWidget(new ButtonGlobalOpenGui(x - 8, y + 163, screenID.equals(IAbilityData.ScreenID.GLOBAL)));
        this.addRenderableWidget(new ButtonOpenPassiveSkillTree(x - 8, y + 195));

        if (!guiLeveling.isPlayerScreen()) {
            this.addRenderableWidget(new ButtonConvernSilver_Gold(x + 373, y + 56));
            this.addRenderableWidget(new ButtonConvernLazotep_Auriteh(x + 373, y + 100));
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

        if (effectData.isCurseKnef()) {
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

        if (effectData.isCurseKnef())
            blit(poseStack, x + 288, y + 3, 229, 346, 80, 40, texWidth, texHeight);

        blit(poseStack, x + 305, y + 188, 168, 347, 22, 24, texWidth, texHeight);
        blit(poseStack, x + 305, y + 147, 197, 347, 22, 24, texWidth, texHeight);
        blit(poseStack, x + 301, y + 104, 163, 373, 30, 24, texWidth, texHeight);
        blit(poseStack, x + 301, y + 61, 194, 372, 30, 30, texWidth, texHeight);

        poseStack.pushPose();
        poseStack.scale(1.1F, 1.1F, 1.1F);

        this.MC.font.draw(poseStack, String.valueOf(scarabsData.getScarabSilver(player)), (x + 275) - this.font.width( String.valueOf(scarabsData.getScarabSilver(player))) / 2.0F, (y + 196), 0x673E09);
        this.MC.font.draw(poseStack, String.valueOf(scarabsData.getScarabGold()), (x + 275) - this.font.width( String.valueOf(scarabsData.getScarabGold())) / 2.0F, (y + 157), 0x673E09);
        this.MC.font.draw(poseStack, String.valueOf(scarabsData.getScarabAuriteh()), (x + 275) - this.font.width( String.valueOf(scarabsData.getScarabAuriteh())) / 2.0F, (y + 120), 0x673E09);
        this.MC.font.draw(poseStack, String.valueOf(scarabsData.getScarabLazotep()), (x + 275) - this.font.width( String.valueOf(scarabsData.getScarabLazotep())) / 2.0F, (y + 82), 0x673E09);

        poseStack.popPose();

        if (effectData.isCurseKnef()) {
            poseStack.pushPose();
            poseStack.scale(1.6F, 1.6F, 1.6F);

            this.MC.font.draw(poseStack, "-" + guiLeveling.getProcentCurse() + "%", x + 140, y, 0xFFFFFFFF);

            poseStack.scale(1F, 1F, 1F);

            this.MC.font.draw(poseStack, Component.translatable("gui.curse.info"), x + 153 - this.font.width(Component.translatable("gui.curse.info")) / 2.0F, y + 10, 0xFFFFFFFF);

            poseStack.popPose();
        }

        poseStack.pushPose();
        poseStack.scale(1.6F, 1.6F, 1.6F);

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
                case MINING -> level = guiLeveling.getGuiMiningLevelAmount();
                case CRAFT -> level = guiLeveling.getGuiCraftLevelAmount();
                case FIGHT -> level = guiLeveling.getGuiFightLevelAmount();
                case HEALTH -> level = guiLeveling.getGuiHealthLevelAmount();
            }
        } else {
            level = levelGui;
        }

        this.MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.id." + screenName + ".info", level), (x + 2), (y * 2 + 2), 0x673E09);

        poseStack.scale(0.42F, 0.42F, 0.42F);

        List<MutableComponent> bonusInfo = new ArrayList<>();

        boolean isMaxLevel = false;

        switch (screenID) {
            case MINING -> isMaxLevel = guiLeveling.getGuiMiningLevelAmount() >= 100;
            case CRAFT -> isMaxLevel = guiLeveling.getGuiCraftLevelAmount() >= 100;
            case FIGHT -> isMaxLevel = guiLeveling.getGuiFightLevelAmount() >= 100;
            case HEALTH -> isMaxLevel = guiLeveling.getGuiHealthLevelAmount() >= 100;
        }

        if (!screenID.equals(IAbilityData.ScreenID.GLOBAL)) {
            bonusInfo.add(Component.translatable("gui.thirteen_flames.bonus_info"));

            MutableComponent bonus = Component.literal("        §l◆§r");

            switch (screenID) {
                case MINING -> {
                    bonusInfo.add(bonus.append(Component.translatable("gui.thirteen_flames.bonus_mining").append(guiLeveling.getGuiMiningLevelAmount() + "%.")));
                    bonusInfo.add(Component.literal(" "));
                }
                case CRAFT -> {
                    bonusInfo.add(bonus.append(Component.translatable("gui.thirteen_flames.bonus_craft").append(guiLeveling.getGuiCraftLevelAmount() + "%.")));
                    bonusInfo.add(Component.literal(" "));
                }
                case FIGHT -> {
                    bonusInfo.add(bonus.append(Component.translatable("gui.thirteen_flames.bonus_fight_1").append(guiLeveling.getGuiFightLevelAmount() + "%.\n        §l◆§r").append(Component.translatable("gui.thirteen_flames.bonus_fight_2")).append(Math.round((float) guiLeveling.getGuiFightLevelAmount() / 10) + "%.")));
                }
                case HEALTH -> {
                    bonusInfo.add(bonus.append(Component.translatable("gui.thirteen_flames.bonus_health").append(guiLeveling.getGuiHealthLevelAmount() + "%.")));
                    bonusInfo.add(Component.literal(" "));
                }
            }

            bonusInfo.add(Component.literal(" "));

            if (!effectData.isCurseKnef()) {
                bonusInfo.add(Component.literal(" "));
                bonusInfo.add(Component.literal(" "));
            }

            if (isMaxLevel)
                bonusInfo.add(Component.translatable("gui.thirteen_flames.bonus_max_level"));
            else
                bonusInfo.add(Component.translatable("gui.thirteen_flames.bonus_cost_upgrade"));

            if (!guiLeveling.isPlayerScreen())
                bonusInfo.add(Component.translatable("gui.thirteen_flames.gui_upgrade_info"));
            else
                bonusInfo.add(Component.translatable("gui.thirteen_flames.player_gui_info"));
        } else {
            bonusInfo.add(Component.translatable("gui.thirteen_flames.global_info"));
        }

        if (screenID.equals(IAbilityData.ScreenID.GLOBAL)) {
            if (!effectData.isCurseKnef()) {
                bonusInfo.add(Component.literal(" "));
                bonusInfo.add(Component.literal(" "));
            }

            MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.global_scarab_info", xpScarabsData.getXpScarabSilver(), xpScarabsData.getXpScarabGold(), xpScarabsData.getXpScarabAuriteh(), xpScarabsData.getXpScarabLazotep()), (x + 200), (y + 417), 4269832);
        }

        if (effectData.isCurseKnef()) {
            if (!screenID.equals(IAbilityData.ScreenID.GLOBAL)) {
                bonusInfo.add(Component.literal(" "));
                bonusInfo.add(Component.translatable("gui.thirteen_flames.curse_knef").withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.curse_knef"), (x + 200), (y + 429), 11141290);
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
            poseStack.pushPose();

            MC.font.draw(poseStack, entry, (x + 200), (y + 376 + yOff), 4269832);

            yOff += 9;

            poseStack.popPose();
        }

        poseStack.popPose();

        RenderSystem.setShaderTexture(0, BACKGROUNG);
        manager.bindForSetup(BACKGROUNG);

        if (!screenID.equals(IAbilityData.ScreenID.GLOBAL) && !isMaxLevel) {
            poseStack.pushPose();

            poseStack.scale(0.45F, 0.45F, 0.45F);

            int extraYOff = 0;

            if (!effectData.isCurseKnef())
                extraYOff = 27;

            blit(poseStack, (x + 752), (y + 618 + extraYOff), 197, 347, 22, 24, texWidth, texHeight);

            poseStack.scale(0.48F, 0.48F, 0.48F);

            poseStack.popPose();
        } else if (screenID.equals(IAbilityData.ScreenID.GLOBAL) && guiLeveling.isPlayerScreen()) {
            poseStack.pushPose();

            poseStack.scale(1.4F, 1.4F, 1.4F);

            this.MC.font.draw(poseStack, Component.translatable("gui.thirteen_flames.xp"), x - 5 - this.font.width(Component.translatable("gui.thirteen_flames.xp")) / 2.0F, (y + 178), 0x673E09);
            this.MC.font.draw(poseStack, Component.literal("__"), x - 12, (y + 182), 0x673E09);
            this.MC.font.draw(poseStack, String.valueOf(player.totalExperience), x - 6 - this.font.width(String.valueOf(player.totalExperience)) / 2.0F, (y + 193), 0x673E09);

            poseStack.popPose();
        }

        super.render( poseStack, pMouseX, pMouseY, pPartialTick );
        Iterator var33 = this.children().iterator();

        while (var33.hasNext()) {
            GuiEventListener listener = (GuiEventListener) var33.next();
            if (listener instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) listener;
                if (button.isHoveredOrFocused() && button instanceof IHoveredWidget) {
                    IHoveredWidget widget = (IHoveredWidget) button;
                    widget.onHovered( poseStack, pMouseX, pMouseY );
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
