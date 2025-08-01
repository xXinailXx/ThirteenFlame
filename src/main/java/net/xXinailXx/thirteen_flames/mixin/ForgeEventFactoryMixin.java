package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import net.xXinailXx.thirteen_flames.client.progress.ProgressManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeEventFactory.class)
public class ForgeEventFactoryMixin {
    @Inject(method = "getBreakSpeed", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void getBreakSpeed(Player player, BlockState state, float original, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (player == null)
            return;

        ItemStack stack = player.getMainHandItem();

        if (!ProgressManager.isAllowStatUsage(ProgressManager.ProgressType.MINING, stack)) {
            cir.setReturnValue(0F);
            cir.cancel();
        }
    }
}
