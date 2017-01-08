package techreborn.compat.jei;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import reborncore.api.recipe.RecipeHandler;

import techreborn.Core;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.api.recipe.machines.ImplosionCompressorRecipe;
import techreborn.client.container.*;
import techreborn.client.gui.*;
import techreborn.compat.CompatManager;
import techreborn.compat.jei.alloySmelter.AlloySmelterRecipeCategory;
import techreborn.compat.jei.alloySmelter.AlloySmelterRecipeHandler;
import techreborn.compat.jei.assemblingMachine.AssemblingMachineRecipeCategory;
import techreborn.compat.jei.assemblingMachine.AssemblingMachineRecipeHandler;
import techreborn.compat.jei.blastFurnace.BlastFurnaceRecipeCategory;
import techreborn.compat.jei.blastFurnace.BlastFurnaceRecipeHandler;
import techreborn.compat.jei.centrifuge.CentrifugeRecipeCategory;
import techreborn.compat.jei.centrifuge.CentrifugeRecipeHandler;
import techreborn.compat.jei.chemicalReactor.ChemicalReactorRecipeCategory;
import techreborn.compat.jei.chemicalReactor.ChemicalReactorRecipeHandler;
import techreborn.compat.jei.compressor.CompressorRecipeCategory;
import techreborn.compat.jei.compressor.CompressorRecipeHandler;
import techreborn.compat.jei.extractor.ExtractorRecipeCategory;
import techreborn.compat.jei.extractor.ExtractorRecipeHandler;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeCategory;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeHandler;
import techreborn.compat.jei.generators.fluid.FluidGeneratorRecipeCategory;
import techreborn.compat.jei.generators.fluid.FluidGeneratorRecipeHandler;
import techreborn.compat.jei.grinder.GrinderRecipeCategory;
import techreborn.compat.jei.grinder.GrinderRecipeHandler;
import techreborn.compat.jei.implosionCompressor.ImplosionCompressorRecipeCategory;
import techreborn.compat.jei.implosionCompressor.ImplosionCompressorRecipeHandler;
import techreborn.compat.jei.industrialElectrolyzer.IndustrialElectrolyzerRecipeCategory;
import techreborn.compat.jei.industrialElectrolyzer.IndustrialElectrolyzerRecipeHandler;
import techreborn.compat.jei.industrialGrinder.IndustrialGrinderRecipeCategory;
import techreborn.compat.jei.industrialGrinder.IndustrialGrinderRecipeHandler;
import techreborn.compat.jei.rollingMachine.RollingMachineRecipeCategory;
import techreborn.compat.jei.rollingMachine.RollingMachineRecipeHandler;
import techreborn.compat.jei.rollingMachine.RollingMachineRecipeMaker;
import techreborn.compat.jei.scrapbox.ScrapboxRecipeCategory;
import techreborn.compat.jei.scrapbox.ScrapboxRecipeHandler;
import techreborn.compat.jei.vacuumFreezer.VacuumFreezerRecipeCategory;
import techreborn.compat.jei.vacuumFreezer.VacuumFreezerRecipeHandler;
import techreborn.config.ConfigTechReborn;
import techreborn.init.IC2Duplicates;
import techreborn.init.ModBlocks;
import techreborn.init.ModFluids;
import techreborn.init.ModItems;
import techreborn.items.ItemParts;
import techreborn.parts.TechRebornParts;
import techreborn.parts.powerCables.EnumCableType;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.config.Config;

