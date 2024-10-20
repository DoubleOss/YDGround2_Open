package com.doubleos.yd.handler;


import com.doubleos.yd.BlockInterface.IHasModel;
import com.doubleos.yd.BlockInterface.IObjModel;
import com.doubleos.yd.init.ModBlocks;
import com.doubleos.yd.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryHandler
{

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
        for (int i = 0; i < ModItems.ITEMS.size(); i++)
        {
            event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[i]));
        }
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event)
    {
        for (int i = 0; i < ModBlocks.BLOCKS.size(); i++)
        {
            event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[i]));
        }



    }



    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
        for(Item item : ModItems.ITEMS)
        {
            if(item instanceof IHasModel)
            {
                ((IHasModel) item).registerModels();
            }
        }
        for(Block block : ModBlocks.BLOCKS)
        {
            if(block instanceof IHasModel)
            {
                ((IHasModel) block).registerModels();
                if (block instanceof IObjModel)
                {
                    ((IObjModel) block).initModel();
                }
            }
        }

    }

}
