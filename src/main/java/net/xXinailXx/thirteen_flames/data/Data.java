package net.xXinailXx.thirteen_flames.data;

import daripher.skilltree.capability.skill.IPlayerSkills;
import daripher.skilltree.capability.skill.PlayerSkillsProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xXinailXx.enderdragonlib.capability.managers.UUIDManager;
import net.xXinailXx.enderdragonlib.client.utils.MessageUtil;
import net.xXinailXx.enderdragonlib.utils.MathUtils;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.block.entity.StatueBE;
import net.xXinailXx.thirteen_flames.client.gui.button.abilities.data.*;
import net.xXinailXx.thirteen_flames.init.EffectRegistry;
import net.xXinailXx.thirteen_flames.network.packet.SetSkillPointPacket;
import net.xXinailXx.thirteen_flames.utils.Gods;
import org.zeith.hammerlib.net.Network;

import java.util.*;

@Mod.EventBusSubscriber
public class Data implements IData {
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        AbilitiesData.serializeNBT(nbt);
        EffectData.serializeNBT(nbt);
        GuiLevelingData.serializeNBT(nbt);
        XpScarabsData.serializeNBT(nbt);
        ScarabsData.serializeNBT(nbt);

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        AbilitiesData.deserializeNBT(nbt);
        EffectData.deserializeNBT(nbt);
        GuiLevelingData.deserializeNBT(nbt);
        XpScarabsData.deserializeNBT(nbt);
        ScarabsData.deserializeNBT(nbt);
    }

    public static class StatueBuilderData {
        private static final List<StatueBuilder> STATUE_LIST = new ArrayList<>();
        private static final List<StatueBE> STATUE_BE_LIST = new ArrayList<>();
        private static final List<ShcemeBuilder> SHCEME_BUILDER_LIST = new ArrayList();
        private static final List<UUID> SHCEME_BUILDER_UUID_LIST = new ArrayList<>();

        public static void addShceme(ShcemeBuilder builder, UUID uuid) {
            SHCEME_BUILDER_LIST.add(builder);
            SHCEME_BUILDER_UUID_LIST.add(uuid);
        }

        public static void removeShceme(ShcemeBuilder builder, UUID uuid) {
            SHCEME_BUILDER_LIST.remove(builder);
            SHCEME_BUILDER_UUID_LIST.remove(uuid);
        }

        public static void addStatue(StatueBuilder statues, StatueBE entity) {
            STATUE_LIST.add(statues);
            STATUE_BE_LIST.add(entity);
        }

        public static void removeStatue(StatueBuilder statues) {
            if (!STATUE_LIST.contains(statues))
                return;

            STATUE_BE_LIST.remove(STATUE_LIST.indexOf(statues));
            STATUE_LIST.remove(statues);
        }

        public static void removeStatue(StatueBE entity) {
            if (!STATUE_BE_LIST.contains(entity))
                return;

            STATUE_LIST.remove(STATUE_BE_LIST.indexOf(entity));
            STATUE_BE_LIST.remove(entity);
        }

        public static List<StatueBuilder> getStatueList() {
            return STATUE_LIST;
        }

        public static List<StatueBE> getStatueBEList() {
            return STATUE_BE_LIST;
        }

        public static List<ShcemeBuilder> getShcemeBuilderList() {
            return SHCEME_BUILDER_LIST;
        }

        public static List<UUID> getShcemeBuilderUuidList() {
            return SHCEME_BUILDER_UUID_LIST;
        }

        public record ShcemeBuilder(List<BlockPos> posList, BlockPos mainPos, Gods god) {}

        public record StatueBuilder(List<BlockPos> posList, BlockPos mainPos) {}
    }

    public static class AbilitiesData {
        private static CompoundTag abilitiesTags = new CompoundTag();

        private static void serializeNBT(CompoundTag nbt) {
            nbt.put("abilities_data", abilitiesTags);

            AbilityStorage.abilitiesList.forEach(ability -> ability.getInfo().serializeNBT(nbt));
        }

        private static void deserializeNBT(CompoundTag nbt) {
            if (nbt.contains("abilities_data"))
                abilitiesTags = nbt.getCompound("abilities_data");
            else
                abilitiesTags = new CompoundTag();

            AbilityStorage.abilitiesList.forEach(ability -> ability.getInfo().deserializeNBT(nbt));
        }

        public static class Handler {
            private final String abilityName;

            public Handler(String abilityName) {
                this.abilityName = abilityName;
            }

            private void serializeNBT(CompoundTag nbt) {
                nbt.putBoolean(this.abilityName + "_lock", isLock());
                nbt.putBoolean(this.abilityName + "_buy", isBuy());
                nbt.putBoolean(this.abilityName + "_active", isActive());
                nbt.putInt(this.abilityName + "_level", getLevel());
            }

            private void deserializeNBT(CompoundTag nbt) {
                setLock(nbt.getBoolean(this.abilityName + "_lock"));
                setBuy(nbt.getBoolean(this.abilityName + "_buy"));
                setActive(nbt.getBoolean(this.abilityName + "_active"));
                setLevel(nbt.getInt(this.abilityName + "_level"));
            }

            private boolean isLock() {
                boolean lock = false;

                if (!abilitiesTags.contains(abilityName + "_lock")) {
                    lock = false;
                    abilitiesTags.putBoolean(abilityName + "_lock", false);
                } else if (abilitiesTags.getBoolean(abilityName + "_lock") != lock) {
                    if (abilitiesTags.getBoolean(abilityName + "_lock") && !lock)
                        lock = true;
                    else
                        abilitiesTags.putBoolean(abilityName + "_lock", true);
                }

                return lock;
            }

            private void setLock(boolean value) {
                abilitiesTags.putBoolean(abilityName + "_lock", value);
            }

            private boolean isBuy() {
                boolean buy = false;

                if (!abilitiesTags.contains(abilityName + "_buy")) {
                    buy = false;
                    abilitiesTags.putBoolean(abilityName + "_buy", false);
                } else if (abilitiesTags.getBoolean(abilityName + "_buy") != buy) {
                    if (abilitiesTags.getBoolean(abilityName + "_buy") && !buy)
                        buy = true;
                    else
                        abilitiesTags.putBoolean(abilityName + "_buy", true);
                }

                if (!buy) {
                    setActive(false);
                    setLevel(0);
                }

                return buy;
            }

            private void setBuy(boolean value) {
                if (!value) {
                    setActive(false);
                    setLevel(0);
                }

                abilitiesTags.putBoolean(abilityName + "_buy", value);
            }

            private boolean isActive() {
                boolean active = false;

                if (!abilitiesTags.contains(abilityName + "_active")) {
                    active = false;
                    abilitiesTags.putBoolean(abilityName + "_active", false);
                } else  if (abilitiesTags.getBoolean(abilityName + "_active") != active) {
                    if (abilitiesTags.getBoolean(abilityName + "_active") && !active)
                        active = true;
                    else
                        abilitiesTags.putBoolean(abilityName + "_active", true);
                }

                return active;
            }

            private void setActive(boolean value) {
                abilitiesTags.putBoolean(abilityName + "_active", value);
            }

            private int getLevel() {
                int level = 0;

                if (!abilitiesTags.contains(abilityName + "_level")) {
                    level = 0;
                    abilitiesTags.putInt(abilityName + "_level", 0);
                } else if (abilitiesTags.getInt(abilityName + "_level") != level) {
                    if (abilitiesTags.getInt(abilityName + "_level") != level)
                        level = abilitiesTags.getInt(abilityName + "_level");
                    else
                        abilitiesTags.putInt(abilityName + "_level", level);
                }

                return level;
            }

            private void setLevel(int amount) {
                abilitiesTags.putInt(abilityName + "_level", Math.max(amount, 1));
            }

            private void addLevel(int amount, int maxLevel) {
                if (amount < 0)
                    setLevel(Math.max(getLevel() - amount, 1));
                else
                    setLevel(Math.min(getLevel() + amount, maxLevel));
            }
        }

        public static class Utils implements IAbilitiesData {
            public Handler getAbilityHandler(String abilityName) {
                return new Handler(abilityName);
            }

            public boolean isLockAbility(String abilityName) {
                return getAbilityHandler(abilityName).isLock();
            }

            public void setLockAbility(String abilityName, boolean value) {
                getAbilityHandler(abilityName).setLock(value);
            }

            public boolean isBuyAbility(String abilityName) {
                return getAbilityHandler(abilityName).isBuy();
            }

            public void setBuyAbility(String abilityName, boolean value) {
                getAbilityHandler(abilityName).setBuy(value);
            }

            public boolean isActiveAbility(String abilityName) {
                return getAbilityHandler(abilityName).isActive();
            }

            public void setActiveAbility(String abilityName, boolean value) {
                getAbilityHandler(abilityName).setActive(value);
            }

            public int getLevelAbility(String abilityName) {
                return getAbilityHandler(abilityName).getLevel();
            }

            public void setLevelAbility(String abilityName, int amount) {
                getAbilityHandler(abilityName).setLevel(amount);
            }

            public void addLevelAbility(String abilityName, int amount, int maxLevel) {
                getAbilityHandler(abilityName).addLevel(amount, maxLevel);
            }
        }
    }

    public static class EffectData implements IEffectData {
        private static int effectMontuAmount = 750;
        private static int effectRonosAmount = 750;
        private static int effectKnefAmount = 350;
        private static int effectSelyaAmount = 500;
        private static int effectHetAmount = 750;
        private static boolean curseKnef = false;

        private static void serializeNBT(CompoundTag nbt) {
            nbt.putInt("effect_montu_amount", effectMontuAmount);
            nbt.putInt("effect_ronos_amount", effectRonosAmount);
            nbt.putInt("effect_knef_amount", effectKnefAmount);
            nbt.putInt("effect_selya_amount", effectSelyaAmount);
            nbt.putInt("effect_het_amount", effectHetAmount);
            nbt.putBoolean("curse_knef", curseKnef);
        }

        private static void deserializeNBT(CompoundTag nbt) {
            effectMontuAmount = nbt.getInt("effect_montu_amount");
            effectRonosAmount = nbt.getInt("effect_ronos_amount");
            effectKnefAmount = nbt.getInt("effect_knef_amount");
            effectSelyaAmount = nbt.getInt("effect_selya_amount");
            effectHetAmount = nbt.getInt("effect_het_amount");
            curseKnef = nbt.getBoolean("curse_knef");
        }

        public int getEffectMontuAmount() {
            return effectMontuAmount;
        }

        public void setEffectMontuAmount(int amount) {
            effectMontuAmount = amount;
        }

        public int getEffectRonosAmount() {
            return effectRonosAmount;
        }

        public void setEffectRonosAmount(int amount) {
            effectRonosAmount = amount;
        }

        public int getEffectKnefAmount() {
            return effectKnefAmount;
        }

        public void setEffectKnefAmount(int amount) {
            effectKnefAmount = amount;
        }

        public int getEffectSelyaAmount() {
            return effectSelyaAmount;
        }

        public void setEffectSelyaAmount(int amount) {
            effectSelyaAmount = amount;
        }

        public int getEffectHetAmount() {
            return effectHetAmount;
        }

        public void setEffectHetAmount(int amount) {
            effectHetAmount = amount;
        }

        public boolean isCurseKnef() {
            return curseKnef;
        }

        public void setCurseKnef(boolean value) {
            curseKnef = value;
        }

        public void subEffectMontuAmount(int amount) {
            effectMontuAmount = Math.max(effectMontuAmount - amount, 0);
        }

        public void subEffectRonosAmount(int amount) {
            effectRonosAmount = Math.max(effectRonosAmount - amount, 0);
        }

        public void subEffectKnefAmount(int amount) {
            effectKnefAmount = Math.max(effectKnefAmount - amount, 0);
        }

        public void subEffectSelyaAmount(int amount) {
            effectSelyaAmount = Math.max(effectSelyaAmount - amount, 0);
        }

        public void subEffectHetAmount(int amount) {
            effectHetAmount = Math.max(effectHetAmount - amount, 0);
        }
    }

    public static class GuiLevelingData implements IGuiLevelingData {
        private static int guiMiningLevelAmount;
        private static int guiCraftLevelAmount;
        private static int guiFightLevelAmount;
        private static int guiHealthLevelAmount;
        private static int procentCurse;
        private static boolean isPlayerScreen;

        private static void serializeNBT(CompoundTag nbt) {
            nbt.putInt("gui_mining_level_amount", guiMiningLevelAmount);
            nbt.putInt("gui_craft_level_amount", guiCraftLevelAmount);
            nbt.putInt("gui_fight_level_amount", guiFightLevelAmount);
            nbt.putInt("gui_health_level_amount", guiHealthLevelAmount);
            nbt.putInt("procent_curse", procentCurse);
        }

        private static void deserializeNBT(CompoundTag nbt) {
            guiMiningLevelAmount = nbt.getInt("gui_mining_level_amount");
            guiCraftLevelAmount = nbt.getInt("gui_craft_level_amount");
            guiFightLevelAmount = nbt.getInt("gui_fight_level_amount");
            guiHealthLevelAmount = nbt.getInt("gui_health_level_amount");
            procentCurse = nbt.getInt("procent_curse");
        }

        public int getGuiMiningLevelAmount() {
            return guiMiningLevelAmount;
        }

        public void setGuiMiningLevelAmount(int amount) {
            guiMiningLevelAmount = Math.min(amount, 100);
        }

        public int getGuiCraftLevelAmount() {
            return guiCraftLevelAmount;
        }

        public void setGuiCraftLevelAmount(int amount) {
            guiCraftLevelAmount = Math.min(amount, 100);
        }

        public int getGuiFightLevelAmount() {
            return guiFightLevelAmount;
        }

        public void setGuiFightLevelAmount(int amount) {
            guiFightLevelAmount = Math.min(amount, 100);
        }

        public int getGuiHealthLevelAmount() {
            return guiHealthLevelAmount;
        }

        public void setGuiHealthLevelAmount(int amount) {
            guiHealthLevelAmount = Math.min(amount, 100);
        }

        public int getProcentCurse() {
            return procentCurse;
        }

        public void setProcentCurse(int value) {
            procentCurse = value;
        }

        public boolean isPlayerScreen() {
            return isPlayerScreen;
        }

        public void setPlayerScreen(boolean value) {
            isPlayerScreen = value;
        }

        public void addGuiMiningLevelAmount(int amount) {
            setGuiMiningLevelAmount(getGuiMiningLevelAmount() + amount);
        }

        public void addGuiCraftLevelAmount(int amount) {
            setGuiCraftLevelAmount(getGuiCraftLevelAmount() + amount);
        }

        public void addGuiFightLevelAmount(int amount) {
            setGuiFightLevelAmount(getGuiFightLevelAmount() + amount);
        }

        public void addGuiHealthLevelAmount(int amount) {
            setGuiHealthLevelAmount(getGuiHealthLevelAmount() + amount);
        }

        public void subProcentCurse(int amount) {
            setProcentCurse(getProcentCurse() + amount);
        }
    }

    public static class XpScarabsData implements IXpScarabsData {
        private static int xpScarabSilver;
        private static int xpScarabGold;
        private static int xpScarabAuriteh;
        private static int xpScarabLazotep;

        private static void serializeNBT(CompoundTag nbt) {
            nbt.putInt("xp_scarab_silver", xpScarabSilver);
            nbt.putInt("xp_scarab_gold", xpScarabGold);
            nbt.putInt("xp_scarab_auriteh", xpScarabAuriteh);
            nbt.putInt("xp_scarab_lazotep", xpScarabLazotep);
        }

        private static void deserializeNBT(CompoundTag nbt) {
            xpScarabSilver = nbt.getInt("xp_scarab_silver");
            xpScarabGold = nbt.getInt("xp_scarab_gold");
            xpScarabAuriteh = nbt.getInt("xp_scarab_auriteh");
            xpScarabLazotep = nbt.getInt("xp_scarab_lazotep");
        }

        public int getXpScarabSilver() {
            return xpScarabSilver;
        }

        public void setXpScarabSilver(int amount) {
            if (amount > 500 + addExtraXp(ScarabsType.SILVER)) {
                xpScarabSilver = 500 + addExtraXp(ScarabsType.SILVER);
            } else {
                xpScarabSilver = Math.max(amount, 0);
            }
        }

        public int getXpScarabGold() {
            return xpScarabGold;
        }

        public void setXpScarabGold(int amount) {
            if (amount > 1000 + addExtraXp(ScarabsType.GOLD)) {
                xpScarabGold = 1000 + addExtraXp(ScarabsType.GOLD);
            } else {
                xpScarabGold = Math.max(amount, 0);
            }
        }

        public int getXpScarabAuriteh() {
            return xpScarabAuriteh;
        }

        public void setXpScarabAuriteh(int amount) {
            if (amount > 1500 + addExtraXp(ScarabsType.AURITEH)) {
                xpScarabAuriteh = 1500 + addExtraXp(ScarabsType.AURITEH);
            } else {
                xpScarabAuriteh = Math.max(amount, 0);
            }
        }

        public int getXpScarabLazotep() {
            return xpScarabLazotep;
        }

        public void setXpScarabLazotep(int amount) {
            if (amount > 2000 + addExtraXp(ScarabsType.LAZOTEP)) {
                xpScarabLazotep = 2000 + addExtraXp(ScarabsType.LAZOTEP);
            } else {
                xpScarabLazotep = Math.max(amount, 0);
            }
        }

        public void subXpScarab(int amount) {
            subXpScarabsSilver(amount);
            subXpScarabsGold(amount);
            subXpScarabsAuriteh(amount);
            subXpScarabsLazotep(amount);
        }

        public void subXpScarabsSilver(int amount) {
            xpScarabSilver = Math.max(xpScarabSilver - amount, 0);
        }

        public void subXpScarabsGold(int amount) {
            xpScarabGold = Math.max(xpScarabGold - amount, 0);
        }

        public void subXpScarabsAuriteh(int amount) {
            xpScarabAuriteh = Math.max(xpScarabAuriteh - amount, 0);
        }

        public void subXpScarabsLazotep(int amount) {
            xpScarabLazotep = Math.max(xpScarabLazotep - amount, 0);
        }

        private static int addExtraXp(ScarabsType type) {
            IGuiLevelingData guiLevelingData = new GuiLevelingData();

            return switch (type) {
                case SILVER -> guiLevelingData.getProcentCurse() * 10;
                case GOLD -> guiLevelingData.getProcentCurse() * 20;
                case AURITEH -> guiLevelingData.getProcentCurse() * 30;
                case LAZOTEP -> guiLevelingData.getProcentCurse() * 40;
            };
        }

        private enum ScarabsType {
            SILVER,
            GOLD,
            AURITEH,
            LAZOTEP
        }
    }

    public static class ScarabsData implements IScarabsData {
        private static int scarabGold;
        private static int scarabAuriteh;
        private static int scarabLazotep;

        private static void serializeNBT(CompoundTag nbt) {
            nbt.putInt("scarab_gold", scarabGold);
            nbt.putInt("scarab_auriteh", scarabAuriteh);
            nbt.putInt("scarab_lazotep", scarabLazotep);
        }

        private static void deserializeNBT(CompoundTag nbt) {
            scarabGold = nbt.getInt("scarab_gold");
            scarabAuriteh = nbt.getInt("scarab_auriteh");
            scarabLazotep = nbt.getInt("scarab_lazotep");
        }

        public int getScarabSilver(Player player) {
            IPlayerSkills skills = PlayerSkillsProvider.get(player);
            return skills.getSkillPoints();
        }

        public void setScarabSilver(Player player, int amount) {
            Network.sendToServer(new SetSkillPointPacket(Math.max(amount, 0)));
        }

        public int getScarabGold() {
            return scarabGold;
        }

        public void setScarabGold(int amount) {
            scarabGold = amount;
        }

        public int getScarabAuriteh() {
            return scarabAuriteh;
        }

        public void setScarabAuriteh(int amount) {
            scarabAuriteh = amount;
        }

        public int getScarabLazotep() {
            return scarabLazotep;
        }

        public void setScarabLazotep(int amount) {
            scarabLazotep = amount;
        }

        public void addScarabSilver(Player player, int amount) {
            setScarabSilver(player, getScarabSilver(player) + amount);
        }

        public void addScarabGold(int amount) {
            scarabGold = scarabGold + amount;
        }

        public void addScarabAuriteh(int amount) {
            scarabAuriteh = scarabAuriteh + amount;
        }

        public void addScarabLazotep(int amount) {
            scarabLazotep = scarabLazotep + amount;
        }

        public void resetScarabs(Player player) {
            setScarabSilver(player, 0);
            setScarabGold(0);
            setScarabAuriteh(0);
            setScarabLazotep(0);
        }
    }

    @Mod.EventBusSubscriber
    public static class DataActions {
        private static final IAbilitiesData abilitiesData = new AbilitiesData.Utils();
        private static final IGuiLevelingData guiLevelingData = new GuiLevelingData();
        private static final IXpScarabsData xpScarabsData = new XpScarabsData();
        private static final IScarabsData scarabsData = new ScarabsData();
        private static final IEffectData effectData = new EffectData();

        @SubscribeEvent
        public static void setBreakSpeed(PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();

            if (!player.isCreative())
                event.setNewSpeed((float) (event.getOriginalSpeed() + (guiLevelingData.getGuiMiningLevelAmount() * 0.01)));
        }

        @SubscribeEvent
        public static void pickupXp(PlayerXpEvent.PickupXp event) {
            effectData.subEffectHetAmount(event.getOrb().value);
        }

        @SubscribeEvent
        public static void deathEntity(LivingDeathEvent event) {
            if (event.getSource().getEntity() instanceof Player player) {
                LivingEntity entity = event.getEntity();

                if (entity.getMaxHealth() <= 20) {
                    xpScarabsData.subXpScarab(5);
                    effectData.subEffectRonosAmount(1);
                    effectData.subEffectKnefAmount(1);
                } else if (entity.getMaxHealth() <= 50 && entity.getMaxHealth() > 20) {
                    xpScarabsData.subXpScarab(10);
                    effectData.subEffectRonosAmount(2);
                    effectData.subEffectKnefAmount(1);
                } else if (entity.getMaxHealth() <= 80 && entity.getMaxHealth() > 50) {
                    xpScarabsData.subXpScarab(15);
                    effectData.subEffectRonosAmount(3);
                    effectData.subEffectKnefAmount(1);
                } else if (entity.getMaxHealth() <= 100 && entity.getMaxHealth() > 80) {
                    xpScarabsData.subXpScarab(20);
                    effectData.subEffectRonosAmount(4);
                    effectData.subEffectKnefAmount(1);
                } else {
                    xpScarabsData.subXpScarab(25);
                    effectData.subEffectRonosAmount(6);
                    effectData.subEffectKnefAmount(1);
                }
            }
        }

        @SubscribeEvent
        public static void harvest(PlayerEvent.HarvestCheck event) {
            xpScarabsData.subXpScarab(1);
        }

        @SubscribeEvent
        public static void bonemeal(BonemealEvent event) {
            effectData.subEffectSelyaAmount(1);
        }

        @SubscribeEvent
        public static void fished(ItemFishedEvent event) {
            xpScarabsData.subXpScarab(4);
            effectData.subEffectSelyaAmount(1);
        }

        @SubscribeEvent
        public static void crafting(PlayerEvent.ItemCraftedEvent event) {
            xpScarabsData.subXpScarab(2);
            effectData.setEffectMontuAmount(1);
        }

        @SubscribeEvent
        public static void playerLogged(PlayerEvent.PlayerLoggedInEvent event) {
            Player player = event.getEntity();

            if (xpScarabsData.getXpScarabSilver() <= 0)
                xpScarabsData.setXpScarabSilver(500 + XpScarabsData.addExtraXp(XpScarabsData.ScarabsType.SILVER));
            if (xpScarabsData.getXpScarabGold() <= 0)
                xpScarabsData.setXpScarabGold(750 + XpScarabsData.addExtraXp(XpScarabsData.ScarabsType.GOLD));
            if (xpScarabsData.getXpScarabAuriteh() <= 0)
                xpScarabsData.setXpScarabAuriteh(1000 + XpScarabsData.addExtraXp(XpScarabsData.ScarabsType.AURITEH));
            if (xpScarabsData.getXpScarabLazotep() <= 0)
                xpScarabsData.setXpScarabLazotep(2000 + XpScarabsData.addExtraXp(XpScarabsData.ScarabsType.LAZOTEP));
        }

        @SubscribeEvent
        public static void healPlayer(LivingHealEvent event) {
            if (event.getEntity() instanceof Player player && guiLevelingData.getGuiHealthLevelAmount() > 0)
                event.setAmount((float) (event.getAmount() * (0.8 * guiLevelingData.getGuiHealthLevelAmount())));
        }

        @SubscribeEvent
        public static void addExtraDrop(LivingDropsEvent event) {
            Level level = event.getEntity().getLevel();

            if (level == null && !(event.getSource().getEntity() instanceof Player))
                return;

            if (MathUtils.isRandom(level, guiLevelingData.getGuiCraftLevelAmount())) {
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

        private static void extraDrop(Level level, Entity entity, int value) {
            if (level.isClientSide)
                return;

            List<ItemEntity> dropEntities = new ArrayList<ItemEntity>();

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

            if (player == null)
                return;

            if (guiLevelingData.getGuiMiningLevelAmount() < 0)
                guiLevelingData.setGuiMiningLevelAmount(0);
            if (guiLevelingData.getGuiCraftLevelAmount() < 0)
                guiLevelingData.setGuiCraftLevelAmount(0);
            if (guiLevelingData.getGuiFightLevelAmount() < 0)
                guiLevelingData.setGuiFightLevelAmount(0);
            if (guiLevelingData.getGuiHealthLevelAmount() < 0)
                guiLevelingData.setGuiHealthLevelAmount(0);

            if (guiLevelingData.getProcentCurse() <= 0) {
                guiLevelingData.setProcentCurse(0);
                effectData.setCurseKnef(false);
                abilitiesData.setBuyAbility("recovery", false);
            } else {
                guiLevelingData.setProcentCurse(70 - (abilitiesData.isActiveAbility("recovery") ? abilitiesData.getLevelAbility("recovery") * 5 : 0));
            }

            AttributeModifier attack_damage_bonus = new AttributeModifier(UUIDManager.getOrCreate("gui_mining_attack_damage"), ThirteenFlames.MODID + ":attack_damage", (guiLevelingData.getGuiFightLevelAmount() * 0.01), AttributeModifier.Operation.ADDITION);
            AttributeInstance attack_damage = player.getAttribute(Attributes.ATTACK_DAMAGE);

            if (!attack_damage.hasModifier(attack_damage_bonus)) {
                attack_damage.addTransientModifier(attack_damage_bonus);
            }

            if (effectData.getEffectMontuAmount() <= 0) {
                player.addEffect(new MobEffectInstance(EffectRegistry.BLESSING_MONTU.get(), 18000, 1, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.montu.add_effect", ChatFormatting.GRAY);
                effectData.setEffectMontuAmount(750);
            }
            if (effectData.getEffectRonosAmount() <= 0) {
                player.addEffect(new MobEffectInstance( EffectRegistry.BLESSING_RONOSA.get(), 18000, 1, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.ronos.add_effect", ChatFormatting.GRAY);
                effectData.setEffectRonosAmount(750);
            }
            if (effectData.getEffectKnefAmount() <= 0) {
                player.addEffect(new MobEffectInstance( EffectRegistry.BLESSING_KNEF.get(), 18000, 1, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.knef.add_effect", ChatFormatting.GRAY);
                effectData.setEffectKnefAmount(350);
            }
            if (effectData.getEffectSelyaAmount() <= 0) {
                player.addEffect(new MobEffectInstance( EffectRegistry.BLESSING_SELIASET.get(), 18000, 1, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.seliaset.add_effect", ChatFormatting.GRAY);
                effectData.setEffectSelyaAmount(500);
            }
            if (effectData.getEffectHetAmount() <= 0) {
                player.addEffect(new MobEffectInstance( EffectRegistry.BLESSING_HET.get(), 18000, 1, true, true));
                MessageUtil.displayClientMessageTranslateStyle(player, "message.thirteen_flames.het.add_effect", ChatFormatting.GRAY);
                effectData.setEffectHetAmount(750);
            }

            if (xpScarabsData.getXpScarabSilver() <= 0) {
                xpScarabsData.setXpScarabSilver(500 + XpScarabsData.addExtraXp(XpScarabsData.ScarabsType.SILVER));

                if (!player.getLevel().isClientSide)
                    scarabsData.addScarabSilver(player, 2);
            }
            if (xpScarabsData.getXpScarabGold() <= 0) {
                xpScarabsData.setXpScarabGold(750 + XpScarabsData.addExtraXp(XpScarabsData.ScarabsType.SILVER));
                scarabsData.addScarabGold(1);
            }
            if (xpScarabsData.getXpScarabAuriteh() <= 0) {
                xpScarabsData.setXpScarabAuriteh(1000 + XpScarabsData.addExtraXp(XpScarabsData.ScarabsType.SILVER));
                scarabsData.addScarabAuriteh(1);
            }
            if (xpScarabsData.getXpScarabLazotep() <= 0) {
                xpScarabsData.setXpScarabLazotep(2000 + XpScarabsData.addExtraXp(XpScarabsData.ScarabsType.SILVER));
                scarabsData.addScarabLazotep(1);
            }
        }
    }
}
