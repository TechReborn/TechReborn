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

package techreborn.blocks.tier3;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.RebornCoreConfig;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.EGui;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileTechStorageBase;

public class BlockQuantumChest extends BlockMachineBase {

	public BlockQuantumChest() {
		super();
		this.setUnlocalizedName("techreborn.quantumChest");
		setCreativeTab(TechRebornCreativeTab.instance);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/tier3_machines"));
	}
	
	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new TileQuantumChest();
	}

	@Override
	public IMachineGuiHandler getGui() {
		return EGui.QUANTUM_CHEST;
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {	
/*		if (RebornCoreConfig.wrenchRequired){
			drops.add(isAdvanced() ? advancedFrameStack.copy() : basicFrameStack.copy());
		}
		else {
			super.getDrops(drops, world, pos, state, fortune);
		}*/
		
		TileEntity storageTile = world.getTileEntity(pos);
		if (storageTile != null && storageTile instanceof TileTechStorageBase) {
			drops.addAll(((TileTechStorageBase) storageTile).getContentDrops());
		}
		super.getDrops(drops, world, pos, state, fortune);
	}
}
