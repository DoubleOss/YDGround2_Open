package com.doubleos.yd.gui;

import com.doubleos.yd.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiFriendship extends GuiScreen
{
    float m_mouseX;

    float m_mouseY;

    GuiTextField m_textField_X;

    int m_playerTextureId = 0;

    String m_playerName = "";

    int m_score = 1;

    String str = "";

    int count = 1;

    float alpha = 0f;

    float alphaBack = 0f;
    Minecraft minecraft = Minecraft.getMinecraft();

    public GuiFriendship(String str, int count)
    {
        this.str = str;
        this.count = count;
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == Keyboard.KEY_ESCAPE)
        {
            this.mc.player.closeScreen();
            return;
        }

    }


    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {

        mc.player.closeScreen();
    }

    @Override
    public void initGui()
    {
        ScaledResolution scaled = new ScaledResolution(mc);

        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();


        super.initGui();

    }


    @Override
    protected void renderToolTip(ItemStack stack, int x, int y)
    {
        super.renderToolTip(stack, x, y);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);


        drawDefaultBackground();
        this.m_mouseX = mouseX;
        this.m_mouseY = mouseY;

        ScaledResolution scaled = new ScaledResolution(mc);

        float width = (float)scaled.getScaledWidth_double();
        float height = (float)scaled.getScaledHeight_double();

        float fpsCurrection = (120f / Minecraft.getDebugFPS()) * 0.5f;

        float random = (float) ((Math.random() * (10 - 5) + 5) * 0.1f);


        // 프레임 호출 될 떄마다 랜덤 값으로 알파값 증가
        float partialTicksCurrection = (0.25f + random)  * fpsCurrection;

        if(alphaBack < 100)
        {
            alphaBack += partialTicksCurrection;
            // 증가되는 알파값 ease Func 함수를 이용한 효과 연출
            alpha = easeOutCubic(alphaBack * 0.01f);
        }
        else
        {
            alpha = 1;
        }


        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/guis/winner"+count+".png"));
        drawTexture(0, 0, width, height, 0, 0, 1, 1, 1, alpha);

        //OpenGL 이용한 알파값 조절
        GlStateManager.pushMatrix();
        {
            float scaleFont = 3;
            GlStateManager.scale(scaleFont, scaleFont, 1);
            GlStateManager.translate(22/scaleFont, 25/scaleFont, 10);
            GlStateManager.color(1, 1, 1, alpha);
            minecraft.fontRenderer.drawString(str, 0, 0, -1);

        }
        GlStateManager.popMatrix();




    }

    public float easeOutCubic(float x)
    {
        return (float)(1 - Math.pow(1 - x, 3));
    }
    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();


    }

    public void drawTexture(float x, float y, double xSize, double ySize, double u, double v, double uAfter, double vAfter, float z, float alpha)
    {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder bb = t.getBuffer();
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            bb.begin(7, DefaultVertexFormats.POSITION_TEX);
            bb.pos(x, y, z).tex(u, v).endVertex();
            bb.pos(x, y + ySize, z).tex(u, vAfter).endVertex();
            bb.pos(x + xSize, y + ySize, z).tex(uAfter, vAfter).endVertex();
            bb.pos(x + xSize, y, z).tex(uAfter, v).endVertex();
            t.draw();
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
    }


}

