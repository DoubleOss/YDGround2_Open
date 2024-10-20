package com.doubleos.yd.packet;

import com.doubleos.yd.util.Ping;
import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketTeamListAdd implements IMessage, IMessageHandler<CPacketTeamListAdd,CPacketTeamListAdd>
{

    String m_name;

    public CPacketTeamListAdd(String name)
    {
        m_name = name;
    }
    public CPacketTeamListAdd(){}


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
    public CPacketTeamListAdd onMessage(CPacketTeamListAdd message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();

        Minecraft.getMinecraft().addScheduledTask(()->
        {
            Ping ping = new Ping(message.m_name);
            variable.m_teamNames.add(ping);
        });
        return null;
    }
}
