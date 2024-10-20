package com.doubleos.yd.packet;

import com.doubleos.yd.util.Ping;
import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketReciveHealth implements IMessage, IMessageHandler<CPacketReciveHealth, CPacketReciveHealth> {
    String sender = "";
    String reciver = "";

    float healthPer = 0.0f;


    public CPacketReciveHealth(String sender, float healthPer)
    {
        this.sender = sender;

        this.healthPer = healthPer;

    }

    public CPacketReciveHealth(){

    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        sender = ByteBufUtils.readUTF8String(buf);
        healthPer = buf.readFloat();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, sender);
        buf.writeFloat(healthPer);

    }

    @Override
    public CPacketReciveHealth onMessage(CPacketReciveHealth message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();
        Minecraft.getMinecraft().addScheduledTask(()->
        {
           for(int i = 0; i<variable.m_teamNames.size(); i++)
           {
               Ping ping = variable.m_teamNames.get(i);
               if(ping.m_name.equals(message.sender))
               {
                   ping.helathPer = message.healthPer;
               }
           }
        });
        return null;
    }
}
