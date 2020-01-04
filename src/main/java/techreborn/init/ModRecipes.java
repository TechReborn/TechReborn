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

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.RecipeManager;
import techreborn.api.recipe.recipes.*;

public class ModRecipes {

	public static final RebornRecipeType<RebornRecipe> ALLOY_SMELTER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:alloy_smelter"));
	public static final RebornRecipeType<RebornRecipe> ASSEMBLING_MACHINE = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:assembling_machine"));
	public static final RebornRecipeType<BlastFurnaceRecipe> BLAST_FURNACE = RecipeManager.newRecipeType(BlastFurnaceRecipe.class, new Identifier("techreborn:blast_furnace"));
	public static final RebornRecipeType<RebornRecipe> CENTRIFUGE = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:centrifuge"));
	public static final RebornRecipeType<RebornRecipe> CHEMICAL_REACTOR = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:chemical_reactor"));
	public static final RebornRecipeType<RebornRecipe> COMPRESSOR = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:compressor"));
	public static final RebornRecipeType<RebornRecipe> DISTILLATION_TOWER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:distillation_tower"));
	public static final RebornRecipeType<RebornRecipe> EXTRACTOR = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:extractor"));
	public static final RebornRecipeType<RebornRecipe> GRINDER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:grinder"));
	public static final RebornRecipeType<RebornRecipe> IMPLOSION_COMPRESSOR = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:implosion_compressor"));
	public static final RebornRecipeType<RebornRecipe> INDUSTRIAL_ELECTROLYZER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:industrial_electrolyzer"));
	public static final RebornRecipeType<IndustrialGrinderRecipe> INDUSTRIAL_GRINDER = RecipeManager.newRecipeType(IndustrialGrinderRecipe.class, new Identifier("techreborn:industrial_grinder"));
	public static final RebornRecipeType<IndustrialSawmillRecipe> INDUSTRIAL_SAWMILL = RecipeManager.newRecipeType(IndustrialSawmillRecipe.class, new Identifier("techreborn:industrial_sawmill"));
	public static final RebornRecipeType<RebornRecipe> RECYCLER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:recycler"));
	public static final RebornRecipeType<RebornRecipe> SCRAPBOX = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:scrapbox"));
	public static final RebornRecipeType<RebornRecipe> VACUUM_FREEZER = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:vacuum_freezer"));
	public static final RebornRecipeType<FluidReplicatorRecipe> FLUID_REPLICATOR = RecipeManager.newRecipeType(FluidReplicatorRecipe.class, new Identifier("techreborn:fluid_replicator"));
	public static final RebornRecipeType<FusionReactorRecipe> FUSION_REACTOR = RecipeManager.newRecipeType(FusionReactorRecipe.class, new Identifier("techreborn:fusion_reactor"));
	public static final RebornRecipeType<RollingMachineRecipe> ROLLING_MACHINE = RecipeManager.newRecipeType(RollingMachineRecipe.class, new Identifier("techreborn:rolling_machine"));
	public static final RebornRecipeType<RebornRecipe> SOLID_CANNING_MACHINE = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:solid_canning_machine"));
	public static final RebornRecipeType<RebornRecipe> WIRE_MILL = RecipeManager.newRecipeType(RebornRecipe.class, new Identifier("techreborn:wire_mill"));

	public static RebornRecipeType<?> byName(Identifier identifier){
		return (RebornRecipeType<?>) Registry.RECIPE_SERIALIZER.get(identifier);
	}
}
