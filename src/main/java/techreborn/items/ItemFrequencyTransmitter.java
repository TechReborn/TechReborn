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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;
import reborncore.client.hud.StackInfoElement;
import reborncore.common.util.ChatUtils;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrequencyTransmitter extends Item {

	public ItemFrequencyTransmitter() {
		super(new Item.Properties().group(TechReborn.ITEMGROUP).maxStackSize(1));
		this.addPropertyOverride(new ResourceLocation("techreborn", "coords"), new IItemPropertyGetter() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public float call(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null && stack.getTag().contains("x")
						&& stack.getTag().contains("y") && stack.getTag().contains("z") && stack.getTag().contains("dim")) {
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}

	@Override
	public EnumActionResult onItemUse(ItemUseContext context) {
		EntityPlayer player = context.getPlayer();
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		ItemStack stack = context.getItem();

		stack.setTag(new NBTTagCompound());
		stack.getTag().putInt("x", pos.getX());
		stack.getTag().putInt("y", pos.getY());
		stack.getTag().putInt("z", pos.getZ());
		stack.getTag().putInt("dim", world.getDimension().getType().getId());

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
					.getName() + " (" + world.getDimension().getType().getRegistryName() + ")"));
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player,
	                                                EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			stack.setTag(null);
			if (!world.isRemote) {
				ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TextComponentString(
					TextFormatting.GRAY + I18n.format("techreborn.message.coordsHaveBeen") + " "
						+ TextFormatting.GOLD + I18n.format("techreborn.message.cleared")));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTag() && stack.getTag() != null && stack.getTag().contains("x") && stack.getTag().contains("y") && stack.getTag().contains("z") && stack.getTag().contains("dim")) {
			int x = stack.getTag().getInt("x");
			int y = stack.getTag().getInt("y");
			int z = stack.getTag().getInt("z");
			int dim = stack.getTag().getInt("dim");

			tooltip.add(new TextComponentString(TextFormatting.GRAY + "X: " + TextFormatting.GOLD + x));
			tooltip.add(new TextComponentString(TextFormatting.GRAY + "Y: " + TextFormatting.GOLD + y));
			tooltip.add(new TextComponentString(TextFormatting.GRAY + "Z: " + TextFormatting.GOLD + z));
			tooltip.add(new TextComponentString(TextFormatting.DARK_GRAY + DimensionManager.getProviderType(dim).getName()));

		} else {
			tooltip.add(new TextComponentString(TextFormatting.GRAY + I18n.format("techreborn.message.noCoordsSet")));
		}
	}

	public static class StackInfoFreqTransmitter extends StackInfoElement {
		public StackInfoFreqTransmitter() {
			super(TRContent.FREQUENCY_TRANSMITTER);
		}

		@Override
		public String getText(ItemStack stack) {
			String text = "";
			TextFormatting gold = TextFormatting.GOLD;
			TextFormatting grey = TextFormatting.GRAY;
			if (stack.getItem() instanceof ItemFrequencyTransmitter) {
				if (stack.hasTag() && stack.getTag() != null && stack.getTag().contains("x") && stack.getTag().contains("y") && stack.getTag().contains("z") && stack.getTag().contains("dim")) {
					int coordX = stack.getTag().getInt("x");
					int coordY = stack.getTag().getInt("y");
					int coordZ = stack.getTag().getInt("z");
					int coordDim = stack.getTag().getInt("dim");
					text = grey + "X: " + gold + coordX + grey + " Y: " + gold + coordY + grey + " Z: " + gold + coordZ + grey + " Dim: " + gold + DimensionManager.getProviderType(coordDim).getName() + " (" + coordDim + ")";
				} else {
					text = grey + I18n.format("techreborn.message.noCoordsSet");
				}
			}
			return text;
		}
	}
}
