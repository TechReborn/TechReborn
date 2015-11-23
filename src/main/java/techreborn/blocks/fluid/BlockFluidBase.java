package techreborn.blocks.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import techreborn.client.TechRebornCreativeTabMisc;

public class BlockFluidBase extends BlockFluidClassic {

    public BlockFluidBase(Fluid fluid, Material material) {
        super(fluid, material);
        setCreativeTab(TechRebornCreativeTabMisc.instance);
    }


    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        if (world.getBlock(x, y, z).getMaterial().isLiquid())
            return false;
        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z).getMaterial().isLiquid())
            return false;
        return super.displaceIfPossible(world, x, y, z);
    }

}
