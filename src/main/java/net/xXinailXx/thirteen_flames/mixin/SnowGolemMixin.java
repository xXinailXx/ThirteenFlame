package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.data.IData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SnowGolem.class)
public class SnowGolemMixin extends SnowGolem {
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    public SnowGolemMixin(EntityType<? extends SnowGolem> p_29902_, Level p_29903_) {
        super(p_29902_, p_29903_);
    }

    @Overwrite
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (false && itemstack.getItem() == Items.SHEARS && this.readyForShearing()) { //Forge: Moved to onSheared
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);

            if (!data.isActiveAbility("scissorhands")) {
                if (!this.level.isClientSide) {
                    itemstack.hurtAndBreak(1, player, (p_29910_) -> {
                        p_29910_.broadcastBreakEvent(hand);
                    });
                }
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}
