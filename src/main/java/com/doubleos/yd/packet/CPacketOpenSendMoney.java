package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketOpenSendMoney implements IMessage, IMessageHandler<CPacketOpenSendMoney, CPacketOpenSendMoney>
{

    public CPacketOpenSendMoney(){}



    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public CPacketOpenSendMoney onMessage(CPacketOpenSendMoney message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(()->{
            com.doubleos.yd.Yd.proxy.openGuiSendMoenyOpen();
        });
        return null;
    }
}
