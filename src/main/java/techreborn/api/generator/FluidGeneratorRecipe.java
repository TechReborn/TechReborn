package techreborn.api.generator;

import net.minecraftforge.fluids.Fluid;

public class FluidGeneratorRecipe {
	private Fluid fluid;
	private int energyPerMb;
	
	public FluidGeneratorRecipe(Fluid fluid, int energyPerMb)
	{
		this.fluid = fluid;
		this.energyPerMb = energyPerMb;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	public int getEnergyPerMb() {
		return energyPerMb;
	}

	public void setEnergyPerMb(int energyPerMb) {
		this.energyPerMb = energyPerMb;
	}

	@Override
	public String toString() {
		return "FluidGeneratorRecipe [fluid=" + fluid + ", energyPerMb=" + energyPerMb + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + energyPerMb;
		result = prime * result + ((fluid == null) ? 0 : fluid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FluidGeneratorRecipe other = (FluidGeneratorRecipe) obj;
		if (energyPerMb != other.energyPerMb)
			return false;
		if (fluid == null) {
			if (other.fluid != null)
				return false;
		} else if (!fluid.equals(other.fluid))
			return false;
		return true;
	}
}
