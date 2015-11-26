package techreborn.blocks.fluid;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class TechRebornFluid extends Fluid {
    public TechRebornFluid(String fluidName) {
        super(fluidName,  new ModelResourceLocation("techreborn:textures/block/" + fluidName + "_flowing"), new ModelResourceLocation("techreborn:textures/block/" + fluidName + "_flowing"));
    }
}
