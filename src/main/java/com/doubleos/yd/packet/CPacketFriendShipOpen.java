package com.doubleos.yd.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketFriendShipOpen implements IMessage, IMessageHandler<CPacketFriendShipOpen, CPacketFriendShipOpen>
{

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public CPacketFriendShipOpen onMessage(CPacketFriendShipOpen message, MessageContext ctx)
    {

        return null;
    }
}
