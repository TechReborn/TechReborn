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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.WorldUtils;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileTechStorageBase;

import java.util.ArrayList;
import java.util.List;

public class BlockQuantumChest extends BlockMachineBase {

	public BlockQuantumChest() {
		super();
		this.setUnlocalizedName("techreborn.quantumChest");
		this.setCreativeTab(TechRebornCreativeTab.instance);
		this.setHardness(2.0F);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/tier3_machines"));
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
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

			if (itemStack.isEmpty()) {
				continue;
			}
			if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
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
		WorldUtils.dropItems(items, world, pos);
		}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileQuantumChest();
	}

	@Override
	public IMachineGuiHandler getGui() {
		return EGui.QUANTUM_CHEST;
	}
}
