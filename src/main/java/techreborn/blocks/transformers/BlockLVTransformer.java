package techreborn.blocks.transformers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.tiles.transformers.TileLVTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class BlockLVTransformer extends BlockTransformer
{

	public BlockLVTransformer()
	{
		super("lvtransformer");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileLVTransformer();
	}

	@Override public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(this, 1 , 0));
		return list;
	}
}
