package techreborn.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.client.hud.StackInfoElement;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.Color;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;
import techreborn.lib.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrequencyTransmitter extends ItemTRNoDestroy {

	public ItemFrequencyTransmitter() {
		setUnlocalizedName("techreborn.frequencyTransmitter");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setMaxStackSize(1);
		this.addPropertyOverride(new ResourceLocation("techreborn:coords"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack,
			                   @Nullable
				                   World worldIn,
			                   @Nullable
				                   EntityLivingBase entityIn) {
				if (stack != ItemStack.EMPTY && stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound().hasKey("x") && stack.getTagCompound().hasKey("y") && stack.getTagCompound().hasKey("z") && stack.getTagCompound().hasKey("dim")) {
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
	                                  EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			stack.setTagCompound(null);
			if (!world.isRemote && ConfigTechReborn.FreqTransmitterChat) {
				ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TextComponentString(
					TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.coordsHaveBeen") + " "
						+ TextFormatting.GOLD + I18n.translateToLocal("techreborn.message.cleared")));
			}
			return EnumActionResult.PASS;
		} else {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("x", pos.getX());
			stack.getTagCompound().setInteger("y", pos.getY());
			stack.getTagCompound().setInteger("z", pos.getZ());
			stack.getTagCompound().setInteger("dim", world.provider.getDimension());

			if (!world.isRemote && ConfigTechReborn.FreqTransmitterChat) {
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
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,
	                                                EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			stack.setTagCompound(null);
			if (!world.isRemote && ConfigTechReborn.FreqTransmitterChat) {
				ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TextComponentString(
					TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.coordsHaveBeen") + " "
						+ TextFormatting.GOLD + I18n.translateToLocal("techreborn.message.cleared")));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if (ConfigTechReborn.FreqTransmitterTooltip) {
			if (stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound().hasKey("x") && stack.getTagCompound().hasKey("y") && stack.getTagCompound().hasKey("z") && stack.getTagCompound().hasKey("dim")) {
				int x = stack.getTagCompound().getInteger("x");
				int y = stack.getTagCompound().getInteger("y");
				int z = stack.getTagCompound().getInteger("z");
				int dim = stack.getTagCompound().getInteger("dim");

				list.add("X: " + x);
				list.add("Y: " + y);
				list.add("X: " + z);
				list.add(TextFormatting.DARK_GRAY + DimensionManager.getProviderType(dim).getName());

			} else {
				list.add(TextFormatting.GRAY + I18n.translateToLocal("techreborn.message.noCoordsSet"));
			}
		}
	}

	public static class StackInfoFreqTransmitter extends StackInfoElement {
		public StackInfoFreqTransmitter() {
			super(ModItems.frequencyTransmitter);
		}

		@Override
		public String getText(ItemStack stack) {
			String text = "";
			Color gold = Color.GOLD;
			Color grey = Color.GRAY;
			if (stack.getItem() instanceof ItemFrequencyTransmitter) {
				if (stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound().hasKey("x") && stack.getTagCompound().hasKey("y") && stack.getTagCompound().hasKey("z") && stack.getTagCompound().hasKey("dim")) {
					int coordX = stack.getTagCompound().getInteger("x");
					int coordY = stack.getTagCompound().getInteger("y");
					int coordZ = stack.getTagCompound().getInteger("z");
					int coordDim = stack.getTagCompound().getInteger("dim");
					text = grey + "X: " + gold + coordX + grey + " Y: " + gold + coordY + grey + " Z: " + gold + coordZ + grey + " Dim: " + gold + DimensionManager.getProviderType(coordDim).getName() + " (" + coordDim + ")";
				} else {
					text = grey + I18n.translateToLocal("techreborn.message.noCoordsSet");
				}
			}
			return text;
		}
	}
}
