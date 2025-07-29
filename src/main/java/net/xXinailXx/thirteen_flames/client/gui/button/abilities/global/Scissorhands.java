package net.xXinailXx.thirteen_flames.client.gui.button.abilities.global;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.ScreenID;

@Mod.EventBusSubscriber
public class Scissorhands extends AbstarctAbilityWidgets {
    public Scissorhands(int x, int y) {
        super(x, y, 8 + (effectData.isCurseKnef(Minecraft.getInstance().player) ? 1 : 0));
    }

    public AbilityData constructAbilityData() {
        return AbilityData.builder("scissorhands").screenID(ScreenID.GLOBAL).build();
    }

    private static boolean isShearsMinesBlock(BlockState state) {
        return state.is(BlockTags.LEAVES) || state.is(Blocks.COBWEB) || state.is(Blocks.GRASS) || state.is(Blocks.FERN) || state.is(Blocks.DEAD_BUSH) || state.is(Blocks.HANGING_ROOTS) || state.is(Blocks.VINE) || state.is(Blocks.TRIPWIRE) || state.is(BlockTags.WOOL);
    }

    @SubscribeEvent
    public static void interactEntity(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getTarget();

        if (entity instanceof net.minecraftforge.common.IForgeShearable target) {
            if (entity.level.isClientSide)
                return;

            BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
            Player player = event.getEntity();

            if (data.isActiveAbility(player, "scissorhands") && event.getItemStack().isEmpty()) {
                if (target.isShearable(event.getItemStack(), entity.level, pos)) {
                    java.util.List<ItemStack> drops = target.onSheared(player, event.getItemStack(), entity.level, pos, 0);
                    java.util.Random rand = new java.util.Random();

                    drops.forEach(d -> {
                        net.minecraft.world.entity.item.ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
                        ent.setDeltaMovement(ent.getDeltaMovement().add((rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F, (rand.nextFloat() - rand.nextFloat()) * 0.1F));
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void breakeSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        if (data.isActiveAbility(player, "scissorhands")) {
            if (player.getMainHandItem().is(Items.SHEARS))
                return;

            Level level = player.getLevel();
            BlockState state = level.getBlockState(event.getPos());

            if (! state.is(Blocks.COBWEB) && ! state.is(BlockTags.LEAVES)) {
                if (state.is(BlockTags.WOOL))
                    event.setNewSpeed(5.0F);
                else
                    event.setNewSpeed(!isShearsMinesBlock(state) ? event.getOriginalSpeed() : 2.0F);
            } else {
                event.setNewSpeed(15.0F);
            }
        }
    }

    @SubscribeEvent
    public static void breakeBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();

        if (player == null)
            return;

        if (data.isActiveAbility(player, "scissorhands")) {
            if (player.getMainHandItem().isEmpty()) {
                Level level = player.getLevel();
                BlockState state = level.getBlockState(event.getPos());
                ItemStack stack = null;

                if (isShearsMinesBlock(state))
                    stack = stack.getItem().getDefaultInstance();

                if (stack != null) {
                    ItemEntity itemEntity = new ItemEntity(level, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), stack);
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void clickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (player == null)
            return;

        if (data.isActiveAbility(player, "scissorhands")) {
            if (player.getMainHandItem().is(Items.SHEARS))
                return;

            Level level = player.getLevel();
            BlockState state = level.getBlockState(event.getPos());

            if (state.getBlock() instanceof GrowingPlantHeadBlock growingplantheadblock) {
                if (!growingplantheadblock.isMaxAge(state)) {
                    if (player instanceof ServerPlayer)
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, event.getPos(), player.getMainHandItem());

                    level.playSound(player, event.getPos(), SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.setBlockAndUpdate(event.getPos(), growingplantheadblock.getMaxAgeState(state));
                }
            }
        }
    }
}
