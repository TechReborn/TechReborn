package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidMercury extends BlockFluidBase{

	public BlockFluidMercury(Fluid fluid, Material material)
	{
		super(fluid, material);
		setBlockName("techreborn.mercury");
	}

}
