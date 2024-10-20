package com.doubleos.yd.packet;


import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketClientOpenInventory implements IMessage, IMessageHandler<SPacketClientOpenInventory, SPacketClientOpenInventory>
{
    public void fromBytes(ByteBuf buf) {}

    public void toBytes(ByteBuf buf) {}

    public SPacketClientOpenInventory onMessage(SPacketClientOpenInventory message, MessageContext messageContext)
    {
        EntityPlayerMP entplayer = (messageContext.getServerHandler()).player;
        entplayer.openGui(com.doubleos.yd.Yd.instance, 0, entplayer.getEntityWorld(), (int)entplayer.posX, (int)entplayer.posY, (int)entplayer.posZ);
        return null;
    }
}
