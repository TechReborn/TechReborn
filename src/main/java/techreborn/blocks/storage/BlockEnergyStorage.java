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

package techreborn.blocks.storage;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.ToolManager;
import reborncore.common.BaseTileBlock;
import techreborn.Core;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModSounds;
import techreborn.lib.ModInfo;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by Rushmead
 */
public abstract class BlockEnergyStorage extends BaseTileBlock {
	public static PropertyDirection FACING = PropertyDirection.create("facing", Facings.ALL);
	public String name;
	public int guiID;

	public BlockEnergyStorage(String name, int guiID) {
		super(Material.IRON);
		setHardness(2f);
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.name = name;
		this.guiID = guiID;
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/energy"));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
	                                EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldStack = player.getHeldItem(hand);
		if(ToolManager.INSTANCE.canHandleTool(heldStack)){
			if(ToolManager.INSTANCE.handleTool(heldStack, pos, world, player, side, true)){
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
					EnumFacing facing2 = state.getValue(BlockEnergyStorage.FACING);
					if (facing2.getOpposite() == side) {
						facing2 = side;
					} else {
						facing2 = side.getOpposite();
					}
					world.setBlockState(pos, state.withProperty(BlockEnergyStorage.FACING, facing2));
					return true;
				}
			}
		} else if (!player.isSneaking()){
			player.openGui(Core.INSTANCE, guiID, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}

	protected BlockStateContainer createBlockState() {
		FACING = PropertyDirection.create("facing", Facings.ALL);
		return new BlockStateContainer(this, FACING);
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

	public String getSimpleName(String fullName) {
		if (fullName.equalsIgnoreCase("Batbox")) {
			return "lv_storage";
		}
		if (fullName.equalsIgnoreCase("MEDIUM_VOLTAGE_SU")) {
			return "mv_storage";
		}
		if (fullName.equalsIgnoreCase("HIGH_VOLTAGE_SU")) {
			return "hv_storage";
		}
		if (fullName.equalsIgnoreCase("AESU")) {
			return "ev_storage_adjust";
		}
		if (fullName.equalsIgnoreCase("IDSU")) {
			return "ev_storage_transmitter";
		}
		if (fullName.equalsIgnoreCase("LESU")) {
			return "ev_multi";
		}
		return fullName.toLowerCase();
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

		public boolean apply(EnumFacing p_apply_1_) {
			return p_apply_1_ != null;
		}

		public Iterator<EnumFacing> iterator() {
			return Iterators.forArray(this.facings());
		}
	}

}
