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

package techreborn.compat.rei;

import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import techreborn.TechReborn;
import techreborn.api.recipe.recipes.RollingMachineRecipe;
import techreborn.compat.rei.rollingmachine.RollingMachineCategory;
import techreborn.compat.rei.rollingmachine.RollingMachineDisplay;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRContent.Machine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReiPlugin implements REIPluginV0 {

	public static final Identifier PLUGIN = new Identifier(TechReborn.MOD_ID, "techreborn_plugin");

	public static final Map<RebornRecipeType<?>, ItemConvertible> iconMap = new HashMap<>();

	public ReiPlugin() {
		iconMap.put(ModRecipes.ALLOY_SMELTER, Machine.ALLOY_SMELTER);
		iconMap.put(ModRecipes.ASSEMBLING_MACHINE, Machine.ASSEMBLY_MACHINE);
		iconMap.put(ModRecipes.BLAST_FURNACE, Machine.INDUSTRIAL_BLAST_FURNACE);
		iconMap.put(ModRecipes.CENTRIFUGE, Machine.INDUSTRIAL_CENTRIFUGE);
		iconMap.put(ModRecipes.CHEMICAL_REACTOR, Machine.CHEMICAL_REACTOR);
		iconMap.put(ModRecipes.COMPRESSOR, Machine.COMPRESSOR);
		iconMap.put(ModRecipes.DISTILLATION_TOWER, Machine.DISTILLATION_TOWER);
		iconMap.put(ModRecipes.EXTRACTOR, Machine.EXTRACTOR);
		iconMap.put(ModRecipes.FLUID_REPLICATOR, Machine.FLUID_REPLICATOR);
		iconMap.put(ModRecipes.FUSION_REACTOR, Machine.FUSION_CONTROL_COMPUTER);
		iconMap.put(ModRecipes.GRINDER, Machine.GRINDER);
		iconMap.put(ModRecipes.IMPLOSION_COMPRESSOR, Machine.IMPLOSION_COMPRESSOR);
		iconMap.put(ModRecipes.INDUSTRIAL_ELECTROLYZER, Machine.INDUSTRIAL_ELECTROLYZER);
		iconMap.put(ModRecipes.INDUSTRIAL_GRINDER, Machine.INDUSTRIAL_GRINDER);
		iconMap.put(ModRecipes.INDUSTRIAL_SAWMILL, Machine.INDUSTRIAL_SAWMILL);
		iconMap.put(ModRecipes.ROLLING_MACHINE, Machine.ROLLING_MACHINE);
		iconMap.put(ModRecipes.SCRAPBOX, TRContent.SCRAP_BOX);
		iconMap.put(ModRecipes.VACUUM_FREEZER, Machine.VACUUM_FREEZER);

	}

	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN;
	}

	@Override
	public SemanticVersion getMinimumVersion() throws VersionParsingException {
		return SemanticVersion.parse("3.0-pre");
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
	    recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.ALLOY_SMELTER));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.ASSEMBLING_MACHINE));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.BLAST_FURNACE));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.CENTRIFUGE, 4));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.CHEMICAL_REACTOR));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.COMPRESSOR, 1));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.DISTILLATION_TOWER, 3));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.EXTRACTOR, 1));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.FLUID_REPLICATOR, 1));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.FUSION_REACTOR, 2));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.GRINDER, 1));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.IMPLOSION_COMPRESSOR));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.INDUSTRIAL_ELECTROLYZER, 4));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.INDUSTRIAL_GRINDER, 3));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.INDUSTRIAL_SAWMILL, 3));
        recipeHelper.registerCategory(new RollingMachineCategory(ModRecipes.ROLLING_MACHINE));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.SCRAPBOX));
        recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.VACUUM_FREEZER, 1));
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		RecipeManager.getRecipeTypes("techreborn").forEach(rebornRecipeType -> registerMachineRecipe(recipeHelper, rebornRecipeType));
}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(ModRecipes.ALLOY_SMELTER.getName(), new ItemStack(Machine.ALLOY_SMELTER.asItem()), new ItemStack(Machine.IRON_ALLOY_FURNACE.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.BLAST_FURNACE.getName(), new ItemStack(Machine.INDUSTRIAL_BLAST_FURNACE.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.CENTRIFUGE.getName(), new ItemStack(Machine.INDUSTRIAL_CENTRIFUGE.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.CHEMICAL_REACTOR.getName(), new ItemStack(Machine.CHEMICAL_REACTOR.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.COMPRESSOR.getName(), new ItemStack(Machine.COMPRESSOR.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.DISTILLATION_TOWER.getName(), new ItemStack(Machine.DISTILLATION_TOWER.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.EXTRACTOR.getName(), new ItemStack(Machine.EXTRACTOR.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.FLUID_REPLICATOR.getName(), new ItemStack(Machine.FLUID_REPLICATOR.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.FUSION_REACTOR.getName(), new ItemStack(Machine.FUSION_CONTROL_COMPUTER.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.GRINDER.getName(), new ItemStack(Machine.GRINDER.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.IMPLOSION_COMPRESSOR.getName(), new ItemStack(Machine.IMPLOSION_COMPRESSOR.asItem()));
        recipeHelper.registerWorkingStations(ModRecipes.INDUSTRIAL_ELECTROLYZER.getName(), new ItemStack(Machine.INDUSTRIAL_ELECTROLYZER.asItem()));
        recipeHelper.registerWorkingStations(ModRecipes.INDUSTRIAL_GRINDER.getName(), new ItemStack(Machine.INDUSTRIAL_GRINDER.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.INDUSTRIAL_SAWMILL.getName(), new ItemStack(Machine.INDUSTRIAL_SAWMILL.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.ROLLING_MACHINE.getName(), new ItemStack(Machine.ROLLING_MACHINE.asItem()));
		recipeHelper.registerWorkingStations(ModRecipes.VACUUM_FREEZER.getName(), new ItemStack(Machine.VACUUM_FREEZER.asItem()));
	}

	private <R extends RebornRecipe> void registerMachineRecipe(RecipeHelper recipeHelper, RebornRecipeType<R> recipeType){
		Function<R, RecipeDisplay> recipeDisplay = r -> new MachineRecipeDisplay<>((RebornRecipe) r);

		if(recipeType == ModRecipes.ROLLING_MACHINE){
			recipeDisplay = r -> {
				RollingMachineRecipe rollingMachineRecipe = (RollingMachineRecipe) r;
				return new RollingMachineDisplay(rollingMachineRecipe.getShapedRecipe());
			};
		}

		recipeHelper.registerRecipes(recipeType.getName(), (Predicate<Recipe>) recipe -> {
			if (recipe instanceof RebornRecipe) {
				return ((RebornRecipe) recipe).getRebornRecipeType() == recipeType;
			}
			return false;
		}, recipeDisplay);
	}
}
