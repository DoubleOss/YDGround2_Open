package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketGameMenu implements IMessage, IMessageHandler<CPacketGameMenu, CPacketGameMenu>
{

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public CPacketGameMenu onMessage(CPacketGameMenu message, MessageContext ctx)
    {

        Minecraft.getMinecraft().addScheduledTask(()->{
            com.doubleos.yd.Yd.proxy.openGuiGameMenu();
        });
        return null;
    }
}
