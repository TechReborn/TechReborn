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

package techreborn.blockentity.machine.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class ImplosionCompressorBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	public ImplosionCompressorBlockEntity() {
		super(TRBlockEntities.IMPLOSION_COMPRESSOR, "ImplosionCompressor", TechRebornConfig.implosionCompressorMaxInput, TechRebornConfig.implosionCompressorMaxEnergy, TRContent.Machine.IMPLOSION_COMPRESSOR.block, 4);
		final int[] inputs = new int[]{0, 1};
		final int[] outputs = new int[]{2, 3};
		this.inventory = new RebornInventory<>(5, "ImplosionCompressorBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.IMPLOSION_COMPRESSOR, this, 2, 2, this.inventory, inputs, outputs);
	}

	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		writer.translate(-1, -3, -1)
				.fill(0, 0, 0, 3, 1, 3, TRContent.MachineBlocks.ADVANCED.getCasing().getDefaultState())
				.ringWithAir(Direction.Axis.Y, 3, 1, 3, TRContent.MachineBlocks.ADVANCED.getCasing().getDefaultState())
				.fill(0, 2, 0, 3, 3, 3, TRContent.MachineBlocks.ADVANCED.getCasing().getDefaultState());
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("implosioncompressor").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 50, 27).slot(1, 50, 47).outputSlot(2, 92, 36).outputSlot(3, 110, 36)
				.energySlot(4, 8, 72).syncEnergyValue().syncCrafterValue().addInventory().create(this, syncID);
	}

	@Override
	public boolean canCraft(RebornRecipe rebornRecipe) {
		return isMultiblockValid();
	}
}
