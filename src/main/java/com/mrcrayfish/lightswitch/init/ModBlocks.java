package com.mrcrayfish.lightswitch.init;

import com.mrcrayfish.lightswitch.Reference;
import com.mrcrayfish.lightswitch.block.BlockLight;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author: MrCrayfish
 */
public class ModBlocks
{
    public static Block light_off;
    public static Block light_on;

    public static void init()
    {
        light_off = new BlockLight(false);
        light_on = new BlockLight(true);
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
        {
            init();
            event.getRegistry().register(light_off);
            event.getRegistry().register(light_on);
        }

        @SubscribeEvent
        public static void onRegisterItems(RegistryEvent.Register<Item> event)
        {
            registerItemBlock(event.getRegistry(), light_off);
            registerItemBlock(event.getRegistry(), light_on);
        }

        private static void registerItemBlock(IForgeRegistry<Item> registry, Block block)
        {
            ItemBlock item = new ItemBlock(block);
            item.setRegistryName(block.getRegistryName());
            registry.register(item);
        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void onRegisterModels(ModelRegistryEvent event)
        {
            registerModel(light_off);
            registerModel(light_on);
        }

        @SideOnly(Side.CLIENT)
        private static void registerModel(Block block)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }
    }
}
