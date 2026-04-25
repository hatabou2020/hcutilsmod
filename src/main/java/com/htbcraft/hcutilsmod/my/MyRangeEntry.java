package com.htbcraft.hcutilsmod.my;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class MyRangeEntry extends MyBaseEntry {
    private final ModConfigSpec.IntValue value;
    private final AbstractWidget widget;

    public MyRangeEntry(
            ModConfigSpec.IntValue value,
            Component titleLabel,
            int min,
            int max,
            int interval,
            Component unitLabel) {
        super(titleLabel);
        this.value = value;

        Options options = Minecraft.getInstance().options;

        this.widget = new OptionInstance<>(
                "",
                OptionInstance.noTooltip(),
                (l, v) -> Component.literal(String.valueOf(v)).append(" ").append(unitLabel),
                (new OptionInstance.IntRange(min / interval, max / interval)).xmap(
                        (v) -> v * interval,
                        (v) -> v / interval,
                        false
                ),
                this.value.get(),
                this.value::set
        )
        .createButton(options, 0, 0, ITEM_WIDTH);
    }

    @Override
    public void extractContent(@NonNull GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1, boolean b, float v) {
        super.extractContent(guiGraphicsExtractor, i, i1, b, v);

        int x = getItemX();
        int y = getY() + getItemY();
        this.widget.setPosition(x, y);
        this.widget.extractRenderState(guiGraphicsExtractor, i, i1, v);
    }

    @Override
    public @NonNull List<? extends GuiEventListener> children() {
        return List.of(this.widget);
    }
}
