package com.htbcraft.hcutilsmod.mods.brightness;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.common.MinecraftColor;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BrightnessModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    // https://icon-rainbow.com/
    private static final ResourceLocation LIGHT_ICON = new ResourceLocation(HCUtilsMod.MOD_ID, "textures/gui/light.png");

    private final BrightnessMarkerRenderer brightnessMarkerRenderer;

    private Boolean dispBrightness = false;
    private BlockPos prevPlayerPos = BlockPos.ZERO;
    private ArrayList<BrightnessMarker> tergetMarkers = null;

    // デフォルトキー：[b]
    private static final HCKeyBinding BIND_KEY = new HCKeyBinding(
            "hcutilsmod.brightness.key_description",
            GLFW_KEY_B,
            0,
            GLFW_RELEASE
    );

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BIND_KEY);
    }

    public BrightnessModHandler() {
        brightnessMarkerRenderer = new BrightnessMarkerRenderer(Minecraft.getInstance());
    }

    // オーバーワールドにいるときだけ利用可能にする
    private Boolean isOverWorld() {
        Level level = Minecraft.getInstance().level;
        return (level != null ? level.dimension().location().getPath().compareTo("overworld") : 0) == 0;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (Minecraft.getInstance().screen != null) {
            LOGGER.info("Displaying on screen");
            return;
        }

        int key = event.getKey();
        int modifiers = event.getModifiers();
        int action = event.getAction();

        if (BIND_KEY.test(key, modifiers, action)) {
            if (isOverWorld()) {
                dispBrightness = !dispBrightness;
                LOGGER.info("dispBrightness: " + dispBrightness);
            }
        }
    }

    @SubscribeEvent
    public void onScreenClosing(ScreenEvent.Closing event) {
        // 設定変更されたら更新
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public void onBlock(BlockEvent event) {
        // ブロックに変化があったら更新
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) {
            return;
        }

        if (!isOverWorld()) {
            dispBrightness = false;
        }

        if (dispBrightness) {
            BlockPos playerPos = event.player.blockPosition();
            if (prevPlayerPos.compareTo(playerPos) != 0) {
                prevPlayerPos = playerPos;

                new Thread(() ->
                    tergetMarkers = makeBrightnessMarkers(
                                        event.player.level(),
                                        playerPos,
                                        HCSettings.getInstance().rangeBrightness,
                                        HCSettings.getInstance().thresholdBrightness,
                                        HCSettings.getInstance().zombieBrightness,
                                        HCSettings.getInstance().colorBrightness,
                                        HCSettings.getInstance().alphaBrightness)
                ).start();
            }
        }
        else {
            if (tergetMarkers != null) {
                tergetMarkers = null;
                prevPlayerPos = BlockPos.ZERO;
            }
        }
    }

    private ArrayList<BrightnessMarker> makeBrightnessMarkers(Level world, BlockPos playerPos, int range, int threshold, Boolean zombie, MinecraftColor color, int alpha) {
        ArrayList<BrightnessMarker> makeMarkers = new ArrayList<>();

        int i = playerPos.getX() - range;
        int j = playerPos.getX() + range;
        int k = playerPos.getY() - range;
        int l = playerPos.getY() + range;
        int i1 = playerPos.getZ() - range;
        int j1 = playerPos.getZ() + range;

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mutableBlockPosY1 = new BlockPos.MutableBlockPos();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    mutableBlockPos.set(k1, l1, i2);
                    mutableBlockPosY1.set(k1, l1 - 1, i2);
                    if (checkBrightness(world, mutableBlockPos, mutableBlockPosY1, threshold, zombie) != -1) {
                        makeMarkers.add(
                                new BrightnessMarker(
                                        color,
                                        alpha,
                                        mutableBlockPos.immutable()));
                    }
                }
            }
        }

        if (makeMarkers.size() == 0) {
            return null;
        }

        return makeMarkers;
    }

    private int checkBrightness(Level world, BlockPos pos, BlockPos posY1, int threshold, Boolean zombie) {
        // ゾンビが湧くことができないブロックは除外する
        if (zombie && !SpawnPlacements.isSpawnPositionOk(EntityType.ZOMBIE, world, pos)) {
            return -1;
        }

        BlockState state = world.getBlockState(pos);
        BlockState stateY1 = world.getBlockState(posY1);

        int brightness = world.getBrightness(LightLayer.BLOCK, pos);

        if ((threshold >= brightness) && state.isAir() && !stateY1.isAir()) {
            return brightness;
        }

        return -1;
    }

    @SubscribeEvent
    public void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }

        if (tergetMarkers != null) {
//            tergetMarkers.forEach(
//                    m -> m.draw(brightnessMarkerRenderer.update(event.getPoseStack())));
        }
    }

//    @SubscribeEvent
//    public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
//        if (event.getOverlay().id() != VanillaGuiOverlay.AIR_LEVEL.id()) {
//            return;
//        }
//
//        // マーカー表示中がわかるように画面の左下にアイコン出す
//        if (dispBrightness) {
//            int x = 1;
//            int y = event.getWindow().getGuiScaledHeight() - 20 - 1;
//
//            RenderSystem.enableBlend();
//            event.getGuiGraphics().blit(LIGHT_ICON,
//                    x,
//                    y,
//                    0,
//                    0,
//                    0,
//                    20,
//                    20,
//                    20,
//                    20);
//            RenderSystem.disableBlend();
//        }
//    }
}
