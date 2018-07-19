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

package techreborn.compat.compat.jei;

import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;
import techreborn.client.container.builder.BuiltContainer;

import java.util.ArrayList;
import java.util.List;

public class BuiltContainerTransferInfo implements IRecipeTransferInfo<BuiltContainer> {

	private final String containerName, recipeCategory;

	private final int recipeSlotStart, recipeSlotCount, inventorySlotStart, inventorySlotCount;

	public BuiltContainerTransferInfo(final String containerName, final String recipeCategory,
	                                  final int recipeSlotStart, final int recipeSlotCount, final int inventorySlotStart,
	                                  final int inventorySlotCount) {
		this.containerName = containerName;
		this.recipeCategory = recipeCategory;

		this.recipeSlotStart = recipeSlotStart;
		this.recipeSlotCount = recipeSlotCount;

		this.inventorySlotStart = inventorySlotStart;
		this.inventorySlotCount = inventorySlotCount;
	}

	@Override
	public Class<BuiltContainer> getContainerClass() {
		return BuiltContainer.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return this.recipeCategory;
	}

	@Override
	public boolean canHandle(final BuiltContainer container) {
		return container.getName().equals(this.containerName);
	}

	@Override
	public List<Slot> getRecipeSlots(final BuiltContainer container) {
		final List<Slot> slots = new ArrayList<>();
		for (int i = this.recipeSlotStart; i < this.recipeSlotStart + this.recipeSlotCount; i++)
			slots.add(container.getSlot(i));
		return slots;
	}

	@Override
	public List<Slot> getInventorySlots(final BuiltContainer container) {
		final List<Slot> slots = new ArrayList<>();
		for (int i = this.inventorySlotStart; i < this.inventorySlotStart + this.inventorySlotCount; i++)
			slots.add(container.getSlot(i));
		return slots;
	}
}
