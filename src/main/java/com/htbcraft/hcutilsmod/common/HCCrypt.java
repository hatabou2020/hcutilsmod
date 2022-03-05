package com.htbcraft.hcutilsmod.common;

import com.htbcraft.hcutilsmod.HCUtilsMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Base64;

public class HCCrypt {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String EXTENSION = ".crypt";

    // MSC03-J. センシティブな情報をハードコードしない
    // https://www.jpcert.or.jp/java-rules/msc03-j.html
    public static void init() throws Exception {
        if (isExist()) {
            LOGGER.info("file exist");
            return;
        }

        // フォルダの作成
        LOGGER.info(new File(geDirectory()).mkdir());

        // try-with-resources
        // https://qiita.com/NagaokaKenichi/items/124f0e14ce5dfcbd0b6b
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(getFullPath()))) {
            SecureRandom secureRandom = new SecureRandom();

            byte[] key = new byte[16];
            secureRandom.nextBytes(key);
            bos.write(key, 0, 16);

            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            bos.write(iv, 0, 16);
        }
    }

    public static void destroy() {
        if (isAvailable()) {
            String dirPath = geDirectory();
            File dir = new File(dirPath);
            String[] files = dir.list();
            if (files != null) {
                for(String file : files) {
                    LOGGER.info(new File(dir + File.separator + file).delete());
                }
            }

            LOGGER.info(dir.delete());
        }
    }

    public static Boolean isSupportOS() {
        // とりあえずWindowsに限定する
        String os = System.getProperty("os.name");
        if (!os.toLowerCase().startsWith("win")) {
            LOGGER.info(os);
            return false;
        }

        return true;
    }

    public static Boolean isAvailable() {
        return isExist();
    }

    public static String geDirectory() {
        return System.getenv("LOCALAPPDATA") +
                File.separator +
                HCUtilsMod.MOD_ID;
    }

    private static String getFullPath() {
        return geDirectory() +
                File.separator +
                HCUtilsMod.MOD_ID + EXTENSION;
    }

    private static Boolean isExist() {
        return Files.exists(Path.of(getFullPath()));
    }

    private static Cipher getCipher(int mode) throws Exception {
        if (!isExist()) {
            throw new Exception("[HCCrypt]file no exist...");
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(getFullPath()))) {
            byte[] key = new byte[16];
            byte[] iv = new byte[16];

            if ((bis.read(key) == 16) && (bis.read(iv) == 16)) {

                SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
                IvParameterSpec ivParameter = new IvParameterSpec(iv);

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(mode, secretKey, ivParameter);

                return cipher;
            }
            else {
                throw new Exception("[HCCrypt]read error...");
            }
        }
    }

    public static String encrypt(String plain) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] encryptText = cipher.doFinal(plain.getBytes());
        return Base64.getEncoder().encodeToString(encryptText);
    }

    public static String decrypt(String encrypted) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        byte[] decryptText = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(decryptText);
    }
}
