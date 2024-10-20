package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketMainTimerSync implements IMessage, IMessageHandler<CPacketMainTimerSync, CPacketMainTimerSync>
{

    int m_min = 0;
    int m_sec = 0;

    public CPacketMainTimerSync(){}

    public CPacketMainTimerSync(int min, int sec)
    {
        m_min = min;
        m_sec = sec;
    }


    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_min = buf.readInt();
        m_sec = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(m_min);
        buf.writeInt(m_sec);

    }

    @Override
    public CPacketMainTimerSync onMessage(CPacketMainTimerSync message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();
        Minecraft.getMinecraft().addScheduledTask(()->{
            variable.m_mainTimer_min = message.m_min;
            variable.m_mainTimer_sec = message.m_sec;
        });


        return null;
    }
}
