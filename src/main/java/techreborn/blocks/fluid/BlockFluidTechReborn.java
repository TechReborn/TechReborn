package techreborn.blocks.fluid;

import me.modmuss50.jsonDestroyer.api.ITexturedFluid;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import techreborn.Core;

public class BlockFluidTechReborn extends BlockFluidBase implements ITexturedFluid {

	String name;

	public BlockFluidTechReborn(Fluid fluid, Material material, String name) {
		super(fluid, material);
		setUnlocalizedName(name);
		this.name = name;
		Core.proxy.registerFluidBlockRendering(this, name);
	}

}
