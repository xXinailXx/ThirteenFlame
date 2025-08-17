package net.xXinailXx.thirteen_flames.data;

import daripher.skilltree.capability.skill.IPlayerSkills;
import daripher.skilltree.capability.skill.PlayerSkillsProvider;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.PlayerCapManager;
import net.xXinailXx.enderdragonlib.capability.ServerCapManager;
import net.xXinailXx.enderdragonlib.capability.managers.UUIDManager;
import net.xXinailXx.enderdragonlib.client.message.MessageManager;
import net.xXinailXx.enderdragonlib.client.utils.MessageUtil;
import net.xXinailXx.enderdragonlib.utils.MathUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.init.EffectRegistry;
import net.xXinailXx.thirteen_flames.network.packet.SetSkillPointPacket;
import net.xXinailXx.thirteen_flames.network.packet.capability.*;
import net.xXinailXx.thirteen_flames.utils.Gods;
import net.xXinailXx.thirteen_flames.utils.ScarabsType;
import org.zeith.hammerlib.api.io.IAutoNBTSerializable;
import org.zeith.hammerlib.api.io.NBTSerializable;
import org.zeith.hammerlib.net.Network;

import javax.annotation.Nullable;
import java.util.*;

public class Data implements IData {
    public static class StatueBuilderData {
        @Nullable
        public static ShcemeBuilder getShceme(UUID uuid) {
            CompoundTag tag = ServerCapManager.getOrCreateData("tf_statue_shceme_builder_data");

            if (!tag.contains(uuid.toString()))
                return null;

            CompoundTag data = tag.getCompound(uuid.toString());
            Gods[] gods = Gods.values();
            Gods gods1 = gods[data.getInt("god")];
            BlockPos mainPos = BlockPos.of(data.getLong("main_pos"));
            ListTag listTag = data.getList("complete_poses", StringTag.valueOf("").getId());
            List<BlockPos> posList = new ArrayList<>();

            for (int i = 0; i < listTag.size(); i++)
                posList.add(BlockPos.of(Long.parseLong(listTag.getString(i))));

            return new ShcemeBuilder(posList, mainPos, gods1);
        }

        public static void addShceme(UUID uuid, ShcemeBuilder builder) {
            CompoundTag tag = ServerCapManager.getOrCreateData("tf_statue_shceme_builder_data");
            CompoundTag data = new CompoundTag();

            data.putInt("god", builder.god().ordinal());
            data.putLong("main_pos", builder.mainPos().asLong());

            ListTag listTag = new ListTag();

            for (BlockPos pos : builder.posList())
                listTag.add(StringTag.valueOf(String.valueOf(pos.asLong())));

            data.put("complete_poses", listTag);
            tag.put(uuid.toString(), data);

            ServerCapManager.addServerData("tf_statue_shceme_builder_data", tag);
        }

        public static void removeShceme(UUID uuid) {
            CompoundTag tag = ServerCapManager.getOrCreateData("tf_statue_shceme_builder_data");

            if (!tag.contains(uuid.toString()))
                return;

            tag.remove(uuid.toString());
            ServerCapManager.addServerData("tf_statue_shceme_builder_data", tag);
        }

        public static boolean containsShceme(UUID uuid) {
            return ServerCapManager.getOrCreateData("tf_statue_shceme_builder_data").contains(uuid.toString());
        }

        public record ShcemeBuilder(List<BlockPos> posList, BlockPos mainPos, Gods god) {}
    }

    public static class AbilitiesData {
        public static CompoundTag getAbilitiesData(Player player) {
            if (player == null)
                return new CompoundTag();

            CompoundTag abilitiesData = new CompoundTag();
            CompoundTag data = PlayerCapManager.getOrCreateData(player, "tf_data");

            if (data.contains("abilities_data"))
                abilitiesData = data.getCompound("abilities_data");

            return abilitiesData;
        }

        public static void setAbilitiesData(Player player, CompoundTag tag) {
            if (player == null)
                return;

            PlayerCapManager.getOrCreateData(player, "tf_data").put("abilities_data", tag);

            if (!player.level.isClientSide())
                Network.sendTo(new AbilitiesSyncPacket(tag), player);
        }

