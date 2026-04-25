package com.htbcraft.hcutilsmod.config;

import com.htbcraft.hcutilsmod.HcUtilsMod;
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

    public static final ModConfigSpec SPEC = BUILDER.build();
}
