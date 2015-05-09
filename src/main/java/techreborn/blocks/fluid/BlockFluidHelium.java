package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidHelium extends BlockFluidBase{

	public BlockFluidHelium(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.helium");
	}

}
