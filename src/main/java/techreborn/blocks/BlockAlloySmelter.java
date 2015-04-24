package techreborn.blocks;

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
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileAlloySmelter;
import techreborn.tiles.TileBlastFurnace;
import techreborn.tiles.TileMachineCasing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAlloySmelter extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon iconFront;

	@SideOnly(Side.CLIENT)
	private IIcon iconTop;

	@SideOnly(Side.CLIENT)
	private IIcon iconBottom;

	public BlockAlloySmelter(Material material)
	{
		super(material);
		setCreativeTab(TechRebornCreativeTab.instance);
		setBlockName("techreborn.alloysmelter");
		setHardness(2F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileAlloySmelter();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, GuiHandler.alloySmelterID, world, x, y,
					z);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.blockIcon = icon.registerIcon("techreborn:machine/machine_side");
		this.iconFront = icon
				.registerIcon("techreborn:machine/industrial_blast_furnace_front_off");
		this.iconTop = icon.registerIcon("techreborn:machine/machine_side");
		this.iconBottom = icon.registerIcon("techreborn:machine/machine_side");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{

		return metadata == 0 && side == 3 ? this.iconFront
				: side == 1 ? this.iconTop : (side == 0 ? this.iconTop
						: (side == metadata ? this.iconFront : this.blockIcon));

	}

	public void onBlockAdded(World world, int x, int y, int z)
	{

		super.onBlockAdded(world, x, y, z);
		this.setDefaultDirection(world, x, y, z);

	}

	private void setDefaultDirection(World world, int x, int y, int z)
	{

		if (!world.isRemote)
		{
			Block block1 = world.getBlock(x, y, z - 1);
			Block block2 = world.getBlock(x, y, z + 1);
			Block block3 = world.getBlock(x - 1, y, z);
			Block block4 = world.getBlock(x + 1, y, z);

			byte b = 3;

			if (block1.func_149730_j() && !block2.func_149730_j())
			{
				b = 3;
			}
			if (block2.func_149730_j() && !block1.func_149730_j())
			{
				b = 2;
			}
			if (block3.func_149730_j() && !block4.func_149730_j())
			{
				b = 5;
			}
			if (block4.func_149730_j() && !block3.func_149730_j())
			{
				b = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, b, 2);

		}

	}

	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase player, ItemStack itemstack)
	{

		int l = MathHelper
				.floor_double((double) (player.rotationYaw * 4.0F / 360F) + 0.5D) & 3;

		if (l == 0)
		{
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}
		if (l == 1)
		{
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}
		if (l == 2)
		{
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}
		if (l == 3)
		{
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}

	}

}
