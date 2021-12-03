package techreborn.datagen.recipes.smelting

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.CookingRecipeSerializer
import net.minecraft.recipe.RecipeSerializer
import techreborn.datagen.tags.CommonTagsTrait
import techreborn.init.TRContent

class SmeltingRecipesProvider extends TechRebornRecipesProvider implements CommonTagsTrait {
	SmeltingRecipesProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	void generateRecipes() {
        // Add smelting and blasting recipes.
        [
            (TRContent.Ingots.MIXED_METAL): TRContent.Ingots.ADVANCED_ALLOY,
            (cItem("brass_dusts")): TRContent.Ingots.BRASS,
            (TRContent.Dusts.BRONZE): TRContent.Ingots.BRONZE,
            (cItem("electrum_dusts")): TRContent.Ingots.ELECTRUM,
            (TRContent.Dusts.INVAR): TRContent.Ingots.INVAR,
            (cItem("lead_ores")): TRContent.Ingots.LEAD,
            (cItem("raw_lead_ores")): TRContent.Ingots.LEAD,
            (TRContent.Dusts.NICKEL): TRContent.Ingots.NICKEL,
            (cItem("sheldonite_ores")): TRContent.Ingots.PLATINUM,
            (cItem("platinum_dusts")): TRContent.Ingots.PLATINUM,
            (Items.IRON_INGOT): TRContent.Ingots.REFINED_IRON,
            (cItem("silver_ores")): TRContent.Ingots.SILVER,
            (cItem("raw_silver_ores")): TRContent.Ingots.SILVER,
            (cItem("tin_ores")): TRContent.Ingots.TIN,
            (cItem("raw_tin_ores")): TRContent.Ingots.TIN,
            (cItem("zinc_dusts")): TRContent.Ingots.ZINC
        ].each {input, output ->
            offerSmelting(input, output)
            offerBlasting(input, output)
        }
	}

    def offerSmelting(def input, ItemConvertible output, float experience = 0.5f, int cookingTime  = 200) {
        offerCookingRecipe(input, output, experience, cookingTime, RecipeSerializer.SMELTING, "smelting/")
    }

    def offerBlasting(def input, ItemConvertible output, float experience = 0.5f, int cookingTime  = 200) {
        offerCookingRecipe(input, output, experience, cookingTime, RecipeSerializer.BLASTING, "blasting/")
    }

    def offerCookingRecipe(def input, ItemConvertible output, float experience, int cookingTime, CookingRecipeSerializer<?> serializer, String prefix = "") {
        CookingRecipeJsonFactory.create(createIngredient(input), output, experience, cookingTime, serializer)
                .criterion(getCriterionName(input), getCriterionConditions(input))
                .offerTo(this.exporter, prefix + getInputPath(output) + "_from_" + getInputPath(input))
	}
}
