package techreborn.api.recipe.machines;


import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.BaseRecipe;
import techreborn.tiles.TileGrinder;

public class GrinderRecipe extends BaseRecipe {

	public FluidStack fluidStack;

		public GrinderRecipe(ItemStack input1, FluidStack fluidStack, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4, int tickTime, int euPerTick)
		{
			super("grinderRecipe", tickTime, euPerTick);
			if(input1 != null)
				inputs.add(input1);
			if(output1 != null)
				outputs.add(output1);
			if(output2 != null)
				outputs.add(output2);
			if(output3 != null)
				outputs.add(output3);
			if(output4 != null)
				outputs.add(output4);
			this.fluidStack = fluidStack;
		}

	@Override
	public boolean canCraft(TileEntity tile) {
		if(tile instanceof TileGrinder){
			TileGrinder grinder = (TileGrinder) tile;
			if(grinder.tank.getFluid() == null){
				return false;
			}
			if(grinder.tank.getFluid().getFluidID() == fluidStack.getFluidID()){
				if(grinder.tank.getFluidAmount() >= fluidStack.amount){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(TileEntity tile) {
		if(tile instanceof TileGrinder) {
			TileGrinder grinder = (TileGrinder) tile;
			if(grinder.tank.getFluid() == null){
				return false;
			}
			if(grinder.tank.getFluid().getFluidID() == fluidStack.getFluidID()){
				if(grinder.tank.getFluidAmount() >= fluidStack.amount){
					if(grinder.tank.getFluidAmount() > 0){
						grinder.tank.setFluid(new FluidStack(fluidStack.getFluid(), grinder.tank.getFluidAmount() - fluidStack.amount));
					} else {
						grinder.tank.setFluid(null);
					}
					return true;
				}
			}
		}
		return false;
	}
}
