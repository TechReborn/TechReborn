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

package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.api.generator.EFluidGenerator;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.TRContent;

@RebornRegister(modID = TechReborn.MOD_ID)
public class TilePlasmaGenerator extends TileBaseFluidGenerator implements IContainerProvider {

	@ConfigRegistry(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorMaxOutput", comment = "Plasma Generator Max Output (Value in EU)")
	public static int maxOutput = 2048;
	@ConfigRegistry(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorMaxEnergy", comment = "Plasma Generator Max Energy (Value in EU)")
	public static double maxEnergy = 500_000_000;
	@ConfigRegistry(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorTankCapacity", comment = "Plasma Generator Tank Capacity")
	public static int tankCapacity = 10_000;
	@ConfigRegistry(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorEnergyPerTick", comment = "Plasma Generator Energy Per Tick (Value in EU)")
	public static int energyPerTick = 400;

	public TilePlasmaGenerator() {
		super(EFluidGenerator.PLASMA, "TilePlasmaGenerator", tankCapacity, energyPerTick);
	}

	@Override
	public ItemStack getToolDrop(EntityPlayer playerIn) {
		return new ItemStack(TRContent.PLASMA_GENERATOR, 1);
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
		return new ContainerBuilder("plasmagenerator").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).slot(0, 25, 35).outputSlot(1, 25, 55).syncEnergyValue()
			.syncIntegerValue(this::getTicksSinceLastChange, this::setTicksSinceLastChange)
			.syncIntegerValue(this::getTankAmount, this::setTankAmount)
			.addInventory().create(this);
	}

}
