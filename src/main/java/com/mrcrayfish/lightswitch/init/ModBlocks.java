package com.mrcrayfish.lightswitch.init;

import com.mrcrayfish.lightswitch.block.BlockController;
import com.mrcrayfish.lightswitch.block.BlockLight;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * Author: MrCrayfish
 */
public class ModBlocks
{
    public static final Block LIGHT;
    public static final Block CONTROLLER;

    static
    {
        LIGHT = new BlockLight();
        CONTROLLER = new BlockController();
    }

    static void register()
    {
        registerBlock(LIGHT);
        registerBlock(CONTROLLER);
    }

    private static void registerBlock(final Block block)
    {
        RegistrationHandler.Blocks.add(block);
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        RegistrationHandler.Items.add(item);
    }
}
