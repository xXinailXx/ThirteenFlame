package net.xXinailXx.thirteen_flames.client.gui.button.abilities.fight;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityData;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbstarctAbilityWidgets;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class Abundance extends AbstarctAbilityWidgets {
    public Abundance(int x, int y) {
        super(x, y, 7);
    }

    @Override
    public AbilityData constructAbilityData() {
        return AbilityData.builder("abundance").screenID(ScreenID.FIGHT).maxLevel(5).requiredLevel(35).requiredScarabsForOpen(2).requiredScarabsForUpgrade(2).build();
    }

    private static void extraDrop(Level level, Entity entity, Vec3 pos, int value) {
        if (level.isClientSide) {
            return;
        }

        List<ItemEntity> dropEntities = new ArrayList<ItemEntity>();

        for (Entity entity1 : level.getEntities(null, new AABB(entity.getX()-1, entity.getY()-1, entity.getZ()-1, entity.getX()+1, entity.getY()+1, entity.getZ()+1))) {
            if (entity1 instanceof ItemEntity) {
                dropEntities.add((ItemEntity)entity1);
            }
        }

        RandomSource random = level.getRandom();

        for (int i = 0; i < value; i++) {
            ItemEntity itemEntity = new ItemEntity(level, pos.x, pos.y, pos.z, dropEntities.get(random.nextInt(0, dropEntities.size())).getItem());
            itemEntity.setPos(pos);
            level.addFreshEntity(itemEntity);
        }
    }

    @SubscribeEvent
    public static void dropNugget(AttackEntityEvent event) {
        if (data.isActiveAbility("abundance")) {
            if (AbilityUtils.isRandomSuccess(event.getEntity().level, data.getLevelAbility("abundance") * 5)) {
                Level level = event.getEntity().level;

                ItemEntity itemEntity = new ItemEntity(level, event.getTarget().getX(), event.getTarget().getY(), event.getTarget().getZ(), Items.GOLD_NUGGET.getDefaultInstance());
                itemEntity.setPos(event.getTarget().getX(), event.getTarget().getY(), event.getTarget().getZ());
                level.addFreshEntity(itemEntity);
            }
        }
    }

    @SubscribeEvent
    public static void addExtraDrop(LivingDeathEvent event) {
        if (data.isActiveAbility("abundance")) {
            if (event.getSource().getEntity() instanceof Player) {
                if (AbilityUtils.isRandomSuccess(event.getEntity().level, data.getLevelAbility("abundance") * 5)) {
                    Level level = event.getEntity().level;
                    RandomSource random = level.getRandom();

                    extraDrop(level, event.getEntity(), new Vec3(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ()), random.nextInt(1, 3));
                }
            }
        }
    }
}
