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

package techreborn.blocks.lighting;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import reborncore.api.ToolManager;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.BaseTileBlock;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.util.WrenchUtils;
import techreborn.TechReborn;
import techreborn.tiles.lighting.TileLamp;

public class BlockLamp extends BaseTileBlock {

	public static DirectionProperty FACING;
	public static BooleanProperty ACTIVE;
	protected final VoxelShape[] shape;

	private int cost;
	private int brightness;

	public BlockLamp(int brightness, int cost, double depth, double width) {
		super(Block.Properties.create(Material.REDSTONE_LIGHT));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH).with(ACTIVE, false));
		this.shape = GenCuboidShapes(depth, width);
		this.cost = cost;
		this.brightness = brightness;
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, "machines/lighting"));
		BlockWrenchEventHandler.wrenableBlocks.add(this);
	}
	
	private VoxelShape[] GenCuboidShapes(double depth, double width) {
		VoxelShape[] shapes = {
				Block.makeCuboidShape(width, 1.0 - depth, width, 1.0 - width, 1.0D, 1.0 - width),
				Block.makeCuboidShape(width, 0.0D, width, 1.0 - width, depth, 1.0 - width),
				Block.makeCuboidShape(width, width, 1.0 - depth, 1.0 - width, 1.0 - width, 1.0D),
				Block.makeCuboidShape(width, width, 0.0D, 1.0 - width, 1.0 - width, depth),
				Block.makeCuboidShape(1.0 - depth, width, width, 1.0D, 1.0 - width, 1.0 - width),
				Block.makeCuboidShape(0.0D, width, width, depth, 1.0 - width, 1.0 - width),
			};
		return shapes;
	}
	
	public static boolean isActive(IBlockState state) {
		return state.get(ACTIVE);
	}

	public static EnumFacing getFacing(IBlockState state) {
		return state.get(FACING);
	}

	public static void setFacing(EnumFacing facing, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
	}

	public static void setActive(Boolean active, World world, BlockPos pos) {
		EnumFacing facing = (EnumFacing)world.getBlockState(pos).get(FACING);
		IBlockState state = world.getBlockState(pos).with(ACTIVE, active).with(FACING, facing);
		world.setBlockState(pos, state, 3);
	}

	public int getCost() {
		return cost;
	}
	
	// BaseTileBlock
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileLamp();
	}	

	// Block
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		FACING = DirectionProperty.create("facing", EnumFacing.Plane.HORIZONTAL);
		ACTIVE = BooleanProperty.create("active");
		builder.add(FACING, ACTIVE);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		setFacing(placer.getHorizontalFacing().getOpposite(), worldIn, pos);
	}
	
	@Override
	public int getLightValue(IBlockState state) {
		return state.get(ACTIVE) ? brightness : 0;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing facing) {
		// This makes only the back face of lamps connect to fences.
		if (getFacing(state).getOpposite() == facing)
			return BlockFaceShape.SOLID;
		else
			return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return shape[getFacing(state).getIndex()];
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		// We extended BaseTileBlock. Thus we should always have tile entity. I hope.
		if (tileEntity == null) {
			return false;
		}

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, side)) {
				return true;
			}
		}

		return super.onBlockActivated(state, worldIn, pos, playerIn, hand, side, hitX, hitY, hitZ);
	}
}
