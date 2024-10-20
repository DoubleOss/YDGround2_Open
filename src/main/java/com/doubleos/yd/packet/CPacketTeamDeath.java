package com.doubleos.yd.packet;

import com.doubleos.yd.util.Ping;
import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketTeamDeath implements IMessage, IMessageHandler<CPacketTeamDeath, CPacketTeamDeath>
{

    String m_name = "";

    boolean m_alive = true;

    public CPacketTeamDeath(){}

    public CPacketTeamDeath(String name,  boolean alive)
    {
        m_name = name;
        m_alive = alive;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_name = ByteBufUtils.readUTF8String(buf);
        m_alive = buf.readBoolean();

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, m_name);
        buf.writeBoolean(m_alive);
    }

    @Override
    public CPacketTeamDeath onMessage(CPacketTeamDeath message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            if(variable.m_teamNames.size() != 0)
            {
                for(int i = 0; i<variable.m_teamNames.size(); i++)
                {
                    Ping ping = variable.m_teamNames.get(i);
                    if(ping.m_name.equals(message.m_name))
                    {
                        ping.m_Alive=false;
                    }
                }

            }
        });
        return null;
    }
}
