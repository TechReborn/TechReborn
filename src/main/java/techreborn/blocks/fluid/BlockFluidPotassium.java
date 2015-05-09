package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidPotassium extends BlockFluidBase{

	public BlockFluidPotassium(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.potassium");
	}

}
