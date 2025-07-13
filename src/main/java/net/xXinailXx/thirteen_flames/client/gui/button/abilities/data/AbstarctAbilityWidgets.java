package net.xXinailXx.thirteen_flames.client.gui.button.abilities.data;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.sskirillss.relics.client.screen.base.IHoverableWidget;
import lombok.EqualsAndHashCode;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.PlayerCapManager;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidget;
import net.xXinailXx.enderdragonlib.utils.GuiUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.global.GiftGodPharaoh;
import net.xXinailXx.thirteen_flames.config.ThirteenFlamesConfig;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.network.packet.AbilityWidgetActionPacket;
import org.zeith.hammerlib.net.Network;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Mod.EventBusSubscriber
public abstract class AbstarctAbilityWidgets extends AbstractWidget implements IHoverableWidget, IAbilityData {
    private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(ThirteenFlames.MODID, "textures/gui/god_faraon_background_screen.png");
    private final ResourceLocation ABILITY_ICON = new ResourceLocation(ThirteenFlames.MODID, "textures/gui/ability/" + getScreenId() + "/" + getAbilityData().getAbilityName() + ".png");
    protected static final IData.IAbilitiesData data = new Data.AbilitiesData.Utils();
    protected static final IData.IEffectData effectData = new Data.EffectData.Utils();
    protected static final IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
    protected static final IData.IScarabsData scarabsData = new Data.ScarabsData.Utils();
    private final int locationNumber;
    private final boolean isConfigBlock;

    public AbstarctAbilityWidgets(int x, int y, int locationNumber) {
        this(x, y, locationNumber, false);
    }

    public AbstarctAbilityWidgets(int x, int y, int locationNumber, boolean isConfigBlock) {
        super(x, y, 32, 32);
        this.locationNumber = locationNumber;
        this.isConfigBlock = isConfigBlock;
    }

    public void renderButton(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        TextureManager manager = MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, ABILITY_ICON);
        manager.bindForSetup(ABILITY_ICON);

        poseStack.pushPose();
        poseStack.scale(1F, 1F, 1F);

        if (!isBuyAbility() || isLockAbility() || getScreenLevel() < getAbilityData().getRequiredLevel())
            RenderSystem.setShaderColor(0.75F, 0.75F, 0.75F, 1F);

