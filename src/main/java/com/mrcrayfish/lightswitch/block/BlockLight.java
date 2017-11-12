package com.mrcrayfish.lightswitch.block;

import com.mrcrayfish.lightswitch.Reference;
import com.mrcrayfish.lightswitch.tileentity.TileEntityLight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class BlockLight extends Block implements ITileEntityProvider
{
    public static final PropertyInteger LIGHT_LEVEL = PropertyInteger.create("light_level", 0, 15);

    public BlockLight()
    {
        super(Material.GLASS);
        this.setUnlocalizedName("light");
        this.setRegistryName("light");
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

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            TileEntity light = new TileEntityLight();
            if(stack.hasTagCompound())
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("x", pos.getX());
                tag.setInteger("y", pos.getY());
                tag.setInteger("z", pos.getZ());
                tag.setString("id", Reference.MOD_ID + "Light");

                if(stack.hasDisplayName())
                {
                    tag.setString("name", stack.getDisplayName());
                }

                light.readFromNBT(tag);
                light.validate();
                worldIn.setTileEntity(pos, light);
            }
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(LIGHT_LEVEL);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LIGHT_LEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(LIGHT_LEVEL, meta);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LIGHT_LEVEL);
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityLight();
    }
}
