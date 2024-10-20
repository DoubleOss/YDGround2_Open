package com.doubleos.yd.packet;

import com.doubleos.yd.util.AnimationBroadCast;
import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketKillBroadcast implements IMessage, IMessageHandler<CPacketKillBroadcast, CPacketKillBroadcast>
{

    String m_killerName = "";

    String m_victimName = "";

    ItemStack m_stack = ItemStack.EMPTY;


    public CPacketKillBroadcast(){}

    public CPacketKillBroadcast(String killerName, String victimName, ItemStack stack)
    {
        m_killerName = killerName;
        m_victimName = victimName;
        m_stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_killerName = ByteBufUtils.readUTF8String(buf);
        m_victimName = ByteBufUtils.readUTF8String(buf);
        m_stack = ByteBufUtils.readItemStack(buf);

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, m_killerName);
        ByteBufUtils.writeUTF8String(buf, m_victimName);
        ByteBufUtils.writeItemStack(buf, m_stack);



    }

    @Override
    public CPacketKillBroadcast onMessage(CPacketKillBroadcast message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            AnimationBroadCast broadcast = new AnimationBroadCast(message.m_killerName, message.m_victimName);
            broadcast.m_stack = message.m_stack;
            variable.m_animationBroadcastList.add(broadcast);
        });
        return null;
    }
}
