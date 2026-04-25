package com.htbcraft.hcutilsmod.config;

import com.htbcraft.hcutilsmod.HcUtilsMod;
import com.htbcraft.hcutilsmod.my.MySettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HcUtilsModSettingsScreen extends MySettingsScreen {
    public HcUtilsModSettingsScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected void init() {
        // ヘッダ
        LinearLayout header = addToHeader(LinearLayout.vertical().spacing(8));
        header.addChild(
                new StringWidget(
                        Component.translatable("hcutilsmod.configuration.title"),
                        this.font),
                LayoutSettings::alignHorizontallyCenter);

        // 設定
        HcUtilsModOptionList optList = new HcUtilsModOptionList(
                getParent(),
                this.width,
                this.height - (HEADER_HEIGHT + FOOTER_HEIGHT),
                HEADER_HEIGHT);
        addRenderableWidget(optList);

        // フッタ
        GridLayout footerLayout = new GridLayout();
        footerLayout.defaultCellSetting().alignHorizontallyCenter();
        GridLayout.RowHelper footerLayoutHelper = footerLayout.createRowHelper(2);

        footerLayoutHelper.addChild(
                Button.builder(
                        Component.translatable("hcutilsmod.configuration.done"),
                        (var1) -> {
                            HcUtilsMod.LOGGER.info("Push Done");
                            Config.SPEC.save();
                            Minecraft.getInstance().setScreen(getParent());
                        })
                        .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                        .build());
        addToFooter(footerLayout);

        super.init();
    }
}
