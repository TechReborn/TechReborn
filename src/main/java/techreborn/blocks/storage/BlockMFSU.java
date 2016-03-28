package techreborn.blocks.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.client.GuiHandler;
import techreborn.init.ModBlocks;
import techreborn.tiles.storage.TileMFSU;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class BlockMFSU extends BlockEnergyStorage
{
	public BlockMFSU()
	{
		super("MFSU", GuiHandler.mfsuID);
		setHardness(2f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileMFSU();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		dropInventory(worldIn, pos, new ItemStack(ModBlocks.machineframe, 1 ,7));
		super.breakBlock(worldIn, pos, state);
	}

}
