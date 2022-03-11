package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCCrypt;
import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.mods.twitter.AccessTokenLoader;
import com.htbcraft.hcutilsmod.mods.twitter.TwitterConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterSettingsScreen extends SettingsScreen {
    private static final Logger LOGGER = LogManager.getLogger();

    private Button connectButton = null;
    private static TranslatableComponent restartText = null;


    // 連携の状態（メニュー用）
    private enum TwitterConnectStatus {
        INIT("hcutilsmod.settings.twitter.connect"),
        SUCCESS("hcutilsmod.settings.twitter.connect.success"),
        FAILURE("hcutilsmod.settings.twitter.connect.failure"),
        COMPLETE("hcutilsmod.settings.twitter.connect.complete");

        private final TranslatableComponent menuText;

        TwitterConnectStatus(String lang) {
            this.menuText = new TranslatableComponent(lang);
        }

        public TranslatableComponent getMenuText() {
            if (this == FAILURE) {
                Style style = this.menuText.getStyle().withColor(0xE01000);
                this.menuText.setStyle(style);
            }
            else if (this == SUCCESS) {
                Style style = this.menuText.getStyle().withColor(0x00E0E0);
                this.menuText.setStyle(style);
            }

            return this.menuText;
        }
    }

    private TwitterConnectStatus connectStatus = TwitterConnectStatus.INIT;

    public TwitterSettingsScreen(Screen parent) {
        super(parent, new TranslatableComponent("hcutilsmod.settings.twitter.title"));
    }

    protected void init() {
        super.init();

        // Twitterと連携する
        this.connectButton = this.addRenderableWidget(
            new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 30, 180, 20,
            this.connectStatus.getMenuText().append("..."),
            (var1) -> {
                LOGGER.info("Push Connect");
                connectTwitter();
            }));

        // スクリーンショットのツイート：オン/オフ
        Button onoffButton = this.addRenderableWidget(
            new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 55, 180, 20,
            getEnableTwitterText(),
            (var1) -> {
                HCSettings.getInstance().enableTwitterMod = !HCSettings.getInstance().enableTwitterMod;
                var1.setMessage(getEnableTwitterText());
            }));

        // ツイートのデフォルト本文
        Button defBoxyButton = this.addRenderableWidget(
            new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 80, 180, 20,
            new TranslatableComponent("hcutilsmod.settings.twitter.body.default.title").append("..."),
            (var1) -> this.getMinecraft().setScreen(new TwitterDefaultBodyScreen(this))));

        // Twitter連携の解除
        Button destroyButton = this.addRenderableWidget(
            new Button(getPosX() + (getWidth() - 180) / 2, getPosY() + 105, 180, 20,
            new TranslatableComponent("hcutilsmod.settings.twitter.destroy.title").append("..."),
            (var1) ->
                this.getMinecraft().setScreen(
                    new ConfirmScreen((result) -> {
                        if (result) {
                            HCCrypt.destroy();
                            this.connectStatus = TwitterConnectStatus.INIT;
                            restartText = new TranslatableComponent("hcutilsmod.settings.twitter.destroy.restart");
                            Style style = restartText.getStyle().withBold(true).withUnderlined(true);
                            restartText.setStyle(style);
                        }
                        this.getMinecraft().setScreen(this);
                    },
                    new TranslatableComponent("hcutilsmod.settings.twitter.destroy.title"),
                    new TranslatableComponent("hcutilsmod.settings.twitter.destroy.text")))));

        // アクセストークンを持っていれば認証ボタンを無効にして設定を開放する
        if (AccessTokenLoader.isExist()) {
            if (this.connectStatus == TwitterConnectStatus.SUCCESS) {
                LOGGER.info("Success");
            }
            else {
                LOGGER.info("Authorized");
                this.connectStatus = TwitterConnectStatus.COMPLETE;
                this.connectButton.setMessage(this.connectStatus.getMenuText());
            }
            this.connectButton.active = false;
        }
        else {
            onoffButton.active = false;
            defBoxyButton.active = false;
            destroyButton.active = false;
            HCSettings.getInstance().enableTwitterMod = false;  // 強制的にOFFにする
            onoffButton.setMessage(getEnableTwitterText());
        }

        // 戻る
        this.addRenderableWidget(new Button(getPosX() + (getWidth() - 100) / 2, getPosY() + 165, 100, 20,
            new TranslatableComponent("hcutilsmod.settings.twitter.return"),
            (var1) -> this.getMinecraft().setScreen(this.getParent())));
    }

    // スクリーンショットのツイート：オン/オフ
    private Component getEnableTwitterText() {
        if (HCSettings.getInstance().enableTwitterMod) {
            return new TranslatableComponent("hcutilsmod.settings.twitter.enable");
        }
        else {
            return new TranslatableComponent("hcutilsmod.settings.twitter.disable");
        }
    }

    // Twitter連携
    private void connectTwitter() {
        Twitter twitter = new TwitterFactory().getInstance();

        try {
            TwitterConsumer consumer = TwitterConsumer.INSTANCE;
            twitter.setOAuthConsumer(consumer.getKey(), consumer.getSecret());
            RequestToken requestToken = twitter.getOAuthRequestToken("oob");
            String authorizationURL = requestToken.getAuthorizationURL();

            this.getMinecraft().setScreen(
                new ConfirmScreen((result) -> {
                    if (result) {
                        // ブラウザを起動して認証サイトへ
                        Util.getPlatform().openUri(authorizationURL);

                        // PINコード入力待ち
                        this.getMinecraft().setScreen(
                            new TwitterPinScreen(this, (pin) -> {
                                try {
                                    // PINコードが入力されたらアクセストークンを取得
                                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                                    // アクセストークンの永続化
                                    AccessTokenLoader.save(accessToken);
                                    this.connectStatus = TwitterConnectStatus.SUCCESS;
                                } catch (Exception e) {
                                    this.connectStatus = TwitterConnectStatus.FAILURE;
                                    e.printStackTrace();
                                }
                                this.getMinecraft().setScreen(this);
                            }
                        ));
                    }
                    else {
                        this.getMinecraft().setScreen(this);
                    }
                },
                new TranslatableComponent("hcutilsmod.settings.twitter.connect.title"),
                new TranslatableComponent("hcutilsmod.settings.twitter.connect.text", authorizationURL)
            ));
        } catch (Exception e) {
            this.connectStatus = TwitterConnectStatus.FAILURE;
            this.connectButton.setMessage(this.connectStatus.getMenuText());
            e.printStackTrace();
        }
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if (restartText != null) {
            int x = getPosX() + (getWidth() / 2) - (this.font.width(restartText) / 2) - 2;
            int y = getPosY() + 140 - 2;
            int w = x + 2 + this.font.width(restartText) + 2;
            int h = y + 2 + this.font.lineHeight + 2;
            fill(matrixStack, x, y, w, h, -1873784752);
            drawCenteredString(matrixStack, this.font, restartText, getPosX() + (getWidth() / 2), getPosY() + 140, 0xE01000);
        }
    }
}
