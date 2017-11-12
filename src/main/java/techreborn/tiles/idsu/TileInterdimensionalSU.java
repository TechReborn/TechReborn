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
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.tiles.storage.TileEnergyStorage;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileInterdimensionalSU extends TileEnergyStorage implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "idsu", key = "IdsuMaxInput", comment = "IDSU Max Input (Value in EU)")
	public static int maxInput = 8192;
	@ConfigRegistry(config = "machines", category = "idsu", key = "IdsuMaxOutput", comment = "IDSU Max Output (Value in EU)")
	public static int maxOutput = 8192;
	@ConfigRegistry(config = "machines", category = "idsu", key = "IdsuMaxEnergy", comment = "IDSU Max Energy (Value in EU)")
	public static int maxEnergy = 100000000;

	public String ownerUdid;

	public TileInterdimensionalSU() {
		super("IDSU", 2, ModBlocks.INTERDIMENSIONAL_SU, EnumPowerTier.EXTREME, maxInput, maxOutput, maxEnergy);
	}

	@Override
	public double getEnergy() {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return 0.0;
		}
		return IDSUManager.getData(world).getStoredPower();
	}

	@Override
	public void setEnergy(double energy) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return;
		}
		IDSUManager.getData(world).setStoredPower(energy);
	}
	
	@Override
	public double useEnergy(double extract, boolean simulate) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return 0.0;
		}
		double energy = IDSUManager.getData(world).getStoredPower();
		if (extract > energy) {
			extract = energy;
		}
		if (!simulate) {
			setEnergy(energy - extract);
		}
		return extract;
	}
	
	@Override
	public boolean canUseEnergy(double input) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return false;
		}
		return input <= IDSUManager.getData(world).getStoredPower();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.ownerUdid = nbttagcompound.getString("ownerUdid");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if (ownerUdid == null && StringUtils.isBlank(ownerUdid) || StringUtils.isEmpty(ownerUdid)) {
			return nbttagcompound;
		}
		nbttagcompound.setString("ownerUdid", this.ownerUdid);
		return nbttagcompound;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("idsu").player(player.inventory).inventory().hotbar().armor()
			.complete(8, 18).addArmor().addInventory().tile(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
			.syncEnergyValue().addInventory().create(this);
	}

	@Override
	public boolean shouldHanldeEnergyNBT() {
		return false;
	}
}
