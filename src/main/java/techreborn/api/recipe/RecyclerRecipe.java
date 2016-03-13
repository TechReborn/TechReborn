package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import techreborn.items.ItemParts;
import techreborn.lib.Reference;
//THIS is only here to trick JEI into showing recipes for the recycler
public class RecyclerRecipe extends BaseRecipe {

	public RecyclerRecipe(ItemStack input) {
		super(Reference.recyclerRecipe, 0, 0);
		inputs.add(input);
		addOutput(ItemParts.getPartByName("scrap"));
	}

	@Override
	public String getUserFreindlyName() {
		return "Recycler";
	}
}
