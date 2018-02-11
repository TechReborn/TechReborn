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

package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileDragonEggSyphon extends TilePowerAcceptor implements IToolDrop, IInventoryProvider {

	@ConfigRegistry(config = "machines", category = "dragon_egg_siphoner", key = "DragonEggSiphonerMaxOutput", comment = "Dragon Egg Siphoner Max Output (Value in EU)")
	public static int maxOutput = 128;
	@ConfigRegistry(config = "machines", category = "dragon_egg_siphoner", key = "DragonEggSiphonerMaxEnergy", comment = "Dragon Egg Siphoner Max Energy (Value in EU)")
	public static int maxEnergy = 1000;
	@ConfigRegistry(config = "machines", category = "dragon_egg_siphoner", key = "DragonEggSiphonerEnergyPerTick", comment = "Dragon Egg Siphoner Energy Per Tick (Value in EU)")
	public static int energyPerTick = 4;

	public Inventory inventory = new Inventory(3, "TileDragonEggSyphon", 64, this);
	private long lastOutput = 0;

	public TileDragonEggSyphon() {
		super();
	}

	@Override
	public void update() {
		super.update();

		if (!world.isRemote) {
			if (world.getBlockState(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ()))
				.getBlock() == Blocks.DRAGON_EGG) {
				if (tryAddingEnergy(energyPerTick))
					this.lastOutput = this.world.getTotalWorldTime();
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
	public ItemStack getToolDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.DRAGON_EGG_SYPHON, 1);
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

	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
