package techreborn.blocks.storage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.lesu.TileLesuStorage;

import java.util.ArrayList;
import java.util.List;

public class BlockLESUStorage extends BlockMachineBase {

	private final String prefix = "techreborn:blocks/machines/energy/";

	public BlockLESUStorage(Material material) {
		super(true);
		setUnlocalizedName("techreborn.lesustorage");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[0]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
		super.onBlockPlacedBy(world, x, y, z, player, itemstack);
		if (world.getTileEntity(new BlockPos(x, y, z)) instanceof TileLesuStorage) {
			((TileLesuStorage) world.getTileEntity(new BlockPos(x, y, z))).rebuildNetwork();
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (world.getTileEntity(new BlockPos(x, y, z)) instanceof TileLesuStorage) {
			((TileLesuStorage) world.getTileEntity(new BlockPos(x, y, z))).removeFromNetwork();
		}
		super.breakBlock(world, x, y, z, block, meta);
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
