package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.common.MinecraftColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

//@Mod(value = HCUtilsMod.MODID, dist = Dist.CLIENT)
//@EventBusSubscriber(modid = HCUtilsMod.MODID, value = Dist.CLIENT)
public class BrightnessModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    // https://icon-rainbow.com/
    private static final ResourceLocation LIGHT = ResourceLocation.fromNamespaceAndPath(HCUtilsMod.MODID, "hud/light");

    private static final BrightnessMarkerRenderer brightnessMarkerRenderer = new BrightnessMarkerRenderer();

    private static Boolean dispBrightness = false;
    private static BlockPos prevPlayerPos = BlockPos.ZERO;
    private static ArrayList<BrightnessMarker> targetMarkers = null;

    // デフォルトキー：[b]
    private static final HCKeyBinding BIND_KEY = new HCKeyBinding(
            "key.category.minecraft.hcutilsmod.brightness",
            GLFW_KEY_B,
            0,
            GLFW_RELEASE
    );

    // オーバーワールドにいるときだけ利用可能にする
    private static Boolean isOverWorld() {
        Level level = Minecraft.getInstance().level;
        return (level != null ? level.dimension().location().getPath().compareTo("overworld") : 0) == 0;
    }

    public BrightnessModHandler(ModContainer container) {
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BIND_KEY);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
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
                LOGGER.info("dispBrightness: {}", dispBrightness);
            }
        }
    }

    @SubscribeEvent
    public static void onScreenClosing(ScreenEvent.Closing event) {
        // 設定変更されたら更新
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public static void onBlockBlockToolModification(BlockEvent.BlockToolModificationEvent event) {
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public static void onBlockPortalSpawn(BlockEvent.PortalSpawnEvent event) {
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public static void onBlockFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public static void onBlockFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public static void onBlockNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public static void onBlockEntityMultiPlace(BlockEvent.EntityMultiPlaceEvent event) {
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public static void onBlockEntityPlace(BlockEvent.EntityPlaceEvent event) {
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer) {
            return;
        }
        if (!isOverWorld()) {
            dispBrightness = false;
        }

        if (dispBrightness) {
            BlockPos playerPos = event.getEntity().blockPosition();
            if (prevPlayerPos.compareTo(playerPos) != 0) {
                prevPlayerPos = playerPos;

                new Thread(() ->
                    targetMarkers = makeBrightnessMarkers(
                                        event.getEntity().level(),
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
            if (targetMarkers != null) {
                targetMarkers = null;
                prevPlayerPos = BlockPos.ZERO;
            }
        }
    }

    private static ArrayList<BrightnessMarker> makeBrightnessMarkers(Level world, BlockPos playerPos, int range, int threshold, Boolean zombie, MinecraftColor color, int alpha) {
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

        if (makeMarkers.isEmpty()) {
            return null;
        }

        return makeMarkers;
    }

    private static int checkBrightness(Level world, BlockPos pos, BlockPos posY1, int threshold, Boolean zombie) {
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
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterLevel event) {
        if (targetMarkers != null) {
            targetMarkers.forEach(
                    m -> m.draw(brightnessMarkerRenderer.update(event.getPoseStack(), event.getModelViewMatrix())));
        }
    }

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        // マーカー表示中がわかるように画面の左下にアイコン出す
        if (dispBrightness) {
            int x = 1;
            int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() - 20 - 1;

            event.getGuiGraphics().blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    LIGHT,
                    x,
                    y,
                    20,
                    20);
        }
    }
}
