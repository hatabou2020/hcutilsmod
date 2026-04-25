package com.htbcraft.hcutilsmod.my;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;

public class MyOptionList extends ContainerObjectSelectionList<MyOptionList.Entry> {
    protected static final int ROW_HEIGHT = 26;

    protected final Screen parent;

    public MyOptionList(Screen parent, int width, int height, int y) {
        super(Minecraft.getInstance(), width, height, y, ROW_HEIGHT);
        this.parent = parent;
    }

    @Override
    public int getRowWidth() {
        return this.getWidth();
    }

    @Override
    protected int scrollBarX() {
        return getRowWidth() - this.scrollbarWidth();
    }

    public Screen getParent() {
        return parent;
    }

    public abstract static class Entry extends ContainerObjectSelectionList.Entry<MyOptionList.Entry> {
        protected int getRowWidth() {
            return this.getWidth();
        }
        protected int getRowHeight() {
            return ROW_HEIGHT;
        }
    }
}
