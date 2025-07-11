package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.xXinailXx.thirteen_flames.client.progress.ProgressManager;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Item.class)
public class ItemMixin {
    @Mutable @Shadow @Final @Nullable private FoodProperties foodProperties;
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    @Inject(method = "appendHoverText", at = @At(value = "TAIL"))
    public void appendHoverText(ItemStack stack, Level level, List<Component> components, TooltipFlag tooltipFlag, CallbackInfo ci) {
        List<Component> tooltip = ProgressManager.getProgressTooltip(stack);

        if (!tooltip.isEmpty()) {
            for (Component component : tooltip)
                components.add(component);
        }
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b, CallbackInfo ci) {
        if (entity instanceof Player player && stack.getFoodProperties((LivingEntity) entity) == null && data.isActiveAbility(player, "sword_swallower")) {
            Item item = (Item) (Object) this;
            int nuration = 0;
            float saturation = 0;

            if (item instanceof ArmorItem || item instanceof BlockItem || item instanceof BowItem || item instanceof ElytraItem || item instanceof ArrowItem) {
                nuration = 4;
                saturation = 0.2F;
            } else if (item instanceof TieredItem || item instanceof SpawnEggItem || item instanceof ArmorItem || item instanceof TridentItem) {
                nuration = 5;
                saturation = 0.4F;
            } else if (!(item instanceof PotionItem)) {
                nuration = 3;
                saturation = 0.1F;
            } else {
                return;
            }

            this.foodProperties = new FoodProperties.Builder().nutrition(nuration).saturationMod(saturation).build();
        }
    }

    @Inject(method = "isEdible", at = @At("HEAD"), cancellable = true)
    public void isEdible(CallbackInfoReturnable<Boolean> cir) {
        if (!ProgressManager.isAllowUsage(((Item) (Object) this).getDefaultInstance())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "getUseAnimation", at = @At(value = "HEAD"), cancellable = true)
    public void getUseAnimation(ItemStack stack, CallbackInfoReturnable<UseAnim> cir) {
        if (data.isActiveAbility(Minecraft.getInstance().player, "sword_swallower"))
            if (stack.getItem() instanceof PotionItem)
               cir.setReturnValue(UseAnim.NONE);
            else
                cir.setReturnValue(UseAnim.EAT);
        else
            cir.setReturnValue(stack.getItem().isEdible() ? UseAnim.EAT : UseAnim.NONE);

        cir.cancel();
    }
}
