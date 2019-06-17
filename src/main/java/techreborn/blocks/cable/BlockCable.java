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

package techreborn.blocks.cable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.AbstractProperty;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import reborncore.api.ToolManager;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.WrenchUtils;
import techreborn.TechReborn;
import techreborn.init.ModSounds;
import techreborn.init.TRContent;
import techreborn.tiles.cable.TileCable;
import techreborn.utils.damageSources.ElectrialShockSource;

import javax.annotation.Nullable;

/**
 * Created by modmuss50 on 19/05/2017.
 */
@RebornRegister(TechReborn.MOD_ID)
public class BlockCable extends BlockContainer {

	public static final BooleanProperty EAST = BooleanProperty.create("east");
	public static final BooleanProperty WEST = BooleanProperty.create("west");
	public static final BooleanProperty NORTH = BooleanProperty.create("north");
	public static final BooleanProperty SOUTH = BooleanProperty.create("south");
	public static final BooleanProperty UP = BooleanProperty.create("up");
	public static final BooleanProperty DOWN = BooleanProperty.create("down");

	@ConfigRegistry(config = "misc", category = "cable", key = "uninsulatedElectrocutionDamage", comment = "When true an uninsulated cable will cause damage to entities")
	public static boolean uninsulatedElectrocutionDamage = true;

	@ConfigRegistry(config = "misc", category = "cable", key = "uninsulatedElectrocutionSound", comment = "When true an uninsulated cable will create a spark sound when an entity touches it")
	public static boolean uninsulatedElectrocutionSound = true;

	@ConfigRegistry(config = "misc", category = "cable", key = "uninsulatedElectrocutionParticles", comment = "When true an uninsulated cable will create a spark when an entity touches it")
	public static boolean uninsulatedElectrocutionParticles = true;

	public final TRContent.Cables type;

	public BlockCable(TRContent.Cables type) {
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(1f, 8f));
		this.type = type;
		setDefaultState(this.stateContainer.getBaseState().with(EAST, false).with(WEST, false).with(NORTH, false).with(SOUTH, false).with(UP, false).with(DOWN, false));
		BlockWrenchEventHandler.wrenableBlocks.add(this);
	}

	public AbstractProperty<Boolean> getProperty(EnumFacing facing) {
		switch (facing) {
			case EAST:
				return EAST;
			case WEST:
				return WEST;
			case NORTH:
				return NORTH;
			case SOUTH:
				return SOUTH;
			case UP:
				return UP;
			case DOWN:
				return DOWN;
			default:
				return EAST;
		}
	}
	
	private IBlockState makeConnections(IBlockReader world, BlockPos pos) {
		Boolean down = checkEnergyCapability(world, pos.down(), EnumFacing.UP);
		Boolean up = checkEnergyCapability(world, pos.up(), EnumFacing.DOWN); 
		Boolean north = checkEnergyCapability(world, pos.north(), EnumFacing.SOUTH); 
		Boolean east = checkEnergyCapability(world, pos.east(), EnumFacing.WEST); 
		Boolean south = checkEnergyCapability(world, pos.south(), EnumFacing.NORTH); 
		Boolean west = checkEnergyCapability(world, pos.west(), EnumFacing.WEST); 

		return this.getDefaultState().with(DOWN, down).with(UP, up).with(NORTH, north).with(EAST, east)
				.with(SOUTH, south).with(WEST, west);
	}
	
	private Boolean checkEnergyCapability(IBlockReader world, BlockPos pos, EnumFacing facing) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity!= null && tileEntity.getCapability(CapabilityEnergy.ENERGY, facing).isPresent()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	// BlockContainer
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileCable();
	}

	// Block
	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		// We should always have tile entity. I hope.
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

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN);
	}

	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		return makeConnections(context.getWorld(), context.getPos());
	}
	
	@Override
	public IBlockState updatePostPlacement(IBlockState ourState, EnumFacing ourFacing, IBlockState otherState, IWorld worldIn, BlockPos ourPos, BlockPos otherPos) {
		Boolean value = checkEnergyCapability(worldIn, otherPos, ourFacing.getOpposite());
		return ourState.with(getProperty(ourFacing), value);
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		super.onEntityCollision(state, worldIn, pos, entityIn);
		if (!type.canKill) {
			return;
		}
		if (!(entityIn instanceof EntityLivingBase)) {
			return;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity == null) {
			return;
		}
		if (!(tileEntity instanceof TileCable)) {
			return;
		}

		TileCable tileCable = (TileCable) tileEntity;
		if (tileCable.power <= 0) {
			return;
		}

		if (uninsulatedElectrocutionDamage) {
			if (type == TRContent.Cables.HV) {
				entityIn.setFire(1);
			}
			entityIn.attackEntityFrom(new ElectrialShockSource(), 1F);
		}
		if (uninsulatedElectrocutionSound) {
			worldIn.playSound(null, entityIn.posX, entityIn.posY, entityIn.posZ, ModSounds.CABLE_SHOCK,
					SoundCategory.BLOCKS, 0.6F, 1F);
		}
		if (uninsulatedElectrocutionParticles) {
			worldIn.addParticle(Particles.CRIT, entityIn.posX, entityIn.posY, entityIn.posZ, 0, 0, 0);
		}
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		if (type != null) {
			double culling = type.cableThickness / 2 ;
			return Block.makeCuboidShape(culling , culling, culling, 16.0D - culling, 16.0D - culling, 16.0D - culling);
		}
		return Block.makeCuboidShape(6, 6, 6, 10, 10, 10);
	}
}
