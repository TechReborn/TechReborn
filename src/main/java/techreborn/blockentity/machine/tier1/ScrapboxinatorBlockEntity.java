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

package techreborn.blockentity.machine.tier1;

import net.minecraft.entity.player.PlayerEntity;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.api.recipe.ScrapboxRecipeCrafter;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

public class ScrapboxinatorBlockEntity extends GenericMachineBlockEntity implements IContainerProvider {

	public ScrapboxinatorBlockEntity() {
		super(TRBlockEntities.SCRAPBOXINATOR, "Scrapboxinator", TechRebornConfig.scrapboxinatorMaxInput, TechRebornConfig.scrapboxinatorMaxEnergy, TRContent.Machine.SCRAPBOXINATOR.block, 2);
		final int[] inputs = new int[] { 0 };
		final int[] outputs = new int[] { 1 };
		this.inventory = new RebornInventory<>(3, "ScrapboxinatorBlockEntity", 64, this);
		this.crafter = new ScrapboxRecipeCrafter(this, this.inventory, inputs, outputs);
	}
	
	// TileMachineBase
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("scrapboxinator").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).filterSlot(0, 55, 45, stack -> stack.getItem() == TRContent.SCRAP_BOX).outputSlot(1, 101, 45)
				.energySlot(2, 8, 72).syncEnergyValue().syncCrafterValue().addInventory().create(this, syncID);
	}
}