package techreborn.blocks.storage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.lesu.TileLesuStorage;

import java.util.ArrayList;
import java.util.List;

public class BlockLESUStorage extends BaseTileBlock {;

	public BlockLESUStorage(Material material) {
		super(material);
		setUnlocalizedName("techreborn.lesustorage");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack itemstack) {
		super.onBlockPlacedBy(world, pos, state, player, itemstack);
		if (world.getTileEntity(pos) instanceof TileLesuStorage) {
			((TileLesuStorage) world.getTileEntity(pos)).rebuildNetwork();
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (world.getTileEntity(pos) instanceof TileLesuStorage) {
			((TileLesuStorage) world.getTileEntity(pos)).removeFromNetwork();
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(this));
		return items;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileLesuStorage();
	}

	public boolean shouldConnectToBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int meta) {
		return block == this;
	}
}
