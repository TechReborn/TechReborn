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

package techreborn.blocks.tier0;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.TechReborn;
import techreborn.client.EGui;
import techreborn.tiles.machine.iron.TileIronFurnace;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockIronFurnace extends BlockMachineBase {

	public BlockIronFurnace() {
		super();
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, this, "machines/tier0_machines"));
	}

	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("incomplete-switch")
	public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
		if (this.isActive(state)) {
			final EnumFacing enumfacing = state.getValue(BlockMachineBase.FACING);
			final double d0 = pos.getX() + 0.5D;
			final double d1 = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			final double d2 = pos.getZ() + 0.5D;
			final double d3 = 0.52D;
			final double d4 = rand.nextDouble() * 0.6D - 0.3D;

			switch (enumfacing) {
			case WEST:
				worldIn.spawnParticle(	EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D,
										new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				break;
			case EAST:
				worldIn.spawnParticle(	EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D,
										new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				break;
			case NORTH:
				worldIn.spawnParticle(	EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D,
										new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
				break;
			case SOUTH:
				worldIn.spawnParticle(	EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D,
										new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}
	
	// BlockMachineBase
	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new TileIronFurnace();
	}

	@Override
	public IMachineGuiHandler getGui() {
		return EGui.IRON_FURNACE;
	}

	@Override
	public List<ItemStack> getDrops(final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		final List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(this));
		return items;
	}
}
