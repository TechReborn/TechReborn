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

package techreborn.blocks;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileTechStorageBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockQuantumChest extends BlockMachineBase {

	public BlockQuantumChest() {
		super();
		this.setUnlocalizedName("techreborn.quantumChest");
		this.setCreativeTab(TechRebornCreativeTab.instance);
		this.setHardness(2.0F);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/tier3_machines"));
	}

	@Override
	protected void dropInventory(final World world, final BlockPos pos) {
		final TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity == null) {
			return;
		}
		if (!(tileEntity instanceof TileTechStorageBase)) {
			return;
		}

		final TileTechStorageBase inventory = (TileTechStorageBase) tileEntity;

		final List<ItemStack> items = new ArrayList<>();

		final List<ItemStack> droppables = inventory.getContentDrops();
		for (int i = 0; i < droppables.size(); i++) {
			final ItemStack itemStack = droppables.get(i);

			if (itemStack == ItemStack.EMPTY) {
				continue;
			}
			if (itemStack != ItemStack.EMPTY && itemStack.getCount() > 0) {
				if (itemStack.getItem() instanceof ItemBlock) {
					if (((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockFluidBase
						|| ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockStaticLiquid
						|| ((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockDynamicLiquid) {
						continue;
					}
				}
			}
			items.add(itemStack.copy());
		}

		for (final ItemStack itemStack : items) {
			final Random rand = new Random();

			final float dX = rand.nextFloat() * 0.8F + 0.1F;
			final float dY = rand.nextFloat() * 0.8F + 0.1F;
			final float dZ = rand.nextFloat() * 0.8F + 0.1F;

			final EntityItem entityItem = new EntityItem(world, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ,
				itemStack.copy());

			if (itemStack.hasTagCompound()) {
				entityItem.getItem().setTagCompound(itemStack.getTagCompound().copy());
			}

			final float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntity(entityItem);
			itemStack.setCount(0);
		}
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileQuantumChest();
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn,
	                                final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (!playerIn.isSneaking())
			playerIn.openGui(Core.INSTANCE, EGui.QUANTUM_CHEST.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
