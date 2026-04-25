package com.htbcraft.hcutilsmod.my;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class MyStringEntry extends MyBaseEntry {
    private final ModConfigSpec.ConfigValue<String> value;
    private final EditBox editBox;

    public MyStringEntry(
            ModConfigSpec.ConfigValue<String> value,
            Component titleLabel,
            int maxLength,
            String defaultText) {
        super(titleLabel);
        this.value = value;

        this.editBox = new EditBox(
                Minecraft.getInstance().font,
                ITEM_WIDTH,
                ITEM_HEIGHT,
                Component.empty());
        this.editBox.setMaxLength(maxLength);
        this.editBox.setValue(defaultText);
        this.editBox.setResponder(this.value::set);
    }

    @Override
    public void extractContent(@NonNull GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1, boolean b, float v) {
        super.extractContent(guiGraphicsExtractor, i, i1, b, v);

        int x = getItemX();
        int y = getY() + getItemY();
        this.editBox.setPosition(x, y);
        this.editBox.extractRenderState(guiGraphicsExtractor, i, i1, v);
    }

    @Override
    public @NonNull List<? extends GuiEventListener> children() {
        return List.of(this.editBox);
    }

    public ModConfigSpec.ConfigValue<String> getValue() {
        return value;
    }
}
