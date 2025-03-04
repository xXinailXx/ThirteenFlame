package net.xXinailXx.thirteen_flames.init;

import it.hurts.sskirillss.relics.client.renderer.entities.NullRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.renderer.entity.OreBlockSimulationRenderer;

@Mod.EventBusSubscriber(modid = ThirteenFlames.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RemoteRegistry {
    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.SHOCKWAVE.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.POISON_CLOUD.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ORE_SIMULATION.get(), OreBlockSimulationRenderer::new);
    }
}
