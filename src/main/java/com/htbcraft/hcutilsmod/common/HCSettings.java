package com.htbcraft.hcutilsmod.common;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class HCSettings {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Splitter KEY_VALUE_SPLITTER = Splitter.on(':').limit(2);

    private static HCSettings instance;

    private final File optionsFile;

    public enum SortType {
        NAME,       // 名前順
        CATEGORY,   // カテゴリ順
    }

    public Boolean enableCordsMod = false;                      // プレイヤー座標の表示
    public SortType sortType = SortType.NAME;                   // インベントリのソート種類
    public Boolean enableAutoReplaceItem = true;                // 手持ちアイテムの自動補充
    public Boolean enableFindSpawnerMod = false;                // スポナー検索
    public int rangeFindSpawner = 64;                           // スポナー検索の範囲
    public long timeFindSpawner = 30L;                          // スポナー座標の表示時間
    public int rangeBrightness = 8;                             // 明るさマーカー表示の範囲
    public int thresholdBrightness = 0;                         // 明るさの閾値
    public Boolean zombieBrightness = true;                     // ゾンビが湧くことができないブロック：除外する(true)／除外しない(false)
    public MinecraftColor colorBrightness = MinecraftColor.RED; // 明るさマーカーの色
    public int alphaBrightness = 0x7F;                          // 明るさマーカーの透過度
    public Boolean enableTwitterMod = false;                    // スクリーンショットのツイート
    public String twitterText1 = "";                            // ツイートのデフォルト本文１
    public String twitterText2 = "";                            // ツイートのデフォルト本文２
    public String twitterText3 = "";                            // ツイートのデフォルト本文３
    public String twitterText4 = " #Minecraft #マインクラフト #hcutilsmod";// ツイートのデフォルト本文４

    public HCSettings(Minecraft mcIn) {
        instance = this;
        this.optionsFile = new File(mcIn.gameDirectory, "hcutilsmod_options.txt");

        this.loadOptions();
    }

    public static HCSettings getInstance() {
        return instance;
    }

    public void loadOptions() {
        try {
            if (!this.optionsFile.exists()) {
                LOGGER.info("hcutilsmod options file not exist!");
                return;
            }

            CompoundTag compoundTag = new CompoundTag();

            try (BufferedReader bufferedreader = Files.newReader(this.optionsFile, Charsets.UTF_8)) {
                bufferedreader.lines().forEach((p_230004_1_) -> {
                    try {
                        Iterator<String> iterator = KEY_VALUE_SPLITTER.split(p_230004_1_).iterator();
                        compoundTag.putString(iterator.next(), iterator.next());
                    } catch (Exception exception2) {
                        LOGGER.warn("Skipping bad option: {}", p_230004_1_);
                    }

                });
            }

            for(String s : compoundTag.getAllKeys()) {
                String s1 = compoundTag.getString(s);

                try {
                    if ("cordsmod".equals(s)) {
                        enableCordsMod = Boolean.valueOf(s1);
                    }
                    if ("inventorymod".equals(s)) {
                        SortType[] values = SortType.values();
                        sortType = values[Integer.parseInt(s1)];
                    }
                    if ("inventorymod.autoreplaceitem".equals(s)) {
                        enableAutoReplaceItem = Boolean.valueOf(s1);
                    }
                    if ("findspawnermod".equals(s)) {
                        enableFindSpawnerMod = Boolean.valueOf(s1);
                    }
                    if ("findspawnermod.range".equals(s)) {
                        rangeFindSpawner = Integer.parseInt(s1);
                    }
                    if ("findspawnermod.time".equals(s)) {
                        timeFindSpawner = Long.getLong(s1);
                    }
                    if ("brightnessmod.range".equals(s)) {
                        rangeBrightness = Integer.parseInt(s1);
                    }
                    if ("brightnessmod.threshold".equals(s)) {
                        thresholdBrightness = Integer.parseInt(s1);
                    }
                    if ("brightnessmod.zombie".equals(s)) {
                        zombieBrightness = Boolean.valueOf(s1);
                    }
                    if ("brightnessmod.color".equals(s)) {
                        colorBrightness = MinecraftColor.values()[Integer.parseInt(s1)];
                    }
                    if ("brightnessmod.alpha".equals(s)) {
                        alphaBrightness = Integer.parseInt(s1);
                    }
                    if (HCCrypt.isSupportOS()) {
                        if ("twittermod".equals(s)) {
                            enableTwitterMod = Boolean.valueOf(s1);
                        }
                        if ("twittermod.text1".equals(s)) {
                            twitterText1 = s1;
                        }
                        if ("twittermod.text2".equals(s)) {
                            twitterText2 = s1;
                        }
                        if ("twittermod.text3".equals(s)) {
                            twitterText3 = s1;
                        }
                        if ("twittermod.text4".equals(s)) {
                            twitterText4 = s1;
                        }
                    }
                } catch (Exception exception) {
                    LOGGER.warn("Skipping bad option: {}:{}", s, s1);
                }
            }
        } catch (Exception exception1) {
            LOGGER.error("Failed to load options", exception1);
        }
    }

    public void saveOptions() {
        if (net.minecraftforge.client.loading.ClientModLoader.isLoading())
            return; //Don't save settings before mods add keybindigns and the like to prevent them from being deleted.
        try (PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8))) {
            printwriter.println("cordsmod:" + enableCordsMod);
            printwriter.println("inventorymod:" + sortType.ordinal());
            printwriter.println("inventorymod.autoreplaceitem:" + enableAutoReplaceItem);
            printwriter.println("findspawnermod:" + enableFindSpawnerMod);
            printwriter.println("findspawnermod.range:" + rangeFindSpawner);
            printwriter.println("findspawnermod.time:" + timeFindSpawner);
            printwriter.println("brightnessmod.range:" + rangeBrightness);
            printwriter.println("brightnessmod.threshold:" + thresholdBrightness);
            printwriter.println("brightnessmod.zombie:" + zombieBrightness);
            printwriter.println("brightnessmod.color:" + colorBrightness.ordinal());
            printwriter.println("brightnessmod.alpha:" + alphaBrightness);
            if (HCCrypt.isSupportOS()) {
                printwriter.println("twittermod:" + enableTwitterMod);
                printwriter.println("twittermod.text1:" + twitterText1);
                printwriter.println("twittermod.text2:" + twitterText2);
                printwriter.println("twittermod.text3:" + twitterText3);
                printwriter.println("twittermod.text4:" + twitterText4);
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to save options", exception);
        }
    }
}
