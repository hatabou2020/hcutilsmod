package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TwitterTweetScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();

    private final TwitterTweetScreen.OnTweet callback;
    private final String fileName;

    private MultiLineLabel message = MultiLineLabel.EMPTY;
    private EditBox tweetEdit1 = null;
    private EditBox tweetEdit2 = null;
    private EditBox tweetEdit3 = null;
    private EditBox tweetEdit4 = null;
    private Component tweetLength = null;

    public TwitterTweetScreen(TwitterTweetScreen.OnTweet callback, String fileName) {
        super(Component.translatable("hcutilsmod.settings.twitter.tweet.title"));
        this.callback = callback;
        this.fileName = fileName;
    }

    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.font,
            Component.translatable("hcutilsmod.settings.twitter.tweet.text", this.fileName),
            this.width - 50);

        // はい
        this.addRenderableWidget(
                Button.builder(
                    CommonComponents.GUI_YES,
                    (var1) -> {
                        LOGGER.info("Push YES");
                        this.callback.onTweet(true, getTweetTextBody());
                    })
                .pos(this.width / 2 - 155, 180)
                .size(150, 20)
                .build());

        // いいえ
        this.addRenderableWidget(
                Button.builder(
                    CommonComponents.GUI_NO,
                    (var1) -> {
                        LOGGER.info("Push NO");
                        this.callback.onTweet(false, getTweetTextBody());
                    })
                .pos(this.width / 2 - 155 + 160, 180)
                .size(150, 20)
                .build());

        // 本文１
        this.tweetEdit1 = this.addRenderableWidget(
            new EditBox(this.font, this.width - 230, 60, 220, 20,
            Component.translatable("")));
        this.tweetEdit1.setMaxLength(35);
        this.tweetEdit1.setResponder((val) -> this.dispTweetTextLength());
        this.tweetEdit1.setValue(HCSettings.getInstance().twitterText1);

        // 本文２
        this.tweetEdit2 = this.addRenderableWidget(
            new EditBox(this.font, this.width - 230, 83, 220, 20,
            Component.translatable("")));
        this.tweetEdit2.setMaxLength(35);
        this.tweetEdit2.setResponder((val) -> this.dispTweetTextLength());
        this.tweetEdit2.setValue(HCSettings.getInstance().twitterText2);

        // 本文３
        this.tweetEdit3 = this.addRenderableWidget(
            new EditBox(this.font, this.width - 230, 106, 220, 20,
            Component.translatable("")));
        this.tweetEdit3.setMaxLength(35);
        this.tweetEdit3.setResponder((val) -> this.dispTweetTextLength());
        this.tweetEdit3.setValue(HCSettings.getInstance().twitterText3);

        // 本文４
        this.tweetEdit4 = this.addRenderableWidget(
            new EditBox(this.font, this.width - 230, 129, 220, 20,
            Component.translatable("")));
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
                Component.translatable("hcutilsmod.settings.twitter.body.length").append(String.valueOf(len));
    }

    // ツイート本文
    public String getTweetTextBody() {
        return this.tweetEdit1.getValue() +
                this.tweetEdit2.getValue() +
                this.tweetEdit3.getValue() +
                this.tweetEdit4.getValue();
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
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
