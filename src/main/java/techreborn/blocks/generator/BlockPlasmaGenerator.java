package techreborn.blocks.generator;

import ic2.api.item.IC2Items;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import techreborn.blocks.BlockMachineBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPlasmaGenerator extends BlockMachineBase {

	@SideOnly(Side.CLIENT)
	private IIcon iconFront;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockPlasmaGenerator(Material material)
	{
		super(material);
		setBlockName("techreborn.plasmagenerator");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("techreborn:machine/plasma_generator_side_off");
		this.iconFront = icon.registerIcon("techreborn:machine/plasma_generator_front");
		this.iconTop = icon.registerIcon("techreborn:machine/plasma_generator_side_off");
		this.iconBottom = icon.registerIcon("techreborn:machine/plasma_generator_side_off");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{

		return metadata == 0 && side == 3 ? this.iconFront
				: side == 1 ? this.iconTop : 
					side == 0 ? this.iconBottom: (side == 0 ? this.iconTop
						: (side == metadata ? this.iconFront : this.blockIcon));

	}
	
	@Override
	public Item getItemDropped(int meta, Random random, int fortune)
	{
		return IC2Items.getItem("machine").getItem();
	}

}
