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

package techreborn.init;

import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import reborncore.common.crafting.serde.RebornFluidRecipeSerde;
import reborncore.common.crafting.serde.RebornRecipeSerde;
import techreborn.api.recipe.recipes.AssemblingMachineRecipe;
import techreborn.api.recipe.recipes.BlastFurnaceRecipe;
import techreborn.api.recipe.recipes.CentrifugeRecipe;
import techreborn.api.recipe.recipes.FluidReplicatorRecipe;
import techreborn.api.recipe.recipes.FusionReactorRecipe;
import techreborn.api.recipe.recipes.IndustrialGrinderRecipe;
import techreborn.api.recipe.recipes.IndustrialSawmillRecipe;
import techreborn.api.recipe.recipes.RollingMachineRecipe;
import techreborn.api.recipe.recipes.serde.BlastFurnaceRecipeSerde;
import techreborn.api.recipe.recipes.serde.FusionReactorRecipeSerde;
import techreborn.api.recipe.recipes.serde.RollingMachineRecipeSerde;

public class ModRecipes {
	public static final BlastFurnaceRecipeSerde BLAST_FURNACE_RECIPE_SERDE = new BlastFurnaceRecipeSerde();
	public static final RebornFluidRecipeSerde<IndustrialGrinderRecipe> INDUSTRIAL_GRINDER_RECIPE_SERDE = RebornFluidRecipeSerde.create(IndustrialGrinderRecipe::new);
	public static final RebornFluidRecipeSerde<IndustrialSawmillRecipe> INDUSTRIAL_SAWMILL_RECIPE_SERDE = RebornFluidRecipeSerde.create(IndustrialSawmillRecipe::new);
	public static final RebornFluidRecipeSerde<FluidReplicatorRecipe> FLUID_REPLICATOR_RECIPE_SERDE = RebornFluidRecipeSerde.create(FluidReplicatorRecipe::new);
	public static final FusionReactorRecipeSerde FUSION_REACTOR_RECIPE_SERDE = new FusionReactorRecipeSerde();
	public static final RollingMachineRecipeSerde ROLLING_MACHINE_RECIPE_SERDE = new RollingMachineRecipeSerde();
	public static final RebornRecipeSerde<AssemblingMachineRecipe> ASSEMBLING_RECIPE_SERDE = RebornRecipeSerde.create(AssemblingMachineRecipe::new);
	public static final RebornRecipeSerde<CentrifugeRecipe> CENTRIFUGE_RECIPE_SERDE = RebornRecipeSerde.create(CentrifugeRecipe::new);

	public static final RebornRecipeType<RebornRecipe> ALLOY_SMELTER = RecipeManager.newRecipeType(new Identifier("techreborn:alloy_smelter"));
	public static final RebornRecipeType<AssemblingMachineRecipe> ASSEMBLING_MACHINE = RecipeManager.newRecipeType(ASSEMBLING_RECIPE_SERDE, new Identifier("techreborn:assembling_machine"));
	public static final RebornRecipeType<BlastFurnaceRecipe> BLAST_FURNACE = RecipeManager.newRecipeType(BLAST_FURNACE_RECIPE_SERDE, new Identifier("techreborn:blast_furnace"));
	public static final RebornRecipeType<CentrifugeRecipe> CENTRIFUGE = RecipeManager.newRecipeType(CENTRIFUGE_RECIPE_SERDE, new Identifier("techreborn:centrifuge"));
	public static final RebornRecipeType<RebornRecipe> CHEMICAL_REACTOR = RecipeManager.newRecipeType(new Identifier("techreborn:chemical_reactor"));
	public static final RebornRecipeType<RebornRecipe> COMPRESSOR = RecipeManager.newRecipeType(new Identifier("techreborn:compressor"));
	public static final RebornRecipeType<RebornRecipe> DISTILLATION_TOWER = RecipeManager.newRecipeType(new Identifier("techreborn:distillation_tower"));
	public static final RebornRecipeType<RebornRecipe> EXTRACTOR = RecipeManager.newRecipeType(new Identifier("techreborn:extractor"));
	public static final RebornRecipeType<RebornRecipe> GRINDER = RecipeManager.newRecipeType(new Identifier("techreborn:grinder"));
	public static final RebornRecipeType<RebornRecipe> IMPLOSION_COMPRESSOR = RecipeManager.newRecipeType(new Identifier("techreborn:implosion_compressor"));
	public static final RebornRecipeType<RebornRecipe> INDUSTRIAL_ELECTROLYZER = RecipeManager.newRecipeType(new Identifier("techreborn:industrial_electrolyzer"));
	public static final RebornRecipeType<IndustrialGrinderRecipe> INDUSTRIAL_GRINDER = RecipeManager.newRecipeType(INDUSTRIAL_GRINDER_RECIPE_SERDE, new Identifier("techreborn:industrial_grinder"));
	public static final RebornRecipeType<IndustrialSawmillRecipe> INDUSTRIAL_SAWMILL = RecipeManager.newRecipeType(INDUSTRIAL_SAWMILL_RECIPE_SERDE, new Identifier("techreborn:industrial_sawmill"));
	public static final RebornRecipeType<RebornRecipe> RECYCLER = RecipeManager.newRecipeType(new Identifier("techreborn:recycler"));
	public static final RebornRecipeType<RebornRecipe> SCRAPBOX = RecipeManager.newRecipeType(new Identifier("techreborn:scrapbox"));
	public static final RebornRecipeType<RebornRecipe> VACUUM_FREEZER = RecipeManager.newRecipeType(new Identifier("techreborn:vacuum_freezer"));
	public static final RebornRecipeType<FluidReplicatorRecipe> FLUID_REPLICATOR = RecipeManager.newRecipeType(FLUID_REPLICATOR_RECIPE_SERDE, new Identifier("techreborn:fluid_replicator"));
	public static final RebornRecipeType<FusionReactorRecipe> FUSION_REACTOR = RecipeManager.newRecipeType(FUSION_REACTOR_RECIPE_SERDE, new Identifier("techreborn:fusion_reactor"));
	public static final RebornRecipeType<RollingMachineRecipe> ROLLING_MACHINE = RecipeManager.newRecipeType(ROLLING_MACHINE_RECIPE_SERDE, new Identifier("techreborn:rolling_machine"));
	public static final RebornRecipeType<RebornRecipe> SOLID_CANNING_MACHINE = RecipeManager.newRecipeType(new Identifier("techreborn:solid_canning_machine"));
	public static final RebornRecipeType<RebornRecipe> WIRE_MILL = RecipeManager.newRecipeType(new Identifier("techreborn:wire_mill"));

	public static RebornRecipeType<?> byName(Identifier identifier) {
		return (RebornRecipeType<?>) Registries.RECIPE_SERIALIZER.get(identifier);
	}
}
