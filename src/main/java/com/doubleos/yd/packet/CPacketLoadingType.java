package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketLoadingType implements IMessage, IMessageHandler<CPacketLoadingType, CPacketLoadingType>
{

    String m_name = "";
    public CPacketLoadingType()
    {

    }

    public CPacketLoadingType(String name)
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
    public CPacketLoadingType onMessage(CPacketLoadingType message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            Variable.Instance().m_barType = message.m_name;
        });
        return null;
    }
}
