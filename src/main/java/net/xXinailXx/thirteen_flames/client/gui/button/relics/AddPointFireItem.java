package net.xXinailXx.thirteen_flames.client.gui.button.relics;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.sskirillss.relics.client.screen.base.IHoverableWidget;
import it.hurts.sskirillss.relics.client.screen.description.RelicDescriptionScreen;
import it.hurts.sskirillss.relics.client.screen.description.widgets.base.AbstractDescriptionWidget;
import it.hurts.sskirillss.relics.client.screen.utils.ScreenUtils;
import it.hurts.sskirillss.relics.items.relics.base.RelicItem;
import it.hurts.sskirillss.relics.items.relics.base.data.base.RelicData;
import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.network.packet.AddExpFlamePacket;
import org.zeith.hammerlib.net.Network;

import java.util.Iterator;
import java.util.List;

public class AddPointFireItem extends AbstractDescriptionWidget implements IHoverableWidget {
    public static final ResourceLocation ADD_POINT_FIRE = new ResourceLocation(ThirteenFlames.MODID, "textures/gui/add_point_fire.png");
    private static final IData.IScarabsData scarabsData = new Data.ScarabsData();
    private final Player player = MC.player;
    private final ItemStack stack;
    private final BlockPos pos;

    public AddPointFireItem(int x, int y, ItemStack stack, BlockPos pos) {
        super(x, y, 83, 29);
        this.stack = stack;
        this.pos = pos;
    }

    public void onPress() {
        if (scarabsData.getScarabSilver(this.player) >= 1) {
            scarabsData.addScarabSilver(this.player, -1);

            LevelingUtils.addExperience(this.stack, 50);
            Network.sendToServer(new AddExpFlamePacket(this.pos));

            if (MC.screen instanceof RelicDescriptionScreen)
                MC.setScreen(new RelicDescriptionScreen(this.pos, this.stack));
        }
    }

    public void renderButton(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        TextureManager manager = this.MC.getTextureManager();
        manager.bindForSetup(ADD_POINT_FIRE);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, ADD_POINT_FIRE);

        poseStack.pushPose();

        blit(poseStack, this.x, this.y + 2, 0.0F, 0.0F, 79, 22, 512, 512);
        blit(poseStack, this.x, this.y, 0.0f, 24.0f, 83, 29, 512, 512);

        if (this.isHovered)
            blit(poseStack, this.x, this.y - 1, 85.0f, 0.0f, 85, 31, 512, 512);

        poseStack.scale(1.2F, 1.2F, 1.2F);

        drawString(poseStack, MC.font, String.valueOf(50), this.x - MC.font.width(String.valueOf(50)) / 2 - 20, this.y - 16, 16777215);

        poseStack.scale(1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    public void onHovered(PoseStack poseStack, int i, int i1) {
        Item item = this.stack.getItem();

        if (item instanceof RelicItem relic) {
            RelicData relicData = relic.getRelicData();
            List<FormattedCharSequence> tooltip = Lists.newArrayList();
            int maxWidth = 100;
            int renderWidth = 0;
            int scarabsSilver = scarabsData.getScarabSilver(this.player);
            int levelRelic = LevelingUtils.getLevel(this.stack);
            int maxLevelRelic = relicData.levelingData.getMaxLevel();
            boolean isMaxLevelRelic = levelRelic >= maxLevelRelic ? true : false;
            List<MutableComponent> entries = Lists.newArrayList(new MutableComponent[]{});

            if (!(isMaxLevelRelic))
                entries.add(Component.translatable("gui.thirteen_flames.relic.button_add_point.add_level", String.valueOf(scarabsSilver)));
            else
                entries.add(Component.translatable("gui.thirteen_flames.relic.button_add_point.max_level"));

            MutableComponent entry;
            int renderY;

            for(Iterator var15 = entries.iterator(); var15.hasNext(); tooltip.addAll(this.MC.font.split(entry, maxWidth * 2))) {
                entry = (MutableComponent)var15.next();
                renderY = (this.MC.font.width(entry) + 4) / 2;

                if (renderY > renderWidth)
                    renderWidth = Math.min(renderY, maxWidth);
            }

            int height = tooltip.size() * 4;
            int renderX = this.x + this.width - 105;
            renderY = this.y - height / 2 + 42;

            ScreenUtils.drawTexturedTooltipBorder(poseStack, new ResourceLocation("relics", "textures/gui/tooltip/border/paper.png"), renderWidth, height, renderX, renderY);

            int yOff = 0;

            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);

            for(Iterator var19 = tooltip.iterator(); var19.hasNext(); yOff += 4) {
                FormattedCharSequence entrys = (FormattedCharSequence)var19.next();
                this.MC.font.draw(poseStack, entrys, (float)((renderX + 9) * 2), (float)((renderY + 9 + yOff) * 2), 4269832);
            }

            poseStack.popPose();
        }
    }
}
