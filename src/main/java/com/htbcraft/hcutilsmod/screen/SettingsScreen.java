package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends Screen {
    private static final ResourceLocation WINDOW_TEXTURE = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("textures/gui/advancements/backgrounds/stone.png");

    private static final int WINDOW_WIDTH = 240;
    private static final int WINDOW_HEIGHT = 180;

    private Screen parent;
    private int win_x, win_y;
    private int win_w, win_h;

    public SettingsScreen(Screen parent, Component titleIn) {
        super(titleIn);
        this.parent = parent;
        win_x = 0;
        win_y = 0;
        win_w = WINDOW_WIDTH;
        win_h = WINDOW_HEIGHT;
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
        win_x = (this.width - WINDOW_WIDTH) / 2;
        win_y = (this.height - WINDOW_HEIGHT) / 2;
    }

    public void onClose() {
        HCSettings.getInstance().saveOptions();
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(matrixStack);
        renderWindow(matrixStack, win_x, win_y, WINDOW_WIDTH, WINDOW_HEIGHT, this.title, BACKGROUND_TEXTURE);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    // このメソッドはライブラリ化したい
    public static void renderWindow(PoseStack matrixStack, int x, int y, int width, int height, Component title, ResourceLocation background) {
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
        RenderSystem.setShaderTexture(0, background);
        RenderSystem.enableBlend();
        GuiComponent.blit(matrixStack, x + 10, y + 10, 0, 0, width - 20, height - 20, 16, 16);
        RenderSystem.disableBlend();

        // ウィンドウ枠の描画
        RenderSystem.setShaderTexture(0, WINDOW_TEXTURE);
        RenderSystem.enableBlend();

        // ウィンドウ枠の4隅
        GuiComponent.blit(matrixStack, x, y, 0, 0, 20, 30, 256, 256);
        GuiComponent.blit(matrixStack, x + width - 20, y, 252 - 20, 0, 20, 30, 256, 256);
        GuiComponent.blit(matrixStack, x, y + height - 20, 0, 140 - 20, 20, 20, 256, 256);
        GuiComponent.blit(matrixStack, x + width - 20, y + height - 20, 252 - 20, 140 - 20, 20, 20, 256, 256);

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

            GuiComponent.blit(matrixStack, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
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

            GuiComponent.blit(matrixStack, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
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

            GuiComponent.blit(matrixStack, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
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

            GuiComponent.blit(matrixStack, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
        }

        RenderSystem.disableBlend();

        // タイトルの描画
        mc.font.draw(matrixStack, title.getString(), (float)x + 8, (float)y + 6, 4210752);
    }
}
