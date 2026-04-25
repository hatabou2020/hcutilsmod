package com.htbcraft.hcutilsmod.common;

import com.htbcraft.hcutilsmod.HcUtilsMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, HcUtilsMod.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BED_CHIME =
            SOUNDS.register("bed.chime",
                    () -> SoundEvent.createVariableRangeEvent(
                            Identifier.fromNamespaceAndPath(HcUtilsMod.MODID, "bed.chime")));
}
