package techreborn.client.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.fabricmc.fabric.impl.recipe.sync.SynchronizedRecipesImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import reborncore.client.gui.GuiBase;
import reborncore.common.crafting.RebornRecipe;
import techreborn.client.compat.jei.gui.handler.GuiBaseExtraAreaHandler;
import techreborn.client.compat.jei.gui.render.RecipeClickAreaRenderable;
import techreborn.client.compat.jei.recipe.category.*;
import techreborn.client.compat.jei.recipe.transfer.BuiltScreenHandlerTransferInfo;
import techreborn.client.compat.jei.subtype.EnergyItemSubtypeInterpreter;
import techreborn.client.compat.jei.subtype.FluidItemSubtypeInterpreter;
import techreborn.client.gui.*;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;
import techreborn.recipe.recipes.*;

import java.util.List;
import java.util.stream.IntStream;

public class JEIPlugin implements IModPlugin {
	public static final Identifier UID = Identifier.parse("techreborn:techreborn");

	public static SynchronizedRecipes recipeMap = SynchronizedRecipesImpl.EMPTY;

	public static IJeiHelpers jeiHelpers;
	public static IJeiRuntime jeiRuntime;

	public static final IRecipeHolderType<RebornRecipe> ALLOY_SMELTER = IRecipeHolderType.create(ModRecipes.ALLOY_SMELTER);
	public static final IRecipeHolderType<AssemblingMachineRecipe> ASSEMBLING_MACHINE = IRecipeHolderType.create(ModRecipes.ASSEMBLING_MACHINE);
	public static final IRecipeHolderType<BlastFurnaceRecipe> BLAST_FURNACE = IRecipeHolderType.create(ModRecipes.BLAST_FURNACE);
	public static final IRecipeHolderType<CentrifugeRecipe> CENTRIFUGE = IRecipeHolderType.create(ModRecipes.CENTRIFUGE);
	public static final IRecipeHolderType<RebornRecipe> CHEMICAL_REACTOR = IRecipeHolderType.create(ModRecipes.CHEMICAL_REACTOR);
	public static final IRecipeHolderType<RebornRecipe> COMPRESSOR = IRecipeHolderType.create(ModRecipes.COMPRESSOR);
	public static final IRecipeHolderType<RebornRecipe> DISTILLATION_TOWER = IRecipeHolderType.create(ModRecipes.DISTILLATION_TOWER);
	public static final IRecipeHolderType<RebornRecipe> EXTRACTOR = IRecipeHolderType.create(ModRecipes.EXTRACTOR);
	public static final IRecipeHolderType<FluidReplicatorRecipe> FLUID_REPLICATOR = IRecipeHolderType.create(ModRecipes.FLUID_REPLICATOR);
	public static final IRecipeHolderType<FusionReactorRecipe> FUSION_REACTOR = IRecipeHolderType.create(ModRecipes.FUSION_REACTOR);
	public static final IRecipeHolderType<RebornRecipe> GRINDER = IRecipeHolderType.create(ModRecipes.GRINDER);
	public static final IRecipeHolderType<RebornRecipe> IMPLOSION_COMPRESSOR = IRecipeHolderType.create(ModRecipes.IMPLOSION_COMPRESSOR);
	public static final IRecipeHolderType<RebornRecipe> INDUSTRIAL_ELECTROLYZER = IRecipeHolderType.create(ModRecipes.INDUSTRIAL_ELECTROLYZER);
	public static final IRecipeHolderType<IndustrialGrinderRecipe> INDUSTRIAL_GRINDER = IRecipeHolderType.create(ModRecipes.INDUSTRIAL_GRINDER);
	public static final IRecipeHolderType<IndustrialSawmillRecipe> INDUSTRIAL_SAWMILL = IRecipeHolderType.create(ModRecipes.INDUSTRIAL_SAWMILL);
	public static final IRecipeHolderType<RollingMachineRecipe> ROLLING_MACHINE = IRecipeHolderType.create(ModRecipes.ROLLING_MACHINE);
	public static final IRecipeHolderType<ScrapBoxRecipe> SCRAPBOX = IRecipeHolderType.create(ModRecipes.SCRAPBOX);
	public static final IRecipeHolderType<RebornRecipe> SOLID_CANNING_MACHINE = IRecipeHolderType.create(ModRecipes.SOLID_CANNING_MACHINE);
	public static final IRecipeHolderType<RebornRecipe> VACUUM_FREEZER = IRecipeHolderType.create(ModRecipes.VACUUM_FREEZER);
	public static final IRecipeHolderType<RebornRecipe> WIRE_MILL = IRecipeHolderType.create(ModRecipes.WIRE_MILL);

