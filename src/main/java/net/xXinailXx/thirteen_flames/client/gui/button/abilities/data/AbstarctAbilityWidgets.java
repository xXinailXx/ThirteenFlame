package net.xXinailXx.thirteen_flames.client.gui.button.abilities.data;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.EqualsAndHashCode;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.dragonworldlib.client.utils.gui.AbstractWidgetUtils;
import net.xXinailXx.dragonworldlib.interfaces.IHoveredWidget;
import net.xXinailXx.dragonworldlib.utils.GuiUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;

import java.util.List;

@EqualsAndHashCode
@Mod.EventBusSubscriber
public abstract class AbstarctAbilityWidgets extends AbstractWidgetUtils implements IHoveredWidget, IAbilityData {
    private static final ResourceLocation FRAME_BUTTON = new ResourceLocation(ThirteenFlames.MODID, "textures/gui/god_faraon_background_screen.png");
    private final ResourceLocation ABILITY_ICON = new ResourceLocation(ThirteenFlames.MODID, "textures/gui/ability/" + getScreenId() + "/" + getAbilityData().getAbilityName() + ".png");
    protected MutableComponent abilityName = Component.translatable("button.thirteen_flames." + getScreenId() + "." + getAbilityData().getAbilityName() + ".name");
    protected static final IData.IAbilitiesData data = new Data.AbilitiesData.Utils();
    protected static final IData.IEffectData effectData = new Data.EffectData();
    protected static final IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData();
    protected static final IData.IScarabsData scarabsData = new Data.ScarabsData();
    private final int locationNumber;

    public AbstarctAbilityWidgets(int x, int y, int locationNumber) {
        super(x, y, 32, 32);
        this.locationNumber = locationNumber;
    }

    @Override
    public void renderButton(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        TextureManager manager = MC.getTextureManager();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, ABILITY_ICON);
        manager.bindForSetup(ABILITY_ICON);

        poseStack.pushPose();
        poseStack.scale(1F, 1F, 1F);

