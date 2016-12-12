package techreborn.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.apache.commons.lang3.Validate;
import reborncore.common.util.StringUtils;

public class ItemCells {
	public static ItemStack getCellByName(String name, int count) {
		if (name.equalsIgnoreCase("empty") || name.equalsIgnoreCase("cell")) {
			return DynamicCell.getEmptyCell(16).copy();
		}
		Fluid fluid = FluidRegistry.getFluid("fluid" + name.toLowerCase());
		if (fluid == null) {
			fluid = FluidRegistry.getFluid(name.toLowerCase());
			if (fluid == null) {
				fluid = FluidRegistry.getFluid("fluid" + StringUtils.toFirstCapital(name.toLowerCase()));
			}
		}
		Validate.notNull(fluid, "The fluid " + name + " could not be found");
		return DynamicCell.getCellWithFluid(fluid, count);
	}

	public static ItemStack getCellByName(String name) {
		return getCellByName(name, 1);
	}

}
