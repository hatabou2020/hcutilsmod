package com.htbcraft.hcutilsmod.my;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class MyBooleanEntry extends MyBaseEntry {
    private final ModConfigSpec.BooleanValue value;
    private final Button button;
    private final Component trueLabel;
    private final Component falseLabel;

    public MyBooleanEntry(
            ModConfigSpec.BooleanValue value,
            Component titleLabel,
            Component trueLabel,
            Component falseLabel) {
        super(titleLabel);
        this.value = value;
        this.trueLabel = trueLabel;
        this.falseLabel = falseLabel;

        this.button = Button.builder(
                this.value.get() ? this.trueLabel : this.falseLabel,
                (btn) -> {
                    this.value.set(!this.value.get());
                    btn.setMessage(this.value.get() ? this.trueLabel : this.falseLabel);
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
