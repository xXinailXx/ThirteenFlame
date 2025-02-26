package net.xXinailXx.thirteen_flames.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.xXinailXx.thirteen_flames.item.utils.render.StatueKnefBlockItemRender;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Consumer;

public class StatueKnefBlockItem extends BlockItem implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);

    public StatueKnefBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient( consumer );
        consumer.accept( new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new StatueKnefBlockItemRender();

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        } );
    }

    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    public AnimationFactory getFactory() {
        return this.factory;
    }
}
