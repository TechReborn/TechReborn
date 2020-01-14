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

package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.util.WorldUtils;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModSounds;
import techreborn.items.ingredients.ItemParts;
import techreborn.items.tools.ItemElectricTreetap;
import techreborn.items.tools.ItemTreeTap;
import techreborn.lib.ModInfo;
import techreborn.utils.TechRebornCreativeTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by modmuss50 on 19/02/2016.
 */
public class BlockRubberLog extends Block {

	public static PropertyDirection SAP_SIDE = PropertyDirection.create("sapside", EnumFacing.Plane.HORIZONTAL);
	public static PropertyBool HAS_SAP = PropertyBool.create("hassap");

	public BlockRubberLog() {
		super(Material.WOOD);
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setHardness(2.0F);
		this.setDefaultState(
			this.getDefaultState().withProperty(SAP_SIDE, EnumFacing.NORTH).withProperty(HAS_SAP, false));
		this.setTickRandomly(true);
		this.setSoundType(SoundType.WOOD);
		Blocks.FIRE.setFireInfo(this, 5, 5);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SAP_SIDE, HAS_SAP);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean hasSap = false;
		int tempMeta = meta;
		if (meta > 3) {
			hasSap = true;
			tempMeta -= 3;
		}
		EnumFacing facing = EnumFacing.byHorizontalIndex(tempMeta);
		return this.getDefaultState().withProperty(SAP_SIDE, facing).withProperty(HAS_SAP, hasSap);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int tempMeta = 0;
		EnumFacing facing = state.getValue(SAP_SIDE);
		switch (facing) {
			case SOUTH:
				tempMeta = 0;
				break;
			case WEST:
				tempMeta = 1;
				break;
			case NORTH:
				tempMeta = 2;
				break;
			case EAST:
				tempMeta = 3;
				break;
			case UP:
				tempMeta = 0;
				break;
			case DOWN:
				tempMeta = 0;
		}
		if (state.getValue(HAS_SAP)) {
			tempMeta += 4;
		}
		return tempMeta;
	}

	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isWood(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		int i = 4;
		int j = i + 1;
		if (worldIn.isAreaLoaded(pos.add(-j, -j, -j), pos.add(j, j, j))) {
			for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-i, -i, -i), pos.add(i, i, i))) {
				IBlockState state1 = worldIn.getBlockState(blockpos);
				if (state1.getBlock().isLeaves(state1, worldIn, blockpos)) {
					state1.getBlock().beginLeavesDecay(state1, worldIn, blockpos);
				}
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		if (!state.getValue(HAS_SAP)) {
			if (rand.nextInt(50) == 0) {
				EnumFacing facing = EnumFacing.byHorizontalIndex(rand.nextInt(4));
				if (worldIn.getBlockState(pos.down()).getBlock() == this
					&& worldIn.getBlockState(pos.up()).getBlock() == this) {
					worldIn.setBlockState(pos, state.withProperty(HAS_SAP, true).withProperty(SAP_SIDE, facing));
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
	                                EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
		ItemStack stack = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		if (stack.isEmpty()) {
			return false;
		}
		ForgePowerItemManager capEnergy = null;
		if (stack.getItem() instanceof ItemElectricTreetap) {
			capEnergy = new ForgePowerItemManager(stack);
		}
		if ((capEnergy != null && capEnergy.getEnergyStored() > 20) || stack.getItem() instanceof ItemTreeTap) {
			if (state.getValue(HAS_SAP) && state.getValue(SAP_SIDE) == side) {
				worldIn.setBlockState(pos,
					state.withProperty(HAS_SAP, false).withProperty(SAP_SIDE, EnumFacing.byHorizontalIndex(0)));
				worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.SAP_EXTRACT, SoundCategory.BLOCKS,
					0.6F, 1F);
				if (!worldIn.isRemote) {
					if (capEnergy != null) {
						capEnergy.extractEnergy(20, false);

						ExternalPowerSystems.requestEnergyFromArmor(capEnergy, playerIn);
					} else {
						playerIn.getHeldItem(EnumHand.MAIN_HAND).damageItem(1, playerIn);
					}
					if (!playerIn.inventory.addItemStackToInventory(ItemParts.getPartByName("rubberSap").copy())) {
						WorldUtils.dropItem(ItemParts.getPartByName("rubberSap").copy(), worldIn, pos.offset(side));
					}
					if (playerIn instanceof EntityPlayerMP) {
						TRRecipeHandler.unlockTRRecipes((EntityPlayerMP) playerIn);
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = new ArrayList<>();
		drops.add(new ItemStack(this));
		if (state.getValue(HAS_SAP)) {
			if (new Random().nextInt(4) == 0) {
				drops.add(ItemParts.getPartByName("rubberSap"));
			}
		}
		return drops;
	}
}
