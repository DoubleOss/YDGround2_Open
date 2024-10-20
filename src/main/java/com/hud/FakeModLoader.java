package com.hud;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

public class FakeModLoader implements IFMLLoadingPlugin {
    public static File modFile;

    public String[] getASMTransformerClass()
    {
        System.out.println("getASMTransformerClass");
        return new String[] { ChnageClass.class.getName() };
    }

    public String getModContainerClass() {
        return FakeMod.class.getName();
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        modFile = (File)data.get("coremodLocation");
    }

    public String getAccessTransformerClass() {
        return null;
    }
}
