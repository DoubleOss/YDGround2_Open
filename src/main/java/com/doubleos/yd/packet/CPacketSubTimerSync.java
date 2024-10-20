package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketSubTimerSync implements IMessage, IMessageHandler<CPacketSubTimerSync, CPacketSubTimerSync>
{

    int m_min = 0;
    int m_sec = 0;

    public CPacketSubTimerSync(){}

    public CPacketSubTimerSync(int min, int sec)
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
    public CPacketSubTimerSync onMessage(CPacketSubTimerSync message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();
        Minecraft.getMinecraft().addScheduledTask(()->{
            variable.m_subTimer_min = message.m_min;
            variable.m_subTimer_sec = message.m_sec;
        });


        return null;
    }
}
