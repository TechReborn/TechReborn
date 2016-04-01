package techreborn.blocks.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.client.GuiHandler;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileAesu;

import java.util.ArrayList;
import java.util.List;

public class BlockAESU extends BlockEnergyStorage
{
	public BlockAESU()
	{
		super("AESU", GuiHandler.aesuID);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileAesu();
	}

	@Override public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(ModBlocks.machineframe, 1 , 7));
		return list;
	}
}
