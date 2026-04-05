/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.client.compat.jei.recipe.transfer;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.commons.lang3.Range;

import it.unimi.dsi.fastutil.ints.IntImmutableList;
import it.unimi.dsi.fastutil.ints.IntList;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;

public record BuiltScreenHandlerTransferInfo<R>(String name, IRecipeType<R> recipeType, IntList recipeSlotOffsets) implements IRecipeTransferInfo<BuiltScreenHandler, R> {

	public BuiltScreenHandlerTransferInfo(String name, IRecipeType<R> recipeType, IntStream recipeSlotOffsets) {
		this(name, recipeType, IntImmutableList.toList(recipeSlotOffsets));
	}

	@Override
	public Class<BuiltScreenHandler> getContainerClass() {
		return BuiltScreenHandler.class;
	}

	@Override
	public Optional<MenuType<BuiltScreenHandler>> getMenuType() {
		return Optional.empty();
	}

	@Override
	public IRecipeType<R> getRecipeType() {
		return recipeType;
	}

	@Override
	public boolean canHandle(BuiltScreenHandler screenHandler, R recipe) {
		List<Range<Integer>> playerSlotRanges = screenHandler.getPlayerSlotRanges();
		List<Range<Integer>> blockEntitySlotRanges = screenHandler.getBlockEntitySlotRanges();
		return screenHandler.getName().equals(name) && !playerSlotRanges.isEmpty() && !blockEntitySlotRanges.isEmpty();
	}

	@Override
	public List<Slot> getRecipeSlots(BuiltScreenHandler screenHandler, R recipe) {
		Range<Integer> blockEntitySlotRange = screenHandler.getBlockEntitySlotRanges().get(0);
		MachineBaseBlockEntity blockEntity = screenHandler.getBlockEntity();
		int baseOffset = blockEntitySlotRange.getMinimum();
		int upgradeOffset = blockEntity.canBeUpgraded() ? blockEntity.getUpgradeSlotCount() : 0;
		return recipeSlotOffsets.intStream().map(i -> i + baseOffset + upgradeOffset).mapToObj(screenHandler::getSlot).toList();
	}

	@Override
	public List<Slot> getInventorySlots(BuiltScreenHandler screenHandler, R recipe) {
		Range<Integer> playerSlotRange = screenHandler.getPlayerSlotRanges().get(0);
		return IntStream.rangeClosed(playerSlotRange.getMinimum(), playerSlotRange.getMaximum()).mapToObj(screenHandler::getSlot).toList();
	}
}
