package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketBeeHitActive implements IMessage, IMessageHandler<CPacketBeeHitActive, CPacketBeeHitActive>
{

    boolean m_active = false;

    int m_number = 0;

    public CPacketBeeHitActive() {}

    public CPacketBeeHitActive(int number)
    {
        m_number = number;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        m_number = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(m_number);
    }


    @Override
    public CPacketBeeHitActive onMessage(CPacketBeeHitActive message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();
        variable.mapNumber = message.m_number;
        return null;
    }
}
