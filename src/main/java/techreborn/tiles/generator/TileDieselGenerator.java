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

package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.api.generator.EFluidGenerator;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileDieselGenerator extends TileBaseFluidGenerator implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "diesel_generator", key = "DieselGeneratorMaxOutput", comment = "Diesel Generator Max Output (Value in EU)")
	public static int maxOutput = 128;
	@ConfigRegistry(config = "machines", category = "diesel_generator", key = "DieselGeneratorMaxEnergy", comment = "Diesel Generator Max Energy (Value in EU)")
	public static int maxEnergy = 1000000;
	@ConfigRegistry(config = "machines", category = "diesel_generator", key = "DieselGeneratorTankCapacity", comment = "Diesel Generator Tank Capacity")
	public static int tankCapacity = 10000;
	@ConfigRegistry(config = "machines", category = "diesel_generator", key = "DieselGeneratorEnergyPerTick", comment = "Diesel Generator Energy Per Tick (Value in EU)")
	public static int energyPerTick = 20;

	public TileDieselGenerator() {
		super(EFluidGenerator.DIESEL, "TileDieselGenerator", tankCapacity, energyPerTick);
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.DIESEL_GENERATOR, 1);
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public double getBaseMaxOutput() {
		return maxOutput;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("dieselgenerator").player(player.inventory).inventory(8, 84).hotbar(8, 142)
			.addInventory().tile(this).slot(0, 80, 17).outputSlot(1, 80, 53).fakeSlot(2, 59, 42).syncEnergyValue()
			.addInventory().create();
	}
}
