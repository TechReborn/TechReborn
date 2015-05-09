package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidNitrogen extends BlockFluidBase{

	public BlockFluidNitrogen(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.nitrogen");
	}

}
