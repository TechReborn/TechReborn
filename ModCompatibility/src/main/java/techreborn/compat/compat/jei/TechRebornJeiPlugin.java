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

package techreborn.compat.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.collect.ListMultiMap;
import mezz.jei.config.Config;
import mezz.jei.gui.TooltipRenderer;
import mezz.jei.gui.recipes.RecipeClickableArea;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Translator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.lwjgl.input.Mouse;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.api.fluidreplicator.FluidReplicatorRecipe;
import techreborn.api.fluidreplicator.FluidReplicatorRecipeList;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.FluidGeneratorRecipe;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.machines.*;
import techreborn.blocks.cable.EnumCableType;
import techreborn.client.gui.*;
import techreborn.client.gui.slot.GuiSlotConfiguration;
import techreborn.compat.compat.CompatConfigs;
import techreborn.compat.compat.CompatManager;
import techreborn.compat.compat.jei.alloySmelter.AlloySmelterRecipeCategory;
import techreborn.compat.compat.jei.alloySmelter.AlloySmelterRecipeWrapper;
import techreborn.compat.compat.jei.assemblingMachine.AssemblingMachineRecipeCategory;
import techreborn.compat.compat.jei.assemblingMachine.AssemblingMachineRecipeWrapper;
import techreborn.compat.compat.jei.blastFurnace.BlastFurnaceRecipeCategory;
import techreborn.compat.compat.jei.blastFurnace.BlastFurnaceRecipeWrapper;
import techreborn.compat.compat.jei.centrifuge.CentrifugeRecipeCategory;
import techreborn.compat.compat.jei.centrifuge.CentrifugeRecipeWrapper;
import techreborn.compat.compat.jei.chemicalReactor.ChemicalReactorRecipeCategory;
import techreborn.compat.compat.jei.chemicalReactor.ChemicalReactorRecipeWrapper;
import techreborn.compat.compat.jei.compressor.CompressorRecipeCategory;
import techreborn.compat.compat.jei.compressor.CompressorRecipeWrapper;
import techreborn.compat.compat.jei.distillationTower.DistillationTowerRecipeCategory;
import techreborn.compat.compat.jei.distillationTower.DistillationTowerRecipeWrapper;
import techreborn.compat.compat.jei.extractor.ExtractorRecipeCategory;
import techreborn.compat.compat.jei.extractor.ExtractorRecipeWrapper;
import techreborn.compat.compat.jei.fluidReplicator.FluidReplicatorRecipeCategory;
import techreborn.compat.compat.jei.fluidReplicator.FluidReplicatorRecipeWrapper;
import techreborn.compat.compat.jei.fusionReactor.FusionReactorRecipeCategory;
import techreborn.compat.compat.jei.fusionReactor.FusionReactorRecipeWrapper;
import techreborn.compat.compat.jei.generators.fluid.FluidGeneratorRecipeCategory;
import techreborn.compat.compat.jei.generators.fluid.FluidGeneratorRecipeWrapper;
import techreborn.compat.compat.jei.grinder.GrinderRecipeCategory;
import techreborn.compat.compat.jei.grinder.GrinderRecipeWrapper;
import techreborn.compat.compat.jei.implosionCompressor.ImplosionCompressorRecipeCategory;
import techreborn.compat.compat.jei.implosionCompressor.ImplosionCompressorRecipeWrapper;
import techreborn.compat.compat.jei.industrialElectrolyzer.IndustrialElectrolyzerRecipeCategory;
import techreborn.compat.compat.jei.industrialElectrolyzer.IndustrialElectrolyzerRecipeWrapper;
import techreborn.compat.compat.jei.industrialGrinder.IndustrialGrinderRecipeCategory;
import techreborn.compat.compat.jei.industrialGrinder.IndustrialGrinderRecipeWrapper;
import techreborn.compat.compat.jei.industrialSawmill.IndustrialSawmillRecipeCategory;
import techreborn.compat.compat.jei.industrialSawmill.IndustrialSawmillRecipeWrapper;
import techreborn.compat.compat.jei.rollingMachine.RollingMachineRecipeCategory;
import techreborn.compat.compat.jei.rollingMachine.RollingMachineRecipeMaker;
import techreborn.compat.compat.jei.rollingMachine.RollingMachineRecipeWrapper;
import techreborn.compat.compat.jei.scrapbox.ScrapboxRecipeCategory;
import techreborn.compat.compat.jei.scrapbox.ScrapboxRecipeWrapper;
import techreborn.compat.compat.jei.vacuumFreezer.VacuumFreezerRecipeCategory;
import techreborn.compat.compat.jei.vacuumFreezer.VacuumFreezerRecipeWrapper;
import techreborn.dispenser.BehaviorDispenseScrapbox;
import techreborn.init.IC2Duplicates;
import techreborn.init.ModBlocks;
import techreborn.init.ModFluids;
import techreborn.init.ModItems;
import techreborn.items.ItemParts;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@mezz.jei.api.JEIPlugin
public class TechRebornJeiPlugin implements IModPlugin {

