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

package techreborn.blockentity.machine.tier1;

import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class AssemblingMachineBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public AssemblingMachineBlockEntity() {
		super(TRBlockEntities.ASSEMBLY_MACHINE, "AssemblingMachine", TechRebornConfig.assemblingMachineMaxInput, TechRebornConfig.assemblingMachineMaxEnergy, TRContent.Machine.ASSEMBLY_MACHINE.block, 3);
		final int[] inputs = new int[]{0, 1};
		final int[] outputs = new int[]{2};
		this.inventory = new RebornInventory<>(4, "AssemblingMachineBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.ASSEMBLING_MACHINE, this, 2, 2, this.inventory, inputs, outputs);
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("assemblingmachine").player(player.inventory).inventory().hotbar()
				.addInventory().blockEntity(this).slot(0, 55, 35).slot(1, 55, 55).outputSlot(2, 101, 45).energySlot(3, 8, 72)
				.syncEnergyValue().syncCrafterValue().addInventory().create(this, syncID);
	}
}
