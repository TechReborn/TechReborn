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

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.energy.CapabilityEnergy;
import reborncore.api.ToolManager;
import reborncore.common.blocks.BlockWrenchEventHandler;
import reborncore.common.util.WrenchUtils;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.init.ModSounds;
import techreborn.init.TRContent;
import techreborn.tiles.cable.TileCable;
import techreborn.utils.damageSources.ElectrialShockSource;

import javax.annotation.Nullable;

/**
 * Created by modmuss50 on 19/05/2017.
 */
@RebornRegister(modID = TechReborn.MOD_ID)
public class BlockCable extends BlockContainer {

	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool DOWN = PropertyBool.create("down");

	@ConfigRegistry(config = "misc", category = "cable", key = "uninsulatedElectrocutionDamage", comment = "When true an uninsulated cable will cause damage to entities")
	public static boolean uninsulatedElectrocutionDamage = true;

	@ConfigRegistry(config = "misc", category = "cable", key = "uninsulatedElectrocutionSound", comment = "When true an uninsulated cable will create a spark sound when an entity touches it")
	public static boolean uninsulatedElectrocutionSound = true;

	@ConfigRegistry(config = "misc", category = "cable", key = "uninsulatedElectrocutionParticles", comment = "When true an uninsulated cable will create a spark when an entity touches it")
	public static boolean uninsulatedElectrocutionParticles = true;

	public final TRContent.Cables type;

	public BlockCable(TRContent.Cables type) {
		super(Material.ROCK);
		this.type = type;
		setHardness(1F);
		setResistance(8F);
		setDefaultState(getDefaultState().with(EAST, false).with(WEST, false).with(NORTH, false).with(SOUTH, false).with(UP, false).with(DOWN, false));
		BlockWrenchEventHandler.wrenableBlocks.add(this);
	}

	//see for more info https://www.reddit.com/r/feedthebeast/comments/5mxwq9/psa_mod_devs_do_you_call_worldgettileentity_from/
	public TileEntity getTileEntitySafely(IBlockAccess blockAccess, BlockPos pos) {
		if (blockAccess instanceof ChunkCache) {
			return ((ChunkCache) blockAccess).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
		} else {
			return blockAccess.getTileEntity(pos);
		}
	}

	public IProperty<Boolean> getProperty(EnumFacing facing) {
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
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCable();
	}

	// Block
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
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
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, EAST, WEST, NORTH, SOUTH, UP, DOWN);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
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
		float minX = state.getValue(WEST) ? 0.0F : minSize;
		float minY = state.getValue(DOWN) ? 0.0F : minSize;
		float minZ = state.getValue(NORTH) ? 0.0F : minSize;
		float maxX = state.getValue(EAST) ? 1.0F : maxSize;
		float maxY = state.getValue(UP) ? 1.0F : maxSize;
		float maxZ = state.getValue(SOUTH) ? 1.0F : maxSize;
		return new AxisAlignedBB((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY, (double) maxZ);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState actualState = state;
		for (EnumFacing facing : EnumFacing.values()) {
			TileEntity tileEntity = getTileEntitySafely(worldIn, pos.offset(facing));
			if (tileEntity != null) {
				actualState = actualState.with(getProperty(facing), tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()));
			}
		}
		return actualState;
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
		super.onEntityCollision(worldIn, pos, state, entity);
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
}
