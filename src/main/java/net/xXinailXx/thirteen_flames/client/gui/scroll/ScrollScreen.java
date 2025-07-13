package net.xXinailXx.thirteen_flames.client.gui.scroll;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.hurts.sskirillss.relics.items.relics.base.utils.AbilityUtils;
import it.hurts.sskirillss.relics.utils.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xXinailXx.enderdragonlib.client.utils.gui.AbstractWidget;
import net.xXinailXx.thirteen_flames.ThirteenFlames;
import net.xXinailXx.thirteen_flames.client.gui.button.gui.*;
import net.xXinailXx.thirteen_flames.network.packet.ScrollMenuOpenPacket;
import net.xXinailXx.thirteen_flames.network.packet.ScrollScreenClosePacket;
import org.zeith.hammerlib.net.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ScrollScreen extends AbstractContainerScreen<ScrollMenu> {
    public static final ResourceLocation BACKGROUNG = new ResourceLocation(ThirteenFlames.MODID, "textures/gui/scroll_background.png");
    private final Minecraft MC = Minecraft.getInstance();
    private final List<AbstractWidget> possibleWidget = new ArrayList<>();
    private final List<AbstractWidget> enchWidget = new ArrayList<>();
    public final Map<Enchantment, EnchantmentUtils.Ench> possibleEnchs;
    public final Map<Enchantment, Integer> enchs;
    private EnchantmentUtils.Ench enchSelected;
    private final Inventory inventory;
    private final int backgroundHeight = 249;
    private final int backgroundWidth = 331;
    private int scrollType;
    private double expModifier;
    private ItemStack stack;
    private int enchLevel;
    private int expCost = 0;
    public int scrollY0;
    public int scrollY1;

    public ScrollScreen(ScrollMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.inventory = inventory;
        this.scrollType = NBTUtils.getInt(menu.scrollStack, "type", 0);
        this.expModifier = AbilityUtils.getAbilityValue(menu.scrollStack, "catalog", "modifier");
        this.stack = menu.stack;
        this.possibleEnchs = menu.possibleEnchs;
        this.enchs = menu.enchs;
        this.enchSelected = menu.ench;
        this.enchLevel = menu.enchLevel;
        this.scrollY0 = menu.scroll0;
        this.scrollY1 = menu.scroll1;

        for (Enchantment enchantment : this.enchs.keySet()) {
            int i = 0;

            switch (enchantment.getRarity()) {
                case COMMON:
                    i = 1;
                    break;
                case UNCOMMON:
                    i = 2;
                    break;
                case RARE:
                    i = 4;
                    break;
                case VERY_RARE:
                    i = 8;
            }

            this.expCost += (int) (this.enchs.get(enchantment) * i * AbilityUtils.getAbilityValue(this.menu.scrollStack, "catalog", "modifier"));
        }
    }

    protected void containerTick() {
        super.containerTick();
        this.menu.tick();

        this.stack = this.menu.stack;
        this.enchSelected = this.menu.ench;

        if (this.menu.rebuild) {
            this.enchSelected = null;
            this.enchLevel = 0;
            this.possibleEnchs.clear();
            this.possibleEnchs.putAll(EnchantmentUtils.getPossibleEnch(this.stack));
            this.enchs.clear();
            rebuildWidgets();
            this.menu.rebuild = false;
        }

        if (this.menu.action > -1) {
            switch (this.menu.action) {
                case 0:
                    addEnchSelectedLevel();
                    break;
                case 1:
                    decreaseEnchSelectedLevel();
                    break;
                case 2:
                    addEnch();
                    break;
                case 3:
                    if (this.menu.ench != null)
                        setEnchSelected(this.menu.ench);
            }

            this.menu.action = -1;
        }
    }

    protected void init() {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        this.addRenderableWidget(new ButtonAddEnchLevel(x + 120, y + 65, this));
        this.addRenderableWidget(new ButtonDecreaseEnchLevel(x + 194, y + 65, this));
        this.addRenderableWidget(new ButtonAddEnch(x + 135, y + 65, this));
        this.addRenderableWidget(new ButtonItemEnchant(x + 122, y + 108, this));

        int countP = 0;

        for (Enchantment ench : this.possibleEnchs.keySet()) {
            EnchantmentUtils.Ench ench1 = this.possibleEnchs.get(ench);

            ButtonEnchSelected button = new ButtonEnchSelected(x + 46, y + 22 + 22 * countP + this.scrollY0, y, ench1, this);

            this.addRenderableWidget(button);
            this.possibleWidget.add(button);

            countP++;
        }

        int countE = 0;

        for (Enchantment ench : this.enchs.keySet()) {
            ButtonEnchSelected button = new ButtonEnchSelected(x + 223, y + 22 + 22 * countE + this.scrollY1, y, new EnchantmentUtils.Ench(ench, this.enchs.get(ench), ench.getMaxLevel()), this);

            this.addRenderableWidget(button);
            this.enchWidget.add(button);

            countE++;
        }
    }

    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUNG);

        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        stack.pushPose();

        this.blit(stack, x, y, 0, 0, backgroundWidth, backgroundHeight, 512, 512);

        RenderSystem.setShaderTexture(0, new ResourceLocation(ThirteenFlames.MODID, "textures/gui/gems/scroll_gem_" + this.scrollType + ".png"));

        this.blit(stack, x + 1, y + 72, 0, 0, 10, 21, 10, 21);

        if (!this.enchs.isEmpty())
            this.font.draw(stack, Component.translatable("gui.thirteen_flames.scroll.ench_level_info", expCost), x + 164 - MC.font.width(Component.translatable("gui.thirteen_flames.scroll.ench_level_info", expCost)) / 2, y + 90, 0x673E09);

        stack.popPose();
    }

    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        renderTooltip(stack, mouseX, mouseY);
    }

    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        this.font.draw(stack, this.playerInventoryTitle, x + 84, y + 152, 4210752);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        if (mouseY >= y + 22 && mouseY <= y + 132) {
            if (!this.possibleWidget.isEmpty() && this.possibleWidget.size() > 5 && mouseX >= x + 46 && mouseX <= x + 105) {
                if (amount > 0) {
                    if (this.possibleWidget.get(this.possibleEnchs.size() - 1).y > y + 22 + 22 * 3)
                        this.scrollY0 -= 11;
                } else if (amount < 0) {
                    if (this.possibleWidget.get(0).y < y + 22)
                        this.scrollY0 += 11;
                }
            } else if (!this.enchWidget.isEmpty() && this.enchWidget.size() > 5 && mouseX >= x + 223 && mouseX <= x + 282) {
                if (amount > 0) {
                    if (this.enchWidget.get(this.enchs.size() - 1).y > y + 22 + 22 * 3)
                        this.scrollY1 -= 11;
                } else if (amount < 0) {
                    if (this.enchWidget.get(0).y < y + 22)
                        this.scrollY1 += 11;
                }
            }
        }

        Network.sendToServer(new ScrollMenuOpenPacket(this.menu.scrollStack, -1, this.enchLevel, this.scrollY0, this.scrollY1, this.enchSelected, this.stack, this.enchs, this.possibleEnchs));

        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public void onClose() {
        if (!this.stack.isEmpty())
            Network.sendToServer(new ScrollScreenClosePacket(this.stack));

        super.onClose();
    }

    public void addEnchSelectedLevel() {
        if (this.enchSelected != null) {
            if (this.enchLevel < this.enchSelected.getMaxLevel()) {
                this.enchLevel += 1;

                if (getEnchSelectedInLists() == 0) {
                    this.enchSelected = new EnchantmentUtils.Ench(this.enchSelected.getEnch(), this.enchLevel, this.enchSelected.getMaxLevel());
                    this.possibleEnchs.put(this.enchSelected.getEnch(), this.enchSelected);

                    if (this.enchs.containsKey(this.enchSelected.getEnch()))
                        this.enchs.put(this.enchSelected.getEnch(), this.enchLevel - 1);
                } else {
                    if (this.enchLevel == this.enchSelected.getMaxLevel()) {
                        this.possibleEnchs.remove(this.enchSelected.getEnch());
                        this.enchs.put(this.enchSelected.getEnch(), this.enchSelected.getMaxLevel());
                    } else {
                        EnchantmentUtils.Ench ench = new EnchantmentUtils.Ench(this.enchSelected.getEnch(), this.enchLevel + 1, this.enchSelected.getMaxLevel());
                        this.possibleEnchs.put(ench.getEnch(), ench);
                        this.enchs.put(ench.getEnch(), this.enchLevel);
                    }
                }

                Network.sendToServer(new ScrollMenuOpenPacket(this.menu.scrollStack, -1, this.enchLevel, this.scrollY0, this.scrollY1, this.enchSelected, this.stack, this.enchs, this.possibleEnchs));
            }
        }
    }

    public void decreaseEnchSelectedLevel() {
        if (this.enchSelected != null) {
            if (this.enchLevel >= this.enchSelected.getMinLevel()) {
                this.enchLevel -= 1;

                if (getEnchSelectedInLists() == 0) {
                    if (this.enchLevel == 0) {
                        this.enchLevel = 1;
                        return;
                    }

                    EnchantmentUtils.Ench ench = new EnchantmentUtils.Ench(this.enchSelected.getEnch(), this.enchLevel, this.enchSelected.getMaxLevel());
                    this.possibleEnchs.put(this.enchSelected.getEnch(), ench);

                    if (this.enchs.containsKey(this.enchSelected.getEnch())) {
                        if (this.enchs.get(this.enchSelected.getEnch()) == 1) {
                            this.enchs.remove(this.enchSelected.getEnch());

                            if (this.enchWidget.get(0).y < (this.height - this.backgroundHeight) / 2 + 22)
                                this.scrollY1 += 22;
                        } else {
                            this.enchs.put(this.enchSelected.getEnch(), this.enchLevel - 1);
                        }
                    }

                    this.enchSelected = ench;
                } else {
                    EnchantmentUtils.Ench ench = new EnchantmentUtils.Ench(this.enchSelected.getEnch(), this.enchLevel, this.enchSelected.getMaxLevel());
                    this.possibleEnchs.put(ench.getEnch(), new EnchantmentUtils.Ench(this.enchSelected.getEnch(), this.enchLevel + 1, this.enchSelected.getMaxLevel()));

                    if (this.enchLevel == 0) {
                        this.enchs.remove(ench.getEnch());
                        this.enchSelected = null;
                        this.enchLevel = 0;

                        if (this.enchWidget.get(0).y < (this.height - this.backgroundHeight) / 2 + 22)
                            this.scrollY1 += 22;
                    } else {
                        this.enchs.put(ench.getEnch(), this.enchLevel);
                        this.enchSelected = ench;
                    }
                }

                Network.sendToServer(new ScrollMenuOpenPacket(this.menu.scrollStack, -1, this.enchLevel, this.scrollY0, this.scrollY1, this.enchSelected, this.stack, this.enchs, this.possibleEnchs));
            }
        }
    }

    public void addEnch() {
        if (this.enchSelected != null) {
            if (this.enchLevel >= this.enchSelected.getMaxLevel()) {
                this.possibleEnchs.remove(this.enchSelected.getEnch());
                this.enchs.put(this.enchSelected.getEnch(), this.enchSelected.getMaxLevel());

                if (this.possibleWidget.get(0).y < (this.height - this.backgroundHeight) / 2 + 22)
                    this.scrollY0 += 22;
            } else {
                this.possibleEnchs.put(this.enchSelected.getEnch(), new EnchantmentUtils.Ench(this.enchSelected.getEnch(), this.enchLevel + 1, this.enchSelected.getMaxLevel()));
                this.enchs.put(this.enchSelected.getEnch(), this.enchLevel);
            }

            Network.sendToServer(new ScrollMenuOpenPacket(this.menu.scrollStack, -1, 0, this.scrollY0, this.scrollY1, null, this.stack, this.enchs, this.possibleEnchs));
        }
    }

    public void setEnchSelected(EnchantmentUtils.Ench enchSelected) {
        this.enchSelected = enchSelected;

        Network.sendToServer(new ScrollMenuOpenPacket(this.menu.scrollStack, -1, enchSelected.getMinLevel(), this.scrollY0, this.scrollY1, enchSelected, this.stack, this.enchs, this.possibleEnchs));
    }

    public int getEnchSelectedInLists() {
        if (this.enchSelected == null)
            return -1;

        for (EnchantmentUtils.Ench ench : this.possibleEnchs.values())
            if (equalsEnch(ench))
                return 0;

        return 1;
    }

    public boolean equalsEnch(EnchantmentUtils.Ench ench) {
        if (this.enchSelected == null)
            return false;
        else if (!ench.getEnch().equals(this.enchSelected.getEnch()))
            return false;
        else if (ench.getMinLevel() != this.enchSelected.getMinLevel())
            return false;
        else if (ench.getMaxLevel() != this.enchSelected.getMaxLevel())
            return false;

        return true;
    }

    public EnchantmentUtils.Ench getEnchSelected() {
        return enchSelected;
    }

    public int getEnchLevel() {
        return enchLevel;
    }

    public int getExpCost() {
        return expCost;
    }

    public boolean isPauseScreen() {
        return false;
    }
}
