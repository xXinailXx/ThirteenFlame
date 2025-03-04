package net.xXinailXx.thirteen_flames.block;

import it.hurts.sskirillss.relics.items.relics.base.utils.LevelingUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.utils.statues.StatueSize3x3Util;
import net.xXinailXx.thirteen_flames.init.BlockRegistry;
import net.xXinailXx.thirteen_flames.init.ItemsRegistry;
import net.xXinailXx.thirteen_flames.utils.FlameItemSetting;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class StatueHendler extends StatueSize3x3Util {
    private int updateParticle = 100;
//    private long nextUpdateFire = 360000;
    private long nextUpdateFire = 100;


//    @Override
//    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
//        if (source.nextInt(0, 3) == source.nextInt(0, 3)) {
//            int x = source.nextInt(0, 1);
//            int z = source.nextInt(0, 1);
//            int deviationX = source.nextInt(0, 10);
//            int deviationZ = source.nextInt(0, 10);
//            int blockCount = source.nextInt(0, 7);
//
//            ClientLevel clientLevel = (ClientLevel) level;
//
//            switch (blockCount) {
//                case 0 -> {
//                    ColoredParticle particle = new ColoredParticle(clientLevel, pos.getX() + x - deviationX, pos.getY(), pos.getZ() + z - deviationZ,
//                            0, 0.1, 0, ColoredParticle.Constructor.builder().color(0, 255, 0)
//                                    .lifetime(50).diameter(1).roll(1).reduction(true).physical(false).build());
//
//                    level.addParticle();
//                }
//            }
//        }
//    }

//    @Override
//    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
//        if (this.updateParticle == 0) {
//            spawnParticle(level, pos, random);
//            this.updateParticle = 10;
//        } else {
//            this.updateParticle--;
//        }
//    }
//
//    private static void createSaw(Level level, Vec3 center, double limitX, double limitY, double limitZ, boolean normalRot) {
//        double len = (double)((float)((Math.PI * 2D) * 1.4));
//        int num = (int)(len / (double)0.2);
//
//        RandomSource random = level.getRandom();
//
//        for (int i = 0; i < num; i++) {
//            double angle = Math.toRadians( (360.0F / num * i) + 360.0F * ((1.4 / 0.2 - num) / num / 1.4) );
//
//            double extraX = 1.4 * Math.sin( angle ) + center.x();
//            double extraZ = 1.4 * Math.cos( angle ) + center.z();
//            double extraY = center.y() + 0.5F;
//
//            double rotX = 0.01 * (Mth.sin( (float) angle ));
//            double rotZ = 0.01 * (Mth.cos( (float) angle ));
//
//            level.addParticle( new CircleTintData( new Color( 200, 150 + random.nextInt( 50 ), random.nextInt( 50 ) ), 0.05F, 100, 1.0F, true ), extraX, extraY, extraZ, extraX * 0.01, 0.01, extraZ * 0.01);
//        }
//    }

    public void use(Level level, Player player, BlockPos pos, BlockState state, ItemStack stack) {
        if (this.nextUpdateFire == 0) {
            if (stack == null) {
                return;
            }

            if (stack.getItem() instanceof FlameItemSetting fireItemSetting) {
                if (stack.is( ItemsRegistry.MOLOT_MONTU.get() ) || stack.is( ItemsRegistry.GLOVES_MONTU.get() )) {
                    if (state.is( BlockRegistry.STATUE_MONTU_BLOCK.get() ) || state.is( BlockRegistry.STATUE_MONTU_STRUCTURE_BLOCK.get() )) {
                        LevelingUtils.addLevel( stack, 1 );
                        reloadUpdataTick();
                    }
                } else if (stack.is( ItemsRegistry.BLACK_ROSE.get() ) || stack.is( ItemsRegistry.MOONBOW.get() )) {
                    if (state.is( BlockRegistry.STATUE_KNEF_BLOCK.get() ) || state.is( BlockRegistry.STATUE_KNEF_STRUCTURE_BLOCK.get() )) {
                        LevelingUtils.addLevel( stack, 1 );
                        reloadUpdataTick();
                    }
                } else if (stack.is( ItemsRegistry.GORN_SELEASET.get() ) || stack.is( ItemsRegistry.SUN_SELEASET.get() )) {
                    if (state.is( BlockRegistry.STATUE_SELYA_BLOCK.get() ) || state.is( BlockRegistry.STATUE_SELYA_STRUCTURE_BLOCK.get() )) {
                        LevelingUtils.addLevel( stack, 1 );
                        reloadUpdataTick();
                    }
                } else if (stack.is( ItemsRegistry.SHIELD_RONOSA.get() ) || stack.is( ItemsRegistry.SWORD_RONOSA.get() )) {
                    if (state.is( BlockRegistry.STATUE_RONOS_BLOCK.get() ) || state.is( BlockRegistry.STATUE_RONOS_STRUCTURE_BLOCK.get() )) {
                        LevelingUtils.addLevel( stack, 1 );
                        reloadUpdataTick();
                    }
                } else if (stack.is( ItemsRegistry.SCROLL_HET.get() ) || stack.is( ItemsRegistry.FLIGHT_HET.get() )) {
                    if (state.is( BlockRegistry.STATUE_HET_BLOCK.get() ) || state.is( BlockRegistry.STATUE_HET_STRUCTURE_BLOCK.get() )) {
                        LevelingUtils.addLevel( stack, 1 );
                        reloadUpdataTick();
                    }
                }
            }
        }
    }

    public void reloadUpdataTick() {
//        this.nextUpdateFire = 360000;
        this.nextUpdateFire = 100;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }

    @Mod.EventBusSubscriber
    public static class Events {
        @SubscribeEvent
        public static void useBlock(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();

//            RandomSource random = level.getRandom();
//
//            double len = (double)((float)((Math.PI * 2D) * 1.4));
//            int num = (int)(len / (double)0.2);
//
//            Vec3 vec = new Vec3(pos.getX(), pos.getY(), pos.getZ());
//
//            for (int i = 0; i < 360; i++) {
//                if (i % 5 == 0) {
//                    double angle = Math.toRadians((360.0F / num * i) + 360.0F * ((1.4 / 0.2 - num) / num / 1.4));
//
//                    double extraX = Math.sin(angle) + vec.x();
//                    double extraZ = Math.cos(angle) + vec.z();
//                    double extraY = vec.y() + 0.5F;
//
//                    double rotX = 0;
//                    double rotZ = 0;
//
//                    level.addParticle(new ColoredParticle.Options(ColoredParticle.Constructor.builder().color(0, 255, 0)
//                            .lifetime(50).diameter(1).roll(1).reduction(true).physical(false).build()), extraX, extraY, extraZ, rotX, 0.01, rotZ);
//                }
//            }

            if (level.getBlockState(pos).getBlock() instanceof StatueHendler hendler) {
                hendler.use(level, event.getEntity(), pos, level.getBlockState(pos), event.getItemStack());
            }
        }
    }
}
