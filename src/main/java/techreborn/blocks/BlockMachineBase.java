package techreborn.blocks;

import techreborn.client.TechRebornCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockMachineBase extends BlockContainer{

	public BlockMachineBase(Material material)
	{
		super(Material.rock);
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(2f);
		setStepSound(soundTypeMetal);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return null;
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
	
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x,
			int y, int z)
	{
		return false;
	}
	
	

}
