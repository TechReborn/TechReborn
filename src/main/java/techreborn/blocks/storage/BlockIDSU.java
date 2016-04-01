package techreborn.blocks.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.client.GuiHandler;
import techreborn.init.ModBlocks;
import techreborn.tiles.idsu.TileIDSU;

import java.util.ArrayList;
import java.util.List;

public class BlockIDSU extends BlockEnergyStorage
{
	public BlockIDSU()
	{
		super("IDSU", GuiHandler.idsuID);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileIDSU();
	}


	@Override public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileIDSU)
		{
			((TileIDSU) tile).ownerUdid = placer.getUniqueID().toString();
		}
		return this.getDefaultState();
	}

	@Override public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(ModBlocks.machineframe, 1 , 7));
		return list;
	}
}
