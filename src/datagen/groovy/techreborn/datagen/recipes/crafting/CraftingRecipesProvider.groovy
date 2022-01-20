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

package techreborn.datagen.recipes.crafting

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import techreborn.TechReborn
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.tags.CommonTags
import techreborn.init.TRContent 

class CraftingRecipesProvider extends TechRebornRecipesProvider {
	CraftingRecipesProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	void generateRecipes() {
        // Add dust from small dust and vice versa recipes.
        TRContent.SmallDusts.getSD2DMap().each { input, output ->
            offerMonoShapelessRecipe(input, 4, output, 1, "small", "crafting_table/dust/")
            offerMonoShapelessRecipe(output, 1, input, 4, "dust", "crafting_table/small_dust/")
        }
        generateToolRecipes()
        generateArmorRecipes()
	}

    def generateToolRecipes() {
        // add axes
        [
                (CommonTags.Items.bronzeIngots): TRContent.BRONZE_AXE,
                (TRContent.Gems.PERIDOT): TRContent.PERIDOT_AXE,
                (TRContent.Gems.RUBY): TRContent.RUBY_AXE,
                (TRContent.Gems.SAPPHIRE): TRContent.SAPPHIRE_AXE
        ].each { material, axe ->
            offerAxeRecipe(material, axe, "crafting_table/gem_armor_tools/")
        }
        // add hoes
        [
                (CommonTags.Items.bronzeIngots): TRContent.BRONZE_HOE,
                (TRContent.Gems.PERIDOT): TRContent.PERIDOT_HOE,
                (TRContent.Gems.RUBY): TRContent.RUBY_HOE,
                (TRContent.Gems.SAPPHIRE): TRContent.SAPPHIRE_HOE
        ].each { material, hoe ->
            offerHoeRecipe(material, hoe, "crafting_table/gem_armor_tools/")
        }
        // add pickaxes
        [
                (CommonTags.Items.bronzeIngots): TRContent.BRONZE_PICKAXE,
                (TRContent.Gems.PERIDOT): TRContent.PERIDOT_PICKAXE,
                (TRContent.Gems.RUBY): TRContent.RUBY_PICKAXE,
                (TRContent.Gems.SAPPHIRE): TRContent.SAPPHIRE_PICKAXE
        ].each { material, pickaxe ->
            offerPickaxeRecipe(material, pickaxe, "crafting_table/gem_armor_tools/")
        }
        // add shovels
        [
                (CommonTags.Items.bronzeIngots): TRContent.BRONZE_SPADE,
                (TRContent.Gems.PERIDOT): TRContent.PERIDOT_SPADE,
                (TRContent.Gems.RUBY): TRContent.RUBY_SPADE,
                (TRContent.Gems.SAPPHIRE): TRContent.SAPPHIRE_SPADE
        ].each { material, shovel ->
            offerShovelRecipe(material, shovel, "crafting_table/gem_armor_tools/", "spade")
        }
        // add swords
        [
                (CommonTags.Items.bronzeIngots): TRContent.BRONZE_SWORD,
                (TRContent.Gems.PERIDOT): TRContent.PERIDOT_SWORD,
                (TRContent.Gems.RUBY): TRContent.RUBY_SWORD,
                (TRContent.Gems.SAPPHIRE): TRContent.SAPPHIRE_SWORD
        ].each { material, sword ->
            offerSwordRecipe(material, sword, "crafting_table/gem_armor_tools/")
        }
    }

    def generateArmorRecipes() {
        // add boots
        [
                (CommonTags.Items.bronzeIngots): TRContent.BRONZE_BOOTS,
                (TRContent.Gems.PERIDOT): TRContent.PERIDOT_BOOTS,
                (TRContent.Gems.RUBY): TRContent.RUBY_BOOTS,
                (TRContent.Gems.SAPPHIRE): TRContent.SAPPHIRE_BOOTS,
                (TRContent.Ingots.SILVER): TRContent.SILVER_BOOTS,
                (TRContent.Ingots.STEEL): TRContent.STEEL_BOOTS
        ].each { material, boots ->
            offerBootsRecipe(material, boots, "crafting_table/gem_armor_tools/")
        }
        // add chestplate
        [
                (CommonTags.Items.bronzeIngots): TRContent.BRONZE_CHESTPLATE,
                (TRContent.Gems.PERIDOT): TRContent.PERIDOT_CHESTPLATE,
                (TRContent.Gems.RUBY): TRContent.RUBY_CHESTPLATE,
                (TRContent.Gems.SAPPHIRE): TRContent.SAPPHIRE_CHESTPLATE,
                (TRContent.Ingots.SILVER): TRContent.SILVER_CHESTPLATE,
                (TRContent.Ingots.STEEL): TRContent.STEEL_CHESTPLATE
        ].each { material, chestplate ->
            offerChestplateRecipe(material, chestplate, "crafting_table/gem_armor_tools/")
        }
        // add helmets
        [
                (CommonTags.Items.bronzeIngots): TRContent.BRONZE_HELMET,
                (TRContent.Gems.PERIDOT): TRContent.PERIDOT_HELMET,
                (TRContent.Gems.RUBY): TRContent.RUBY_HELMET,
                (TRContent.Gems.SAPPHIRE): TRContent.SAPPHIRE_HELMET,
                (TRContent.Ingots.SILVER): TRContent.SILVER_HELMET,
                (TRContent.Ingots.STEEL): TRContent.STEEL_HELMET
        ].each { material, helmet ->
            offerHelmetRecipe(material, helmet, "crafting_table/gem_armor_tools/")
        }
        // add leggings
        [
                (CommonTags.Items.bronzeIngots): TRContent.BRONZE_LEGGINGS,
                (TRContent.Gems.PERIDOT): TRContent.PERIDOT_LEGGINGS,
                (TRContent.Gems.RUBY): TRContent.RUBY_LEGGINGS,
                (TRContent.Gems.SAPPHIRE): TRContent.SAPPHIRE_LEGGINGS,
                (TRContent.Ingots.SILVER): TRContent.SILVER_LEGGINGS,
                (TRContent.Ingots.STEEL): TRContent.STEEL_LEGGINGS
        ].each { material, leggings ->
            offerLeggingsRecipe(material, leggings, "crafting_table/gem_armor_tools/")
        }
    }

