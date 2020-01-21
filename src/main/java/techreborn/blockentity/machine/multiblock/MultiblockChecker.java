/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blockentity.machine.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.init.TRContent;

public class MultiblockChecker {

	public static final BlockPos ZERO_OFFSET = BlockPos.ORIGIN;

	public static final String STANDARD_CASING = "standard";
	public static final String ADVANCED_CASING = "advanced";
	public static final String INDUSTRIAL_CASING = "industrial";
	public static final String CASING_ANY = "any";

	private final World world;
	private final BlockPos downCenter;

	public MultiblockChecker(World world, BlockPos downCenter) {
		this.world = world;
		this.downCenter = downCenter;
	}

	// TODO: make thid not so ugly
	public boolean checkCasing(int offX, int offY, int offZ, String type) {
		Block block = getBlock(offX, offY, offZ).getBlock();
		if (block == TRContent.MachineBlocks.BASIC.getCasing()|| block == TRContent.MachineBlocks.ADVANCED.getCasing() || block == TRContent.MachineBlocks.INDUSTRIAL.getCasing() ) {
			if (type == MultiblockChecker.CASING_ANY) {
				return true;
			} else if ( type == "standard" && block ==  TRContent.MachineBlocks.BASIC.getCasing()) {
				return true;
			}
			else if (type == "advanced" && block ==  TRContent.MachineBlocks.ADVANCED.getCasing()) {
				return true;
			}
			else if (type == "industrial" && block ==  TRContent.MachineBlocks.INDUSTRIAL.getCasing()) {
				return true;
			}
		}
		return false;
	}

	public boolean checkAir(int offX, int offY, int offZ) {
		BlockPos pos = downCenter.add(offX, offY, offZ);
		return world.isAir(pos);
	}

	public BlockState getBlock(int offX, int offY, int offZ) {
		BlockPos pos = downCenter.add(offX, offY, offZ);
		return world.getBlockState(pos);
	}

	public boolean checkRectY(int sizeX, int sizeZ, String casingType, BlockPos offset) {
		for (int x = -sizeX; x <= sizeX; x++) {
			for (int z = -sizeZ; z <= sizeZ; z++) {
				if (!checkCasing(x + offset.getX(), offset.getY(), z + offset.getZ(), casingType))
					return false;
			}
		}
		return true;
	}

	public boolean checkRectZ(int sizeX, int sizeY, String casingType, BlockPos offset) {
		for (int x = -sizeX; x <= sizeX; x++) {
			for (int y = -sizeY; y <= sizeY; y++) {
				if (!checkCasing(x + offset.getX(), y + offset.getY(), offset.getZ(), casingType))
					return false;
			}
		}
		return true;
	}

	public boolean checkRectX(int sizeZ, int sizeY, String casingType, BlockPos offset) {
		for (int z = -sizeZ; z <= sizeZ; z++) {
			for (int y = -sizeY; y <= sizeY; y++) {
				if (!checkCasing(offset.getX(), y + offset.getY(), z + offset.getZ(), casingType))
					return false;
			}
		}
		return true;
	}

	public boolean checkRingY(int sizeX, int sizeZ, String casingType, BlockPos offset) {
		for (int x = -sizeX; x <= sizeX; x++) {
			for (int z = -sizeZ; z <= sizeZ; z++) {
				if ((x == sizeX || x == -sizeX) || (z == sizeZ || z == -sizeZ)) {
					if (!checkCasing(x + offset.getX(), offset.getY(), z + offset.getZ(), casingType))
						return false;
				}
			}
		}
		return true;
	}

	public boolean checkRingYHollow(int sizeX, int sizeZ, String casingType, BlockPos offset) {
		for (int x = -sizeX; x <= sizeX; x++) {
			for (int z = -sizeZ; z <= sizeZ; z++) {
				if ((x == sizeX || x == -sizeX) || (z == sizeZ || z == -sizeZ)) {
					if (!checkCasing(x + offset.getX(), offset.getY(), z + offset.getZ(), casingType))
						return false;
				} else if (!checkAir(x + offset.getX(), offset.getY(), z + offset.getZ()))
					return false;
			}
		}
		return true;
	}

}
