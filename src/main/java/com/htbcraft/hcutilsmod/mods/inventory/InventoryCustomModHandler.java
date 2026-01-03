package com.htbcraft.hcutilsmod.mods.inventory;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

// 仕様
// ・インベントリ、チェストを開いているときに登録キー押下で整理することができる
// ・手持ちのアイテムを使い切ったら、インベントリ内から自動で取り出す
@Mod(value = HCUtilsMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HCUtilsMod.MODID, value = Dist.CLIENT)
public class InventoryCustomModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String INVENTORY_BUTTON_TEXT = "hcutilsmod.inventory.button";

    private static boolean sortInventory = false;
    private static boolean sortEnable = false;
    private static Button inventorySortButton = null;

    // デフォルトキー：[ｏ]
    private static final HCKeyBinding BIND_KEY = new HCKeyBinding(
            "key.category.minecraft.hcutilsmod.inventory",
            GLFW_KEY_O,
            0,
            GLFW_RELEASE
    );

    public InventoryCustomModHandler(ModContainer container) {
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BIND_KEY);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
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
    public static void onScreenMouseButtonReleasedPost(ScreenEvent.MouseButtonReleased.Post event) {
        if (sortEnable) {
            if (event.getButton() == GLFW_MOUSE_BUTTON_LEFT) {
                inventorySortButton.mouseClicked(event.getMouseButtonEvent(), false);
            }
        }
    }

    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {
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
    public static void onScreenClosing(ScreenEvent.Closing event) {
        sortEnable = false;
        inventorySortButton = null;
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        if (sortEnable) {
            Screen gui = event.getScreen();
            double mouseX = event.getMouseX();
            double mouseY = event.getMouseY();

            inventorySortButton.setX(gui.width - inventorySortButton.getWidth());
            inventorySortButton.setY(0);
            inventorySortButton.render(event.getGuiGraphics(), (int) mouseX, (int) mouseY, 0.0F);

            if (!sortInventory) {
                inventorySortButton.active = true;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer) {
            if (sortEnable) {
                // サーバー側で処理しないとソート結果は反映されない
                if (sortInventory) {
                    sortInventory = false;

                    // インベントリのソート
                    sortPlayerInventory(event.getEntity().getInventory().getNonEquipmentItems());

                    if (event.getEntity().containerMenu instanceof ChestMenu) {
                        // チェストのソート
                        sortChestInventory((ChestMenu) event.getEntity().containerMenu);
                    }
                }
            }

            if (destroyItemParam != null) {
                // 使い切ったアイテムを補充する
                autoReplaceItem(event.getEntity());
            }
        }
    }

    private static void sortPlayerInventory(NonNullList<ItemStack> inInventory) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            LOGGER.info("player == null");
            return;
        }

        // ホットバーを除いたインベントリ
        int hotbarSize = Inventory.getSelectionSize();
        int inventorySize = inInventory.size();
        List<ItemStack> itemStacks = inInventory.subList(hotbarSize, inventorySize);

        if (HCSettings.getInstance().sortType == HCSettings.SortType.CATEGORY) {
            try (Level level = player.level()) {
                // クリエイティブモードのタブをロード
                CreativeModeTabs.tryRebuildTabContents(
                        player.connection.enabledFeatures(),
                        player.canUseGameMasterBlocks(),
                        level.registryAccess()
                );

                // カテゴリ順でソート
                itemStacks.sort(new InventoryCategorySort());
            } catch (IOException e) {
                LOGGER.info("catch IOException");
            }
        }
        else {
            // 名前順でソート
            itemStacks.sort(new InventoryNameSort());
        }
    }

    private static void sortChestInventory(ChestMenu container) {
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

    private static DestroyItemParam destroyItemParam = null;

    private static void autoReplaceItem(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack.isEmpty()) {
                continue;   // AIRははじく
            }

            // 手に持っていたアイテムと同じものがインベントリにあれば取り出す
            if (destroyItemParam.getOriginalItem().is(itemStack.getItem())) {
                if (destroyItemParam.getHand().equals(InteractionHand.MAIN_HAND)) {
                    player.getInventory().setSelectedItem(itemStack);
                }
                else {
                    player.setItemInHand(InteractionHand.OFF_HAND, itemStack);
                }
                player.getInventory().setItem(i, ItemStack.EMPTY);
                break;
            }
        }

        destroyItemParam = null;
    }

    @SubscribeEvent
    public static void onPlayerDestroyItem(PlayerDestroyItemEvent event) {
        if (!HCSettings.getInstance().enableAutoReplaceItem) {
            return;
        }

        if (event.getOriginal().is(Items.BUCKET)) {
            // 牛乳もこのイベントがくるので捨てる
            return;
        }

        destroyItemParam = new DestroyItemParam(event.getHand(), event.getOriginal());
        LOGGER.info("PlayerDestroyItemEvent: {} / {}", event.getHand(), event.getOriginal());
    }
}
