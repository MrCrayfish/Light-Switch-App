package com.mrcrayfish.lightswitch.object;

import net.minecraft.util.math.BlockPos;

/**
 * Author: MrCrayfish
 */
public class Light
{
    private String name;
    private BlockPos pos;
    private boolean powered;
    private int level;

    public Light(String name, BlockPos pos, int level)
    {
        this.name = name;
        this.pos = pos;
        this.powered = level > 0;
        this.level = level;
    }

    public String getName()
    {
        return name;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public boolean isPowered()
    {
        return powered;
    }

    public void setPowered(boolean powered)
    {
        this.powered = powered;
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
        return name;
    }
}
