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
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.RebornInventory;
import techreborn.TechReborn;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;
import techreborn.blockentity.GenericMachineBlockEntity;

@RebornRegister(TechReborn.MOD_ID)
public class ExtractorBlockEntity extends GenericMachineBlockEntity implements IContainerProvider {
	
	@ConfigRegistry(config = "machines", category = "extractor", key = "ExtractorInput", comment = "Extractor Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "extractor", key = "ExtractorMaxEnergy", comment = "Extractor Max Energy (Value in EU)")
	public static int maxEnergy = 1_000;

	public ExtractorBlockEntity() {
		super(TRBlockEntities.EXTRACTOR, "Extractor", maxInput, maxEnergy, TRContent.Machine.EXTRACTOR.block, 2);
		final int[] inputs = new int[] { 0 };
		final int[] outputs = new int[] { 1 };
		this.inventory = new RebornInventory<>(3, "ExtractorBlockEntity", 64, this).withConfiguredAccess();
		this.crafter = new RecipeCrafter(ModRecipes.EXTRACTOR, this, 2, 1, this.inventory, inputs, outputs);
	}
	
	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("extractor").player(player.inventory).inventory().hotbar().addInventory().blockEntity(this)
				.slot(0, 55, 45).outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue().syncCrafterValue()
				.addInventory().create(this, syncID);
	}
}
