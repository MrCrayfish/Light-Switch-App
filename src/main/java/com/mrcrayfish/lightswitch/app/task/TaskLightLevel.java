package com.mrcrayfish.lightswitch.app.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.lightswitch.block.BlockLight;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class TaskLightLevel extends Task
{
    private long pos;
    private int level;

    private TaskLightLevel()
    {
        super("power_light");
    }

    public TaskLightLevel(BlockPos pos, int level)
    {
        this();
        this.pos = pos.toLong();
        this.level = level;
    }

    @Override
    public void prepareRequest(NBTTagCompound tag)
    {
        tag.setLong("pos", pos);
        tag.setInteger("level", level);
    }

    @Override
    public void processRequest(NBTTagCompound tag, World world, EntityPlayer entityPlayer)
    {
        BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockLight)
        {
            world.setBlockState(pos, state.withProperty(BlockLight.LIGHT_LEVEL, Math.max(0, Math.min(15, tag.getInteger("level")))));
            this.setSuccessful();
        }
    }

    @Override
    public void prepareResponse(NBTTagCompound tag)
    {

    }

    @Override
    public void processResponse(NBTTagCompound tag)
    {

    }
}
