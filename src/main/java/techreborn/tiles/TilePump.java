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

package techreborn.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;

import java.util.List;

/**
 * Created by modmuss50 on 08/05/2016.
 */
public class TilePump extends TilePowerAcceptor {

	public Tank tank = new Tank("TilePump", 10000, this);

	public TilePump() {
		super(1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!world.isRemote && world.getTotalWorldTime() % 10 == 0 && !tank.isFull() && tank.getCapacity() - tank.getFluidAmount() >= 1000 && canUseEnergy(ConfigTechReborn.pumpExtractEU)) {
			FluidStack fluidStack = drainBlock(world, pos.down(), false);
			if (fluidStack != null) {
				tank.fill(drainBlock(world, pos.down(), true), true);
				useEnergy(ConfigTechReborn.pumpExtractEU);
			}
			tank.compareAndUpdate();
		}
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
		super.addInfo(info, isRealTile);
		info.add(TextFormatting.LIGHT_PURPLE + "Eu per extract " + TextFormatting.GREEN
			+ PowerSystem.getLocaliszedPower(ConfigTechReborn.pumpExtractEU));
		info.add(TextFormatting.LIGHT_PURPLE + "Speed: " + TextFormatting.GREEN
			+ "1000mb/5 sec");
	}

	public static FluidStack drainBlock(World world, BlockPos pos, boolean doDrain) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

		if (fluid != null && FluidRegistry.isFluidRegistered(fluid)) {
			if (block instanceof IFluidBlock) {
				IFluidBlock fluidBlock = (IFluidBlock) block;
				if (!fluidBlock.canDrain(world, pos)) {
					return null;
				}
				return fluidBlock.drain(world, pos, doDrain);
			} else {
				//Checks if source
				int level = state.getValue(BlockLiquid.LEVEL);
				if (level != 0) {
					return null;
				}
				if (doDrain) {
					world.setBlockToAir(pos);
				}
				return new FluidStack(fluid, 1000);
			}
		} else {
			return null;
		}
	}

	@Override
	public double getBaseMaxPower() {
		return 10000;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return 32;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		readFromNBTWithoutCoords(tagCompound);
	}

	public void readFromNBTWithoutCoords(NBTTagCompound tagCompound) {
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		writeToNBTWithoutCoords(tagCompound);
		return tagCompound;
	}

	public NBTTagCompound writeToNBTWithoutCoords(NBTTagCompound tagCompound) {
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) tank;
		}
		return super.getCapability(capability, facing);
	}
}
