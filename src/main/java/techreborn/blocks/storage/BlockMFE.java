package techreborn.blocks.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.client.GuiHandler;
import techreborn.init.ModBlocks;
import techreborn.tiles.storage.TileMFE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class BlockMFE extends BlockEnergyStorage
{
	public BlockMFE()
	{
		super("MFE", GuiHandler.mfeID);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileMFE();
	}

	@Override public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(ModBlocks.machineframe, 1 , 6));
		return list;
	}
}
