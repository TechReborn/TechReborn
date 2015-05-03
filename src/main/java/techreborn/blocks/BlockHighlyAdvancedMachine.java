package techreborn.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockHighlyAdvancedMachine extends BlockMachineBase{
	
	@SideOnly(Side.CLIENT)
	private IIcon iconFront;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockHighlyAdvancedMachine(Material material)
	{
		super(material);
		setBlockName("techreboen.highlyadvancedmachine");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("techreborn:machine/highlyadvancedmachine");
		this.iconFront = icon.registerIcon("techreborn:machine/highlyadvancedmachine");
		this.iconTop = icon.registerIcon("techreborn:machine/highlyadvancedmachine");
		this.iconBottom = icon.registerIcon("techreborn:machine/highlyadvancedmachine");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{

		return metadata == 0 && side == 3 ? this.iconFront
				: side == 1 ? this.iconTop : (side == 0 ? this.iconTop
						: (side == metadata ? this.iconFront : this.blockIcon));

	}

}
