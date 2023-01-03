package com.htbcraft.hcutilsmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class TwitterPinScreen extends SettingsScreen {
    private static final Logger LOGGER = LogManager.getLogger();

    private final TwitterPinScreen.OnAuth callback;

    private MultiLineLabel message = MultiLineLabel.EMPTY;
    private EditBox pinEdit;
    private Button authButton;

    public TwitterPinScreen(Screen parent, TwitterPinScreen.OnAuth callback) {
        super(parent, Component.translatable("hcutilsmod.settings.twitter.pin.title"), 240, 150);
        this.callback = callback;
    }

    public void tick() {
        this.pinEdit.tick();
    }

    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.font,
            Component.translatable("hcutilsmod.settings.twitter.pin.text"),
            this.width - 50);

//        Objects.requireNonNull(this.minecraft).keyboardHandler.setSendRepeatsToGui(true);

        // 認証する
        this.authButton = this.addRenderableWidget(
            Button.builder(
                Component.translatable("hcutilsmod.settings.twitter.pin.auth"),
                (var1) -> {
                    LOGGER.info("Push Auth");
                    this.callback.onAuth(this.pinEdit.getValue());
                })
            .pos(getPosX() + 18, getPosY() + 100)
            .size(100, 20)
            .build());

        // キャンセル
        this.addRenderableWidget(
            Button.builder(
                Component.translatable("hcutilsmod.settings.twitter.pin.cancel"),
                (var1) -> this.getMinecraft().setScreen(this.getParent()))
            .pos(getPosX() + (getWidth() - 118), getPosY() + 100)
            .size(100, 20)
            .build());

        // PINコード
        this.pinEdit = this.addRenderableWidget(
            new EditBox(this.font, getPosX() + (getWidth() - 100) / 2, getPosY() + 60, 100, 20,
            Component.translatable("")));
        this.pinEdit.setMaxLength(7);
        this.pinEdit.setFocus(true);
        this.pinEdit.setResponder((val) -> this.updateAuthButtonStatus());
        this.setInitialFocus(this.pinEdit);

        this.updateAuthButtonStatus();
    }

    private void updateAuthButtonStatus() {
        // PINは7文字の数字
        boolean isDigit = this.pinEdit.getValue().matches("[+-]?\\d*(\\.\\d+)?");
        this.authButton.active = isDigit && (this.pinEdit.getValue().length() == 7);
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.message.renderCentered(matrixStack, this.width / 2, getPosY() + 30);
    }

    public interface OnAuth {
        void onAuth(String pin);
    }
}