        if (!isBuyAbility()) {
            RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 1F);
        }

        blit(poseStack, this.x + 5, this.y + 5, 0, 0, 24, 24, 24, 24);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, FRAME_BUTTON);
        manager.bindForSetup(FRAME_BUTTON);

        if (this.isHovered) {
            if (getScreenLevel() >= getAbilityData().getRequiredLevel() || !isBuyAbility()) {
                blit(poseStack, this.x - 2, this.y - 2, 454, 167, 38, 38, 512, 512);
            } else {
                blit(poseStack, this.x - 2, this.y - 2, 454, 212, 38, 38, 512, 512);
            }
        }

        if (getScreenLevel() >= getAbilityData().getRequiredLevel() || getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
            if (isBuyAbility()) {
                if (isActiveAbility()) {
                    blit(poseStack, this.x + 1, this.y + 1, 412, 215, 32, 32, 512, 512);
                } else {
                    blit(poseStack, this.x + 1, this.y + 1, 376, 215, 32, 32, 512, 512);
                }
            } else {
                blit(poseStack, this.x + 1, this.y + 1, 412, 170, 32, 32, 512, 512);
            }
        } else {
            blit(poseStack, this.x, this.y + 1, 375, 170, 34, 32, 512, 512);
        }

        poseStack.popPose();
    }

    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void onPress() {
        if (!guiLevelingData.isPlayerScreen()) {
            if (getScreenLevel() >= getAbilityData().getRequiredLevel() || getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
                if (isBuyAbility()) {
                    if (! isActiveAbility()) {
                        if (Screen.hasShiftDown()) {
                            setActiveAbility( true );
                        } else {
                            if (getLevelAbility() < getAbilityData().getMaxLevel()) {
                                if (! getAbilityData().getScreenID().equals( ScreenID.GLOBAL )) {
                                    if (scarabsData.getScarabAuriteh() >= getAbilityData().getRequiredScarabsForUpgrade()) {
                                        scarabsData.addScarabAuriteh(-getAbilityData().getRequiredScarabsForUpgrade());
                                        addLevelAbility( 1 );
                                    }
                                } else {
                                    if (scarabsData.getScarabLazotep() >= getAbilityData().getRequiredScarabsForUpgrade()) {
                                        scarabsData.addScarabLazotep(-getAbilityData().getRequiredScarabsForUpgrade());
                                        addLevelAbility( 1 );
                                    }
                                }
                            }
                        }
                    } else {
                        if (Screen.hasShiftDown()) {
                            setActiveAbility( false );
                        } else {
                            if (getLevelAbility() < getAbilityData().getMaxLevel()) {
                                if (! getAbilityData().getScreenID().equals( ScreenID.GLOBAL )) {
                                    if (scarabsData.getScarabAuriteh() >= getAbilityData().getRequiredScarabsForUpgrade()) {
                                        scarabsData.addScarabAuriteh(-getAbilityData().getRequiredScarabsForUpgrade());
                                        addLevelAbility( 1 );
                                    }
                                } else {
                                    if (scarabsData.getScarabLazotep() >= getAbilityData().getRequiredScarabsForUpgrade()) {
                                        scarabsData.addScarabLazotep(-getAbilityData().getRequiredScarabsForUpgrade());
                                        addLevelAbility( 1 );
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (!getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
                        if (scarabsData.getScarabAuriteh() >= getAbilityData().getRequiredScarabsForOpen()) {
                            scarabsData.addScarabAuriteh(-getAbilityData().getRequiredScarabsForOpen());
                            setBuyAbility(true);
                            setActiveAbility(false);
                            setLevelAbility(1);
                        }
                    } else {
                        if (scarabsData.getScarabLazotep() >= getAbilityData().getRequiredScarabsForOpen()) {
                            scarabsData.addScarabLazotep(-getAbilityData().getRequiredScarabsForOpen());
                            setBuyAbility(true);
                            setActiveAbility(false);
                            setLevelAbility(1);
                        }
                    }
                }
            }
        }
    }

    @Override
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
        entries.add(abilityName);
        entries.add(Component.literal("§l_________________________________________"));
        entries.add(Component.literal(" "));
        entries.add(Component.translatable("button.thirteen_flames." + getScreenId() + "." + getAbilityData().getAbilityName() + ".description"));
        entries.add(Component.literal("§l_________________________________________"));
        entries.add(Component.literal(" "));
        entries.add(Component.literal(" "));

        if (getScreenLevel() >= getAbilityData().getRequiredLevel() || getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
            if (isBuyAbility()) {
                entries.add(Component.translatable("button.thirteen_flames.button.total_level", new Object[]{getLevelAbility(), getAbilityData().getMaxLevel()}));
                entries.add(Component.literal(" " ));
                renderIconYOff += 2;

                if (getLevelAbility() == getAbilityData().getMaxLevel()) {
                    entries.add(Component.translatable("button.thirteen_flames.button.max_level"));
                    entries.add(Component.literal(" "));
                    renderIconYOff += 2;
                } else {
                    entries.add(Component.translatable("button.thirteen_flames.button.cost_upgrade", getAbilityData().getRequiredScarabsForUpgrade()));
                    entries.add(Component.literal(" "));
                    entries.add(Component.translatable("button.thirteen_flames.button.upgrade"));
                    renderIconYOff += 3;
                }

                if (isActiveAbility()) {
                    entries.add(Component.translatable("button.thirteen_flames.button.off"));
                } else {
                    entries.add(Component.translatable("button.thirteen_flames.button.on"));
                }
                renderIconYOff++;
            } else {
                entries.add(Component.translatable("button.thirteen_flames.button.cost_open", getAbilityData().getRequiredScarabsForOpen()));
                entries.add(Component.literal(" "));
                entries.add(Component.translatable("button.thirteen_flames.button.open"));
            }
        } else {
            entries.add(Component.translatable("button.thirteen_flames.button.lock", new Object[]{screenName, getAbilityData().getRequiredLevel()}));
        }

        for (MutableComponent entry : entries) {
            int entryWidth = (MC.font.width(entry)) / 2;

            if (entryWidth > renderWidth)
                renderWidth = Math.min(entryWidth, maxWidth);

            tooltip.addAll(MC.font.split(entry, (maxWidth - 20) * 2));
        }

        int height = Math.round(tooltip.size() * 4.5F);

        poseStack.scale(1F, 1F, 1F);

        int extraXOff = getExtraXOff();

        int extraYOff = getExtraYOff();

        GuiUtils.drawTexturedTooltipBorder(poseStack, new ResourceLocation(ThirteenFlames.MODID, "textures/gui/border/sand.png"), renderX, renderY, maxWidth, height);

        int yOff = 0;

        for (FormattedCharSequence entry : tooltip) {
            poseStack.pushPose();

            poseStack.scale(0.58F, 0.58F, 0.58F);

            MC.font.draw(poseStack, entry, (renderX * 2 - 57 - (extraXOff * 10)), (renderY + 63 + (extraYOff * 26 + (extraYOff > 1 ? extraYOff : 0)) + yOff), 4269832);

            yOff += 8;

            poseStack.popPose();
        }

        poseStack.pushPose();

        poseStack.scale(0.5F, 0.5F, 0.5F);

        List<FormattedCharSequence> description = Lists.newArrayList();

        description.addAll(MC.font.split(Component.translatable("button.thirteen_flames." + getScreenId() + "." + getAbilityData().getAbilityName() + ".description"), (maxWidth - 20) * 2));

        int descriptionLines = (description.size() > 1 ? description.size() : 0);

        if (descriptionLines % 2 == 0) {
            descriptionLines = descriptionLines * 5;
        } else {
            descriptionLines = descriptionLines * 6;
        }

        int extraY = 0;

        switch (extraYOff) {
            case 2 -> extraY = 3;
            case 3 -> extraY = 13;
        }

        int renderIconX = (renderX * 2 + 246);
        int renderIconY = (renderY + 142 + (renderIconYOff * 3 + (renderIconYOff / 3)) + descriptionLines + (extraYOff * 36) + extraY);

        description.clear();

        if (getScreenLevel() >= getAbilityData().getRequiredLevel() || getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
            if (!isBuyAbility()) {
                if (!getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
                    GuiUtils.drawTexture(poseStack, FRAME_BUTTON, renderIconX, renderIconY, 100, 373, 28, 24, 512, 512);
                } else {
                    GuiUtils.drawTexture(poseStack, FRAME_BUTTON, renderIconX, renderIconY - 4, 131, 373, 28, 28, 512, 512);
                }
            } else {
                if (getLevelAbility() != getAbilityData().getMaxLevel()) {
                    if (!getAbilityData().getScreenID().equals(ScreenID.GLOBAL)) {
                        GuiUtils.drawTexture(poseStack, FRAME_BUTTON, renderIconX - 25, renderIconY - 2, 100, 373, 28, 24, 512, 512);
                    } else {
                        GuiUtils.drawTexture(poseStack, FRAME_BUTTON, renderIconX - 26, renderIconY - 5, 131, 373, 28, 28, 512, 512);
                    }
                }
            }
        }

        poseStack.scale(1F, 1F, 1F);
        poseStack.popPose();
    }

    public Data.AbilitiesData.Handler getInfo() {
        return data.getAbilityHandler(getAbilityData().getAbilityName());
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int getExtraXOff() {
        int xOff = 0;

        if (locationNumber < 5) {
            xOff = locationNumber;
        } else if (locationNumber >= 5 && locationNumber < 10) {
            xOff = locationNumber - 5;
        } else if (locationNumber >= 10 && locationNumber < 15) {
            xOff = locationNumber - 10;
        } else if (locationNumber >= 15 && locationNumber < 20) {
            xOff = locationNumber - 15;
        }
        return xOff;
    }

    private int getExtraYOff() {
        int yOff = 0;

        if (locationNumber < 5) {
            return 0;
        } else if (locationNumber >= 5 && locationNumber < 10) {
            return 1;
        } else if (locationNumber >= 10 && locationNumber < 15) {
            return 2;
        } else if (locationNumber >= 15 && locationNumber < 20) {
            return 3;
        }

        return 0;
    }

    public boolean isBuyAbility() {
        return data.isBuyAbility(getAbilityData().getAbilityName());
    }

    public boolean isActiveAbility() {
        return data.isActiveAbility(getAbilityData().getAbilityName());
    }

    public int getLevelAbility() {
        return data.getLevelAbility(getAbilityData().getAbilityName());
    }

    public void setBuyAbility(boolean value) {
        data.setBuyAbility(getAbilityData().getAbilityName(), value);
    }

    public void setActiveAbility(boolean value) {
        data.setActiveAbility(getAbilityData().getAbilityName(), value);
    }

    public void setLevelAbility(int amount) {
        data.setLevelAbility(getAbilityData().getAbilityName(), amount);
    }

    public void addLevelAbility(int amount) {
        data.addLevelAbility(getAbilityData().getAbilityName(), amount, getAbilityData().getMaxLevel());
    }
}
