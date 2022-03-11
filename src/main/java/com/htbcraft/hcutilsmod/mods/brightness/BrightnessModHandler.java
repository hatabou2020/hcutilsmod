package com.htbcraft.hcutilsmod.mods.brightness;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.HCKeyBinding;
import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.common.MinecraftColor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
        World level = Minecraft.getInstance().world;
        return (level != null ? level.getDimensionKey().getLocation().getPath().compareTo("overworld") : 0) == 0;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().currentScreen != null) {
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
    public void onGuiOpen(GuiOpenEvent event) {
        // 設定変更されたら更新
        if (event.getGui() == null) {
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
        if (event.side.isServer()) {
            return;
        }

        if (!isOverWorld()) {
            dispBrightness = false;
        }

        if (dispBrightness) {
            BlockPos playerPos = event.player.getPosition();
            if (prevPlayerPos.compareTo(playerPos) != 0) {
                prevPlayerPos = playerPos;

                new Thread(() ->
                    tergetMarkers = makeBrightnessMarkers(
                                        event.player.world,
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

    private ArrayList<BrightnessMarker> makeBrightnessMarkers(World world, BlockPos playerPos, int range, int threshold, Boolean zombie, MinecraftColor color, int alpha) {
        ArrayList<BrightnessMarker> makeMarkers = new ArrayList<>();

        int i = playerPos.getX() - range;
        int j = playerPos.getX() + range;
        int k = playerPos.getY() - range;
        int l = playerPos.getY() + range;
        int i1 = playerPos.getZ() - range;
        int j1 = playerPos.getZ() + range;

        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
        BlockPos.Mutable mutableBlockPosY1 = new BlockPos.Mutable();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    mutableBlockPos.setPos(k1, l1, i2);
                    mutableBlockPosY1.setPos(k1, l1 - 1, i2);
                    if (checkBrightness(world, mutableBlockPos, mutableBlockPosY1, threshold, zombie) != -1) {
                        makeMarkers.add(
                                new BrightnessMarker(
                                        color,
                                        alpha,
                                        mutableBlockPos.toImmutable()));
                    }
                }
            }
        }

        if (makeMarkers.size() == 0) {
            return null;
        }

        return makeMarkers;
    }

    private int checkBrightness(World world, BlockPos pos, BlockPos posY1, int threshold, Boolean zombie) {
        // ゾンビが湧くことができないブロックは除外する
        if (zombie && !EntitySpawnPlacementRegistry.PlacementType.ON_GROUND.canSpawnAt(world, pos, EntityType.ZOMBIE)) {
            return -1;
        }

        BlockState state = world.getBlockState(pos);
        BlockState stateY1 = world.getBlockState(posY1);

        int brightness = world.getLightFor(LightType.BLOCK, pos);

        if ((threshold >= brightness) && state.isAir() && !stateY1.isAir()) {
            return brightness;
        }

        return -1;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (tergetMarkers != null) {
            tergetMarkers.forEach(
                    m -> m.draw(brightnessMarkerRenderer.update(event.getMatrixStack())));
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayPre(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        // マーカー表示中がわかるように画面の左下にアイコン出す
        if (dispBrightness) {
            int x = 1;
            int y = event.getWindow().getScaledHeight() - 20 - 1;

            Minecraft.getInstance().getTextureManager().bindTexture(LIGHT_ICON);
            RenderSystem.enableBlend();
            AbstractGui.blit(event.getMatrixStack(),
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
