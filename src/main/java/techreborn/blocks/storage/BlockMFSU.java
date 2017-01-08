package techreborn.blocks.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import techreborn.client.EGui;
import techreborn.init.ModBlocks;
import techreborn.tiles.storage.TileMFSU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class BlockMFSU extends BlockEnergyStorage {
	public BlockMFSU() {
		super("MFSU", EGui.MFSU.ordinal());
		this.setHardness(2f);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileMFSU();
	}

	@Override
	public List<ItemStack> getDrops(final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		final ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(ModBlocks.MACHINE_FRAMES, 1, 1));
		return list;
	}
}
