package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketAreaMiddlePos implements IMessage, IMessageHandler<CPacketAreaMiddlePos, CPacketAreaMiddlePos>
{

    int m_posX = 0;
    int m_posZ = 0;


    public CPacketAreaMiddlePos(){}

    public CPacketAreaMiddlePos(int posX, int posZ)
    {

        m_posX = posX;
        m_posZ = posZ;

    }


    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_posX = buf.readInt();
        m_posZ = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(m_posX);
        buf.writeInt(m_posZ);
    }

    @Override
    public CPacketAreaMiddlePos onMessage(CPacketAreaMiddlePos message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            Variable variable = Variable.Instance();
            variable.m_borderMiddlePosX = message.m_posX;
            variable.m_borderMiddlePosZ = message.m_posZ;
        });
        return null;
    }
}
