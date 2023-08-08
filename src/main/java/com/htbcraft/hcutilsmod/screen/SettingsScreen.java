package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SettingsScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation WINDOW_TEXTURE = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("textures/gui/advancements/backgrounds/stone.png");

    private static final int WINDOW_WIDTH = 240;
    private static final int WINDOW_HEIGHT = 205;

    private final Screen parent;
    private int win_x;
    private int win_y;
    private final int win_w;
    private final int win_h;

    public SettingsScreen(Screen parent, Component titleIn) {
        this(parent, titleIn, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public SettingsScreen(Screen parent, Component titleIn, int width, int height) {
        super(titleIn);
        this.parent = parent;
        win_x = 0;
        win_y = 0;
        win_w = width;
        win_h = height;
    }

    protected Screen getParent() {
        return this.parent;
    }

    public int getPosX() {
        return win_x;
    }

    public int getPosY() {
        return win_y;
    }

    public int getWidth() {
        return win_w;
    }

    public int getHeight() {
        return win_h;
    }

    protected void init() {
        win_x = (this.width - win_w) / 2;
        win_y = (this.height - win_h) / 2;
        LOGGER.info("win_x: " + win_x);
        LOGGER.info("win_y: " + win_y);
        LOGGER.info("win_w: " + win_w);
        LOGGER.info("win_h: " + win_h);
        LOGGER.info("width: " + width);
        LOGGER.info("height: " + height);
    }

    public void onClose() {
        HCSettings.getInstance().saveOptions();
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(guiGraphics);
        renderWindow(guiGraphics, win_x, win_y, win_w, win_h, this.title, BACKGROUND_TEXTURE);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    // このメソッドはライブラリ化したい
    public static void renderWindow(GuiGraphics guiGraphics, int x, int y, int width, int height, Component title, ResourceLocation background) {
        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();

        if (window.getGuiScaledWidth() < width) {
            width = window.getGuiScaledWidth();
        }

        if (window.getGuiScaledHeight() < height) {
            height = window.getGuiScaledHeight();
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // ウィンドウ背景の描画
        RenderSystem.enableBlend();
        guiGraphics.blit(background, x + 10, y + 10, 0, 0, width - 20, height - 20, 16, 16);
        RenderSystem.disableBlend();

        // ウィンドウ枠の描画
        RenderSystem.enableBlend();

        // ウィンドウ枠の4隅
        guiGraphics.blit(WINDOW_TEXTURE, x, y, 0, 0, 20, 30, 256, 256);
        guiGraphics.blit(WINDOW_TEXTURE, x + width - 20, y, 252 - 20, 0, 20, 30, 256, 256);
        guiGraphics.blit(WINDOW_TEXTURE, x, y + height - 20, 0, 140 - 20, 20, 20, 256, 256);
        guiGraphics.blit(WINDOW_TEXTURE, x + width - 20, y + height - 20, 252 - 20, 140 - 20, 20, 20, 256, 256);

        int x1, y1, texture_x, texture_y, texture_width, texture_height, count;

        // ウィンドウ枠の上線
        count = ((width - 40) / (252 - 40)) + (((width - 40) % (252 - 40)) != 0 ? 1 : 0);
        for (int i = 0; i < count; i++) {
            x1 = x + 20 + ((252 - 40) * i);
            y1 = y;
            texture_x = 20;
            texture_y = 0;
            texture_width = ((width - 40) > ((252 - 40) * (i + 1))) ? (252 - 40) : (width - 40) - ((252 - 40) * (i + 1));
            if (texture_width < 0) {
                texture_width = (252 - 40) + (width - 40) - ((252 - 40) * (i + 1));
            }
            texture_height = 30;

            guiGraphics.blit(WINDOW_TEXTURE, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
        }

        // ウィンドウ枠の下線
        for (int i = 0; i < count; i++) {
            x1 = x + 20 + ((252 - 40) * i);
            y1 = y + height - 20;
            texture_x = 20;
            texture_y = 140 - 20;
            texture_width = ((width - 40) > ((252 - 40) * (i + 1))) ? (252 - 40) : (width - 40) - ((252 - 40) * (i + 1));
            if (texture_width < 0) {
                texture_width = (252 - 40) + (width - 40) - ((252 - 40) * (i + 1));
            }
            texture_height = 20;

            guiGraphics.blit(WINDOW_TEXTURE, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
        }

        // ウィンドウ枠の左線
        count = ((height - 50) / (140 - 50)) + (((height - 50) % (140 - 50)) != 0 ? 1 : 0);
        for (int i = 0; i < count; i++) {
            x1 = x;
            y1 = y + 30 + ((140 - 50) * i);
            texture_x = 0;
            texture_y = 30;
            texture_width = 20;
            texture_height = ((height - 50) > ((140 - 50) * (i + 1))) ? (140 - 50) : (height - 50) - ((140 - 50) * (i + 1));
            if (texture_height < 0) {
                texture_height = (140 - 50) + (height - 50) - ((140 - 50) * (i + 1));
            }

            guiGraphics.blit(WINDOW_TEXTURE, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
        }

        // ウィンドウ枠の右線
        for (int i = 0; i < count; i++) {
            x1 = x + width - 20;
            y1 = y + 30 + ((140 - 50) * i);
            texture_x = 252 - 20;
            texture_y = 30;
            texture_width = 20;
            texture_height = ((height - 50) > ((140 - 50) * (i + 1))) ? (140 - 50) : (height - 50) - ((140 - 50) * (i + 1));
            if (texture_height < 0) {
                texture_height = (140 - 50) + (height - 50) - ((140 - 50) * (i + 1));
            }

            guiGraphics.blit(WINDOW_TEXTURE, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
        }

        RenderSystem.disableBlend();

        // タイトルの描画
        guiGraphics.drawString(mc.font, title.getString(), x + 8, y + 6, 4210752);
    }
}
