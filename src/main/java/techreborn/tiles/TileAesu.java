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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileAesu extends TilePowerAcceptor implements IWrenchable {

	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuMaxInput", comment = "AESU Max Input (Value in EU)")
	public static int MAX_INPUT = 8192;
	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuMaxOutput", comment = "AESU Max Output (Value in EU)")
	public static int MAX_OUTPUT = 8192;
	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuMaxStorage", comment = "AESU Max Storage (Value in EU)")
	public static int MAX_STORAGE = 100000000;
	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuTier", comment = "AESU Tier")
	public static int TIER = 5;
//	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuWrenchDropRate", comment = "AESU Wrench Drop Rate")
	public static float WRENCH_DROP_RATE = 1.0F;

	public Inventory inventory = new Inventory(4, "TileAesu", 64, this);
	private int OUTPUT = 64; // The current output
	private double euLastTick = 0;
	private double euChange;
	private int ticks;

	public TileAesu() {
		super(TIER);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (ticks == ConfigTechReborn.AverageEuOutTickTime) {
			euChange = -1;
			ticks = 0;

		} else {
			ticks++;
			euChange += getEnergy() - euLastTick;
			if (euLastTick == getEnergy()) {
				euChange = 0;
			}
		}

		euLastTick = getEnergy();
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return true;
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
		return WRENCH_DROP_RATE;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return getDropWithNBT();
	}

	public boolean isComplete() {
		return false;
	}

	public void handleGuiInputFromClient(int id) {
		if (id == 0) {
			OUTPUT += 256;
		}
		if (id == 1) {
			OUTPUT += 64;
		}
		if (id == 2) {
			OUTPUT -= 64;
		}
		if (id == 3) {
			OUTPUT -= 256;
		}
		if (OUTPUT > MAX_OUTPUT) {
			OUTPUT = MAX_OUTPUT;
		}
		if (OUTPUT <= -1) {
			OUTPUT = 0;
		}
	}

	public double getEuChange() {
		if (euChange == -1) {
			return -1;
		}
		return (euChange / ticks);
	}

	public ItemStack getDropWithNBT() {
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(ModBlocks.ADJUSTABLE_SU, 1);
		writeToNBTWithoutCoords(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setDouble("euChange", euChange);
		tagCompound.setDouble("euLastTick", euLastTick);
		tagCompound.setInteger("output", OUTPUT);
		inventory.writeToNBT(tagCompound);
		return tagCompound;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.euChange = nbttagcompound.getDouble("euChange");
		this.euLastTick = nbttagcompound.getDouble("euLastTick");
		this.OUTPUT = nbttagcompound.getInteger("output");
		inventory.readFromNBT(nbttagcompound);
	}

	@Override
	public double getBaseMaxPower() {
		return MAX_STORAGE;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return getFacingEnum() != direction;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return getFacingEnum() == direction;
	}

	@Override
	public double getBaseMaxOutput() {
		return OUTPUT;
	}

	@Override
	public double getBaseMaxInput() {
		return MAX_INPUT;
	}

	@Override
	public EnumPowerTier getBaseTier() {
		return EnumPowerTier.INSANE;
	}
}
