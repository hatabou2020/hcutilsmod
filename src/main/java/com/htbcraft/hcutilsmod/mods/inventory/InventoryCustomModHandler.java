package com.htbcraft.hcutilsmod.mods.inventory;

import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

// 仕様
// ・インベントリ、チェストを開いているときに登録キー押下で整理することができる
// ・手持ちのアイテムを使い切ったら、インベントリ内から自動で取り出す
public class InventoryCustomModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String INVENTORY_BUTTON_TEXT = "hcutilsmod.inventory.button";

    private boolean sortInventory = false;
    private boolean sortEnable = false;
    private Button inventorySortButton = null;

    // デフォルトキー：[ｏ]
    private static final HCKeyBinding BIND_KEY = new HCKeyBinding(
            "hcutilsmod.inventory.key_description",
            GLFW_KEY_O,
            0,
            GLFW_RELEASE
    );

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BIND_KEY);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if ((Minecraft.getInstance().screen != null) &&
            !(Minecraft.getInstance().screen instanceof InventoryScreen) &&
            !(Minecraft.getInstance().screen instanceof ContainerScreen)) {
            LOGGER.info("Displaying on screen");
            return;
        }

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
    public void onMouseReleased(ScreenEvent.MouseButtonReleased event) {
        if (sortEnable) {
            if (event.getButton() == GLFW_MOUSE_BUTTON_LEFT) {
                inventorySortButton.mouseClicked(event.getMouseX(), event.getMouseY(), 0);
            }
        }
    }

    @SubscribeEvent
    public void onScreenOpening(ScreenEvent.Opening event) {
        sortEnable = false;

        Screen gui = event.getScreen();
        LOGGER.info(gui.getTitle().getString());

        Component keyName = Component.translatable(INVENTORY_BUTTON_TEXT, BIND_KEY.getKeyName());
        int width = Minecraft.getInstance().font.width(keyName.getString());

        if ((gui instanceof InventoryScreen) || (gui instanceof ContainerScreen)) {
            sortEnable = true;
            inventorySortButton = Button.builder(keyName,
                                                (var1) -> {
                                                    sortInventory = true;
                                                    inventorySortButton.active = false;
                                                })
                                            .pos(0, 0)
                                            .size(width + 10, 20)
                                            .build();
        }
    }

    @SubscribeEvent
    public void onScreenClosing(ScreenEvent.Closing event) {
        sortEnable = false;
        inventorySortButton = null;
    }

    @SubscribeEvent
    public void onDrawScreen(ScreenEvent.Render event) {
        if (sortEnable) {
            Screen gui = event.getScreen();
            double mouseX = event.getMouseX();
            double mouseY = event.getMouseY();

            inventorySortButton.setX(gui.width - inventorySortButton.getWidth());
            inventorySortButton.setY(0);
            inventorySortButton.render(event.getPoseStack(), (int) mouseX, (int) mouseY, 0.0F);

            if (!sortInventory) {
                inventorySortButton.active = true;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            if (sortEnable) {
                // サーバー側で処理しないとソート結果は反映されない
                if (sortInventory) {
                    sortInventory = false;

                    // インベントリのソート
                    sortPlayerInventory(event.player.getInventory().items);

                    if (event.player.containerMenu instanceof ChestMenu) {
                        // チェストのソート
                        sortChestInventory((ChestMenu) event.player.containerMenu);
                    }
                }
            }

            if (destroyItemParam != null) {
                // 使い切ったアイテムを補充する
                autoReplaceItem(event.player.getInventory());
            }
        }
    }

    private void sortPlayerInventory(NonNullList<ItemStack> inInventory) {
        // ホットバーを除いたインベントリ
        int hotbarSize = Inventory.getSelectionSize();
        int inventorySize = inInventory.size();
        List<ItemStack> itemStacks = inInventory.subList(hotbarSize, inventorySize);

        if (HCSettings.getInstance().sortType == HCSettings.SortType.CATEGORY) {
            // カテゴリ順でソート
            itemStacks.sort(new InventoryCategorySort());
        }
        else {
            // 名前順でソート
            itemStacks.sort(new InventoryNameSort());
        }
    }

    private void sortChestInventory(ChestMenu container) {
        // チェスト以外を除いたインベントリ
        Container inventory = container.getContainer();
        int size = inventory.getContainerSize();
        List<ItemStack> itemStacks = container.getItems().subList(0, size);

        if (HCSettings.getInstance().sortType == HCSettings.SortType.CATEGORY) {
            // カテゴリ順でソート
            itemStacks.sort(new InventoryCategorySort());
        }
        else {
            // 名前順でソート
            itemStacks.sort(new InventoryNameSort());
        }

        // ソートした結果をスロットに戻す
        for (int i = 0; i < size; i++) {
            inventory.setItem(i, itemStacks.get(i));
        }
    }

    private record DestroyItemParam(InteractionHand hand, ItemStack original) {
        public InteractionHand getHand() {
            return this.hand;
        }

        public ItemStack getOriginalItem() {
            return this.original;
        }
    }

    private DestroyItemParam destroyItemParam = null;

    private void autoReplaceItem(Inventory inventory) {
        for (int i = 0; i < inventory.items.size(); i++) {
            ItemStack itemStack = inventory.items.get(i);
            if (itemStack.isEmpty()) {
                continue;   // AIRははじく
            }

            // 手に持っていたアイテムと同じものがインベントリにあれば取り出す
            if (destroyItemParam.getOriginalItem().sameItem(itemStack)) {
                if (destroyItemParam.getHand().equals(InteractionHand.MAIN_HAND)) {
                    inventory.items.set(inventory.selected, itemStack);
                }
                else {
                    inventory.offhand.set(0, itemStack);
                }
                inventory.items.set(i, ItemStack.EMPTY);
                break;
            }
        }

        destroyItemParam = null;
    }

    @SubscribeEvent
    public void onPlayerDestroyItem(PlayerDestroyItemEvent event) {
        if (!HCSettings.getInstance().enableAutoReplaceItem) {
            return;
        }

        if (event.getOriginal().is(Items.BUCKET)) {
            // 牛乳もこのイベントがくるので捨てる
            return;
        }

        destroyItemParam = new DestroyItemParam(event.getHand(), event.getOriginal());
        LOGGER.info("PlayerDestroyItemEvent: " + event.getHand() + " / " + event.getOriginal());
    }
}
