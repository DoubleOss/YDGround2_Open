package com.doubleos.yd.item;

import com.doubleos.yd.init.ModItems;

public class OceanFishItem extends ItemBase implements OceanFish{


    public OceanFishItem(String name, String koreaName) {
        super(name, koreaName);

        ModItems.m_oceanFishList.add(this);
    }
}
