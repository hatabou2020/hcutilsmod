package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SettingsScreen extends Screen {
    private static final ResourceLocation WINDOW_TEXTURE = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("textures/gui/advancements/backgrounds/stone.png");

    private static final int WINDOW_WIDTH = 240;
    private static final int WINDOW_HEIGHT = 180;

    private int win_x, win_y;

    public SettingsScreen(ITextComponent titleIn) {
        super(titleIn);
        win_x = 0;
        win_y = 0;
    }

    public int getPosX() {
        return win_x;
    }

    public int getPosY() {
        return win_y;
    }

    public int getWidth() {
        return WINDOW_WIDTH;
    }

    public int getHeight() {
        return WINDOW_HEIGHT;
    }

    public void func_231158_b_(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.func_231158_b_(p_231158_1_, p_231158_2_, p_231158_3_);

        win_x = (p_231158_2_ - WINDOW_WIDTH) / 2;
        win_y = (p_231158_3_ - WINDOW_HEIGHT) / 2;
    }

    public void func_231164_f_() {
        HCSettings.getInstance().saveOptions();
    }

    public void func_230430_a_(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.func_230446_a_(p_230430_1_);
        renderWindow(p_230430_1_, win_x, win_y, WINDOW_WIDTH, WINDOW_HEIGHT, this.field_230704_d_, BACKGROUND_TEXTURE);
        super.func_230430_a_(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    // このメソッドはライブラリ化したい
    public static void renderWindow(MatrixStack matrixStack, int x, int y, int width, int height, ITextComponent title, ResourceLocation background) {
        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        Minecraft mc = Minecraft.getInstance();
        MainWindow window = mc.getMainWindow();

        if (window.getScaledWidth() < width) {
            width = window.getScaledWidth();
        }

        if (window.getScaledHeight() < height) {
            height = window.getScaledHeight();
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        // ウィンドウ背景の描画
        mc.getTextureManager().bindTexture(background);
        RenderSystem.enableBlend();
        AbstractGui.func_238463_a_(matrixStack, x + 10, y + 10, 0, 0, width - 20, height - 20, 16, 16);
        RenderSystem.disableBlend();

        // ウィンドウ枠の描画
        mc.getTextureManager().bindTexture(WINDOW_TEXTURE);
        RenderSystem.enableBlend();

        // ウィンドウ枠の4隅
        AbstractGui.func_238463_a_(matrixStack, x, y, 0, 0, 20, 30, 256, 256);
        AbstractGui.func_238463_a_(matrixStack, x + width - 20, y, 252 - 20, 0, 20, 30, 256, 256);
        AbstractGui.func_238463_a_(matrixStack, x, y + height - 20, 0, 140 - 20, 20, 20, 256, 256);
        AbstractGui.func_238463_a_(matrixStack, x + width - 20, y + height - 20, 252 - 20, 140 - 20, 20, 20, 256, 256);

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

            AbstractGui.func_238463_a_(matrixStack, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
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

            AbstractGui.func_238463_a_(matrixStack, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
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

            AbstractGui.func_238463_a_(matrixStack, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
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

            AbstractGui.func_238463_a_(matrixStack, x1, y1, texture_x, texture_y, texture_width, texture_height, 256, 256);
        }

        RenderSystem.disableBlend();

        // タイトルの描画
        mc.fontRenderer.func_238421_b_(matrixStack, title.getString(), (float)x + 8, (float)y + 6, 4210752);
    }
}
