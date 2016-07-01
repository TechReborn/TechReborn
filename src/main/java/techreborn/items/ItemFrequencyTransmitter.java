package techreborn.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import reborncore.RebornCore;
import reborncore.common.util.ChatUtils;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.config.ConfigTechReborn;
import techreborn.lib.MessageIDs;

import java.util.List;

public class ItemFrequencyTransmitter extends ItemTextureBase implements ITexturedItem
{

	public ItemFrequencyTransmitter()
	{
		setUnlocalizedName("techreborn.frequencyTransmitter");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setMaxStackSize(1);
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("x", pos.getX());
		stack.getTagCompound().setInteger("y", pos.getY());
		stack.getTagCompound().setInteger("z", pos.getZ());
		stack.getTagCompound().setInteger("dim", world.provider.getDimension());

		if (!world.isRemote && ConfigTechReborn.FreqTransmitterChat)
		{
			ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TextComponentString(
					TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.setTo") + " X: " +
							TextFormatting.GOLD + pos.getX() +
							TextFormatting.GRAY + " Y: " +
							TextFormatting.GOLD + pos.getY() +
							TextFormatting.GRAY + " Z: " +
							TextFormatting.GOLD + pos.getZ() +
							TextFormatting.GRAY + " " + I18n.translateToLocal("techreborn.message.in") + " " +
							TextFormatting.GOLD + DimensionManager.getProviderType(world.provider.getDimension())
							.getName() + " (" + world.provider.getDimension() + ")"));
		}
		return EnumActionResult.SUCCESS;
	}

	@Override public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player,
			EnumHand hand)
	{
		if (player.isSneaking())
		{
			stack.setTagCompound(null);
			if (!world.isRemote && ConfigTechReborn.FreqTransmitterChat)
			{
				ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TextComponentString(
						TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.coordsHaveBeen") + " "
								+ TextFormatting.GOLD + I18n.translateToLocal("techreborn.message.cleared")));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if (ConfigTechReborn.FreqTransmitterTooltip)
		{
			if (stack.getTagCompound() != null)
			{
				int x = stack.getTagCompound().getInteger("x");
				int y = stack.getTagCompound().getInteger("y");
				int z = stack.getTagCompound().getInteger("z");
				int dim = stack.getTagCompound().getInteger("dim");

				list.add("X: " + x);
				list.add("Y: " + y);
				list.add("X: " + z);
				list.add(TextFormatting.DARK_GRAY + DimensionManager.getProviderType(dim).getName());

			} else
			{
				list.add(TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.noCoordsSet"));
			}
		}
	}

	@Override public int getMaxMeta()
	{
		return 1;
	}

	@Override public String getTextureName(int arg0)
	{
		return "techreborn:items/tool/frequency_transmitter";
	}
}
