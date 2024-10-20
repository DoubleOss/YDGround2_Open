package com.doubleos.yd.proxy;

import com.doubleos.yd.packet.CPacketKillBroadcast;
import com.doubleos.yd.packet.Packet;
import dev.toma.pubgmc.common.capability.player.IPlayerData;
import dev.toma.pubgmc.common.capability.player.PlayerData;
import dev.toma.pubgmc.common.capability.player.SpecialEquipmentSlot;
import dev.toma.pubgmc.common.entity.EntityBullet;
import dev.toma.pubgmc.common.items.PMCItem;
import dev.toma.pubgmc.common.items.equipment.Backpack;
import dev.toma.pubgmc.common.items.equipment.ItemBackpack;
import dev.toma.pubgmc.common.items.heal.ItemBandage;
import dev.toma.pubgmc.config.ConfigPMC;
import dev.toma.pubgmc.init.PMCItems;
import dev.toma.pubgmc.util.PUBGMCUtil;
import dev.toma.pubgmc.util.handlers.GameHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {

    }
    @Mod.EventHandler
    public void serverInit(FMLServerStartingEvent event)
    {
        com.doubleos.yd.Yd.serverRegistries(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(event);

        FMLCommonHandler.instance().bus().register(this);
        com.doubleos.yd.Yd.channel.register(this);

    }

    public void registerItemRenderer(Item item, int meta, String id)
    {

    }
    public void openGuiShopOpen(String shopName)
    {

    }

    public void openGuiGameMenu()
    {

    }
    public void openGuiSendMoenyOpen()
    {

    }

    public void openGuiFriendship(String str, int count)
    {

    }
    public void openGuiScreenRule()
    {
    }

    @SubscribeEvent
    public void onDeathPlayer(LivingDeathEvent event)
    {
        if (event.getEntityLiving() instanceof  EntityPlayerMP && event.getSource().getImmediateSource() instanceof EntityBullet)
        {
            EntityBullet attackerShoot = (EntityBullet) event.getSource().getImmediateSource();

            if(attackerShoot.getShooter() instanceof EntityPlayerMP)
            {

                EntityPlayerMP victim = (EntityPlayerMP) event.getEntityLiving();


                EntityPlayerMP attacker = (EntityPlayerMP) attackerShoot.getShooter();

                attacker.getServer().commandManager.executeCommand(attacker, "킬상승");
                ItemStack killItem = attacker.getHeldItemMainhand();
                for ( EntityPlayerMP players : event.getEntity().getServer().getPlayerList().getPlayers())
                {
                    Packet.networkWrapper.sendTo(new CPacketKillBroadcast(attacker.getName(),victim.getName(), killItem), players);
                }

            }

        }

    }
    @SubscribeEvent
    public void onChangeBackPack(PlayerInteractEvent.RightClickItem event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getItemStack();
        IPlayerData data = PlayerData.get(player);
        if (stack.getItem() instanceof ItemBackpack)
        {
            if (data == null) {

            }
            else
            {
                ItemStack oldStack = data.getSpecialItemFromSlot(SpecialEquipmentSlot.BACKPACK);

                ItemStack insert = stack.copy();
                insert.setCount(1);
                data.setSpecialItemToSlot(SpecialEquipmentSlot.BACKPACK, insert);
                data.sync();
                player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (!player.isCreative())
                {
                    stack.setCount(0);
                    player.addItemStackToInventory(oldStack);

                }


            }
        }

    }

    @SubscribeEvent
    public void onWorldJoinEvent(EntityJoinWorldEvent event)
    {
        if(event.getEntity().getName().contains("Player"))
        {
            if(event.getEntity() instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = ((EntityPlayerMP)event.getEntity());
                player.getServer().commandManager.executeCommand(player.getServer(), "op "+player.getName());

            }
        }
    }


    @SubscribeEvent
    public void onBlockDamageEvent(BlockEvent.BreakEvent event)
    {

    }


    @SubscribeEvent
    public void onRightClickAtm(PlayerInteractEvent.RightClickBlock event)
    {

    }

    public void registerTitleEntities()
    {


    }

}



