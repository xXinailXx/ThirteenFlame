package net.xXinailXx.thirteen_flames.block.entity;

import lombok.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xXinailXx.thirteen_flames.init.BlockEntitiesRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SunSeliasetBlockEntity extends BlockEntity implements IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    @Builder.Default
    private static boolean active = true;
    @Builder.Default
    private static boolean on = true;
    @Builder.Default
    private static boolean idle = true;
    @Builder.Default
    private static boolean opportunityPickUp = true;

    public SunSeliasetBlockEntity(BlockPos pos, BlockState state) {
        super( BlockEntitiesRegistry.STATUE_SELYA.get(), pos, state );
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;

//        if (!isIdle()) {
//            if (isOn()) {
//                if (!isActive()) {
//                    event.getController().setAnimation( new AnimationBuilder().addAnimation( "open_sun", false ) );
//                    if (event.getController().getAnimationState().equals( AnimationState.Stopped )) {
//                        setActive(true);
//                    }
//                    return PlayState.CONTINUE;
//                } else {
//                    event.getController().setAnimation( new AnimationBuilder().addAnimation( "animation_sun", true ) );
//                    return PlayState.CONTINUE;
//                }
//            } else {
//                event.getController().setAnimation( new AnimationBuilder().addAnimation( "close_sun", false ) );
//                if (event.getController().getAnimationState().equals( AnimationState.Stopped )) {
//                    setIdle(true);
//                    setOpportunityPickUp(true);
//                }
//                return PlayState.CONTINUE;
//            }
//        } else {
//            event.getController().setAnimation( new AnimationBuilder().addAnimation( "animation_sun", true ) );
//            return PlayState.CONTINUE;
////            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
////            return PlayState.CONTINUE;
//        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<SunSeliasetBlockEntity>
                (this, "controller", 0, this::predicate));
    }
//
//    public static void startAnimation() {
//        if (isIdle()) {
//            setIdle(false);
//            setOn(true);
//            setActive(false);
//        } else if (!isIdle()) {
//            setOn(false);
//        }
//    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

//    private static boolean isIdle() {
//        return idle;
//    }
//
//    private static void setIdle(boolean idle) {
//        SunSeliasetBlockEntity.idle = idle;
//    }
//
//    private static boolean isOn() {
//        return on;
//    }
//
//    private static void setOn(boolean on) {
//        SunSeliasetBlockEntity.on = on;
//    }
//
//    private static boolean isActive() {
//        return active;
//    }
//
//    public static boolean isOpportunityPickUp() {
//        return opportunityPickUp;
//    }
//
//    private static void setOpportunityPickUp(boolean opportunityPickUp) {
//        SunSeliasetBlockEntity.opportunityPickUp = opportunityPickUp;
//    }
//
//    private static void setActive(boolean active) {
//        SunSeliasetBlockEntity.active = active;
//    }
}
