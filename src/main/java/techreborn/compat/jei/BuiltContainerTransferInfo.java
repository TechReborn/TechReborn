package techreborn.compat.jei;

import net.minecraft.inventory.Slot;

import techreborn.client.container.builder.BuiltContainer;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;

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

//	@Override
//	public boolean canHandle(final BuiltContainer container) {
//		return container.getName().equals(this.containerName);
//	}
//
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
