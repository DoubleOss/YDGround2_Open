package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketAreaShrinkSize implements IMessage, IMessageHandler<CPacketAreaShrinkSize, CPacketAreaShrinkSize>
{

    float m_areaSize = 0f;
    public CPacketAreaShrinkSize(){}

    public CPacketAreaShrinkSize(float areaSize)
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
    public CPacketAreaShrinkSize onMessage(CPacketAreaShrinkSize message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            Variable.Instance().m_shrinkBorderSize = message.m_areaSize;
        });
        return null;
    }
}