package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketBalloonArrowHit implements IMessage, IMessageHandler<CPacketBalloonArrowHit, CPacketBalloonArrowHit>
{


    int m_entityId = 0;

    public CPacketBalloonArrowHit(){}

    public CPacketBalloonArrowHit(int entityId)
    {
        m_entityId = entityId;
    }


    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(m_entityId);
    }

    @Override
    public CPacketBalloonArrowHit onMessage(CPacketBalloonArrowHit message, MessageContext ctx)
    {

        return null;
    }
}

