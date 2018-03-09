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
import java.util.function.Predicate;

/**
 * Author: MrCrayfish
 */
public class TileEntityController extends TileEntitySource
{
    private Set<NetworkDevice> connectedLights = new HashSet<>();
    private int counter;
    private int lightLevel;

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

        if(compound.hasKey("connected_lights", Constants.NBT.TAG_LIST))
        {
            connectedLights.clear();
            NBTTagList deviceList = compound.getTagList("connected_lights", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < deviceList.tagCount(); i++)
            {
                connectedLights.add(NetworkDevice.fromTag(deviceList.getCompoundTagAt(i)));
            }
        }

        if(compound.hasKey("light_level", Constants.NBT.TAG_INT))
        {
            lightLevel = compound.getInteger("light_level");
        }
    }

    @Override
    public NBTTagCompound writeSyncTag()
    {
        NBTTagCompound tag = super.writeSyncTag();
        tag.setInteger("light_level", lightLevel);
        return tag;
    }

    public boolean addLight(BlockPos pos)
    {
        if(this.isRegistered(pos))
            return false;

        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileEntitySource)
        {
            connectedLights.add(new NetworkDevice((TileEntityNetworkDevice) tileEntity));
            return true;
        }
        return false;
    }

    public void removeLight(TileEntitySource source)
    {
        connectedLights.removeIf(networkDevice -> networkDevice.getPos().equals(source.getPos()));
    }

    @Override
    public void setLevel(int level)
    {
        this.lightLevel = level;
        this.sync();
        connectedLights.forEach(networkDevice ->
        {
            TileEntity tileEntity = world.getTileEntity(networkDevice.getPos());
            if(tileEntity instanceof TileEntitySource)
            {
                ((TileEntitySource) tileEntity).setLevel(level);
            }
        });
    }

    @Override
    public int getLevel()
    {
        return this.lightLevel;
    }

    public boolean isRegistered(BlockPos pos)
    {
        if(this.getPos().equals(pos))
            return true;

        final Predicate<NetworkDevice> HAS_DEVICE = networkDevice ->
        {
            TileEntity tileEntity = world.getTileEntity(networkDevice.getPos());
            if(tileEntity instanceof TileEntityController)
            {
                return networkDevice.getPos().equals(pos) || ((TileEntityController) tileEntity).isRegistered(pos);
            }
            return false;
        };
        return connectedLights.stream().anyMatch(HAS_DEVICE);
    }
}
