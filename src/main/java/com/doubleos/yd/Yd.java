package com.doubleos.yd;

import com.doubleos.yd.command.EntityCommand;
import com.doubleos.yd.container.InventoryContainer;
import com.doubleos.yd.handler.GuiHandler;
import com.doubleos.yd.handler.HudConfig;
import com.doubleos.yd.init.ModItems;
import com.doubleos.yd.packet.Packet;
import com.doubleos.yd.proxy.CommonProxy;
import com.doubleos.yd.util.Reference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Yd
{

    @Mod.Instance
    public static com.doubleos.yd.Yd instance;


    private static Logger logger;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;


    public static final FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("Animals");

    public boolean useBlueFirework = false;

    public boolean useWhiteFirework = false;

    public boolean blueBallRemove = false;
    public boolean whiteBallRemove = false;

    public boolean healAll = false;
    public boolean healBlue = false;
    public boolean healWhite = false;


    public InventoryContainer inventoryContainer;




    public int whitePosX = 0;
    public int whitePosY = 0;
    public int whitePosZ = 0;

    public int bluePosX = 0;
    public int bluePosY = 0;
    public int bluePosZ = 0;





    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
        logger = event.getModLog();
        int entityId = 0;
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        proxy.registerTitleEntities();
        Packet.init();

        HudConfig.init(event.getModConfigurationDirectory());
        //FMLProxyPacket fmlProxyPacket = new FMLProxyPacket(CPacketCustomPayload)
        //init();

    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        //SoundHandler.registerSounds();
        proxy.init(event);
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

    }



    void init()
    {

        try {
            FileWriter usFw = new FileWriter("F:\\마인크래프트 코딩\\마인크래프트 모딩\\개인연합\\동물의 숲\\parseJson\\en_us.lang");

            for(Item item: ModItems.ITEMS)
            {
                String itemName = item.getRegistryName().getResourcePath();
                usFw.write("item."+itemName+".name="+itemName+"\r\n");

            }
            usFw.write("itemGroup.Animals=Animals");
            usFw.write("itemGroup.OceanFish=OceanFish");
            usFw.write("itemGroup.FreshWater=FreshWater Fish");

            usFw.flush();
            usFw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try
        {
            FileWriter koFw = new FileWriter("F:\\마인크래프트 코딩\\마인크래프트 모딩\\개인연합\\동물의 숲\\parseJson\\ko_kr.lang");
            int i = 0;
            for(Item item: ModItems.ITEMS)
            {
                String itemName = item.getRegistryName().getResourcePath();
                koFw.write("item."+itemName+".name="+ModItems.itemKoreaName.get(i)+"\r\n");
                i++;
            }
            koFw.write("itemGroup.Animals=동물의 숲");
            koFw.write("itemGroup.OceanFish=바다 물고기");
            koFw.write("itemGroup.FreshWater=민 물고기");
            koFw.flush();
            koFw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(Item item: ModItems.ITEMS)
        {
            String itemName = item.getRegistryName().getResourcePath();
            System.out.println(itemName);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = new JsonObject();
            JsonObject jsonObject2 = new JsonObject();

            jsonObject.addProperty("parent", "item/generated");

            jsonObject2.addProperty("layer0", Reference.MODID+":items/"+itemName);

            jsonObject.add("textures", jsonObject2);

            System.out.println(jsonObject.toString());
            try {
                FileWriter fw = new FileWriter("F:\\마인크래프트 코딩\\마인크래프트 모딩\\개인연합\\동물의 숲\\parseJson\\"+itemName+".json");
                gson.toJson(jsonObject, fw);
                fw.flush();
                fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        }

    }


    @EventHandler
    public static void serverRegistries(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new EntityCommand());
    }
}
