package com.mrcrayfish.lightswitch.object;

import net.minecraft.util.math.BlockPos;

/**
 * Author: MrCrayfish
 */
public class Light
{
    private BlockPos pos;
    private boolean power;
    private int level;

    public Light(BlockPos pos, int level)
    {
        this.pos = pos;
        this.power = level > 0;
        this.level = level;
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

    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getLevel()
    {
        return level;
    }

    @Override
    public String toString()
    {
        return "Light";
    }
}
