/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.ToolManager;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.BaseTileBlock;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.util.WrenchUtils;
import techreborn.TechReborn;

/**
 * Created by Rushmead
 */
public abstract class BlockEnergyStorage extends BaseTileBlock {
	public static DirectionProperty FACING = BlockStateProperties.FACING;
	public String name;
	public IMachineGuiHandler gui;

	public BlockEnergyStorage(String name, IMachineGuiHandler gui) {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(2f));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
		this.name = name;
		this.gui = gui;
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, "machines/energy"));
		BlockWrenchEventHandler.wrenableBlocks.add(this);
	}

	public void setFacing(EnumFacing facing, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
	}

	public EnumFacing getFacing(IBlockState state) {
		return state.get(FACING);
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

	// Block
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(FACING);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		// We extended BlockTileBase. Thus we should always have tile entity. I hope.
		if (tileEntity == null) {
			return false;
		}

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, side)) {
				return true;
			}
		}

		if (!playerIn.isSneaking() && gui != null) {
			gui.open(playerIn, pos, worldIn);
			return true;
		}

		return super.onBlockActivated(state, worldIn, pos, playerIn, hand, side, hitX, hitY, hitZ);
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
}
