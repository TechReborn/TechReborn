package techreborn.blocks.fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.lib.ModInfo;

public class BlockFluidBase extends BlockFluidClassic{
	@SideOnly(Side.CLIENT)
	protected IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon flowingIcon;
	
	public BlockFluidBase(Fluid fluid, Material material)
	{
		super(fluid, material);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
	}
	
	@Override
	public IIcon getIcon(int side, int meta) 
	{
		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) 
	{
		stillIcon = register.registerIcon(ModInfo.MOD_ID.toLowerCase() + ":" + "fluids/" + getUnlocalizedName().substring(16) + "_flowing");
		flowingIcon = register.registerIcon(ModInfo.MOD_ID.toLowerCase() + ":" + "fluids/" + getUnlocalizedName().substring(16) + "_flowing");
		
		this.stack.getFluid().setIcons(stillIcon, flowingIcon);
	}
	
	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) 
	{
		if (world.getBlock(x, y, z).getMaterial().isLiquid())
			return false;
		return super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) 
	{
		if (world.getBlock(x, y, z).getMaterial().isLiquid())
			return false;
		return super.displaceIfPossible(world, x, y, z);
	}

}
