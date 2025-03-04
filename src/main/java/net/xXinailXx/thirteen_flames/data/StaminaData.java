package net.xXinailXx.thirteen_flames.data;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.AbilityUtils;
import net.xXinailXx.thirteen_flames.network.NetworkRegistry;
import net.xXinailXx.thirteen_flames.network.packet.capability.StaminaSyncPacket;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;

public class StaminaData implements IAutoNBTSerializable {
    @NBTSerializable
    private int maxStamina;
    @NBTSerializable
    private int stamina;
    @NBTSerializable
    private int regenCooldown;
    @NBTSerializable
    private int shakeTime;

    public int getMaxStamina() {
        return maxStamina;
    }

    public void setMaxStamina(int amount) {
        if (amount > 993) {
            this.maxStamina = 993;
        } else {
            this.maxStamina = Math.max(amount, 1);
        }
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int amount) {
        if (amount > this.maxStamina) {
            this.stamina = 0;
        } else {
            this.stamina = amount;
        }
    }

    public int getRegenCooldown() {
        return regenCooldown;
    }

    public void setRegenCooldown(int amount) {
        this.regenCooldown = amount;
    }

    public int getShakeTime() {
        return shakeTime;
    }

    public void setShakeTime(int amount) {
        this.shakeTime = amount;
    }

    public boolean isStaminaEmpty() {
        return this.stamina <= 0;
    }

    public boolean isStaminaFull() {
        return this.stamina == this.maxStamina;
    }

    public static class Utils implements IStaminaData {
        private final IData.IAbilitiesData abilitiesData = new Data.AbilitiesData.Utils();

        public static StaminaData getStaminaData(Player player) {
            StaminaData fake = new StaminaData();
            CompoundTag data = player.getPersistentData();

            if (data.contains("stamina_data")) {
                fake.deserializeNBT(data.getCompound("stamina_data"));
            }

            return fake;
        }

        public static void setStaminaData(Player player, StaminaData data) {
            CompoundTag nbt = data.serializeNBT();
            player.getPersistentData().put("stamina_data", nbt);

            if (!player.level.isClientSide()) {
                NetworkRegistry.sendToClient(new StaminaSyncPacket(nbt), (ServerPlayer)player);
            }
        }

        public int getStamina(Player player) {
            return getStaminaData(player).getStamina();
        }

        public void setStamina(Player player, int stamina) {
            StaminaData data = getStaminaData(player);
            data.setStamina(Mth.clamp(stamina, 0, getMaxStamina(player)));
            setStaminaData(player, data);
        }

        public int getMaxStamina(Player player) {
            return Math.max(10 * (abilitiesData.isActiveAbility("stamina_mantra") ? abilitiesData.getLevelAbility("stamina_mantra") : 1), getStaminaData(player).getMaxStamina());
        }

        public void setMaxStamina(Player player, int stamina) {
            StaminaData data = getStaminaData(player);
            data.setMaxStamina(stamina);
            setStaminaData(player, data);
        }

        public int getRegenCooldown(Player player) {
            return getStaminaData(player).getRegenCooldown();
        }

        public void setRegenCooldown(Player player, int cooldown) {
            StaminaData data = getStaminaData(player);
            data.setRegenCooldown(Math.max(0, cooldown));
            setStaminaData(player, data);
        }

        public int getShakeTime(Player player) {
            return getStaminaData(player).getShakeTime();
        }

        public boolean isStaminaEmpty(Player player) {
            return getStaminaData(player).isStaminaEmpty();
        }

        public boolean isStaminaFull(Player player) {
            return getStaminaData(player).isStaminaFull();
        }

        public void setShakeTime(Player player, int time) {
            StaminaData data = getStaminaData(player);
            data.setShakeTime(Math.max(0, time));
            setStaminaData(player, data);
        }

        public void addMaxStamina(Player player, int stamina) {
            setMaxStamina(player, getMaxStamina(player) + stamina);
        }

        public void addStamina(Player player, int stamina) {
            if (stamina < 0) {
                if (abilitiesData.isActiveAbility("second_wind") && player.getHealth() < (player.getMaxHealth() / 2)) {
                    return;
                }

                setRegenCooldown(player, 5);
            }

            setStamina(player, getStamina(player) + stamina);
        }

        public void addStaminaReqAbil(Player player, int stamina) {
            if (abilitiesData.isActiveAbility("stamina_mantra")) {
                addStamina(player, stamina * abilitiesData.getLevelAbility("stamina_mantra"));
            } else {
                addStamina(player, stamina);
            }
        }

