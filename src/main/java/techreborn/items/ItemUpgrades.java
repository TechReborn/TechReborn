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

package techreborn.items;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.tile.IUpgrade;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.IUpgradeHandler;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.tile.TileLegacyMachineBase;
import techreborn.init.TRItems;
import techreborn.lib.ModInfo;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class ItemUpgrades extends ItemTR implements IUpgrade {

	@ConfigRegistry(config = "items", category = "upgrades", key = "overclcoker_speed", comment = "Overclocker upgrade speed multipiler")
	public static double overclockerSpeed = 0.25;

	@ConfigRegistry(config = "items", category = "upgrades", key = "overclcoker_power", comment = "Overclocker upgrade power multipiler")
	public static double overclockerPower = 0.75;

	@ConfigRegistry(config = "items", category = "upgrades", key = "energy_storage", comment = "Energy storage upgrade extra power")
	public static double energyStoragePower = 40_000;

	public ItemUpgrades() {
		setMaxStackSize(16);
	}

	@Override
	public void process(@Nonnull TileLegacyMachineBase tile, @Nullable IUpgradeHandler handler, @Nonnull ItemStack stack) {
		TilePowerAcceptor powerAcceptor = null;
		if (tile instanceof TilePowerAcceptor) {
			powerAcceptor = (TilePowerAcceptor) tile;
		}
		if (stack.isItemEqual(new ItemStack(TRItems.UPGRADE_OVERCLOCKER))) {
			handler.addSpeedMulti(overclockerSpeed);
			handler.addPowerMulti(overclockerPower);
			if (powerAcceptor != null) {
				powerAcceptor.extraPowerInput += powerAcceptor.getMaxInput();
				powerAcceptor.extraPowerStoage += powerAcceptor.getBaseMaxPower();
			}			
		}
		if (powerAcceptor != null) {
			if (stack.isItemEqual(new ItemStack(TRItems.UPGRADE_TRANSFORMER))) {
				powerAcceptor.extraTeir += 1;				
			}
			if (stack.isItemEqual(new ItemStack(TRItems.UPGRADE_ENERGY_STORAGE))) {
				powerAcceptor.extraPowerStoage += energyStoragePower;			
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleRightClick(TileEntity tile, ItemStack stack, Container container, int slotID) {

	}
}
