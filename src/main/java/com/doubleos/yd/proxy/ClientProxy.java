package com.doubleos.yd.proxy;


import com.doubleos.yd.gui.*;
import com.doubleos.yd.manager.SoundHandler;
import com.doubleos.yd.packet.Packet;
import com.doubleos.yd.packet.SPacketSendHealth;
import com.doubleos.yd.packet.SPacketSendPingPos;
import com.doubleos.yd.packet.SPacketTeleport;
import com.doubleos.yd.util.*;
import dev.toma.pubgmc.init.PMCSounds;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.*;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.nio.charset.Charset;
import java.text.NumberFormat;


public class ClientProxy extends CommonProxy
{

    int m_count = 3;

    int m_tickCheck = 0;

    boolean m_activeCount = false;

    boolean m_isStop = false;

    int playerTextureId = 0;

    public ITextureObject inventory;


    boolean fristLoadingCheck = false;
    boolean m_mapView = false;
    boolean m_hudTimerActive = true;

    int m_keyInputDelay = 0;


    float beeHitCount = 0;
    float beeRenderCount = 0;

    float m_mouseX = 0;

    float m_mouseY = 0;



    Variable variable = Variable.Instance();


    NumberFormat numberFormat = NumberFormat.getNumberInstance();



    KeyBinding m_hudBellActiveKeyInput;
    KeyBinding m_hudTimerActiveKeyInput;


