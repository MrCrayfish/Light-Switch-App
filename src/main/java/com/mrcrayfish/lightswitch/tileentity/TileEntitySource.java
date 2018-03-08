package com.mrcrayfish.lightswitch.tileentity;

import com.mrcrayfish.device.tileentity.TileEntityNetworkDevice;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

/**
 * Author: MrCrayfish
 */
public abstract class TileEntitySource extends TileEntityNetworkDevice
{
    private String name = "Light";

    public String getName()
    {
        return name;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if(compound.hasKey("name", Constants.NBT.TAG_STRING))
        {
            this.name = compound.getString("name");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setString("name", this.name);
        return super.writeToNBT(compound);
    }

    @Override
    public String getDeviceName()
    {
        return name;
    }

    @Override
    public void update()
    {

    }

    public abstract void setLevel(int level);
}
