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

package techreborn.blocks.advanced_machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.tiles.multiblock.TileFluidReplicator;

public class BlockFluidReplicator extends BlockMachineBase {
	
	public BlockFluidReplicator() {
		super();
		setCreativeTab(TechRebornCreativeTab.instance);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/tier3_machines"));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileFluidReplicator();
	}

	@Override
	public IMachineGuiHandler getGui() {
		return EGui.FLUID_REPLICATOR;
	}
	
	@Override
	public boolean isAdvanced() {
		return true;
	}
}