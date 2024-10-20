package com.doubleos.yd.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientUtil
{

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onClientTick(TickEvent.WorldTickEvent event)
    {
        System.out.println("DELAY");

        /*
        System.out.println(event.phase.toString());
        if(tick == 80)
        {
            System.out.print("DELAY");
            tick = 0;
        }
        tick++;

         */
    }

    @SideOnly(Side.CLIENT)
    public static void renderStack(final ItemStack stack, final World world)
    {
        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

    }

    @SideOnly(Side.CLIENT)
    public static void renderModel(final IBakedModel model, final ResourceLocation texture) {
        renderModelWithColor(model, -1, texture);
    }

    @SideOnly(Side.CLIENT)
    public static void renderModelWithColor(final IBakedModel model, final int color, final ResourceLocation texture) {
        GlStateManager.pushMatrix();

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);


        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);

        for (final EnumFacing enumfacing : EnumFacing.values()) {
            renderQuadsColor(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color);
            //System.out.println(enumfacing.toString());
        }

        renderQuadsColor(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color);
        tessellator.draw();

        GlStateManager.popMatrix();
    }



    @SideOnly(Side.CLIENT)
    private static void renderQuadsColor(final BufferBuilder bufferbuilder, final List<BakedQuad> quads, int color) {

        int i = 0;
        for (final int j = quads.size(); i < j; ++i) {
            final BakedQuad bakedquad = quads.get(i);

            if ((color == -1) && bakedquad.hasTintIndex()) {
                if (EntityRenderer.anaglyphEnable) {
                    color = TextureUtil.anaglyphColor(color);
                }

                color = color | -0x1000000;
            }
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(bufferbuilder, bakedquad, color);
        }
    }


}