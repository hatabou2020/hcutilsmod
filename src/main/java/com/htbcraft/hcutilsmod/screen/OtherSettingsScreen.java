package com.htbcraft.hcutilsmod.screen;

import com.htbcraft.hcutilsmod.common.HCSettings;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OtherSettingsScreen extends SettingsScreen {
    public OtherSettingsScreen(Screen parent) {
        super(parent, Component.translatable("hcutilsmod.settings.other.title"));
    }

    protected void init() {
        super.init();

        // 就寝可タイミング通知のオンオフ
        this.addRenderableWidget(
                Button.builder(
                    getBedChimeText(),
                    (var1) -> {
                        HCSettings.getInstance().enableBedChime = !HCSettings.getInstance().enableBedChime;
                        var1.setMessage(getBedChimeText());
                    })
                .pos(getPosX() + (getWidth() - 180) / 2, getPosY() + 30)
                .size(180, 20)
                .build());

        // 戻る
        this.addRenderableWidget(
                Button.builder(
                    Component.translatable("hcutilsmod.settings.other.return"),
                    (var1) -> this.getMinecraft().setScreen(this.getParent()))
                .pos(getPosX() + (getWidth() - 100) / 2, getPosY() + 55)
                .size(100, 20)
                .build());
    }

    private Component getBedChimeText() {
        if (HCSettings.getInstance().enableBedChime) {
            return Component.translatable("hcutilsmod.settings.other.bed.chime.on");
        }
        else {
            return Component.translatable("hcutilsmod.settings.other.bed.chime.off");
        }
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }   // この画面はESCをはじく
}
