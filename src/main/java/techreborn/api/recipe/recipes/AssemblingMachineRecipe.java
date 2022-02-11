package techreborn.api.recipe.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import techreborn.init.TRContent;

import java.util.List;

public class AssemblingMachineRecipe extends RebornRecipe {

	public AssemblingMachineRecipe(RebornRecipeType<?> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
		super(type, name, ingredients, outputs, power, time);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(TRContent.Machine.ASSEMBLY_MACHINE);
	}

}
