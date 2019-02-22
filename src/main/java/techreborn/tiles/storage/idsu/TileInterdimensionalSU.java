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

package techreborn.tiles.storage.idsu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;
import reborncore.api.power.EnumPowerTier;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;
import techreborn.tiles.storage.TileEnergyStorage;

@RebornRegister(TechReborn.MOD_ID)
public class TileInterdimensionalSU extends TileEnergyStorage implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "idsu", key = "IdsuMaxInput", comment = "IDSU Max Input (Value in EU)")
	public static int maxInput = 8192;
	@ConfigRegistry(config = "machines", category = "idsu", key = "IdsuMaxOutput", comment = "IDSU Max Output (Value in EU)")
	public static int maxOutput = 8192;
	@ConfigRegistry(config = "machines", category = "idsu", key = "IdsuMaxEnergy", comment = "IDSU Max Energy (Value in EU)")
	public static int maxEnergy = 100_000_000;

	public String ownerUdid;

	public TileInterdimensionalSU() {
		super(TRTileEntities.INTERDIMENSIONAL_SU, "IDSU", 2, TRContent.Machine.INTERDIMENSIONAL_SU.block, EnumPowerTier.EXTREME, maxInput, maxOutput, maxEnergy);
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
	public void read(NBTTagCompound nbttagcompound) {
		super.read(nbttagcompound);
		this.ownerUdid = nbttagcompound.getString("ownerUdid");
	}

	@Override
	public NBTTagCompound write(NBTTagCompound nbttagcompound) {
		super.write(nbttagcompound);
		if (ownerUdid == null && StringUtils.isBlank(ownerUdid) || StringUtils.isEmpty(ownerUdid)) {
			return nbttagcompound;
		}
		nbttagcompound.putString("ownerUdid", this.ownerUdid);
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
