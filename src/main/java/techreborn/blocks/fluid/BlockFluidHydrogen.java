package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidHydrogen extends BlockFluidBase{

	public BlockFluidHydrogen(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.hydrogen");
	}

}
