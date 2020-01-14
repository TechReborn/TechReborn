/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.client.hud.StackInfoElement;
import reborncore.common.util.ChatUtils;
import techreborn.init.ModItems;
import techreborn.lib.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrequencyTransmitter extends ItemTR {

	public ItemFrequencyTransmitter() {
		setTranslationKey("techreborn.frequencyTransmitter");
		setMaxStackSize(1);
		this.addPropertyOverride(new ResourceLocation("techreborn:coords"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack,
			                   @Nullable
				                   World worldIn,
			                   @Nullable
				                   EntityLivingBase entityIn) {
				if (!stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound().hasKey("x") && stack.getTagCompound().hasKey("y") && stack.getTagCompound().hasKey("z") && stack.getTagCompound().hasKey("dim")) {
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
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("x", pos.getX());
		stack.getTagCompound().setInteger("y", pos.getY());
		stack.getTagCompound().setInteger("z", pos.getZ());
		stack.getTagCompound().setInteger("dim", world.provider.getDimension());

		if (!world.isRemote) {
			ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TextComponentString(
				TextFormatting.GRAY + I18n.format("techreborn.message.setTo") + " X: " +
					TextFormatting.GOLD + pos.getX() +
					TextFormatting.GRAY + " Y: " +
					TextFormatting.GOLD + pos.getY() +
					TextFormatting.GRAY + " Z: " +
					TextFormatting.GOLD + pos.getZ() +
					TextFormatting.GRAY + " " + I18n.format("techreborn.message.in") + " " +
					TextFormatting.GOLD + DimensionManager.getProviderType(world.provider.getDimension())
					.getName() + " (" + world.provider.getDimension() + ")"));
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,
	                                                EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			stack.setTagCompound(null);
			if (!world.isRemote) {
				ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TextComponentString(
					TextFormatting.GRAY + I18n.format("techreborn.message.coordsHaveBeen") + " "
						+ TextFormatting.GOLD + I18n.format("techreborn.message.cleared")));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound().hasKey("x") && stack.getTagCompound().hasKey("y") && stack.getTagCompound().hasKey("z") && stack.getTagCompound().hasKey("dim")) {
			int x = stack.getTagCompound().getInteger("x");
			int y = stack.getTagCompound().getInteger("y");
			int z = stack.getTagCompound().getInteger("z");
			int dim = stack.getTagCompound().getInteger("dim");

			tooltip.add(TextFormatting.GRAY + "X: " + TextFormatting.GOLD + x);
			tooltip.add(TextFormatting.GRAY + "Y: " + TextFormatting.GOLD + y);
			tooltip.add(TextFormatting.GRAY + "Z: " + TextFormatting.GOLD + z);
			tooltip.add(TextFormatting.DARK_GRAY + DimensionManager.getProviderType(dim).getName());

		} else {
			tooltip.add(TextFormatting.GRAY + I18n.format("techreborn.message.noCoordsSet"));
		}
	}

	public static class StackInfoFreqTransmitter extends StackInfoElement {
		public StackInfoFreqTransmitter() {
			super(ModItems.FREQUENCY_TRANSMITTER);
		}

		@Override
		public String getText(ItemStack stack) {
			String text = "";
			TextFormatting gold = TextFormatting.GOLD;
			TextFormatting grey = TextFormatting.GRAY;
			if (stack.getItem() instanceof ItemFrequencyTransmitter) {
				if (stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound().hasKey("x") && stack.getTagCompound().hasKey("y") && stack.getTagCompound().hasKey("z") && stack.getTagCompound().hasKey("dim")) {
					int coordX = stack.getTagCompound().getInteger("x");
					int coordY = stack.getTagCompound().getInteger("y");
					int coordZ = stack.getTagCompound().getInteger("z");
					int coordDim = stack.getTagCompound().getInteger("dim");
					text = grey + "X: " + gold + coordX + grey + " Y: " + gold + coordY + grey + " Z: " + gold + coordZ + grey + " Dim: " + gold + DimensionManager.getProviderType(coordDim).getName() + " (" + coordDim + ")";
				} else {
					text = grey + I18n.format("techreborn.message.noCoordsSet");
				}
			}
			return text;
		}
	}
}
