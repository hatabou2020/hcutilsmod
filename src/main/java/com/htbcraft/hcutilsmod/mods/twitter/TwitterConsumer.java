package com.htbcraft.hcutilsmod.mods.twitter;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface TwitterConsumer extends Library {
    TwitterConsumer INSTANCE = Native.loadLibrary("/jna/TwitterConsumer.dll", TwitterConsumer.class);

    String getKey();
    String getSecret();
}
