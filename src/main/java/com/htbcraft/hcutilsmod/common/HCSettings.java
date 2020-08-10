package com.htbcraft.hcutilsmod.common;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class HCSettings {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Splitter KEY_VALUE_SPLITTER = Splitter.on(':').limit(2);

    private static HCSettings instance;

    private Minecraft mc;
    private File optionsFile;

    public Boolean enableCordsMod = false;

    public HCSettings(Minecraft mcIn) {
        instance = this;
        this.mc = mcIn;
        this.optionsFile = new File(this.mc.gameDir, "hcoptions.txt");

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

            CompoundNBT compoundnbt = new CompoundNBT();

            try (BufferedReader bufferedreader = Files.newReader(this.optionsFile, Charsets.UTF_8)) {
                bufferedreader.lines().forEach((p_230004_1_) -> {
                    try {
                        Iterator<String> iterator = KEY_VALUE_SPLITTER.split(p_230004_1_).iterator();
                        compoundnbt.putString(iterator.next(), iterator.next());
                    } catch (Exception exception2) {
                        LOGGER.warn("Skipping bad option: {}", p_230004_1_);
                    }

                });
            }

            for(String s : compoundnbt.keySet()) {
                String s1 = compoundnbt.getString(s);

                try {
                    if ("cordsmod".equals(s)) {
                        enableCordsMod = Boolean.valueOf(s1);
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
        if (net.minecraftforge.fml.client.ClientModLoader.isLoading())
            return; //Don't save settings before mods add keybindigns and the like to prevent them from being deleted.
        try (PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8))) {
            printwriter.println("cordsmod:" + enableCordsMod);
        } catch (Exception exception) {
            LOGGER.error("Failed to save options", exception);
        }
    }
}
