package net.xXinailXx.thirteen_flames.mixin;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
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

@Mixin(Sheep.class)
public class SheepMixin extends Sheep {
    @Unique private IData.IAbilitiesData data = new Data.AbilitiesData.Utils();

    public SheepMixin(EntityType<? extends Sheep> p_29806_, Level p_29807_) {
        super(p_29806_, p_29807_);
    }

    @Overwrite
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (false && itemstack.getItem() == Items.SHEARS) { //Forge: Moved to onSheared
            if (!this.level.isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, player);

                if (!data.isActiveAbility("scissorhands")) {
                    if (!this.level.isClientSide) {
                        itemstack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(hand));
                    }
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }
}