@mezz.jei.api.JEIPlugin
public class TechRebornJeiPlugin extends BlankModPlugin {
	private static void addDebugRecipes(final IModRegistry registry) {
		final ItemStack diamondBlock = new ItemStack(Blocks.DIAMOND_BLOCK);
		final ItemStack dirtBlock = new ItemStack(Blocks.DIRT);
		final List<Object> debugRecipes = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final int time = (int) Math.round(200 + Math.random() * 100);
			final AssemblingMachineRecipe assemblingMachineRecipe = new AssemblingMachineRecipe(diamondBlock, diamondBlock,
					dirtBlock, time, 120);
			debugRecipes.add(assemblingMachineRecipe);
		}
		for (int i = 0; i < 10; i++) {
			final int time = (int) Math.round(200 + Math.random() * 100);
			final ImplosionCompressorRecipe recipe = new ImplosionCompressorRecipe(diamondBlock, diamondBlock, dirtBlock,
					dirtBlock, time, 120);
			debugRecipes.add(recipe);
		}
		registry.addRecipes(debugRecipes);
	}

	@Override
	public void register(
			@Nonnull final
			IModRegistry registry) {
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_BERYLLIUM));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_CALCIUM));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_CALCIUM_CARBONATE));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_CHLORITE));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_DEUTERIUM));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_GLYCERYL));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_HELIUM));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_HELIUM_3));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_HELIUMPLASMA));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_HYDROGEN));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_LITHIUM));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_MERCURY));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_METHANE));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_NITROCOAL_FUEL));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_NITROFUEL));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_NITROGEN));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_NITROGENDIOXIDE));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_POTASSIUM));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_SILICON));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_SODIUM));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_SODIUMPERSULFATE));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_TRITIUM));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_WOLFRAMIUM));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_SULFUR));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_SULFURIC_ACID));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_CARBON));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_CARBON_FIBER));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_NITRO_CARBON));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_SODIUM_SULFIDE));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_DIESEL));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_NITRO_DIESEL));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BLOCK_OIL));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.MISSING_RECIPE_PLACEHOLDER));

		if(IC2Duplicates.deduplicate()){
			for(final IC2Duplicates duplicate : IC2Duplicates.values()){
				if(duplicate.hasIC2Stack()){
					jeiHelpers.getItemBlacklist().addItemToBlacklist(duplicate.getTrStack());
				}
			}
			if(TechRebornParts.cables != null){
				for (int i = 0; i < EnumCableType.values().length; i++) {
					jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(TechRebornParts.cables, 1, i));
				}
			}
			jeiHelpers.getItemBlacklist().addItemToBlacklist(ItemParts.getPartByName("rubber"));
			jeiHelpers.getItemBlacklist().addItemToBlacklist(ItemParts.getPartByName("rubberSap"));
			jeiHelpers.getItemBlacklist().addItemToBlacklist(ItemParts.getPartByName("electronicCircuit"));
			jeiHelpers.getItemBlacklist().addItemToBlacklist(ItemParts.getPartByName("advancedCircuit"));
			if(!Core.worldGen.config.rubberTreeConfig.shouldSpawn){
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.RUBBER_SAPLING));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.RUBBER_LOG));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.RUBBER_PLANKS));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.RUBBER_LEAVES));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.TREE_TAP));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.ELECTRIC_TREE_TAP));
			}
		}

		registry.addRecipeCategories(new AlloySmelterRecipeCategory(guiHelper),
				new AssemblingMachineRecipeCategory(guiHelper), new BlastFurnaceRecipeCategory(guiHelper),
				new CentrifugeRecipeCategory(guiHelper), new ChemicalReactorRecipeCategory(guiHelper),
				new FusionReactorRecipeCategory(guiHelper), new IndustrialGrinderRecipeCategory(guiHelper),
				new ImplosionCompressorRecipeCategory(guiHelper), new IndustrialElectrolyzerRecipeCategory(guiHelper),
				new RollingMachineRecipeCategory(guiHelper), new VacuumFreezerRecipeCategory(guiHelper),
				new GrinderRecipeCategory(guiHelper), new ExtractorRecipeCategory(guiHelper),
				new CompressorRecipeCategory(guiHelper), new ScrapboxRecipeCategory(guiHelper));

		for(final EFluidGenerator type : EFluidGenerator.values())
			registry.addRecipeCategories(new FluidGeneratorRecipeCategory(type, guiHelper));

		registry.addRecipeHandlers(new AlloySmelterRecipeHandler(jeiHelpers),
				new AssemblingMachineRecipeHandler(jeiHelpers), new BlastFurnaceRecipeHandler(jeiHelpers),
				new CentrifugeRecipeHandler(jeiHelpers), new ChemicalReactorRecipeHandler(jeiHelpers),
				new FusionReactorRecipeHandler(), new IndustrialGrinderRecipeHandler(jeiHelpers),
				new ImplosionCompressorRecipeHandler(jeiHelpers), new IndustrialElectrolyzerRecipeHandler(jeiHelpers),
				new RollingMachineRecipeHandler(), new VacuumFreezerRecipeHandler(jeiHelpers),
				new GrinderRecipeHandler(jeiHelpers), new ExtractorRecipeHandler(jeiHelpers),
				new CompressorRecipeHandler(jeiHelpers), new ScrapboxRecipeHandler(jeiHelpers),
				new FluidGeneratorRecipeHandler(jeiHelpers));

		registry.addRecipes(RecipeHandler.recipeList);
		registry.addRecipes(FusionReactorRecipeHelper.reactorRecipes);

		GeneratorRecipeHelper.fluidRecipes.forEach((type, list) -> registry.addRecipes(list.getRecipes()));

		try {
			registry.addRecipes(RollingMachineRecipeMaker.getRecipes(jeiHelpers));
		} catch (final RuntimeException e) {
			Core.logHelper
			.error("Could not register rolling machine recipes. JEI may have changed its internal recipe wrapper locations.");
			e.printStackTrace();
		}

		if (Config.isDebugModeEnabled()) {
			TechRebornJeiPlugin.addDebugRecipes(registry);
		}

		registry.addDescription(ItemParts.getPartByName("rubberSap"),
				I18n.translateToLocal("techreborn.desc.rubberSap"));
		if (!ConfigTechReborn.ScrapboxDispenser) {
			registry.addDescription(new ItemStack(ModItems.SCRAP_BOX),
					I18n.translateToLocal("techreborn.desc.scrapBoxNoDispenser"));
		} else {
			registry.addDescription(new ItemStack(ModItems.SCRAP_BOX),
					I18n.translateToLocal("techreborn.desc.scrapBox"));
		}

		registry.addRecipeClickArea(GuiAlloyFurnace.class, 80, 35, 26, 20, RecipeCategoryUids.ALLOY_SMELTER,
				VanillaRecipeCategoryUid.FUEL);
		registry.addRecipeClickArea(GuiAlloySmelter.class, 80, 35, 26, 20, RecipeCategoryUids.ALLOY_SMELTER);
		registry.addRecipeClickArea(GuiAssemblingMachine.class, 85, 34, 24, 20, RecipeCategoryUids.ASSEMBLING_MACHINE);
		registry.addRecipeClickArea(GuiBlastFurnace.class, 63, 36, 24, 15, RecipeCategoryUids.BLAST_FURNACE);
		registry.addRecipeClickArea(GuiCentrifuge.class, 98, 37, 9, 12, RecipeCategoryUids.CENTRIFUGE);
		registry.addRecipeClickArea(GuiCentrifuge.class, 68, 37, 9, 12, RecipeCategoryUids.CENTRIFUGE);
		registry.addRecipeClickArea(GuiCentrifuge.class, 83, 23, 12, 9, RecipeCategoryUids.CENTRIFUGE);
		registry.addRecipeClickArea(GuiCentrifuge.class, 83, 53, 12, 9, RecipeCategoryUids.CENTRIFUGE);
		registry.addRecipeClickArea(GuiChemicalReactor.class, 73, 39, 32, 12, RecipeCategoryUids.CHEMICAL_REACTOR);
		registry.addRecipeClickArea(GuiFusionReactor.class, 111, 34, 27, 19, RecipeCategoryUids.FUSION_REACTOR);
		registry.addRecipeClickArea(GuiIndustrialGrinder.class, 50, 35, 25, 16, RecipeCategoryUids.INDUSTRIAL_GRINDER);
		registry.addRecipeClickArea(GuiImplosionCompressor.class, 60, 37, 24, 15,
				RecipeCategoryUids.IMPLOSION_COMPRESSOR);
		registry.addRecipeClickArea(GuiIndustrialElectrolyzer.class, 72, 37, 33, 14,
				RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER);
		registry.addRecipeClickArea(GuiRollingMachine.class, 89, 32, 26, 25, RecipeCategoryUids.ROLLING_MACHINE);
		registry.addRecipeClickArea(GuiVacuumFreezer.class, 78, 36, 24, 16, RecipeCategoryUids.VACUUM_FREEZER);
		registry.addRecipeClickArea(GuiGrinder.class, 78, 36, 24, 16, RecipeCategoryUids.GRINDER);
		registry.addRecipeClickArea(GuiExtractor.class, 78, 36, 24, 16, RecipeCategoryUids.EXTRACTOR);
		registry.addRecipeClickArea(GuiCompressor.class, 78, 36, 24, 16, RecipeCategoryUids.COMPRESSOR);
		registry.addRecipeClickArea(GuiIronFurnace.class, 78, 36, 24, 16, VanillaRecipeCategoryUid.SMELTING,
				VanillaRecipeCategoryUid.FUEL);
		registry.addRecipeClickArea(GuiElectricFurnace.class, 78, 36, 24, 16, VanillaRecipeCategoryUid.SMELTING);

		registry.addRecipeClickArea(GuiSemifluidGenerator.class, 79, 34, 18, 18,
				EFluidGenerator.SEMIFLUID.getRecipeID());
		registry.addRecipeClickArea(GuiDieselGenerator.class, 79, 34, 18, 18,
				EFluidGenerator.DIESEL.getRecipeID());
		registry.addRecipeClickArea(GuiGasTurbine.class, 79, 34, 18, 18,
				EFluidGenerator.GAS.getRecipeID());
		registry.addRecipeClickArea(GuiThermalGenerator.class, 79, 34, 18, 18,
				EFluidGenerator.THERMAL.getRecipeID());

		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.COMPRESSOR), RecipeCategoryUids.COMPRESSOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.IRON_ALLOY_FURNACE), RecipeCategoryUids.ALLOY_SMELTER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.ALLOY_SMELTER), RecipeCategoryUids.ALLOY_SMELTER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.ASSEMBLY_MACHINE),
				RecipeCategoryUids.ASSEMBLING_MACHINE);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.INDUSTRIAL_BLAST_FURNACE), RecipeCategoryUids.BLAST_FURNACE);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), RecipeCategoryUids.CENTRIFUGE);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.CHEMICAL_REACTOR),
				RecipeCategoryUids.CHEMICAL_REACTOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.EXTRACTOR), RecipeCategoryUids.EXTRACTOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.FUSION_CONTROL_COMPUTER),
				RecipeCategoryUids.FUSION_REACTOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.GRINDER), RecipeCategoryUids.GRINDER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.IMPLOSION_COMPRESSOR),
				RecipeCategoryUids.IMPLOSION_COMPRESSOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.INDUSTRIAL_ELECTROLYZER),
				RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.INDUSTRIAL_GRINDER),
				RecipeCategoryUids.INDUSTRIAL_GRINDER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.ROLLING_MACHINE),
				RecipeCategoryUids.ROLLING_MACHINE);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModItems.SCRAP_BOX), RecipeCategoryUids.SCRAPBOX);

		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.SEMIFLUID_GENERATOR),
				EFluidGenerator.SEMIFLUID.getRecipeID());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.GAS_TURBINE),
				EFluidGenerator.GAS.getRecipeID());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.DIESEL_GENERATOR),
				EFluidGenerator.DIESEL.getRecipeID());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.THERMAL_GENERATOR),
				EFluidGenerator.THERMAL.getRecipeID());

		final IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerAlloyFurnace.class, RecipeCategoryUids.ALLOY_SMELTER, 0, 2, 4, 36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerAlloySmelter.class, RecipeCategoryUids.ALLOY_SMELTER, 0, 2, 8, 36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerAlloyFurnace.class, VanillaRecipeCategoryUid.FUEL, 3, 1, 4, 36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerAssemblingMachine.class, RecipeCategoryUids.ASSEMBLING_MACHINE, 0, 2,
				8, 36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerBlastFurnace.class, RecipeCategoryUids.BLAST_FURNACE, 0, 2, 4, 36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerCentrifuge.class, RecipeCategoryUids.CENTRIFUGE, 0, 2, 11, 36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerChemicalReactor.class, RecipeCategoryUids.CHEMICAL_REACTOR, 0, 2, 8,
				36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerFusionReactor.class, RecipeCategoryUids.FUSION_REACTOR, 0, 2, 3, 36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerIndustrialGrinder.class, RecipeCategoryUids.GRINDER, 0, 2, 6, 36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerImplosionCompressor.class, RecipeCategoryUids.IMPLOSION_COMPRESSOR,
				0, 2, 4, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerIndustrialElectrolyzer.class,
				RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER, 0, 2, 7, 36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerRollingMachine.class, RecipeCategoryUids.ROLLING_MACHINE, 0, 9, 11,
				36);
		recipeTransferRegistry
		.addRecipeTransferHandler(ContainerVacuumFreezer.class, RecipeCategoryUids.VACUUM_FREEZER, 0, 1, 2, 36);

		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("grinder", RecipeCategoryUids.GRINDER, 36, 1, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("extractor", RecipeCategoryUids.EXTRACTOR, 36, 1, 0, 36));
		recipeTransferRegistry.addRecipeTransferHandler(
				new BuiltContainerTransferInfo("compressor", RecipeCategoryUids.COMPRESSOR, 36, 1, 0, 36));

		if (CompatManager.isQuantumStorageLoaded) {
			registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.QUANTUM_CHEST));
			registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.QUANTUM_TANK));
		}

	}
}
