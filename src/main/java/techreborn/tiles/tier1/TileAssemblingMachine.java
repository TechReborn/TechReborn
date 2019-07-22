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

package techreborn.tiles.tier1;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.api.recipe.RecipeHandler;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileGenericMachine;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileAssemblingMachine extends TileGenericMachine implements IContainerProvider {
	// Fields >>
	@ConfigRegistry(config = "machines", category = "assembling_machine", key = "AssemblingMachineMaxInput", comment = "Assembling Machine Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "assembling_machine", key = "AssemblingMachineMaxEnergy", comment = "Assembling Machine Max Energy (Value in EU)")
	public static int maxEnergy = 1_000;
	// << Fields

	public TileAssemblingMachine() {
		super("AssemblingMachine", maxInput, maxEnergy, ModBlocks.ASSEMBLING_MACHINE, 3);
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] { 2 };
		this.inventory = new Inventory(4, "TileAssemblingMachine", 64, this);
		this.crafter = new RecipeCrafter(Reference.ASSEMBLING_MACHINE_RECIPE, this, 2, 1, this.inventory, inputs, outputs);
	}
	
	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("assemblingmachine").player(player.inventory).inventory().hotbar()
			.addInventory()
			.tile(this)
			.filterSlot(0, 34, 47,
				stack -> RecipeHandler.recipeList.stream()
					.anyMatch(recipe -> recipe instanceof AssemblingMachineRecipe
						&& ItemUtils.isInputEqual(recipe.getInputs().get(0), stack, true, true, true)))
			.filterSlot(1, 126, 47,
				stack -> RecipeHandler.recipeList.stream()
					.anyMatch(recipe -> recipe instanceof AssemblingMachineRecipe
						&& ItemUtils.isInputEqual(recipe.getInputs().get(1), stack, true, true, true)))
			.outputSlot(2, 80, 47)
			.energySlot(3, 8, 72)
			.syncEnergyValue()
			.syncCrafterValue()
			.addInventory()
			.create(this);
	}
}
