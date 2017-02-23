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

package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.multiblock.TileVacuumFreezer;

import javax.annotation.Nullable;

public class BlockVacuumFreezer extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockVacuumFreezer(final Material material) {
		super();
		this.setUnlocalizedName("techreborn.vacuumfreezer");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new TileVacuumFreezer();
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, @Nullable
		ItemStack heldItem, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (!player.isSneaking()) {
			player.openGui(Core.INSTANCE, EGui.VACUUM_FREEZER.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public String getFront(final boolean isActive) {
		return this.prefix + "vacuum_freezer_front";
	}

	@Override
	public String getSide(final boolean isActive) {
		return this.prefix + "machine_side";
	}

	@Override
	public String getTop(final boolean isActive) {
		return this.prefix + "vacuum_freezer_top";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return this.prefix + "machine_bottom";
	}
}
