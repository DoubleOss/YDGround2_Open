package com.doubleos.yd.packet;

import com.doubleos.yd.util.Shop;
import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketTeamListClear implements IMessage, IMessageHandler<CPacketTeamListClear, CPacketTeamListClear>
{

    String m_shopName = "";

    public CPacketTeamListClear(){}

    public CPacketTeamListClear(String shopname)
    {
        m_shopName = shopname;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_shopName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, m_shopName);
    }

    @Override
    public CPacketTeamListClear onMessage(CPacketTeamListClear message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            variable.m_teamNames.clear();
        });

        return null;
    }
}
