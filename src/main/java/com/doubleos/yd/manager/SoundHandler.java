package com.doubleos.yd.manager;

import com.doubleos.yd.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler
{

    public static SoundEvent READY, START, WHISTLE;


    public static void registerSounds()
    {
        READY = registerSound("songs.ready");
        START = registerSound("songs.start");
        WHISTLE = registerSound("songs.whistle");
    }

    static SoundEvent registerSound(String name)
    {
        ResourceLocation location = new ResourceLocation(Reference.MODID, name);
        SoundEvent sound = new SoundEvent(location);
        sound.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(sound);
        return sound;

    }

}
