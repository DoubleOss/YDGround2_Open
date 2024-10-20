package com.doubleos.yd.packet;

import com.doubleos.yd.util.Shop;
import com.doubleos.yd.util.ShopItemData;
import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketShopDataSync implements IMessage, IMessageHandler<CPacketShopDataSync, CPacketShopDataSync>
{
    //ShopName ItemStack stack, int buyPrice, int sellPrice, int buyCount, int sellCount, boolean buyCountActive, boolean sellCountActive

    public ItemStack m_stack = new ItemStack(Items.AIR, 0);

    public String m_shopName = "";
    public int m_buyPrice = 0;
    public int m_sellPrice = 0;
    public int m_buyCount = 0;
    public int m_sellCount = 0;
    public boolean m_buyCountActive = false;

    public boolean m_sellCountActive = false;


    public CPacketShopDataSync() {}

    public CPacketShopDataSync(String shopName, ItemStack stack, int buyPrice, int sellPrice, int buyCount, int sellCount, boolean buyCountActive, boolean sellCountActive)
    {

        m_shopName = shopName;

        m_stack = stack;

        m_buyPrice = buyPrice;

        m_sellPrice = sellPrice;

        m_buyCount = buyCount;

        m_sellCount = sellCount;

        m_buyCountActive = buyCountActive;

        m_sellCountActive = sellCountActive;
    }


    @Override
    public void fromBytes(ByteBuf buf)
    {
        m_shopName = ByteBufUtils.readUTF8String(buf);
        m_stack = ByteBufUtils.readItemStack(buf);
        m_buyPrice = buf.readInt();
        m_sellPrice = buf.readInt();
        m_buyCount = buf.readInt();
        m_sellCount = buf.readInt();
        m_buyCountActive = buf.readBoolean();
        m_sellCountActive = buf.readBoolean();

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, m_shopName);
        ByteBufUtils.writeItemStack(buf, m_stack);
        buf.writeInt(m_buyPrice);
        buf.writeInt(m_sellPrice);
        buf.writeInt(m_buyCount);
        buf.writeInt(m_sellCount);
        buf.writeBoolean(m_buyCountActive);
        buf.writeBoolean(m_sellCountActive);


    }

    @Override
    public CPacketShopDataSync onMessage(CPacketShopDataSync message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();

        Minecraft.getMinecraft().addScheduledTask(()->
        {
            if(variable.m_shopNameToShopData.get(message.m_shopName) != null)
            {
                Shop shop = variable.m_shopNameToShopData.get(message.m_shopName);
                ShopItemData shopItemData = new ShopItemData(message.m_stack, message.m_buyPrice, message.m_sellPrice, message.m_buyCount, message.m_sellCount, message.m_buyCountActive,message.m_sellCountActive);
                shop.m_shopItemDataList.add(shopItemData);
            }
            else
            {
                Shop shop = new Shop();
                shop.m_shopName = message.m_shopName;
                ShopItemData shopItemData = new ShopItemData(message.m_stack, message.m_buyPrice, message.m_sellPrice, message.m_buyCount, message.m_sellCount, message.m_buyCountActive ,message.m_sellCountActive);
                shop.m_shopItemDataList.add(shopItemData);
            }
        });
        return null;
    }

}
