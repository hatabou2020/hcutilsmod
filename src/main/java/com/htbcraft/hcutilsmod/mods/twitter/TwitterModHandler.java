package com.htbcraft.hcutilsmod.mods.twitter;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.htbcraft.hcutilsmod.screen.TwitterTweetScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.*;

import java.io.File;

public class TwitterModHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void onScreenshot(ScreenshotEvent event) {
        if (!HCSettings.getInstance().enableTwitterMod) {
            return;
        }

        try {
            Twitter twitter = new TwitterFactory().getInstance();
            TwitterConsumer consumer = TwitterConsumer.INSTANCE;
            twitter.setOAuthConsumer(consumer.getKey(), consumer.getSecret());
            twitter.setOAuthAccessToken(AccessTokenLoader.load());

            File screenShot = event.getScreenshotFile();

            // ツイート画面
            Minecraft.getInstance().displayGuiScreen(
                new TwitterTweetScreen((result, tweet) -> {
                    if (result) {
                        new Thread(() -> {
                            try {
                                // ツイートする
                                StatusUpdate statusUpdate = new StatusUpdate(tweet);
                                statusUpdate.setMedia(screenShot);
                                Status status = twitter.updateStatus(statusUpdate);
                                LOGGER.info(status);
                            } catch (TwitterException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                    Minecraft.getInstance().displayGuiScreen(null);
                },
                screenShot.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
