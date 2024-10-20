package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketTeleport implements IMessage, IMessageHandler<SPacketTeleport, SPacketTeleport>
{

    float m_xPos = 0f;
    float m_yPos = 0f;
    float m_zPos = 0f;

    public SPacketTeleport(){}
    public SPacketTeleport(float x, float y, float z)
    {

        m_xPos = x;
        m_yPos = y;
        m_zPos = z;

    }
    @Override
    public void fromBytes(ByteBuf buf)
    {


        m_xPos = buf.readFloat();
        m_yPos = buf.readFloat();
        m_zPos = buf.readFloat();


    }

    @Override
    public void toBytes(ByteBuf buf)
    {

        buf.writeFloat(m_xPos);
        buf.writeFloat(m_yPos);
        buf.writeFloat(m_zPos);

    }
    @Override
    public SPacketTeleport onMessage(SPacketTeleport message, MessageContext ctx)
    {
        ctx.getServerHandler().player.setPositionAndUpdate(message.m_xPos, message.m_yPos, message.m_zPos);


        return null;
    }
}
