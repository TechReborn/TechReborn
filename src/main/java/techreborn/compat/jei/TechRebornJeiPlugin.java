package techreborn.compat.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
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
import techreborn.parts.powerCables.ItemCables;
import techreborn.world.TechRebornWorldGen;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JEIPlugin
public class TechRebornJeiPlugin extends BlankModPlugin {
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
		for (int i = 0; i < 10; i++) {
			int time = (int) Math.round(200 + Math.random() * 100);
			ImplosionCompressorRecipe recipe = new ImplosionCompressorRecipe(diamondBlock, diamondBlock, dirtBlock,
				dirtBlock, time, 120);
			debugRecipes.add(recipe);
		}
		registry.addRecipes(debugRecipes);
	}

	@Override
	public void register(
		@Nonnull
			IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidBerylium));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidCalcium));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidCalciumCarbonate));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidChlorite));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidDeuterium));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidGlyceryl));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidHelium));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidHelium3));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidHeliumplasma));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidHydrogen));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidLithium));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidMercury));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidMethane));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidNitrocoalfuel));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidNitrofuel));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidNitrogen));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidNitrogendioxide));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidPotassium));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidSilicon));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidSodium));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidSodiumpersulfate));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidTritium));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidWolframium));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidSulfur));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidSulfuricAcid));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidCarbon));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidCarbonFiber));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidNitroCarbon));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidSodiumSulfide));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidDiesel));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidNitroDiesel));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModFluids.BlockFluidOil));
		jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.missingRecipe));

		if(IC2Duplicates.deduplicate()){
			for(IC2Duplicates duplicate : IC2Duplicates.values()){
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
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.rubberSapling));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.rubberLog));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.rubberPlanks));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.rubberLeaves));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.treeTap));
				jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModItems.electricTreetap));
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

		for(EFluidGenerator type : EFluidGenerator.values())
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
		} catch (RuntimeException e) {
			Core.logHelper
				.error("Could not register rolling machine recipes. JEI may have changed its internal recipe wrapper locations.");
			e.printStackTrace();
		}

		if (mezz.jei.config.Config.isDebugModeEnabled()) {
			addDebugRecipes(registry);
		}

		registry.addDescription(ItemParts.getPartByName("rubberSap"),
			I18n.translateToLocal("techreborn.desc.rubberSap"));
		if (!ConfigTechReborn.ScrapboxDispenser) {
			registry.addDescription(new ItemStack(ModItems.scrapBox),
				I18n.translateToLocal("techreborn.desc.scrapBoxNoDispenser"));
		} else {
			registry.addDescription(new ItemStack(ModItems.scrapBox),
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

		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.compressor), RecipeCategoryUids.COMPRESSOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.alloyFurnace), RecipeCategoryUids.ALLOY_SMELTER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.alloySmelter), RecipeCategoryUids.ALLOY_SMELTER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.assemblyMachine),
			RecipeCategoryUids.ASSEMBLING_MACHINE);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blastFurnace), RecipeCategoryUids.BLAST_FURNACE);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.centrifuge), RecipeCategoryUids.CENTRIFUGE);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.chemicalReactor),
			RecipeCategoryUids.CHEMICAL_REACTOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.extractor), RecipeCategoryUids.EXTRACTOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.fusionControlComputer),
			RecipeCategoryUids.FUSION_REACTOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.grinder), RecipeCategoryUids.GRINDER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.implosionCompressor),
			RecipeCategoryUids.IMPLOSION_COMPRESSOR);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.industrialElectrolyzer),
			RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.industrialGrinder),
			RecipeCategoryUids.INDUSTRIAL_GRINDER);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.rollingMachine),
			RecipeCategoryUids.ROLLING_MACHINE);
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModItems.scrapBox), RecipeCategoryUids.SCRAPBOX);

		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.semiFluidGenerator),
				EFluidGenerator.SEMIFLUID.getRecipeID());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.gasTurbine),
				EFluidGenerator.GAS.getRecipeID());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.dieselGenerator),
				EFluidGenerator.DIESEL.getRecipeID());
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.thermalGenerator),
				EFluidGenerator.THERMAL.getRecipeID());

		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
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
		recipeTransferRegistry
			.addRecipeTransferHandler(ContainerGrinder.class, RecipeCategoryUids.GRINDER, 0, 1, 2, 36);
		recipeTransferRegistry
			.addRecipeTransferHandler(ContainerExtractor.class, RecipeCategoryUids.EXTRACTOR, 0, 1, 2, 36);
		recipeTransferRegistry
			.addRecipeTransferHandler(ContainerCompressor.class, RecipeCategoryUids.COMPRESSOR, 0, 1, 2, 36);

		if (CompatManager.isQuantumStorageLoaded) {
			registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.quantumChest));
			registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.quantumTank));
		}

	}
}
