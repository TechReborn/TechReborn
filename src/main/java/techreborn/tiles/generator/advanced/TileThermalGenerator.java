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

package techreborn.tiles.generator.advanced;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.api.generator.EFluidGenerator;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;
import techreborn.tiles.generator.TileBaseFluidGenerator;

@RebornRegister(TechReborn.MOD_ID)
public class TileThermalGenerator extends TileBaseFluidGenerator implements IContainerProvider {

	@ConfigRegistry(config = "generators", category = "thermal_generator", key = "ThermalGeneratorMaxOutput", comment = "Thermal Generator Max Output (Value in EU)")
	public static int maxOutput = 128;
	@ConfigRegistry(config = "generators", category = "thermal_generator", key = "ThermalGeneratorMaxEnergy", comment = "Thermal Generator Max Energy (Value in EU)")
	public static int maxEnergy = 1_000_000;
	@ConfigRegistry(config = "generators", category = "thermal_generator", key = "ThermalGeneratorTankCapacity", comment = "Thermal Generator Tank Capacity")
	public static int tankCapacity = 10_000;
	@ConfigRegistry(config = "generators", category = "thermal_generator", key = "ThermalGeneratorEnergyPerTick", comment = "Thermal Generator Energy Per Tick (Value in EU)")
	public static int energyPerTick = 16;

	public TileThermalGenerator() {
		super(TRTileEntities.THERMAL_GEN, EFluidGenerator.THERMAL, "TileThermalGenerator", tankCapacity, energyPerTick);
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return TRContent.Machine.THERMAL_GENERATOR.getStack();
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
	public BuiltContainer createContainer(final PlayerEntity player) {
		return new ContainerBuilder("thermalgenerator").player(player.inventory).inventory().hotbar()
			.addInventory().tile(this).slot(0, 25, 35).outputSlot(1, 25, 55).syncEnergyValue()
			.syncIntegerValue(this::getTicksSinceLastChange, this::setTicksSinceLastChange)
			.syncIntegerValue(this::getTankAmount, this::setTankAmount)
			.addInventory().create(this);
	}
}
