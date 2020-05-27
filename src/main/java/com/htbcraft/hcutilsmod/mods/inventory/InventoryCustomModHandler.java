package com.htbcraft.hcutilsmod.mods.inventory;

import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;

// 仕様
// ・インベントリ、チェストを開いているときに登録キー押下で整理することができる
// ・手持ちのアイテムを使い切ったら、インベントリ内から自動で取り出す
public class InventoryCustomModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String INVENTORY_BUTTON_TEXT = "hcutilsmod.inventory.button";

    private boolean sortInventory = false;
    private boolean sortEnable = false;
    private InventorySortButton inventorySortButton = null;
    private ItemStack mainHandItem = ItemStack.EMPTY;
    private ItemStack offHandItem = ItemStack.EMPTY;

    // デフォルトキー：[ｏ]
    private static final HCKeyBinding BIND_KEY = new HCKeyBinding(
            "hcutilsmod.inventory.key_description",
            GLFW_KEY_O,
            0,
            GLFW_RELEASE
    );

    static {
        ClientRegistry.registerKeyBinding(BIND_KEY);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (sortEnable) {
            int key = event.getKey();
            int modifiers = event.getModifiers();
            int action = event.getAction();

            sortInventory = BIND_KEY.test(key, modifiers, action);

            if (sortInventory) {
                inventorySortButton.active = false;
            }
        }
    }

    @SubscribeEvent
    public void onMouseReleased(GuiScreenEvent.MouseReleasedEvent event) {
        if (sortEnable) {
            if (event.getButton() == GLFW_MOUSE_BUTTON_LEFT) {
                inventorySortButton.mouseClicked(event.getMouseX(), event.getMouseY(), 0);
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        sortEnable = false;

        Screen gui = event.getGui();
        if (gui == null) {
            LOGGER.info("gui == null");
            inventorySortButton = null;
            return;
        }

        LOGGER.info(gui.getTitle().getString());

        String textKeyName = I18n.format(INVENTORY_BUTTON_TEXT, BIND_KEY.getKeyName());
        int width = Minecraft.getInstance().fontRenderer.getStringWidth(textKeyName);

        if ((gui instanceof InventoryScreen) || (gui instanceof ChestScreen)) {
            sortEnable = true;
            inventorySortButton = new InventorySortButton(0, 0, width + 10, 20, textKeyName);
        }
    }

    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent event) {
        if (sortEnable) {
            Screen gui = event.getGui();
            if (gui == null) {
                LOGGER.info("gui == null");
                return;
            }

            double mouseX = event.getMouseX();
            double mouseY = event.getMouseY();

            inventorySortButton.x = gui.width - inventorySortButton.getWidth();
            inventorySortButton.y = 0;
            inventorySortButton.render((int) mouseX, (int) mouseY, 0.0F);

            if (!sortInventory) {
                inventorySortButton.active = true;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (sortEnable) {
            if (event.side.isServer()) {
                // サーバー側で処理しないとソート結果は反映されない
                if (sortInventory) {
                    sortInventory = false;

                    // インベントリのソート
                    sortPlayerInventory(event.player.inventory.mainInventory);

                    if (event.player.openContainer instanceof ChestContainer) {
                        // チェストのソート
                        sortChestInventory((ChestContainer) event.player.openContainer);
                    }
                }
            }
        }
    }

    private void sortPlayerInventory(NonNullList<ItemStack> inInventory) {
        // ホットバーを除いたインベントリ
        int hotbarSize = PlayerInventory.getHotbarSize();
        int inventorySize = inInventory.size();
        List<ItemStack> itemStacks = inInventory.subList(hotbarSize, inventorySize);

        // 名前順でソート
        itemStacks.sort(new InventoryNameSort());
    }

    private void sortChestInventory(ChestContainer container) {
        // チェスト以外を除いたインベントリ
        IInventory inventory = container.getLowerChestInventory();
        int size = inventory.getSizeInventory();
        List<ItemStack> itemStacks = container.getInventory().subList(0, size);

        // 名前順でソート
        itemStacks.sort(new InventoryNameSort());

        // ソートした結果をスロットに戻す
        for (int i = 0; i < size; i++) {
            inventory.setInventorySlotContents(i, itemStacks.get(i));
        }
    }

    class InventorySortButton extends AbstractButton {
        public InventorySortButton(int x, int y, int width, int height, String msg) {
            super(x, y, width, height, msg);
        }

        public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
            super.renderButton(p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
        }

        public void onPress() {
            InventoryCustomModHandler.this.sortInventory = true;
            InventoryCustomModHandler.this.inventorySortButton.active = false;
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getSide().isServer()) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getCount() <= 1) {
                if (Hand.MAIN_HAND.equals(event.getHand())) {
                    mainHandItem = itemStack.copy();
                } else {
                    offHandItem = itemStack.copy();
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDestroyItem(PlayerDestroyItemEvent event) {
        PlayerInventory inventory = event.getPlayer().inventory;
        Hand hand = Objects.requireNonNull(event.getHand());

        // event.getOriginal()のバグで1.14.4まではnullになってしまうため
        // 右クリックイベントで取っておいたItemStackを使う。
        // event.getOriginal()のバグは1.15.2-31.1.73で修正されている。
        ItemStack original;
        if (Hand.MAIN_HAND.equals(hand)) {
            original = mainHandItem;
            mainHandItem = ItemStack.EMPTY;
        } else {
            original = offHandItem;
            offHandItem = ItemStack.EMPTY;
        }

        LOGGER.info("PlayerDestroyItemEvent: " + original.toString());

        for (int i = 0; i < inventory.mainInventory.size(); i++) {
            ItemStack itemStack = inventory.mainInventory.get(i);
            if (itemStack.isEmpty()) {
                continue;   // AIRははじく
            }

            // 手に持っていたアイテムと同じものがインベントリにあれば取り出す
            if (original.isItemEqual(itemStack)) {
                if (Hand.MAIN_HAND.equals(hand)) {
                    inventory.mainInventory.set(inventory.currentItem, itemStack);
                }
                else {
                    inventory.offHandInventory.set(0, itemStack);
                }
                inventory.mainInventory.set(i, ItemStack.EMPTY);
                break;
            }
        }
    }
}
