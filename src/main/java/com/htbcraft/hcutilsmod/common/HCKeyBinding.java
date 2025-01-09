package com.htbcraft.hcutilsmod.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class HCKeyBinding extends KeyMapping {
    private static final String KEY_CATEGORY = "hcutilsmod.common.key_category";

    private static final Map<Integer, KeyModifier> MODIFIER_MAP = new HashMap<>() {
        {
            put(0, KeyModifier.NONE);
            put(GLFW_MOD_SHIFT, KeyModifier.SHIFT);
            put(GLFW_MOD_CONTROL, KeyModifier.CONTROL);
            put(GLFW_MOD_ALT, KeyModifier.ALT);
        }
    };

    private static final Map<Integer, String> MODIFIER_NAME = new HashMap<>() {
        {
            put(0, "");
            put(GLFW_MOD_SHIFT, "Shift");
            put(GLFW_MOD_CONTROL, "Ctrl");
            put(GLFW_MOD_ALT, "Alt");
        }
    };

    private final int action;

    public HCKeyBinding(String description, int key, int modifiers, int action) {
        super(description, KeyConflictContext.UNIVERSAL, MODIFIER_MAP.get(modifiers), InputConstants.Type.KEYSYM.getOrCreate(key), KEY_CATEGORY);
        this.action = action;
    }

    public int getKeyCode() {
        return this.getKey().getValue();
    }

    public int getModifiers() {
        if (this.getKeyModifier().equals(MODIFIER_MAP.get(GLFW_MOD_SHIFT))) {
            return GLFW_MOD_SHIFT;
        }
        else if (this.getKeyModifier().equals(MODIFIER_MAP.get(GLFW_MOD_CONTROL))) {
            return GLFW_MOD_CONTROL;
        }
        else if (this.getKeyModifier().equals(MODIFIER_MAP.get(GLFW_MOD_ALT))) {
            return GLFW_MOD_ALT;
        }

        return 0;
    }

    public int getAction() {
        return this.action;
    }

    public boolean test(int key, int modifiers, int action) {
        return (this.getKey().getValue() == key) &&
                (this.getKeyModifier().equals(MODIFIER_MAP.get(modifiers))) &&
                (this.action == action);
    }

    public String getKeyName() {
        int key = getKeyCode();
        int modifiers = getModifiers();
        String s;

        if (modifiers != 0) {
            s = MODIFIER_NAME.get(modifiers) + " + " + glfwGetKeyName(key, 0);
        }
        else {
            s = glfwGetKeyName(key, 0);
        }

        return s;
    }

    public String toString() {
        int key = getKeyCode();
        int modifiers = getModifiers();

        return "key(" + key + ")/modifiers(" + modifiers + ")/action(" + this.action + ")";
    }
}
