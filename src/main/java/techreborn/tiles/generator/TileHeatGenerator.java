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

package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileHeatGenerator extends TilePowerAcceptor implements IWrenchable {

	@ConfigRegistry(config = "machines", category = "heat_generator", key = "HeatGeneratorMaxOutput", comment = "Heat Generator Max Output (Value in EU)")
	public static int maxOutput = 128;
	@ConfigRegistry(config = "machines", category = "heat_generator", key = "HeatGeneratorMaxEnergy", comment = "Heat Generator Max Energy (Value in EU)")
	public static int maxEnergy = 10000;
	@ConfigRegistry(config = "machines", category = "heat_generator", key = "HeatGeneratorEnergyPerTick", comment = "Heat Generator Energy Per Tick (Value in EU)")
	public static int energyPerTick = 5;

	private long lastOutput = 0;

	public TileHeatGenerator() {
		super(1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!world.isRemote) {
			for (final EnumFacing direction : EnumFacing.values()) {
				if (direction.equals(EnumFacing.UP))
					continue;
				if (this.world
					.getBlockState(new BlockPos(this.getPos().getX() + direction.getFrontOffsetX(),
						this.getPos().getY() + direction.getFrontOffsetY(),
						this.getPos().getZ() + direction.getFrontOffsetZ()))
					.getBlock() == Blocks.LAVA) {
					if (tryAddingEnergy(energyPerTick))
						this.lastOutput = this.world.getTotalWorldTime();
				}
			}

			if (this.world.getTotalWorldTime() - this.lastOutput < 30 && !this.isActive())
				this.world.setBlockState(this.getPos(),
					this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, true));
			else if (this.world.getTotalWorldTime() - this.lastOutput > 30 && this.isActive())
				this.world.setBlockState(this.getPos(),
					this.world.getBlockState(this.getPos()).withProperty(BlockMachineBase.ACTIVE, false));
		}
	}

	private boolean tryAddingEnergy(int amount) {
		if (this.getMaxPower() - this.getEnergy() >= amount) {
			addEnergy(amount);
			return true;
		} else if (this.getMaxPower() - this.getEnergy() > 0) {
			addEnergy(this.getMaxPower() - this.getEnergy());
			return true;
		}
		return false;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.HEAT_GENERATOR, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return true;
	}

	@Override
	public double getBaseMaxOutput() {
		return maxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	// @Override
	// public void addWailaInfo(List<String> info)
	// {
	// super.addWailaInfo(info);
	// info.add("Power Generarating " + euTick +" EU/t");
	//
	// }

}
