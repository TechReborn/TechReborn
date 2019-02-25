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
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.state.AbstractProperty;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import reborncore.api.ToolManager;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.WrenchUtils;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.tiles.cable.TileCable;

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

	//see for more info https://www.reddit.com/r/feedthebeast/comments/5mxwq9/psa_mod_devs_do_you_call_worldgettileentity_from/
	public TileEntity getTileEntitySafely(IWorld blockAccess, BlockPos pos) {
//		if (blockAccess instanceof ChunkCache) {
//			return ((ChunkCache) blockAccess).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
//		} else {
			return blockAccess.getTileEntity(pos);
	//	}
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
		return state.onBlockActivated(worldIn, pos, playerIn, hand, side, hitX, hitY, hitZ);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(EAST, WEST, NORTH, SOUTH, UP, DOWN);
	}

	/*

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IWorld blockAccess, BlockPos pos, EnumFacing side) {
		if (type == TRContent.Cables.GLASSFIBER)
			return false;
		else
			return true;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(meta);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = state.getActualState(source, pos);
		float minSize = 0.3125F;
		float maxSize =  0.6875F;
		int thinkness = (int) type.cableThickness;
		if(thinkness == 6){
			minSize = 0.35F;
			maxSize = 0.65F;
		}
		float minX = state.get(WEST) ? 0.0F : minSize;
		float minY = state.get(DOWN) ? 0.0F : minSize;
		float minZ = state.get(NORTH) ? 0.0F : minSize;
		float maxX = state.get(EAST) ? 1.0F : maxSize;
		float maxY = state.get(UP) ? 1.0F : maxSize;
		float maxZ = state.get(SOUTH) ? 1.0F : maxSize;
		return new AxisAlignedBB((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY, (double) maxZ);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IWorld worldIn, BlockPos pos) {
		IBlockState actualState = state;
		for (EnumFacing facing : EnumFacing.values()) {
			TileEntity tileEntity = getTileEntitySafely(worldIn, pos.offset(facing));
			if (tileEntity != null) {
				actualState = actualState.with(getProperty(facing), tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).isPresent());
			}
		}
		return actualState;
	}

	@Override
	public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos,  Entity entity) {
		super.onEntityCollision(state, worldIn, pos, entity);
		if (type.canKill && entity instanceof EntityLivingBase) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity != null && tileEntity instanceof TileCable) {
				TileCable tileCable = (TileCable) tileEntity;
				if (tileCable.power != 0) {
					if (uninsulatedElectrocutionDamage) {
						if (type == TRContent.Cables.HV) {
							entity.setFire(1);
						}
						entity.attackEntityFrom(new ElectrialShockSource(), 1F);
					}
					if (uninsulatedElectrocutionSound) {
						worldIn.playSound(null, entity.posX, entity.posY,
							entity.posZ, ModSounds.CABLE_SHOCK,
							SoundCategory.BLOCKS, 0.6F, 1F);
					}
					if (uninsulatedElectrocutionParticles) {
						worldIn.spawnParticle(EnumParticleTypes.CRIT, entity.posX, entity.posY, entity.posZ, 0,
							0, 0);
					}
				}
			}
		}
	}

	*/
}
