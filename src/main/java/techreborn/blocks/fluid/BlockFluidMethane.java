package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidMethane extends BlockFluidBase{

	public BlockFluidMethane(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.methane");
	}

}
