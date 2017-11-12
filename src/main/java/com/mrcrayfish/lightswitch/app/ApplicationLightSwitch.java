package com.mrcrayfish.lightswitch.app;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Icon;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.ButtonToggle;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.api.task.Callback;
import com.mrcrayfish.device.api.task.TaskManager;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.lightswitch.app.task.TaskPower;
import com.mrcrayfish.lightswitch.init.ModBlocks;
import com.mrcrayfish.lightswitch.object.Light;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class ApplicationLightSwitch extends Application
{
    @Override
    public void init()
    {
        Layout layoutMain = new Layout(150, 108);

        ItemList<Light> itemListLights = new ItemList<>(5, 18, 120, 6);

        ButtonToggle buttonSwitch = new ButtonToggle(130, 18, Icon.POWER_ON);
        buttonSwitch.setEnabled(false);
        buttonSwitch.setClickListener((component, i) ->
        {
            if(i == 0 && itemListLights.getSelectedIndex() != -1)
            {
                Light light = itemListLights.getSelectedItem();
                TaskPower task = new TaskPower(light.getPos(), !light.isPower());
                task.setCallback((nbtTagCompound, success) ->
                {
                    if(success)
                    {
                        light.setPower(!light.isPower());
                        buttonSwitch.setIcon(light.isPower() ? Icon.POWER_ON : Icon.POWER_OFF);
                    }
                });
                TaskManager.sendTask(task);
            }
        });
        layoutMain.addComponent(buttonSwitch);

        Label labelSelect = new Label("Select a Light", 5, 5);
        layoutMain.addComponent(labelSelect);

        itemListLights.setItems(getLights());
        itemListLights.setItemClickListener((light, i, mouseButton) ->
        {
            if(mouseButton == 0)
            {
                buttonSwitch.setEnabled(true);
                buttonSwitch.setSelected(light.isPower());
                buttonSwitch.setIcon(light.isPower() ? Icon.POWER_ON : Icon.POWER_OFF);
            }
        });
        layoutMain.addComponent(itemListLights);

        setCurrentLayout(layoutMain);
    }

    @Override
    public void load(NBTTagCompound nbtTagCompound)
    {

    }

    @Override
    public void save(NBTTagCompound nbtTagCompound)
    {

    }

    private List<Light> getLights()
    {
        List<Light> lights = new ArrayList<>();

        World world = Minecraft.getMinecraft().world;
        BlockPos laptopPos = Laptop.getPos();
        int range = 20;

        for(int y = -range; y < range + 1; y++)
        {
            for(int z = -range; z < range + 1; z++)
            {
                for(int x = -range; x < range + 1; x++)
                {
                    BlockPos pos = new BlockPos(laptopPos.getX() + x, laptopPos.getY() + y, laptopPos.getZ() + z);
                    IBlockState state = world.getBlockState(pos);
                    if(state.getBlock() == ModBlocks.light_off || state.getBlock() == ModBlocks.light_on)
                    {
                        Light light = new Light(pos, state.getBlock() != ModBlocks.light_off);
                        lights.add(light);
                    }
                }
            }
        }
        return lights;
    }
}
