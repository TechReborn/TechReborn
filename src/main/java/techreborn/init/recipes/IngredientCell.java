package techreborn.init.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import techreborn.items.ItemCells;

import javax.annotation.Nullable;

public class IngredientCell extends Ingredient {

	public IngredientCell(ItemStack... stacks) {
		super(stacks);
	}

	@Override
	public boolean apply(@Nullable ItemStack itemStack) {
		if(super.apply(itemStack)){
			return ItemCells.isCellEqual(itemStack, this.getMatchingStacks()[0]);
		}
		return false;
	}
}
