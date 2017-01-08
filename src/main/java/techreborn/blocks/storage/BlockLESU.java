package techreborn.blocks.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import techreborn.client.EGui;
import techreborn.tiles.lesu.TileLesu;

import java.util.ArrayList;
import java.util.List;

public class BlockLESU extends BlockEnergyStorage {
	public BlockLESU() {
		super("LESU", EGui.LESU.ordinal());
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileLesu();
	}

	@Override
	public List<ItemStack> getDrops(final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		final ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(this, 1, 2));
		return list;
	}
}
