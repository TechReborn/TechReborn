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

package techreborn.blockentity.generator.advanced;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import reborncore.common.fluid.FluidValue;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import techreborn.api.generator.EFluidGenerator;
import techreborn.blockentity.generator.BaseFluidGeneratorBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class DieselGeneratorBlockEntity extends BaseFluidGeneratorBlockEntity implements BuiltScreenHandlerProvider {

	public DieselGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.DIESEL_GENERATOR, pos, state, EFluidGenerator.DIESEL, "DieselGeneratorBlockEntity", FluidValue.BUCKET.multiply(10), TechRebornConfig.dieselGeneratorEnergyPerTick);
	}

	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.DIESEL_GENERATOR.getStack();
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.dieselGeneratorMaxEnergy;
	}

	@Override
	public long getBaseMaxOutput() {
		return TechRebornConfig.dieselGeneratorMaxOutput;
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("dieselgenerator").player(player.getInventory()).inventory().hotbar()
				.addInventory().blockEntity(this).slot(0, 25, 35).outputSlot(1, 25, 55).syncEnergyValue()
				.sync(this::getTicksSinceLastChange, this::setTicksSinceLastChange)
				.sync(this::getTankAmount, this::setTankAmount)
				.sync(tank)
				.addInventory().create(this, syncID);
	}
}
