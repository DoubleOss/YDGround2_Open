package com.doubleos.yd.block;

import com.doubleos.yd.BlockInterface.IHasModel;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;

public class BlockGlass extends BlockBreakable implements IHasModel
{

    protected BlockGlass(String name, Material materialIn, boolean ignoreSimilarityIn)
    {
        super(materialIn, ignoreSimilarityIn);
        setUnlocalizedName(name);
        setRegistryName(name);
        setLightOpacity(0);
        setLightLevel(1f);

    }

    @Override
    public void registerModels()
    {

    }
}
