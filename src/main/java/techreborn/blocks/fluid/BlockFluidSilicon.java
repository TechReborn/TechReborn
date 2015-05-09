package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidSilicon extends BlockFluidBase{

	public BlockFluidSilicon(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.silicon");
	}

}
