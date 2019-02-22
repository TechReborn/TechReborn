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

package techreborn.items.tool.industrial;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRContent;
import techreborn.items.tool.ItemDrill;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ItemIndustrialDrill extends ItemDrill {

	// 4M FE max charge with 1k charge rate
	public ItemIndustrialDrill() {
		super(ToolMaterial.DIAMOND, ConfigTechReborn.IndustrialDrillCharge, 2.0F, 10F);
		this.cost = 250;
		this.transferLimit = 1000;
	}

	public Set<BlockPos> getTargetBlocks(World worldIn, BlockPos pos, @Nullable EntityPlayer playerIn) {
		Set<BlockPos> targetBlocks = new HashSet<BlockPos>();
		if (!(playerIn instanceof EntityPlayer)) {
			return new HashSet<BlockPos>();
		}
		RayTraceResult raytrace = rayTrace(worldIn, playerIn, false);
		if(raytrace == null || raytrace.sideHit == null){
			return Collections.emptySet();
		}
		EnumFacing enumfacing = raytrace.sideHit;
		if (enumfacing == EnumFacing.SOUTH || enumfacing == EnumFacing.NORTH) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos newPos = pos.add(i, j, 0);
					if (shouldBreak(playerIn, worldIn, pos, newPos)) {
						targetBlocks.add(newPos);
					}
				}
			}
		} else if (enumfacing == EnumFacing.EAST || enumfacing == EnumFacing.WEST) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos newPos = pos.add(0, j, i);
					if (shouldBreak(playerIn, worldIn, pos, newPos)) {
						targetBlocks.add(newPos);
					}
				}
			}
		} else if (enumfacing == EnumFacing.DOWN || enumfacing == EnumFacing.UP) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos newPos = pos.add(j, 0, i);
					if (shouldBreak(playerIn, worldIn, pos, newPos)) {
						targetBlocks.add(newPos);
					}
				}
			}
		}
		return targetBlocks;
	}

	public void breakBlock(BlockPos pos, World world, EntityPlayer playerIn, ItemStack drill) {
		IBlockState blockState = world.getBlockState(pos);

		ForgePowerItemManager capEnergy = new ForgePowerItemManager(drill);

		capEnergy.extractEnergy(cost, false);
		ExternalPowerSystems.requestEnergyFromArmor(capEnergy, playerIn);

		blockState.getBlock().harvestBlock(world, playerIn, pos, blockState, world.getTileEntity(pos), drill);
		world.setBlockToAir(pos);
		world.removeTileEntity(pos);
	}
	
	private boolean shouldBreak(EntityPlayer playerIn, World worldIn, BlockPos originalPos, BlockPos pos) {
		if (originalPos.equals(pos)) {
			return false;
		}
		IBlockState blockState = worldIn.getBlockState(pos);
		if (blockState.getMaterial() == Material.AIR) {
			return false;
		}
		if (blockState.getMaterial().isLiquid()) {
			return false;
		}
		float blockHardness = blockState.getPlayerRelativeBlockHardness(playerIn, worldIn, pos);
		if (blockHardness == -1.0F) {
			return false;
		}
		float originalHardness = worldIn.getBlockState(originalPos).getPlayerRelativeBlockHardness(playerIn, worldIn, originalPos);
		if ((originalHardness / blockHardness) > 10.0F) {
			return false;
		}
		
		return true;	
	}

	// ItemDrill
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
		EntityPlayer playerIn = null;
		if ((entityLiving instanceof EntityPlayer)) {
			playerIn = (EntityPlayer) entityLiving;
		}
		for (BlockPos additionalPos : getTargetBlocks(worldIn, pos, playerIn)) {
			breakBlock(additionalPos, worldIn, playerIn, stack);
		}
		return super.onBlockDestroyed(stack, worldIn, blockIn, pos, entityLiving);
	}

	// ItemPickaxe
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return (Items.DIAMOND_PICKAXE.canHarvestBlock(blockIn) || Items.DIAMOND_SHOVEL.canHarvestBlock(blockIn)) && !Items.DIAMOND_AXE.canHarvestBlock(blockIn);
	}

	// Item
	@OnlyIn(Dist.CLIENT)
	@Override
	public void fillItemGroup(ItemGroup par2ItemGroup, NonNullList<ItemStack> itemList) {
		if (!isInGroup(par2ItemGroup)) {
			return;
		}
		ItemStack stack = new ItemStack(TRContent.ADVANCED_DRILL);
		ItemStack charged = stack.copy();
		ForgePowerItemManager capEnergy = new ForgePowerItemManager(charged);
		capEnergy.setEnergyStored(capEnergy.getMaxEnergyStored());

		itemList.add(stack);
		itemList.add(charged);
	}
}
