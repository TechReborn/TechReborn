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

package techreborn.tiles.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileAdjustableSU extends TileEnergyStorage implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuMaxInput", comment = "AESU Max Input (Value in EU)")
	public static int maxInput = 8192;
	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuMaxOutput", comment = "AESU Max Output (Value in EU)")
	public static int maxOutput = 8192;
	@ConfigRegistry(config = "machines", category = "aesu", key = "AesuMaxEnergy", comment = "AESU Max Energy (Value in EU)")
	public static int maxEnergy = 100000000;

	public Inventory inventory = new Inventory(4, "TileAdjustableSU", 64, this);
	private int OUTPUT = 64; // The current output
	//private double euLastTick = 0;
	//private double euChange;
	//private int ticks;

	public TileAdjustableSU() {
		super("ADJUSTABLE_SU", 4, ModBlocks.ADJUSTABLE_SU, EnumPowerTier.INSANE, maxInput, maxOutput, maxEnergy);
	}

//	@Override
//	public void update() {
//		super.update();
//		if (ticks == 100) {
//			euChange = -1;
//			ticks = 0;
//
//		} else {
//			ticks++;
//			euChange += getEnergy() - euLastTick;
//			if (euLastTick == getEnergy()) {
//				euChange = 0;
//			}
//		}
//
//		euLastTick = getEnergy();
//	}

	@Override
	public ItemStack getToolDrop(EntityPlayer entityPlayer) {
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
		if (OUTPUT > maxOutput) {
			OUTPUT = maxOutput;
		}
		if (OUTPUT <= -1) {
			OUTPUT = 0;
		}
	}

//	public double getEuChange() {
//		if (euChange == -1) {
//			return -1;
//		}
//		return (euChange / ticks);
//	}

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
		//tagCompound.setDouble("euChange", euChange);
		//tagCompound.setDouble("euLastTick", euLastTick);
		tagCompound.setInteger("output", OUTPUT);
		inventory.writeToNBT(tagCompound);
		return tagCompound;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		//this.euChange = nbttagcompound.getDouble("euChange");
		//this.euLastTick = nbttagcompound.getDouble("euLastTick");
		this.OUTPUT = nbttagcompound.getInteger("output");
		inventory.readFromNBT(nbttagcompound);
	}


	@Override
	public double getBaseMaxOutput() {
		return OUTPUT;
	}

	@Override
	public BuiltContainer createContainer(EntityPlayer player) {
		return new ContainerBuilder("aesu").player(player.inventory).inventory().hotbar().armor()
				.complete(8, 18).addArmor().addInventory().tile(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
				.syncEnergyValue().addInventory().create(this);
	}
}
