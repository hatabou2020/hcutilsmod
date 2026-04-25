package com.htbcraft.hcutilsmod.my;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class MyCenterTitleOnlyEntry extends MyBaseEntry {
    public MyCenterTitleOnlyEntry(Component titleLabel) {
        super(titleLabel);
    }

    @Override
    public void extractContent(@NonNull GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1, boolean b, float v) {
        int x = (getRowWidth() / 2) - (Minecraft.getInstance().font.width(this.title.getMessage()) / 2);
        int y = this.getY() + ((getRowHeight() - Minecraft.getInstance().font.lineHeight) / 2);
        this.title.setPosition(x, y);
        this.title.extractRenderState(guiGraphicsExtractor, i, i1, v);
    }
}
