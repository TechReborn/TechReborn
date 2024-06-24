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

import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RecipeManager;
import techreborn.recipe.recipes.AssemblingMachineRecipe;
import techreborn.recipe.recipes.BlastFurnaceRecipe;
import techreborn.recipe.recipes.CentrifugeRecipe;
import techreborn.recipe.recipes.FluidGeneratorRecipe;
import techreborn.recipe.recipes.FluidReplicatorRecipe;
import techreborn.recipe.recipes.FusionReactorRecipe;
import techreborn.recipe.recipes.IndustrialGrinderRecipe;
import techreborn.recipe.recipes.IndustrialSawmillRecipe;
import techreborn.recipe.recipes.RollingMachineRecipe;

public class ModRecipes {
	public static final RecipeType<RebornRecipe> ALLOY_SMELTER = RecipeManager.newRecipeType(Identifier.of("techreborn:alloy_smelter"));
	public static final RecipeType<AssemblingMachineRecipe> ASSEMBLING_MACHINE = RecipeManager.newRecipeType(Identifier.of("techreborn:assembling_machine"), AssemblingMachineRecipe.CODEC, AssemblingMachineRecipe.PACKET_CODEC);
	public static final RecipeType<BlastFurnaceRecipe> BLAST_FURNACE = RecipeManager.newRecipeType(Identifier.of("techreborn:blast_furnace"), BlastFurnaceRecipe.CODEC, BlastFurnaceRecipe.PACKET_CODEC);
	public static final RecipeType<CentrifugeRecipe> CENTRIFUGE = RecipeManager.newRecipeType(Identifier.of("techreborn:centrifuge"), CentrifugeRecipe.CODEC, CentrifugeRecipe.PACKET_CODEC);
	public static final RecipeType<RebornRecipe> CHEMICAL_REACTOR = RecipeManager.newRecipeType(Identifier.of("techreborn:chemical_reactor"));
	public static final RecipeType<RebornRecipe> COMPRESSOR = RecipeManager.newRecipeType(Identifier.of("techreborn:compressor"));
	public static final RecipeType<RebornRecipe> DISTILLATION_TOWER = RecipeManager.newRecipeType(Identifier.of("techreborn:distillation_tower"));
	public static final RecipeType<RebornRecipe> EXTRACTOR = RecipeManager.newRecipeType(Identifier.of("techreborn:extractor"));
	public static final RecipeType<RebornRecipe> GRINDER = RecipeManager.newRecipeType(Identifier.of("techreborn:grinder"));
	public static final RecipeType<RebornRecipe> IMPLOSION_COMPRESSOR = RecipeManager.newRecipeType(Identifier.of("techreborn:implosion_compressor"));
	public static final RecipeType<RebornRecipe> INDUSTRIAL_ELECTROLYZER = RecipeManager.newRecipeType(Identifier.of("techreborn:industrial_electrolyzer"));
	public static final RecipeType<IndustrialGrinderRecipe> INDUSTRIAL_GRINDER = RecipeManager.newRecipeType(Identifier.of("techreborn:industrial_grinder"), IndustrialGrinderRecipe.CODEC, IndustrialGrinderRecipe.PACKET_CODEC);
	public static final RecipeType<IndustrialSawmillRecipe> INDUSTRIAL_SAWMILL = RecipeManager.newRecipeType(Identifier.of("techreborn:industrial_sawmill"), IndustrialSawmillRecipe.CODEC, IndustrialSawmillRecipe.PACKET_CODEC);
	public static final RecipeType<RebornRecipe> RECYCLER = RecipeManager.newRecipeType(Identifier.of("techreborn:recycler"));
	public static final RecipeType<RebornRecipe> SCRAPBOX = RecipeManager.newRecipeType(Identifier.of("techreborn:scrapbox"));
	public static final RecipeType<RebornRecipe> VACUUM_FREEZER = RecipeManager.newRecipeType(Identifier.of("techreborn:vacuum_freezer"));
	public static final RecipeType<FluidReplicatorRecipe> FLUID_REPLICATOR = RecipeManager.newRecipeType(Identifier.of("techreborn:fluid_replicator"), FluidReplicatorRecipe.CODEC, FluidReplicatorRecipe.PACKET_CODEC);
	public static final RecipeType<FusionReactorRecipe> FUSION_REACTOR = RecipeManager.newRecipeType(Identifier.of("techreborn:fusion_reactor"), FusionReactorRecipe.CODEC, FusionReactorRecipe.PACKET_CODEC);
	public static final RecipeType<RollingMachineRecipe> ROLLING_MACHINE = RecipeManager.newRecipeType(Identifier.of("techreborn:rolling_machine"), RollingMachineRecipe.CODEC, RollingMachineRecipe.PACKET_CODEC);
	public static final RecipeType<RebornRecipe> SOLID_CANNING_MACHINE = RecipeManager.newRecipeType(Identifier.of("techreborn:solid_canning_machine"));
	public static final RecipeType<RebornRecipe> WIRE_MILL = RecipeManager.newRecipeType(Identifier.of("techreborn:wire_mill"));
	public static final RecipeType<FluidGeneratorRecipe> THERMAL_GENERATOR = RecipeManager.newRecipeType(Identifier.of("techreborn:thermal_generator"), FluidGeneratorRecipe.CODEC, FluidGeneratorRecipe.PACKET_CODEC);
	public static final RecipeType<FluidGeneratorRecipe> GAS_GENERATOR = RecipeManager.newRecipeType(Identifier.of("techreborn:gas_generator"), FluidGeneratorRecipe.CODEC, FluidGeneratorRecipe.PACKET_CODEC);
	public static final RecipeType<FluidGeneratorRecipe> DIESEL_GENERATOR = RecipeManager.newRecipeType(Identifier.of("techreborn:diesel_generator"), FluidGeneratorRecipe.CODEC, FluidGeneratorRecipe.PACKET_CODEC);
	public static final RecipeType<FluidGeneratorRecipe> SEMI_FLUID_GENERATOR = RecipeManager.newRecipeType(Identifier.of("techreborn:semi_fluid_generator"), FluidGeneratorRecipe.CODEC, FluidGeneratorRecipe.PACKET_CODEC);
	public static final RecipeType<FluidGeneratorRecipe> PLASMA_GENERATOR = RecipeManager.newRecipeType(Identifier.of("techreborn:plasma_generator"), FluidGeneratorRecipe.CODEC, FluidGeneratorRecipe.PACKET_CODEC);
}
