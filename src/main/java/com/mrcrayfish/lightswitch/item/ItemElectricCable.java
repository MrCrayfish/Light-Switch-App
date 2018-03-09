package com.mrcrayfish.lightswitch.item;

import com.mrcrayfish.device.tileentity.TileEntityDevice;
import com.mrcrayfish.lightswitch.LightSwitch;
import com.mrcrayfish.lightswitch.tileentity.TileEntityController;
import com.mrcrayfish.lightswitch.tileentity.TileEntitySource;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class ItemElectricCable extends Item
{
    public ItemElectricCable()
    {
        this.setUnlocalizedName("electric_cable");
        this.setRegistryName("electric_cable");
        this.setCreativeTab(LightSwitch.TAB);
        this.setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        if(!world.isRemote)
        {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntity source = world.getTileEntity(pos);

            if(heldItem.hasTagCompound() && source instanceof TileEntityController)
            {
                if(!heldItem.hasTagCompound())
                {
                    sendGameInfoMessage(player, "message.invalid_cable");
                    return EnumActionResult.SUCCESS;
                }

                TileEntityController controller = (TileEntityController) source;
                NBTTagCompound tag = heldItem.getTagCompound();
                BlockPos devicePos = BlockPos.fromLong(tag.getLong("pos"));

                TileEntity device = world.getTileEntity(devicePos);
                if(device instanceof TileEntitySource)
                {
                    if(controller.isRegistered(devicePos))
                    {
                        sendGameInfoMessage(player, "message.light_already_registered");
                    }
                    else if(getDistance(device.getPos(), source.getPos()) <= 10) //TODO: LightConfig.getSignalRange() add config soon
                    {
                        if(controller.addLight(devicePos))
                        {
                            heldItem.shrink(1);
                        }
                    }
                    else
                    {
                        sendGameInfoMessage(player, "message.light_too_far");
                    }
                }
            }
            else if(source instanceof TileEntitySource)
            {
                TileEntitySource tileEntitySource = (TileEntitySource) source;
                heldItem.setTagCompound(new NBTTagCompound());
                NBTTagCompound tag = heldItem.getTagCompound();
                tag.setUniqueId("id", tileEntitySource.getId());
                tag.setString("name", tileEntitySource.getCustomName());
                tag.setLong("pos", tileEntitySource.getPos().toLong());
                sendGameInfoMessage(player, "message.select_controller");
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.SUCCESS;
    }

    private void sendGameInfoMessage(EntityPlayer player, String message)
    {
        if(player instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketChat(new TextComponentTranslation(message), ChatType.GAME_INFO));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if(!world.isRemote)
        {
            ItemStack heldItem = player.getHeldItem(hand);
            if(player.isSneaking())
            {
                heldItem.clearCustomName();
                heldItem.setTagCompound(null);
                return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
            }
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if(stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound();
            if(tag != null)
            {
                tooltip.add(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "ID: " + TextFormatting.RESET.toString() + tag.getUniqueId("id"));
                tooltip.add(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Device: " + TextFormatting.RESET.toString() + tag.getString("name"));

                BlockPos devicePos = BlockPos.fromLong(tag.getLong("pos"));
                StringBuilder builder = new StringBuilder();
                builder.append(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "X: " + TextFormatting.RESET.toString() + devicePos.getX() + " ");
                builder.append(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Y: " + TextFormatting.RESET.toString() + devicePos.getY() + " ");
                builder.append(TextFormatting.RED.toString() + TextFormatting.BOLD.toString() + "Z: " + TextFormatting.RESET.toString() + devicePos.getZ());
                tooltip.add(builder.toString());
            }
        }
        else
        {
            if(!GuiScreen.isShiftKeyDown())
            {
                tooltip.add(TextFormatting.GRAY.toString() + "Use this cable to connect");
                tooltip.add(TextFormatting.GRAY.toString() + "lights to a controller.");
                tooltip.add(TextFormatting.YELLOW.toString() + "Hold SHIFT for How-To");
                return;
            }

            tooltip.add(TextFormatting.GRAY.toString() + "Start by right clicking a");
            tooltip.add(TextFormatting.GRAY.toString() + "light with this cable");
            tooltip.add(TextFormatting.GRAY.toString() + "then right click the ");
            tooltip.add(TextFormatting.GRAY.toString() + "controller you want to");
            tooltip.add(TextFormatting.GRAY.toString() + "connect this light to.");
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return stack.hasTagCompound();
    }

    private static double getDistance(BlockPos source, BlockPos target)
    {
        return Math.sqrt(source.distanceSqToCenter(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            return TextFormatting.GRAY.toString() + TextFormatting.BOLD.toString() + super.getItemStackDisplayName(stack);
        }
        return super.getItemStackDisplayName(stack);
    }
}
