package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidDeuterium extends BlockFluidBase{

	public BlockFluidDeuterium(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.deuterium");
	}

}
