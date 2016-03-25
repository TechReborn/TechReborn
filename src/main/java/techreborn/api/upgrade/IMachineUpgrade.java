package techreborn.api.upgrade;

import net.minecraft.item.ItemStack;
import techreborn.api.recipe.RecipeCrafter;

public interface IMachineUpgrade
{

	public void processUpgrade(RecipeCrafter crafter, ItemStack stack);
}
