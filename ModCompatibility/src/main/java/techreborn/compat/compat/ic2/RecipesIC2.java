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

package techreborn.compat.compat.ic2;

import ic2.api.item.IC2Items;
import ic2.core.ref.ItemName;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.RebornCraftingHelper;
import techreborn.api.recipe.machines.CompressorRecipe;
import techreborn.api.recipe.machines.ExtractorRecipe;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.compat.compat.ICompatModule;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.init.recipes.RecipeMethods;
import techreborn.items.ItemParts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 06/06/2016.
 */
public class RecipesIC2 implements ICompatModule {

	List<RecipeDuplicate> recipeDuplicateList = new ArrayList<>();

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}

	// LOW is used as we want it to load as late as possible, but before crafttweaker
	@SubscribeEvent(priority = EventPriority.LOW)
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		recipeDuplicateList.add(new RecipeDuplicate(new ItemStack(ModBlocks.MACHINE_FRAMES, 1, 0),
				IC2Items.getItem("resource", "machine")));

		for (RecipeDuplicate duplicate : recipeDuplicateList) {
			duplicate.add();
		}

		RebornCraftingHelper.addShapelessRecipe(ItemParts.getPartByName("rubber"),
				IC2Items.getItem("crafting", "rubber"));
		RebornCraftingHelper.addShapelessRecipe(IC2Items.getItem("crafting", "rubber"),
				ItemParts.getPartByName("rubber"));
		RebornCraftingHelper.addShapelessRecipe(IC2Items.getItem("electric_wrench"), new ItemStack(ModItems.WRENCH),
				IC2Items.getItem("crafting", "small_power_unit"));

		RecipeHandler.addRecipe(new CompressorRecipe(IC2Items.getItem("crafting", "carbon_mesh"),
				IC2Items.getItem("crafting", "carbon_plate"), 300, 4));
		RecipeHandler.addRecipe(new CompressorRecipe(IC2Items.getItem("crafting", "coal_ball"),
				IC2Items.getItem("crafting", "coal_block"), 300, 4));

		RecipeHandler.addRecipe(new GrinderRecipe(ItemName.crafting.getItemStack("tin_can"),
				RecipeMethods.getOre("dustTin", 2), 300, 16));

		RecipeHandler.addRecipe(new ExtractorRecipe(ItemName.filled_tin_can.getItemStack(),
				ItemName.crafting.getItemStack("tin_can"), 300, 16));
	}

	public class RecipeDuplicate {
		ItemStack stack1;
		ItemStack stack2;

		public RecipeDuplicate(ItemStack stack1, ItemStack stack2) {
			this.stack1 = stack1;
			this.stack2 = stack2;
		}

		public void add() {
			RebornCraftingHelper.addShapelessRecipe(stack2, stack1);
			RebornCraftingHelper.addShapelessRecipe(stack1, stack2);
		}
	}
}
