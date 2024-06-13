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

import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.chunkloading.ChunkLoaderManager;
import techreborn.component.TRDataComponentTypes;

import java.util.List;
import java.util.Optional;

public class FrequencyTransmitterItem extends Item {

	public FrequencyTransmitterItem() {
		super(new Item.Settings().maxCount(1));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		ItemStack stack = context.getStack();

		GlobalPos globalPos = GlobalPos.create(ChunkLoaderManager.getDimensionRegistryKey(world), pos);

		stack.set(TRDataComponentTypes.FREQUENCY_TRANSMITTER, globalPos);

		if (context.getPlayer() instanceof ServerPlayerEntity serverPlayerEntity) {
			serverPlayerEntity.sendMessage(Text.translatable("techreborn.message.setTo")
											.append(Text.literal(" X:").formatted(Formatting.GRAY))
											.append(Text.literal(String.valueOf(pos.getX())).formatted(Formatting.GOLD))
											.append(Text.literal(" Y:").formatted(Formatting.GRAY))
											.append(Text.literal(String.valueOf(pos.getY())).formatted(Formatting.GOLD))
											.append(Text.literal(" Z:").formatted(Formatting.GRAY))
											.append(Text.literal(String.valueOf(pos.getZ())).formatted(Formatting.GOLD))
											.append(" ")
											.append(Text.translatable("techreborn.message.in").formatted(Formatting.GRAY))
											.append(" ")
											.append(Text.literal(getDimName(globalPos.dimension()).toString()).formatted(Formatting.GOLD)), true);
		}

		return ActionResult.SUCCESS;
	}

	public static Optional<GlobalPos> getPos(ItemStack stack) {
		return Optional.ofNullable(stack.get(TRDataComponentTypes.FREQUENCY_TRANSMITTER));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player,
											Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			stack.remove(TRDataComponentTypes.FREQUENCY_TRANSMITTER);

			if (player instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.sendMessage(Text.translatable("techreborn.message.coordsHaveBeen")
												.formatted(Formatting.GRAY)
												.append(" ")
												.append(
													Text.translatable("techreborn.message.cleared")
														.formatted(Formatting.GOLD)
												), true);
			}
		}

		return new TypedActionResult<>(ActionResult.SUCCESS, stack);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		super.appendTooltip(stack, context, tooltip, type);
		getPos(stack)
			.ifPresent(globalPos -> {
				tooltip.add(Text.literal(Formatting.GRAY + "X: " + Formatting.GOLD + globalPos.pos().getX()));
				tooltip.add(Text.literal(Formatting.GRAY + "Y: " + Formatting.GOLD + globalPos.pos().getY()));
				tooltip.add(Text.literal(Formatting.GRAY + "Z: " + Formatting.GOLD + globalPos.pos().getZ()));
				tooltip.add(Text.literal(Formatting.DARK_GRAY + getDimName(globalPos.dimension()).toString()));
			});
	}

	private static Identifier getDimName(RegistryKey<World> dimensionRegistryKey) {
		return dimensionRegistryKey.getValue();
	}
}