        @Getter
        @Setter
        public static class Handler implements IAutoNBTSerializable {
            @NBTSerializable
            private boolean lock = false;
            @NBTSerializable
            private boolean buy = false;
            @NBTSerializable
            private boolean active = false;
            @NBTSerializable
            private int level = 1;
        }

        public static class Utils implements IAbilitiesData {
            public static Handler getAbilityData(Player player, String ability) {
                Handler handler = new Handler();
                CompoundTag data = getAbilitiesData(player);

                if (!data.contains(ability)) {
                    data.put(ability, handler.serializeNBT());
                    setAbilitiesData(player, data);
                } else {
                    handler.deserializeNBT(data.getCompound(ability));
                }

                return handler;
            }

            public static void setAbilityData(Player player, String ability, Handler handler) {
                CompoundTag data = getAbilitiesData(player);

                data.put(ability, handler.serializeNBT());

                setAbilitiesData(player, data);
            }

            public boolean isLockAbility(Player player, String ability) {
                return getAbilityData(player, ability).isLock();
            }

            public void setLockAbility(Player player, String ability, boolean value) {
                Handler handler = getAbilityData(player, ability);
                handler.setLock(value);
                setAbilityData(player, ability, handler);
            }

            public boolean isBuyAbility(Player player, String ability) {
                return getAbilityData(player, ability).isBuy();
            }

            public void setBuyAbility(Player player, String ability, boolean value) {
                Handler handler = getAbilityData(player, ability);
                handler.setBuy(value);
                setAbilityData(player, ability, handler);
            }

            public boolean isActiveAbility(Player player, String ability) {
                return getAbilityData(player, ability).isActive();
            }

            public void setActiveAbility(Player player, String ability, boolean value) {
                Handler handler = getAbilityData(player, ability);
                handler.setActive(value);
                setAbilityData(player, ability, handler);
            }

            public int getLevelAbility(Player player, String ability) {
                return getAbilityData(player, ability).getLevel();
            }

            public void setLevelAbility(Player player, String ability, int amount) {
                Handler handler = getAbilityData(player, ability);
                handler.setLevel(amount);
                setAbilityData(player, ability, handler);
            }

            public void addLevelAbility(Player player, String ability, int amount) {
                setLevelAbility(player, ability, Math.max(1, getLevelAbility(player, ability) + amount));
            }
        }
    }

    @Getter
    @Setter
    public static class EffectData implements IAutoNBTSerializable {
        @NBTSerializable
        private int knef = 350;
        @NBTSerializable
        private int selya = 500;
        @NBTSerializable
        private int montu = 750;
        @NBTSerializable
        private int ronos = 1000;
        @NBTSerializable
        private int het = 750;
        @NBTSerializable
        private boolean curseKnef = false;

        public static class Utils implements IEffectData {
            public static EffectData getEffectData(Player player) {
                EffectData fake = new EffectData();
                CompoundTag data = PlayerCapManager.getOrCreateData(player, "tf_data");

                if (data.contains("effect_data"))
                    fake.deserializeNBT(data.getCompound("effect_data"));

                return fake;
            }

            public static void setEffectData(Player player, EffectData data) {
                CompoundTag nbt = data.serializeNBT();
                PlayerCapManager.getOrCreateData(player, "tf_data").put("effect_data", nbt);

                if (!player.level.isClientSide())
                    Network.sendTo(new EffectSyncPacket(nbt), player);
            }

            public int getKnef(Player player) {
                return getEffectData(player).getKnef();
            }

            public int getSelya(Player player) {
                return getEffectData(player).getSelya();
            }

            public int getMontu(Player player) {
                return getEffectData(player).getMontu();
            }

            public int getRonos(Player player) {
                return getEffectData(player).getRonos();
            }

            public int getHet(Player player) {
                return getEffectData(player).getHet();
            }

            public boolean isCurseKnef(Player player) {
                return getEffectData(player).isCurseKnef();
            }

