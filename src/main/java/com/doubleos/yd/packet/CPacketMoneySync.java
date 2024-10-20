package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketMoneySync implements IMessage, IMessageHandler<CPacketMoneySync, CPacketMoneySync>
{

    int m_money = 0;

    public CPacketMoneySync(){};

    public CPacketMoneySync(int money)
    {
        m_money = money;
    }
    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_money = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(m_money);
    }

    @Override
    public CPacketMoneySync onMessage(CPacketMoneySync message, MessageContext ctx)
    {

        Minecraft.getMinecraft().addScheduledTask(()->{
            Variable.Instance().m_money = message.m_money;
        });
        return null;
    }
}
