package techreborn.blocks.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import reborncore.common.util.Color;

public class TechRebornFluid extends Fluid
{
	public TechRebornFluid(String fluidName)
	{
		super(fluidName,
				new ResourceLocation("techreborn:blocks/fluids/" + fluidName.replaceFirst("fluid", "") + "_flowing"),
				new ResourceLocation("techreborn:blocks/fluids/" + fluidName.replaceFirst("fluid", "") + "_flowing"));
	}
}
