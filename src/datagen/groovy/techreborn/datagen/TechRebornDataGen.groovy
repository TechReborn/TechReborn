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

package techreborn.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import techreborn.datagen.recipes.machine.blast_furnace.BlastFurnaceRecipesProvider
import techreborn.datagen.recipes.machine.compressor.CompressorRecipesProvider
import techreborn.datagen.recipes.machine.grinder.GrinderRecipesProvider
import techreborn.datagen.recipes.machine.industrial_sawmill.IndustrialSawmillRecipesProvider
import techreborn.datagen.recipes.smelting.SmeltingRecipesProvider
import techreborn.datagen.recipes.crafting.CraftingRecipesProvider
import techreborn.datagen.tags.TRItemTagProvider
import techreborn.datagen.tags.WaterExplosionTagProvider

class TechRebornDataGen implements DataGeneratorEntrypoint {

    @Override
    void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(WaterExplosionTagProvider.&new)
		fabricDataGenerator.addProvider(TRItemTagProvider.&new)
        // tags before all else, very important!!
        fabricDataGenerator.addProvider(SmeltingRecipesProvider.&new)
        fabricDataGenerator.addProvider(CraftingRecipesProvider.&new)

		fabricDataGenerator.addProvider(GrinderRecipesProvider.&new)
		fabricDataGenerator.addProvider(CompressorRecipesProvider.&new)
		fabricDataGenerator.addProvider(BlastFurnaceRecipesProvider.&new)
		fabricDataGenerator.addProvider(IndustrialSawmillRecipesProvider.&new)
    }
}
