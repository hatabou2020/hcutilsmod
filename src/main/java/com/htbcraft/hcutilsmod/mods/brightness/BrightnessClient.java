package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.HcUtilsMod;
import com.htbcraft.hcutilsmod.common.MinecraftColor;
import com.htbcraft.hcutilsmod.config.Config;
import com.htbcraft.hcutilsmod.my.MyKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
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
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Mod(value = HcUtilsMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HcUtilsMod.MODID, value = Dist.CLIENT)
public class BrightnessClient {
    // https://icon-rainbow.com/
    private static final Identifier LIGHT = Identifier.fromNamespaceAndPath(HcUtilsMod.MODID, "hud/light");

    private static boolean displayBrightness = false;
    private static BrightnessScan brightnessScan;
    private static List<BrightnessMarker> targetMarkers = List.of();
    // 1 tickあたりの走査量を制限し、フレーム落ちを抑える
    private static final int SCAN_BLOCKS_PER_TICK = 4096;

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
        Minecraft minecraft = Minecraft.getInstance();
        if (event.getEntity() != minecraft.player) {
            return;
        }
        if (!isOverWorld()) {
            displayBrightness = false;
        }

        if (!displayBrightness) {
            brightnessScan = null;
            targetMarkers = List.of();
            return;
        }

        Level world = event.getEntity().level();
        BlockPos playerPos = event.getEntity().blockPosition().immutable();
        int range = Config.BRIGHTNESS_RANGE.get();
        int threshold = Config.BRIGHTNESS_THRESHOLD.get();
        boolean zombie = Config.BRIGHTNESS_ZOMBIE.get();
        MinecraftColor color = Config.BRIGHTNESS_COLOR.get();
        int alpha = Config.BRIGHTNESS_ALPHA.get();

        // 完了後も再走査し、プレイヤー位置やブロック状態の変化を反映する
        if (brightnessScan == null || brightnessScan.isComplete() || !brightnessScan.matches(
                world, range, threshold, zombie, color, alpha)) {
            brightnessScan = new BrightnessScan(
                    world, playerPos, range, threshold, zombie, color, alpha);
        }

        if (!brightnessScan.isComplete() && brightnessScan.advance()) {
            targetMarkers = List.copyOf(brightnessScan.markers);
        }
    }

    @SubscribeEvent
    public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        // 範囲内のブロック更新があれば、次の tick から走査し直す
        if (displayBrightness
                && event.getLevel() == Minecraft.getInstance().level
                && brightnessScan != null
                && brightnessScan.containsAffectedPosition(event.getPos())) {
            brightnessScan = null;
        }
    }

    private static int checkBrightness(Level world, BlockPos pos, BlockPos posY1, int threshold, boolean zombie) {
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
                marker.draw(
                        event.getPoseStack(),
                        event.getSubmitNodeCollector(),
                        event.getLevelRenderState().cameraRenderState.pos);
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

    private static final class BrightnessScan {
        private final Level world;
        private final BlockPos playerPos;
        private final int range;
        private final int threshold;
        private final boolean zombie;
        private final MinecraftColor color;
        private final int alpha;
        private final int width;
        private final int totalBlocks;
        private final ArrayList<BrightnessMarker> markers = new ArrayList<>();
        private final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        private final BlockPos.MutableBlockPos mutableBlockPosY1 = new BlockPos.MutableBlockPos();
        private int nextBlock;

        private BrightnessScan(
                Level world,
                BlockPos playerPos,
                int range,
                int threshold,
                boolean zombie,
                MinecraftColor color,
                int alpha) {
            this.world = world;
            this.playerPos = playerPos;
            this.range = range;
            this.threshold = threshold;
            this.zombie = zombie;
            this.color = color;
            this.alpha = alpha;
            this.width = range * 2;
            this.totalBlocks = width * width * width;
        }

        private boolean matches(
                Level world,
                int range,
                int threshold,
                boolean zombie,
                MinecraftColor color,
                int alpha) {
            return this.world == world
                    && this.range == range
                    && this.threshold == threshold
                    && this.zombie == zombie
                    && this.color == color
                    && this.alpha == alpha;
        }

        private boolean isComplete() {
            return nextBlock >= totalBlocks;
        }

        private boolean containsAffectedPosition(BlockPos pos) {
            return pos.getX() >= playerPos.getX() - range
                    && pos.getX() < playerPos.getX() + range
                    && pos.getY() >= playerPos.getY() - range - 1
                    && pos.getY() < playerPos.getY() + range
                    && pos.getZ() >= playerPos.getZ() - range
                    && pos.getZ() < playerPos.getZ() + range;
        }

        private boolean advance() {
            int processed = 0;
            while (nextBlock < totalBlocks && processed < BrightnessClient.SCAN_BLOCKS_PER_TICK) {
                // 1次元の走査位置を x, y, z 座標に戻す
                int x = playerPos.getX() - range + nextBlock / (width * width);
                int y = playerPos.getY() - range + nextBlock / width % width;
                int z = playerPos.getZ() - range + nextBlock % width;
                mutableBlockPos.set(x, y, z);
                mutableBlockPosY1.set(x, y - 1, z);
                if (checkBrightness(world, mutableBlockPos, mutableBlockPosY1, threshold, zombie) != -1) {
                    markers.add(new BrightnessMarker(color, alpha, mutableBlockPos.immutable()));
                }
                nextBlock++;
                processed++;
            }
            return isComplete();
        }
    }
}
