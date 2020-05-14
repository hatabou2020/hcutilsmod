package com.htbcraft.hcutilsmod.common;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class HCKeyBinding extends KeyBinding {
    private static final String KEY_CATEGORY = "hcutilsmod.common.key_category";

    private static final Map<Integer, KeyModifier> MODIFIER_MAP = new HashMap<Integer, KeyModifier>() {
        {
            put(0, KeyModifier.NONE);
            put(GLFW_MOD_SHIFT, KeyModifier.SHIFT);
            put(GLFW_MOD_CONTROL, KeyModifier.CONTROL);
            put(GLFW_MOD_ALT, KeyModifier.ALT);
        }
    };

    private int action;

    public HCKeyBinding(String description, int key, int modifiers, int action) {
        super(description, KeyConflictContext.UNIVERSAL, MODIFIER_MAP.get(modifiers), InputMappings.Type.KEYSYM.getOrMakeInput(key), KEY_CATEGORY);
        this.action = action;
    }

    public boolean test(int key, int modifiers, int action) {
        return (this.getKey().getKeyCode() == key) &&
                (this.getKeyModifier().equals(MODIFIER_MAP.get(modifiers))) &&
                (this.action == action);
    }
}
