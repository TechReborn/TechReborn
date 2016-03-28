package techreborn.blocks.transformers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.blocks.storage.BlockBatBox;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.transformers.TileLVTransformer;

import java.util.Random;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class BlockLVTransformer extends BlockBatBox
{

	public BlockLVTransformer()
	{
		super();
		setUnlocalizedName("techreborn.lvt");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_)
	{
		return new TileLVTransformer();
	}

	@Override
	public String getFrontOff()
	{
		return prefix + "lv_transformer_front";
	}

	@Override
	public String getFrontOn()
	{
		return prefix + "lv_transformer_front";
	}

	@Override
	public String getSide()
	{
		return prefix + "lv_transformer_side";
	}

	@Override
	public String getTop()
	{
		return prefix + "lv_transformer_side";
	}

	@Override
	public String getBottom()
	{
		return prefix + "lv_transformer_side";
	}
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		ItemStack itemStack = new ItemStack(this);

		Random rand = new Random();

		float dX = rand.nextFloat() * 0.8F + 0.1F;
		float dY = rand.nextFloat() * 0.8F + 0.1F;
		float dZ = rand.nextFloat() * 0.8F + 0.1F;

		EntityItem entityItem = new EntityItem(world, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ,
				itemStack.copy());

		if (itemStack.hasTagCompound())
		{
			entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
		}

		float factor = 0.05F;
		entityItem.motionX = rand.nextGaussian() * factor;
		entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
		entityItem.motionZ = rand.nextGaussian() * factor;
		world.spawnEntityInWorld(entityItem);
		itemStack.stackSize = 0;
	}
}
