package techreborn.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModSounds;
import techreborn.items.ItemParts;
import techreborn.items.tools.ItemTreeTap;

/**
 * Created by modmuss50 on 19/02/2016.
 */
public class BlockRubberLog extends Block implements ITexturedBlock
{

	public static PropertyDirection SAP_SIDE = PropertyDirection.create("sapside", EnumFacing.Plane.HORIZONTAL);
	public static PropertyBool HAS_SAP = PropertyBool.create("hassap");

	public BlockRubberLog()
	{
		super(Material.WOOD);
		setUnlocalizedName("techreborn.rubberlog");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.setHardness(2.0F);
		RebornCore.jsonDestroyer.registerObject(this);
		this.setDefaultState(
				this.getDefaultState().withProperty(SAP_SIDE, EnumFacing.NORTH).withProperty(HAS_SAP, false));
		this.setTickRandomly(true);
		this.setSoundType(SoundType.WOOD);
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, SAP_SIDE, HAS_SAP);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean hasSap = false;
		int tempMeta = meta;
		if (meta > 3)
		{
			hasSap = true;
			tempMeta -= 3;
		}
		EnumFacing facing = EnumFacing.getHorizontal(tempMeta);
		return this.getDefaultState().withProperty(SAP_SIDE, facing).withProperty(HAS_SAP, hasSap);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int tempMeta = 0;
		EnumFacing facing = state.getValue(SAP_SIDE);
		switch (facing)
		{
			case SOUTH:
				tempMeta = 0;
				break;
			case WEST:
				tempMeta = 1;
				break;
			case NORTH:
				tempMeta = 2;
				break;
			case EAST:
				tempMeta = 3;
		}
		if (state.getValue(HAS_SAP))
		{
			tempMeta += 3;
		}
		return tempMeta;
	}

	@Override
	public String getTextureNameFromState(IBlockState IBlockState, EnumFacing enumFacing)
	{
		if (enumFacing == EnumFacing.DOWN || enumFacing == EnumFacing.UP)
		{
			return "techreborn:blocks/rubber_log_top";
		}
		if (IBlockState.getValue(HAS_SAP))
		{
			if (IBlockState.getValue(SAP_SIDE) == enumFacing)
			{
				return "techreborn:blocks/rubber_log_sap";
			}
		}
		return "techreborn:blocks/rubber_log_side";
	}

	@Override
	public int amountOfStates()
	{
		return 8;
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean isWood(net.minecraft.world.IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		int i = 4;
		int j = i + 1;
		if (worldIn.isAreaLoaded(pos.add(-j, -j, -j), pos.add(j, j, j)))
		{
			for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-i, -i, -i), pos.add(i, i, i)))
			{
				IBlockState state1 = worldIn.getBlockState(blockpos);
				if (state1.getBlock().isLeaves(state1, worldIn, blockpos))
				{
					state1.getBlock().beginLeavesDecay(state1, worldIn, blockpos);
				}
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);
		if (!state.getValue(HAS_SAP))
		{
			if (rand.nextInt(50) == 0)
			{
				EnumFacing facing = EnumFacing.getHorizontal(rand.nextInt(4));
				if (worldIn.getBlockState(pos.down()).getBlock() == this
						&& worldIn.getBlockState(pos.up()).getBlock() == this)
				{
					worldIn.setBlockState(pos, state.withProperty(HAS_SAP, true).withProperty(SAP_SIDE, facing));
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		if (playerIn.getHeldItem(EnumHand.MAIN_HAND) != null
				&& playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemTreeTap)
			if (state.getValue(HAS_SAP))
			{
				if (state.getValue(SAP_SIDE) == side)
				{
					worldIn.setBlockState(pos,
							state.withProperty(HAS_SAP, false).withProperty(SAP_SIDE, EnumFacing.getHorizontal(0)));
					worldIn.playSound(null, pos.getX(), pos.getY(),
							pos.getZ(), ModSounds.extract,
							SoundCategory.BLOCKS, 0.6F, 1F);
					if (!worldIn.isRemote)
					{
						Random rand = new Random();
						BlockPos itemPos = pos.offset(side);
						EntityItem item = new EntityItem(worldIn, itemPos.getX(), itemPos.getY(), itemPos.getZ(),
								ItemParts.getPartByName("rubberSap").copy());
						float factor = 0.05F;
						playerIn.getHeldItem(EnumHand.MAIN_HAND).damageItem(1, playerIn);
						item.motionX = rand.nextGaussian() * factor;
						item.motionY = rand.nextGaussian() * factor + 0.2F;
						item.motionZ = rand.nextGaussian() * factor;
						worldIn.spawnEntityInWorld(item);
					}
					return true;
				}
			}
		return false;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		List<ItemStack> drops = new ArrayList<>();
		drops.add(new ItemStack(this));
		if (state.getValue(HAS_SAP))
		{
			if (new Random().nextInt(4) == 0)
			{
				drops.add(ItemParts.getPartByName("rubberSap"));
			}
		}
		return drops;
	}
}
