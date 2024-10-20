package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.core.jmx.Server;

public class SPacketSendPingPos implements IMessage, IMessageHandler<SPacketSendPingPos, SPacketSendPingPos>
{

    String sender = "";

    String reciver = "";

    float x = 0.0f;
    float y = 0.0f;


    public SPacketSendPingPos(){}
    public SPacketSendPingPos (String sender, String reciver, float x, float y)
    {

        this.sender = sender;
        this.reciver = reciver;


        this.x = x;
        this.y = y;

    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        sender =ByteBufUtils.readUTF8String(buf);
        reciver = ByteBufUtils.readUTF8String(buf);

        this.x = buf.readFloat();
        this.y = buf.readFloat();

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, sender);
        ByteBufUtils.writeUTF8String(buf, reciver);

        buf.writeFloat(this.x);
        buf.writeFloat(this.y);


    }

    @Override
    public SPacketSendPingPos onMessage(SPacketSendPingPos message, MessageContext ctx)
    {
        MinecraftServer server = ctx.getServerHandler().player.getServer();
        EntityPlayerMP playerMP = server.getPlayerList().getPlayerByUsername(message.reciver);
        if (playerMP != null)
        {
            Packet.networkWrapper.sendTo(new CPacketSendPing(message.sender, message.x, message.y), playerMP);
        }

        return null;
    }
}
