package techreborn.client.compat.jei.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import reborncore.common.powerSystem.RcEnergyItem;

public class EnergyItemSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

	@Override
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		if(context == UidContext.Ingredient &&
				ingredient.getItem() instanceof RcEnergyItem energyItem &&
				energyItem.getEnergyCapacity(ingredient) > 0 &&
				energyItem.getStoredEnergy(ingredient) >= energyItem.getEnergyCapacity(ingredient)) {
			return true;
		}
		return null;
	}
}
