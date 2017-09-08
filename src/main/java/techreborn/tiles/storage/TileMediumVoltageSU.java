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
import reborncore.api.power.EnumPowerTier;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 14/03/2016.
 *
 */
public class TileMediumVoltageSU extends TileEnergyStorage implements IContainerProvider {

	/**
	 *  MFE should store 1.2M FE with 512 FE/t I/O
	 */
	public TileMediumVoltageSU() {
		super("MEDIUM_VOLTAGE_SU", 2, ModBlocks.MEDIUM_VOLTAGE_SU, EnumPowerTier.MEDIUM, 128, 128, 300000);
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("mfe").player(player.inventory).inventory().hotbar().armor()
			.complete(8, 18).addArmor().addInventory().tile(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
			.syncEnergyValue().addInventory().create(this);
	}

}