            public void setKnef(Player player, int amount) {
                EffectData data = getEffectData(player);
                data.setKnef(amount);
                setEffectData(player, data);
            }

            public void setSelya(Player player, int amount) {
                EffectData data = getEffectData(player);
                data.setSelya(amount);
                setEffectData(player, data);
            }

            public void setMontu(Player player, int amount) {
                EffectData data = getEffectData(player);
                data.setMontu(amount);
                setEffectData(player, data);
            }

            public void setRonos(Player player, int amount) {
                EffectData data = getEffectData(player);
                data.setRonos(amount);
                setEffectData(player, data);
            }

            public void setHet(Player player, int amount) {
                EffectData data = getEffectData(player);
                data.setHet(amount);
                setEffectData(player, data);
            }

            public void setCurseKnef(Player player, boolean value) {
                if ((isCurseKnef(player) && value) || (!isCurseKnef(player) && !value))
                    return;

                IGuiLevelingData guiLevelingData = new GuiLevelingData.Utils();
                IAbilitiesData abilitiesData = new AbilitiesData.Utils();

                abilitiesData.setLockAbility(player, "recovery", false);
                abilitiesData.setBuyAbility(player, "recovery", false);
                abilitiesData.setActiveAbility(player, "recovery", false);
                abilitiesData.setLevelAbility(player, "recovery", 1);

                if (value)
                    guiLevelingData.setProcentCurse(player, 70);
                else
                    guiLevelingData.setProcentCurse(player, 0);

                EffectData data = getEffectData(player);
                data.setCurseKnef(value);
                setEffectData(player, data);
            }

            public void subKnef(Player player, int amount) {
                setKnef(player, Math.max(getKnef(player) - amount, 0));
            }

            public void subSelya(Player player, int amount) {
                setSelya(player, Math.max(getSelya(player) - amount, 0));
            }

            public void subMontu(Player player, int amount) {
                setMontu(player, Math.max(getMontu(player) - amount, 0));
            }

            public void subRonos(Player player, int amount) {
                setRonos(player, Math.max(getRonos(player) - amount, 0));
            }

            public void subHet(Player player, int amount) {
                setHet(player, Math.max(getHet(player) - amount, 0));
            }
        }
    }

    @Getter
    @Setter
    public static class GuiLevelingData implements IAutoNBTSerializable {
        @NBTSerializable
        private int miningLevel;
        @NBTSerializable
        private int craftLevel;
        @NBTSerializable
        private int fightLevel;
        @NBTSerializable
        private int healthLevel;
        @NBTSerializable
        private int procentCurse;
        private boolean isPlayerScreen;

        public static class Utils implements IGuiLevelingData {
            public static GuiLevelingData getGuiData(Player player) {
                if (player == null && Minecraft.getInstance().player == null)
                    return new GuiLevelingData();
                else if (Minecraft.getInstance().player != null)
                    player = Minecraft.getInstance().player;

                GuiLevelingData fake = new GuiLevelingData();
                CompoundTag data = PlayerCapManager.getOrCreateData(player, "tf_data");

                if (data.contains("gui_data"))
                    fake.deserializeNBT(data.getCompound("gui_data"));

                return fake;
            }

            public static void setGuiData(Player player, GuiLevelingData data) {
                CompoundTag nbt = data.serializeNBT();
                PlayerCapManager.getOrCreateData(player, "tf_data").put("gui_data", nbt);

                if (!player.level.isClientSide())
                    Network.sendTo(new GuiSyncPacket(nbt), player);
            }

            public boolean isPlayerScreen(Player player) {
                return getGuiData(player).isPlayerScreen();
            }

            public int getProcentCurse(Player player) {
                return getGuiData(player).getProcentCurse();
            }

            public int getMiningLevel(Player player) {
                return getGuiData(player).getMiningLevel();
            }

            public int getCraftLevel(Player player) {
                return getGuiData(player).getCraftLevel();
            }

            public int getFightLevel(Player player) {
                return getGuiData(player).getFightLevel();
            }

            public int getHealthLevel(Player player) {
                return getGuiData(player).getHealthLevel();
            }

