package techreborn.api.recipe.recipes;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import reborncore.common.crafting.Recipe;
import reborncore.common.crafting.RecipeType;
import techreborn.tiles.machine.multiblock.TileIndustrialSawmill;

public class IndustrialSawmillRecipe extends Recipe {

	//TODO 1.14 fluids
	FluidStack fluidStack = null;

	public IndustrialSawmillRecipe(RecipeType type, ResourceLocation name) {
		super(type, name);
	}

	@Override
	public boolean canCraft(TileEntity tileEntity) {
		TileIndustrialSawmill tile = (TileIndustrialSawmill) tileEntity;
		if (!tile.getMutliBlock()) {
			return false;
		}
		final FluidStack recipeFluid = fluidStack;
		final FluidStack tankFluid = tile.tank.getFluid();
		if (fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.isFluidEqual(recipeFluid)) {
			if (tankFluid.amount >= recipeFluid.amount) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(TileEntity tileEntity) {
		TileIndustrialSawmill tile = (TileIndustrialSawmill) tileEntity;
		final FluidStack recipeFluid = fluidStack;
		final FluidStack tankFluid = tile.tank.getFluid();
		if (fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.isFluidEqual(recipeFluid)) {
			if (tankFluid.amount >= recipeFluid.amount) {
				if (tankFluid.amount == recipeFluid.amount) {
					tile.tank.setFluid(null);
				} else {
					tankFluid.amount -= recipeFluid.amount;
				}
				tile.syncWithAll();
				return true;
			}
		}
		return false;
	}
}
