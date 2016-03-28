package techreborn.blocks.storage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.client.GuiHandler;
import techreborn.tiles.storage.TileBatBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class BlockBatBox extends BlockEnergyStorage
{
	public BlockBatBox()
	{
		super("Batbox", GuiHandler.batboxID);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileBatBox();
	}
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		ArrayList<ItemStack> items = (ArrayList<ItemStack>) dropInventory(worldIn, pos, new ItemStack(this));
		for(ItemStack itemStack : items){
			Random rand = new Random();

			float dX = rand.nextFloat() * 0.8F + 0.1F;
			float dY = rand.nextFloat() * 0.8F + 0.1F;
			float dZ = rand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityItem = new EntityItem(worldIn, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ,
					itemStack.copy());

			if (itemStack.hasTagCompound())
			{
				entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
			}

			float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			worldIn.spawnEntityInWorld(entityItem);
			itemStack.stackSize = 0;
		}
	}

	@Override public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{

		return new ArrayList<ItemStack>();
	}
}