            public void setPlayerScreen(Player player, boolean value) {
                GuiLevelingData data = getGuiData(player);
                data.setPlayerScreen(value);
                setGuiData(player, data);
            }

            public void setMiningLevel(Player player, int amount) {
                GuiLevelingData data = getGuiData(player);
                data.setMiningLevel(Mth.clamp(amount, 0, 100));
                setGuiData(player, data);
            }

            public void setCraftLevel(Player player, int amount) {
                GuiLevelingData data = getGuiData(player);
                data.setCraftLevel(Mth.clamp(amount, 0, 100));
                setGuiData(player, data);
            }

            public void setFightLevel(Player player, int amount) {
                GuiLevelingData data = getGuiData(player);
                data.setFightLevel(Mth.clamp(amount, 0, 100));
                setGuiData(player, data);
            }

            public void setHealthLevel(Player player, int amount) {
                GuiLevelingData data = getGuiData(player);
                data.setHealthLevel(Mth.clamp(amount, 0, 100));
                setGuiData(player, data);
            }

            public void setProcentCurse(Player player, int amount) {
                amount = Mth.clamp(amount, 0, 70);
                IData.IXpScarabsData xpScarabsData = new Data.XpScarabsData.Utils();

                if (amount == 0) {
                    xpScarabsData.subXpScarabsSilver(player, getProcentCurse(player) * 10);
                    xpScarabsData.subXpScarabsGold(player, getProcentCurse(player) * 20);
                    xpScarabsData.subXpScarabsAuriteh(player, getProcentCurse(player) * 30);
                    xpScarabsData.subXpScarabsLazotep(player, getProcentCurse(player) * 40);
                } else {
                    int procent = getProcentCurse(player) - amount;

                    xpScarabsData.subXpScarabsSilver(player, procent * 10);
                    xpScarabsData.subXpScarabsGold(player, procent * 20);
                    xpScarabsData.subXpScarabsAuriteh(player, procent * 30);
                    xpScarabsData.subXpScarabsLazotep(player, procent * 40);
                }

                GuiLevelingData data = getGuiData(player);
                data.setProcentCurse(amount);
                setGuiData(player, data);
            }

            public void addMiningLevel(Player player, int amount) {
                setMiningLevel(player, Mth.clamp(getMiningLevel(player) + amount, 0, 100));
            }

            public void addCraftLevel(Player player, int amount) {
                setCraftLevel(player, Mth.clamp(getCraftLevel(player) + amount, 0, 100));
            }

            public void addFightLevel(Player player, int amount) {
                setFightLevel(player, Mth.clamp(getFightLevel(player) + amount, 0, 100));
            }

            public void addHealthLevel(Player player, int amount) {
                setHealthLevel(player, Mth.clamp(getHealthLevel(player) + amount, 0, 100));
            }
        }
    }

    @Getter
    @Setter
    public static class XpScarabsData implements IAutoNBTSerializable {
        @NBTSerializable
        private int silver = 500;
        @NBTSerializable
        private int gold = 1000;
        @NBTSerializable
        private int auriteh = 1500;
        @NBTSerializable
        private int lazotep = 2000;

        public static class Utils implements IXpScarabsData {
            public static XpScarabsData getXpScarabsData(Player player) {
                XpScarabsData fake = new XpScarabsData();
                CompoundTag data = PlayerCapManager.getOrCreateData(player, "tf_data");

                if (data.contains("xp_scarabs_data"))
                    fake.deserializeNBT(data.getCompound("xp_scarabs_data"));

                return fake;
            }

            public static void setXpScarabsData(Player player, XpScarabsData data) {
                CompoundTag nbt = data.serializeNBT();
                PlayerCapManager.getOrCreateData(player, "tf_data").put("xp_scarabs_data", nbt);

                if (!player.level.isClientSide())
                    Network.sendTo(new XpScarabsSyncPacket(nbt), player);
            }

            public int getXpScarabSilver(Player player) {
                return getXpScarabsData(player).getSilver();
            }

            public int getXpScarabGold(Player player) {
                return getXpScarabsData(player).getGold();
            }

