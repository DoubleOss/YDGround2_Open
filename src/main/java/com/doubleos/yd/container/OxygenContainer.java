package com.doubleos.yd.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class OxygenContainer extends Container
{
    public OxygenContainer(InventoryPlayer inventoryPlayer, EntityPlayer player)
    {

    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        System.out.println("@@@@@@@@@");
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if(index >= 0 && index <=1 && itemStack1.getItem() instanceof ItemArmor)
            {
                if (!mergeItemStack(itemStack1, 2, 10, false))
                {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemStack1, itemStack);
            }
            if (itemStack1.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
            if(itemStack1.getCount() == itemStack.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onSlotChange(itemStack1, itemStack);

        }
        return itemStack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }
}
