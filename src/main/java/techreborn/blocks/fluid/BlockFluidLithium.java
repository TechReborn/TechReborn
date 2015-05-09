package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidLithium extends BlockFluidBase{

	public BlockFluidLithium(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.lithium");
	}

}
