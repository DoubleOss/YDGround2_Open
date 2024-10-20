package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketOpenRule implements IMessageHandler<CPacketOpenRule, CPacketOpenRule>, IMessage
{

    int m_min = 0;
    int m_max = 0;


    public CPacketOpenRule(){}

    public CPacketOpenRule(int min, int max)
    {
        m_min = min;
        m_max = max;


    }

    @Override
    public void fromBytes(ByteBuf buf)
    {

        m_min = buf.readInt();
        m_max = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(m_min);
        buf.writeInt(m_max);

    }

    @Override
    public CPacketOpenRule onMessage(CPacketOpenRule message, MessageContext ctx)
    {

        Minecraft minecraft = Minecraft.getMinecraft();

        minecraft.addScheduledTask(()->
        {
            com.doubleos.yd.Yd.proxy.openGuiScreenRule();
        });
        return null;
    }
}
