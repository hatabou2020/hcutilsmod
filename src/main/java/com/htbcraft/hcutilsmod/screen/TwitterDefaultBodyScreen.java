package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TwitterDefaultBodyScreen extends SettingsScreen {
    private static final Logger LOGGER = LogManager.getLogger();

    private TextFieldWidget defTweetEdit1 = null;
    private TextFieldWidget defTweetEdit2 = null;
    private TextFieldWidget defTweetEdit3 = null;
    private TextFieldWidget defTweetEdit4 = null;

    private TranslationTextComponent tweetBodyDef;

    public TwitterDefaultBodyScreen(Screen parent) {
        super(parent, new TranslationTextComponent("hcutilsmod.settings.twitter.body.default.title"), 260, 180);
    }

    protected void init() {
        super.init();

        // デフォルト本文１
        this.defTweetEdit1 = this.addButton(
            new TextFieldWidget(this.font, getPosX() + (getWidth() - 220) / 2, getPosY() + 40, 220, 20,
            new TranslationTextComponent("")));
        this.defTweetEdit1.setMaxLength(35);
        this.defTweetEdit1.setResponder((val) -> this.dispTweetTextLength());
        this.defTweetEdit1.setValue(HCSettings.getInstance().twitterText1);

        // デフォルト本文２
        this.defTweetEdit2 = this.addButton(
            new TextFieldWidget(this.font, getPosX() + (getWidth() - 220) / 2, getPosY() + 63, 220, 20,
            new TranslationTextComponent("")));
        this.defTweetEdit2.setMaxLength(35);
        this.defTweetEdit2.setResponder((val) -> this.dispTweetTextLength());
        this.defTweetEdit2.setValue(HCSettings.getInstance().twitterText2);

        // デフォルト本文３
        this.defTweetEdit3 = this.addButton(
            new TextFieldWidget(this.font, getPosX() + (getWidth() - 220) / 2, getPosY() + 86, 220, 20,
            new TranslationTextComponent("")));
        this.defTweetEdit3.setMaxLength(35);
        this.defTweetEdit3.setResponder((val) -> this.dispTweetTextLength());
        this.defTweetEdit3.setValue(HCSettings.getInstance().twitterText3);

        // デフォルト本文４
        this.defTweetEdit4 = this.addButton(
            new TextFieldWidget(this.font, getPosX() + (getWidth() - 220) / 2, getPosY() + 109, 220, 20,
            new TranslationTextComponent("")));
        this.defTweetEdit4.setMaxLength(35);
        this.defTweetEdit4.setResponder((val) -> this.dispTweetTextLength());
        this.defTweetEdit4.setValue(HCSettings.getInstance().twitterText4);

        // ツイート本文の文字数
        dispTweetTextLength();

        // 戻る
        this.addButton(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
            ITextComponent.nullToEmpty(I18n.get("hcutilsmod.settings.twitter.return")),
            (var1) -> {
                updateTweetText();
                this.getMinecraft().setScreen(this.getParent());
            }));
    }

    // ツイート本文の文字数
    private void dispTweetTextLength() {
        int len = 0;

        if (this.defTweetEdit1 != null) {
            len += this.defTweetEdit1.getValue().length();
        }
        if (this.defTweetEdit2 != null) {
            len += this.defTweetEdit2.getValue().length();
        }
        if (this.defTweetEdit3 != null) {
            len += this.defTweetEdit3.getValue().length();
        }
        if (this.defTweetEdit4 != null) {
            len += this.defTweetEdit4.getValue().length();
        }

        this.tweetBodyDef =
                (TranslationTextComponent) new TranslationTextComponent("hcutilsmod.settings.twitter.body.default.length").append("" + len);
    }

    // デフォルト本文の保存
    private void updateTweetText() {
        HCSettings.getInstance().twitterText1 = this.defTweetEdit1.getValue();
        HCSettings.getInstance().twitterText2 = this.defTweetEdit2.getValue();
        HCSettings.getInstance().twitterText3 = this.defTweetEdit3.getValue();
        HCSettings.getInstance().twitterText4 = this.defTweetEdit4.getValue();
        LOGGER.info(this.defTweetEdit1.getValue() + this.defTweetEdit2.getValue() + this.defTweetEdit3.getValue() + this.defTweetEdit4.getValue());
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        drawString(matrixStack, this.font, this.tweetBodyDef, getPosX() + 18, getPosY() + 30, 16777215);
    }
}
