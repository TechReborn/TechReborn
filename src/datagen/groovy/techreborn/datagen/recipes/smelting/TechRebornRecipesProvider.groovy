package techreborn.datagen.recipes.smelting

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipesProvider
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.Ingredient
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

import java.util.function.Consumer

abstract class TechRebornRecipesProvider extends FabricRecipesProvider {
    protected Consumer<RecipeJsonProvider> exporter
    TechRebornRecipesProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator)
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        this.exporter = exporter
        generateRecipes()
    }

    abstract void generateRecipes()

    static Ingredient createIngredient(def input) {
        if (input instanceof ItemConvertible) {
            return Ingredient.ofItems(input)
        } else if (input instanceof Tag.Identified) {
            return Ingredient.fromTag(input)
        }

        throw new UnsupportedOperationException()
    }

    static String getCriterionName(def input) {
        if (input instanceof ItemConvertible) {
            return hasItem(input)
        } else if (input instanceof Tag.Identified) {
            return "has_tag_" + input.getId()
        }

        throw new UnsupportedOperationException()
    }

    static CriterionConditions getCriterionConditions(def input) {
        if (input instanceof ItemConvertible) {
            return conditionsFromItem(input)
        } else if (input instanceof Tag.Identified) {
            return conditionsFromTag(input)
        }

        throw new UnsupportedOperationException()
    }

    static String getInputPath(def input) {
        if (input instanceof ItemConvertible) {
            return getItemPath(input)
        } else if (input instanceof Tag.Identified) {
            return input.getId().toString().replace(":", "_")
        }

        throw new UnsupportedOperationException()
    }

    @Override
    protected Identifier getRecipeIdentifier(Identifier identifier) {
        return new Identifier("techreborn", super.getRecipeIdentifier(identifier).path)
    }
}
