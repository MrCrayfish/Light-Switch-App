package com.mrcrayfish.lightswitch;

import com.mrcrayfish.device.api.ApplicationManager;
import com.mrcrayfish.device.api.task.TaskManager;
import com.mrcrayfish.lightswitch.app.ApplicationLightSwitch;
import com.mrcrayfish.lightswitch.app.task.TaskLightLevel;
import com.mrcrayfish.lightswitch.tileentity.TileEntityLight;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Author: MrCrayfish
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = Reference.MOD_DEPENDS)
public class LightSwitch
{
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerTileEntity(TileEntityLight.class, Reference.MOD_ID + "Light");
        ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "light_switch"), ApplicationLightSwitch.class);
        TaskManager.registerTask(TaskLightLevel.class);
    }
}
