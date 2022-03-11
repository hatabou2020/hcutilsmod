package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.IBidiRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TwitterTweetScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();

    private final TwitterTweetScreen.OnTweet callback;
    private final String fileName;

    private IBidiRenderer message = IBidiRenderer.EMPTY;
    private TextFieldWidget tweetEdit1 = null;
    private TextFieldWidget tweetEdit2 = null;
    private TextFieldWidget tweetEdit3 = null;
    private TextFieldWidget tweetEdit4 = null;
    private ITextComponent tweetLength = null;

    public TwitterTweetScreen(TwitterTweetScreen.OnTweet callback, String fileName) {
        super(new TranslationTextComponent("hcutilsmod.settings.twitter.tweet.title"));
        this.callback = callback;
        this.fileName = fileName;
    }

    protected void init() {
        super.init();
        this.message = IBidiRenderer.create(this.font,
            new TranslationTextComponent("hcutilsmod.settings.twitter.tweet.text", this.fileName),
            this.width - 50);

        // はい
        this.addButton(new Button(this.width / 2 - 155, 180, 150, 20,
            DialogTexts.GUI_YES,
            (var1) -> {
                LOGGER.info("Push YES");
                this.callback.onTweet(true, getTweetTextBody());
            }));

        // いいえ
        this.addButton(new Button(this.width / 2 - 155 + 160, 180, 150, 20,
            DialogTexts.GUI_NO,
            (var1) -> {
                LOGGER.info("Push NO");
                this.callback.onTweet(false, getTweetTextBody());
            }));

        // 本文１
        this.tweetEdit1 = this.addButton(
            new TextFieldWidget(this.font, this.width - 230, 60, 220, 20,
            new TranslationTextComponent("")));
        this.tweetEdit1.setMaxLength(35);
        this.tweetEdit1.setResponder((val) -> this.dispTweetTextLength());
        this.tweetEdit1.setValue(HCSettings.getInstance().twitterText1);

        // 本文２
        this.tweetEdit2 = this.addButton(
            new TextFieldWidget(this.font, this.width - 230, 83, 220, 20,
            new TranslationTextComponent("")));
        this.tweetEdit2.setMaxLength(35);
        this.tweetEdit2.setResponder((val) -> this.dispTweetTextLength());
        this.tweetEdit2.setValue(HCSettings.getInstance().twitterText2);

        // 本文３
        this.tweetEdit3 = this.addButton(
            new TextFieldWidget(this.font, this.width - 230, 106, 220, 20,
            new TranslationTextComponent("")));
        this.tweetEdit3.setMaxLength(35);
        this.tweetEdit3.setResponder((val) -> this.dispTweetTextLength());
        this.tweetEdit3.setValue(HCSettings.getInstance().twitterText3);

        // 本文４
        this.tweetEdit4 = this.addButton(
            new TextFieldWidget(this.font, this.width - 230, 129, 220, 20,
            new TranslationTextComponent("")));
        this.tweetEdit4.setMaxLength(35);
        this.tweetEdit4.setResponder((val) -> this.dispTweetTextLength());
        this.tweetEdit4.setValue(HCSettings.getInstance().twitterText4);

        // ツイート本文の文字数
        dispTweetTextLength();
    }

    // ツイート本文の文字数
    private void dispTweetTextLength() {
        int len = 0;

        if (this.tweetEdit1 != null) {
            len += this.tweetEdit1.getValue().length();
        }
        if (this.tweetEdit2 != null) {
            len += this.tweetEdit2.getValue().length();
        }
        if (this.tweetEdit3 != null) {
            len += this.tweetEdit3.getValue().length();
        }
        if (this.tweetEdit4 != null) {
            len += this.tweetEdit4.getValue().length();
        }

        this.tweetLength =
                new TranslationTextComponent("hcutilsmod.settings.twitter.body.length").append(String.valueOf(len));
    }

    // ツイート本文
    public String getTweetTextBody() {
        return this.tweetEdit1.getValue() +
                this.tweetEdit2.getValue() +
                this.tweetEdit3.getValue() +
                this.tweetEdit4.getValue();
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawString(matrixStack, this.font, this.title, 12, 40, 16777215);
        this.message.renderLeftAligned(matrixStack, 12, 60, 20, 16777215);
        drawString(matrixStack, this.font, this.tweetLength, this.width - 80, 155, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public interface OnTweet {
        void onTweet(Boolean result, String tweet);
    }
}
