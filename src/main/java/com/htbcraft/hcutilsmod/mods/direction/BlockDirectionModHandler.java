package com.htbcraft.hcutilsmod.mods.direction;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.glfw.GLFW.*;

public class BlockDirectionModHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int STATE_INFO_FLAGS = (1 | 2);

    // https://icon-rainbow.com/
    private static final ResourceLocation DIRECT_UP = new ResourceLocation(HCUtilsMod.MOD_ID, "hud/direct_up");
    private static final ResourceLocation DIRECT_DOWN = new ResourceLocation(HCUtilsMod.MOD_ID, "hud/direct_down");
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

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BIND_KEY);
    }

    @SubscribeEvent
    public void onScreenOpening(ScreenEvent.Opening event) {
        guiOpened = true;
    }

    @SubscribeEvent
    public void onScreenClosing(ScreenEvent.Closing event) {
        guiOpened = false;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (Minecraft.getInstance().screen != null) {
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
    public void onMouseInput(InputEvent.MouseButton event) {
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
            if (event.getSide().isServer() && event.getHand().equals(InteractionHand.MAIN_HAND)) {
                // ブロックの方向を変える
                boolean ret = change(event.getLevel(), event.getPos());
                LOGGER.info("change: " + ret);
            }

            // ここでキャンセルしておけばちらつかない
            event.setCanceled(true);
        }
    }

    private boolean change(Level world, BlockPos pos) {
        boolean ret = false;
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        LOGGER.info(block);

        if (block instanceof StairBlock) {                  // 階段
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
        else if (block instanceof PistonBaseBlock) {        // ピストン
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
            ret = world.setBlock(pos, blockState, STATE_INFO_FLAGS);
        }

        return ret;
    }

    @SubscribeEvent
    public void onCustomizeGuiOverlay(CustomizeGuiOverlayEvent event) {
        if (directMode) {
            int width = event.getWindow().getGuiScaledWidth();
            int height = event.getWindow().getGuiScaledHeight();
            int x = (width - DIRECT_ICON_SIZE) / 2;
            int y = (height - DIRECT_ICON_SIZE) / 2;

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );

            // 十字カーソルを指アイコンに切り替え
            event.getGuiGraphics().blitSprite(
                    mouseClick ? DIRECT_DOWN : DIRECT_UP,
                    x,
                    y + 12,
                    DIRECT_ICON_SIZE,
                    DIRECT_ICON_SIZE);

            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }
}
