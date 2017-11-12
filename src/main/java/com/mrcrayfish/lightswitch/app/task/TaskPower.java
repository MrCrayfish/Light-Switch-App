package com.mrcrayfish.lightswitch.app.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.lightswitch.init.ModBlocks;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class TaskPower extends Task
{
    private long pos;
    private boolean power;

    private TaskPower()
    {
        super("power_light");
    }

    public TaskPower(BlockPos pos, boolean power)
    {
        this();
        this.pos = pos.toLong();
        this.power = power;
    }

    @Override
    public void prepareRequest(NBTTagCompound tag)
    {
        tag.setLong("pos", pos);
        tag.setBoolean("power", power);
    }

    @Override
    public void processRequest(NBTTagCompound tag, World world, EntityPlayer entityPlayer)
    {
        BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));
        world.setBlockState(pos, tag.getBoolean("power") ? ModBlocks.light_on.getDefaultState() : ModBlocks.light_off.getDefaultState());
        this.setSuccessful();
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
