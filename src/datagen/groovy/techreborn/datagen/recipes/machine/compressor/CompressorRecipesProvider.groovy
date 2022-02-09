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

package techreborn.datagen.recipes.machine.compressor

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.tag.TagFactory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import reborncore.common.misc.TagConvertible
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.TRContent

class CompressorRecipesProvider extends TechRebornRecipesProvider {
	CompressorRecipesProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	void generateRecipes() {
		TRContent.Plates.values().each {plate ->
			if (plate.getSource() != null)
				offerCompressorRecipe {
					ingredients (plate.getSource() instanceof TagConvertible<Item> ? ((TagConvertible<Item>)plate.getSource()).asTag() : plate.getSource())
					outputs new ItemStack(plate, 1)
					power 10
					time 300
				}
			if (plate.getSourceBlock() != null)
				offerCompressorRecipe {
					ingredients (plate.getSourceBlock() instanceof TagConvertible<Item> ? ((TagConvertible<Item>)plate.getSourceBlock()).asTag() : plate.getSourceBlock())
					outputs new ItemStack(plate, 9)
					power 10
					time 300
					source "block"
				}
		}
	}
}