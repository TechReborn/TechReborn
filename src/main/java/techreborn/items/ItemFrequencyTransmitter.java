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

import net.minecraft.ChatFormat;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import reborncore.client.hud.StackInfoElement;
import reborncore.common.util.ChatUtils;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrequencyTransmitter extends Item {

	public ItemFrequencyTransmitter() {
		super(new Item.Settings().itemGroup(TechReborn.ITEMGROUP).stackSize(1));
		this.addProperty(new Identifier("techreborn", "coords"), new ItemPropertyGetter() {
			@Override
			@Environment(EnvType.CLIENT)
			public float call(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
				if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null && stack.getTag().containsKey("x")
						&& stack.getTag().containsKey("y") && stack.getTag().containsKey("z") && stack.getTag().containsKey("dim")) {
					return 1.0F;
				}
				return 0.0F;
			}
		});
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		ItemStack stack = context.getItemStack();

		stack.setTag(new CompoundTag());
		stack.getTag().putInt("x", pos.getX());
		stack.getTag().putInt("y", pos.getY());
		stack.getTag().putInt("z", pos.getZ());
		stack.getTag().putInt("dim", world.getDimension().getType().getRawId());

		if (!world.isClient) {
			ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TextComponent(
				ChatFormat.GRAY + I18n.translate("techreborn.message.setTo") + " X: " +
					ChatFormat.GOLD + pos.getX() +
					ChatFormat.GRAY + " Y: " +
					ChatFormat.GOLD + pos.getY() +
					ChatFormat.GRAY + " Z: " +
					ChatFormat.GOLD + pos.getZ() +
					ChatFormat.GRAY + " " + I18n.translate("techreborn.message.in") + " " +
					ChatFormat.GOLD + world.getDimension().getType().getRegistryName()
					+ " (" + world.getDimension().getType().getRegistryName() + ")"));
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player,
	                                                Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			stack.setTag(null);
			if (!world.isClient) {
				ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TextComponent(
					ChatFormat.GRAY + I18n.translate("techreborn.message.coordsHaveBeen") + " "
						+ ChatFormat.GOLD + I18n.translate("techreborn.message.cleared")));
			}
		}

		return new TypedActionResult<>(ActionResult.SUCCESS, stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void buildTooltip(ItemStack stack, @Nullable World worldIn, List<Component> tooltip, TooltipContext flagIn) {
		if (stack.hasTag() && stack.getTag() != null && stack.getTag().containsKey("x") && stack.getTag().containsKey("y") && stack.getTag().containsKey("z") && stack.getTag().containsKey("dim")) {
			int x = stack.getTag().getInt("x");
			int y = stack.getTag().getInt("y");
			int z = stack.getTag().getInt("z");
			int dim = stack.getTag().getInt("dim");

			tooltip.add(new TextComponent(ChatFormat.GRAY + "X: " + ChatFormat.GOLD + x));
			tooltip.add(new TextComponent(ChatFormat.GRAY + "Y: " + ChatFormat.GOLD + y));
			tooltip.add(new TextComponent(ChatFormat.GRAY + "Z: " + ChatFormat.GOLD + z));
			tooltip.add(new TextComponent(ChatFormat.DARK_GRAY + Registry.DIMENSION.get(dim).getRegistryName().toString()));

		} else {
			tooltip.add(new TextComponent(ChatFormat.GRAY + I18n.translate("techreborn.message.noCoordsSet")));
		}
	}

	public static class StackInfoFreqTransmitter extends StackInfoElement {
		public StackInfoFreqTransmitter() {
			super(TRContent.FREQUENCY_TRANSMITTER);
		}

		@Override
		public String getText(ItemStack stack) {
			String text = "";
			ChatFormat gold = ChatFormat.GOLD;
			ChatFormat grey = ChatFormat.GRAY;
			if (stack.getItem() instanceof ItemFrequencyTransmitter) {
				if (stack.hasTag() && stack.getTag() != null && stack.getTag().containsKey("x") && stack.getTag().containsKey("y") && stack.getTag().containsKey("z") && stack.getTag().containsKey("dim")) {
					int coordX = stack.getTag().getInt("x");
					int coordY = stack.getTag().getInt("y");
					int coordZ = stack.getTag().getInt("z");
					int coordDim = stack.getTag().getInt("dim");
					text = grey + "X: " + gold + coordX + grey + " Y: " + gold + coordY + grey + " Z: " + gold + coordZ + grey + " Dim: " + gold + Registry.DIMENSION.get(coordDim).getRegistryName().toString() + " (" + coordDim + ")";
				} else {
					text = grey + I18n.translate("techreborn.message.noCoordsSet");
				}
			}
			return text;
		}
	}
}
