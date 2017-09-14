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
public class TileThermalGenerator extends TileBaseFluidGenerator implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "thermal_generator", key = "ThermalGeneratorMaxOutput", comment = "Thermal Generator Max Output (Value in EU)")
	public static int maxOutput = 128;
	@ConfigRegistry(config = "machines", category = "thermal_generator", key = "ThermalGeneratorMaxEnergy", comment = "Thermal Generator Max Energy (Value in EU)")
	public static int maxEnergy = 1000000;
	@ConfigRegistry(config = "machines", category = "thermal_generator", key = "ThermalGeneratorTankCapacity", comment = "Thermal Generator Tank Capacity")
	public static int tankCapacity = 10000;
	@ConfigRegistry(config = "machines", category = "thermal_generator", key = "ThermalGeneratorEnergyPerTick", comment = "Thermal Generator Energy Per Tick (Value in EU)")
	public static int energyPerTick = 10;

	public TileThermalGenerator() {
		super(EFluidGenerator.THERMAL, "TileThermalGenerator", tankCapacity, energyPerTick);
	}

	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.THERMAL_GENERATOR, 1);
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
		return new ContainerBuilder("thermalgenerator").player(player.inventory).inventory().hotbar()
			.addInventory().tile(this).slot(0, 25, 35).outputSlot(1, 25, 55).syncEnergyValue()
			.addInventory().create(this);
	}
}
