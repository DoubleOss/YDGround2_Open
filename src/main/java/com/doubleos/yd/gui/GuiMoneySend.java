package com.doubleos.yd.gui;

import com.doubleos.yd.gui.button.BTNSend;
import com.doubleos.yd.gui.button.BtnMoneySendMember;
import com.doubleos.yd.util.ImageBuffer;
import com.doubleos.yd.util.Reference;
import com.doubleos.yd.util.Variable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;

public class GuiMoneySend extends GuiScreen
{

    float m_mouseX;

    float m_mouseY;

    GuiTextField m_textField_X;

    int m_playerTextureId = 0;

    String m_playerName = "";

    int m_score = 1;

    int textureId = 0;

    Minecraft minecraft = Minecraft.getMinecraft();

    String m_clickPlayerName = "";


    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public GuiMoneySend(){}

    public GuiMoneySend(String playerName)
    {
        m_playerName = playerName;
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        m_textField_X.setFocused(true);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        Variable variable = Variable.Instance();
        if (keyCode == Keyboard.KEY_ESCAPE)
        {
            this.mc.player.closeScreen();
            return;
        }

        if(m_textField_X.isFocused())
        {

            if(keyCode == 14 || keyCode >= 2 && keyCode <=11 || keyCode >= 71 && keyCode <= 82)
            {
                m_textField_X.textboxKeyTyped(typedChar, keyCode);

                if(!m_textField_X.getText().isEmpty())
                {
                    if(m_textField_X.getText().isEmpty())
                    {
                        m_textField_X.setText("");
                    }
                    else
                    {

                        String replaceText = m_textField_X.getText().replaceAll(",", "");
                        double money = Double.parseDouble(replaceText);
                        if( variable.m_money < money)
                            money = variable.m_money;

                        replaceText = numberFormat.format(money);
                        m_textField_X.setText(replaceText);
                    }
                }
            }
        }

    }


    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if(button.id >= 1  && button.id <= 8)
        {
            BtnMoneySendMember moneyBtn = (BtnMoneySendMember)button;
            m_clickPlayerName = moneyBtn.m_BtnName;
        }
        else if(button.id >= 0)
        {
            if(m_textField_X.getText().length()>= 1)
            {
                int money = Integer.parseInt(m_textField_X.getText().replaceAll(",", ""));
                minecraft.player.sendChatMessage("/송금 " +money+ " " + m_clickPlayerName);
                minecraft.player.closeScreen();
            }

        }

    }

    @Override
    public void initGui()
    {
        Variable variable = Variable.Instance();

        ScaledResolution scaled = new ScaledResolution(mc);

        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();

        buttonList.clear();
        int btnId = 0;
        int btn_width = 42;
        int btn_hegiht = 42;

        int btn_yPos = 171;


        buttonList.add(new BTNSend(btnId++, 303, btn_yPos, btn_width, btn_hegiht, "", "button"));//송금

        buttonList.add(new BtnMoneySendMember(btnId++, 87, 259, 40, 40, "","d7297"));
        buttonList.add(new BtnMoneySendMember(btnId++, 148, 259, 40, 40, "","samsik23"));
        buttonList.add(new BtnMoneySendMember(btnId++, 208, 259, 40, 40, "","Seoneng"));
        buttonList.add(new BtnMoneySendMember(btnId++, 269, 259, 40, 40, "","Huchu95"));
        buttonList.add(new BtnMoneySendMember(btnId++, 331, 259, 40, 40, "","Noonkkob"));
        buttonList.add(new BtnMoneySendMember(btnId++, 391, 259, 40, 40, "","KonG7"));
        buttonList.add(new BtnMoneySendMember(btnId++, 453, 259, 40, 40, "","Daju_"));
        buttonList.add(new BtnMoneySendMember(btnId++, 513, 259, 40, 40, "","RuTaeY"));


        m_textField_X = new GuiTextField(0, fontRenderer, (int) ((width/2-152)/1.6f), (int) ((height/2-46)/1.6f), 220 ,9);

        Keyboard.enableRepeatEvents(true);

        m_textField_X.setMaxStringLength(18);
        m_textField_X.setEnableBackgroundDrawing(false);
        m_textField_X.setVisible(true);
        m_textField_X.setFocused(true);
        m_textField_X.setTextColor(16777215);
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
        //super.drawScreen(mouseX, mouseY, partialTicks);
        drawDefaultBackground();
        this.m_mouseX = mouseX;
        this.m_mouseY = mouseY;

        ScaledResolution scaled = new ScaledResolution(mc);

        float width = (float)scaled.getScaledWidth_double();
        float height = (float)scaled.getScaledHeight_double();

        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/guis/send/background.png"));
        drawTexture(0, 0, width, height, 0, 0, 1, 1, 1 ,1);


        if(!m_clickPlayerName.isEmpty())
        {
            BufferedImage image = ImageBuffer.m_nameToBufferImage.get(m_clickPlayerName);
            textureId = new DynamicTexture(image).getGlTextureId();
            GlStateManager.bindTexture(textureId);
            drawTexture(width/2 - 76/2 + 130, height/2 - 76/2 - 21, 76,76, 0, 0, 1, 1, 1, 1);

        }

        for(int i = 0; i<buttonList.size(); i++)
        {
            buttonList.get(i).drawButton(mc, mouseX, mouseY, partialTicks);
        }

        GlStateManager.pushMatrix();
        {
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.translate(0,0,10);
            GlStateManager.scale(1.6f, 1.6f,1.6f);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0f);
            m_textField_X.drawTextBox();
            m_textField_X.updateCursorCounter();
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();

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
