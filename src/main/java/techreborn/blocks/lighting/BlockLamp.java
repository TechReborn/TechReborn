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

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.ToolManager;
import reborncore.common.BaseTileBlock;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.items.WrenchHelper;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.tiles.lighting.TileLamp;

public class BlockLamp extends BaseTileBlock {

	public static PropertyDirection FACING;
	public static PropertyBool ACTIVE;
	private AxisAlignedBB[] bbs;

	private int cost;
	private int brightness;

	public BlockLamp(int brightness, int cost, double depth, double width) {
		super(Material.REDSTONE_LIGHT);
		this.setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
		this.bbs = GenBoundingBoxes(depth, width);
		this.cost = cost;
		this.brightness = brightness;
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/lighting"));
		BlockWrenchEventHandler.wrenableBlocks.add(this);
	}
	
	private static AxisAlignedBB[] GenBoundingBoxes(double depth, double width) {
		AxisAlignedBB[] bb = {
			new AxisAlignedBB(width, 1.0 - depth, width, 1.0 - width, 1.0D, 1.0 - width),
			new AxisAlignedBB(width, 0.0D, width, 1.0 - width, depth, 1.0 - width),
			new AxisAlignedBB(width, width, 1.0 - depth, 1.0 - width, 1.0 - width, 1.0D),
			new AxisAlignedBB(width, width, 0.0D, 1.0 - width, 1.0 - width, depth),
			new AxisAlignedBB(1.0 - depth, width, width, 1.0D, 1.0 - width, 1.0 - width),
			new AxisAlignedBB(0.0D, width, width, depth, 1.0 - width, 1.0 - width),
		};
		return bb;
	}
	
	public static boolean isActive(IBlockState state) {
		return state.getValue(ACTIVE);
	}

	public static EnumFacing getFacing(IBlockState state) {
		if(!state.getProperties().containsKey(FACING)){
			return EnumFacing.NORTH;
		}
		return state.getValue(FACING);
	}

	public static void setFacing(EnumFacing facing, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING, facing));
	}

	public static void setActive(Boolean active, World world, BlockPos pos) {
		EnumFacing facing = (EnumFacing)world.getBlockState(pos).getValue(FACING);
		IBlockState state = world.getBlockState(pos).withProperty(ACTIVE, active).withProperty(FACING, facing);
		world.setBlockState(pos, state, 3);
	}

	public int getCost() {
		return cost;
	}
	
	// BaseTileBlock
	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new TileLamp();
	}	

	// Block
	@Override
	protected BlockStateContainer createBlockState() {
		FACING = PropertyDirection.create("facing");
		ACTIVE = PropertyBool.create("active");
		return new BlockStateContainer(this, FACING, ACTIVE);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
											float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(ACTIVE) ? brightness : 0;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing facing) {
		// This makes only the back face of lamps connect to fences.
		if (getFacing(state).getOpposite() == facing)
			return BlockFaceShape.SOLID;
		else
			return BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.bbs[getFacing(state).getIndex()];
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
	                                EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		// We extended BaseTileBlock. Thus we should always have tile entity. I hope.
		if (tileEntity == null) {
			return false;
		}

		if (!stack.isEmpty() && ToolManager.INSTANCE.canHandleTool(stack)) {
			if (WrenchHelper.handleWrench(stack, worldIn, pos, playerIn, side)) {
				return true;
			}
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int facingInt = state.getValue(FACING).getIndex();
		int activeInt = state.getValue(ACTIVE) ? 8 : 0;
		return facingInt + activeInt;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		Boolean active = (meta&8)==8;
		EnumFacing facing = EnumFacing.byIndex(meta&7);
		return this.getDefaultState().withProperty(FACING, facing).withProperty(ACTIVE, active);
	}
}
