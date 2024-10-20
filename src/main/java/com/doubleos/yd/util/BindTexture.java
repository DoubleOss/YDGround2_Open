package com.doubleos.yd.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public class BindTexture
{
    public static void bindTexture(ResourceLocation resource)
    {
        ITextureObject textureObj = Minecraft.getMinecraft().getTextureManager().getTexture(resource);
        if(textureObj == null) {
            textureObj = new BlurTexture(resource);
            Minecraft.getMinecraft().getTextureManager().loadTexture(resource, textureObj);
        }
        GlStateManager.bindTexture(textureObj.getGlTextureId());
    }
}
