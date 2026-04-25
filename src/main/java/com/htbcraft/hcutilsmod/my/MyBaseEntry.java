package com.htbcraft.hcutilsmod.my;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class MyBaseEntry extends MyOptionList.Entry {
    protected static final int ITEM_WIDTH = 140;    // ボタンの幅
    protected static final int ITEM_HEIGHT = 20;    // ボタンの高さ

    protected final StringWidget title;

    public MyBaseEntry(Component titleLabel) {
        this.title = new StringWidget(
                titleLabel, Minecraft.getInstance().font);
    }

    @Override
    public @NonNull List<? extends NarratableEntry> narratables() {
        return List.of();
    }

    @Override
    public void extractContent(@NonNull GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1, boolean b, float v) {
        int x = (getRowWidth() / 2) - (Minecraft.getInstance().font.width(this.title.getMessage()));
        int y = this.getY() + ((getRowHeight() - Minecraft.getInstance().font.lineHeight) / 2);
        this.title.setPosition(x, y);
        this.title.extractRenderState(guiGraphicsExtractor, i, i1, v);
    }

    @Override
    public @NonNull List<? extends GuiEventListener> children() {
        return List.of();
    }

    protected int getItemX() {
        return (getRowWidth() / 2) + 30;
    }

    protected int getItemY() {
        return (getRowHeight() - ITEM_HEIGHT) / 2;
    }
}
