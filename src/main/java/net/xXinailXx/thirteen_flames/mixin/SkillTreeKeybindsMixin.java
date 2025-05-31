package net.xXinailXx.thirteen_flames.mixin;

import daripher.skilltree.client.init.SkillTreeKeybinds;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkillTreeKeybinds.class)
public class SkillTreeKeybindsMixin {
    @Inject(method = "registerKeybinds", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void registerKeybinds(RegisterKeyMappingsEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
