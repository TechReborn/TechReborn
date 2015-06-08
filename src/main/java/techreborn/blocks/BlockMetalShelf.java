package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMetalShelf extends BlockMachineBase {

	@SideOnly(Side.CLIENT)
	private IIcon iconFrontEmpty;
	
	@SideOnly(Side.CLIENT)
	private IIcon iconFrontBooks;
	
	@SideOnly(Side.CLIENT)
	private IIcon iconFrontCans;
	
	@SideOnly(Side.CLIENT)
	private IIcon iconFrontPaper;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockMetalShelf(Material material)
	{
		super(material);
		setBlockName("techreborn.metalshelf");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("techreborn:machine/machine_side");
		this.iconFrontEmpty = icon.registerIcon("techreborn:machine/metal_shelf_empty");
		this.iconTop = icon.registerIcon("techreborn:machine/machine_top");
		this.iconBottom = icon.registerIcon("techreborn:machine/machine_bottom");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{
		return metadata == 0 && side == 3 ? this.iconFrontEmpty
				: side == 1 ? this.iconTop : 
					side == 0 ? this.iconBottom: (side == 0 ? this.iconTop
						: (side == metadata ? this.iconFrontEmpty : this.blockIcon));

	}

}
