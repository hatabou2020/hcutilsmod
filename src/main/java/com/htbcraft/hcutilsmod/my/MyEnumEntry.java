package com.htbcraft.hcutilsmod.my;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class MyEnumEntry<T extends Enum<T>> extends MyBaseEntry {
    private final ModConfigSpec.EnumValue<T> value;
    private final Button button;
    private final List<Component> labels;

    public MyEnumEntry(
            ModConfigSpec.EnumValue<T> value,
            Class<T> enumClass,
            Component titleLabel,
            List<Component> labels) {
        super(titleLabel);
        this.value = value;
        this.labels = labels;

        this.button = Button.builder(
                this.labels.get(this.value.get().ordinal()),
                (btn) -> {
                    int index = this.value.get().ordinal() + 1;
                    if (enumClass.getEnumConstants().length <= index) {
                        index = 0;
                    }
                    this.value.set(enumClass.getEnumConstants()[index]);
                    btn.setMessage(this.labels.get(this.value.get().ordinal()));
                })
                .size(ITEM_WIDTH, ITEM_HEIGHT)
                .build();
    }

    @Override
    public void extractContent(@NonNull GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1, boolean b, float v) {
        super.extractContent(guiGraphicsExtractor, i, i1, b, v);

        int x = getItemX();
        int y = getY() + getItemY();
        this.button.setPosition(x, y);
        this.button.extractRenderState(guiGraphicsExtractor, i, i1, v);
    }

    @Override
    public @NonNull List<? extends GuiEventListener> children() {
        return List.of(this.button);
    }
}
