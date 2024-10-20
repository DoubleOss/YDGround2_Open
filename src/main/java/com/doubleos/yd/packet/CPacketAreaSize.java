package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketAreaSize implements IMessage, IMessageHandler<CPacketAreaSize, CPacketAreaSize>
{

    float m_areaSize = 0f;
    public CPacketAreaSize(){}

    public CPacketAreaSize(float areaSize)
    {
        m_areaSize = areaSize;
    }
    @Override
    public void fromBytes(ByteBuf buf) {

        m_areaSize = buf.readFloat();

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeFloat(m_areaSize);
    }

    @Override
    public CPacketAreaSize onMessage(CPacketAreaSize message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            Variable.Instance().m_borderSize = message.m_areaSize;
        });
        return null;
    }
}
