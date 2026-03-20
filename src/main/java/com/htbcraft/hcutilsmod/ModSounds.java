package com.htbcraft.hcutilsmod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, HCUtilsMod.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BED_CHIME =
            SOUNDS.register("bed.chime", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HCUtilsMod.MODID, "bed.chime")));
}
