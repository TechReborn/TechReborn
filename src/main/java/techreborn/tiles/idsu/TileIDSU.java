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

package techreborn.tiles.idsu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.StringUtils;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileIDSU extends TilePowerAcceptor implements IWrenchable {

	@ConfigRegistry(config = "machines", category = "idsu", key = "IdsuMaxInput", comment = "IDSU Max Input (Value in EU)")
	public static int maxInput = 8192;
	@ConfigRegistry(config = "machines", category = "idsu", key = "IdsuMaxOutput", comment = "IDSU Max Output (Value in EU)")
	public static int maxOutput = 8192;
	@ConfigRegistry(config = "machines", category = "idsu", key = "IdsuMaxEnergy", comment = "IDSU Max Energy (Value in EU)")
	public static int maxEnergy = 100000000;

	public String ownerUdid;
	private double euLastTick = 0;
	private double euChange;
	private int ticks;

	public TileIDSU() {
		super();
	}

	@Override
	public double getEnergy() {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return 0.0;
		}
		return IDSUManager.INSTANCE.getSaveDataForWorld(world, ownerUdid).storedPower;
	}

	@Override
	public void setEnergy(double energy) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return;
		}
		IDSUManager.INSTANCE.getSaveDataForWorld(world, ownerUdid).storedPower = energy;
	}

	@Override
	public void readFromNBTWithoutCoords(NBTTagCompound tag) {

	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
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
		return maxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	public float getChargeLevel() {
		float ret = (float) this.getEnergy() / (float) maxEnergy;
		if (ret > 1.0F) {
			ret = 1.0F;
		}

		return ret;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.ownerUdid = nbttagcompound.getString("ownerUdid");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if (ownerUdid == null && StringUtils.isBlank(ownerUdid) || StringUtils.isEmpty(ownerUdid)) {
			return nbttagcompound;
		}
		nbttagcompound.setString("ownerUdid", this.ownerUdid);
		return nbttagcompound;
	}

	public void updateEntity() {
		super.updateEntity();

		if (ticks == ConfigTechReborn.$$$$$$$$$$$$DONT_DELETE_$$$$$$$$$$$AverageEuOutTickTime) {
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

		boolean needsInvUpdate = false;

		if (needsInvUpdate) {
			this.markDirty();
		}

	}

	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return false;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer p0, EnumFacing p1) {
		return true;
	}

	@Override
	public EnumFacing getFacing() {
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer p0) {
		return true;
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(ModBlocks.INTERDIMENSIONAL_SU, 1);
		writeToNBT(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	public double getEuChange() {
		if (euChange == -1) {
			return -1;
		}
		return (euChange / ticks);
	}
}
