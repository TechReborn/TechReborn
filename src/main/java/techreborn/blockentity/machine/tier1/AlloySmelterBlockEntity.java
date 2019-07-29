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
public class AlloySmelterBlockEntity extends GenericMachineBlockEntity implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "alloy_smelter", key = "AlloySmelterMaxInput", comment = "Alloy Smelter Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "alloy_smelter", key = "AlloySmelterMaxEnergy", comment = "Alloy Smelter Max Energy (Value in EU)")
	public static int maxEnergy = 1_000;

	public AlloySmelterBlockEntity() {
		super(TRBlockEntities.ALLOY_SMELTER, "AlloySmelter", maxInput, maxEnergy, TRContent.Machine.ALLOY_SMELTER.block, 3);
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] { 2 };
		this.inventory = new RebornInventory<>(4, "AlloySmelterBlockEntity", 64, this).withConfiguredAccess();
		this.crafter = new RecipeCrafter(ModRecipes.ALLOY_SMELTER, this, 2, 1, this.inventory, inputs, outputs);
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("alloysmelter").player(player.inventory).inventory().hotbar()
			.addInventory().blockEntity(this)
			.filterSlot(0, 34, 47,
				stack -> ModRecipes.ALLOY_SMELTER.getRecipes(player.world).stream().anyMatch(recipe -> recipe.getRebornIngredients().get(0).test(stack)))
			.filterSlot(1, 126, 47,
			            stack -> ModRecipes.ALLOY_SMELTER.getRecipes(player.world).stream().anyMatch(recipe -> recipe.getRebornIngredients().get(1).test(stack)))
			.outputSlot(2, 80, 47).energySlot(3, 8, 72).syncEnergyValue().syncCrafterValue().addInventory()
			.create(this, syncID);
	}
}