            public int getXpScarabAuriteh(Player player) {
                return getXpScarabsData(player).getAuriteh();
            }

            public int getXpScarabLazotep(Player player) {
                return getXpScarabsData(player).getLazotep();
            }

            public void setXpScarabSilver(Player player, int amount) {
                XpScarabsData data = getXpScarabsData(player);
                data.setSilver(amount);
                setXpScarabsData(player, data);
            }

            public void setXpScarabGold(Player player, int amount) {
                XpScarabsData data = getXpScarabsData(player);
                data.setGold(amount);
                setXpScarabsData(player, data);
            }

            public void setXpScarabAuriteh(Player player, int amount) {
                XpScarabsData data = getXpScarabsData(player);
                data.setAuriteh(amount);
                setXpScarabsData(player, data);
            }

            public void setXpScarabLazotep(Player player, int amount) {
                XpScarabsData data = getXpScarabsData(player);
                data.setLazotep(amount);
                setXpScarabsData(player, data);
            }

            public void subXpScarab(Player player, int amount) {
                subXpScarabsSilver(player, amount);
                subXpScarabsGold(player, amount);
                subXpScarabsAuriteh(player, amount);
                subXpScarabsLazotep(player, amount);
            }

            public void subXpScarabsSilver(Player player, int amount) {
                setXpScarabSilver(player, getXpScarabSilver(player) - amount);
            }

            public void subXpScarabsGold(Player player, int amount) {
                setXpScarabGold(player, getXpScarabGold(player) - amount);
            }

            public void subXpScarabsAuriteh(Player player, int amount) {
                setXpScarabAuriteh(player, getXpScarabAuriteh(player) - amount);
            }

            public void subXpScarabsLazotep(Player player, int amount) {
                setXpScarabLazotep(player, getXpScarabLazotep(player) - amount);
            }

            public static int addExtraXp(Player player, ScarabsType type) {
                IGuiLevelingData guiLevelingData = new GuiLevelingData.Utils();

                return switch (type) {
                    case SILVER -> guiLevelingData.getProcentCurse(player) * 10;
                    case GOLD -> guiLevelingData.getProcentCurse(player) * 20;
                    case AURITEH -> guiLevelingData.getProcentCurse(player) * 30;
                    case LAZOTEP -> guiLevelingData.getProcentCurse(player) * 40;
                };
            }
        }
    }

    @Getter
    @Setter
    public static class ScarabsData implements IAutoNBTSerializable {
        @NBTSerializable
        private int scarabGold;
        @NBTSerializable
        private int scarabAuriteh;
        @NBTSerializable
        private int scarabLazotep;

        public static class Utils implements IScarabsData {
            public static ScarabsData getScarabsData(Player player) {
                ScarabsData fake = new ScarabsData();
                CompoundTag data = PlayerCapManager.getOrCreateData(player, "tf_data");

                if (data.contains("scarabs_data"))
                    fake.deserializeNBT(data.getCompound("scarabs_data"));

                return fake;
            }

            public static void setScarabsData(Player player, ScarabsData data) {
                CompoundTag nbt = data.serializeNBT();
                PlayerCapManager.getOrCreateData(player, "tf_data").put("scarabs_data", nbt);

                if (!player.level.isClientSide())
                    Network.sendTo(new ScarabsSyncPacket(nbt), player);
            }

            public int getScarabSilver(Player player) {
                IPlayerSkills skills = PlayerSkillsProvider.get(player);
                return skills.getSkillPoints();
            }

            public int getScarabGold(Player player) {
                return getScarabsData(player).getScarabGold();
            }

            public int getScarabAuriteh(Player player) {
                return getScarabsData(player).getScarabAuriteh();
            }

            public int getScarabLazotep(Player player) {
                return getScarabsData(player).getScarabLazotep();
            }

            public void setScarabSilver(Player player, int amount) {
                Network.sendToServer(new SetSkillPointPacket(Math.max(0, amount)));
            }

