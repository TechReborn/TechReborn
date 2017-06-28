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

package techreborn.blocks.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.tiles.generator.TileGasTurbine;

public class BlockGasTurbine extends BlockMachineBase {

	public BlockGasTurbine() {
		super();
		this.setCreativeTab(TechRebornCreativeTab.instance);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/generators"));
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileGasTurbine();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
	                                final float hitY, final float hitZ) {
		if (this.fillBlockWithFluid(world, new BlockPos(x, y, z), player)) {
			return true;
		}
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.GAS_TURBINE.ordinal(), world, x, y, z);
		return true;
	}

}
