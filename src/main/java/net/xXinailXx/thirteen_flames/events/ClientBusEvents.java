package net.xXinailXx.thirteen_flames.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.item.MaskSalmana;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.custom.render.*;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.entity.client.armor.MaskSalmanaRenderer;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ClientBusEvents {
    @Mod.EventBusSubscriber(modid = ThirteenFlames.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
            GeoArmorRenderer.registerArmorRenderer(MaskSalmana.class, new MaskSalmanaRenderer());
        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer( BlockEntityRegistry.STATUE_KNEF_BLOCK_ENTITY.get(), StatueKnefRenderer::new );
            event.registerBlockEntityRenderer( BlockEntityRegistry.STATUE_SELYA_BLOCK_ENTITY.get(), StatueSelyaRenderer::new );
            event.registerBlockEntityRenderer( BlockEntityRegistry.STATUE_MONTU_BLOCK_ENTITY.get(), StatueMontuRenderer::new );
            event.registerBlockEntityRenderer( BlockEntityRegistry.STATUE_RONOS_BLOCK_ENTITY.get(), StatueRonosRenderer::new );
            event.registerBlockEntityRenderer( BlockEntityRegistry.STATUE_HET_BLOCK_ENTITY.get(), StatueHetRenderer::new );
            event.registerBlockEntityRenderer( BlockEntityRegistry.GOD_FARAON_BLOCK_ENTITY.get(), GodFaraonRenderer::new );
            event.registerBlockEntityRenderer( BlockEntityRegistry.SUN_SELIASET_BLOCK_ENTITY.get(), SunSeliasetRenderer::new );
        }
    }
}
