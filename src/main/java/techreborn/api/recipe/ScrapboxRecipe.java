package techreborn.api.recipe;

import net.minecraft.item.ItemStack;
import techreborn.api.Reference;
import techreborn.api.TechRebornAPI;

public class ScrapboxRecipe extends BaseRecipe {

	public ScrapboxRecipe(ItemStack output) {
		super(Reference.scrapboxRecipe, 0, 0);
		addInput(new ItemStack(TechRebornAPI.getItem("SCRAP_BOX")));
		addOutput(output);
	}

	@Override
	public String getUserFreindlyName() {
		return "Scrapbox";
	}
}
