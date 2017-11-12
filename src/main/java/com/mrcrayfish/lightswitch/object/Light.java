package com.mrcrayfish.lightswitch.object;

import net.minecraft.util.math.BlockPos;

/**
 * Author: MrCrayfish
 */
public class Light
{
    private BlockPos pos;
    private boolean power;

    public Light(BlockPos pos, boolean power)
    {
        this.pos = pos;
        this.power = power;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public boolean isPower()
    {
        return power;
    }

    public void setPower(boolean power)
    {
        this.power = power;
    }

    @Override
    public String toString()
    {
        return "Light";
    }
}
