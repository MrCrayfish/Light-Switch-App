package com.mrcrayfish.lightswitch.tileentity;

import com.mrcrayfish.device.tileentity.TileEntityNetworkDevice;
import com.mrcrayfish.lightswitch.block.BlockLight;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Author: MrCrayfish
 */
public class TileEntityLight extends TileEntitySource
{
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void setLevel(int level)
    {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.withProperty(BlockLight.LIGHT_LEVEL, Math.max(0, Math.min(15, level))));
    }
}