        blit(poseStack, this.x + 5, this.y + 5, 0, 0, 24, 24, 24, 24);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, GUI_BACKGROUND);
        manager.bindForSetup(GUI_BACKGROUND);

        if (this.isHovered) {
            if (getScreenLevel() >= getAbilityData().getRequiredLevel() || !isBuyAbility())
                blit(poseStack, this.x - 2, this.y - 2, 454, 167, 38, 38, 512, 512);
            else
                blit(poseStack, this.x - 2, this.y - 2, 454, 212, 38, 38, 512, 512);
        }

        if (isUnlock()) {
            if (isBuyAbility()) {
                if (isActiveAbility())
                    blit(poseStack, this.x + 1, this.y + 1, 412, 215, 32, 32, 512, 512);
                else
                    blit(poseStack, this.x + 1, this.y + 1, 376, 215, 32, 32, 512, 512);
            } else {
                blit(poseStack, this.x + 1, this.y + 1, 412, 170, 32, 32, 512, 512);
            }
        } else {
            blit(poseStack, this.x, this.y + 1, 375, 170, 34, 32, 512, 512);
        }

        poseStack.popPose();
    }

    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        if (getLevelAbility() < 1)
            setLevelAbility(1);
        else if (getLevelAbility() > getAbilityData().getMaxLevel())
            setLevelAbility(getAbilityData().getMaxLevel());

        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
    }

    public void onPress() {
        if (!guiLevelingData.isPlayerScreen(MC.player)) {
            if (isUnlock()) {
                if (isBuyAbility()) {
                    if (!isActiveAbility()) {
                        if (Screen.hasShiftDown()) {
                            setActiveAbility(true);
                        } else {
                            if (getLevelAbility() < getAbilityData().getMaxLevel()) {
                                if (!getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
                                    if (scarabsData.getScarabAuriteh(MC.player) >= getAbilityData().getRequiredScarabsForUpgrade()) {
                                        scarabsData.addScarabAuriteh(MC.player, -getAbilityData().getRequiredScarabsForUpgrade());
                                        addLevelAbility(1);
                                    }
                                } else {
                                    if (scarabsData.getScarabLazotep(MC.player) >= getAbilityData().getRequiredScarabsForUpgrade()) {
                                        scarabsData.addScarabLazotep(MC.player, -getAbilityData().getRequiredScarabsForUpgrade());
                                        addLevelAbility(1);
                                    }
                                }
                            }
                        }
                    } else {
                        if (Screen.hasShiftDown()) {
                            setActiveAbility(false);
                        } else {
                            if (getLevelAbility() < getAbilityData().getMaxLevel()) {
                                if (!getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
                                    if (scarabsData.getScarabAuriteh(MC.player) >= getAbilityData().getRequiredScarabsForUpgrade()) {
                                        scarabsData.addScarabAuriteh(MC.player, -getAbilityData().getRequiredScarabsForUpgrade());
                                        addLevelAbility(1);
                                    }
                                } else {
                                    if (scarabsData.getScarabLazotep(MC.player) >= getAbilityData().getRequiredScarabsForUpgrade()) {
                                        scarabsData.addScarabLazotep(MC.player, -getAbilityData().getRequiredScarabsForUpgrade());
                                        addLevelAbility(1);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (!getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
                        if (scarabsData.getScarabAuriteh(MC.player) >= getAbilityData().getRequiredScarabsForOpen()) {
                            scarabsData.addScarabAuriteh(MC.player, -getAbilityData().getRequiredScarabsForOpen());
                            setBuyAbility(true);
                            setActiveAbility(false);
                            setLevelAbility(1);
                        }
                    } else {
                        if (scarabsData.getScarabLazotep(MC.player) >= getAbilityData().getRequiredScarabsForOpen()) {
                            scarabsData.addScarabLazotep(MC.player, -getAbilityData().getRequiredScarabsForOpen());
                            setBuyAbility(true);
                            setActiveAbility(false);
                            setLevelAbility(1);
                        }
                    }
                }
            }
        }
    }

    public void onHovered(PoseStack poseStack, int i, int i1) {
        List<FormattedCharSequence> tooltip = Lists.newArrayList();

        MutableComponent screenName = null;
        int maxWidth = 170;
        int renderWidth = 0;
        int renderX = this.x + 32;
        int renderY = this.y - 20;
        int renderIconYOff = 0;

        switch (getAbilityData().getScreenID()) {
            case MINING -> screenName = Component.translatable("gui.thirteen_flames.id.mining");
            case CRAFT -> screenName = Component.translatable("gui.thirteen_flames.id.craft");
            case FIGHT -> screenName = Component.translatable("gui.thirteen_flames.id.fight");
            case HEALTH -> screenName = Component.translatable("gui.thirteen_flames.id.health");
        }

        List<MutableComponent> entries = Lists.newArrayList(new MutableComponent[]{});
        entries.add(Component.translatable("button.thirteen_flames." + getScreenId() + "." + getAbilityData().getAbilityName() + ".name"));
        entries.add(Component.literal("§l_________________________________________"));
        entries.add(Component.literal(" "));

        MutableComponent component = Component.translatable("button.thirteen_flames." + getScreenId() + "." + getAbilityData().getAbilityName() + ".description");

        if (getAbilityData().getAbilityName().equals("egyptian_strength")) {
            component.append(String.valueOf(getTime()));
            component.append(Component.translatable("button.thirteen_flames." + getScreenId() + "." + getAbilityData().getAbilityName() + ".description_2"));
        }

        entries.add(component);
        entries.add(Component.literal("§l_________________________________________"));
        entries.add(Component.literal(" "));
        entries.add(Component.literal(" "));

        if (isUnlock()) {
            if (isBuyAbility()) {
                entries.add(Component.translatable("button.thirteen_flames.button.total_level", new Object[]{getLevelAbility(), getAbilityData().getMaxLevel()}));
                entries.add(Component.literal(" "));

                renderIconYOff += 2;

                if (getLevelAbility() == getAbilityData().getMaxLevel()) {
                    entries.add(Component.translatable("button.thirteen_flames.button.max_level"));
                    entries.add(Component.literal(" "));

                    renderIconYOff += 2;
                } else {
                    entries.add(Component.translatable("button.thirteen_flames.button.cost_upgrade", getAbilityData().getRequiredScarabsForUpgrade()));
                    entries.add(Component.literal(" "));
                    entries.add(Component.translatable("button.thirteen_flames.button.upgrade"));

                    renderIconYOff += 1;
                }

                if (isActiveAbility())
                    entries.add(Component.translatable("button.thirteen_flames.button.off"));
                else
                    entries.add(Component.translatable("button.thirteen_flames.button.on"));
            } else {
                entries.add(Component.translatable("button.thirteen_flames.button.cost_open", getAbilityData().getRequiredScarabsForOpen()));
                entries.add(Component.literal(" "));
                entries.add(Component.translatable("button.thirteen_flames.button.open"));

                renderIconYOff += 1;
            }
        } else {
            if (isLockAbility())
                entries.add(Component.translatable("button.thirteen_flames.button.command_lock"));
            else
                entries.add(Component.translatable("button.thirteen_flames.button.lock", new Object[]{screenName, getAbilityData().getRequiredLevel()}));
        }

        for (MutableComponent entry : entries) {
            int entryWidth = (MC.font.width(entry)) / 2;

            if (entryWidth > renderWidth)
                renderWidth = Math.min(entryWidth, maxWidth);

            tooltip.addAll(MC.font.split(entry, (maxWidth - 20) * 2));
        }

        int height = Math.round(tooltip.size() * 4.5F);

        GuiUtils.drawTexturedTooltipBorder(poseStack, new ResourceLocation(ThirteenFlames.MODID, "textures/gui/border/sand.png"), renderX, renderY, maxWidth, height);

        int yOff = 0;

        for (FormattedCharSequence entry : tooltip) {
            poseStack.pushPose();

            poseStack.scale(0.58F, 0.58F, 0.58F);

            MC.font.draw(poseStack, entry, (renderX * 1.724F + 15), (renderY * 1.724F + 15 + yOff), 4269832);

            yOff += 8;

            poseStack.popPose();
        }

        poseStack.pushPose();

        poseStack.scale(0.5F, 0.5F, 0.5F);

        List<FormattedCharSequence> description = Lists.newArrayList();

        description.addAll(MC.font.split(component, (maxWidth - 20) * 2));

        int descriptionLines = (description.size() > 1 ? description.size() - 1 : 0);

        int renderIconX = (renderX * 2 + 246);
        int renderIconY = (renderY * 2 + 70 + renderIconYOff * 8 + descriptionLines * 8 + (getAbilityData().getAbilityName().equals("grain_grower") ? 8 : 0));

        if (isUnlock()) {
            if (!isBuyAbility()) {
                if (!getAbilityData().getScreenID().equals(ScreenID.GLOBAL))
                    GuiUtils.drawTexture(poseStack, GUI_BACKGROUND, renderIconX, renderIconY - 2, 100, 373, 28, 24, 512, 512);
                else
                    GuiUtils.drawTexture(poseStack, GUI_BACKGROUND, renderIconX, renderIconY, 131, 373, 28, 28, 512, 512);
            } else {
                if (getLevelAbility() != getAbilityData().getMaxLevel()) {
                    if (!getAbilityData().getScreenID().equals(ScreenID.GLOBAL))
                        GuiUtils.drawTexture(poseStack, GUI_BACKGROUND, renderIconX - 25, renderIconY - 3, 100, 373, 28, 24, 512, 512);
                    else
                        GuiUtils.drawTexture(poseStack, GUI_BACKGROUND, renderIconX - 26, renderIconY - 2, 131, 373, 28, 28, 512, 512);
                }
            }
        }

        poseStack.popPose();
    }

    private int getTime() {
        if (getAbilityData().getAbilityName().equals("egyptian_strength")) {
            int time = PlayerCapManager.getPlayerTimers(MC.player).contains("tf_ability_egyptian_strength") ? PlayerCapManager.getPlayerTimer(MC.player, "tf_ability_egyptian_strength") : 0;

            if (time % 20 != 0)
                time -= time % 20;

            return time / 20;
        }

        return 0;
    }

    private int getExtraYOff() {
        if (this.locationNumber < 5)
            return 0;
        else if (this.locationNumber >= 5 && this.locationNumber < 10)
            return 1;
        else if (this.locationNumber >= 10 && this.locationNumber < 15)
            return 2;
        else if (this.locationNumber >= 15 && this.locationNumber < 20)
            return 3;

        return 0;
    }

    private boolean isUnlock() {
        if (isLockAbility() || (this.isConfigBlock && !ThirteenFlamesConfig.STAMINA_ACTIVE.get()))
            return false;

        if (this instanceof GiftGodPharaoh)
            if (ModList.get().isLoaded("pocket_dimension"))
                return true;
            else
                return false;

        return getScreenLevel() >= getAbilityData().getRequiredLevel() || getAbilityData().getScreenID().equals(ScreenID.GLOBAL);
    }

    public boolean isLockAbility() {
        return data.isLockAbility(MC.player, getAbilityData().getAbilityName()) || (this.isConfigBlock && !ThirteenFlamesConfig.STAMINA_ACTIVE.get());
    }

    public boolean isBuyAbility() {
        return data.isBuyAbility(MC.player, getAbilityData().getAbilityName());
    }

    public boolean isActiveAbility() {
        return data.isActiveAbility(MC.player, getAbilityData().getAbilityName());
    }

    public int getLevelAbility() {
        return data.getLevelAbility(MC.player, getAbilityData().getAbilityName());
    }

    public void setBuyAbility(boolean value) {
        Network.sendToServer(new AbilityWidgetActionPacket(getAbilityData().getAbilityName(), 0, value, -1));
    }

    public void setActiveAbility(boolean value) {
        Network.sendToServer(new AbilityWidgetActionPacket(getAbilityData().getAbilityName(), 1, value, -1));

        if (getAbilityData().getAbilityName().equals("recovery"))
            if (value)
                guiLevelingData.setProcentCurse(MC.player, 70 - getLevelAbility() * 5);
            else
            guiLevelingData.setProcentCurse(MC.player, 70);
    }

    public void setLevelAbility(int amount) {
        Network.sendToServer(new AbilityWidgetActionPacket(getAbilityData().getAbilityName(), 2, false, amount));

        if (getAbilityData().getAbilityName().equals("recovery") && isActiveAbility())
            guiLevelingData.setProcentCurse(MC.player, 70 - amount * 5);
    }

    public void addLevelAbility(int amount) {
        Network.sendToServer(new AbilityWidgetActionPacket(getAbilityData().getAbilityName(), 3, false, amount));

        if (getAbilityData().getAbilityName().equals("recovery") && isActiveAbility())
            guiLevelingData.setProcentCurse(MC.player, guiLevelingData.getProcentCurse(MC.player) - 5);
    }
}
