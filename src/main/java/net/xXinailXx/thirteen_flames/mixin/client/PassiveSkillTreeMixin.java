package net.xXinailXx.thirteen_flames.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import daripher.skilltree.client.screen.SkillTreeScreen;
import daripher.skilltree.client.widget.SkillButton;
import daripher.skilltree.client.widget.SkillTreeButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(SkillTreeScreen.class)
public abstract class PassiveSkillTreeMixin extends Screen {
    @Unique private static final ResourceLocation SCARABS_SILVER = new ResourceLocation( ThirteenFlames.MODID, "textures/gui/god_faraon_background_screen.png");
    @Unique private Minecraft MC = Minecraft.getInstance();
    @Shadow public abstract void addSkillConnections();
    @Shadow protected int maxScrollX;
    @Shadow protected int maxScrollY;
    @Shadow private AbstractWidget progressBar;
    @Shadow private AbstractWidget buySkillButton;
    @Shadow protected abstract void updateScreen(float partialTick);
    @Shadow public float renderAnimation;
    @Shadow protected abstract void renderConnections(PoseStack poseStack, int mouseX, int mouseY, float partialTick);
    @Shadow protected double scrollX;
    @Shadow protected double scrollY;
    @Shadow protected abstract void renderOverlay(PoseStack poseStack, int mouseX, int mouseY, float partialTick);
    @Shadow public abstract void renderButtonTooltip(Button button, PoseStack poseStack, int mouseX, int mouseY);
    @Shadow public static void prepareTextureRendering(ResourceLocation textureLocation) {}
    @Shadow protected abstract void initSkillsIfNeeded();
    @Shadow protected abstract void highlightSkillsThatCanBeLearned();
    @Shadow public int skillPoints;
    @Shadow public abstract void addSkillButtons();
    @Unique private static final IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
    @Unique private static final IData.IScarabsData scarabsData = new Data.ScarabsData.Utils();
    @Unique private static final boolean rebuild = false;

    public PassiveSkillTreeMixin() {
        super(Component.empty());
    }

    @Overwrite
    public void init() {
        this.clearWidgets();
        this.initSkillsIfNeeded();
        this.addSkillButtons();
        this.maxScrollX -= this.width / 2 - 80;
        this.maxScrollY -= this.height / 2 - 80;

        if (this.maxScrollX < 0)
            this.maxScrollX = 0;

        if (this.maxScrollY < 0)
            this.maxScrollY = 0;

        this.addSkillConnections();
        this.highlightSkillsThatCanBeLearned();
        this.buySkillButton = new SkillTreeButton(0, 0, 0, 0, Component.empty(), Button::onPress);
    }

    @Overwrite
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.updateScreen(partialTick);
        this.renderAnimation += partialTick;
        this.renderBackground(poseStack);
        this.renderConnections(poseStack, mouseX, mouseY, partialTick);

        poseStack.pushPose();
        poseStack.translate(this.scrollX, this.scrollY, 0.0);

        Iterator var5 = this.renderables.iterator();

        while(var5.hasNext()) {
            Widget widget = (Widget)var5.next();

            if (widget != this.progressBar && widget != this.buySkillButton)
                widget.render(poseStack, mouseX, mouseY, partialTick);
        }

        poseStack.popPose();

        this.renderOverlay(poseStack, mouseX, mouseY, partialTick);
        this.prepareTextureRendering(SCARABS_SILVER);

        blit(poseStack, this.width / 2 - 12, this.height - 50, 103.0F, 347.0F, 23, 24, 512, 512);

        MutableComponent pointsLeft = Component.literal("" + this.skillPoints);

        drawCenteredString(poseStack, this.font, pointsLeft, this.width / 2, this.height - 15, 16777215);

        Optional optional = this.getChildAt((double)mouseX, (double)mouseY);
        Objects.requireNonNull(SkillButton.class);
        optional = optional.filter(SkillButton.class::isInstance);
        Objects.requireNonNull(SkillButton.class);
        optional.map(SkillButton.class::cast).ifPresent((button) -> this.renderButtonTooltip((Button) button, poseStack, mouseX, mouseY));

        if (this.skillPoints != scarabsData.getScarabSilver(MC.player)) {
            this.skillPoints = scarabsData.getScarabSilver(MC.player);

            initSkillsIfNeeded();
        }
    }

    @Inject(method = "skillButtonPressed", at = @At(value = "INVOKE", target = "Ldaripher/skilltree/client/screen/SkillTreeScreen;learnSkill(Ldaripher/skilltree/skill/PassiveSkill;)V"), remap = false, cancellable = true)
    protected void skillButtonPressed(SkillButton button, CallbackInfo ci) {
        if (guiLevelingData.isPlayerScreen(MC.player))
            ci.cancel();
    }

    @Inject(method = "renderConnection", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0), cancellable = true)
    private void renderConnection(PoseStack poseStack, Pair<SkillButton, SkillButton> connection, CallbackInfo ci) {
        if (guiLevelingData.isPlayerScreen(MC.player)) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            ci.cancel();
        }
    }
}
