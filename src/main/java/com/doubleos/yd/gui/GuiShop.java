package com.doubleos.yd.gui;

import com.doubleos.yd.gui.button.BTNShopItem;
import com.doubleos.yd.gui.button.BTNShopPage;
import com.doubleos.yd.util.Reference;
import com.doubleos.yd.util.ShopItemData;
import com.doubleos.yd.util.Variable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiShop extends GuiScreen
{

    String m_shopName = "";

    Minecraft minecraft = Minecraft.getMinecraft();

    Variable variable = Variable.Instance();

    int m_page = 1;


    int m_buttonDraw = 8;
    int m_buttonStartDraw = 0;

    boolean m_clickActive = false;

    ItemStack m_stack;

    int m_clickSlotListNumber = 9999;




    public GuiShop(String shopName)
    {
        m_shopName = shopName;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {

        if(button.id >= 0 && button.id <= 99)
        {
            if(button.id >= m_buttonStartDraw && button.id < m_buttonDraw )
            {
                m_clickActive = true;
                m_stack = variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(button.id).m_stack;
                m_clickSlotListNumber = button.id;
                System.out.println(button.id);
            }

        }
        if(button.id == 101)
        {
            if (m_page <= 1)
            {
                m_page += 1;
                m_buttonDraw += 8;
                if(m_buttonStartDraw == 0)
                    m_buttonStartDraw = 8;
                else
                    m_buttonStartDraw += 8;
            }
        }
        else if (button.id == 100)
        {
            if (m_page > 1)
            {
                m_page -= 1;
                m_buttonDraw -= 8;
                if(m_page == 1)
                    m_buttonStartDraw = 0;
                else
                    m_buttonStartDraw -= 8;
            }
        }
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


        int xPos[] = new int[]{223-15, 265-13, 308-10, 350-6, 248-13, 290-10, 334-8, 377-5};
        int yPos[] = new int[]{151, 203};

        int yCount = 0;
        int xCount = 0;

        for(int i = 0; i < variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.size(); i++)
        {
            xCount++;
            if(i % 4 == 0)
                yCount = 1;

            if(i%8 == 0)
            {
                xCount = 0;
                yCount = 0;
            }
            buttonList.add(new BTNShopItem(btnId, xPos[xCount], yPos[yCount], 32, "", variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(btnId).m_stack));
            btnId++;
        }

        /*
        buttonList.add(new BTNShopItem(btnId++, 223, 153, 32, "", variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(btnId).m_stack));
        buttonList.add(new BTNShopItem(btnId++, 265, 153, 32, "", variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(btnId).m_stack));
        buttonList.add(new BTNShopItem(btnId++, 308, 153, 32, "", variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(btnId).m_stack));
        buttonList.add(new BTNShopItem(btnId++, 350, 153, 32, "", variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(btnId).m_stack));

        buttonList.add(new BTNShopItem(btnId++, 248, 203, 32, "", variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(btnId).m_stack));
        buttonList.add(new BTNShopItem(btnId++, 290, 203, 32, "", variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(btnId).m_stack));
        buttonList.add(new BTNShopItem(btnId++, 334, 203, 32, "", variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(btnId).m_stack));
        buttonList.add(new BTNShopItem(btnId++, 377, 203, 32, "", variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(btnId).m_stack));
         */


        buttonList.add(new BTNShopPage(100, 254, 248, 12,12, "", "right"));
        buttonList.add(new BTNShopPage(101, 327, 248, 12,12, "", "left"));

        buttonList.add(new BTNShopPage(200, 483, 253, 27,27, "", "buy"));
        buttonList.add(new BTNShopPage(201, 508, 253, 27,27, "", "sell"));

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

        ScaledResolution scaled = new ScaledResolution(mc);

        float width = (float)scaled.getScaledWidth_double();
        float height = (float)scaled.getScaledHeight_double();

        float menuBackgroundWidth = 289;
        float menuBackgroundHeight = 39;


        if(!m_clickActive)
        {
            minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/guis/shop/background.png"));
            drawTexture(0, 0, scaled.getScaledWidth_double(), scaled.getScaledHeight_double(), 0, 0, 1, 1, 1 ,1);
        }
        else
        {
            minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/guis/shop/background_active.png"));
            drawTexture(0, 0, scaled.getScaledWidth_double(), scaled.getScaledHeight_double(), 0, 0, 1, 1, 1 ,1);

            ShopItemData data = variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.get(m_clickSlotListNumber);
            drawStringScaleResizeByMiddleWidth(data.m_stack.getDisplayName(), width/2 + 190, height/2 - 73, 5, 1, 1);

            GlStateManager.pushMatrix();
            {
                GlStateManager.scale(1.8f, 1.8f, 1.8f);
                renderHotbarItem((width/2 + 176)/1.8f, (height/2 - 35)/1.8f, partialTicks, mc.player, data.m_stack);
            }
            GlStateManager.popMatrix();

            String buyprice = data.m_buyPrice + " 벨";
            String sellprice = data.m_sellPrice + " 벨";;

            drawStringScaleResizeByMiddleWidth(buyprice, width/2 + 192, height/2 + 37, 5, 1, -1);
            drawStringScaleResizeByMiddleWidth(sellprice, width/2 + 192, height/2 + 60, 5, 1, -1);
            for(int i = 0; i<buttonList.size(); i++)
            {
                if(buttonList.get(i).id == 200 || buttonList.get(i).id == 201)
                {
                    buttonList.get(i).drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }


        }



        for(int i = m_buttonStartDraw; i < m_buttonDraw; i++)
        {
            if(buttonList.size() > i)
            {
                if(buttonList.get(i).id <= variable.m_shopNameToShopData.get(m_shopName).m_shopItemDataList.size())
                {
                    buttonList.get(i).drawButton(mc, mouseX, mouseY, partialTicks);
                }

            }

        }
        for(int i = 0; i<buttonList.size(); i++)
        {
            if(buttonList.get(i).id == 100 || buttonList.get(i).id == 101)
            {
                buttonList.get(i).drawButton(mc, mouseX, mouseY, partialTicks);
            }
        }

    }


    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();


    }
    protected void renderHotbarItem(float p_184044_1_, float p_184044_2_, float p_184044_3_, EntityPlayer player, ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            float f = (float)stack.getAnimationsToGo() - p_184044_3_;

            if (f > 0.0F)
            {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                GlStateManager.translate((float)(p_184044_1_ + 8), (float)(p_184044_2_ + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float)(-(p_184044_1_ + 8)), (float)(-(p_184044_2_ + 12)), 0.0F);
            }
            RenderHelper.enableGUIStandardItemLighting();
            minecraft.getRenderItem().renderItemAndEffectIntoGUI(player, stack, (int) p_184044_1_, (int) p_184044_2_);

            if (f > 0.0F)
            {
                GlStateManager.popMatrix();
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 1);
            minecraft.getRenderItem().renderItemOverlays(minecraft.fontRenderer, stack, (int) p_184044_1_, (int) p_184044_2_);
            GlStateManager.popMatrix();

        }
    }
    public void drawStringScaleResizeByMiddleWidth(String text, float x, float y, float depth, float scale, int color)
    {
        ScaledResolution scaled = new ScaledResolution(minecraft);

        float width = (float)scaled.getScaledWidth_double();
        float height = (float)scaled.getScaledHeight_double();

        int stringSize = minecraft.fontRenderer.getStringWidth(text);

        GlStateManager.pushMatrix();
        {
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(0f, 0f, depth);
            minecraft.fontRenderer.drawString(text, (x - stringSize/2)/scale, (y)/scale, color, false);
        }
        GlStateManager.popMatrix();
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
