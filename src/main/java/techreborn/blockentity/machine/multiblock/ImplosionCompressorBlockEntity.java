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

package techreborn.blockentity.machine.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModRecipes;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class ImplosionCompressorBlockEntity extends GenericMachineBlockEntity implements IContainerProvider {

	public MultiblockChecker multiblockChecker;

	public ImplosionCompressorBlockEntity() {
		super(TRBlockEntities.IMPLOSION_COMPRESSOR, "ImplosionCompressor", TechRebornConfig.implosionCompressorMaxInput, TechRebornConfig.implosionCompressorMaxEnergy, TRContent.Machine.IMPLOSION_COMPRESSOR.block, 4);
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] { 2, 3 };
		this.inventory = new RebornInventory<>(5, "ImplosionCompressorBlockEntity", 64, this);
		this.crafter = new RecipeCrafter(ModRecipes.IMPLOSION_COMPRESSOR, this, 2, 2, this.inventory, inputs, outputs);
	}
	
	public boolean getMutliBlock() {
		if(multiblockChecker == null){
			return false;
		}
		final boolean down = multiblockChecker.checkRectY(1, 1, MultiblockChecker.REINFORCED_CASING, MultiblockChecker.ZERO_OFFSET);
		final boolean up = multiblockChecker.checkRectY(1, 1, MultiblockChecker.REINFORCED_CASING, new BlockPos(0, 2, 0));
		final boolean chamber = multiblockChecker.checkRingYHollow(1, 1, MultiblockChecker.REINFORCED_CASING, new BlockPos(0, 1, 0));
		return down && chamber && up;
	}

	// TileGenericMachine
	@Override
	public void tick() {
		if (multiblockChecker == null) {
			multiblockChecker = new MultiblockChecker(world, pos.down(3));
		}

		super.tick();
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("implosioncompressor").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 50, 27).slot(1, 50, 47).outputSlot(2, 92, 36).outputSlot(3, 110, 36)
				.energySlot(4, 8, 72).syncEnergyValue().syncCrafterValue().addInventory().create(this, syncID);
	}

	@Override
	public boolean canCraft(RebornRecipe rebornRecipe) {
		return getMutliBlock();
	}
}
