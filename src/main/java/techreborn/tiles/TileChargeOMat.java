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

package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.api.IToolDrop;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.compat.CompatManager;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.utils.IC2ItemCharger;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileChargeOMat extends TilePowerAcceptor
	implements IToolDrop, IInventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "charge_bench", key = "ChargeBenchMaxInput", comment = "Charge Bench Max Input (Value in EU)")
	public static int maxInput = 512;
	@ConfigRegistry(config = "machines", category = "charge_bench", key = "ChargeBenchMaxEnergy", comment = "Charge Bench Max Energy (Value in EU)")
	public static int maxEnergy = 100_000_000;

	public Inventory inventory = new Inventory(6, "TileChargeOMat", 64, this);

	public TileChargeOMat() {
		super();
	}

	// TilePowerAcceptor
	@Override
	public void update() {
		super.update();

		if(world.isRemote){
			return;
		}
		for (int i = 0; i < 6; i++) {
			if (this.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
				if (this.getEnergy() > 0) {
					final ItemStack stack = this.inventory.getStackInSlot(i);
					if (stack.getItem() instanceof IEnergyItemInfo) {
						IEnergyItemInfo item = (IEnergyItemInfo) stack.getItem();
						if (PoweredItem.getEnergy(stack) != PoweredItem.getMaxPower(stack)) {
							if (canUseEnergy(item.getMaxTransfer(stack))) {
								useEnergy(item.getMaxTransfer(stack));
								PoweredItem.setEnergy(PoweredItem.getEnergy(stack) + item.getMaxTransfer(stack), stack);
							}
						}
					}
					if(CompatManager.isIC2Loaded){
						IC2ItemCharger.chargeIc2Item(this, stack);
					}
					if(stack.hasCapability(CapabilityEnergy.ENERGY, null)){
						IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
						int max = Math.min(maxInput, (int) getEnergy()) * RebornCoreConfig.euPerFU;
						useEnergy(energyStorage.receiveEnergy(max, false) / RebornCoreConfig.euPerFU);
					}
				}
			}
		}
	}
	
	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}
	
	// TileLegacyMachineBase
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.CHARGE_O_MAT, 1);
	}

	// IInventoryProvider
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("chargebench").player(player.inventory).inventory().hotbar().addInventory()
			.tile(this).energySlot(0, 62, 25).energySlot(1, 98, 25).energySlot(2, 62, 45).energySlot(3, 98, 45)
			.energySlot(4, 62, 65).energySlot(5, 98, 65).syncEnergyValue().addInventory().create(this);
	}
}