            public void setScarabGold(Player player, int amount) {
                ScarabsData data = getScarabsData(player);
                data.setScarabGold(amount);
                setScarabsData(player, data);
            }

            public void setScarabAuriteh(Player player, int amount) {
                ScarabsData data = getScarabsData(player);
                data.setScarabAuriteh(amount);
                setScarabsData(player, data);
            }

            public void setScarabLazotep(Player player, int amount) {
                ScarabsData data = getScarabsData(player);
                data.setScarabLazotep(amount);
                setScarabsData(player, data);
            }

            public void addScarabSilver(Player player, int amount) {
                setScarabSilver(player, getScarabSilver(player) + amount);
            }

            public void addScarabGold(Player player, int amount) {
                setScarabGold(player, getScarabGold(player) + amount);
            }

            public void addScarabAuriteh(Player player, int amount) {
                setScarabAuriteh(player, getScarabAuriteh(player) + amount);
            }

            public void addScarabLazotep(Player player, int amount) {
                setScarabLazotep(player, getScarabLazotep(player) + amount);
            }

            public void resetScarabs(Player player) {
                setScarabSilver(player, 0);
                setScarabGold(player, 0);
                setScarabAuriteh(player, 0);
                setScarabLazotep(player, 0);
            }
        }
    }

    @Mod.EventBusSubscriber
    public static class DataActions {
        @SubscribeEvent
        public static void setBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();

            if (player == null)
                return;

            if (!player.isCreative())
                event.setNewSpeed((float) (event.getOriginalSpeed() + (GuiLevelingData.Utils.getGuiData(player).getMiningLevel() * 0.01)));
        }

        @SubscribeEvent
        public static void pickupXp(PlayerXpEvent.PickupXp event) {
            new EffectData.Utils().subHet(event.getEntity(), event.getOrb().value);
        }

        @SubscribeEvent
        public static void attackEntity(AttackEntityEvent event) {
            Player player = event.getEntity();

            if (player == null)
                return;

            new EffectData.Utils().subRonos(player, 1);
        }

        @SubscribeEvent
        public static void deathEntity(LivingDeathEvent event) {
            if (event.getSource().getEntity() instanceof Player player) {
                LivingEntity entity = event.getEntity();
                IXpScarabsData xpScarabsData = new XpScarabsData.Utils();
                IEffectData effectData = new EffectData.Utils();

                if (entity.getMaxHealth() <= 20) {
                    xpScarabsData.subXpScarab(player, 5);
                    effectData.subKnef(player, 1);
                } else if (entity.getMaxHealth() <= 50 && entity.getMaxHealth() > 20) {
                    xpScarabsData.subXpScarab(player, 10);
                    effectData.subKnef(player, 1);
                } else if (entity.getMaxHealth() <= 80 && entity.getMaxHealth() > 50) {
                    xpScarabsData.subXpScarab(player, 15);
                    effectData.subKnef(player, 1);
                } else if (entity.getMaxHealth() <= 100 && entity.getMaxHealth() > 80) {
                    xpScarabsData.subXpScarab(player, 20);
                    effectData.subKnef(player, 1);
                } else {
                    xpScarabsData.subXpScarab(player, 25);
                    effectData.subKnef(player, 1);
                }
            }
        }

        @SubscribeEvent
        public static void breakBlock(BlockEvent.BreakEvent event) {
            if (event.getPlayer() == null)
                return;

            new XpScarabsData.Utils().subXpScarab(event.getPlayer(), 1);
        }

        @SubscribeEvent
        public static void bonemeal(BonemealEvent event) {
            new EffectData.Utils().subSelya(event.getEntity(), 1);
        }

        @SubscribeEvent
        public static void fished(ItemFishedEvent event) {
            new XpScarabsData.Utils().subXpScarab(event.getEntity(), 4);
            new EffectData.Utils().subSelya(event.getEntity(), 1);
        }

        @SubscribeEvent
        public static void crafting(PlayerEvent.ItemCraftedEvent event) {
            new XpScarabsData.Utils().subXpScarab(event.getEntity(), 2);
            new EffectData.Utils().setMontu(event.getEntity(), 1);
        }

