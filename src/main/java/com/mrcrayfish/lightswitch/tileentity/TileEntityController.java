package com.mrcrayfish.lightswitch.tileentity;

import com.mrcrayfish.device.core.network.NetworkDevice;
import com.mrcrayfish.device.tileentity.TileEntityNetworkDevice;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: MrCrayfish
 */
public class TileEntityController extends TileEntitySource
{
    private Set<NetworkDevice> connectedLights = new HashSet<>();
    private int counter;

    @Override
    public void update()
    {
        if(++counter == 20)
        {
            this.updateLights();
            counter = 0;
        }
    }

    private void updateLights()
    {
        connectedLights.removeIf(networkDevice ->
        {
           TileEntity tileEntity = world.getTileEntity(networkDevice.getPos());
           if(tileEntity instanceof TileEntitySource)
           {
               return !networkDevice.getId().equals(((TileEntitySource) tileEntity).getId());
           }
           return true;
        });
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if(connectedLights.size() > 0)
        {
            NBTTagList deviceList = new NBTTagList();
            connectedLights.forEach(networkDevice -> deviceList.appendTag(networkDevice.toTag(true)));
            compound.setTag("connected_lights", deviceList);
        }
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        connectedLights.clear();
        if(compound.hasKey("connected_lights", Constants.NBT.TAG_LIST))
        {
            NBTTagList deviceList = compound.getTagList("connected_lights", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < deviceList.tagCount(); i++)
            {
                connectedLights.add(NetworkDevice.fromTag(deviceList.getCompoundTagAt(i)));
            }
        }
    }

    public void addLight(BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileEntityLight)
        {
            connectedLights.add(new NetworkDevice((TileEntityNetworkDevice) tileEntity));
        }
    }

    public void removeLight(TileEntitySource source)
    {
        connectedLights.removeIf(networkDevice -> networkDevice.getPos().equals(source.getPos()));
    }

    @Override
    public void setLevel(int level)
    {
        connectedLights.forEach(networkDevice ->
        {
            TileEntity tileEntity = world.getTileEntity(networkDevice.getPos());
            if(tileEntity instanceof TileEntitySource)
            {
                ((TileEntitySource) tileEntity).setLevel(level);
            }
        });
    }
}
