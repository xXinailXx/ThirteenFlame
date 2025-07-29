package net.xXinailXx.thirteen_flames.item;

import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.entity.StatueShcemeEntity;
import net.xXinailXx.thirteen_flames.item.base.ItemSetting;
import net.xXinailXx.thirteen_flames.network.packet.DiscardShcemeEntityPacket;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.net.Network;

import java.util.List;
import java.util.Optional;

public class MarkupItem extends ItemSetting {
    public static final String TAG_SHCEME = "shceme_entity";
    public static final String TAG_SHCEME_UUID = "shceme_entity_uuid";
    private final Gods god;

    public MarkupItem(Gods gods) {
        super(new Properties().tab(ThirteenFlames.ITEMS_TAB).stacksTo(1));
        this.god = gods;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        String name = ForgeRegistries.ITEMS.getKey(this).getPath();

        tooltip.add(Component.translatable("item." + ThirteenFlames.MODID + "." + name + ".tooltip"));

        if (!NBTUtils.getCompound(stack, TAG_SHCEME, new CompoundTag()).isEmpty() && !NBTUtils.getString(stack, TAG_SHCEME_UUID, "").isEmpty())
            tooltip.add(Component.translatable("item." + ThirteenFlames.MODID + ".markup.tooltip.info"));

        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("item." + ThirteenFlames.MODID + ".markup.tooltip.warning"));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown() && !NBTUtils.getCompound(stack, TAG_SHCEME, new CompoundTag()).isEmpty() && !NBTUtils.getString(stack, TAG_SHCEME_UUID, "").isEmpty())
            Network.sendToServer(new DiscardShcemeEntityPacket(player.getItemInHand(hand)));

        return super.use(level, player, hand);
    }

    public InteractionResult useOn(UseOnContext use) {
        Player player = use.getPlayer();

        if (player == null)
            return InteractionResult.FAIL;

        ItemStack stack = use.getItemInHand();
        Level level = player.getLevel();
        BlockPos pos = new BlockPos(Vec3.atCenterOf(use.getClickedPos())).above();
        BlockState state = level.getBlockState(pos);
        CompoundTag data = stack.getTag();

        if (data == null || data.isEmpty()) {
            spawnShceme(player, level, pos, stack);
            return InteractionResult.SUCCESS;
        }

        Optional<EntityType<?>> type = EntityType.by(data.getCompound(TAG_SHCEME));

        if (type.isEmpty())
            return InteractionResult.FAIL;

        Entity entity = type.get().create(level);

        if (!(entity instanceof StatueShcemeEntity))
            return InteractionResult.FAIL;

        Network.sendToServer(new DiscardShcemeEntityPacket(stack));

        spawnShceme(player, level, pos, stack);

        return InteractionResult.SUCCESS;
    }

    private void spawnShceme(Player player, Level level, BlockPos pos, ItemStack stack) {
        StatueShcemeEntity entity = new StatueShcemeEntity(level, this.god);
        entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        entity.setOwner(player);

        Direction direction;

        switch (player.getDirection()) {
            case NORTH -> direction = Direction.SOUTH;
            case SOUTH -> direction = Direction.NORTH;
            case WEST -> direction = Direction.EAST;
            case EAST -> direction = Direction.WEST;
            default -> direction = Direction.NORTH;
        }

        entity.setYRot(direction.toYRot());
        level.addFreshEntity(entity);

        CompoundTag data = new CompoundTag();
        entity.save(data);

        NBTUtils.setCompound(stack, TAG_SHCEME, data);
        NBTUtils.setString(stack, TAG_SHCEME_UUID, entity.getUUID().toString());
    }
}