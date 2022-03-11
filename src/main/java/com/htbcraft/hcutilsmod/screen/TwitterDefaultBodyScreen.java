package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TwitterDefaultBodyScreen extends SettingsScreen {
    private static final Logger LOGGER = LogManager.getLogger();

    private EditBox defTweetEdit1 = null;
    private EditBox defTweetEdit2 = null;
    private EditBox defTweetEdit3 = null;
    private EditBox defTweetEdit4 = null;
    private Component tweetBodyDef = null;

    public TwitterDefaultBodyScreen(Screen parent) {
        super(parent, new TranslatableComponent("hcutilsmod.settings.twitter.body.default.title"), 260, 180);
    }

    protected void init() {
        super.init();

        // デフォルト本文１
        this.defTweetEdit1 = this.addRenderableWidget(
            new EditBox(this.font, getPosX() + (getWidth() - 220) / 2, getPosY() + 40, 220, 20,
            new TranslatableComponent("")));
        this.defTweetEdit1.setMaxLength(35);
        this.defTweetEdit1.setResponder((val) -> this.dispTweetTextLength());
        this.defTweetEdit1.setValue(HCSettings.getInstance().twitterText1);

        // デフォルト本文２
        this.defTweetEdit2 = this.addRenderableWidget(
            new EditBox(this.font, getPosX() + (getWidth() - 220) / 2, getPosY() + 63, 220, 20,
            new TranslatableComponent("")));
        this.defTweetEdit2.setMaxLength(35);
        this.defTweetEdit2.setResponder((val) -> this.dispTweetTextLength());
        this.defTweetEdit2.setValue(HCSettings.getInstance().twitterText2);

        // デフォルト本文３
        this.defTweetEdit3 = this.addRenderableWidget(
            new EditBox(this.font, getPosX() + (getWidth() - 220) / 2, getPosY() + 86, 220, 20,
            new TranslatableComponent("")));
        this.defTweetEdit3.setMaxLength(35);
        this.defTweetEdit3.setResponder((val) -> this.dispTweetTextLength());
        this.defTweetEdit3.setValue(HCSettings.getInstance().twitterText3);

        // デフォルト本文４
        this.defTweetEdit4 = this.addRenderableWidget(
            new EditBox(this.font, getPosX() + (getWidth() - 220) / 2, getPosY() + 109, 220, 20,
            new TranslatableComponent("")));
        this.defTweetEdit4.setMaxLength(35);
        this.defTweetEdit4.setResponder((val) -> this.dispTweetTextLength());
        this.defTweetEdit4.setValue(HCSettings.getInstance().twitterText4);

        // ツイート本文の文字数
        dispTweetTextLength();

        // 戻る
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 140, 100, 20,
            new TranslatableComponent("hcutilsmod.settings.twitter.return"),
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
                new TranslatableComponent("hcutilsmod.settings.twitter.body.default.length").append(String.valueOf(len));
    }

    // デフォルト本文の保存
    private void updateTweetText() {
        HCSettings.getInstance().twitterText1 = this.defTweetEdit1.getValue();
        HCSettings.getInstance().twitterText2 = this.defTweetEdit2.getValue();
        HCSettings.getInstance().twitterText3 = this.defTweetEdit3.getValue();
        HCSettings.getInstance().twitterText4 = this.defTweetEdit4.getValue();
        LOGGER.info(this.defTweetEdit1.getValue() + this.defTweetEdit2.getValue() + this.defTweetEdit3.getValue() + this.defTweetEdit4.getValue());
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if (this.tweetBodyDef != null) {
            drawString(matrixStack, this.font, this.tweetBodyDef, getPosX() + 18, getPosY() + 30, 16777215);
        }
    }
}
