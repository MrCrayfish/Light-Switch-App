package com.mrcrayfish.lightswitch.app;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.ButtonToggle;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Label;
import com.mrcrayfish.device.api.app.component.Slider;
import com.mrcrayfish.device.api.task.TaskManager;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.programs.system.layout.StandardLayout;
import com.mrcrayfish.lightswitch.app.task.TaskLightLevel;
import com.mrcrayfish.lightswitch.block.BlockLight;
import com.mrcrayfish.lightswitch.object.Light;
import com.mrcrayfish.lightswitch.tileentity.TileEntityLight;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        StandardLayout layoutMain = new StandardLayout("Select a Light", 150, 115, this, null);
        layoutMain.setIcon(Icons.LIGHT_BULB_ON);

        ItemList<Light> itemListLights = new ItemList<>(5, 26, 120, 5);
        Slider sliderLightLevel = new Slider(5, 100, 120);
        ButtonToggle buttonSwitch = new ButtonToggle(130, 26, Icons.LIGHT_BULB_OFF);

        buttonSwitch.setEnabled(false);
        buttonSwitch.setClickListener((mouseX, mouseY, i) ->
        {
            if(i == 0 && itemListLights.getSelectedIndex() != -1)
            {
                Light light = itemListLights.getSelectedItem();
                TaskLightLevel task = new TaskLightLevel(light.getPos(), !light.isPower() ? 15 : 0);
                task.setCallback((nbtTagCompound, success) ->
                {
                    if(success)
                    {
                        light.setPower(!light.isPower());
                        buttonSwitch.setIcon(light.isPower() ? Icons.LIGHT_BULB_ON : Icons.LIGHT_BULB_OFF);
                        sliderLightLevel.setPercentage(light.isPower() ? 1F : 0F);
                    }
                });
                TaskManager.sendTask(task);
            }
        });
        layoutMain.addComponent(buttonSwitch);

        itemListLights.setItems(getLights());
        itemListLights.setItemClickListener((light, i, mouseButton) ->
        {
            if(mouseButton == 0)
            {
                buttonSwitch.setEnabled(true);
                buttonSwitch.setSelected(light.isPower());
                buttonSwitch.setIcon(light.isPower() ? Icons.LIGHT_BULB_ON : Icons.LIGHT_BULB_OFF);
                sliderLightLevel.setPercentage(!light.isPower() ? 0F : (light.getLevel() - 1) / 14F);
            }
        });
        layoutMain.addComponent(itemListLights);

        sliderLightLevel.setSlideListener(v ->
        {
            if(itemListLights.getSelectedIndex() != -1)
            {
                int level = (int) (14.0F * v) + 1;
                Light light = itemListLights.getSelectedItem();
                light.setLevel(level);
                TaskManager.sendTask(new TaskLightLevel(light.getPos(), level));
            }
        });
        layoutMain.addComponent(sliderLightLevel);

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
                    if(state.getBlock() instanceof BlockLight)
                    {
                        TileEntity tileEntity = world.getTileEntity(pos);
                        if(tileEntity instanceof TileEntityLight)
                        {
                            TileEntityLight teLight = (TileEntityLight) tileEntity;
                            Light light = new Light(teLight.getName(), pos, state.getValue(BlockLight.LIGHT_LEVEL));
                            lights.add(light);
                        }
                    }
                }
            }
        }
        return lights;
    }
}
