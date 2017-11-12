package com.mrcrayfish.lightswitch.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Author: MrCrayfish
 */
public class BlockLight extends Block
{
    public BlockLight(boolean powered)
    {
        super(Material.GLASS);
        String id = "light_" + (powered ? "on" : "off");
        this.setUnlocalizedName(id);
        this.setRegistryName(id);
        this.setLightLevel(powered ? 1.0F : 0.0F);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
