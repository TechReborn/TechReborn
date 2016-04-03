package techreborn.blocks.storage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.lesu.TileLesuStorage;

public class BlockLESUStorage extends BlockMachineBase implements IAdvancedRotationTexture
{

	private final String prefix = "techreborn:blocks/machine/storage/";

	public BlockLESUStorage(Material material)
	{
		super();
		setUnlocalizedName("techreborn.lesustorage");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack)
	{
		super.onBlockPlacedBy(world, x, y, z, player, itemstack);
		if (world.getTileEntity(new BlockPos(x, y, z)) instanceof TileLesuStorage)
		{
			((TileLesuStorage) world.getTileEntity(new BlockPos(x, y, z))).rebuildNetwork();
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		if (world.getTileEntity(new BlockPos(x, y, z)) instanceof TileLesuStorage)
		{
			((TileLesuStorage) world.getTileEntity(new BlockPos(x, y, z))).removeFromNetwork();
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(this));
		return items;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileLesuStorage();
	}

	public boolean shouldConnectToBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int meta)
	{
		return block == this;
	}

	@Override
	public String getFront(boolean isActive)
	{
		return prefix + "lesu_block";
	}

	@Override
	public String getSide(boolean isActive)
	{
		return prefix + "lesu_block";
	}

	@Override
	public String getTop(boolean isActive)
	{
		return prefix + "lesu_block";
	}

	@Override
	public String getBottom(boolean isActive)
	{
		return prefix + "lesu_block";
	}
}
