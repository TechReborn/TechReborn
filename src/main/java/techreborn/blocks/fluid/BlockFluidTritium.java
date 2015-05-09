package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidTritium extends BlockFluidBase{

	public BlockFluidTritium(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.tritium");
	}

}
