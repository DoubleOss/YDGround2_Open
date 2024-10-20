package com.doubleos.yd.gui;

import com.doubleos.yd.gui.button.BTNGameMenu;
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

public class GuiGameMenu extends GuiScreen
{
    float m_mouseX;

    float m_mouseY;

    GuiTextField m_textField_X;

    int m_playerTextureId = 0;

    String m_playerName = "";

    int m_score = 1;

    Minecraft minecraft = Minecraft.getMinecraft();

    public GuiGameMenu(){}

    public GuiGameMenu(String playerName)
    {
        m_playerName = playerName;
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
        if (button.id == 0)
        {
            mc.player.sendChatMessage("/animal 이모션창");
        }
        else if (button.id == 1)
        {
            mc.player.sendChatMessage("/animal 호감도창");
        }
        else if (button.id == 2)
        {
            mc.player.sendChatMessage("/animal 퍼즐창");
        }
        else if (button.id == 3)
        {
            mc.player.sendChatMessage("/animal 퀘스트창");
        }
        mc.player.closeScreen();
    }

    @Override
    public void initGui()
    {
        ScaledResolution scaled = new ScaledResolution(mc);

        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();

        buttonList.clear();
        int btnId = 0;
        int btn_width = 57;

        int btn_yPos = 217;

        buttonList.clear();

        buttonList.add(new BTNGameMenu(btnId++, 204, 144, btn_width, "", "emotion"));
        buttonList.add(new BTNGameMenu(btnId++, 262, 154, btn_width, "", "friendship"));
        buttonList.add(new BTNGameMenu(btnId++, 323, 144, btn_width, "", "diy"));
        buttonList.add(new BTNGameMenu(btnId++, 381, 154, btn_width, "", "quest"));

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

        float menuBackgroundWidth = 289;
        float menuBackgroundHeight = 39;


        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/guis/menu/background.png"));
        drawTexture(width/2 - menuBackgroundWidth/2, height/2-menuBackgroundHeight/2, menuBackgroundWidth, menuBackgroundHeight, 0, 0, 1, 1, 1 ,1);


        for(int i = 0; i<buttonList.size(); i++)
        {
            buttonList.get(i).drawButton(mc, mouseX, mouseY, partialTicks);
        }

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
