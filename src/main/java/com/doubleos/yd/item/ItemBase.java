package com.doubleos.yd.item;


import com.doubleos.yd.BlockInterface.IHasModel;
import com.doubleos.yd.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemBase extends Item implements IHasModel
{

    public ItemBase(String name, String koreaName)
    {

        setUnlocalizedName(name);
        setRegistryName(name);

        ModItems.ITEMS.add(this);
        ModItems.itemKoreaName.add(koreaName);


    }

    @Override
    public void registerModels()
    {
        com.doubleos.yd.Yd.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        if(!worldIn.isRemote)
        {
            player.sendMessage(new TextComponentString("Right Click"));

        }
        return super.onItemRightClick(worldIn, player, hand);

    }

}