        @SubscribeEvent
        public static void healPlayer(LivingHealEvent event) {
            if (event.getEntity() instanceof Player player && new GuiLevelingData.Utils().getHealthLevel(player) > 0)
                event.setAmount(event.getAmount() * (0.8F * new GuiLevelingData.Utils().getHealthLevel(player)));
        }

        @SubscribeEvent
        public static void addExtraDrop(LivingDropsEvent event) {
            Level level = event.getEntity().getLevel();
            Entity entity = event.getSource().getEntity();

            if (entity instanceof LivingEntity living && living instanceof Player player){
                if (MathUtils.isRandom(level, new GuiLevelingData.Utils().getCraftLevel(player))) {
                    if (MathUtils.isRandom(level, 85)) {
                        extraDrop(level, event.getEntity(), 2);
                    } else {
                        if (MathUtils.isRandom(level, 95))
                            extraDrop(level, event.getEntity(), 3);
                        else
                            extraDrop(level, event.getEntity(), 4);
                    }
                }
            }
        }

        private static void extraDrop(Level level, Entity entity, int value) {
            if (level.isClientSide)
                return;

            List<ItemEntity> dropEntities = new ArrayList<>();

            for (Entity entity1 : level.getEntities(null, new AABB(entity.getX()-1, entity.getY()-1, entity.getZ()-1, entity.getX()+1, entity.getY()+1, entity.getZ()+1))) {
                if (entity1 instanceof ItemEntity)
                    dropEntities.add((ItemEntity)entity1);
            }

            for (ItemEntity dropEntity : dropEntities) {
                int tickCount = dropEntity.tickCount;

                if (tickCount <= 1) {
                    ItemStack itemStack = dropEntity.getItem();

                    itemStack.setCount(itemStack.getCount() * value);
                    dropEntity.setItem(itemStack);
                }
            }
        }

