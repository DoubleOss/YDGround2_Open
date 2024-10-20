package com.doubleos.yd.gui;



import com.doubleos.yd.container.InventoryContainer;
import com.doubleos.yd.util.BlurTexture;
import com.doubleos.yd.util.ImageLoad;
import com.doubleos.yd.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiInventory extends GuiContainer
{

    float m_mouseX;

    float m_mouseY;
    InventoryContainer m_inventoryContainer;


    private float oldMouseX;
    private float oldMouseY;


    ITextureObject inventory;

    public GuiInventory(EntityPlayer player, InventoryPlayer inventoryPlayer)
    {
        super(new InventoryContainer(inventoryPlayer, player));
        m_inventoryContainer = com.doubleos.yd.Yd.instance.inventoryContainer;

        inventory = Minecraft.getMinecraft().getTextureManager().getTexture(new ResourceLocation(Reference.MODID, "textures/guis/inventory/background.png"));
        if(inventory == null) {
            inventory = new BlurTexture(new ResourceLocation(Reference.MODID, "textures/guis/inventory/background.png"));
            Minecraft.getMinecraft().getTextureManager().loadTexture(new ResourceLocation(Reference.MODID, "textures/guis/inventory/background.png"), inventory);
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.m_mouseX = mouseX;
        this.m_mouseY = mouseY;
        renderHoveredToolTip(mouseX, mouseY);
        this.oldMouseX = (float)mouseX;
        this.oldMouseY = (float)mouseY;


    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {

        ScaledResolution scaled = new ScaledResolution(mc);

        float width = (float)scaled.getScaledWidth();
        float height = (float)scaled.getScaledHeight();
        //sendChatMessage(String.format("x: %d, y: %d", width, height));
        //sendChatMessage(String.valueOf(width));
        //sendChatMessage(String.valueOf(height));

        //640, 360
        //xPos = -240 ~ 400 오른쪽끝 45 75 104 133 160
        //Ypos = 맨위 171 144 117,89, 61, 33, 6



    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        drawDefaultBackground();
        ScaledResolution scale = new ScaledResolution(mc);

        ImageLoad imageLoad = ImageLoad.Instance();

        float width = (float) scale.getScaledWidth_double();
        float height = (float) scale.getScaledHeight_double();

        float rescalePosition = height / 360f;

        int i = this.guiLeft;
        int j = this.guiTop;

        drawEntityOnScreen((int) (width/2) - 27, (int) (height/2) - 50, 32, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 40) - this.oldMouseY, mc.player);

        GlStateManager.pushMatrix();
        {
            //this.mc.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/guis/inventory/inventory.png"));
            GlStateManager.bindTexture(inventory.getGlTextureId());
            drawTexture(0, 0, width, height, 0, 0, 1d, 1d, 1, 1);
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

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

}
