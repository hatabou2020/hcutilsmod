package com.htbcraft.hcutilsmod.my;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.util.HashMap;
import java.util.Map;

public class MyKeyBinding extends KeyMapping {
    private static final Map<Integer, KeyModifier> MODIFIER_MAP = new HashMap<>() {
        {
            put(0, KeyModifier.NONE);
            put(InputConstants.MOD_SHIFT, KeyModifier.SHIFT);
            put(InputConstants.MOD_CONTROL, KeyModifier.CONTROL);
            put(InputConstants.MOD_ALT, KeyModifier.ALT);
        }
    };

    private static final Map<Integer, String> MODIFIER_NAME = new HashMap<>() {
        {
            put(0, "");
            put(InputConstants.MOD_SHIFT, "Shift");
            put(InputConstants.MOD_CONTROL, "Ctrl");
            put(InputConstants.MOD_ALT, "Alt");
        }
    };

    private final int action;

    public MyKeyBinding(Category category, String description, int key, int modifiers, int action) {
        super(description,
                KeyConflictContext.UNIVERSAL,
                MODIFIER_MAP.get(modifiers),
                InputConstants.Type.KEYBOARD.getOrCreate(key),
                category);
        this.action = action;
    }

    public int getKeyCode() {
        return this.getKey().getValue();
    }

    public int getModifiers() {
        if (this.getKeyModifier().equals(KeyModifier.SHIFT)) {
            return InputConstants.MOD_SHIFT;
        }
        else if (this.getKeyModifier().equals(KeyModifier.CONTROL)) {
            return InputConstants.MOD_CONTROL;
        }
        else if (this.getKeyModifier().equals(KeyModifier.ALT)) {
            return InputConstants.MOD_ALT;
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
        int modifiers = getModifiers();
        String s;

        if (modifiers != 0) {
            s = MODIFIER_NAME.get(modifiers) + " + " + this.getKey().getDisplayName().getString();
        }
        else {
            s = this.getKey().getDisplayName().getString();
        }

        return s;
    }

    public String toString() {
        int key = getKeyCode();
        int modifiers = getModifiers();

        return "key(" + key + ")/modifiers(" + modifiers + ")/action(" + this.action + ")";
    }
}
