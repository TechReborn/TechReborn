package techreborn.client.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import techreborn.TechReborn;
import techreborn.client.compat.emi.machine.OneInputOneOutputRecipe;
import techreborn.client.compat.emi.machine.TwoInputsCenterOutputRecipe;
import techreborn.init.ModRecipes;
import techreborn.init.TRContent;

import java.util.List;

public class EMIPlugin implements EmiPlugin {
	public static final EmiRecipeCategory ALLOY_SMELTER_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.ALLOY_SMELTER.name),
			EmiStack.of(TRContent.Machine.ALLOY_SMELTER),
			EmiStack.of(TRContent.Machine.ALLOY_SMELTER),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory ASSEMBLING_MACHINE_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.ASSEMBLY_MACHINE.name),
			EmiStack.of(TRContent.Machine.ASSEMBLY_MACHINE),
			EmiStack.of(TRContent.Machine.ASSEMBLY_MACHINE),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory BLAST_FURNACE_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.INDUSTRIAL_BLAST_FURNACE.name),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_BLAST_FURNACE),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_BLAST_FURNACE),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory CENTRIFUGE_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.INDUSTRIAL_CENTRIFUGE.name),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_CENTRIFUGE),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_CENTRIFUGE),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory CHEMICAL_REACTOR_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.CHEMICAL_REACTOR.name),
			EmiStack.of(TRContent.Machine.CHEMICAL_REACTOR),
			EmiStack.of(TRContent.Machine.CHEMICAL_REACTOR),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory COMPRESSOR_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.COMPRESSOR.name),
			EmiStack.of(TRContent.Machine.COMPRESSOR),
			EmiStack.of(TRContent.Machine.COMPRESSOR),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory DISTILLATION_TOWER_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.DISTILLATION_TOWER.name),
			EmiStack.of(TRContent.Machine.DISTILLATION_TOWER),
			EmiStack.of(TRContent.Machine.DISTILLATION_TOWER),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory EXTRACTOR_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.EXTRACTOR.name),
			EmiStack.of(TRContent.Machine.EXTRACTOR),
			EmiStack.of(TRContent.Machine.EXTRACTOR),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory FLUID_REPLICATOR_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.FLUID_REPLICATOR.name),
			EmiStack.of(TRContent.Machine.FLUID_REPLICATOR),
			EmiStack.of(TRContent.Machine.FLUID_REPLICATOR),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory FUSION_REACTOR_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.FUSION_CONTROL_COMPUTER.name),
			EmiStack.of(TRContent.Machine.FUSION_CONTROL_COMPUTER),
			EmiStack.of(TRContent.Machine.FUSION_CONTROL_COMPUTER),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory GRINDER_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.GRINDER.name),
			EmiStack.of(TRContent.Machine.GRINDER),
			EmiStack.of(TRContent.Machine.GRINDER),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory IMPLOSION_COMPRESSOR_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.IMPLOSION_COMPRESSOR.name),
			EmiStack.of(TRContent.Machine.IMPLOSION_COMPRESSOR),
			EmiStack.of(TRContent.Machine.IMPLOSION_COMPRESSOR),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory INDUSTRIAL_ELECTROLYZER_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.INDUSTRIAL_ELECTROLYZER.name),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_ELECTROLYZER),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_ELECTROLYZER),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory INDUSTRIAL_GRINDER_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.INDUSTRIAL_GRINDER.name),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_GRINDER),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_GRINDER),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory INDUSTRIAL_SAWMILL_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.INDUSTRIAL_SAWMILL.name),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_SAWMILL),
			EmiStack.of(TRContent.Machine.INDUSTRIAL_SAWMILL),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory ROLLING_MACHINE_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.ROLLING_MACHINE.name),
			EmiStack.of(TRContent.Machine.ROLLING_MACHINE),
			EmiStack.of(TRContent.Machine.ROLLING_MACHINE),
			EmiRecipeSorting.compareOutputThenInput()
		);
