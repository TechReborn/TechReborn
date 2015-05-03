package techreborn.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileImplosionCompressor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockImplosionCompressor extends BlockMachineBase{
	
	@SideOnly(Side.CLIENT)
	private IIcon iconFront;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockImplosionCompressor(Material material)
	{
		super(material);
		setBlockName("techreborn.implosioncompressor");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileImplosionCompressor();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.compresserID, world, x, y,
					z);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("techreborn:machine/machine_side");
		this.iconFront = icon.registerIcon("techreborn:machine/implosion_compressor_front_off");
		this.iconTop = icon.registerIcon("techreborn:machine/advanced_machine_side");
		this.iconBottom = icon.registerIcon("techreborn:machine/machine_side");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{

		return metadata == 0 && side == 3 ? this.iconFront
				: side == 1 ? this.iconTop : 
					side == 6 ? this.iconBottom: (side == 0 ? this.iconTop
						: (side == metadata ? this.iconFront : this.blockIcon));

	}

}
