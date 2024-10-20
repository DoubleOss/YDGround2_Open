package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSendHealth implements IMessage, IMessageHandler<SPacketSendHealth, SPacketSendHealth>
{

    String sender = "";
    String reciver = "";

    float healthPer = 0.0f;


    public SPacketSendHealth(String sender, String reciver, float healthPer)
    {
        this.sender = sender;
        this.reciver = reciver;
        this.healthPer = healthPer;

    }

    public SPacketSendHealth(){

    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        sender = ByteBufUtils.readUTF8String(buf);
        reciver = ByteBufUtils.readUTF8String(buf);
        healthPer = buf.readFloat();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, sender);
        ByteBufUtils.writeUTF8String(buf, reciver);
        buf.writeFloat(healthPer);

    }

    @Override
    public SPacketSendHealth onMessage(SPacketSendHealth message, MessageContext ctx)
    {
        MinecraftServer server = ctx.getServerHandler().player.getServer();
        EntityPlayerMP playerMP = server.getPlayerList().getPlayerByUsername(message.reciver);
        if (playerMP != null)
        {
            Packet.networkWrapper.sendTo(new CPacketReciveHealth(message.sender, message.healthPer), playerMP);
        }

        return null;
    }
}
