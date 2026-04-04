package techreborn.client.compat.jei.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import reborncore.common.fluid.container.ItemFluidInfo;

public class FluidItemSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

	@Override
	public Object getSubtypeData(ItemStack ingredient, UidContext context) {
		if(ingredient.getItem() instanceof ItemFluidInfo info) {
			return info.getFluid(ingredient);
		}
		return null;
	}
}
