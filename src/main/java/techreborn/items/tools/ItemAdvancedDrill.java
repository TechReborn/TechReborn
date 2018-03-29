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

package techreborn.items.tools;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;

import java.util.HashSet;
import java.util.Set;

public class ItemAdvancedDrill extends ItemDrill {

	public ItemAdvancedDrill() {
		super(ToolMaterial.DIAMOND, "techreborn.advancedDrill", ConfigTechReborn.AdvancedDrillCharge, 2.0F, 10F);
		this.cost = 250;
	}

	public Set<BlockPos> getTargetBlocks(World worldIn, BlockPos pos, EntityLivingBase entityLiving) {
		Set<BlockPos> targetBlocks = new HashSet<BlockPos>();
		if (!(entityLiving instanceof EntityPlayer)) {
			return new HashSet<BlockPos>();
		}
		RayTraceResult raytrace = rayTrace(worldIn, (EntityPlayer) entityLiving, false);
		EnumFacing enumfacing = raytrace.sideHit;
		if (enumfacing == EnumFacing.SOUTH || enumfacing == EnumFacing.NORTH) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos newPos = pos.add(i, j, 0);
					if (worldIn.getBlockState(pos).getBlock() != Blocks.AIR) {
						targetBlocks.add(newPos);
					}

				}
			}
		} else if (enumfacing == EnumFacing.EAST || enumfacing == EnumFacing.WEST) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos newPos = pos.add(0, j, i);
					if (worldIn.getBlockState(pos).getBlock() != Blocks.AIR) {
						targetBlocks.add(newPos);
					}
				}
			}
		} else if (enumfacing == EnumFacing.DOWN || enumfacing == EnumFacing.UP) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					BlockPos newPos = pos.add(j, 0, i);
					if (worldIn.getBlockState(pos).getBlock() != Blocks.AIR) {
						targetBlocks.add(newPos);
					}
				}
			}
		}
		return targetBlocks;
	}

	public void breakBlock(BlockPos pos, World world, EntityPlayer playerIn, BlockPos originalPos, float originalHardness, ItemStack drill) {
		if (originalPos == pos) {
			return;
		}
		IBlockState blockState = world.getBlockState(pos);
		float blockHardness = world.getBlockState(pos).getPlayerRelativeBlockHardness(playerIn, world, pos);
		if (blockHardness == -1.0F) {
			return;
		}
		if(blockState.getMaterial().isLiquid()){
			return;
		}
		if ((originalHardness / blockHardness) > 10.0F) {
			return;
		}
		if(blockState.getMaterial() == Material.AIR){
			return;
		}
		if(!PoweredItem.canUseEnergy(cost, drill)){
			return;
		}
		PoweredItem.useEnergy(cost, drill);
		blockState.getBlock().harvestBlock(world, playerIn, pos, blockState, world.getTileEntity(pos), drill);
		world.setBlockToAir(pos);
		world.removeTileEntity(pos);
	}

	// ItemDrill
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
		EntityPlayer playerIn = (EntityPlayer) entityLiving;
		float originalHardness = blockIn.getPlayerRelativeBlockHardness(playerIn, worldIn, pos);
		for (BlockPos additionalPos : getTargetBlocks(worldIn, pos, entityLiving)) {
			breakBlock(additionalPos, worldIn, playerIn, pos, originalHardness, stack);
		}
		// Use energy only once no matter how many blocks were broken, e.g. energy used per application of a drill
		return super.onBlockDestroyed(stack, worldIn, blockIn, pos, entityLiving);
	}

	// ItemPickaxe
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return (Items.DIAMOND_PICKAXE.canHarvestBlock(blockIn) || Items.DIAMOND_SHOVEL.canHarvestBlock(blockIn)) && !Items.DIAMOND_AXE.canHarvestBlock(blockIn);
	}

	// Item
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack stack = new ItemStack(ModItems.ADVANCED_DRILL);
		ItemStack uncharged = stack.copy();
		ItemStack charged = stack.copy();
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}
}