	public static final ListMultiMap<Class<? extends GuiContainer>, RecipeClickableArea> recipeClickableAreas = new ListMultiMap<>();

	static IRecipesGui recipesGui;

	static {
		MinecraftForge.EVENT_BUS.register(TechRebornJeiPlugin.class);
	}

	private static void addDebugRecipes(IModRegistry registry) {
		ItemStack diamondBlock = new ItemStack(Blocks.DIAMOND_BLOCK);
		ItemStack dirtBlock = new ItemStack(Blocks.DIRT);
		List<Object> debugRecipes = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			int time = (int) Math.round(200 + Math.random() * 100);
			AssemblingMachineRecipe assemblingMachineRecipe = new AssemblingMachineRecipe(diamondBlock, diamondBlock,
					dirtBlock, time, 120);
			debugRecipes.add(assemblingMachineRecipe);
		}
		registry.addRecipes(debugRecipes, RecipeCategoryUids.ASSEMBLING_MACHINE);
		debugRecipes.clear();
		for (int i = 0; i < 10; i++) {
			int time = (int) Math.round(200 + Math.random() * 100);
			ImplosionCompressorRecipe recipe = new ImplosionCompressorRecipe(diamondBlock, diamondBlock, dirtBlock,
					dirtBlock, time, 120);
			debugRecipes.add(recipe);
		}
		registry.addRecipes(debugRecipes, RecipeCategoryUids.IMPLOSION_COMPRESSOR);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		registry.addRecipeCategories(new AlloySmelterRecipeCategory(guiHelper));
		registry.addRecipeCategories(new AssemblingMachineRecipeCategory(guiHelper));
		registry.addRecipeCategories(new BlastFurnaceRecipeCategory(guiHelper));
		registry.addRecipeCategories(new CentrifugeRecipeCategory(guiHelper));
		registry.addRecipeCategories(new ChemicalReactorRecipeCategory(guiHelper));
		registry.addRecipeCategories(new DistillationTowerRecipeCategory(guiHelper));
		registry.addRecipeCategories(new FusionReactorRecipeCategory(guiHelper));
		registry.addRecipeCategories(new GrinderRecipeCategory(guiHelper));
		registry.addRecipeCategories(new ImplosionCompressorRecipeCategory(guiHelper));
		registry.addRecipeCategories(new IndustrialGrinderRecipeCategory(guiHelper));
		registry.addRecipeCategories(new IndustrialElectrolyzerRecipeCategory(guiHelper));
		registry.addRecipeCategories(new IndustrialSawmillRecipeCategory(guiHelper));
		registry.addRecipeCategories(new RollingMachineRecipeCategory(guiHelper));
		registry.addRecipeCategories(new VacuumFreezerRecipeCategory(guiHelper));
		registry.addRecipeCategories(new FluidReplicatorRecipeCategory(guiHelper));