	public static final IRecipeHolderType<FluidGeneratorRecipe> THERMAL_GENERATOR = IRecipeHolderType.create(ModRecipes.THERMAL_GENERATOR);
	public static final IRecipeHolderType<FluidGeneratorRecipe> GAS_GENERATOR = IRecipeHolderType.create(ModRecipes.GAS_GENERATOR);
	public static final IRecipeHolderType<FluidGeneratorRecipe> DIESEL_GENERATOR = IRecipeHolderType.create(ModRecipes.DIESEL_GENERATOR);
	public static final IRecipeHolderType<FluidGeneratorRecipe> SEMI_FLUID_GENERATOR = IRecipeHolderType.create(ModRecipes.SEMI_FLUID_GENERATOR);
	public static final IRecipeHolderType<FluidGeneratorRecipe> PLASMA_GENERATOR = IRecipeHolderType.create(ModRecipes.PLASMA_GENERATOR);

	public JEIPlugin() {
		ClientRecipeSynchronizedEvent.EVENT.register((client, recipes) -> recipeMap = recipes);
	}

	@Override
	public Identifier getPluginUid() {
		return UID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(TRContent.CELL, new FluidItemSubtypeInterpreter());

		EnergyItemSubtypeInterpreter energy = new EnergyItemSubtypeInterpreter();
		registration.registerSubtypeInterpreter(TRContent.QUANTUM_HELMET, energy);
		registration.registerSubtypeInterpreter(TRContent.QUANTUM_CHESTPLATE, energy);
		registration.registerSubtypeInterpreter(TRContent.QUANTUM_LEGGINGS, energy);
		registration.registerSubtypeInterpreter(TRContent.QUANTUM_BOOTS, energy);
		registration.registerSubtypeInterpreter(TRContent.RED_CELL_BATTERY, energy);
		registration.registerSubtypeInterpreter(TRContent.LITHIUM_ION_BATTERY, energy);
		registration.registerSubtypeInterpreter(TRContent.LITHIUM_ION_BATPACK, energy);
		registration.registerSubtypeInterpreter(TRContent.ENERGY_CRYSTAL, energy);
		registration.registerSubtypeInterpreter(TRContent.LAPOTRON_CRYSTAL, energy);
		registration.registerSubtypeInterpreter(TRContent.LAPOTRONIC_ORB, energy);
		registration.registerSubtypeInterpreter(TRContent.LAPOTRONIC_ORBPACK, energy);
		registration.registerSubtypeInterpreter(TRContent.BASIC_DRILL, energy);
		registration.registerSubtypeInterpreter(TRContent.BASIC_CHAINSAW, energy);
		registration.registerSubtypeInterpreter(TRContent.BASIC_JACKHAMMER, energy);
		registration.registerSubtypeInterpreter(TRContent.ELECTRIC_TREE_TAP, energy);
		registration.registerSubtypeInterpreter(TRContent.ADVANCED_DRILL, energy);
		registration.registerSubtypeInterpreter(TRContent.ADVANCED_CHAINSAW, energy);
		registration.registerSubtypeInterpreter(TRContent.ADVANCED_JACKHAMMER, energy);
		registration.registerSubtypeInterpreter(TRContent.ROCK_CUTTER, energy);
		registration.registerSubtypeInterpreter(TRContent.INDUSTRIAL_DRILL, energy);
		registration.registerSubtypeInterpreter(TRContent.INDUSTRIAL_CHAINSAW, energy);
		registration.registerSubtypeInterpreter(TRContent.INDUSTRIAL_JACKHAMMER, energy);
		registration.registerSubtypeInterpreter(TRContent.NANOSABER, energy);
		registration.registerSubtypeInterpreter(TRContent.OMNI_TOOL, energy);
		registration.registerSubtypeInterpreter(TRContent.CLOAKING_DEVICE, energy);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		jeiHelpers = registration.getJeiHelpers();

		registration.addRecipeCategories(new TwoItemToItemCenterRecipeCategory<>(ALLOY_SMELTER));
		registration.addRecipeCategories(new TwoItemToItemRecipeCategory<>(ASSEMBLING_MACHINE));
		registration.addRecipeCategories(new TwoItemToTwoItemRecipeCategory<>(BLAST_FURNACE));
		registration.addRecipeCategories(new TwoItemToFourItemCircleRecipeCategory<>(CENTRIFUGE));
		registration.addRecipeCategories(new TwoItemToItemCenterRecipeCategory<>(CHEMICAL_REACTOR));
		registration.addRecipeCategories(new ItemToItemRecipeCategory<>(COMPRESSOR));
		registration.addRecipeCategories(new TwoItemToThreeItemRecipeCategory<>(DISTILLATION_TOWER));
		registration.addRecipeCategories(new ItemToItemRecipeCategory<>(EXTRACTOR));
		registration.addRecipeCategories(new ItemToFluidRecipeCategory<>(FLUID_REPLICATOR));
		registration.addRecipeCategories(new FusionReactorRecipeCategory(FUSION_REACTOR));
		registration.addRecipeCategories(new ItemToItemRecipeCategory<>(GRINDER));
		registration.addRecipeCategories(new TwoItemToTwoItemRecipeCategory<>(IMPLOSION_COMPRESSOR));
		registration.addRecipeCategories(new TwoItemToFourItemRecipeCategory<>(INDUSTRIAL_ELECTROLYZER));
		registration.addRecipeCategories(new ItemFluidToFourItemRecipeCategory<>(INDUSTRIAL_GRINDER));
		registration.addRecipeCategories(new ItemFluidToThreeItemRecipeCategory<>(INDUSTRIAL_SAWMILL));
		registration.addRecipeCategories(new RollingMachineRecipeCategory(ROLLING_MACHINE));
		registration.addRecipeCategories(new ItemToItemRecipeCategory<>(SCRAPBOX));
		registration.addRecipeCategories(new TwoItemToItemCenterRecipeCategory<>(SOLID_CANNING_MACHINE));
		registration.addRecipeCategories(new ItemToItemRecipeCategory<>(VACUUM_FREEZER));
		registration.addRecipeCategories(new ItemToItemRecipeCategory<>(WIRE_MILL));

		registration.addRecipeCategories(new FluidGeneratorRecipeCategory(THERMAL_GENERATOR));
		registration.addRecipeCategories(new FluidGeneratorRecipeCategory(GAS_GENERATOR, Component.translatable("techreborn:gas_turbine")));
		registration.addRecipeCategories(new FluidGeneratorRecipeCategory(DIESEL_GENERATOR));
		registration.addRecipeCategories(new FluidGeneratorRecipeCategory(SEMI_FLUID_GENERATOR));
		registration.addRecipeCategories(new FluidGeneratorRecipeCategory(PLASMA_GENERATOR));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(ALLOY_SMELTER, List.copyOf(recipeMap.getAllOfType(ModRecipes.ALLOY_SMELTER)));
		registration.addRecipes(ASSEMBLING_MACHINE, List.copyOf(recipeMap.getAllOfType(ModRecipes.ASSEMBLING_MACHINE)));
		registration.addRecipes(BLAST_FURNACE, List.copyOf(recipeMap.getAllOfType(ModRecipes.BLAST_FURNACE)));
		registration.addRecipes(CENTRIFUGE, List.copyOf(recipeMap.getAllOfType(ModRecipes.CENTRIFUGE)));
		registration.addRecipes(CHEMICAL_REACTOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.CHEMICAL_REACTOR)));
		registration.addRecipes(COMPRESSOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.COMPRESSOR)));
		registration.addRecipes(DISTILLATION_TOWER, List.copyOf(recipeMap.getAllOfType(ModRecipes.DISTILLATION_TOWER)));
		registration.addRecipes(EXTRACTOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.EXTRACTOR)));
		registration.addRecipes(FLUID_REPLICATOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.FLUID_REPLICATOR)));
		registration.addRecipes(FUSION_REACTOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.FUSION_REACTOR)));
		registration.addRecipes(GRINDER, List.copyOf(recipeMap.getAllOfType(ModRecipes.GRINDER)));
		registration.addRecipes(IMPLOSION_COMPRESSOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.IMPLOSION_COMPRESSOR)));
		registration.addRecipes(INDUSTRIAL_ELECTROLYZER, List.copyOf(recipeMap.getAllOfType(ModRecipes.INDUSTRIAL_ELECTROLYZER)));
		registration.addRecipes(INDUSTRIAL_GRINDER, List.copyOf(recipeMap.getAllOfType(ModRecipes.INDUSTRIAL_GRINDER)));
		registration.addRecipes(INDUSTRIAL_SAWMILL, List.copyOf(recipeMap.getAllOfType(ModRecipes.INDUSTRIAL_SAWMILL)));
		registration.addRecipes(ROLLING_MACHINE, List.copyOf(recipeMap.getAllOfType(ModRecipes.ROLLING_MACHINE)));
		registration.addRecipes(SCRAPBOX, List.copyOf(recipeMap.getAllOfType(ModRecipes.SCRAPBOX)));
		registration.addRecipes(SOLID_CANNING_MACHINE, List.copyOf(recipeMap.getAllOfType(ModRecipes.SOLID_CANNING_MACHINE)));
		registration.addRecipes(VACUUM_FREEZER, List.copyOf(recipeMap.getAllOfType(ModRecipes.VACUUM_FREEZER)));
		registration.addRecipes(WIRE_MILL, List.copyOf(recipeMap.getAllOfType(ModRecipes.WIRE_MILL)));

		registration.addRecipes(THERMAL_GENERATOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.THERMAL_GENERATOR)));
		registration.addRecipes(GAS_GENERATOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.GAS_GENERATOR)));
		registration.addRecipes(DIESEL_GENERATOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.DIESEL_GENERATOR)));
		registration.addRecipes(SEMI_FLUID_GENERATOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.SEMI_FLUID_GENERATOR)));
		registration.addRecipes(PLASMA_GENERATOR, List.copyOf(recipeMap.getAllOfType(ModRecipes.PLASMA_GENERATOR)));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(new BuiltScreenHandlerTransferInfo<>("rollingmachine", ROLLING_MACHINE, IntStream.range(0, 9)));
		registration.addRecipeTransferHandler(new BuiltScreenHandlerTransferInfo<>("autocraftingtable", RecipeTypes.CRAFTING, IntStream.range(0, 9)));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(ALLOY_SMELTER, TRContent.Machine.IRON_ALLOY_FURNACE);
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, TRContent.Machine.IRON_ALLOY_FURNACE);
		registration.addCraftingStation(ALLOY_SMELTER, TRContent.Machine.ALLOY_SMELTER);
		registration.addCraftingStation(ASSEMBLING_MACHINE, TRContent.Machine.ASSEMBLY_MACHINE);
		registration.addCraftingStation(BLAST_FURNACE, TRContent.Machine.INDUSTRIAL_BLAST_FURNACE);
		registration.addCraftingStation(CENTRIFUGE, TRContent.Machine.INDUSTRIAL_CENTRIFUGE);
		registration.addCraftingStation(CHEMICAL_REACTOR, TRContent.Machine.CHEMICAL_REACTOR);
		registration.addCraftingStation(COMPRESSOR, TRContent.Machine.COMPRESSOR);
		registration.addCraftingStation(DISTILLATION_TOWER, TRContent.Machine.DISTILLATION_TOWER);
		registration.addCraftingStation(EXTRACTOR, TRContent.Machine.EXTRACTOR);
		registration.addCraftingStation(FLUID_REPLICATOR, TRContent.Machine.FLUID_REPLICATOR);
		registration.addCraftingStation(FUSION_REACTOR, TRContent.Machine.FUSION_CONTROL_COMPUTER);
		registration.addCraftingStation(GRINDER, TRContent.Machine.GRINDER);
		registration.addCraftingStation(IMPLOSION_COMPRESSOR, TRContent.Machine.IMPLOSION_COMPRESSOR);
		registration.addCraftingStation(INDUSTRIAL_ELECTROLYZER, TRContent.Machine.INDUSTRIAL_ELECTROLYZER);
		registration.addCraftingStation(INDUSTRIAL_GRINDER, TRContent.Machine.INDUSTRIAL_GRINDER);
		registration.addCraftingStation(INDUSTRIAL_SAWMILL, TRContent.Machine.INDUSTRIAL_SAWMILL);
		registration.addCraftingStation(ROLLING_MACHINE, TRContent.Machine.ROLLING_MACHINE);
		registration.addCraftingStation(SCRAPBOX, TRContent.SCRAP_BOX);
		registration.addCraftingStation(SCRAPBOX, TRContent.Machine.SCRAPBOXINATOR);
		registration.addCraftingStation(SOLID_CANNING_MACHINE, TRContent.Machine.SOLID_CANNING_MACHINE);
		registration.addCraftingStation(VACUUM_FREEZER, TRContent.Machine.VACUUM_FREEZER);
		registration.addCraftingStation(WIRE_MILL, TRContent.Machine.WIRE_MILL);

		registration.addCraftingStation(THERMAL_GENERATOR, TRContent.Machine.THERMAL_GENERATOR);
		registration.addCraftingStation(GAS_GENERATOR, TRContent.Machine.GAS_TURBINE);
		registration.addCraftingStation(DIESEL_GENERATOR, TRContent.Machine.DIESEL_GENERATOR);
		registration.addCraftingStation(SEMI_FLUID_GENERATOR, TRContent.Machine.SEMI_FLUID_GENERATOR);
		registration.addCraftingStation(PLASMA_GENERATOR, TRContent.Machine.PLASMA_GENERATOR);

		registration.addCraftingStation(RecipeTypes.CRAFTING, TRContent.Machine.AUTO_CRAFTING_TABLE);
		registration.addCraftingStation(RecipeTypes.SMELTING, TRContent.Machine.IRON_FURNACE);
		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, TRContent.Machine.IRON_FURNACE);
		registration.addCraftingStation(RecipeTypes.SMELTING, TRContent.Machine.ELECTRIC_FURNACE);

		registration.addCraftingStation(RecipeTypes.SMELTING_FUEL, TRContent.Machine.SOLID_FUEL_GENERATOR);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(GuiAlloyFurnace.class, 158, 5, 12, 12, ALLOY_SMELTER, RecipeTypes.SMELTING_FUEL);
		registration.addRecipeClickArea(GuiAlloySmelter.class, 158, 5, 12, 12, ALLOY_SMELTER);
		registration.addRecipeClickArea(GuiAssemblingMachine.class, 158, 5, 12, 12, ASSEMBLING_MACHINE);
		registration.addRecipeClickArea(GuiBlastFurnace.class, 158, 5, 12, 12, BLAST_FURNACE);
		registration.addRecipeClickArea(GuiCentrifuge.class, 158, 5, 12, 12, CENTRIFUGE);
		registration.addRecipeClickArea(GuiChemicalReactor.class, 158, 5, 12, 12, CHEMICAL_REACTOR);
		registration.addRecipeClickArea(GuiCompressor.class, 158, 5, 12, 12, COMPRESSOR);
		registration.addRecipeClickArea(GuiDistillationTower.class, 158, 5, 12, 12, DISTILLATION_TOWER);
		registration.addRecipeClickArea(GuiExtractor.class, 158, 5, 12, 12, EXTRACTOR);
		registration.addRecipeClickArea(GuiFluidReplicator.class, 158, 5, 12, 12, FLUID_REPLICATOR);
		registration.addRecipeClickArea(GuiFusionReactor.class, 158, 5, 12, 12, FUSION_REACTOR);
		registration.addRecipeClickArea(GuiGrinder.class, 158, 5, 12, 12, GRINDER);
		registration.addRecipeClickArea(GuiImplosionCompressor.class, 158, 5, 12, 12, IMPLOSION_COMPRESSOR);
		registration.addRecipeClickArea(GuiIndustrialElectrolyzer.class, 158, 5, 12, 12, INDUSTRIAL_ELECTROLYZER);
		registration.addRecipeClickArea(GuiIndustrialGrinder.class, 158, 5, 12, 12, INDUSTRIAL_GRINDER);
		registration.addRecipeClickArea(GuiIndustrialSawmill.class, 158, 5, 12, 12, INDUSTRIAL_SAWMILL);
		registration.addRecipeClickArea(GuiRollingMachine.class, 158, 5, 12, 12, ROLLING_MACHINE);
		registration.addRecipeClickArea(GuiScrapboxinator.class, 158, 5, 12, 12, SCRAPBOX);
		registration.addRecipeClickArea(GuiSolidCanningMachine.class, 158, 5, 12, 12, SOLID_CANNING_MACHINE);
		registration.addRecipeClickArea(GuiVacuumFreezer.class, 158, 5, 12, 12, VACUUM_FREEZER);
		registration.addRecipeClickArea(GuiWireMill.class, 158, 5, 12, 12, WIRE_MILL);

		registration.addRecipeClickArea(GuiThermalGenerator.class, 158, 5, 12, 12, THERMAL_GENERATOR);
		registration.addRecipeClickArea(GuiGasTurbine.class, 158, 5, 12, 12, GAS_GENERATOR);
		registration.addRecipeClickArea(GuiDieselGenerator.class, 158, 5, 12, 12, DIESEL_GENERATOR);
		registration.addRecipeClickArea(GuiSemifluidGenerator.class, 158, 5, 12, 12, SEMI_FLUID_GENERATOR);
		registration.addRecipeClickArea(GuiPlasmaGenerator.class, 158, 5, 12, 12, PLASMA_GENERATOR);

		registration.addRecipeClickArea(GuiAutoCrafting.class, 158, 18, 12, 12, RecipeTypes.CRAFTING);
		registration.addRecipeClickArea(GuiIronFurnace.class, 158, 5, 12, 12, RecipeTypes.SMELTING, RecipeTypes.SMELTING_FUEL);
		registration.addRecipeClickArea(GuiElectricFurnace.class, 158, 5, 12, 12, RecipeTypes.SMELTING);

		registration.addRecipeClickArea(GuiGenerator.class, 158, 5, 12, 12, RecipeTypes.SMELTING_FUEL);

		registration.addGenericGuiContainerHandler(GuiBase.class, new GuiBaseExtraAreaHandler());

		RecipeClickAreaRenderable.clearEntries();

		RecipeClickAreaRenderable.addEntry(GuiAlloyFurnace.class);
		RecipeClickAreaRenderable.addEntry(GuiAlloySmelter.class);
		RecipeClickAreaRenderable.addEntry(GuiAssemblingMachine.class);
		RecipeClickAreaRenderable.addEntry(GuiBlastFurnace.class);
		RecipeClickAreaRenderable.addEntry(GuiCentrifuge.class);
		RecipeClickAreaRenderable.addEntry(GuiChemicalReactor.class);
		RecipeClickAreaRenderable.addEntry(GuiCompressor.class);
		RecipeClickAreaRenderable.addEntry(GuiDistillationTower.class);
		RecipeClickAreaRenderable.addEntry(GuiExtractor.class);
		RecipeClickAreaRenderable.addEntry(GuiFluidReplicator.class);
		RecipeClickAreaRenderable.addEntry(GuiFusionReactor.class);
		RecipeClickAreaRenderable.addEntry(GuiGrinder.class);
		RecipeClickAreaRenderable.addEntry(GuiImplosionCompressor.class);
		RecipeClickAreaRenderable.addEntry(GuiIndustrialElectrolyzer.class);
		RecipeClickAreaRenderable.addEntry(GuiIndustrialGrinder.class);
		RecipeClickAreaRenderable.addEntry(GuiIndustrialSawmill.class);
		RecipeClickAreaRenderable.addEntry(GuiRollingMachine.class);
		RecipeClickAreaRenderable.addEntry(GuiScrapboxinator.class);
		RecipeClickAreaRenderable.addEntry(GuiSolidCanningMachine.class);
		RecipeClickAreaRenderable.addEntry(GuiVacuumFreezer.class);
		RecipeClickAreaRenderable.addEntry(GuiWireMill.class);

		RecipeClickAreaRenderable.addEntry(GuiThermalGenerator.class);
		RecipeClickAreaRenderable.addEntry(GuiGasTurbine.class);
		RecipeClickAreaRenderable.addEntry(GuiDieselGenerator.class);
		RecipeClickAreaRenderable.addEntry(GuiSemifluidGenerator.class);
		RecipeClickAreaRenderable.addEntry(GuiPlasmaGenerator.class);

		RecipeClickAreaRenderable.addEntry(GuiAutoCrafting.class, 158, 18);
		RecipeClickAreaRenderable.addEntry(GuiIronFurnace.class);
		RecipeClickAreaRenderable.addEntry(GuiElectricFurnace.class);

		RecipeClickAreaRenderable.addEntry(GuiGenerator.class);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		JEIPlugin.jeiRuntime = jeiRuntime;
	}
}
