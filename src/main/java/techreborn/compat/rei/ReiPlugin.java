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

package techreborn.compat.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.*;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.slot.GuiTab;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import techreborn.TechReborn;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.recipe.recipes.FluidReplicatorRecipe;
import techreborn.api.recipe.recipes.RollingMachineRecipe;
import techreborn.compat.rei.fluidgenerator.FluidGeneratorRecipeCategory;
import techreborn.compat.rei.fluidgenerator.FluidGeneratorRecipeDisplay;
import techreborn.compat.rei.fluidreplicator.FluidReplicatorRecipeCategory;
import techreborn.compat.rei.fluidreplicator.FluidReplicatorRecipeDisplay;
import techreborn.compat.rei.rollingmachine.RollingMachineCategory;
import techreborn.compat.rei.rollingmachine.RollingMachineDisplay;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.init.TRContent.Machine;

import java.util.Collections;
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
		iconMap.put(ModRecipes.SOLID_CANNING_MACHINE, Machine.SOLID_CANNING_MACHINE);
		iconMap.put(ModRecipes.VACUUM_FREEZER, Machine.VACUUM_FREEZER);
		iconMap.put(ModRecipes.WIRE_MILL, Machine.WIRE_MILL);
	}
	
	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN;
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
		recipeHelper.registerCategory(new FluidReplicatorRecipeCategory(ModRecipes.FLUID_REPLICATOR));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.FUSION_REACTOR, 2));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.GRINDER, 1));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.IMPLOSION_COMPRESSOR));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.INDUSTRIAL_ELECTROLYZER, 4));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.INDUSTRIAL_GRINDER, 3));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.INDUSTRIAL_SAWMILL, 3));
		recipeHelper.registerCategory(new RollingMachineCategory(ModRecipes.ROLLING_MACHINE));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.SCRAPBOX));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.SOLID_CANNING_MACHINE));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.VACUUM_FREEZER, 1));
		recipeHelper.registerCategory(new MachineRecipeCategory<>(ModRecipes.WIRE_MILL, 1));
		
		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.THERMAL_GENERATOR));
		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.GAS_TURBINE));
		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.DIESEL_GENERATOR));
		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.SEMI_FLUID_GENERATOR));
		recipeHelper.registerCategory(new FluidGeneratorRecipeCategory(Machine.PLASMA_GENERATOR));
	}
	
	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		RecipeManager.getRecipeTypes("techreborn").forEach(rebornRecipeType -> registerMachineRecipe(recipeHelper, rebornRecipeType));
		
		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.THERMAL, Machine.THERMAL_GENERATOR);
		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.GAS, Machine.GAS_TURBINE);
		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.DIESEL, Machine.DIESEL_GENERATOR);
		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.SEMIFLUID, Machine.SEMI_FLUID_GENERATOR);
		registerFluidGeneratorDisplays(recipeHelper, EFluidGenerator.PLASMA, Machine.PLASMA_GENERATOR);
	}
	
	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(ModRecipes.ALLOY_SMELTER.getName(), EntryStack.create(Machine.ALLOY_SMELTER), EntryStack.create(Machine.IRON_ALLOY_FURNACE));
		recipeHelper.registerWorkingStations(ModRecipes.ASSEMBLING_MACHINE.getName(), EntryStack.create(Machine.ASSEMBLY_MACHINE));
		recipeHelper.registerWorkingStations(ModRecipes.BLAST_FURNACE.getName(), EntryStack.create(Machine.INDUSTRIAL_BLAST_FURNACE));
		recipeHelper.registerWorkingStations(ModRecipes.CENTRIFUGE.getName(), EntryStack.create(Machine.INDUSTRIAL_CENTRIFUGE));
		recipeHelper.registerWorkingStations(ModRecipes.CHEMICAL_REACTOR.getName(), EntryStack.create(Machine.CHEMICAL_REACTOR));
		recipeHelper.registerWorkingStations(ModRecipes.COMPRESSOR.getName(), EntryStack.create(Machine.COMPRESSOR));
		recipeHelper.registerWorkingStations(ModRecipes.DISTILLATION_TOWER.getName(), EntryStack.create(Machine.DISTILLATION_TOWER));
		recipeHelper.registerWorkingStations(ModRecipes.EXTRACTOR.getName(), EntryStack.create(Machine.EXTRACTOR));
		recipeHelper.registerWorkingStations(ModRecipes.FLUID_REPLICATOR.getName(), EntryStack.create(Machine.FLUID_REPLICATOR));
		recipeHelper.registerWorkingStations(ModRecipes.FUSION_REACTOR.getName(), EntryStack.create(Machine.FUSION_CONTROL_COMPUTER));
		recipeHelper.registerWorkingStations(ModRecipes.GRINDER.getName(), EntryStack.create(Machine.GRINDER));
		recipeHelper.registerWorkingStations(ModRecipes.IMPLOSION_COMPRESSOR.getName(), EntryStack.create(Machine.IMPLOSION_COMPRESSOR));
		recipeHelper.registerWorkingStations(ModRecipes.INDUSTRIAL_ELECTROLYZER.getName(), EntryStack.create(Machine.INDUSTRIAL_ELECTROLYZER));
		recipeHelper.registerWorkingStations(ModRecipes.INDUSTRIAL_GRINDER.getName(), EntryStack.create(Machine.INDUSTRIAL_GRINDER));
		recipeHelper.registerWorkingStations(ModRecipes.INDUSTRIAL_SAWMILL.getName(), EntryStack.create(Machine.INDUSTRIAL_SAWMILL));
		recipeHelper.registerWorkingStations(ModRecipes.ROLLING_MACHINE.getName(), EntryStack.create(Machine.ROLLING_MACHINE));
		recipeHelper.registerWorkingStations(ModRecipes.SOLID_CANNING_MACHINE.getName(), EntryStack.create(Machine.SOLID_CANNING_MACHINE));
		recipeHelper.registerWorkingStations(ModRecipes.VACUUM_FREEZER.getName(), EntryStack.create(Machine.VACUUM_FREEZER));
		recipeHelper.registerWorkingStations(ModRecipes.WIRE_MILL.getName(), EntryStack.create(Machine.WIRE_MILL));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.THERMAL_GENERATOR.name), EntryStack.create(Machine.THERMAL_GENERATOR));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.GAS_TURBINE.name), EntryStack.create(Machine.GAS_TURBINE));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.DIESEL_GENERATOR.name), EntryStack.create(Machine.DIESEL_GENERATOR));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.SEMI_FLUID_GENERATOR.name), EntryStack.create(Machine.SEMI_FLUID_GENERATOR));
		recipeHelper.registerWorkingStations(new Identifier(TechReborn.MOD_ID, Machine.PLASMA_GENERATOR.name), EntryStack.create(Machine.PLASMA_GENERATOR));
	}
	
	@Override
	public void postRegister() {
		// Alright we are going to apply check tags to cells, this should not take long at all.
		// Check Tags will not check the amount of the ItemStack, but will enable checking their tags.
		for (EntryStack stack : EntryRegistry.getInstance().getStacksList())
			applyCellEntry(stack);
	}
	
	public static void applyCellEntry(EntryStack stack) {
		// getItem can be null but this works
		if (stack.getItem() == TRContent.CELL)
			stack.addSetting(EntryStack.Settings.CHECK_TAGS, EntryStack.Settings.TRUE);
	}
	
	private void registerFluidGeneratorDisplays(RecipeHelper recipeHelper, EFluidGenerator generator, Machine machine) {
		Identifier identifier = new Identifier(TechReborn.MOD_ID, machine.name);
		GeneratorRecipeHelper.getFluidRecipesForGenerator(generator).getRecipes().forEach(recipe -> {
			recipeHelper.registerDisplay(new FluidGeneratorRecipeDisplay(recipe, identifier));
		});
	}
	
	private <R extends RebornRecipe> void registerMachineRecipe(RecipeHelper recipeHelper, RebornRecipeType<R> recipeType) {
		Function<R, RecipeDisplay> recipeDisplay = r -> new MachineRecipeDisplay<>((RebornRecipe) r);
		
		if (recipeType == ModRecipes.ROLLING_MACHINE) {
			recipeDisplay = r -> {
				RollingMachineRecipe rollingMachineRecipe = (RollingMachineRecipe) r;
				return new RollingMachineDisplay(rollingMachineRecipe.getShapedRecipe());
			};
		}
		
		if (recipeType == ModRecipes.FLUID_REPLICATOR) {
			recipeDisplay = r -> {
				FluidReplicatorRecipe recipe = (FluidReplicatorRecipe) r;
				return new FluidReplicatorRecipeDisplay(recipe);
			};
		}
		
		
		recipeHelper.registerRecipes(recipeType.getName(), (Predicate<Recipe>) recipe -> {
			if (recipe instanceof RebornRecipe) {
				return ((RebornRecipe) recipe).getRebornRecipeType() == recipeType;
			}
			return false;
		}, recipeDisplay);
	}

	@Override
	public void registerBounds(DisplayHelper displayHelper) {
		BaseBoundsHandler baseBoundsHandler = BaseBoundsHandler.getInstance();
		baseBoundsHandler.registerExclusionZones(GuiBase.class, () -> {
			Screen currentScreen = MinecraftClient.getInstance().currentScreen;
			if (currentScreen instanceof GuiBase) {
				GuiBase<?> guiBase = (GuiBase<?>) currentScreen;
				int height = 0;
				if (guiBase.tryAddUpgrades() && guiBase.be instanceof IUpgradeable) {
					IUpgradeable upgradeable = (IUpgradeable) guiBase.be;
					if (upgradeable.canBeUpgraded()) {
						height = 80;
					}
				}
				for (GuiTab slot : guiBase.getTabs()) {
					if (slot.enabled()) {
						height += 24;
					}
				}
				if (height > 0) {
					int width = 20;
					return Collections.singletonList(new Rectangle(guiBase.getGuiLeft() - width, guiBase.getGuiTop() + 8, width , height));
				}
			}
			return Collections.emptyList();
		});
	}
}
