package com.doubleos.yd.packet;

import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class CPacketSendPing implements IMessage, IMessageHandler<CPacketSendPing, CPacketSendPing>
{

    String sender = "";

    String reciver = "";

    float x = 0.0f;
    float y = 0.0f;


    public CPacketSendPing (String sender, float x, float y)
    {

        this.sender = sender;

        this.x = x;
        this.y = y;

    }
    public CPacketSendPing ()
    {

    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        sender = ByteBufUtils.readUTF8String(buf);

        this.x = buf.readFloat();
        this.y = buf.readFloat();

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, sender);

        buf.writeFloat(this.x);
        buf.writeFloat(this.y);


    }

    @Override
    public CPacketSendPing onMessage(CPacketSendPing message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            for(int i = 0; i<variable.m_teamNames.size(); i++)
            {
                if(message.sender.equals(variable.m_teamNames.get(i).m_name))
                {
                    variable.m_teamNames.get(i).m_PingX = message.x;
                    variable.m_teamNames.get(i).m_PingY = message.y;

                }
            }
        });
        return null;
    }
}