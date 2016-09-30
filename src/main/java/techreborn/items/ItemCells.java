package techreborn.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.apache.commons.lang3.Validate;
import reborncore.common.util.StringUtils;
import techreborn.init.ModItems;

public class ItemCells {

	@Deprecated
	public static final String[] types = new String[] { "Berylium", "biomass", "calciumCarbonate", "calcium", "carbon",
		"chlorine", "deuterium", "diesel", "ethanol", "glyceryl", "helium3", "helium", "heliumPlasma", "hydrogen",
		"ice", "lithium", "mercury", "methane", "nitrocarbon", "nitroCoalfuel", "nitroDiesel", "nitrogen",
		"nitrogenDioxide", "oil", "potassium", "seedOil", "silicon", "sodium", "sodiumPersulfate", "sodiumSulfide",
		"sulfur", "sulfuricAcid", "tritium", "wolframium", "empty", "lava", "water" };

	@Deprecated
	public static ItemStack getCellByName(String name, int count, boolean lookForIC2) {
		return getCellByName(name, count);
	}

	public static ItemStack getCellByName(String name, int count) {
		if (name.equalsIgnoreCase("empty") || name.equalsIgnoreCase("cell")) {
			return new ItemStack(ModItems.emptyCell, count);
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
