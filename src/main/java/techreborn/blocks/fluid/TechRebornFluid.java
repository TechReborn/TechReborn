package techreborn.blocks.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class TechRebornFluid extends Fluid {
    public TechRebornFluid(String fluidName) {
        super(fluidName,  new ResourceLocation("techreborn:textures/block/" + fluidName + "_flowing"), new ResourceLocation("techreborn:textures/block/" + fluidName + "_flowing"));
    }
}