    def offerMonoShapelessRecipe(def input, int inputSize, ItemConvertible output, int outputSize, String source = getInputPath(input), prefix = "") {
        ShapelessRecipeJsonFactory.create(output, outputSize).input(createIngredient(input), inputSize)
                .criterion(getCriterionName(input), getCriterionConditions(input))
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, prefix + getInputPath(output) + "_from_" + source))
    }

    def static materialTypeString(String prefix, def material, String type) {
        StringBuilder s = new StringBuilder()
        s.append(prefix)
        s.append(getNamePart1(material))
        s.append('_')
        s.append(type)
        return s.toString()
    }

    def static createMonoShapeRecipe(def input, ItemConvertible output, char character) {
        return ShapedRecipeJsonFactory.create(output)
                .input(character, createIngredient(input))
                .criterion(getCriterionName(input), getCriterionConditions(input))
    }

    def static createDuoShapeRecipe(def input1, def input2, ItemConvertible output, char char1, char char2, boolean crit1 = true, boolean crit2 = false) {
        ShapedRecipeJsonFactory factory = ShapedRecipeJsonFactory.create(output)
                .input(char1, createIngredient(input1))
                .input(char2, createIngredient(input1))
        if (crit1)
            factory = factory.criterion(getCriterionName(input1), getCriterionConditions(input1))
        if (crit2)
            factory = factory.criterion(getCriterionName(input2), getCriterionConditions(input2))
        return factory
    }

    def offerAxeRecipe(def material, ItemConvertible output, prefix = "", String type = "axe") {
        createDuoShapeRecipe(material, Ingredient.ofItems(Items.STICK), output,
                'X' as char, '#' as char)
                .pattern("XX")
                .pattern("X#")
                .pattern(" #")
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type)))
    }

    def offerHoeRecipe(def material, ItemConvertible output, prefix = "", String type = "hoe") {
        createDuoShapeRecipe(material, Ingredient.ofItems(Items.STICK), output,
                'X' as char, '#' as char)
                .pattern("XX")
                .pattern(" #")
                .pattern(" #")
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type)))
    }

    def offerPickaxeRecipe(def material, ItemConvertible output, prefix = "", String type = "pickaxe") {
        createDuoShapeRecipe(material, Ingredient.ofItems(Items.STICK), output,
                'X' as char, '#' as char)
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type)))
    }

    def offerShovelRecipe(def material, ItemConvertible output, prefix = "", String type = "shovel") {
        createDuoShapeRecipe(material, Ingredient.ofItems(Items.STICK), output,
                'X' as char, '#' as char)
                .pattern("X")
                .pattern("#")
                .pattern("#")
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type)))
    }

    def offerSwordRecipe(def material, ItemConvertible output, prefix = "", String type = "sword") {
        createDuoShapeRecipe(material, Ingredient.ofItems(Items.STICK), output,
                'X' as char, '#' as char)
                .pattern("X")
                .pattern("X")
                .pattern("#")
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type)))
    }

    def offerBootsRecipe(def material, ItemConvertible output, prefix = "", String type = "boots") {
        createMonoShapeRecipe(material, output, 'X' as char)
                .pattern("X X")
                .pattern("X X")
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type)))
    }

    def offerChestplateRecipe(def material, ItemConvertible output, prefix = "", String type = "chestplate") {
        createMonoShapeRecipe(material, output, 'X' as char)
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type)))
    }

    def offerHelmetRecipe(def material, ItemConvertible output, prefix = "", String type = "helmet") {
        createMonoShapeRecipe(material, output, 'X' as char)
                .pattern("XXX")
                .pattern("X X")
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type)))
    }

    def offerLeggingsRecipe(def material, ItemConvertible output, prefix = "", String type = "leggings") {
        createMonoShapeRecipe(material, output, 'X' as char)
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .offerTo(this.exporter, new Identifier(TechReborn.MOD_ID, materialTypeString(prefix, material, type)))
    }


}
