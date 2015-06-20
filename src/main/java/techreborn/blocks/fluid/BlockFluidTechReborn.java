package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidTechReborn extends BlockFluidBase{

	public BlockFluidTechReborn(Fluid fluid, Material material, String name)
	{
		super(fluid, material);
		setBlockName(name);
	}

}
