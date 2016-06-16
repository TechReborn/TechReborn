package techreborn.blocks.transformers;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import reborncore.common.BaseTileBlock;
import reborncore.common.blocks.IRotationTexture;
import techreborn.client.TechRebornCreativeTab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Rushmead
 */
public abstract class BlockTransformer extends BaseTileBlock implements IRotationTexture, ITexturedBlock
{
	public static PropertyDirection FACING = PropertyDirection.create("facing", Facings.ALL);
	protected final String prefix = "techreborn:blocks/machines/energy/";
	public String name;
	public String textureName;

	public BlockTransformer(String name, String textureName)
	{
		super(Material.ROCK);
		setHardness(2f);
		setUnlocalizedName("techreborn." + name.toLowerCase());
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.name = name;
		this.textureName = textureName + "_transformer";
	}
	protected BlockStateContainer createBlockState()
	{
		FACING = PropertyDirection.create("facing", Facings.ALL);
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		this.setDefaultFacing(worldIn, pos, state);
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			IBlockState sate = worldIn.getBlockState(pos.north());
			Block block = sate.getBlock();
			IBlockState state1 = worldIn.getBlockState(pos.south());
			Block block1 = state1.getBlock();
			IBlockState state2 = worldIn.getBlockState(pos.west());
			Block block2 = state2.getBlock();
			IBlockState state3 = worldIn.getBlockState(pos.east());
			Block block3 = state3.getBlock();
			EnumFacing enumfacing = state.getValue(FACING);

			if (enumfacing == EnumFacing.NORTH && block.isFullBlock(state) && !block1.isFullBlock(state1))
			{
				enumfacing = EnumFacing.SOUTH;
			} else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock(state1) && !block.isFullBlock(state))
			{
				enumfacing = EnumFacing.NORTH;
			} else if (enumfacing == EnumFacing.WEST && block2.isFullBlock(state2) && !block3.isFullBlock(state2))
			{
				enumfacing = EnumFacing.EAST;
			} else if (enumfacing == EnumFacing.EAST && block3.isFullBlock(state3) && !block2.isFullBlock(state2))
			{
				enumfacing = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		EnumFacing facing = placer.getHorizontalFacing().getOpposite();
		if (placer.rotationPitch < -50)
		{
			facing = EnumFacing.DOWN;
		} else if (placer.rotationPitch > 50)
		{
			facing = EnumFacing.UP;
		}
		setFacing(facing, worldIn, pos);
	}



	protected List<ItemStack> dropInventory(IBlockAccess world, BlockPos pos, ItemStack itemToDrop)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity == null)
		{
			System.out.print("Null");
			return null;
		}
		if (!(tileEntity instanceof IInventory))
		{

			System.out.print("Not INstance");
			return null;
		}

		IInventory inventory = (IInventory) tileEntity;

		List<ItemStack> items = new ArrayList<>();

		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack itemStack = inventory.getStackInSlot(i);

			if (itemStack == null)
			{
				continue;
			}
			if (itemStack != null && itemStack.stackSize > 0)
			{
				if (itemStack.getItem() instanceof ItemBlock)
				{
					if (((ItemBlock) itemStack.getItem()).block instanceof BlockFluidBase
							|| ((ItemBlock) itemStack.getItem()).block instanceof BlockStaticLiquid
							|| ((ItemBlock) itemStack.getItem()).block instanceof BlockDynamicLiquid)
					{
						continue;
					}
				}
			}
			items.add(itemStack.copy());
		}
		items.add(itemToDrop.copy());
		return items;

	}


	@Override
	public int getMetaFromState(IBlockState state)
	{
		int facingInt = getSideFromEnum(state.getValue(FACING));
		return facingInt;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean active = false;
		EnumFacing facing = getSideFromint(meta);
		return this.getDefaultState().withProperty(FACING, facing);
	}

	public void setFacing(EnumFacing facing, World world, BlockPos pos)
	{
		world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING, facing));
	}

	public EnumFacing getSideFromint(int i)
	{
		if (i == 0)
		{
			return EnumFacing.NORTH;
		} else if (i == 1)
		{
			return EnumFacing.SOUTH;
		} else if (i == 2)
		{
			return EnumFacing.EAST;
		} else if (i == 3)
		{
			return EnumFacing.WEST;
		} else if (i == 4)
		{
			return EnumFacing.UP;
		} else if (i == 5)
		{
			return EnumFacing.DOWN;
		}
		return EnumFacing.NORTH;
	}

	public int getSideFromEnum(EnumFacing facing)
	{
		if (facing == EnumFacing.NORTH)
		{
			return 0;
		} else if (facing == EnumFacing.SOUTH)
		{
			return 1;
		} else if (facing == EnumFacing.EAST)
		{
			return 2;
		} else if (facing == EnumFacing.WEST)
		{
			return 3;
		} else if (facing == EnumFacing.UP)
		{
			return 4;
		} else if (facing == EnumFacing.DOWN)
		{
			return 5;
		}
		return 0;
	}


	@Override
	public String getFrontOff()
	{
		return prefix  + textureName.toLowerCase() +  "_front";
	}

	@Override
	public String getFrontOn()
	{
		return prefix  + textureName.toLowerCase() +  "_front";
	}

	@Override
	public String getSide()
	{
		return prefix  + textureName.toLowerCase() +  "_side";
	}

	@Override
	public String getTop()
	{
		return prefix + textureName.toLowerCase() +  "_side";
	}

	@Override
	public String getBottom()
	{
		return prefix + textureName.toLowerCase() + "_side";
	}

	@Override
	public String getTextureNameFromState(IBlockState blockState, EnumFacing facing)
	{
		if (this instanceof IRotationTexture)
		{
			IRotationTexture rotationTexture = this;
			if (getFacing(blockState) == facing)
			{
				return rotationTexture.getFrontOff();
			}
			if (facing == EnumFacing.UP)
			{
				return rotationTexture.getTop();
			}
			if (facing == EnumFacing.DOWN)
			{
				return rotationTexture.getBottom();
			}
			return rotationTexture.getSide();
		}
		return "techreborn:blocks/machine/machine_side";
	}

	public EnumFacing getFacing(IBlockState state)
	{
		return state.getValue(FACING);
	}

	@Override
	public int amountOfStates()
	{
		return 6;
	}



	public enum Facings implements Predicate<EnumFacing>,Iterable<EnumFacing>
	{
		ALL;

		public EnumFacing[] facings()
		{
			return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST,
					EnumFacing.UP, EnumFacing.DOWN };
		}

		public EnumFacing random(Random rand)
		{
			EnumFacing[] aenumfacing = this.facings();
			return aenumfacing[rand.nextInt(aenumfacing.length)];
		}

		public boolean apply(EnumFacing p_apply_1_)
		{
			return p_apply_1_ != null;
		}

		public Iterator<EnumFacing> iterator()
		{
			return Iterators.forArray(this.facings());
		}
	}

}
