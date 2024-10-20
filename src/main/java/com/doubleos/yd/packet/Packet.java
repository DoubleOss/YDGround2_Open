package com.doubleos.yd.packet;


import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Packet
{
    public static SimpleNetworkWrapper networkWrapper;

    public static void init()
    {
        int packetId = 0;
        networkWrapper = NetworkRegistry. INSTANCE.newSimpleChannel("Rutae2");
        networkWrapper.registerMessage(CPacketGameMenu.class, CPacketGameMenu.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(SPacketClientOpenInventory.class, SPacketClientOpenInventory.class, packetId++, Side.SERVER);
        networkWrapper.registerMessage(CPacketOpenRule.class, CPacketOpenRule.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketFadeEffectAdd.class, CPacketFadeEffectAdd.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketBalloonArrowHit.class, CPacketBalloonArrowHit.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketShopOpen.class, CPacketShopOpen.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketOpenSendMoney.class, CPacketOpenSendMoney.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketFriendShipOpen.class, CPacketFriendShipOpen.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketBeeHitActive.class, CPacketBeeHitActive.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketTeamListClear.class, CPacketTeamListClear.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketShopDataSync.class, CPacketShopDataSync.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketMainTimerSync.class, CPacketMainTimerSync.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketSubTimerSync.class, CPacketSubTimerSync.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketMoneySync.class, CPacketMoneySync.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketLoadingType.class, CPacketLoadingType.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketTeamListAdd.class, CPacketTeamListAdd.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketKillBroadcast.class, CPacketKillBroadcast.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketAreaMiddlePos.class, CPacketAreaMiddlePos.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketAreaSize.class, CPacketAreaSize.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketKill.class, CPacketKill.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketAreaShrinkSize.class, CPacketAreaShrinkSize.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketSendPing.class, CPacketSendPing.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketTeamDeath.class, CPacketTeamDeath.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketReciveHealth.class, CPacketReciveHealth.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketOpenBook.class, CPacketOpenBook.class, packetId++, Side.CLIENT);
        networkWrapper.registerMessage(CPacketChicken.class, CPacketChicken.class, packetId++, Side.CLIENT);











        networkWrapper.registerMessage(SPacketTeleport.class, SPacketTeleport.class, packetId++, Side.SERVER);
        networkWrapper.registerMessage(SPacketballoonRemove.class, SPacketballoonRemove.class, packetId++, Side.SERVER);
        networkWrapper.registerMessage(SPacketSendPingPos.class, SPacketSendPingPos.class, packetId++, Side.SERVER);
        networkWrapper.registerMessage(SPacketSendHealth.class, SPacketSendHealth.class, packetId++, Side.SERVER);


    }


}
