package techreborn.blocks.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import techreborn.client.EGui;
import techreborn.tiles.idsu.TileIDSU;

import java.util.ArrayList;
import java.util.List;

public class BlockIDSU extends BlockEnergyStorage {
	public BlockIDSU() {
		super("IDSU", EGui.IDSU.ordinal());
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileIDSU();
	}

	@Override
	public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ, final int meta, final EntityLivingBase placer) {
		final TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileIDSU) {
			((TileIDSU) tile).ownerUdid = placer.getUniqueID().toString();
		}
		return this.getDefaultState();
	}

	@Override
	public List<ItemStack> getDrops(final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		final ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(this, 1, 2));
		return list;
	}
}
