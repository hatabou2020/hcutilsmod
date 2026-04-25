package com.htbcraft.hcutilsmod.config;

import com.htbcraft.hcutilsmod.my.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HcUtilsModOptionList extends MyOptionList {
    public HcUtilsModOptionList(Screen parent, int width, int height, int y) {
        super(parent, width, height, y);

        addEntry(new MyBooleanEntry(
                Config.COORDS_ENABLE,
                Component.translatable("hcutilsmod.configuration.coords"),
                Component.translatable("hcutilsmod.configuration.coords.enable"),
                Component.translatable("hcutilsmod.configuration.coords.disable")));

        addEntry(new MyBooleanEntry(
                Config.BED_CHIME_ENABLE,
                Component.translatable("hcutilsmod.configuration.bedChime"),
                Component.translatable("hcutilsmod.configuration.bedChime.enable"),
                Component.translatable("hcutilsmod.configuration.bedChime.disable")));

        addEntry(new MyEnumEntry<>(
                Config.SORT_TYPE,
                Config.SortType.class,
                Component.translatable("hcutilsmod.configuration.sortType"),
                Config.SortType.getLabels()));

        addEntry(new MyBooleanEntry(
                Config.AUTO_REPLACE_ITEM_ENABLE,
                Component.translatable("hcutilsmod.configuration.autoReplaceItem"),
                Component.translatable("hcutilsmod.configuration.autoReplaceItem.enable"),
                Component.translatable("hcutilsmod.configuration.autoReplaceItem.disable")));

        addEntry(new MyCenterTitleOnlyEntry(
                Component.translatable("hcutilsmod.configuration.findSpawner.title")));

        addEntry(new MyBooleanEntry(
                Config.FIND_SPAWNER_ENABLE,
                Component.translatable("hcutilsmod.configuration.findSpawner"),
                Component.translatable("hcutilsmod.configuration.findSpawner.enable"),
                Component.translatable("hcutilsmod.configuration.findSpawner.disable")));

        addEntry(new MyRangeEntry(
                Config.FIND_SPAWNER_RANGE,
                Component.translatable("hcutilsmod.configuration.findSpawner.range"),
                Config.FIND_SPAWNER_RANGE_MIN,
                Config.FIND_SPAWNER_RANGE_MAX,
                Config.FIND_SPAWNER_RANGE_INTERVAL,
                Component.translatable("hcutilsmod.configuration.findSpawner.blocks")));

        addEntry(new MyRangeEntry(
                Config.FIND_SPAWNER_TIME,
                Component.translatable("hcutilsmod.configuration.findSpawner.time"),
                Config.FIND_SPAWNER_TIME_MIN,
                Config.FIND_SPAWNER_TIME_MAX,
                Config.FIND_SPAWNER_TIME_INTERVAL,
                Component.translatable("hcutilsmod.configuration.findSpawner.seconds")));
    }
}