    Minecraft minecraft = Minecraft.getMinecraft();
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        OBJLoader.INSTANCE.addDomain(Reference.MODID);

    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(event);
        ClientRegistry.registerKeyBinding(m_hudBellActiveKeyInput = new KeyBinding("현재 맵 표시", 35, "YD 그라운드"));
        ClientRegistry.registerKeyBinding(m_hudTimerActiveKeyInput = new KeyBinding("낙하산 펼치기", 33, "YD 그라운드"));




    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if(m_hudBellActiveKeyInput.isPressed())
        {
            if(!m_mapView)
                m_mapView = true;
            else
                m_mapView = false;
        }
        else if(m_hudTimerActiveKeyInput.isPressed())
        {
            if(m_keyInputDelay<=0)
            {
                minecraft.player.sendChatMessage("/낙하산펼치기");
                m_keyInputDelay = 40;
            }

        }
    }
    @Override
    public void openGuiShopOpen(String shopName)
    {
        minecraft.player.playSound(PMCSounds.chute_open, 1.0F, 1.0F);
    }
    @Override
    public void openGuiGameMenu()
    {
        minecraft.displayGuiScreen(new Dialog(0, 3, "review_"));
    }
    @Override
    public void openGuiSendMoenyOpen()
    {
        minecraft.displayGuiScreen(new GuiMoneySend());
    }

    public void openGuiFriendship(String str, int count)
    {
        minecraft.displayGuiScreen(new GuiFriendship(str, count));
    }

    @SubscribeEvent
    public void onMainMenuButtonClick(GuiOpenEvent event)
    {
        /*
                if (event.getGui() instanceof GuiMultiplayer)
        {
            event.setCanceled(true);
            //ServerData serverdata = new ServerData("루태데이", "double-server.asuscomm.com:25566", false);
            ServerData serverdata = new ServerData("루태데이", "127.0.0.1", false);

            net.minecraftforge.fml.client.FMLClientHandler.instance().connectToServer(minecraft.currentScreen, serverdata);
        }
         */

        /*
            else if(event.getGui() instanceof GuiIngameMenu)
        {
            event.setCanceled(true);
            minecraft.displayGuiScreen(new GuiInGameMainMenu());
        }

         */




    }

    @SubscribeEvent
    public void onMainMenuButtonClick(GuiScreenEvent.DrawScreenEvent event)
    {

    }

    @SubscribeEvent
    public void onClientWorldJoinFristLoader(EntityJoinWorldEvent event)
    {

    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event)
    {

        Variable variable = Variable.Instance();
        int i = 0;
        boolean check = false;
        for(i = 0; i<variable.m_teamNames.size(); i++)
        {
            if(event.getEntity().getName().equals(variable.m_teamNames.get(i).m_name))
            {
                check = true;
            }
        }
        if(check)
        {
            GlStateManager.pushMatrix();
            {
                if (!event.getEntityPlayer().isGlowing())
                {
                    event.getEntityPlayer().setGlowing(true);
                }


            }
            GlStateManager.popMatrix();

        }
        else
        {
            if (event.getEntityPlayer().isGlowing())
            {
                event.getEntityPlayer().setGlowing(false);
            }
        }
        if(event.getEntityPlayer().isSpectator())
        {
            event.setCanceled(true);
        }


    }
    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event)
    {
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);

        if(Mouse.getEventButton() == 2)
        {

        }
        else
        {
            if(m_mapView)
            {
                if(Mouse.getEventButton() == 1)
                {
                    //맵 핑 찍는 로직
                    m_mouseX = Mouse.getX() / scaledResolution.getScaleFactor(); // 마우스 클릭 x 좌표 에  마인크래프트 UI 스케일 보정값 나누기
                    m_mouseY = scaledResolution.getScaledHeight() - Mouse.getY() / scaledResolution.getScaleFactor();;
//                    System.out.println(m_mouseX + "  " + m_mouseY);
                    
                    /*
                    동작 로직 -> 클라이언트에서 핑 찍을 시 서버측으로 핑 좌표값, 핑찍은 자신의 ID, 스쿼드 멤버 ID 값 전송
                    서버측에서 패킷 확인 후 해당 스쿼드 멤버에게 핑 데이터 전송
                     */
                    //핑 찍은 위치 서버측을 전송 + 전송할 스쿼드 멤버에게
                    for(int i = 0; i<variable.m_teamNames.size(); i++)
                    {
                        Packet.networkWrapper.sendToServer(new SPacketSendPingPos(minecraft.player.getName(), variable.m_teamNames.get(i).m_name, m_mouseX, m_mouseY));
                    }

                }
            }
        }

    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {

        if (minecraft.getIntegratedServer() != null) //싱글플레이
        {
            //minecraft.shutdown();
        }
        if(minecraft.player != null)
        {
            //플레이어는 틱마다 자신의 체력 상태를 스쿼드 멤버들에게 전송
            //서버측은 패킷을 받으면 연결되어있는 스쿼드 멤버들에게 패킷 전송
            if(variable.m_teamNames.size() != 0)
            {
                for(int i = 0; i<variable.m_teamNames.size(); i++)
                {
                    Ping ping = variable.m_teamNames.get(i);
                    float per = minecraft.player.getHealth() / minecraft.player.getMaxHealth() * 100f;
                    if(!ping.m_name.equals(minecraft.player.getName()))
                    {
                        Packet.networkWrapper.sendToServer(new SPacketSendHealth(minecraft.player.getName(), ping.m_name, per));
                    }

                }

            }
        }

        if(m_keyInputDelay > 0)
            m_keyInputDelay--;

    }

    @SubscribeEvent
    public void onPlayerRenderEvent(RenderLivingEvent.Specials.Pre event)
    {
        Variable variable = Variable.Instance();

        if(!minecraft.player.isCreative())
        {
            boolean check = false;
            for(int i = 0; i<variable.m_teamNames.size(); i++)
            {
                if(event.getEntity().getName().equals(variable.m_teamNames.get(i).m_name))
                {
                    if(!variable.m_teamNames.get(i).m_Alive)
                    {
                        if (event.getEntity().isGlowing())
                        {
                            event.getEntity().setGlowing(false);
                        }
                        event.setCanceled(true);
                        return;
                    }
                    else
                        check = true;
                }
            }
            if(!check)
            {
                if(event.getEntity() instanceof  EntityPlayer)
                {
                    EntityPlayer entPlayer = (EntityPlayer) event.getEntity();
                    if(entPlayer.isSpectator())
                    {
                        event.setCanceled(true);
                    }
                    else if (!entPlayer.isSpectator() && !entPlayer.isSpectator() && !minecraft.player.isSpectator())
                    {
                        if(!check)
                        {
                            event.setCanceled(true);
                        }
                    }
                }



            }

        }
        if (minecraft.player.isSpectator())
        {

        }


    }
    @Override
    public void openGuiScreenRule()
    {
        minecraft.displayGuiScreen(new Dialog(0, 5, "rule_"));
    }



    @SubscribeEvent
    public void onCustomPacket(FMLNetworkEvent.ClientCustomPacketEvent event)
    {
        FMLProxyPacket packet = event.getPacket();
        Minecraft minecraft = Minecraft.getMinecraft();
        if (event.getPacket().channel().equals("RuTaeDay"))
        {
            String packetData = new String(ByteBufUtil.getBytes(packet.payload()), Charset.forName("UTF-8"));
            String[] list = packetData.split("_");

            if(list[0].equalsIgnoreCase("StartCount"))
            {
                m_activeCount = true;
                m_count = 3;
                minecraft.player.playSound(SoundHandler.READY, 0.8f, 1f);

            }

            if(list[0].equalsIgnoreCase("Stop"))
            {
                m_isStop = true;
                minecraft.player.playSound(SoundHandler.WHISTLE, 0.8f, 0.8f);
            }
        }
    }
    @SubscribeEvent
    public void onRenderHudMoney(RenderGameOverlayEvent event)
    {
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        float scaleWidth = (float) scaledResolution.getScaledWidth_double();
        float scaleHeight = (float) scaledResolution.getScaledHeight_double();
        float partialTick = event.getPartialTicks();

        float fpsCurrection = (120f / Minecraft.getDebugFPS()) * 1f;

        if(event.getType().equals(RenderGameOverlayEvent.ElementType.EXPERIENCE))
        {

        }
        else if(event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH))
        {
            event.setCanceled(true);
            drawHealth(scaledResolution, partialTick, fpsCurrection);
        }
        else if(event.getType().equals(RenderGameOverlayEvent.ElementType.FOOD))
        {
            event.setCanceled(true);
        }
        else if(event.getType().equals(RenderGameOverlayEvent.ElementType.ALL))
        {
            drawBroadcast(scaledResolution, partialTick, fpsCurrection);
        }
        else if(event.getType().equals(RenderGameOverlayEvent.ElementType.HOTBAR))
        {
            drawCompass(scaledResolution, partialTick, fpsCurrection);

        }
        else if(event.getType().equals(RenderGameOverlayEvent.ElementType.ARMOR))
        {
            event.setCanceled(true);

        }
        else if(event.getType().equals(RenderGameOverlayEvent.ElementType.CHAT))
        {
            event.setCanceled(true);
            GlStateManager.pushMatrix();
            {

                GlStateManager.scale(0.75f, 0.75f, 1f);
                GlStateManager.translate(0, scaleHeight/2+70, 100);
                minecraft.ingameGUI.getChatGUI().drawChat(minecraft.ingameGUI.getUpdateCounter());

            }
            GlStateManager.popMatrix();
        }



    }

    float scaleW = 0.53f;
    String calcal(int n)
    {
        int resultInt = n;
        String result = "";
        if(n > 375)
        {
            resultInt -= 375;
            result = String.valueOf(resultInt);
        }
        if(n == 375)
        {
            resultInt -= 375;
            result = String.valueOf(resultInt);
        }
        if ( n < -15)
        {
            resultInt = 345;
            result = String.valueOf(resultInt);
        }
        if (n ==-15)
        {
            resultInt = 360;
            result = String.valueOf(resultInt);
        }
        if (n==0)
        {
            result = "S";
        }
        if (n==180)
        {
            result = "N";

        }
        if (n==270)
        {
            result = "E";
        }
        if(n==90)
        {
            result = "W";
        }
        if(n == 45)
        {
            result = "SW";

        }
        if(n == 135)
        {
            result = "NW";

        }
        if(n== 225)
        {
            result = "NE";
        }
        if(n == 315)
        {
            result = "SE";
        }
        return result;
    }
    void drawCompass(ScaledResolution scale, float partialTick, float fpsCurrection)
    {
        float scaleWidth = (float) scale.getScaledWidth_double();
        float scaleHeight = (float) scale.getScaledHeight_double();


        float barBack_Width = 291f/2.5f;
        float barBack_Height = 17f/2.5f;

        float bar_Width = 287/2.5f;
        float bar_Height = 13/2.5f;

        float facing_back_width = 1896/3f;
        float facing_back_height = 116/3f;



        scaleW += 0.000015f;
        if (scaleW >= 0.4f)
            scaleW = 0.375f;
        //scaleW = 0.3855555f;
        scaleW = 0.50112f;

        float pitch = minecraft.player.getPitchYaw().y / 360;

        int yaw = (int) (minecraft.player.getPitchYaw().y % 360);

        yaw += 180;

        if(yaw >= 360)
            yaw -= 360;
        else if (yaw <= 0)
            yaw += 360;

        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/facing_back.png"));
        drawTexture(scaleWidth/2 - facing_back_width * 0.55f/2f, 0, facing_back_width * 0.55f, facing_back_height, 0, 0, 1, 1, 0, 1f);

        //상단바 E W S W 움직이는 부분
        // 이미지의 UV 값의 그릴 부분을 수정하여 이미지가 이어지도록
        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/facing.png"));
        drawTexture(scaleWidth/2 - scaleWidth*scaleW /2f, 10, scaleWidth*scaleW, 56/3f, 0.25 + pitch, 0, 0.75 + pitch, 1, 0, 1f);


        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/loc.png"));
        drawTexture(scaleWidth/2 - (12/3f)/2, 3, 12/3f, 16/3f, 0, 0, 1, 1, 0, 1);
        drawStringScaleResizeByMiddleWidth(String.valueOf(yaw) , scaleWidth/2, 30, 3, 1f, -1);

        variable.m_sur = 4;
        float per = 0;
        if(variable.m_mainTimer_sec != 0 && variable.m_mainTimer_min != 0)
        {
            per = (1 - (float)variable.m_mainTimer_sec / (float)variable.m_mainTimer_min) * 100f;
        }
        else
            per = 0;

        drawStringScaleResizeByRightWidth(String.valueOf(variable.m_money)+" 생존", scaleWidth-15, 6, 3, 1.5f, -1, true);

        if(minecraft.displayWidth == 1280 && minecraft.displayHeight == 720)
        {
            minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/bar_back.png"));
            drawTexture(scaleWidth - barBack_Width - 15f, scaleHeight-147f, barBack_Width, barBack_Height, 0, 0, 1, 1, 10, 1f);
            if(variable.m_barType.equals("blue"))
            {
                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/bar.png"));
                drawXLinearTexture(scaleWidth - bar_Width - 15f, scaleHeight-146f, bar_Width, bar_Height, per, 1, 15);
            }
            else
            {
                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/bar_red.png"));
                drawXLinearTexture(scaleWidth - bar_Width - 15f, scaleHeight-146f, bar_Width, bar_Height, per, 1, 15);
            }

        }
        else
        {
            minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/bar_back.png"));
            drawTexture(scaleWidth - barBack_Width - 5f, scaleHeight-122f, barBack_Width, barBack_Height, 0, 0, 1, 1, 10, 1f);
            if(variable.m_barType.equals("blue"))
            {
                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/bar.png"));
                drawXLinearTexture(scaleWidth - bar_Width - 5f, scaleHeight-121f, bar_Width, bar_Height, per, 1, 15);
            }
            else
            {
                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/bar_red.png"));
                drawXLinearTexture(scaleWidth - bar_Width - 5f, scaleHeight-121f, bar_Width, bar_Height, per, 1, 15);
            }

        }

        if(minecraft.displayWidth == 1280 && minecraft.displayHeight == 720)
        {
            String time = "자기장 §b축소§f까지: " + String.valueOf(variable.m_mainTimer_sec) + " 초";
            if(variable.m_barType.equals("red"))
                time = "자기장 §c고정§f까지: " + String.valueOf(variable.m_mainTimer_sec) + " 초";
            drawStringScaleResizeByMiddleWidth(time, scaleWidth - 75, 200, 20, 1f, -1);
        }
        else{
            String time = "자기장 §b축소§f까지: " + String.valueOf(variable.m_mainTimer_sec) + " 초";
            if(variable.m_barType.equals("red"))
                time = "자기장 §c고정§f까지: " + String.valueOf(variable.m_mainTimer_sec) + " 초";
            drawStringScaleResizeByMiddleWidth(time, scaleWidth - 75, 226, 20, 1f, -1);
        }


        float map_width = 800/3f;
        if(m_mapView)
        {
            if(Mouse.isGrabbed())
                Mouse.setGrabbed(false);


            minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/map_"+variable.mapNumber+".png"));
            drawTexture(scaleWidth/2 - map_width/2, scaleHeight/2 - map_width/2, map_width, map_width, 0, 0, 1, 1, 10, 1f);
            int posX = (int) minecraft.player.posX;
            int posZ = (int) minecraft.player.posZ;

            float posXValue = -posX * 0.1579f;
            float posYValue = posZ * 0.1585f;

            int widthInt = scale.getScaledWidth();
            int heightInt = scale.getScaledHeight();

            //int strSize = minecraft.fontRenderer.getStringWidth("x");
            float mapWidth = scaleWidth/2 - map_width/2;
            float mapHeight = scaleHeight/2 - map_width/2;

            float checkWidth = (widthInt/2 - 8 - posXValue) - 2;
            float checkHeight = ((heightInt/2 - 4) + posYValue);

            if(mapWidth <= checkWidth && mapWidth+map_width >= checkWidth)
            {
                if (mapHeight <= checkHeight && mapHeight + map_width >= checkHeight)
                {
                    GlStateManager.pushMatrix();
                    {
                        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/angle.png"));
                        drawRotateTexture(yaw, widthInt/2 - 8 - posXValue + 2f, ((heightInt/2 - 5) + posYValue) + 4f, 64/3f, 64/3f, 0, 0, 1, 1, 30f, 0.4f);
                        drawSkinHead((widthInt/2 - 8 - posXValue) + 2, ((heightInt/2 - 1) + posYValue), 20f, 1d, yaw+180);
                    }
                    GlStateManager.popMatrix();
                }
            }

            //MyPing
            if(variable.m_teamNames.size() == 0)
            {
                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/ping_1.png"));
                drawTexture(m_mouseX - 80f/6f/2f, m_mouseY - 80f/6f, 80f/6f, 80f/6f, 0, 0, 1, 1, 40,1);
            }
            else
            {
                //팀전일 경우 팀원들 핑 그리기
                for(int i = 0; i< variable.m_teamNames.size(); i++)
                {
                    Ping ping = variable.m_teamNames.get(i);
                    if(ping.m_PingX != -50 && ping.m_PingY != -50)
                    {
                        int count = i+1;
                        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/ping_"+count+".png"));
                        drawTexture(ping.m_PingX - 80f/6f/2f, ping.m_PingY - 80f/6f, 80f/6f, 80f/6f, 0, 0, 1, 1, 40,1);
                    }

                }

            }



            float currentBorder = variable.m_borderSize;
            float shrinkBorder = variable.m_shrinkBorderSize; //줄어드는 값


            float uiBorder = currentBorder * 0.16330275f;

            float uishrinkBorder = shrinkBorder * 0.16330275f;

            float borderMaxSize = 266.6666667f;



            int borderPosX = (int) variable.m_borderMiddlePosX;
            int borderPosY = (int) variable.m_borderMiddlePosZ;

            float borderXValue = -borderPosX * 0.1579f;
            float borderYValue = borderPosY * 0.1585f;
            
            //자기장 위치와 자기장 줄어드는 크기 그리기

            GlStateManager.pushMatrix();
            {

                drawSquare(((scaleWidth/2) - borderXValue)- 5, ((scaleHeight/2) + borderYValue), 18, uishrinkBorder, scaleWidth/2 - map_width/2, scaleWidth/2 + map_width/2, 1, 1, 1);

                drawSquare(((scaleWidth/2) - borderXValue)- 5, ((scaleHeight/2) + borderYValue), 20, uiBorder, scaleWidth/2 - map_width/2, scaleWidth/2 + map_width/2, 0.2f, 0.9f, 1.0f);


            }
            GlStateManager.popMatrix();

        }
        else
        {
            if(!Mouse.isGrabbed() && !minecraft.ingameGUI.getChatGUI().getChatOpen() && minecraft.inGameHasFocus)
                Mouse.setGrabbed(true);
        }


        String x = String.valueOf("X: " + minecraft.player.getPosition().getX());
        String y = String.valueOf("Y: " + minecraft.player.getPosition().getY());
        String z = String.valueOf("Z: " + minecraft.player.getPosition().getZ());
        minecraft.fontRenderer.drawStringWithShadow("킬수: "+variable.m_KillAmount, 1, 50, -1);
        minecraft.fontRenderer.drawStringWithShadow(x, 1, 60, -1);
        minecraft.fontRenderer.drawStringWithShadow(y, 1, 70, -1);
        minecraft.fontRenderer.drawStringWithShadow(z, 1, 80, -1);


        float healthbar_BackGround_width = 327/3f;
        float healthbar_BackGround_height = 39/3f;

        float healthbar_healthbarBack_width = 327/3f;
        float healthbar_healthbarBack_height = 15/3f;

        int count = 0;

        int yPosCurrtion = -10;

        float partyPer = 80f;


        //탈것을 타고 있으면 체력바 렌더 중지
        if(!minecraft.player.isRiding())
        {
            for(int i = 0; i<variable.m_teamNames.size(); i++)
            {
                Ping ping = variable.m_teamNames.get(i);
                GlStateManager.pushMatrix();
                {
                    int color = -1;
                    float health_per = ping.helathPer;
                    float myHealth_per = minecraft.player.getHealth() / minecraft.player.getMaxHealth() * 100f;

                    //스쿼드 멤버가 죽었을 경우 회색빛으로 처리
                    if(!ping.m_Alive)
                    {
                        color = 10395548;
                        health_per = 0;
                        myHealth_per = 0;
                    }

                    int drawCount = i+1;

                    minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/health_"+drawCount+".png"));
                    drawTexture(5, scaleHeight - healthbar_BackGround_height - (50 + 25 * (i)) - yPosCurrtion, healthbar_BackGround_width, healthbar_BackGround_height, 0, 0, 1, 1, 10,1);
                    minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/health_background.png"));
                    drawTexture(5, scaleHeight - healthbar_healthbarBack_height - (45 + 25 * (i)) - yPosCurrtion, healthbar_healthbarBack_width, healthbar_healthbarBack_height, 0, 0, 1, 1, 11,1);
                    if(minecraft.player.getName().equals(ping.m_name))
                    {
                        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/healthbar_my.png"));
                        drawXLinearTexture(5, scaleHeight - healthbar_healthbarBack_height - (45 + 25 * (i)) - yPosCurrtion, healthbar_healthbarBack_width, healthbar_healthbarBack_height, myHealth_per, 1, 12);
                    }
                    else
                    {
                        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/healthbar_team.png"));
                        drawXLinearTexture(5, scaleHeight - healthbar_healthbarBack_height - (45 + 25 * (i)) - yPosCurrtion, healthbar_healthbarBack_width, healthbar_healthbarBack_height, health_per, 1, 12);
                    }
                    GlStateManager.pushMatrix();
                    {
                        GlStateManager.translate(0, 0, 13);
                        minecraft.fontRenderer.drawString(ping.m_name, 20, (int) (scaleHeight - (61 + 25 * (i)) - yPosCurrtion), color);
                    }
                    GlStateManager.popMatrix();

                }
                GlStateManager.popMatrix();

            }

        }





    }
    private void drawSquare(float x, float y, float z, double size, float minSize, float maxSize, float r, float g, float b)
    {
        float centerX = x;
        float centerY = y;
        float halfSize = (float) (size / 2);


        //미니맵 크기에 맞춰서 중앙센터값 받아 중심 기준의 사각형 그리기
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GlStateManager.translate(0, 0, 11);
            GlStateManager.color(r, g, b, 0.8f); // 빨간색 (투명도: 0.5)

            //GL_LINE_LOOP 점을 찍어서 연결
            GlStateManager.glBegin(2);
            float left = (centerX - halfSize <= 186.5f) ? 186.5f : centerX - halfSize;
            float right = (centerX + halfSize >= 454f) ? 454f : centerX + halfSize;
            float top = (centerY - halfSize <= 46.5f) ? 46.5f : centerY - halfSize;
            float down = (centerY + halfSize >= 314f) ? 314f : centerY + halfSize;

            GlStateManager.glVertex3f(left, top,z);
            GlStateManager.glVertex3f(left, down,z);
            GlStateManager.glVertex3f(right, down,z);
            GlStateManager.glVertex3f(right, top,z);
            GlStateManager.glEnd();

            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();

        }
        GlStateManager.popMatrix();
    }

    private void drawSkinHead(float x, float y, float z, double scale, float rotate)
    {
        int yaw = (int) (minecraft.player.getPitchYaw().y % 360);

        if(yaw >= 360)
            yaw -= 360;
        else if (yaw <= 0)
            yaw += 360;
        GlStateManager.pushMatrix();
        this.minecraft.renderEngine.bindTexture(this.minecraft.player.getLocationSkin());
        GlStateManager.color(1, 1, 1);
        GlStateManager.scale(scale, scale, 1);
        x = x / (float)scale;
        y = y / (float)scale;
        drawRotateTexture2(yaw, x, y, 8f, 8f, 0.125d, 0.12d, 0.25d, 0.25d, 100, 1f);
        GlStateManager.popMatrix();
    }

    public void drawRotateTexture2(float angle, float x, float y, double xSize, double ySize, double u, double v, double uAfter, double vAfter, float z, float alpha)
    {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder bb = t.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.translate(x, y, 100);
        GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f);
        GlStateManager.scale(0.04f, 0.04f, 1f);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(u, v); GL11.glVertex2f(-100.0f, -100.0f);
        GL11.glTexCoord2d(u, vAfter); GL11.glVertex2f(-100.0f, 100.0f);
        GL11.glTexCoord2d(uAfter, vAfter); GL11.glVertex2f(100.0f, 100.0f);
        GL11.glTexCoord2d(uAfter, v); GL11.glVertex2f(100.0f, -100.0f);
        GL11.glEnd();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }
    public void drawRotateTexture(float angle, float x, float y, double xSize, double ySize, double u, double v, double uAfter, double vAfter, float z, float alpha)
    {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder bb = t.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.translate(x, y, 100);
        GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f);
        GlStateManager.scale(0.09f, 0.09f, 1f);
        GlStateManager.color(1, 1, 1, alpha);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex2f(-100.0f, -100.0f);
        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex2f(-100.0f, 100.0f);
        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex2f(100.0f, 100.0f);
        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex2f(100.0f, -100.0f);
        GL11.glEnd();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }
    void drawFood(ScaledResolution scale, float partialTick, float fpsCurrection)
    {
        float scaleWidth = (float) scale.getScaledWidth_double();
        float scaleHeight = (float) scale.getScaledHeight_double();

        float foodWidth = 101;
        float foodHeight = 11;

        float food_per = minecraft.player.getFoodStats().getFoodLevel() * 5f;

        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/hotbar/food_background.png"));
        drawTexture(scaleWidth / 2.0F - foodWidth/2f + 65, scaleHeight - foodHeight - 40, foodWidth, foodHeight, 0.0F, 0.0F, 1.0F, 1.0F, 5.0F, 1.0F);
        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/hotbar/food.png"));
        drawXLinearTexture(scaleWidth/2 - foodWidth/2 + 65f, scaleHeight -  foodHeight - 40, foodWidth, foodHeight, food_per, 1, 8);

    }
    void drawHealth(ScaledResolution scale, float partialTick, float fpsCurrection)
    {
        float scaleWidth = (float) scale.getScaledWidth_double();
        float scaleHeight = (float) scale.getScaledHeight_double();

        float healthWidth_back = 640/3.5f;
        float healthHeight_back = 41/4f;

        float healthWidth = 640/3.55f;
        float healthHeight = 30/4f;


        float health_per = minecraft.player.getHealth() / minecraft.player.getMaxHealth() * 100f;

        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/health_back.png"));
        drawTexture(scaleWidth / 2.0F - healthWidth_back/2f - 0, scaleHeight - healthHeight_back - 33, healthWidth_back, healthHeight_back, 0.0F, 0.0F, 1.0F, 1.0F, 5.0F, 1.0F);
        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/health.png"));
        drawXLinearTexture(scaleWidth/2 - healthWidth/2 - 0, scaleHeight -  healthHeight - 34.5f, healthWidth, healthHeight, health_per, 1, 8);

    }
    void drawHotbar(ScaledResolution scale, float partialTick, float fpsCurrection)
    {


        float scaleWidth = (float) scale.getScaledWidth_double();
        float scaleHeight = (float) scale.getScaledHeight_double();

        float hotbarWidth = 223;
        float hotbarHeight = 20;


        float hotbarSelectWidth = 28;
        float hotbarSelectHeight = 31;


        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/hotbar/background.png"));
        drawTexture(scaleWidth/2 - hotbarWidth/2, scaleHeight - hotbarHeight - 15, hotbarWidth, hotbarHeight, 0, 0, 1, 1, -1, 1);



        int selectSlot = minecraft.player.inventory.currentItem;

        float hotbarSelectCorrection = 0;
        if(selectSlot==1)
            hotbarSelectCorrection = 27.5f;
        else if(selectSlot==2)
            hotbarSelectCorrection = 26f;
        else if(selectSlot==3)
            hotbarSelectCorrection = 26f;
        else if(selectSlot==4)
            hotbarSelectCorrection = 25.5f;
        else if(selectSlot==5)
            hotbarSelectCorrection = 25.5f;
        else if(selectSlot==6)
            hotbarSelectCorrection = 25.5f;
        else if(selectSlot==7)
            hotbarSelectCorrection = 25.5f;
        else if(selectSlot==8)
            hotbarSelectCorrection = 25.5f;

        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/hotbar/hotbar_select.png"));
        drawTexture(scaleWidth / 2.0F - hotbarSelectWidth/2f - 103 +  selectSlot * hotbarSelectCorrection , scaleHeight - hotbarSelectHeight - 9, hotbarSelectWidth, hotbarSelectHeight, 0.0F, 0.0F, 1.0F, 1.0F, 5.0F, 1.0F);


        float yPos = 31.5f * 1f;

        for(int i = 0; i<9; i++)
        {
            if(i == 0)
            {
                renderHotbarItem((scaleWidth/2 - 109.5f), (scaleHeight - yPos), partialTick, minecraft.player, minecraft.player.inventory.mainInventory.get(i));
            }
            else if(i==1)
            {
                renderHotbarItem(scaleWidth/2 - 83.5f,scaleHeight - yPos, partialTick, minecraft.player, minecraft.player.inventory.mainInventory.get(i));
            }
            else if(i==2)
            {
                renderHotbarItem(scaleWidth/2 - 58.5f,scaleHeight - yPos, partialTick, minecraft.player, minecraft.player.inventory.mainInventory.get(i));
            }
            else if(i==3)
            {
                renderHotbarItem(scaleWidth/2 - 32.5f,scaleHeight - yPos, partialTick, minecraft.player, minecraft.player.inventory.mainInventory.get(i));
            }
            else if(i==4)
            {
                renderHotbarItem(scaleWidth/2 - 7.5f,scaleHeight - yPos, partialTick, minecraft.player, minecraft.player.inventory.mainInventory.get(i));
            }
            else if(i==5)
            {
                renderHotbarItem(scaleWidth/2 + 17.5f,scaleHeight - yPos, partialTick, minecraft.player, minecraft.player.inventory.mainInventory.get(i));
            }
            else if(i==6)
            {
                renderHotbarItem(scaleWidth/2 + 43.5f,scaleHeight - yPos, partialTick, minecraft.player, minecraft.player.inventory.mainInventory.get(i));
            }
            else if(i==7)
            {
                renderHotbarItem(scaleWidth/2 + 68.5f,scaleHeight - yPos, partialTick, minecraft.player, minecraft.player.inventory.mainInventory.get(i));
            }
            else if(i==8)
            {
                renderHotbarItem(scaleWidth/2 + 93.5f,scaleHeight - yPos, partialTick, minecraft.player, minecraft.player.inventory.mainInventory.get(i));
            }

        }

        float middleWidth = 24;

        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/hotbar/middle.png"));
        drawTexture(scaleWidth/2 - middleWidth/2, scaleHeight - middleWidth - 37, middleWidth, middleWidth, 0, 0, 1, 1, 3, 1);


    }
    void drawBell(ScaledResolution scale, float partialTick, float fpsCurrection)
    {
        float scaleWidth = (float) scale.getScaledWidth_double();
        float scaleHeight = (float) scale.getScaledHeight_double();

        float bellWidth = 116;
        float bellHeight = 45;



        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/bell/background.png"));
        drawTexture(scaleWidth-bellWidth, 0-bellHeight + 50, bellWidth, bellHeight, 0, 0, 1, 1, 2, 1);

        String replaceText = numberFormat.format(variable.m_money);

        int textSize = minecraft.fontRenderer.getStringWidth(replaceText);

        drawStringScaleResizeByMiddleWidth(replaceText, scaleWidth-65, 30, 5, 1, -1);



    }

    void drawMainTimer(ScaledResolution scale, float partialTick, float fpsCurrection)
    {

        float scaleWidth = (float) scale.getScaledWidth_double();
        float scaleHeight = (float) scale.getScaledHeight_double();

        float mainTimerWidth = 154;
        float mainTimerHeight = 46;

        float mainTimerNumber = 19;


        int sec = variable.m_mainTimer_sec;
        int sec1 = variable.m_mainTimer_sec % 10;
        int sec10 = variable.m_mainTimer_sec / 10;

        int min = variable.m_mainTimer_min;
        int min1 = variable.m_mainTimer_min % 10;
        int min10 = variable.m_mainTimer_min / 10;


        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/main_timer/timer_background.png"));
        drawTexture(0, 10, mainTimerWidth, mainTimerHeight, 0, 0, 1, 1, 2, 1);


        //sec
        {
            {
                //10자리
                {
                    minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/main_timer/"+sec10+".png"));
                    drawTexture(0 - mainTimerNumber + 84, 0 - mainTimerNumber + 42.5f, mainTimerNumber, mainTimerNumber, 0, 0, 1, 1, 2,1);
                }
                //1자리
                {
                    minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/main_timer/"+sec1+".png"));
                    drawTexture(0 - mainTimerNumber + 101, 0 - mainTimerNumber + 42.5f, mainTimerNumber, mainTimerNumber, 0, 0, 1, 1, 2,1);
                }

            }
        }

        //min
        {
            int min10Curr = 0;
            if(min10 == 0)
            {
                min10Curr = 4;
            }
            //12
            {
                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/main_timer/"+min10+".png"));
                drawTexture(0 + mainTimerNumber + 4, 0 - mainTimerNumber + 42, mainTimerNumber, mainTimerNumber, 0, 0, 1, 1, 2, 1f);

                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/main_timer/"+min1+".png"));
                drawTexture(0 + mainTimerNumber + 17 + min10Curr, 0 - mainTimerNumber + 42, mainTimerNumber, mainTimerNumber, 0, 0, 1, 1, 2, 1f);
            }
        }


    }
    void drawSubTimer(ScaledResolution scale, float partialTick, float fpsCurrection)
    {
        float scaleWidth = (float) scale.getScaledWidth_double();
        float scaleHeight = (float) scale.getScaledHeight_double();

        float mainTimerWidth = 87;
        float mainTimerHeight = 27;

        float mainTimerNumber = 13;

        int sec = variable.m_subTimer_sec;
        int sec1 = variable.m_subTimer_sec % 10;
        int sec10 = variable.m_subTimer_sec / 10;

        int min = variable.m_subTimer_min;
        int min1 = variable.m_subTimer_min % 10;
        int min10 = variable.m_subTimer_min / 10;


        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/sub_timer/background.png"));
        drawTexture(10, 65, mainTimerWidth, mainTimerHeight, 0, 0, 1, 1, 2, 1);


        //sec
        {
            {
                //10자리
                {
                    minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/sub_timer/"+sec10+".png"));
                    drawTexture(0 - mainTimerNumber + 77, 0 - mainTimerNumber + 83, mainTimerNumber, mainTimerNumber, 0, 0, 1, 1, 3,1);
                }
                //1자리
                {
                    minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/sub_timer/"+sec1+".png"));
                    drawTexture(0 - mainTimerNumber + 85, 0 - mainTimerNumber + 83f, mainTimerNumber, mainTimerNumber, 0, 0, 1, 1, 3,1);
                }

            }
        }

        //min
        {
            //12
            {
                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/sub_timer/"+min10+".png"));
                drawTexture(0 + mainTimerNumber + 27, 0 - mainTimerNumber + 83f, mainTimerNumber, mainTimerNumber, 0, 0, 1, 1, 3, 1f);

                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/sub_timer/"+min1+".png"));
                drawTexture(0 + mainTimerNumber + 36, 0 - mainTimerNumber + 83f, mainTimerNumber, mainTimerNumber, 0, 0, 1, 1, 3, 1f);
            }
        }

    }
    void drawLoading(ScaledResolution scale, float partialTick, float fpsCurrection)
    {
        float scaleWidth = (float) scale.getScaledWidth_double();
        float scaleHeight = (float) scale.getScaledHeight_double();


        if(!fristLoadingCheck)
        {
            for(int i = 0; i<16; i++)
            {
                String loadingFileName = "Loading_0000" + i+".png";
                if (i<10)
                {
                    loadingFileName = "Loading_0000" + i+".png";
                }
                else
                {
                    loadingFileName = "Loading_000" + i+".png";
                }
                TextureManager textureManager =  minecraft.getTextureManager();
                ResourceLocation location = new ResourceLocation(Reference.MODID, "textures/huds/loading/"+loadingFileName);

                ITextureObject itextureobject = textureManager.getTexture(location);

                if (itextureobject == null)
                {
                    itextureobject = new SimpleTexture(location);
                    textureManager.loadTexture(location, itextureobject);
                }
            }
            fristLoadingCheck = true;

        }
        if(variable.m_animationStateList.size() != 0) //동그라미 로딩바
        {
            AnimationState aniState = variable.m_animationStateList.get(0);

            if(aniState.m_animationOpen)
            {
                if(aniState.m_currentAnimationFrame <= aniState.m_maxAnimationFrame)
                {
                    aniState.m_currentAnimationFrame += partialTick * fpsCurrection * aniState.m_AnimationFrameTime;

                    System.out.println(aniState.m_currentAnimationFrame);
                    if(aniState.m_currentAnimationFrame > 0 && aniState.m_currentAnimationFrame <= 6.25f)
                        aniState.m_currentFrame = 14;
                    else if(aniState.m_currentAnimationFrame > 6.25f && aniState.m_currentAnimationFrame <= 12.5f)
                        aniState.m_currentFrame = 13;
                    else if(aniState.m_currentAnimationFrame > 12.5f && aniState.m_currentAnimationFrame <= 18.75f)
                        aniState.m_currentFrame = 12;
                    else if(aniState.m_currentAnimationFrame > 18.75f && aniState.m_currentAnimationFrame <= 25f)
                        aniState.m_currentFrame = 11;
                    else if(aniState.m_currentAnimationFrame > 25f && aniState.m_currentAnimationFrame <= 31.25f)
                        aniState.m_currentFrame = 10;
                    else if( aniState.m_currentAnimationFrame > 31.25f && aniState.m_currentAnimationFrame <= 37.5f)
                        aniState.m_currentFrame = 9;
                    else if(aniState.m_currentAnimationFrame > 37.5f && aniState.m_currentAnimationFrame <= 43.75f)
                        aniState.m_currentFrame = 8;
                    else if( aniState.m_currentAnimationFrame > 43.75f && aniState.m_currentAnimationFrame <= 50f)
                        aniState.m_currentFrame = 7;
                    else if(aniState.m_currentAnimationFrame > 56.25f && aniState.m_currentAnimationFrame <= 62.5f)
                        aniState.m_currentFrame = 6;
                    else if( aniState.m_currentAnimationFrame > 62.5f && aniState.m_currentAnimationFrame <= 68.75f)
                        aniState.m_currentFrame = 5;
                    else if(aniState.m_currentAnimationFrame > 68.75f && aniState.m_currentAnimationFrame <= 75f)
                        aniState.m_currentFrame = 4;
                    else if(aniState.m_currentAnimationFrame > 75f && aniState.m_currentAnimationFrame <= 81.25f)
                        aniState.m_currentFrame = 3;
                    else if( aniState.m_currentAnimationFrame > 81.25f && aniState.m_currentAnimationFrame <= 87.5)
                        aniState.m_currentFrame = 2;
                    else if(aniState.m_currentAnimationFrame > 87.5f && aniState.m_currentAnimationFrame <= 93.75f)
                        aniState.m_currentFrame = 1;
                    else if(aniState.m_currentAnimationFrame > 93.75f && aniState.m_currentAnimationFrame <= 100f)
                        aniState.m_currentFrame = 0;

                }
                else
                {
                    //System.out.println("페이드 인 종료 및 딜레이 시작 " + LocalTime.now());
                    aniState.m_animationOpen = false;
                    aniState.m_animationDelay = true;
                    Packet.networkWrapper.sendToServer(new SPacketTeleport(aniState.m_teleportXPos, aniState.m_teleportYPos, aniState.m_teleportZPos));
                }
            }
            else if(!aniState.m_animationOpen && aniState.m_animationDelay)
            {
                if(aniState.m_currentAnimationDelay < aniState.m_maxAnimationDelay)
                {
                    aniState.m_currentAnimationDelay += partialTick * fpsCurrection * aniState.m_AnimationDelayTime;
                }
                else
                {
                    aniState.m_animationDelay = false;
                    aniState.m_animationClose = true;
                    //System.out.println("페이드 딜레이 종료  클로즈 시작" + LocalTime.now());

                }
            }
            else if(!aniState.m_animationOpen && !aniState.m_animationDelay && aniState.m_animationClose)
            {
                if(aniState.m_currentAnimationFrame > 0)
                {
                    aniState.m_currentAnimationFrame -= partialTick * fpsCurrection  * aniState.m_AnimationFrameTime;

                    if(aniState.m_currentAnimationFrame > 0 && aniState.m_currentAnimationFrame <= 6.25f)
                        aniState.m_currentFrame = 14;
                    else if(aniState.m_currentAnimationFrame > 6.25f && aniState.m_currentAnimationFrame <= 12.5f)
                        aniState.m_currentFrame = 13;
                    else if( aniState.m_currentAnimationFrame > 12.5f && aniState.m_currentAnimationFrame <= 18.75f)
                        aniState.m_currentFrame = 12;
                    else if( aniState.m_currentAnimationFrame > 18.75f && aniState.m_currentAnimationFrame <= 25f)
                        aniState.m_currentFrame = 11;
                    else if(aniState.m_currentAnimationFrame > 25f && aniState.m_currentAnimationFrame <= 31.25f)
                        aniState.m_currentFrame = 10;
                    else if(aniState.m_currentAnimationFrame > 31.25f && aniState.m_currentAnimationFrame <= 37.5f)
                        aniState.m_currentFrame = 9;
                    else if(aniState.m_currentAnimationFrame > 37.5f && aniState.m_currentAnimationFrame <= 43.75f)
                        aniState.m_currentFrame = 8;
                    else if(aniState.m_currentAnimationFrame > 43.75f && aniState.m_currentAnimationFrame <= 50f)
                        aniState.m_currentFrame = 7;
                    else if( aniState.m_currentAnimationFrame > 56.25f && aniState.m_currentAnimationFrame <= 62.5f)
                        aniState.m_currentFrame = 6;
                    else if(aniState.m_currentAnimationFrame > 62.5f && aniState.m_currentAnimationFrame <= 68.75f)
                        aniState.m_currentFrame = 5;
                    else if(aniState.m_currentAnimationFrame > 68.75f && aniState.m_currentAnimationFrame <= 75f)
                        aniState.m_currentFrame = 4;
                    else if(aniState.m_currentAnimationFrame > 75f && aniState.m_currentAnimationFrame <= 81.25f)
                        aniState.m_currentFrame = 3;
                    else if(aniState.m_currentAnimationFrame > 81.25f && aniState.m_currentAnimationFrame <= 87.5)
                        aniState.m_currentFrame = 2;
                    else if(aniState.m_currentAnimationFrame > 87.5f && aniState.m_currentAnimationFrame <= 93.75f)
                        aniState.m_currentFrame = 1;
                    else if(aniState.m_currentAnimationFrame > 93.75f && aniState.m_currentAnimationFrame <= 100f)
                        aniState.m_currentFrame = 0;
                }
                else
                {
                    aniState.m_currentAnimationFrame = 0f;
                    aniState.m_animationClose = false;
                    variable.m_animationStateList.remove(0);
                    ///System.out.println("페이드 클로즈 종료 " + LocalTime.now());
                }
            }
            float alpha = aniState.m_currentAnimationFrame * 0.01f;
            GlStateManager.pushMatrix();
            {
                String loading = "";
                if (aniState.m_currentFrame<10)
                {
                    loading = "loading_0000" + aniState.m_currentFrame+".png";
                }
                else
                {
                    loading = "loading_000" + aniState.m_currentFrame+".png";
                }
                minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/loading/"+loading));
                GlStateManager.translate(0, 0, 5);
                drawTexture(0, 0, scaleWidth, scaleHeight, 0, 0, 1, 1, 50, 1);

            }
            GlStateManager.popMatrix();
        }
    }
    //킬로그
    void drawBroadcast(ScaledResolution scale, float partialTick, float fpsCurrection)
    {

        float broadCastWidth = 280f;
        float broadCastHeight = 115f;

        float boradCastYpos = 50f;

        float width = (float) scale.getScaledWidth_double();
        float height = (float) scale.getScaledHeight_double();

        float scaleWidth = (float) scale.getScaledWidth_double();
        float scaleHeight = (float) scale.getScaledHeight_double();


        float barBack_Width = 255/3f;
        float barBack_Height = 15/3f;


        if(variable.m_animationBroadcastList.size() > 0) //공지창
        {
            for(AnimationBroadCast animation : variable.m_animationBroadcastList)
            {
                if(!animation.m_animationShow)
                {
                    animation.m_animationShow = true;
                    animation.m_animationPlay = true;
                }
                else
                {
                    float aniValue = 0f;
                    float animationAlpha = 100f;

                    if(animation.m_animationOpen)
                    {
                        animation.m_currentAnimationFrame += partialTick * 0.8f * fpsCurrection;
                        animation.m_curreentAniYPosPer += partialTick * 0.8f * fpsCurrection;
                        aniValue = easeInOutSine(((animation.m_curreentAniYPosPer)) * 0.01f);
                        animationAlpha = easeOutCubic(animation.m_currentAnimationFrame * 0.01f);

                    }
                    else if (animation.m_animationDelay)
                    {
                        animation.m_curreentAniYPosPer = 100;
                        aniValue = easeInOutSine(animation.m_curreentAniYPosPer * 0.01f);
                        animation.m_currentAnimationFrame += partialTick * 0.2f * fpsCurrection;
                    }
                    else if(animation.m_animationClose)
                    {
                        animation.m_currentAnimationFrame += partialTick * 0.8f * fpsCurrection;
                        animation.m_curreentAniYPosPer -= partialTick* 0.8f * fpsCurrection;
                        animationAlpha = easeOutCubic((100f - animation.m_currentAnimationFrame) * 0.01f);
                        aniValue = easeOutCubic(((animation.m_curreentAniYPosPer)) * 0.01f);
                    }
                    float currentFrame = animation.m_currentAnimationFrame;
                    if(animation.m_maxAnimationFrame <= animation.m_currentAnimationFrame)
                    {
                        animation.m_currentAnimationFrame = 0;
                        if(animation.m_animationOpen)
                        {
                            animation.m_animationOpen = false;
                            animation.m_animationDelay = true;

                        }
                        else if(animation.m_animationDelay)
                        {
                            animation.m_animationDelay = false;
                            animation.m_animationClose = true;
                        }
                        else if(animation.m_animationClose)
                        {

                            animation.m_animationClose = false;
                            variable.m_animationBroadcastList.remove(animation);
                            return;
                        }

                    }
                    if(animation != null)
                    {
                        String killLog = animation.m_killerName + "   §c☠§f   " + animation.m_victimName;
                        int listInteger = variable.m_animationBroadcastList.indexOf(animation);
                        float strSize = minecraft.fontRenderer.getStringWidth(killLog);
                        System.out.println(strSize);
                        float strValue = strSize/ 93f;

                        animation.m_stack.setCount(1);
                        GlStateManager.pushMatrix();
                        {
                            minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/huds/bar_back.png"));
                            drawTexture(scaleWidth - barBack_Width*(strSize/92) - 4f, 30+ (listInteger * 10) - 0.5f , barBack_Width*(strSize/92), barBack_Height*1.5f, 0.2f, 0.2f, 0.8f, 0.8f, 2, 0.4f);
                            drawStringScaleResizeByLeftWidth(killLog, width - strSize + (47*strValue),  30+ (listInteger * 10), 3, 0.75f, -1);

                        }
                        GlStateManager.popMatrix();



                    }

                }
            }

        }
    }


    public void registerCustomModel(Item item, int meta)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));


    }

    public void registerItemRenderer(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));

    }

    protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");

    public float easeOutCubic(float x)
    {
        return (float)(1 - Math.pow(1 - x, 3));
    }
    public float easeInOutSine(float x)
    {
        return (float)-(Math.cos(Math.PI * x) - 1) / 2f;
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
    public void drawStringScaleResizeByRightWidth(String text, float x, float y, float depth, float scale, int color, boolean shadow)
    {
        ScaledResolution scaled = new ScaledResolution(minecraft);

        float width = (float)scaled.getScaledWidth_double();
        float height = (float)scaled.getScaledHeight_double();

        int stringSize = minecraft.fontRenderer.getStringWidth(text);

        GlStateManager.pushMatrix();
        {
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(0f, 0f, depth);
            if (shadow)
                minecraft.fontRenderer.drawStringWithShadow(text, (x - stringSize)/scale, (y)/scale, color);
            else
                minecraft.fontRenderer.drawString(text, (x - stringSize)/scale, (y)/scale, color, false);
        }
        GlStateManager.popMatrix();
    }


    public void drawStringScaleResizeByLeftWidth(String text, float x, float y, float depth, float scale, int color)
    {

        int stringSize = minecraft.fontRenderer.getStringWidth(text);
        RenderItem renderItem = minecraft.getRenderItem();
        GlStateManager.pushMatrix();
        {
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(0f, 0f, depth);
            minecraft.fontRenderer.drawString(text, (x)/scale-stringSize/2, (y)/scale, color, false);

        }
        GlStateManager.popMatrix();
    }



    public void drawCricleProgressToQuad(float x, float y, float z, float textSizeX, float textSizeY, float per, float color)
    {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bb = tessellator.getBuffer();

        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GlStateManager.enableBlend();
        bb.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.color(color, color, color, 1.0f);
        GlStateManager.pushMatrix();
        {

            bb.pos(x +textSizeX/2, y+textSizeY/2, z).tex(0.5f, 0.5f).endVertex();
            //bb.pos(x+(textSizeX*0.5), y, z).tex(0.5f, 0f).endVertex();
            if(per >= 87)
            {
                float b0 = (1-(100-per) / 13);
                bb.pos(x + (textSizeX/2)*b0, y, z).tex(0.5f*b0, 0f).endVertex();
            }
            if(per >= 62)
            {
                if(per > 62 && per <= 87)
                {
                    float b1 = (87-per) / 25;
                    bb.pos(x, (textSizeY * b1) + y, z).tex(0f, 1f* b1).endVertex();
                }
                else if(per > 87)
                {
                    bb.pos(x, y, z).tex(0f, 0f).endVertex();
                }
                bb.pos(x, y+textSizeY, z).tex(0f, 1f).endVertex();

            }
            if(per >= 38)
            {
                if(per > 38 && per <= 62)
                {
                    float b2 = (62-per) / 25;
                    bb.pos((textSizeX * b2) + x, y+textSizeY, z).tex(1f * b2, 1f).endVertex();
                }
                else if(per > 62)
                {
                    bb.pos(x, y+textSizeY, z).tex(0f, 1f).endVertex();
                }
                bb.pos(x+textSizeX, y+textSizeY, z).tex(1f, 1f).endVertex();
            }
            if(per >= 13)
            {
                if(per > 13 && per <= 38)
                {
                    float b3 = (1-((38-per) / 25));
                    bb.pos(x+textSizeX, y+textSizeY*b3, z).tex(1f, 1f*b3).endVertex();
                }
                else if(per > 38)
                {
                    //bb.pos(x+textSizeX, y+textSizeY, z).tex(1f, 1f).endVertex();
                }
                bb.pos(x+textSizeX, y, z).tex(1f, 0f).endVertex();
            }
            if(per < 13)
            {

                float b0 = (1-(13-per) / 13);
                bb.pos(x+textSizeX/2 + (textSizeX/2 * b0), y, z).tex(0.5f + (b0 * 0.5f), 0f).endVertex();
            }
            bb.pos(x+textSizeX/2, y, z).tex(0.5f, 0f).endVertex();

            //GlStateManager.disableBlend();
            tessellator.draw();

        }
        GlStateManager.popMatrix();

    }

    public void drawTexture(float x, float y, double xSize, double ySize, double u, double v, double uAfter, double vAfter, float z, float alpha, float r, float g, float b)
    {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder bb = t.getBuffer();
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(r, g, b, alpha);
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
    public void drawCricleProgress(int x, int y, float u, float v, int sizeU, int sizeV, float radius, float radiusUV,
                                   float progress, float initialDegree, int segment, boolean clockwise) {

        float a = 360F / segment;
        float b = 0;

        double degree, sin, cos;
        double osin = 0;
        double ocos = 0;


        float offsetU = 1F / sizeU;
        float offsetV = 1F / sizeV;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bb = tessellator.getBuffer();
        bb.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);

        GlStateManager.pushMatrix();
        {
            for (int i = 0; i <= segment; ++i)
            {
                b = a * i;
                if (b < progress)
                    degree = (initialDegree + b) * Math.PI / 180D;
                else
                    degree = (initialDegree + progress) * Math.PI / 180D;

                if (clockwise)
                {
                    sin = Math.sin(-degree);
                    cos = Math.cos(-degree);

                    bb.pos((double) x + (osin * radius), (double) y + (ocos * radius), 0d).tex(((double) (u + (osin * radiusUV)) * offsetU), (double) ((v + (ocos * radiusUV)) * offsetV)).endVertex();
                    bb.pos(x, y, 0d).tex(u * offsetU, v * offsetV).endVertex();;
                    bb.pos((double) x + (sin * radius), (double) y + (cos * radius), 0d).tex((u + (sin * radiusUV)) * offsetU, (v + (cos * radiusUV)) * offsetV).endVertex();;
                    osin = sin;
                    ocos = cos;
                }
                else
                {
                    sin = Math.sin(degree);
                    cos = Math.cos(degree);

                    bb.pos(x + (sin * radius), y + (cos * radius), 0d).tex((u + (sin * radiusUV)) * offsetU, (v + (cos * radiusUV)) * offsetV).endVertex();;
                    bb.pos(x, y, 0d).tex(u * offsetU, v * offsetV).endVertex();;
                    bb.pos(x + (osin * radius), y + (ocos * radius), 0d).tex(((u + (osin * radiusUV)) * offsetU), (v + (ocos * radiusUV)) * offsetV).endVertex();;

                    osin = sin;
                    ocos = cos;
                }

                if (b > progress) break;
            }
            tessellator.draw();
        }
        GlStateManager.popMatrix();
    }
    public void drawTexture(float x, float y, double xSize, double ySize, double u, double v, double uAfter, double vAfter, float z, float alpha)
    {
        Tessellator t = Tessellator.getInstance();
        BufferBuilder bb = t.getBuffer();
        GlStateManager.pushMatrix();
        {
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
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

    private void drawYLinearTexture(float x, float y, float xSize, float ySize, float endV, float alpha, float z)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(x, y + (ySize*endV), z).tex(0.0d,endV).endVertex();
            bufferbuilder.pos(x, y+ySize, z).tex(0.0d,1.0d).endVertex();
            bufferbuilder.pos(x+ xSize, y+ySize, z).tex(1.0d,1.0d).endVertex();
            bufferbuilder.pos(x+ xSize, y+ (ySize*endV), z).tex(1.0d,endV).endVertex();

            //bufferbuilder.pos(x, y + ySize * endU, zLevel).tex(1.0d, endU).endVertex();
            //bufferbuilder.pos(x, y + ySize * endU, zLevel).tex(0.0d, endU).endVertex();

            tessellator.draw();
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
    }

    private void drawXLinearTexture(float x, float y, float xSize, float ySize, float endU, float alpha, float z)
    {
        endU = endU * 0.01f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);

            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(x, y, z).tex(0.0d,0.0d).endVertex();
            bufferbuilder.pos(x, y+ySize, z).tex(0.0d,1.0d).endVertex();
            bufferbuilder.pos(x+ xSize * endU, y+ySize, z).tex(endU,1.0d).endVertex();
            bufferbuilder.pos(x+ xSize * endU, y, z).tex(endU,0.0d).endVertex();

            tessellator.draw();
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
    }

}
