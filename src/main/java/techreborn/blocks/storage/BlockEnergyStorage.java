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
import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.BaseTileBlock;
import reborncore.common.blocks.IRotationTexture;
import techreborn.Core;
import techreborn.client.TechRebornCreativeTab;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by Rushmead
 */
public abstract class BlockEnergyStorage extends BaseTileBlock implements IRotationTexture, ITexturedBlock {
	public static PropertyDirection FACING = PropertyDirection.create("facing", Facings.ALL);
	protected final String prefix = "techreborn:blocks/machines/energy/";
	public String name;
	public int guiID;

	public BlockEnergyStorage(String name, int guiID) {
		super(Material.ROCK);
		setHardness(2f);
		setUnlocalizedName("techreborn." + name.toLowerCase());
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.name = name;
		this.guiID = guiID;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack,
	                                EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, guiID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
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

	@Override
	public String getFrontOff() {
		return prefix + getSimpleName(name.toLowerCase()) + "_front";
	}

	@Override
	public String getFrontOn() {
		return prefix + getSimpleName(name.toLowerCase()) + "_front";
	}

	@Override
	public String getSide() {
		return prefix + getSimpleName(name.toLowerCase()) + "_side";
	}

	@Override
	public String getTop() {
		return prefix + getSimpleName(name.toLowerCase()) + "_side";
	}

	@Override
	public String getBottom() {
		return prefix + getSimpleName(name.toLowerCase()) + "_side";
	}

	@Override
	public String getTextureNameFromState(IBlockState blockState, EnumFacing facing) {
		if (this instanceof IRotationTexture) {
			IRotationTexture rotationTexture = this;
			if (getFacing(blockState) == facing) {
				return rotationTexture.getFrontOff();
			}
			if (facing == EnumFacing.UP) {
				return rotationTexture.getTop();
			}
			if (facing == EnumFacing.DOWN) {
				return rotationTexture.getBottom();
			}
			return rotationTexture.getSide();
		}
		return "techreborn:blocks/machine/machine_side";
	}

	public EnumFacing getFacing(IBlockState state) {
		return state.getValue(FACING);
	}

	@Override
	public int amountOfStates() {
		return 6;
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

	public String getSimpleName(String fullName) {
		if (fullName.equalsIgnoreCase("Batbox")) {
			return "lv_storage";
		}
		if (fullName.equalsIgnoreCase("MFE")) {
			return "mv_storage";
		}
		if (fullName.equalsIgnoreCase("MFSU")) {
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

}
