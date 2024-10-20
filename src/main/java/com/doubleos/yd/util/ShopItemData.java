package com.doubleos.yd.util;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ShopItemData
{


    public ItemStack m_stack = new ItemStack(Items.AIR, 0);

    public int m_buyPrice = 0;

    public int m_sellPrice = 0;

    public int m_buyCount = 0;

    public int m_sellCount = 0;

    public boolean m_buyCountActive = false;

    public boolean m_sellCountActive = false;


    public ShopItemData(ItemStack stack, int buyPrice, int sellPrice, int buyCount, int sellCount, boolean buyCountActive, boolean sellCountActive)
    {

        m_stack = stack;

        m_buyPrice = buyPrice;

        m_sellPrice = sellPrice;

        m_buyCount = buyCount;

        m_sellCount = sellCount;

        m_buyCountActive = buyCountActive;

        m_sellCountActive = sellCountActive;
    }




}
