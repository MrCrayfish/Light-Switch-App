package com.mrcrayfish.lightswitch.app;

import com.mojang.text2speech.Narrator;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.component.ButtonToggle;
import com.mrcrayfish.device.api.app.component.ItemList;
import com.mrcrayfish.device.api.app.component.Slider;
import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.api.task.TaskManager;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.core.network.NetworkDevice;
import com.mrcrayfish.device.core.network.task.TaskGetDevices;
import com.mrcrayfish.device.programs.system.layout.StandardLayout;
import com.mrcrayfish.device.tileentity.TileEntityNetworkDevice;
import com.mrcrayfish.lightswitch.app.task.TaskLightLevel;
import com.mrcrayfish.lightswitch.block.BlockLight;
import com.mrcrayfish.lightswitch.object.Light;
import com.mrcrayfish.lightswitch.tileentity.TileEntityController;
import com.mrcrayfish.lightswitch.tileentity.TileEntityLight;
import com.mrcrayfish.lightswitch.tileentity.TileEntitySource;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Author: MrCrayfish
 */
public class ApplicationLightSwitch extends Application
{
    private ItemList<Light> itemListLights;

    @Override
    public void init()
    {
        StandardLayout layoutMain = new StandardLayout("Select a Light", 150, 115, this, null);
        layoutMain.setIcon(Icons.LIGHT_BULB_ON);

        itemListLights = new ItemList<>(5, 26, 120, 5);
        Slider sliderLightLevel = new Slider(5, 100, 120);
        ButtonToggle buttonSwitch = new ButtonToggle(130, 26, Icons.LIGHT_BULB_OFF);

        buttonSwitch.setEnabled(false);
        buttonSwitch.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if(mouseButton == 0 && itemListLights.getSelectedIndex() != -1)
            {
                Light light = itemListLights.getSelectedItem();
                TaskLightLevel task = new TaskLightLevel(light.getPos(), !light.isPowered() ? 15 : 0);
                task.setCallback((nbtTagCompound, success) ->
                {
                    if(success)
                    {
                        light.setPowered(!light.isPowered());
                        buttonSwitch.setIcon(light.isPowered() ? Icons.LIGHT_BULB_ON : Icons.LIGHT_BULB_OFF);
                        sliderLightLevel.setPercentage(light.isPowered() ? 1F : 0F);
                    }
                });
                TaskManager.sendTask(task);
            }
        });
        layoutMain.addComponent(buttonSwitch);

        itemListLights.setItemClickListener((light, i, mouseButton) ->
        {
            if(mouseButton == 0)
            {
                buttonSwitch.setEnabled(true);
                buttonSwitch.setSelected(light.isPowered());
                buttonSwitch.setIcon(light.isPowered() ? Icons.LIGHT_BULB_ON : Icons.LIGHT_BULB_OFF);
                sliderLightLevel.setPercentage(!light.isPowered() ? 0F : (light.getLevel() - 1) / 14F);
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

        this.setCurrentLayout(layoutMain);
        this.getLights();
    }

    @Override
    public void load(NBTTagCompound nbtTagCompound)
    {

    }

    @Override
    public void save(NBTTagCompound nbtTagCompound)
    {

    }

    private void getLights()
    {
        itemListLights.setLoading(true);
        Task task = new TaskGetDevices(Laptop.getPos(), TileEntitySource.class);
        task.setCallback((tagCompound, success) ->
        {
            if(success)
            {
                NBTTagList tagList = tagCompound.getTagList("network_devices", Constants.NBT.TAG_COMPOUND);
                for(int i = 0; i < tagList.tagCount(); i++)
                {
                    NetworkDevice device = NetworkDevice.fromTag(tagList.getCompoundTagAt(i));

                    BlockPos pos = device.getPos();
                    if(pos == null)
                        return;

                    World world = Minecraft.getMinecraft().world;
                    IBlockState state = world.getBlockState(pos);
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if(tileEntity instanceof TileEntityLight)
                    {
                        TileEntityLight teLight = (TileEntityLight) tileEntity;
                        Light light = new Light(teLight.getName(), pos, ((TileEntityLight) tileEntity).getLevel());
                        itemListLights.addItem(light);
                    }
                    else if(tileEntity instanceof TileEntityController)
                    {
                        TileEntityController controller = (TileEntityController) tileEntity;
                        Light light = new Light(controller.getName(), pos, ((TileEntityController) tileEntity).getLevel());
                        itemListLights.addItem(light);
                    }
                }
            }
            itemListLights.setLoading(false);
        });
        TaskManager.sendTask(task);
    }
}
