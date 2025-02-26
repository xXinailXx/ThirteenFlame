package net.xXinailXx.thirteen_flames.mixin.client;

import it.hurts.sskirillss.relics.client.screen.description.AbilityDescriptionScreen;
import it.hurts.sskirillss.relics.client.screen.description.RelicDescriptionScreen;
import it.hurts.sskirillss.relics.items.relics.base.RelicItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.AddPointFireItem;
import net.xXinailXx.thirteen_flames.utils.FireItemSetting;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbilityDescriptionScreen.class)
public class AddXPLightItemAbilityScreenMixin extends Screen {
    @Unique private final BlockPos pos;
    @Unique public final ItemStack stack;
    @Mutable @Shadow @Final private Minecraft MC;
    @Unique private static final ResourceLocation ADD_POINT_FIRE = new ResourceLocation( ThirteenFlames.MODID, "textures/gui/add_point_fire.png");
    @Unique private TextureManager textureManager = this.MC.getTextureManager();
    @Unique private AddPointFireItem addPointFireItem;
    @Unique private RelicDescriptionScreen relicDescriptionScreen;
    @Unique public int backgroundHeight = 177;
    @Unique public int backgroundWidth = 256;

    public AddXPLightItemAbilityScreenMixin(BlockPos pos, ItemStack stack) {
        super(Component.empty());
        this.pos = pos;
        this.stack = stack;
    }


    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lit/hurts/sskirillss/relics/client/screen/description/AbilityDescriptionScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", ordinal = 0))
    protected void init(CallbackInfo ci) {
        Item item = this.stack.getItem();
        if (item instanceof RelicItem relic) {
            if (relic instanceof FireItemSetting) {
                this.addPointFireItem = new AddPointFireItem((this.width - this.backgroundWidth) / 2 + 253, (this.height - this.backgroundHeight) / 2 + 47, relicDescriptionScreen, stack);
                this.addRenderableWidget(this.addPointFireItem);
            }
        }
    }
}
