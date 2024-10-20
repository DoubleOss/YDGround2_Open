package com.hud;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
public class FakeMod extends DummyModContainer
{
    public FakeMod() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "ClassChange";
        meta.name = "DoubleOs ClassChangeMod";
        meta.version = "1.12.2";
        meta.description = "Minecraft Class Chanage";
        meta.credits = "DoubleOs";
        meta.url = "";
        meta.authorList = Arrays.asList(new String[] { "" });
        meta.updateUrl = "";
        meta.screenshots = new String[0];
        meta.logoFile = "";
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void modConstruction(FMLConstructionEvent evt) {}

    @Subscribe
    public void init(FMLInitializationEvent evt) {}

    @Subscribe
    public void preInit(FMLPreInitializationEvent evt) {}

    @Subscribe
    public void postInit(FMLPostInitializationEvent evt) {}


}
