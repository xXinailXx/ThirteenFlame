package net.xXinailXx.thirteen_flames.mixin.client;

import it.hurts.sskirillss.relics.client.screen.description.RelicDescriptionScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.xXinailXx.thirteen_flames.client.gui.button.relics.AddPointFireItem;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RelicDescriptionScreen.class)
public class AddXpButtonRelicScreen extends Screen {
    @Shadow @Final public ItemStack stack;
    @Shadow public int backgroundWidth;
    @Shadow public int backgroundHeight;
    @Shadow @Final public BlockPos pos;

    protected AddXpButtonRelicScreen(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        Item item = this.stack.getItem();

        if (item instanceof FlameItemSetting)
            this.addRenderableWidget(new AddPointFireItem((this.width - this.backgroundWidth) / 2 + 253, (this.height - this.backgroundHeight) / 2 + 47, this.stack, this.pos));
    }
}
