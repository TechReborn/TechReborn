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

package techreborn.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;

import java.util.ArrayList;
import java.util.List;

public class ItemAdvancedChainsaw extends ItemChainsaw {

	public ItemAdvancedChainsaw() {
		super(ToolMaterial.DIAMOND, "techreborn.advancedChainsaw", ConfigTechReborn.AdvancedChainsawCharge,
			1.0F);
		this.cost = 250;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> itemList) {
		if (!isInCreativeTab(par2CreativeTabs)) {
			return;
		}
		ItemStack stack = new ItemStack(ModItems.ADVANCED_CHAINSAW);
		ItemStack uncharged = stack.copy();
		ItemStack charged = stack.copy();
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
		for (int i = 1; i < 10; i++) {
			BlockPos nextPos = pos.up(i);
			IBlockState nextState = worldIn.getBlockState(nextPos);
			if(nextState.getBlock().isWood(worldIn, nextPos)){
				breakBlock(nextPos, stack, worldIn, entityLiving, pos);
			}
		}
		return super.onBlockDestroyed(stack, worldIn, blockIn, pos, entityLiving);
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return Items.DIAMOND_AXE.canHarvestBlock(blockIn);
	}


	@SuppressWarnings("deprecation")
	public void breakBlock(BlockPos pos, ItemStack stack, World world, EntityLivingBase entityLiving, BlockPos oldPos) {
		if (oldPos == pos) {
			return;
		}
		if (!PoweredItem.canUseEnergy(cost, stack)) {
			return;
		}
		IBlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();
		if (blockState.getBlockHardness(world, pos) == -1.0F) {
			return;
		}
		List<ItemStack> stuff = block.getDrops(world, pos, blockState, 0);
		List<ItemStack> dropList = new ArrayList<>();
		BlockEvent.HarvestDropsEvent event = new BlockEvent.HarvestDropsEvent(world, pos, blockState, 0, 1, dropList, (EntityPlayer) entityLiving, false);
		MinecraftForge.EVENT_BUS.post(event);
		for (ItemStack drop : dropList) {
			if (!drop.isEmpty() && drop.getCount() > 0) {
				stuff.add(drop);
			}
		}
		for (ItemStack drop : stuff) {
			if (world.isRemote) {
				continue;
			}
			final EntityItem entityitem = new EntityItem(world, oldPos.getX(), oldPos.getY(), oldPos.getZ(), drop);
			entityitem.motionX = (oldPos.getX() - oldPos.getX()) / 10.0f;
			entityitem.motionY = 0.15000000596046448;
			entityitem.motionZ = (oldPos.getZ() - oldPos.getZ()) / 10.0f;
			world.spawnEntity(entityitem);
		}
		PoweredItem.useEnergy(cost, stack);
		world.setBlockToAir(pos);
	}
}
