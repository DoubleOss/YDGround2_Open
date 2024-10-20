package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketReviewOpen implements IMessage, IMessageHandler<CPacketReviewOpen, CPacketReviewOpen>
{

    String m_name;
    public CPacketReviewOpen()
    {

    }
    public CPacketReviewOpen(String name)
    {
        m_name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, m_name);
    }

    @Override
    public CPacketReviewOpen onMessage(CPacketReviewOpen message, MessageContext ctx)
    {

        return null;
    }
}
