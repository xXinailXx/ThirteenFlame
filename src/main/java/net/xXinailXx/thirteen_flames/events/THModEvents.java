package net.xXinailXx.thirteen_flames.events;

import it.hurts.sskirillss.relics.utils.DurabilityUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.ServerCapManager;
import net.xXinailXx.enderdragonlib.utils.statues.CustomStatueUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.StatueHandler;
import net.xXinailXx.thirteen_flames.client.progress.ProgressHelper;
import net.xXinailXx.thirteen_flames.client.progress.ProgressManager;
import net.xXinailXx.thirteen_flames.data.IData;
import net.xXinailXx.thirteen_flames.client.gui.god_pharaoh.GodPharaohScreenMining;
import net.xXinailXx.thirteen_flames.data.Data;
import net.xXinailXx.thirteen_flames.entity.LivingFleshEntity;
import net.xXinailXx.thirteen_flames.entity.StatueShcemeEntity;
import net.xXinailXx.thirteen_flames.init.*;

import java.util.UUID;

public class THModEvents {
    @Mod.EventBusSubscriber
    public static class ModEvents {
        @SubscribeEvent
        public static void joinWorld(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() == null)
                return;

            ProgressHelper.read();
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void keyPressed(InputEvent.Key event) {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player == null || minecraft.screen != null)
                return;

            if (event.getKey() == new KeyMapping("key.display_skill_tree", 79, "key.categories.skilltree").getKey().getValue())
                event.setCanceled(true);
        }

        @SubscribeEvent
        public static void attackEntity(AttackEntityEvent event) {
            Player player = event.getEntity();

            if (player == null)
                return;

            ItemStack stack = player.getMainHandItem();

            if (!ProgressManager.isAllowStatUsage(ProgressManager.ProgressType.FIGHT, stack))
                event.setCanceled(true);
        }

        @SubscribeEvent
        public static void getDigSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();

            if (player == null)
                return;

            ItemStack stack = player.getMainHandItem();

            if (!ProgressManager.isAllowStatUsage(ProgressManager.ProgressType.MINING, stack))
                event.setNewSpeed(0F);
        }

        @SubscribeEvent
        public static void openFaraonScreen(InputEvent event) {
            if (Minecraft.getInstance().player == null)
                return;

            if (KeyBindingRegistry.OPEN_GUI.isDown()) {
                IData.IGuiLevelingData guiLevelingData = new Data.GuiLevelingData.Utils();
                guiLevelingData.setPlayerScreen(Minecraft.getInstance().player, true);
                Minecraft.getInstance().setScreen(new GodPharaohScreenMining());
            }
        }

        @SubscribeEvent
        public static void createStatue(PlayerInteractEvent.RightClickBlock event) {
            Player player = event.getEntity();

            if (player == null)
                return;

            ItemStack mainHandStack = player.getMainHandItem();
            ItemStack offHandStack = player.getOffhandItem();
            Level level = event.getLevel();

            if ((mainHandStack.is(ItemRegistry.HAMMER_CARVER.get()) && offHandStack.is(ItemRegistry.CHISEL_CARVER.get())) || (mainHandStack.is(ItemRegistry.CHISEL_CARVER.get()) && offHandStack.is(ItemRegistry.HAMMER_CARVER.get()))) {
                if (level.isClientSide)
                    return;

                BlockPos interPos = event.getPos();

                if (!level.getBlockState(interPos).is(Blocks.SANDSTONE))
                    return;

                UUID uuid = null;
                Data.StatueBuilderData.ShcemeBuilder builder = null;

                for (String key : ServerCapManager.getOrCreateData("tf_statue_shceme_builder_data").getAllKeys()) {
                    Data.StatueBuilderData.ShcemeBuilder builder1 = Data.StatueBuilderData.getShceme(UUID.fromString(key));

                    if (builder1 == null)
                        continue;

                    if (builder1.mainPos().equals(interPos) || builder1.posList().contains(interPos)) {
                        uuid = UUID.fromString(key);
                        builder = builder1;

                        break;
                    }
                }

                if (builder == null) {
                    level.destroyBlock(interPos, false);
                    level.setBlock(interPos, BlockRegistry.STATUE_CUP_UNFINISHED.get().defaultBlockState(), 11);

                    if (!player.isCreative()) {
                        DurabilityUtils.hurt(player.getMainHandItem(), 5);
                        DurabilityUtils.hurt(player.getOffhandItem(), 5);
                    }

                    return;
                }

                ServerLevel serverLevel = (ServerLevel) level;
                Entity entity = serverLevel.getEntity(uuid);

                if (!(entity instanceof StatueShcemeEntity shceme)) {
                    return;
                }

                CompoundTag data = new CompoundTag();
                entity.save(data);

                if (!data.contains("progress"))
                    data.putFloat("progress", 0);

                if (data.getFloat("progress") + 1 >= 5) {
                    for (BlockPos pos : builder.posList())
                        level.destroyBlock(pos, false);

                    StatueHandler statue = null;

                    switch (shceme.getGod()) {
                        case KNEF -> statue = (StatueHandler) BlockRegistry.STATUE_KNEF_UNFINISHED.get();
                        case SELYA -> statue = (StatueHandler) BlockRegistry.STATUE_SELYA_UNFINISHED.get();
                        case MONTU -> statue = (StatueHandler) BlockRegistry.STATUE_MONTU_UNFINISHED.get();
                        case RONOS -> statue = (StatueHandler) BlockRegistry.STATUE_RONOS_UNFINISHED.get();
                        case HET -> statue = (StatueHandler) BlockRegistry.STATUE_HET_UNFINISHED.get();
                        case GOD_PHARAOH -> statue = (StatueHandler) BlockRegistry.STATUE_GOD_PHARAOH_UNFINISHED.get();
                    }

                    Direction direction = switch (entity.getDirection()) {
                        case SOUTH -> Direction.SOUTH;
                        case WEST -> Direction.WEST;
                        case EAST -> Direction.EAST;
                        default -> Direction.NORTH;
                    };

                    level.setBlock(builder.mainPos(), statue.defaultBlockState().setValue(CustomStatueUtils.FACING, direction), 11);

                    for (BlockPos pos : statue.getBlockPoses(builder.mainPos(), false))
                        level.setBlock(pos, statue.getStructureBlock().defaultBlockState(), 11);

                    entity.remove(Entity.RemovalReason.KILLED);

                    Data.StatueBuilderData.removeShceme(uuid);
                } else {
                    data.putFloat("progress", data.getFloat("progress") + 1);

                    entity.load(data);
                }

                if (!player.isCreative()) {
                    DurabilityUtils.hurt(player.getMainHandItem(), 5);
                    DurabilityUtils.hurt(player.getOffhandItem(), 5);
                }

                player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = ThirteenFlames.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonEvents {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(EntityRegistry.LIVING_FLESH.get(), LivingFleshEntity.setAttrebutes());
        }
    }
}
