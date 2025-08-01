package net.xXinailXx.thirteen_flames.events;

import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.xXinailXx.enderdragonlib.client.renderer.NullRenderer;
import net.xXinailXx.thirteen_flames.client.renderer.entity.HornSeliasetEntityRenderer;
import net.xXinailXx.thirteen_flames.client.renderer.entity.LivingFleshEntityRenderer;
import net.xXinailXx.thirteen_flames.client.renderer.entity.OreBlockSimulationRenderer;
import net.xXinailXx.thirteen_flames.client.renderer.entity.SunSeliasetEntityRenderer;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;
import net.xXinailXx.thirteen_flames.init.ItemRegistry;
import net.xXinailXx.thirteen_flames.item.MaskSalmana;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.custom.render.*;
import net.xXinailXx.thirteen_flames.init.BlockEntityRegistry;
import net.xXinailXx.thirteen_flames.client.renderer.armor.MaskSalmanaRenderer;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = ThirteenFlames.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void fmlclientsetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ItemRegistry.MOON_BOW.get(), new ResourceLocation(ThirteenFlames.MODID, "pull"), (stack, world, living, a) -> {
                if (living != null && living.isUsingItem())
                    return living.getUseItem() != stack ? 0F : (float) (stack.getUseDuration() - living.getUseItemRemainingTicks()) / 20F;
                else
                    return 0F;
            });
            ItemProperties.register(ItemRegistry.BLACK_ROSE.get(), new ResourceLocation(ThirteenFlames.MODID, "fire_type"),
                    (stack, world, living, a) -> Math.min(8, NBTUtils.getInt(stack, "fire_type", 0)));
            ItemProperties.register(ItemRegistry.SCROLL_HET.get(), new ResourceLocation(ThirteenFlames.MODID, "type"),
                    (stack, world, living, a) -> Math.min(7, NBTUtils.getInt(stack, "type", 0)));
            ItemProperties.register(ItemRegistry.SHIELD_RONOSA.get(), new ResourceLocation(ThirteenFlames.MODID, "use"), (stack, world, living, a) -> {
                if (living != null && living.isUsingItem())
                    return living.getUseItem() != stack ? 0 : 1;
                else
                    return 0;
            });
        });

        EntityRenderers.register(EntityRegistry.LIVING_FLESH.get(), LivingFleshEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.SUN_SELIASET.get(), SunSeliasetEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.HORN_SELIASET.get(), HornSeliasetEntityRenderer::new);
    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.ORE_SIMULATION.get(), OreBlockSimulationRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SHOCKWAVE.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.POISON_CLOUD.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MOON_DISCHARGE.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MOON_CARRIES.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MOON_PROJECTILE.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MOON_PROJECTILE_SPECIAL.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MOON_RAINDROP.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MOON_STORMCALLER.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MOON_STORM.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SHCEME.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.HORN_WIND_SELIASET.get(), NullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SOUL.get(), NullRenderer::new);

        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_KNEF.get(), StatueKnefRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_SELYA.get(), StatueSelyaRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_MONTU.get(), StatueMontuRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_RONOS.get(), StatueRonosRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_HET.get(), StatueHetRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_GOD_PHARAOH.get(), StatueGodPharaohRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_KNEF_UNFINISHED.get(), StatueKnefUnfinishedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_SELYA_UNFINISHED.get(), StatueSelyaUnfinishedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_MONTU_UNFINISHED.get(), StatueMontuUnfinishedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_RONOS_UNFINISHED.get(), StatueRonosUnfinishedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_HET_UNFINISHED.get(), StatueHetUnfinishedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.STATUE_GOD_PHARAOH_UNFINISHED.get(), StatueGodPharaohUnfinishedRenderer::new);
    }

    @SubscribeEvent
    public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(MaskSalmana.class, new MaskSalmanaRenderer());
    }
}
