package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.HcUtilsMod;
import com.htbcraft.hcutilsmod.common.MinecraftColor;
import com.htbcraft.hcutilsmod.config.Config;
import com.htbcraft.hcutilsmod.my.MyKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

@Mod(value = HcUtilsMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HcUtilsMod.MODID, value = Dist.CLIENT)
public class BrightnessClient {
    // https://icon-rainbow.com/
    private static final Identifier LIGHT = Identifier.fromNamespaceAndPath(HcUtilsMod.MODID, "hud/light");

    private static boolean displayBrightness = false;
    private static BlockPos prevPlayerPos = BlockPos.ZERO;
    private static ArrayList<BrightnessMarker> targetMarkers = null;

    // デフォルトキー：[b]
    private static final MyKeyBinding BIND_KEY = new MyKeyBinding(
            Config.KEY_CATEGORY,
            "key.category.minecraft.hcutilsmod.brightness",
            GLFW.GLFW_KEY_B,
            0,
            GLFW.GLFW_RELEASE
    );

    // オーバーワールドにいるときだけ利用可能にする
    private static boolean isOverWorld() {
        Level level = Minecraft.getInstance().level;
        return level != null && level.dimension() == Level.OVERWORLD;
    }

    // 禁止
    private static boolean isProhibit() {
        return Minecraft.getInstance().gui.screen() != null ||
                Minecraft.getInstance().getDebugOverlay().showDebugScreen();
    }

    public BrightnessClient(ModContainer container) {
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BIND_KEY);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (isProhibit()) {
            return;
        }

        int key = event.getKey();
        int modifiers = event.getModifiers();
        int action = event.getAction();

        if (BIND_KEY.test(key, modifiers, action)) {
            if (isOverWorld()) {
                displayBrightness = !displayBrightness;
                HcUtilsMod.LOGGER.info("displayBrightness: {}", displayBrightness);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer) {
            return;
        }
        if (!isOverWorld()) {
            displayBrightness = false;
        }

        if (displayBrightness) {
            BlockPos playerPos = event.getEntity().blockPosition();
            if (prevPlayerPos.compareTo(playerPos) != 0) {
                prevPlayerPos = playerPos;

                new Thread(() ->
                        targetMarkers = makeBrightnessMarkers(
                                event.getEntity().level(),
                                playerPos,
                                Config.BRIGHTNESS_RANGE.get(),
                                Config.BRIGHTNESS_THRESHOLD.get(),
                                Config.BRIGHTNESS_ZOMBIE.get(),
                                Config.BRIGHTNESS_COLOR.get(),
                                Config.BRIGHTNESS_ALPHA.get())
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

    private static ArrayList<BrightnessMarker> makeBrightnessMarkers(
            Level world,
            BlockPos playerPos,
            int range,
            int threshold,
            Boolean zombie,
            MinecraftColor color,
            int alpha) {
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
        if (zombie && !SpawnPlacements.isSpawnPositionOk(EntityTypes.ZOMBIE, world, pos)) {
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
    public static void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
        if (targetMarkers != null) {
            targetMarkers.forEach(marker -> {
                marker.draw(event.getPoseStack());
            });
        }
    }

    @SubscribeEvent
    public static void onRenderGuiLayerPre(RenderGuiLayerEvent.Pre event) {
        // マーカー表示中がわかるように画面の左下にアイコン出す
        if (displayBrightness) {
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
