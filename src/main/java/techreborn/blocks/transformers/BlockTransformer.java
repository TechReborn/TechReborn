/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.blocks.transformers;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.IToolDrop;
import reborncore.api.ToolManager;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModSounds;
import techreborn.lib.ModInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Rushmead
 */
public abstract class BlockTransformer extends BaseTileBlock {
	public static PropertyDirection FACING = PropertyDirection.create("facing", Facings.ALL);
	public String name;

	public BlockTransformer(String name) {
		super(Material.IRON);
		setHardness(2f);
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.name = name;
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/energy"));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		FACING = PropertyDirection.create("facing", Facings.ALL);
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		this.setDefaultFacing(worldIn, pos, state);
	}
	
	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			IBlockState state0 = worldIn.getBlockState(pos.north());
			IBlockState state1 = worldIn.getBlockState(pos.south());
			IBlockState state2 = worldIn.getBlockState(pos.west());
			IBlockState state3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			
			if (enumfacing == EnumFacing.NORTH && state0.isFullBlock() && !state1.isFullBlock()) {
				enumfacing = EnumFacing.SOUTH;
			} else if (enumfacing == EnumFacing.SOUTH && state1.isFullBlock() && !state0.isFullBlock()) {
				enumfacing = EnumFacing.NORTH;
			} else if (enumfacing == EnumFacing.WEST && state2.isFullBlock() && !state3.isFullBlock()) {
				enumfacing = EnumFacing.EAST;
			} else if (enumfacing == EnumFacing.EAST && state3.isFullBlock() && !state2.isFullBlock()) {
				enumfacing = EnumFacing.WEST;
			}
			
			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
	                            ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		EnumFacing facing = placer.getHorizontalFacing().getOpposite();
		if (placer.rotationPitch < -50) {
			facing = EnumFacing.DOWN;
		} else if (placer.rotationPitch > 50) {
			facing = EnumFacing.UP;
		}
		setFacing(facing, worldIn, pos);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
	                                EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if(!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack) && !world.isRemote){
			if(ToolManager.INSTANCE.handleTool(stack, pos, world, player, side, true) && state.getBlock() instanceof BlockTransformer){
				if (player.isSneaking()) {
					TileEntity tileEntity = world.getTileEntity(pos);
					if (tileEntity instanceof IToolDrop) {
						ItemStack drop = ((IToolDrop) tileEntity).getToolDrop(player);
						if (drop == null) {
							return false;
						}
						if (!drop.isEmpty()) {
							spawnAsEntity(world, pos, drop);
						}
						world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.BLOCK_DISMANTLE,
								SoundCategory.BLOCKS, 0.6F, 1F);
						if (!world.isRemote) {
							world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
						}
						return true;
					}
				} else {
					EnumFacing facing2 = state.getValue(BlockTransformer.FACING);
					if (facing2.getOpposite() == side) {
						facing2 = side;
					} else {
						facing2 = side.getOpposite();
					}
					world.setBlockState(pos, state.withProperty(BlockTransformer.FACING, facing2));
					return true;
				}
			}
		}

		return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}
	
	protected List<ItemStack> dropInventory(IBlockAccess world, BlockPos pos, ItemStack itemToDrop) {
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity == null) {
			System.out.print("Null");
			return null;
		}
		if (!(tileEntity instanceof IInventory)) {

			System.out.print("Not INstance");
			return null;
		}

		IInventory inventory = (IInventory) tileEntity;

		List<ItemStack> items = new ArrayList<>();

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);

			if (itemStack.isEmpty()) {
				continue;
			}
			if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
				if (itemStack.getItem() instanceof ItemBlock) {
					if (((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockFluidBase
						|| ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockStaticLiquid
						|| ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockDynamicLiquid) {
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
	public int getMetaFromState(IBlockState state) {
		int facingInt = getSideFromEnum(state.getValue(FACING));
		return facingInt;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = getSideFromint(meta);
		return this.getDefaultState().withProperty(FACING, facing);
	}

	public void setFacing(EnumFacing facing, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING, facing));
	}

	public EnumFacing getSideFromint(int i) {
		if (i == 0) {
			return EnumFacing.NORTH;
		} else if (i == 1) {
			return EnumFacing.SOUTH;
		} else if (i == 2) {
			return EnumFacing.EAST;
		} else if (i == 3) {
			return EnumFacing.WEST;
		} else if (i == 4) {
			return EnumFacing.UP;
		} else if (i == 5) {
			return EnumFacing.DOWN;
		}
		return EnumFacing.NORTH;
	}

	public int getSideFromEnum(EnumFacing facing) {
		if (facing == EnumFacing.NORTH) {
			return 0;
		} else if (facing == EnumFacing.SOUTH) {
			return 1;
		} else if (facing == EnumFacing.EAST) {
			return 2;
		} else if (facing == EnumFacing.WEST) {
			return 3;
		} else if (facing == EnumFacing.UP) {
			return 4;
		} else if (facing == EnumFacing.DOWN) {
			return 5;
		}
		return 0;
	}

	public EnumFacing getFacing(IBlockState state) {
		return state.getValue(FACING);
	}

	public enum Facings implements Predicate<EnumFacing>, Iterable<EnumFacing> {
		ALL;

		public EnumFacing[] facings() {
			return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST,
				EnumFacing.UP, EnumFacing.DOWN };
		}

		public EnumFacing random(Random rand) {
			EnumFacing[] aenumfacing = this.facings();
			return aenumfacing[rand.nextInt(aenumfacing.length)];
		}

		@Override
		public boolean apply(EnumFacing p_apply_1_) {
			return p_apply_1_ != null;
		}

		@Override
		public Iterator<EnumFacing> iterator() {
			return Iterators.forArray(this.facings());
		}
	}

}
