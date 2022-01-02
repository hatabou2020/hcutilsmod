package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.common.MinecraftColor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

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

    static {
        ClientRegistry.registerKeyBinding(BIND_KEY);
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
    public void onKeyInput(InputEvent.KeyInputEvent event) {
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
    public void onGuiOpen(ScreenOpenEvent event) {
        // 設定変更されたら更新
        if (event.getScreen() == null) {
            prevPlayerPos = BlockPos.ZERO;
        }
    }

    @SubscribeEvent
    public void onBlock(BlockEvent event) {
        // ブロックに変化があったら更新
        prevPlayerPos = BlockPos.ZERO;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!isOverWorld()) {
            dispBrightness = false;
        }

        if (dispBrightness) {
            BlockPos playerPos = event.player.blockPosition();
            if (prevPlayerPos.compareTo(playerPos) != 0) {
                prevPlayerPos = playerPos;
                tergetMarkers = makeBrightnessMarkers(
                                    event.player.level,
                                    playerPos,
                                    HCSettings.getInstance().rangeBrightness,
                                    HCSettings.getInstance().thresholdBrightness,
                                    HCSettings.getInstance().colorBrightness,
                                    HCSettings.getInstance().alphaBrightness);
            }
        }
        else {
            if (tergetMarkers != null) {
                tergetMarkers = null;
                prevPlayerPos = BlockPos.ZERO;
            }
        }
    }

    private ArrayList<BrightnessMarker> makeBrightnessMarkers(Level world, BlockPos playerPos, int range, int threshold, MinecraftColor color, int alpha) {
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
                    int brightness = checkBrightness(world, mutableBlockPos, mutableBlockPosY1, threshold);
                    if (brightness != -1) {
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

    private int checkBrightness(Level world, BlockPos pos, BlockPos posY1, int threshold) {
        BlockState state = world.getBlockState(pos);
        BlockState stateY1 = world.getBlockState(posY1);

        int brightness = world.getBrightness(LightLayer.BLOCK, pos);

        if ((threshold >= brightness) && state.isAir() && !stateY1.isAir()) {
            return brightness;
        }

        return -1;
    }

    @SubscribeEvent
    public void onRenderLevelLast(RenderLevelLastEvent event) {
        if (tergetMarkers != null) {
            tergetMarkers.forEach(
                    m -> m.draw(brightnessMarkerRenderer.update(event.getPoseStack())));
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayPreLayer(RenderGameOverlayEvent.PreLayer event) {
        // マーカー表示中がわかるように画面の左下にアイコン出す
        if (dispBrightness) {
            int x = 1;
            int y = event.getWindow().getGuiScaledHeight() - 20 - 1;

            RenderSystem.setShaderTexture(0, LIGHT_ICON);
            RenderSystem.enableBlend();
            GuiComponent.blit(event.getMatrixStack(),
                    x,
                    y,
                    0,
                    0,
                    0,
                    20,
                    20,
                    20,
                    20);
            RenderSystem.disableBlend();
        }
    }
}
