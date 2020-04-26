/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import reborncore.client.hud.StackInfoElement;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.StringUtils;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.utils.MessageIDs;

import javax.annotation.Nullable;
import java.util.List;

public class FrequencyTransmitterItem extends Item {

	public FrequencyTransmitterItem() {
		super(new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1));
		this.addPropertyGetter(new Identifier("techreborn", "coords"), (stack, worldIn, entityIn) -> {
			if (!stack.isEmpty() && stack.hasTag() && stack.getTag() != null && stack.getTag().contains("x")
					&& stack.getTag().contains("y") && stack.getTag().contains("z") && stack.getTag().contains("dim")) {
				return 1.0F;
			}
			return 0.0F;
		});
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		ItemStack stack = context.getStack();

		stack.setTag(new CompoundTag());
		stack.getOrCreateTag().putInt("x", pos.getX());
		stack.getOrCreateTag().putInt("y", pos.getY());
		stack.getOrCreateTag().putInt("z", pos.getZ());
		stack.getOrCreateTag().putInt("dim", world.getDimension().getType().getRawId());

		if (!world.isClient) {
			new TranslatableText("techreborn.message.setTo")
					.append(new LiteralText(" X:").formatted(Formatting.GRAY))
					.append(new LiteralText(String.valueOf(pos.getX())).formatted(Formatting.GOLD))
					.append(new LiteralText(" Y:").formatted(Formatting.GRAY))
					.append(new LiteralText(String.valueOf(pos.getY())).formatted(Formatting.GOLD))
					.append(new LiteralText(" Z:").formatted(Formatting.GRAY))
					.append(new LiteralText(String.valueOf(pos.getZ())).formatted(Formatting.GOLD))
					.append(" ")
					.append(new TranslatableText("techreborn.message.in").formatted(Formatting.GRAY))
					.append(new LiteralText(getDimName(world.getDimension().getType()).toString()).formatted(Formatting.GOLD));
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

				ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID,
						new TranslatableText("techreborn.message.coordsHaveBeen")
							.formatted(Formatting.GRAY)
							.append(" ")
							.append(
								new TranslatableText("techreborn.message.cleared")
									.formatted(Formatting.GOLD)
							)
				);
			}
		}

		return new TypedActionResult<>(ActionResult.SUCCESS, stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		if (stack.hasTag() && stack.getTag() != null && stack.getTag().contains("x") && stack.getTag().contains("y") && stack.getTag().contains("z") && stack.getTag().contains("dim")) {
			int x = stack.getTag().getInt("x");
			int y = stack.getTag().getInt("y");
			int z = stack.getTag().getInt("z");
			int dim = stack.getTag().getInt("dim");

			tooltip.add(new LiteralText(Formatting.GRAY + "X: " + Formatting.GOLD + x));
			tooltip.add(new LiteralText(Formatting.GRAY + "Y: " + Formatting.GOLD + y));
			tooltip.add(new LiteralText(Formatting.GRAY + "Z: " + Formatting.GOLD + z));
			tooltip.add(new LiteralText(Formatting.DARK_GRAY + getDimName(Registry.DIMENSION_TYPE.get(dim)).toString()));

		} else {
			tooltip.add(new TranslatableText("techreborn.message.noCoordsSet").formatted(Formatting.GRAY));
		}
	}

	private static Identifier getDimName(DimensionType type){
		return Registry.DIMENSION_TYPE.getId(type);
	}

	public static class StackInfoFreqTransmitter extends StackInfoElement {
		public StackInfoFreqTransmitter() {
			super(TRContent.FREQUENCY_TRANSMITTER);
		}

		@Override
		public Text getText(ItemStack stack) {
			MutableText text = new LiteralText("");
			Formatting gold = Formatting.GOLD;
			Formatting grey = Formatting.GRAY;
			if (stack.getItem() instanceof FrequencyTransmitterItem) {
				if (stack.hasTag() && stack.getTag() != null && stack.getTag().contains("x") && stack.getTag().contains("y") && stack.getTag().contains("z") && stack.getTag().contains("dim")) {
					int coordX = stack.getTag().getInt("x");
					int coordY = stack.getTag().getInt("y");
					int coordZ = stack.getTag().getInt("z");
					int coordDim = stack.getTag().getInt("dim");
					text = text.append(new LiteralText(grey + "X: " + gold + coordX + grey + " Y: " + gold + coordY + grey + " Z: " + gold + coordZ + grey + " Dim: " + gold + getDimName(Registry.DIMENSION_TYPE.get(coordDim)).toString() + " (" + coordDim + ")"));
				} else {
					text = text.append(new TranslatableText("techreborn.message.noCoordsSet").formatted(Formatting.GRAY));
				}
			}
			return text;
		}
	}
}
