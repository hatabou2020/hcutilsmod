package com.htbcraft.hcutilsmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IBidiRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class TwitterPinScreen extends SettingsScreen {
    private static final Logger LOGGER = LogManager.getLogger();

    private final TwitterPinScreen.OnAuth callback;

    private IBidiRenderer message = IBidiRenderer.field_243257_a;
    private TextFieldWidget pinEdit;
    private Button authButton;

    public TwitterPinScreen(Screen parent, TwitterPinScreen.OnAuth callback) {
        super(parent, new TranslationTextComponent("hcutilsmod.settings.twitter.pin.title"), 240, 150);
        this.callback = callback;
    }

    public void tick() {
        this.pinEdit.tick();
    }

    protected void init() {
        super.init();
        this.message = IBidiRenderer.func_243258_a(this.font,
            new TranslationTextComponent("hcutilsmod.settings.twitter.pin.text"),
            this.width - 50);

        Objects.requireNonNull(this.minecraft).keyboardListener.enableRepeatEvents(true);

        // 認証する
        this.authButton = this.addButton(
            new Button(getPosX() + 18, getPosY() + 100, 100, 20,
            new TranslationTextComponent("hcutilsmod.settings.twitter.pin.auth"),
            (var1) -> {
                LOGGER.info("Push Auth");
                this.callback.onAuth(this.pinEdit.getText());
            }));

        // キャンセル
        this.addButton(
            new Button(getPosX() + (getWidth() - 118), getPosY() + 100, 100, 20,
            new TranslationTextComponent("hcutilsmod.settings.twitter.pin.cancel"),
            (var1) -> this.getMinecraft().displayGuiScreen(this.getParent())));

        // PINコード
        this.pinEdit = this.addButton(
            new TextFieldWidget(this.font, getPosX() + (getWidth() - 100) / 2, getPosY() + 60, 100, 20,
            new TranslationTextComponent("")));
        this.pinEdit.setMaxStringLength(7);
        this.pinEdit.setFocused2(true);
        this.pinEdit.setResponder((val) -> this.updateAuthButtonStatus());
        this.setFocusedDefault(this.pinEdit);

        this.updateAuthButtonStatus();
    }

    private void updateAuthButtonStatus() {
        // PINは7文字の数字
        boolean isDigit = this.pinEdit.getText().matches("[+-]?\\d*(\\.\\d+)?");
        this.authButton.active = isDigit && (this.pinEdit.getText().length() == 7);
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.message.func_241863_a(matrixStack, this.width / 2, getPosY() + 30);
    }

    public interface OnAuth {
        void onAuth(String pin);
    }
}
