package com.mrcrayfish.lightswitch.init;

import com.mrcrayfish.lightswitch.item.ItemElectricCable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

/**
 * Author: MrCrayfish
 */
public class ModItems
{
    public static final Item ELECTRIC_CABLE;

    static
    {
        ELECTRIC_CABLE = new ItemElectricCable();
    }

    static void register()
    {
        RegistrationHandler.Items.add(ELECTRIC_CABLE);
    }
}
