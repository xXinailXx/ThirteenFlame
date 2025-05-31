package net.xXinailXx.thirteen_flames.utils;

import com.google.common.base.Suppliers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.StatueCup;
import net.xXinailXx.thirteen_flames.block.StatueHandler;
import net.xXinailXx.thirteen_flames.block.StatueStructureBlock;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class StatueBlockItem extends BlockItem {
    private final Block block;

    public StatueBlockItem(Block block, Properties properties) {
        super(block, block instanceof CustomStatueUtils || block instanceof StatueStructureBlock || block instanceof StatueCup ? properties.rarity(Rarity.RARE).tab(ThirteenFlames.STATUES_TAB) : properties.tab(ThirteenFlames.ITEMS_TAB));
        this.block = block;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);

        if (this.block instanceof StatueHandler) {
            StatueHandler handler = (StatueHandler) this.block;

            final Supplier<GeoItemRenderer> renderer = Suppliers.memoize((com.google.common.base.Supplier<GeoItemRenderer>) handler.getRenderer());

            consumer.accept(new IClientItemExtensions() {
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return renderer.get();
                }
            });
        }
    }
}
