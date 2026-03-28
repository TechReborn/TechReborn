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

import reborncore.common.chunkloading.ChunkLoaderManager;
import techreborn.component.TRDataComponentTypes;
import techreborn.init.TRItemSettings;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class FrequencyTransmitterItem extends Item {

	public FrequencyTransmitterItem(String name) {
		super(TRItemSettings.item(name).stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();

		GlobalPos globalPos = GlobalPos.of(ChunkLoaderManager.getDimensionRegistryKey(world), pos);

		stack.set(TRDataComponentTypes.FREQUENCY_TRANSMITTER, globalPos);

		if (context.getPlayer() instanceof ServerPlayer serverPlayerEntity) {
			serverPlayerEntity.sendOverlayMessage(Component.translatable("techreborn.message.setTo")
											.append(Component.literal(" X:").withStyle(ChatFormatting.GRAY))
											.append(Component.literal(String.valueOf(pos.getX())).withStyle(ChatFormatting.GOLD))
											.append(Component.literal(" Y:").withStyle(ChatFormatting.GRAY))
											.append(Component.literal(String.valueOf(pos.getY())).withStyle(ChatFormatting.GOLD))
											.append(Component.literal(" Z:").withStyle(ChatFormatting.GRAY))
											.append(Component.literal(String.valueOf(pos.getZ())).withStyle(ChatFormatting.GOLD))
											.append(" ")
											.append(Component.translatable("techreborn.message.in").withStyle(ChatFormatting.GRAY))
											.append(" ")
											.append(Component.literal(getDimName(globalPos.dimension()).toString()).withStyle(ChatFormatting.GOLD)));
		}

		return InteractionResult.SUCCESS;
	}

	public static Optional<GlobalPos> getPos(ItemStack stack) {
		return Optional.ofNullable(stack.get(TRDataComponentTypes.FREQUENCY_TRANSMITTER));
	}

	@Override
	public InteractionResult use(Level world, Player player,
											InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			stack.remove(TRDataComponentTypes.FREQUENCY_TRANSMITTER);

			if (player instanceof ServerPlayer serverPlayerEntity) {
				serverPlayerEntity.sendOverlayMessage(Component.translatable("techreborn.message.coordsHaveBeen")
												.withStyle(ChatFormatting.GRAY)
												.append(" ")
												.append(
													Component.translatable("techreborn.message.cleared")
														.withStyle(ChatFormatting.GOLD)
												));
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> tooltip, TooltipFlag type) {
		getPos(stack)
			.ifPresent(globalPos -> {
				tooltip.accept(Component.literal(ChatFormatting.GRAY + "X: " + ChatFormatting.GOLD + globalPos.pos().getX()));
				tooltip.accept(Component.literal(ChatFormatting.GRAY + "Y: " + ChatFormatting.GOLD + globalPos.pos().getY()));
				tooltip.accept(Component.literal(ChatFormatting.GRAY + "Z: " + ChatFormatting.GOLD + globalPos.pos().getZ()));
				tooltip.accept(Component.literal(ChatFormatting.DARK_GRAY + getDimName(globalPos.dimension()).toString()));
			});
	}

	private static Identifier getDimName(ResourceKey<Level> dimensionRegistryKey) {
		return dimensionRegistryKey.identifier();
	}
}
