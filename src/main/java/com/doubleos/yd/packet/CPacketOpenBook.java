package com.doubleos.yd.packet;

import com.doubleos.yd.Yd;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketOpenBook implements IMessage, IMessageHandler<CPacketOpenBook, CPacketOpenBook>
{

    public CPacketOpenBook(){}

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public CPacketOpenBook onMessage(CPacketOpenBook message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            Yd.proxy.openGuiScreenRule();
        });
        return null;
    }
}
