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

package techreborn.datagen.recipes

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancements.predicates.ItemPredicate
import net.minecraft.advancements.triggers.Criterion
import net.minecraft.advancements.triggers.InventoryChangeTrigger
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.advancements.Advancement
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.core.HolderLookup
import net.minecraft.tags.TagKey
import net.minecraft.resources.Identifier
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.datagen.recipes.machine.assembling_machine.AssemblingMachineRecipeJsonFactory
import techreborn.datagen.recipes.machine.blast_furnace.BlastFurnaceRecipeJsonFactory
import techreborn.datagen.recipes.machine.centrifuge.CentrifugeRecipeJsonFactory
import techreborn.datagen.recipes.machine.fluid_generator.FluidGeneratorRecipeJsonFactory
import techreborn.datagen.recipes.machine.fusion_reactor.FusionReactorRecipeJsonFactory
import techreborn.datagen.recipes.machine.industrial_grinder.IndustrialGrinderRecipeJsonFactory
import techreborn.datagen.recipes.machine.industrial_sawmill.IndustrialSawmillRecipeJsonFactory
import techreborn.datagen.recipes.machine.fluid_replicator.FluidReplicatorRecipeJsonFactory
import techreborn.datagen.recipes.machine.rolling_machine.RollingMachineRecipeJsonFactory
import techreborn.datagen.recipes.machine.scrapbox.ScrapboxRecipeJsonFactory
import techreborn.init.ModFluids
import techreborn.init.ModRecipes
import techreborn.init.TRContent
import techreborn.init.TRContent.Cells
import techreborn.recipe.recipes.FluidGeneratorRecipe

import java.util.concurrent.CompletableFuture

abstract class TechRebornRecipesProvider extends FabricRecipeProvider {
	protected RecipeOutput exporter
	public Set<Identifier> exportedRecipes = []
	public HolderGetter<Item> itemLookup
	public RecipeProvider generator

	TechRebornRecipesProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
		itemLookup = wrapperLookup.lookupOrThrow(Registries.ITEM)
		generator = new TechRebornRecipeGenerator(recipes, advancements)
		exporter = generator.output
		return generator
	}

	abstract void generateRecipes()

	Ingredient createIngredient(def input) {
		if (input instanceof Ingredient) {
			return input
		}
		if (input instanceof ItemLike) {
			return Ingredient.of(input)
		}
		if (input instanceof TagKey) {
			return Ingredient.of(itemLookup.getOrThrow(input))
		}

		throw new IllegalArgumentException()
	}

	static String getCriterionName(def input) {
		if (input instanceof ItemLike) {
			return RecipeProvider.getHasName(input)
		} else if (input instanceof TagKey) {
			return "has_tag_" + input.location().toDebugFileName()
		}

		throw new IllegalArgumentException()
	}

	Criterion<InventoryChangeTrigger.TriggerInstance> getCriterionConditions(def input) {
		if (input instanceof ItemLike) {
			return generator.has(input)
		} else if (input instanceof TagKey) {
			return generator.has(input)
		} else if (input instanceof ItemPredicate)
			return RecipeProvider.inventoryTrigger(input)

		throw new IllegalArgumentException()
	}

	ItemPredicate getCellItemPredicate(ModFluids fluid){
		return getCellItemPredicate(fluid.getFluid())
	}

	ItemPredicate getCellItemPredicate(Fluid fluid){
		def cell = Cells.getCellByFluid(fluid)
		return ItemPredicate.Builder.item()
			.of(itemLookup, cell.asItem())
			.build()
	}

	static String getInputPath(def input) {
		if (input instanceof ItemLike) {
			return RecipeProvider.getItemName(input)
		} else if (input instanceof TagKey) {
			return input.location().toString().replace(":", "_")
		}

		throw new IllegalArgumentException()
	}

	static String getName(def input) {
		if (input instanceof ItemLike) {
			return RecipeProvider.getItemName(input)
		} else if (input instanceof TagKey) {
			String name = input.location().toString()
			if (name.contains(":"))
				name = name.substring(name.indexOf(":")+1)
			return name
		}

		throw new IllegalArgumentException()
	}

	static String getNamePart1(def input) {
		String name
		if (input instanceof ItemLike) {
			name = RecipeProvider.getItemName(input)
			return name.substring(0,name.indexOf("_"))
		} else if (input instanceof TagKey) {
			name = input.location().toString()
			if (name.contains(":"))
				name = name.substring(name.indexOf(":")+1)
			return name.substring(name.lastIndexOf("/") + 1)
		}

		throw new IllegalArgumentException()
	}

	static ItemStackTemplate stack(ItemLike itemConvertible, int count = 1) {
		return new ItemStackTemplate(itemConvertible.asItem(), count)
	}

	static ItemStackTemplate cellStack(ModFluids fluid, int count = 1) {
		return cellStack(fluid.getFluid(), count)
	}

	static ItemStackTemplate cellStack(Fluid fluid, int count = 1) {
		def cell = Cells.getCellByFluid(fluid)
		return new ItemStackTemplate(cell.asItem(), count)
	}

	// Todo refactor me out, used to help port json recipes
	static ItemStackTemplate stack(String id, int count = 1) {
		def item = BuiltInRegistries.ITEM.getValue(Identifier.parse(id))
		return new ItemStackTemplate(item, count)
	}

	// Todo refactor me out, used to help port json recipes
	static TagKey<Item> tag(String id, count = 1) {
		if (count != 1) {
			throw new UnsupportedOperationException()
		}

		return TagKey.create(Registries.ITEM, Identifier.parse(id))
	}

	def offerAlloySmelterRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.ALLOY_SMELTER, this, closure).offerTo(exporter)
	}

	def offerGrinderRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.GRINDER, this, closure).offerTo(exporter)
	}

	def offerCompressorRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.COMPRESSOR, this, closure).offerTo(exporter)
	}

	def offerDistillationTowerRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.DISTILLATION_TOWER, this, closure).offerTo(exporter)
	}

	def offerExtractorRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.EXTRACTOR, this, closure).offerTo(exporter)
	}

	def offerChemicalReactorRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.CHEMICAL_REACTOR, this, closure).offerTo(exporter)
	}

	def offerAssemblingMachineRecipe(@DelegatesTo(value = AssemblingMachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		AssemblingMachineRecipeJsonFactory.createAssemblingMachine(this, closure).offerTo(exporter)
	}

	def offerCentrifugeRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		CentrifugeRecipeJsonFactory.createCentrifuge(this, closure).offerTo(exporter)
	}

	def offerWireMillRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.WIRE_MILL, this, closure).offerTo(exporter)
	}

	def offerImplosionCompressorRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.IMPLOSION_COMPRESSOR, this, closure).offerTo(exporter)
	}

	def offerIndustrialElectrolyzerRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.INDUSTRIAL_ELECTROLYZER, this, closure).offerTo(exporter)
	}

	def offerVacuumFreezerRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.VACUUM_FREEZER, this, closure).offerTo(exporter)
	}

	def offerScrapboxRecipe(@DelegatesTo(value = ScrapboxRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		ScrapboxRecipeJsonFactory.createScrapBox(this, closure).offerTo(exporter)
	}

	def offerRecyclerRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.RECYCLER, this, closure).offerTo(exporter)
	}

	def offerSolidCanningMachineRecipe(@DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		MachineRecipeJsonFactory.create(ModRecipes.SOLID_CANNING_MACHINE, this, closure).offerTo(exporter)
	}

	def offerBlastFurnaceRecipe(@DelegatesTo(value = BlastFurnaceRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		BlastFurnaceRecipeJsonFactory.createBlastFurnace(this, closure).offerTo(exporter)
	}

	def offerIndustrialGrinderRecipe(@DelegatesTo(value = IndustrialGrinderRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		IndustrialGrinderRecipeJsonFactory.createIndustrialGrinder(this, closure).offerTo(exporter)
	}

	def offerIndustrialSawmillRecipe(@DelegatesTo(value = IndustrialSawmillRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		IndustrialSawmillRecipeJsonFactory.createIndustrialSawmill(this, closure).offerTo(exporter)
	}

	def offerFluidReplicatorRecipe(@DelegatesTo(value = FluidReplicatorRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		FluidReplicatorRecipeJsonFactory.createFluidReplicator(this, closure).offerTo(exporter)
	}

	def offerRollingMachineRecipe(@DelegatesTo(value = RollingMachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		RollingMachineRecipeJsonFactory.createRollingMachine(this, closure).offerTo(exporter)
	}

	def offerFusionReactorRecipe(@DelegatesTo(value = FusionReactorRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		FusionReactorRecipeJsonFactory.createFusionReactor(this, closure).offerTo(exporter)
	}

	def offerFluidGeneratorRecipe(RecipeType<FluidGeneratorRecipe> type, @DelegatesTo(value = FluidGeneratorRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		FluidGeneratorRecipeJsonFactory.createFluidGenerator(type, this, closure).offerTo(exporter)
	}

	@Override
	protected Identifier getRecipeIdentifier(Identifier identifier) {
		return Identifier.fromNamespaceAndPath("techreborn", super.getRecipeIdentifier(identifier).path)
	}

	@Override
	String getName() {
		return "Recipes / " + getClass().name
	}

	class TechRebornRecipeGenerator extends RecipeProvider {
		protected TechRebornRecipeGenerator(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
			super(recipes, advancements)
		}

		@Override
		void buildRecipes() {
			TechRebornRecipesProvider.this.generateRecipes()
		}
	}
}
