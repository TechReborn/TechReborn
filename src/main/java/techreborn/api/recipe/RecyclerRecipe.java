package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.TechRebornAPI;

//THIS is only here to trick JEI into showing recipes for the recycler
public class RecyclerRecipe extends BaseRecipe {

	public RecyclerRecipe(ItemStack input) {
		super(Reference.recyclerRecipe, 0, 0);
		inputs.add(input);
		addOutput(TechRebornAPI.subItemRetriever.getPartByName("scrap"));
	}

	@Override
	public String getUserFreindlyName() {
		return "Recycler";
	}
}