//	public static final EmiRecipeCategory SCRAPBOX_CATEGORY =
//		new EmiRecipeCategory(
//			Identifier.of(TechReborn.MOD_ID, TRContent.SCRAP_BOX.toString()),
//			EmiStack.of(TRContent.SCRAP_BOX),
//			EmiStack.of(TRContent.SCRAP_BOX),
//			EmiRecipeSorting.compareOutputThenInput()
//		);
	public static final EmiRecipeCategory VACUUM_FREEZER_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.VACUUM_FREEZER.name),
			EmiStack.of(TRContent.Machine.VACUUM_FREEZER),
			EmiStack.of(TRContent.Machine.VACUUM_FREEZER),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory SOLID_CANNING_MACHINE_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.SOLID_CANNING_MACHINE.name),
			EmiStack.of(TRContent.Machine.SOLID_CANNING_MACHINE),
			EmiStack.of(TRContent.Machine.SOLID_CANNING_MACHINE),
			EmiRecipeSorting.compareOutputThenInput()
		);
	public static final EmiRecipeCategory WIRE_MILL_CATEGORY =
		new EmiRecipeCategory(
			Identifier.of(TechReborn.MOD_ID, TRContent.Machine.WIRE_MILL.name),
			EmiStack.of(TRContent.Machine.WIRE_MILL),
			EmiStack.of(TRContent.Machine.WIRE_MILL),
			EmiRecipeSorting.compareOutputThenInput()
		);

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(ALLOY_SMELTER_CATEGORY);
		registry.addCategory(ASSEMBLING_MACHINE_CATEGORY);
		registry.addCategory(BLAST_FURNACE_CATEGORY);
		registry.addCategory(CENTRIFUGE_CATEGORY);
		registry.addCategory(CHEMICAL_REACTOR_CATEGORY);
		registry.addCategory(COMPRESSOR_CATEGORY);
		registry.addCategory(DISTILLATION_TOWER_CATEGORY);
		registry.addCategory(EXTRACTOR_CATEGORY);
		registry.addCategory(FLUID_REPLICATOR_CATEGORY);
		registry.addCategory(FUSION_REACTOR_CATEGORY);
		registry.addCategory(GRINDER_CATEGORY);
		registry.addCategory(IMPLOSION_COMPRESSOR_CATEGORY);
		registry.addCategory(INDUSTRIAL_ELECTROLYZER_CATEGORY);
		registry.addCategory(INDUSTRIAL_GRINDER_CATEGORY);
		registry.addCategory(INDUSTRIAL_SAWMILL_CATEGORY);
		registry.addCategory(ROLLING_MACHINE_CATEGORY);
		//registry.addCategory(SCRAPBOX_CATEGORY);
		registry.addCategory(VACUUM_FREEZER_CATEGORY);
		registry.addCategory(SOLID_CANNING_MACHINE_CATEGORY);
		registry.addCategory(WIRE_MILL_CATEGORY);

		registry.addWorkstation(ALLOY_SMELTER_CATEGORY, EmiStack.of(TRContent.Machine.ALLOY_SMELTER));
		registry.addWorkstation(ALLOY_SMELTER_CATEGORY, EmiStack.of(TRContent.Machine.IRON_ALLOY_FURNACE));
		registry.addWorkstation(ASSEMBLING_MACHINE_CATEGORY, EmiStack.of(TRContent.Machine.ASSEMBLY_MACHINE));
		registry.addWorkstation(BLAST_FURNACE_CATEGORY, EmiStack.of(TRContent.Machine.INDUSTRIAL_BLAST_FURNACE));
		registry.addWorkstation(CENTRIFUGE_CATEGORY, EmiStack.of(TRContent.Machine.INDUSTRIAL_CENTRIFUGE));
		registry.addWorkstation(CHEMICAL_REACTOR_CATEGORY, EmiStack.of(TRContent.Machine.CHEMICAL_REACTOR));
		registry.addWorkstation(COMPRESSOR_CATEGORY, EmiStack.of(TRContent.Machine.COMPRESSOR));
		registry.addWorkstation(DISTILLATION_TOWER_CATEGORY, EmiStack.of(TRContent.Machine.DISTILLATION_TOWER));
		registry.addWorkstation(EXTRACTOR_CATEGORY, EmiStack.of(TRContent.Machine.EXTRACTOR));
		registry.addWorkstation(FLUID_REPLICATOR_CATEGORY, EmiStack.of(TRContent.Machine.FLUID_REPLICATOR));
		registry.addWorkstation(FUSION_REACTOR_CATEGORY, EmiStack.of(TRContent.Machine.FUSION_CONTROL_COMPUTER));
		registry.addWorkstation(GRINDER_CATEGORY, EmiStack.of(TRContent.Machine.GRINDER));
		registry.addWorkstation(IMPLOSION_COMPRESSOR_CATEGORY, EmiStack.of(TRContent.Machine.IMPLOSION_COMPRESSOR));
		registry.addWorkstation(INDUSTRIAL_ELECTROLYZER_CATEGORY, EmiStack.of(TRContent.Machine.INDUSTRIAL_ELECTROLYZER));
		registry.addWorkstation(INDUSTRIAL_GRINDER_CATEGORY, EmiStack.of(TRContent.Machine.INDUSTRIAL_GRINDER));
		registry.addWorkstation(INDUSTRIAL_SAWMILL_CATEGORY, EmiStack.of(TRContent.Machine.INDUSTRIAL_SAWMILL));
		registry.addWorkstation(ROLLING_MACHINE_CATEGORY, EmiStack.of(TRContent.Machine.ROLLING_MACHINE));
		//registry.addWorkstation(SCRAPBOX_CATEGORY, EmiStack.of(TRContent.SCRAP_BOX));
		registry.addWorkstation(SOLID_CANNING_MACHINE_CATEGORY, EmiStack.of(TRContent.Machine.SOLID_CANNING_MACHINE));
		registry.addWorkstation(WIRE_MILL_CATEGORY, EmiStack.of(TRContent.Machine.WIRE_MILL));
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(TRContent.Machine.AUTO_CRAFTING_TABLE));
		registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, EmiStack.of(TRContent.Machine.IRON_FURNACE));
		registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, EmiStack.of(TRContent.Machine.ELECTRIC_FURNACE));

		registry.setDefaultComparison(EmiStack.of(TRContent.CELL), Comparison.compareComponents());

		registerComplexRecipe(ALLOY_SMELTER_CATEGORY, registry.getRecipeManager().listAllOfType(ModRecipes.ALLOY_SMELTER), registry);
		registerComplexRecipe(CHEMICAL_REACTOR_CATEGORY, registry.getRecipeManager().listAllOfType(ModRecipes.CHEMICAL_REACTOR), registry);
		registerSimpleRecipe(COMPRESSOR_CATEGORY, registry.getRecipeManager().listAllOfType(ModRecipes.COMPRESSOR), registry);
		registerSimpleRecipe(EXTRACTOR_CATEGORY, registry.getRecipeManager().listAllOfType(ModRecipes.EXTRACTOR), registry);
		registerSimpleRecipe(GRINDER_CATEGORY, registry.getRecipeManager().listAllOfType(ModRecipes.GRINDER), registry);
		//registerSimpleRecipe(SCRAPBOX_CATEGORY, registry.getRecipeManager().listAllOfType(ModRecipes.SCRAPBOX), registry);
		registerSimpleRecipe(VACUUM_FREEZER_CATEGORY, registry.getRecipeManager().listAllOfType(ModRecipes.VACUUM_FREEZER), registry);
		registerComplexRecipe(SOLID_CANNING_MACHINE_CATEGORY, registry.getRecipeManager().listAllOfType(ModRecipes.SOLID_CANNING_MACHINE), registry);
		registerSimpleRecipe(WIRE_MILL_CATEGORY, registry.getRecipeManager().listAllOfType(ModRecipes.WIRE_MILL), registry);
	}

	public void registerSimpleRecipe(EmiRecipeCategory recipeCategory, List<RecipeEntry<RebornRecipe>> recipeEntry, EmiRegistry registry) {
		for (RecipeEntry<RebornRecipe> entry : recipeEntry) {
			registry.addRecipe(new OneInputOneOutputRecipe<>(entry, recipeCategory));
		}
	}

	public void registerComplexRecipe(EmiRecipeCategory recipeCategory, List<RecipeEntry<RebornRecipe>> recipeEntry, EmiRegistry registry) {
		for (RecipeEntry<RebornRecipe> entry : recipeEntry) {
			registry.addRecipe(new TwoInputsCenterOutputRecipe<>(entry, recipeCategory));
		}
	}
}
