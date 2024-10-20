package com.doubleos.yd.packet;

import com.doubleos.yd.Yd;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketChicken implements IMessage, IMessageHandler<CPacketChicken, CPacketChicken>
{
    String str = "";
    int count = 1;
    public CPacketChicken(){}

    public CPacketChicken(String str, int count)
    {
        this.str = str;
        this.count = count;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        str = ByteBufUtils.readUTF8String(buf);
        count = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, str);
        buf.writeInt(count);
    }

    @Override
    public CPacketChicken onMessage(CPacketChicken message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(()->
        {
            Yd.proxy.openGuiFriendship(message.str, message.count);
        });
        return null;
    }
}
