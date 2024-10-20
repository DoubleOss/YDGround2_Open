package com.doubleos.yd.util;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ImageLoad
{

    private ImageLoad() {

    }

    private static class InnerInstanceGameVariableClazz {
        private static final ImageLoad uniqueGameVariable = new ImageLoad();
    }


    public static ImageLoad Instance() {
        return InnerInstanceGameVariableClazz.uniqueGameVariable;
    }


    public ITextureObject inventory;

    public void init()
    {

    }





}
