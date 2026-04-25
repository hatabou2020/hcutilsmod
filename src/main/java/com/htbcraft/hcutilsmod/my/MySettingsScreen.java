package com.htbcraft.hcutilsmod.my;

import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MySettingsScreen extends Screen {
    protected static final int HEADER_HEIGHT = 30;    // ヘッダの高さ
    protected static final int FOOTER_HEIGHT = 35;    // フッタの高さ
    protected static final int BUTTON_WIDTH = 100;    // ボタンの幅
    protected static final int BUTTON_HEIGHT = 20;    // ボタンの高さ

    private final Screen parent;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, HEADER_HEIGHT, FOOTER_HEIGHT);

    protected MySettingsScreen(Screen parent) {
        super(Component.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected <T extends net.minecraft.client.gui.layouts.LayoutElement> T addToHeader(T child) {
        return this.layout.addToHeader(child);
    }

    protected <T extends net.minecraft.client.gui.layouts.LayoutElement> T addToContents(T child) {
        return this.layout.addToContents(child);
    }

    protected <T extends net.minecraft.client.gui.layouts.LayoutElement> T addToFooter(T child) {
        return this.layout.addToFooter(child);
    }

    public Screen getParent() {
        return this.parent;
    }
}
