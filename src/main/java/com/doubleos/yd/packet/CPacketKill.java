package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketKill implements IMessage, IMessageHandler<CPacketKill, CPacketKill>
{

    int m_KillSize = 0;

    public CPacketKill(){}

    public CPacketKill(int killSize)
    {
        m_KillSize = killSize;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_KillSize = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(m_KillSize);

    }

    @Override
    public CPacketKill onMessage(CPacketKill message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            Variable.Instance().m_KillAmount = message.m_KillSize;
        });

        return null;
    }
}
