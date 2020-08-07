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
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import reborncore.common.chunkloading.ChunkLoaderManager;
import reborncore.common.util.ChatUtils;
import techreborn.TechReborn;
import techreborn.utils.MessageIDs;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Optional;

public class FrequencyTransmitterItem extends Item {

	public FrequencyTransmitterItem() {
		super(new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		ItemStack stack = context.getStack();

		GlobalPos globalPos = GlobalPos.create(ChunkLoaderManager.getDimensionRegistryKey(world), pos);

		GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, globalPos).result()
				.ifPresent(tag -> stack.getOrCreateTag().put("pos", tag));

		if (!world.isClient) {

			ChatUtils.sendNoSpamMessages(MessageIDs.freqTransmitterID, new TranslatableText("techreborn.message.setTo")
					.append(new LiteralText(" X:").formatted(Formatting.GRAY))
					.append(new LiteralText(String.valueOf(pos.getX())).formatted(Formatting.GOLD))
					.append(new LiteralText(" Y:").formatted(Formatting.GRAY))
					.append(new LiteralText(String.valueOf(pos.getY())).formatted(Formatting.GOLD))
					.append(new LiteralText(" Z:").formatted(Formatting.GRAY))
					.append(new LiteralText(String.valueOf(pos.getZ())).formatted(Formatting.GOLD))
					.append(" ")
					.append(new TranslatableText("techreborn.message.in").formatted(Formatting.GRAY))
					.append(" ")
					.append(new LiteralText(getDimName(globalPos.getDimension()).toString()).formatted(Formatting.GOLD)));
		}
		return ActionResult.SUCCESS;
	}

	public static Optional<GlobalPos> getPos(ItemStack stack) {
		if (!stack.hasTag() || !stack.getOrCreateTag().contains("pos")) {
			return Optional.empty();
		}
		return GlobalPos.CODEC.parse(NbtOps.INSTANCE, stack.getOrCreateTag().getCompound("pos")).result();
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
		getPos(stack)
				.ifPresent(globalPos -> {
					tooltip.add(new LiteralText(Formatting.GRAY + "X: " + Formatting.GOLD + globalPos.getPos().getX()));
					tooltip.add(new LiteralText(Formatting.GRAY + "Y: " + Formatting.GOLD + globalPos.getPos().getY()));
					tooltip.add(new LiteralText(Formatting.GRAY + "Z: " + Formatting.GOLD + globalPos.getPos().getZ()));
					tooltip.add(new LiteralText(Formatting.DARK_GRAY + getDimName(globalPos.getDimension()).toString()));
				});
	}

	private static Identifier getDimName(RegistryKey<World> dimensionRegistryKey) {
		return dimensionRegistryKey.getValue();
	}
}