        @SubscribeEvent
        public static void playerTick(TickEvent.PlayerTickEvent event) {
            Player player = event.player;

            if (player == null || player.getLevel().isClientSide)
                return;

            IAbilitiesData abilitiesData = new AbilitiesData.Utils();
            IGuiLevelingData guiLevelingData = new GuiLevelingData.Utils();
            IXpScarabsData xpScarabsData = new XpScarabsData.Utils();
            IEffectData effectData = new EffectData.Utils();
            IScarabsData scarabsData = new ScarabsData.Utils();

            if (abilitiesData.isActiveAbility(player, "recovery") && abilitiesData.getLevelAbility(player, "recovery") >= 14) {
                guiLevelingData.setProcentCurse(player, 0);
                effectData.setCurseKnef(player, false);
                abilitiesData.setBuyAbility(player, "recovery", false);
                abilitiesData.setActiveAbility(player, "recovery", false);
                abilitiesData.setLevelAbility(player, "recovery", 1);
            }

            AttributeModifier attack_damage_bonus = new AttributeModifier(UUIDManager.getOrCreate("tf_gui_fight_attack_damage"), ThirteenFlames.MODID + ":attack_damage", (guiLevelingData.getFightLevel(player) * 0.01), AttributeModifier.Operation.ADDITION);
            AttributeInstance attack_damage = player.getAttribute(Attributes.ATTACK_DAMAGE);

            if (!attack_damage.hasModifier(attack_damage_bonus))
                attack_damage.addTransientModifier(attack_damage_bonus);

            if (effectData.getMontu(player) <= 0) {
                int level = player.getEffect(EffectRegistry.BLESSING_MONTU.get()) == null ? 1 : player.getEffect(EffectRegistry.BLESSING_MONTU.get()).getAmplifier() + 1;

                player.addEffect(new MobEffectInstance(EffectRegistry.BLESSING_MONTU.get(), 18000, level, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.montu.add_effect", ChatFormatting.GRAY);
                effectData.setMontu(player, 750);
            }

            if (effectData.getRonos(player) <= 0) {
                int level = player.getEffect(EffectRegistry.BLESSING_RONOSA.get()) == null ? 1 : player.getEffect(EffectRegistry.BLESSING_RONOSA.get()).getAmplifier() + 1;

                player.addEffect(new MobEffectInstance(EffectRegistry.BLESSING_RONOSA.get(), 18000, level, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.ronos.add_effect", ChatFormatting.GRAY);
                effectData.setRonos(player, 1000);
            }

            if (effectData.getKnef(player) <= 0) {
                int level = player.getEffect(EffectRegistry.BLESSING_KNEF.get()) == null ? 1 : player.getEffect(EffectRegistry.BLESSING_KNEF.get()).getAmplifier() + 1;

                player.addEffect(new MobEffectInstance(EffectRegistry.BLESSING_KNEF.get(), 18000, level, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.knef.add_effect", ChatFormatting.GRAY);
                effectData.setKnef(player, 350);
            }

            if (effectData.getSelya(player) <= 0) {
                int level = player.getEffect(EffectRegistry.BLESSING_SELIASET.get()) == null ? 1 : player.getEffect(EffectRegistry.BLESSING_SELIASET.get()).getAmplifier() + 1;

                player.addEffect(new MobEffectInstance(EffectRegistry.BLESSING_SELIASET.get(), 18000, level, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.seliaset.add_effect", ChatFormatting.GRAY);
                effectData.setSelya(player, 500);
            }

            if (effectData.getHet(player) <= 0) {
                int level = player.getEffect(EffectRegistry.BLESSING_HET.get()) == null ? 1 : player.getEffect(EffectRegistry.BLESSING_HET.get()).getAmplifier() + 1;

                player.addEffect(new MobEffectInstance(EffectRegistry.BLESSING_HET.get(), 18000, level, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.het.add_effect", ChatFormatting.GRAY);
                effectData.setHet(player, 750);
            }

            if (xpScarabsData.getXpScarabSilver(player) <= 0) {
                xpScarabsData.setXpScarabSilver(player, 500 + XpScarabsData.Utils.addExtraXp(player, ScarabsType.SILVER));
                scarabsData.addScarabSilver(player, 1);

                MessageManager.addMessage(new ResourceLocation(ThirteenFlames.MODID, "textures/gui/icon/scarab_silver_icon.png"), Component.translatable("message.thirteen_flames.add_scarabs", Component.translatable("message.thirteen_flames.scarab_type.silver").getString()));
            }

            if (xpScarabsData.getXpScarabGold(player) <= 0) {
                xpScarabsData.setXpScarabGold(player, 750 + XpScarabsData.Utils.addExtraXp(player, ScarabsType.GOLD));
                scarabsData.addScarabGold(player, 1);

                MessageManager.addMessage(new ResourceLocation(ThirteenFlames.MODID, "textures/gui/icon/scarab_gold_icon.png"), Component.translatable("message.thirteen_flames.add_scarabs", Component.translatable("message.thirteen_flames.scarab_type.gold").getString()));
            }

            if (xpScarabsData.getXpScarabAuriteh(player) <= 0) {
                xpScarabsData.setXpScarabAuriteh(player, 1000 + XpScarabsData.Utils.addExtraXp(player, ScarabsType.AURITEH));
                scarabsData.addScarabAuriteh(player, 1);

                MessageManager.addMessage(new ResourceLocation(ThirteenFlames.MODID, "textures/gui/icon/scarab_lazotep_icon.png"), Component.translatable("message.thirteen_flames.add_scarabs", Component.translatable("message.thirteen_flames.scarab_type.auriteh").getString()));
            }

            if (xpScarabsData.getXpScarabLazotep(player) <= 0) {
                xpScarabsData.setXpScarabLazotep(player, 2000 + XpScarabsData.Utils.addExtraXp(player, ScarabsType.LAZOTEP));
                scarabsData.addScarabLazotep(player, 1);

                MessageManager.addMessage(new ResourceLocation(ThirteenFlames.MODID, "textures/gui/icon/scarab_silver_icon.png"), Component.translatable("message.thirteen_flames.add_scarabs", Component.translatable("message.thirteen_flames.scarab_type.lazotep").getString()));
            }
        }
    }
}
