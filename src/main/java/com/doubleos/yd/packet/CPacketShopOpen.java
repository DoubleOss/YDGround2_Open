package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketShopOpen implements IMessage, IMessageHandler<CPacketShopOpen, CPacketShopOpen>
{


    String m_shopName = "";

    public CPacketShopOpen(){}

    public CPacketShopOpen(String shopName)
    {
        m_shopName = shopName;
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
    public CPacketShopOpen onMessage(CPacketShopOpen message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(()->{
            com.doubleos.yd.Yd.proxy.openGuiShopOpen(message.m_shopName);
        });
        return null;
    }
}
