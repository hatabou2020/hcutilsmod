package com.htbcraft.hcutilsmod.mods.direction;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.glfw.GLFW.*;

public class BlockDirectionModHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int STATE_INFO_FLAGS = (1 | 2);

    // https://icon-rainbow.com/
    private static final ResourceLocation DIRECT_UP_ICON = new ResourceLocation(HCUtilsMod.MOD_ID, "textures/gui/direct_up_icon.png");
    private static final ResourceLocation DIRECT_DOWN_ICON = new ResourceLocation(HCUtilsMod.MOD_ID, "textures/gui/direct_down_icon.png");
    private static final int DIRECT_ICON_SIZE = 24;

    private boolean directMode = false;
    private boolean guiOpened = false;
    private boolean mouseClick = false;

    // デフォルトキー：[r] 長押し
    private static final HCKeyBinding BIND_KEY = new HCKeyBinding(
            "hcutilsmod.direction.key_description",
            GLFW_KEY_R,
            0,
            GLFW_REPEAT
    );

    static {
        ClientRegistry.registerKeyBinding(BIND_KEY);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        guiOpened = (event.getGui() != null);
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (Minecraft.getInstance().currentScreen != null) {
            LOGGER.info("Displaying on screen");
            return;
        }

        if (!guiOpened) {
            int key = event.getKey();
            int modifiers = event.getModifiers();
            int action = event.getAction();

            // キー押下中
            directMode = BIND_KEY.test(key, modifiers, action);
        }
    }

    @SubscribeEvent
    public void onMouseInput(MouseInputEvent event) {
        if (directMode) {
            if (event.getButton() == GLFW_MOUSE_BUTTON_RIGHT) {
                mouseClick = (event.getAction() == GLFW_PRESS);
            }
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (directMode) {
            // このイベントはクライアント・サーバー、メインハンド・オフハンドの組み合わせで受信する
            // サーバーでメインハンドだけを処理する
            if (event.getSide().isServer() && event.getHand().equals(Hand.MAIN_HAND)) {
                // ブロックの方向を変える
                boolean ret = change(event.getWorld(), event.getPos());
                LOGGER.info("change: " + ret);
            }

            // ここでキャンセルしておけばちらつかない
            event.setCanceled(true);
        }
    }

    private boolean change(World world, BlockPos pos) {
        boolean ret = false;
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        LOGGER.info(block);

        if (block instanceof StairsBlock) {                 // 階段
            blockState = new StairsDirectionAdapter(blockState).change();
        }
        else if (block instanceof RotatedPillarBlock) {     // 原木・干草の俵
            blockState = new LogDirectionAdapter(blockState).change();
        }
        else if (block instanceof SlabBlock) {              // ハーフブロック
            blockState = new SlabDirectionAdapter(blockState).change();
        }
        else if (block instanceof HopperBlock) {            // ホッパー
            blockState = new HopperDirectionAdapter(blockState).change();
        }
        else if (block instanceof PistonBlock) {            // ピストン
            blockState = new PistonDirectionAdapter(blockState).change();
        }
        else if (block instanceof ChestBlock) {             // チェスト・トラップチェスト
            blockState = new ChestDirectionAdapter(blockState).change();
        }
        else if (block instanceof BeehiveBlock) {           // ミツバチの巣・養蜂箱
            blockState = new HorizontalDirectionAdapter(blockState).change();
        }
        else if ((block instanceof StandingSignBlock) ||    // 看板
                (block instanceof BannerBlock) ||           // 旗
                (block instanceof SkullBlock)) {            // スケルトンの頭蓋骨・ウィザースケルトンの頭蓋骨・プレイヤーの頭・ゾンビの頭・クリーパーの頭・ドラゴンの頭
            blockState = new Rotation16DirectionAdapter(blockState).change();
        }
        else if ((block instanceof DoorBlock) ||            // ドア
                (block instanceof TrapDoorBlock) ||         // トラップドア
                (block instanceof RailBlock) ||             // レール
                (block instanceof PoweredRailBlock) ||      // パワードレール・アクティベーターレール
                (block instanceof DetectorRailBlock) ||     // ディテクターレール
                (block instanceof RepeaterBlock) ||         // リピーター
                (block instanceof ComparatorBlock) ||       // コンパレーター
                (block instanceof EnderChestBlock) ||       // エンダーチェスト
                (block instanceof GlazedTerracottaBlock) || // 彩釉テラコッタ
                (block instanceof CarvedPumpkinBlock) ||    // くり抜きカボチャ・ジャック・オ・ランタン
                (block instanceof AnvilBlock) ||            // 金床
                (block instanceof FurnaceBlock) ||          // かまど
                (block instanceof BlastFurnaceBlock) ||     // 溶鉱炉
                (block instanceof LoomBlock) ||             // 機織り機
                (block instanceof SmokerBlock) ||           // 燻製器
                (block instanceof GrindstoneBlock) ||       // 砥石
                (block instanceof StonecutterBlock) ||      // 石切台
                (block instanceof CampfireBlock) ||         // 焚き火
                (block instanceof LecternBlock)) {          // 書見台
            blockState = new Rotation4DirectionAdapter(blockState).change();
        }
        else if ((block instanceof DispenserBlock) ||       // ディスペンサー・ドロッパー
                (block instanceof ObserverBlock) ||         // オブザーバー
                (block instanceof ShulkerBoxBlock) ||       // シュルカーボックス
                (block instanceof BarrelBlock) ||           // 樽
                (block instanceof EndRodBlock)) {           // エンドロッド
            blockState = new ComDirectionAdapter(blockState).change();
        }
        else {
            blockState = null;
        }

        if (blockState != null) {
            // Flag 1 will cause a block update.
            // Flag 2 will send the change to clients.
            // Flag 4 will prevent the block from being re-rendered, if this is a client world.
            // Flag 8 will force any re-renders to run on the main thread instead of the worker pool, if this is a client world and flag 4 is clear.
            // Flag 16 will prevent observers from seeing this change.
            // Flags can be OR-ed
            ret = world.setBlockState(pos, blockState, STATE_INFO_FLAGS);
        }

        return ret;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (directMode) {
            // 十字カーソルを指アイコンに切り替え
            if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
                int width = event.getWindow().getScaledWidth();
                int height = event.getWindow().getScaledHeight();
                int x = (width - DIRECT_ICON_SIZE) / 2;
                int y = (height - DIRECT_ICON_SIZE) / 2;

                Minecraft mc = Minecraft.getInstance();

                if (mouseClick) {
                    mc.getTextureManager().bindTexture(DIRECT_DOWN_ICON);
                }
                else {
                    mc.getTextureManager().bindTexture(DIRECT_UP_ICON);
                }

                RenderSystem.enableBlend();
                AbstractGui.blit(event.getMatrixStack(),
                        x,
                        y + 12,
                        0,
                        0,
                        0,
                        DIRECT_ICON_SIZE,
                        DIRECT_ICON_SIZE,
                        DIRECT_ICON_SIZE,
                        DIRECT_ICON_SIZE);
                RenderSystem.disableBlend();

                // 右クリック指示テキスト
//                mc.fontRenderer.func_238421_b_(event.getMatrixStack(), I18n.format("hcutilsmod.direction.icon"), (float)x + DIRECT_ICON_SIZE, (float)y + DIRECT_ICON_SIZE, 14737632);

                event.setCanceled(true);
            }
        }
    }
}
