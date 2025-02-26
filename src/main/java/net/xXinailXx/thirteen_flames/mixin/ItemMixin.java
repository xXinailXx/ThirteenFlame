package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.data.Data;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Item.class)
public class ItemMixin {
    @Mutable @Shadow @Final @Nullable private FoodProperties foodProperties;
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

//    @Inject(method = "<init>", at = @At(value = "TAIL"))
//    public void Item(Item.Properties properties, CallbackInfo ci) {
//        if (this.foodProperties == null && data.isActiveAbility("sword_swallower")) {
//            Item item = (Item) (Object) this;
//            int nuration = 0;
//            float saturation = 0;
//
//            if (item instanceof ArmorItem || item instanceof BlockItem || item instanceof BowItem || item instanceof ElytraItem || item instanceof ArrowItem) {
//                nuration = 4;
//                saturation = 0.2F;
//            } else if (item instanceof TieredItem || item instanceof SpawnEggItem || item instanceof ArmorItem || item instanceof TridentItem) {
//                nuration = 5;
//                saturation = 0.4F;
//            } else if (!(item instanceof PotionItem)) {
//                nuration = 3;
//                saturation = 0.1F;
//            }
//
//            this.foodProperties = new FoodProperties.Builder().nutrition(nuration).saturationMod(saturation).build();
//        }
//    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b, CallbackInfo ci) {
        if (stack.getFoodProperties((LivingEntity) entity) == null && data.isActiveAbility("sword_swallower")) {
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

    @Overwrite
    public UseAnim getUseAnimation(ItemStack stack) {
        if (data.isActiveAbility("sword_swallower")) {
            if (stack.getItem() instanceof PotionItem) {
                return UseAnim.NONE;
            } else {
                return UseAnim.EAT;
            }
        } else {
            return stack.getItem().isEdible() ? UseAnim.EAT : UseAnim.NONE;
        }
    }
}
