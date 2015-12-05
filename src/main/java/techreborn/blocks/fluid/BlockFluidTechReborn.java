package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import reborncore.api.IFluidTextureProvider;

public class BlockFluidTechReborn extends BlockFluidBase implements IFluidTextureProvider {

    String name;

    public BlockFluidTechReborn(Fluid fluid, Material material, String name) {
        super(fluid, material);
        setUnlocalizedName(name);
        this.name = name;
    }

    @Override
    public String getTextureName() {
        return "techreborn:blocks/fluids/" + name.replaceAll("techreborn.", "") + "_flowing";
    }
}
