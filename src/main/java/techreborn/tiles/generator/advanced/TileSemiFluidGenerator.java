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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.api.generator.EFluidGenerator;
import techreborn.init.TRContent;
import techreborn.tiles.generator.TileBaseFluidGenerator;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;

@RebornRegister(TechReborn.MOD_ID)
public class TileSemiFluidGenerator extends TileBaseFluidGenerator implements IContainerProvider {

	@ConfigRegistry(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorMaxOutput", comment = "Semifluid Generator Max Output (Value in EU)")
	public static int maxOutput = 128;
	@ConfigRegistry(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorMaxEnergy", comment = "Semifluid Generator Max Energy (Value in EU)")
	public static int maxEnergy = 1000000;
	@ConfigRegistry(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorTankCapacity", comment = "Semifluid Generator Tank Capacity")
	public static int tankCapacity = 10000;
	@ConfigRegistry(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorEnergyPerTick", comment = "Semifluid Generator Energy Per Tick (Value in EU)")
	public static int energyPerTick = 8;

	public TileSemiFluidGenerator() {
		super(EFluidGenerator.SEMIFLUID, "TileSemiFluidGenerator", tankCapacity, energyPerTick);
	}

	@Override
	public ItemStack getToolDrop(EntityPlayer playerIn) {
		return TRContent.Machine.SEMI_FLUID_GENERATOR.getStack();
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
		return new ContainerBuilder("semifluidgenerator").player(player.inventory).inventory().hotbar()
			.addInventory().tile(this).slot(0, 25, 35).outputSlot(1, 25, 55).syncEnergyValue()
			.syncIntegerValue(this::getTicksSinceLastChange, this::setTicksSinceLastChange)
			.syncIntegerValue(this::getTankAmount, this::setTankAmount)
			.addInventory().create(this);
	}
}
