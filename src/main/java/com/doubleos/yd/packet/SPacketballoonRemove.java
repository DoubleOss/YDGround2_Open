package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketballoonRemove implements IMessage, IMessageHandler<SPacketballoonRemove, SPacketballoonRemove>
{

    int m_posX;
    int m_posY;
    int m_posZ;

    public SPacketballoonRemove(){}

    public SPacketballoonRemove(int x, int y, int z)
    {
        m_posX = x;
        m_posY = y;
        m_posZ = z;
    }
    @Override
    public void fromBytes(ByteBuf buf)
    {

        m_posX = buf.readInt();
        m_posY = buf.readInt();
        m_posZ = buf.readInt();


    }

    @Override
    public void toBytes(ByteBuf buf)
    {

        buf.writeInt(m_posX);
        buf.writeInt(m_posY);
        buf.writeInt(m_posZ);

    }

    @Override
    public SPacketballoonRemove onMessage(SPacketballoonRemove message, MessageContext ctx)
    {
        ctx.getServerHandler().player.getServer().commandManager.executeCommand(ctx.getServerHandler().player.getServer(), "/상자설치 " + message.m_posX + " " + message.m_posY + " " + message.m_posZ);
        return null;
    }
}
