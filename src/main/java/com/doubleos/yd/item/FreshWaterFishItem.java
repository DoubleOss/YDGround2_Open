package com.doubleos.yd.item;

import com.doubleos.yd.init.ModItems;

public class FreshWaterFishItem extends ItemBase implements FreshFish
{

    public FreshWaterFishItem(String name, String koreaName)
    {
        super(name, koreaName);

        ModItems.m_freshFishList.add(this);

    }
}
