package com.mrcrayfish.lightswitch;

import com.mrcrayfish.device.api.ApplicationManager;
import com.mrcrayfish.device.api.task.TaskManager;
import com.mrcrayfish.lightswitch.app.ApplicationLightSwitch;
import com.mrcrayfish.lightswitch.app.task.TaskLightLevel;
import com.mrcrayfish.lightswitch.init.ModBlocks;
import com.mrcrayfish.lightswitch.init.RegistrationHandler;
import com.mrcrayfish.lightswitch.tileentity.TileEntityController;
import com.mrcrayfish.lightswitch.tileentity.TileEntityLight;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Author: MrCrayfish
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = Reference.MOD_DEPENDS)
public class LightSwitch
{
    public static final CreativeTabs TAB = new CreativeTabs("tabLightswitch")
    {
        private final ItemStack ITEM = new ItemStack(ModBlocks.CONTROLLER);

        @Override
        public ItemStack getTabIconItem()
        {
            return ITEM;
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        RegistrationHandler.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerTileEntity(TileEntityLight.class, Reference.MOD_ID + "Light");
        GameRegistry.registerTileEntity(TileEntityController.class, Reference.MOD_ID + "Controller");
        ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "light_switch"), ApplicationLightSwitch.class);
        TaskManager.registerTask(TaskLightLevel.class);
    }
}
