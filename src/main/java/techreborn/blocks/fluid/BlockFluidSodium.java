package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidSodium extends BlockFluidBase{

	public BlockFluidSodium(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.sodium");
	}

}
