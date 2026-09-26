package com.htbcraft.hcutilsmod.config;

import com.htbcraft.hcutilsmod.HcUtilsMod;
import com.htbcraft.hcutilsmod.common.MinecraftColor;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final KeyMapping.Category KEY_CATEGORY = new KeyMapping.Category(Identifier.withDefaultNamespace(HcUtilsMod.MODID));

    public enum SortType {
        NAME("hcutilsmod.configuration.sortType.name"),         // 名前順
        CATEGORY("hcutilsmod.configuration.sortType.category"); // カテゴリ順

        private final Component label;

        SortType(String key) {
            this.label = Component.translatable(key);
        }

        public static List<Component> getLabels() {
            ArrayList<Component> labels = new ArrayList<>();
            for (SortType e : values()) {
                labels.add(e.label);
            }
            return labels;
        }
    }

    public static final int FIND_SPAWNER_RANGE_INTERVAL = 16;
    public static final int FIND_SPAWNER_RANGE_MIN = FIND_SPAWNER_RANGE_INTERVAL;
    public static final int FIND_SPAWNER_RANGE_MAX = 4 * FIND_SPAWNER_RANGE_INTERVAL;
    public static final int FIND_SPAWNER_RANGE_DEF = FIND_SPAWNER_RANGE_INTERVAL;

    public static final int FIND_SPAWNER_TIME_INTERVAL = 10;
    public static final int FIND_SPAWNER_TIME_MIN = FIND_SPAWNER_TIME_INTERVAL;
    public static final int FIND_SPAWNER_TIME_MAX = 6 * FIND_SPAWNER_TIME_INTERVAL;
    public static final int FIND_SPAWNER_TIME_DEF = 3 * FIND_SPAWNER_TIME_INTERVAL;

    public static final int BRIGHTNESS_RANGE_INTERVAL = 1;
    public static final int BRIGHTNESS_RANGE_MIN = 1;
    public static final int BRIGHTNESS_RANGE_MAX = 16;
    public static final int BRIGHTNESS_RANGE_DEF = 16;

    public static final int BRIGHTNESS_THRESHOLD_INTERVAL = 1;
    public static final int BRIGHTNESS_THRESHOLD_MIN = 0;
    public static final int BRIGHTNESS_THRESHOLD_MAX = 14;
    public static final int BRIGHTNESS_THRESHOLD_DEF = 0;

    public static final int BRIGHTNESS_ALPHA_INTERVAL = 1;
    public static final int BRIGHTNESS_ALPHA_MIN = 1;
    public static final int BRIGHTNESS_ALPHA_MAX = 255;
    public static final int BRIGHTNESS_ALPHA_DEF = 128;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue COORDS_ENABLE = BUILDER
            .define("coords", false);

    public static final ModConfigSpec.BooleanValue BED_CHIME_ENABLE = BUILDER
            .define("bedChime", false);

    public static final ModConfigSpec.EnumValue<SortType> SORT_TYPE = BUILDER
            .defineEnum("sortType", SortType.NAME);

    public static final ModConfigSpec.BooleanValue AUTO_REPLACE_ITEM_ENABLE = BUILDER
            .define("autoReplaceItem", false);

    public static final ModConfigSpec.BooleanValue FIND_SPAWNER_ENABLE = BUILDER
            .define("findSpawner", false);

    public static final ModConfigSpec.IntValue FIND_SPAWNER_RANGE = BUILDER
            .defineInRange("findSpawnerRange", FIND_SPAWNER_RANGE_DEF, FIND_SPAWNER_RANGE_MIN, FIND_SPAWNER_RANGE_MAX);

    public static final ModConfigSpec.IntValue FIND_SPAWNER_TIME = BUILDER
            .defineInRange("findSpawnerTime", FIND_SPAWNER_TIME_DEF, FIND_SPAWNER_TIME_MIN, FIND_SPAWNER_TIME_MAX);

    public static final ModConfigSpec.IntValue BRIGHTNESS_RANGE = BUILDER
            .defineInRange("brightnessRange", BRIGHTNESS_RANGE_DEF, BRIGHTNESS_RANGE_MIN, BRIGHTNESS_RANGE_MAX);

    public static final ModConfigSpec.IntValue BRIGHTNESS_THRESHOLD = BUILDER
            .defineInRange("brightnessThreshold", BRIGHTNESS_THRESHOLD_DEF, BRIGHTNESS_THRESHOLD_MIN, BRIGHTNESS_THRESHOLD_MAX);

    public static final ModConfigSpec.BooleanValue BRIGHTNESS_ZOMBIE = BUILDER
            .define("brightnessZombie", true);

    public static final ModConfigSpec.EnumValue<MinecraftColor> BRIGHTNESS_COLOR = BUILDER
            .defineEnum("brightnessColor", MinecraftColor.RED);

    public static final ModConfigSpec.IntValue BRIGHTNESS_ALPHA = BUILDER
            .defineInRange("brightnessAlpha", BRIGHTNESS_ALPHA_DEF, BRIGHTNESS_ALPHA_MIN, BRIGHTNESS_ALPHA_MAX);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
