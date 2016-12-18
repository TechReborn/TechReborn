package techreborn.api.generator;

import net.minecraftforge.fluids.Fluid;

public class FluidGeneratorRecipe {
	private final EFluidGenerator generatorType;
	private final Fluid fluid;
	private final int energyPerMb;
	
	public FluidGeneratorRecipe(Fluid fluid, int energyPerMb, EFluidGenerator generatorType)
	{
		this.fluid = fluid;
		this.energyPerMb = energyPerMb;
		this.generatorType = generatorType;
	}

	public Fluid getFluid() {
		return fluid;
	}
	public int getEnergyPerMb() {
		return energyPerMb;
	}

	public EFluidGenerator getGeneratorType() {
		return generatorType;
	}

	@Override
	public String toString() {
		return "FluidGeneratorRecipe [generatorType=" + generatorType + ", fluid=" + fluid + ", energyPerMb="
				+ energyPerMb + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + energyPerMb;
		result = prime * result + ((fluid == null) ? 0 : fluid.hashCode());
		result = prime * result + ((generatorType == null) ? 0 : generatorType.hashCode());
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
		if (generatorType != other.generatorType)
			return false;
		return true;
	}
}