        public void addRegenCooldown(Player player, int cooldown) {
            setRegenCooldown(player, getRegenCooldown(player) + cooldown);
        }

        public void addShakeTime(Player player, int time) {
            setShakeTime(player, getShakeTime(player) + time);
        }
    }

    @Mod.EventBusSubscriber
    public static class Actions {
        private static final IData.IAbilitiesData abilitiesData = new Data.AbilitiesData.Utils();
        private static final IStaminaData staminaData = new Utils();
        private static Vec3 lastPos;
        private static int posDiffs;

        @SubscribeEvent
        public static void attackEntity(LivingAttackEvent event) {
            if (event.getSource().getEntity() instanceof Player player) {
                if (!player.isCreative()) {
                    if (staminaData.getStamina(player) <= 0) {
                        staminaData.setRegenCooldown(player, 3);
                        staminaData.setShakeTime(player, 10);
                        event.setCanceled(true);
                    } else {
                        staminaData.addStamina(player, -1);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void breakBlock(BlockEvent.BreakEvent event) {
            Player player = event.getPlayer();

            if (!player.isCreative()) {
                if (staminaData.getStamina(player) <= 0) {
                    staminaData.setRegenCooldown(player, 3);
                    staminaData.setShakeTime(player, 10);
                    event.setCanceled(true);
                } else {
                    staminaData.addStamina(player, -1);
                }
            }
        }

        @SubscribeEvent
        public static void setBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();

            if (!player.isCreative()) {
                if (staminaData.getStamina(player) <= 0) {
                    staminaData.setRegenCooldown(player, 3);
                    staminaData.setShakeTime(player, 10);
                    event.setNewSpeed(0.0F);
                }
            }
        }

        @SubscribeEvent
        public static void playerJump(LivingEvent.LivingJumpEvent event) {
            if (event.getEntity() instanceof Player player)
                staminaData.setRegenCooldown(player, 3);
        }

        @SubscribeEvent
        public static void playerTick1(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
                Player player = event.player;
                int staminaLevel = abilitiesData.isActiveAbility("stamina_mantra") ? abilitiesData.getLevelAbility("stamina_mantra") : 1;

                staminaData.setMaxStamina(player, 10 * staminaLevel);

                int time = staminaData.getShakeTime(player);
                int cooldown = staminaData.getRegenCooldown(player);
                int stamina = staminaData.getStamina(player);
                int maxStamina = staminaData.getMaxStamina(player);

                if (cooldown > 0) {
                    if (player.tickCount % 20 == 0)
                        staminaData.addRegenCooldown(player, -1);
                } else if (stamina < maxStamina && player.tickCount % 5 == 0) {
                    int tickCount = player.tickCount - (AbilityUtils.playerLevelSea(player) != AbilityUtils.PlayerPosYState.NONE ? 3 : 0);

                    if (tickCount % 2 == 0)
                        staminaData.addStaminaReqAbil(player, 1);
                }

                if (stamina > maxStamina)
                    staminaData.setStamina(player, maxStamina);

                if (time > 0)
                    staminaData.addShakeTime(player, -1);

                if (!player.isCreative() && !player.isSpectator() && player.isOnGround()) {
                    Vec3 pos = player.position();

                    if (pos.distanceTo(lastPos) >= (double)5.0F) {
                        lastPos = pos;

                        if (posDiffs >= 1) {
                            posDiffs = 0;
                            staminaData.addStamina(player, -1);
                        } else if (player.isSprinting()) {
                            staminaData.setRegenCooldown(player, 3);
                            ++posDiffs;
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void playerTick2(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;

                int stamina = staminaData.getStamina(player);

                if (stamina <= 0 && player.isSprinting()) {
                    if (abilitiesData.isActiveAbility("overcoming") && player.getFoodData().getFoodLevel() > 0) {
                        if (player.tickCount % 20 == 0)
                            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);

                        player.getFoodData().setExhaustion(0);

                        staminaData.setRegenCooldown(player, 3);
                        return;
                    }

                    if (!player.level.isClientSide()) {
                        staminaData.setRegenCooldown(player, 3);
                        staminaData.setShakeTime(player, 10);
                    }

                    player.setSprinting(false);

                    if (player instanceof LocalPlayer) {
                        LocalPlayer localPlayer = (LocalPlayer) player;
                        localPlayer.input.forwardImpulse = 0.0F;
                    }
                }
            }
        }

        static {
            lastPos = Vec3.ZERO;
            posDiffs = 0;
        }
    }
}