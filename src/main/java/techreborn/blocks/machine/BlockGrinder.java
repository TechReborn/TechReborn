package techreborn.blocks.machine;

import ic2.api.item.IC2Items;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileCentrifuge;
import techreborn.tiles.TileGrinder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGrinder extends BlockMachineBase{
	
	@SideOnly(Side.CLIENT)
	private IIcon iconFront;
	
	@SideOnly(Side.CLIENT)
	private IIcon iconFrontOn;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;
	
	@SideOnly(Side.CLIENT)
	private IIcon iconTopOn;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockGrinder(Material material)
	{
		super(material);
		setBlockName("techreborn.grinder");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileGrinder();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.grinderID, world, x, y,
					z);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("techreborn:machine/machine_side");
		this.iconFront = icon.registerIcon("techreborn:machine/industrial_grinder_front_off");
		this.iconFrontOn = icon.registerIcon("techreborn:machine/industrial_grinder_front_on");
		this.iconTop = icon.registerIcon("techreborn:machine/industrial_grinder_top_off");
		this.iconTopOn = icon.registerIcon("techreborn:machine/industrial_grinder_top_on");
		this.iconBottom = icon.registerIcon("techreborn:machine/machine_bottom");
	}

	@Override
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int metadata = blockAccess.getBlockMetadata(x, y, z);
		TileGrinder tileGrinder = (TileGrinder) blockAccess.getTileEntity(x, y, z);
		if(side == metadata && tileGrinder.crafter.isActive()){
			return this.iconFrontOn;
		}
		
		if(side == 1 && tileGrinder.crafter.isActive()){
			return this.iconTopOn;
		}


		return metadata == 0 && side == 3 ? this.iconFront
				: side == 1 ? this.iconTop : 
					side == 0 ? this.iconBottom: (side == 0 ? this.iconTop
						: (side == metadata ? this.iconFront : this.blockIcon));

	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{
		return metadata == 0 && side == 3 ? this.iconFront
				: side == 1 ? this.iconTop : 
					side == 0 ? this.iconBottom: (side == 0 ? this.iconTop
						: (side == metadata ? this.iconFront : this.iconFront));

	}
	
	@Override
	public Item getItemDropped(int meta, Random random, int fortune)
	{
		return IC2Items.getItem("machine").getItem();
	}

}