		for (EFluidGenerator type : EFluidGenerator.values())
			registry.addRecipeCategories(new FluidGeneratorRecipeCategory(type, guiHelper));
		
		if (CompatConfigs.showScrapbox) {
			registry.addRecipeCategories(new ScrapboxRecipeCategory(guiHelper));
		}

		if (!IC2Duplicates.deduplicate()) {
			registry.addRecipeCategories(new CompressorRecipeCategory(guiHelper));
			registry.addRecipeCategories(new ExtractorRecipeCategory(guiHelper));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void register(@Nonnull IModRegistry registry) {
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();

		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_BERYLLIUM));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_CALCIUM));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_CALCIUM_CARBONATE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_CHLORITE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_DEUTERIUM));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_GLYCERYL));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_HELIUM));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_HELIUM_3));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_HELIUMPLASMA));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_HYDROGEN));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_LITHIUM));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_MERCURY));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_METHANE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_NITROCOAL_FUEL));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_NITROFUEL));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_NITROGEN));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_NITROGENDIOXIDE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_POTASSIUM));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_SILICON));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_SODIUM));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_SODIUMPERSULFATE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_TRITIUM));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_WOLFRAMIUM));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_SULFUR));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_SULFURIC_ACID));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_CARBON));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_CARBON_FIBER));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_NITRO_CARBON));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_SODIUM_SULFIDE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_DIESEL));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_NITRO_DIESEL));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_OIL));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_ELECTROLYZED_WATER));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModFluids.BLOCK_COMPRESSED_AIR));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModItems.MISSING_RECIPE_PLACEHOLDER));
		
		if (CompatManager.isQuantumStorageLoaded) {
			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.QUANTUM_CHEST));
			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.QUANTUM_TANK));
		}

		if (IC2Duplicates.deduplicate()) {
			for (IC2Duplicates duplicate : IC2Duplicates.values()) {
				if (duplicate.hasIC2Stack()) {
					jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(duplicate.getTrStack());
				}
			}
			for (int i = 0; i < EnumCableType.values().length; i++) {
				jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.CABLE, 1, i));
			}

			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(ItemParts.getPartByName("rubber"));
			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(ItemParts.getPartByName("rubberSap"));
			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(ItemParts.getPartByName("electronicCircuit"));
			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(ItemParts.getPartByName("advancedCircuit"));
			if (!Core.worldGen.config.rubberTreeConfig.shouldSpawn) {
				jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.RUBBER_SAPLING));
				jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.RUBBER_LOG));
				jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.RUBBER_PLANKS));
				jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.RUBBER_LEAVES));
				jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModItems.TREE_TAP));
				jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModItems.ELECTRIC_TREE_TAP));
			}
		}

		registry.handleRecipes(AlloySmelterRecipe.class, recipe -> new AlloySmelterRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.ALLOY_SMELTER);
		registry.handleRecipes(AssemblingMachineRecipe.class, recipe -> new AssemblingMachineRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.ASSEMBLING_MACHINE);
		registry.handleRecipes(BlastFurnaceRecipe.class, recipe -> new BlastFurnaceRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.BLAST_FURNACE);
		registry.handleRecipes(CentrifugeRecipe.class, recipe -> new CentrifugeRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.CENTRIFUGE);
		registry.handleRecipes(ChemicalReactorRecipe.class, recipe -> new ChemicalReactorRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.CHEMICAL_REACTOR);
		registry.handleRecipes(FusionReactorRecipe.class, FusionReactorRecipeWrapper::new, RecipeCategoryUids.FUSION_REACTOR);
		registry.handleRecipes(GrinderRecipe.class, recipe -> new GrinderRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.GRINDER);
		registry.handleRecipes(ImplosionCompressorRecipe.class, recipe -> new ImplosionCompressorRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.IMPLOSION_COMPRESSOR);
		registry.handleRecipes(IndustrialElectrolyzerRecipe.class, recipe -> new IndustrialElectrolyzerRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER);
		registry.handleRecipes(IndustrialGrinderRecipe.class, recipe -> new IndustrialGrinderRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.INDUSTRIAL_GRINDER);
		registry.handleRecipes(IndustrialSawmillRecipe.class, recipe -> new IndustrialSawmillRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.INDUSTRIAL_SAWMILL);
		registry.handleRecipes(VacuumFreezerRecipe.class, recipe -> new VacuumFreezerRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.VACUUM_FREEZER);
		registry.handleRecipes(DistillationTowerRecipe.class, recipe -> new DistillationTowerRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.DISTILLATION_TOWER);
		registry.handleRecipes(FluidReplicatorRecipe.class, recipe -> new FluidReplicatorRecipeWrapper(jeiHelpers,recipe), RecipeCategoryUids.FLUID_REPLICATOR);
		registry.handleRecipes(ShapelessRecipes.class, recipe -> new RollingMachineRecipeWrapper((IRecipeWrapper) recipe), RecipeCategoryUids.ROLLING_MACHINE);
		registry.handleRecipes(ShapedRecipes.class, recipe -> new RollingMachineRecipeWrapper((IRecipeWrapper) recipe), RecipeCategoryUids.ROLLING_MACHINE);
		registry.handleRecipes(ShapedOreRecipe.class, recipe -> new RollingMachineRecipeWrapper((IRecipeWrapper) recipe), RecipeCategoryUids.ROLLING_MACHINE);
		registry.handleRecipes(ShapelessOreRecipe.class, recipe -> new RollingMachineRecipeWrapper((IRecipeWrapper) recipe), RecipeCategoryUids.ROLLING_MACHINE);
		
		for (EFluidGenerator type : EFluidGenerator.values()) {
			registry.handleRecipes(FluidGeneratorRecipe.class, recipe -> new FluidGeneratorRecipeWrapper(jeiHelpers, recipe), type.getRecipeID());
		}
		
		if (CompatConfigs.showScrapbox) {
			registry.handleRecipes(ScrapboxRecipe.class, recipe -> new ScrapboxRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.SCRAPBOX);
		}

		if (!IC2Duplicates.deduplicate()) {
			registry.handleRecipes(CompressorRecipe.class, recipe -> new CompressorRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.COMPRESSOR);
			registry.handleRecipes(ExtractorRecipe.class, recipe -> new ExtractorRecipeWrapper(jeiHelpers, recipe), RecipeCategoryUids.EXTRACTOR);
		}

		registry.addRecipes(RecipeHandler.recipeList.stream().filter(recipe -> {
			if (recipe instanceof ScrapboxRecipe) {
				return CompatConfigs.showScrapbox;
			}
			return true;
		}).collect(Collectors.toList()));

		registry.addRecipes(FusionReactorRecipeHelper.reactorRecipes, RecipeCategoryUids.FUSION_REACTOR);
		registry.addRecipes(FluidReplicatorRecipeList.recipes, RecipeCategoryUids.FLUID_REPLICATOR);
		GeneratorRecipeHelper.fluidRecipes.forEach((type, list) -> registry.addRecipes(list.getRecipes(), type.getRecipeID()));

		try {
			registry.addRecipes(RollingMachineRecipeMaker.getRecipes(jeiHelpers), RecipeCategoryUids.ROLLING_MACHINE);
		} catch (final RuntimeException e) {
			Core.logHelper
				.error("Could not register rolling machine recipes. JEI may have changed its internal recipe wrapper locations.");
			e.printStackTrace();
		}

		if (Config.isDebugModeEnabled()) {
			TechRebornJeiPlugin.addDebugRecipes(registry);
		}

		registry.addIngredientInfo(ItemParts.getPartByName("rubberSap"), ItemStack.class, StringUtils.t("techreborn.desc.rubberSap"));
		if (!BehaviorDispenseScrapbox.dispenseScrapboxes) {
			registry.addIngredientInfo(new ItemStack(ModItems.SCRAP_BOX), ItemStack.class, StringUtils.t("techreborn.desc.scrapBoxNoDispenser"));
		} else {
			registry.addIngredientInfo(new ItemStack(ModItems.SCRAP_BOX), ItemStack.class, StringUtils.t("techreborn.desc.scrapBox"));
		}

		//NEW ONES
		addRecipeClickArea(GuiCentrifuge.class, 150, 4, 20, 12, RecipeCategoryUids.CENTRIFUGE);
		addRecipeClickArea(GuiElectricFurnace.class, 150, 4, 20, 12, VanillaRecipeCategoryUid.SMELTING);
		addRecipeClickArea(GuiGenerator.class, 150, 4, 20, 12, VanillaRecipeCategoryUid.FUEL);
		addRecipeClickArea(GuiExtractor.class, 150, 4, 20, 12, RecipeCategoryUids.EXTRACTOR);
		addRecipeClickArea(GuiCompressor.class, 150, 4, 20, 12, RecipeCategoryUids.COMPRESSOR);
		addRecipeClickArea(GuiGrinder.class, 150, 4, 20, 12, RecipeCategoryUids.GRINDER);
		addRecipeClickArea(GuiVacuumFreezer.class, 150, 4, 20, 12, RecipeCategoryUids.VACUUM_FREEZER);
		addRecipeClickArea(GuiBlastFurnace.class, 150, 4, 20, 12, RecipeCategoryUids.BLAST_FURNACE);
		addRecipeClickArea(GuiChemicalReactor.class, 150, 4, 20, 12, RecipeCategoryUids.CHEMICAL_REACTOR);
		addRecipeClickArea(GuiImplosionCompressor.class, 150, 4, 20, 12, RecipeCategoryUids.IMPLOSION_COMPRESSOR);
		addRecipeClickArea(GuiIndustrialGrinder.class, 150, 4, 20, 12, RecipeCategoryUids.INDUSTRIAL_GRINDER);
		addRecipeClickArea(GuiIndustrialSawmill.class, 150, 4, 20, 15, RecipeCategoryUids.INDUSTRIAL_SAWMILL);
		addRecipeClickArea(GuiIndustrialElectrolyzer.class, 150, 4, 20, 12, RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER);
		addRecipeClickArea(GuiSemifluidGenerator.class, 150, 4, 18, 18, EFluidGenerator.SEMIFLUID.getRecipeID());
		addRecipeClickArea(GuiDieselGenerator.class, 150, 4, 18, 18, EFluidGenerator.DIESEL.getRecipeID());
		addRecipeClickArea(GuiGasTurbine.class, 150, 4, 18, 18, EFluidGenerator.GAS.getRecipeID());
		addRecipeClickArea(GuiThermalGenerator.class, 150, 4, 18, 18, EFluidGenerator.THERMAL.getRecipeID());
		addRecipeClickArea(GuiAlloySmelter.class, 150, 4, 18, 18, RecipeCategoryUids.ALLOY_SMELTER);
		addRecipeClickArea(GuiPlasmaGenerator.class, 150, 4, 18, 18, EFluidGenerator.PLASMA.getRecipeID());
		addRecipeClickArea(GuiDistillationTower.class, 150, 4, 18, 18, RecipeCategoryUids.DISTILLATION_TOWER);
		addRecipeClickArea(GuiScrapboxinator.class, 150, 4, 18, 18, RecipeCategoryUids.SCRAPBOX);
		addRecipeClickArea(GuiFusionReactor.class, 150, 4, 18, 18, RecipeCategoryUids.FUSION_REACTOR);
		addRecipeClickArea(GuiRollingMachine.class, 150, 4, 20, 12, RecipeCategoryUids.ROLLING_MACHINE);
		addRecipeClickArea(GuiFluidReplicator.class, 150, 4, 20, 12, RecipeCategoryUids.FLUID_REPLICATOR);
		addRecipeClickArea(GuiAssemblingMachine.class, 150, 4, 20, 12, RecipeCategoryUids.ASSEMBLING_MACHINE);
		
		//OLD ONES
		addRecipeClickArea(GuiAlloyFurnace.class, 80, 35, 26, 20, RecipeCategoryUids.ALLOY_SMELTER,
			VanillaRecipeCategoryUid.FUEL);
		addRecipeClickArea(GuiIronFurnace.class, 78, 36, 24, 16, VanillaRecipeCategoryUid.SMELTING,
			VanillaRecipeCategoryUid.FUEL);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.IRON_FURNACE), VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.IRON_ALLOY_FURNACE), RecipeCategoryUids.ALLOY_SMELTER, VanillaRecipeCategoryUid.FUEL);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.SOLID_FUEL_GENEREATOR), VanillaRecipeCategoryUid.FUEL);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.SEMI_FLUID_GENERATOR), EFluidGenerator.SEMIFLUID.getRecipeID());
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.GAS_TURBINE), EFluidGenerator.GAS.getRecipeID());
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.DIESEL_GENERATOR), EFluidGenerator.DIESEL.getRecipeID());
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.THERMAL_GENERATOR), EFluidGenerator.THERMAL.getRecipeID());
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.COMPRESSOR), RecipeCategoryUids.COMPRESSOR);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.EXTRACTOR), RecipeCategoryUids.EXTRACTOR);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.GRINDER), RecipeCategoryUids.GRINDER);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.VACUUM_FREEZER), RecipeCategoryUids.VACUUM_FREEZER);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.ELECTRIC_FURNACE), VanillaRecipeCategoryUid.SMELTING);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.ALLOY_SMELTER), RecipeCategoryUids.ALLOY_SMELTER);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.ASSEMBLY_MACHINE), RecipeCategoryUids.ASSEMBLING_MACHINE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.CHEMICAL_REACTOR), RecipeCategoryUids.CHEMICAL_REACTOR);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.FUSION_CONTROL_COMPUTER), RecipeCategoryUids.FUSION_REACTOR);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.IMPLOSION_COMPRESSOR), RecipeCategoryUids.IMPLOSION_COMPRESSOR);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.INDUSTRIAL_BLAST_FURNACE), RecipeCategoryUids.BLAST_FURNACE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), RecipeCategoryUids.CENTRIFUGE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.INDUSTRIAL_GRINDER), RecipeCategoryUids.INDUSTRIAL_GRINDER);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.INDUSTRIAL_SAWMILL), RecipeCategoryUids.INDUSTRIAL_SAWMILL);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.ROLLING_MACHINE), RecipeCategoryUids.ROLLING_MACHINE);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.DISTILLATION_TOWER), RecipeCategoryUids.DISTILLATION_TOWER);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_REPLICATOR), RecipeCategoryUids.FLUID_REPLICATOR);
		
		if (CompatConfigs.showScrapbox) {
			registry.addRecipeCatalyst(new ItemStack(ModItems.SCRAP_BOX), RecipeCategoryUids.SCRAPBOX);
		}

		final IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("fusionreactor", RecipeCategoryUids.FUSION_REACTOR, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("industrialelectrolyzer", RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("industrialgrinder", RecipeCategoryUids.GRINDER, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("implosioncompressor", RecipeCategoryUids.IMPLOSION_COMPRESSOR, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("vacuumfreezer", RecipeCategoryUids.VACUUM_FREEZER, 36, 1, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("blastfurnace", RecipeCategoryUids.BLAST_FURNACE, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("alloyfurnace", RecipeCategoryUids.ALLOY_SMELTER, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("alloyfurnace", VanillaRecipeCategoryUid.FUEL, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("alloysmelter", RecipeCategoryUids.ALLOY_SMELTER, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("assemblingmachine", RecipeCategoryUids.ASSEMBLING_MACHINE, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("chemicalreactor", RecipeCategoryUids.CHEMICAL_REACTOR, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("centrifuge", RecipeCategoryUids.CENTRIFUGE, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("grinder", RecipeCategoryUids.GRINDER, 36, 1, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("extractor", RecipeCategoryUids.EXTRACTOR, 36, 1, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("compressor", RecipeCategoryUids.COMPRESSOR, 36, 1, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("industrialsawmill", RecipeCategoryUids.INDUSTRIAL_SAWMILL, 36, 2, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("distillationtower", RecipeCategoryUids.DISTILLATION_TOWER, 36, 2, 0, 36));

		registry.addAdvancedGuiHandlers(new AdvancedGuiHandler());
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		recipesGui = jeiRuntime.getRecipesGui();
	}

	public static class AdvancedGuiHandler implements IAdvancedGuiHandler<GuiBase> {

		@Override
		public Class<GuiBase> getGuiContainerClass() {
			return GuiBase.class;
		}

		@Nullable
		@Override
		public List<Rectangle> getGuiExtraAreas(GuiBase guiContainer) {
			return GuiSlotConfiguration.getExtraSpace(guiContainer);
		}

		@Nullable
		@Override
		public Object getIngredientUnderMouse(GuiBase guiContainer, int mouseX, int mouseY) {
			return null;
		}
	}

	//Taken from JEI so we can have a custom impliemntation of it
	//This is done as I didnt see an easy way to disable the show recipes button when a certain condition is met
	public void addRecipeClickArea(Class<? extends GuiContainer> guiContainerClass, int xPos, int yPos, int width, int height, String... recipeCategoryUids) {
		ErrorUtil.checkNotNull(guiContainerClass, "guiContainerClass");
		ErrorUtil.checkNotEmpty(recipeCategoryUids, "recipeCategoryUids");

		RecipeClickableArea recipeClickableArea = new RecipeClickableArea(yPos, yPos + height, xPos, xPos + width, recipeCategoryUids);
		recipeClickableAreas.put(guiContainerClass, recipeClickableArea);
	}

	public static RecipeClickableArea getRecipeClickableArea(GuiContainer gui, int mouseX, int mouseY) {
		for (RecipeClickableArea recipeClickableArea : recipeClickableAreas.toImmutable().get(gui.getClass())) {
			if (recipeClickableArea.checkHover(mouseX, mouseY)) {
				return recipeClickableArea;
			}
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void drawScreenEvent(GuiScreenEvent.DrawScreenEvent.Post event) {
		if (GuiBase.showSlotConfig) {
			return;
		}
		GuiScreen gui = event.getGui();
		if (gui instanceof GuiContainer) {
			GuiContainer guiContainer = (GuiContainer) gui;
			if (getRecipeClickableArea(guiContainer, event.getMouseX() - guiContainer.getGuiLeft(), event.getMouseY() - guiContainer.getGuiTop()) != null) {
				TooltipRenderer.drawHoveringText(guiContainer.mc, Translator.translateToLocal("jei.tooltip.show.recipes"), event.getMouseX(), event.getMouseY());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void handleMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
		if (GuiBase.showSlotConfig) {
			return;
		}
		final int eventButton = Mouse.getEventButton();
		if (eventButton > -1) {
			if (Mouse.getEventButtonState()) {
				if (event.getGui() instanceof GuiContainer) {
					int x = Mouse.getEventX() * event.getGui().width / event.getGui().mc.displayWidth;
					int y = event.getGui().height - Mouse.getEventY() * event.getGui().height / event.getGui().mc.displayHeight - 1;
					GuiContainer guiContainer = (GuiContainer) event.getGui();
					RecipeClickableArea clickableArea = getRecipeClickableArea(guiContainer, x - guiContainer.getGuiLeft(), y - guiContainer.getGuiTop());
					if (clickableArea != null) {
						List<String> recipeCategoryUids = clickableArea.getRecipeCategoryUids();
						recipesGui.showCategories(recipeCategoryUids);
						event.setCanceled(true);
					}
				}
			}
		}
	}
}
