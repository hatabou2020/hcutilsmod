package com.htbcraft.hcutilsmod.mods.twitter;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import com.htbcraft.hcutilsmod.common.HCCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.auth.AccessToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class AccessTokenLoader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String EXTENSION = ".token";

    private static String getFullPath() {
        return HCCrypt.geDirectory() +
                File.separator +
                HCUtilsMod.MOD_ID + EXTENSION;
    }

    public static Boolean isExist() {
        return Files.exists(Path.of(getFullPath()));
    }

    public static void save(AccessToken accessToken) throws Exception {
        LOGGER.info("AccessToken save");

        if (!HCCrypt.isAvailable()) {
            throw new Exception("Crypt not available...");
        }

        try (FileWriter filewriter = new FileWriter(getFullPath(), false)) {
            String token = HCCrypt.encrypt(accessToken.getToken());
            String tokenSecret = HCCrypt.encrypt(accessToken.getTokenSecret());
            filewriter.write(token + System.lineSeparator());
            filewriter.write(tokenSecret);
        }
    }

    public static AccessToken load() throws Exception {
        LOGGER.info("AccessToken load");

        if (!HCCrypt.isAvailable()) {
            throw new Exception("Crypt not available...");
        }

        if (!isExist()) {
            throw new Exception("AccessToken no exist...");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(getFullPath()))) {
            String token = HCCrypt.decrypt(br.readLine());
            String tokenSecret = HCCrypt.decrypt(br.readLine());
            return new AccessToken(token, tokenSecret);
        }
    }
}
