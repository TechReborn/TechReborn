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

package techreborn.blockentity.storage.energy.idsu;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.StringUtils;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class InterdimensionalSUBlockEntity extends EnergyStorageBlockEntity implements BuiltScreenHandlerProvider {

	public String ownerUdid;

	//This is the energy value that is synced to the client
	private double clientEnergy;

	public InterdimensionalSUBlockEntity() {
		super(TRBlockEntities.INTERDIMENSIONAL_SU, "IDSU", 2, TRContent.Machine.INTERDIMENSIONAL_SU.block, EnergyTier.INSANE, TechRebornConfig.idsuMaxEnergy);
	}

	@Override
	public double getStored(EnergySide face) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return 0.0;
		}
		if (world.isClient) {
			return clientEnergy;
		}
		return IDSUManager.getPlayer(world, ownerUdid).getEnergy();
	}

	@Override
	public void setStored(double energy) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return;
		}
		if (world.isClient) {
			clientEnergy = energy;
		} else {
			IDSUManager.getPlayer(world, ownerUdid).setEnergy(energy);
		}
	}

	@Override
	public void useEnergy(double extract) {
		if (ownerUdid == null || ownerUdid.isEmpty()) {
			return;
		}
		if (world.isClient) {
			throw new UnsupportedOperationException("cannot set energy on the client!");
		}
		double energy = IDSUManager.getPlayer(world, ownerUdid).getEnergy();
		if (extract > energy) {
			extract = energy;
		}

		setStored(energy - extract);

		return;
	}

	@Override
	protected boolean shouldHandleEnergyNBT() {
		return false;
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag nbttagcompound) {
		super.fromTag(blockState, nbttagcompound);
		this.ownerUdid = nbttagcompound.getString("ownerUdid");
	}

	@Override
	public CompoundTag toTag(CompoundTag nbttagcompound) {
		super.toTag(nbttagcompound);
		if (ownerUdid == null || StringUtils.isEmpty(ownerUdid)) {
			return nbttagcompound;
		}
		nbttagcompound.putString("ownerUdid", this.ownerUdid);
		return nbttagcompound;
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("idsu").player(player.inventory).inventory().hotbar().armor()
				.complete(8, 18).addArmor().addInventory().blockEntity(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
				.syncEnergyValue().addInventory().create(this, syncID);
	}
}
