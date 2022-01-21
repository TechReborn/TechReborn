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

package techreborn.datagen.tags

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import techreborn.init.TRContent

class TRItemTagProvider extends FabricTagProvider.ItemTagProvider {
	TRItemTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	protected void generateTags() {
		TRContent.Ingots.values().each { ingot ->
			getOrCreateTagBuilder(ingot.getTag()).add(ingot.asItem())
			getOrCreateTagBuilder(TRContent.ORES_TAG).add(ingot.asItem())
		}
		TRContent.Dusts.values().each { dust ->
			getOrCreateTagBuilder(dust.getTag()).add(dust.asItem())
			getOrCreateTagBuilder(TRContent.DUSTS_TAG).add(dust.asItem())
		}
		/*Arrays.stream(TRContent.Ores.values()).forEach(
				value -> getOrCreateTagBuilder(TRContent.ORES_TAG).add(value.asItem()))
		Arrays.stream(TRContent.StorageBlocks.values()).forEach(
				value -> getOrCreateTagBuilder(TRContent.STORAGE_BLOCKS_TAG).add(value.asItem()))
		Arrays.stream(TRContent.Dusts.values()).forEach(
				value -> getOrCreateTagBuilder(TRContent.DUSTS_TAG).add(value.asItem()))
		Arrays.stream(TRContent.RawMetals.values()).forEach(
				value -> getOrCreateTagBuilder(TRContent.RAW_METALS_TAG).add(value.asItem()))
		Arrays.stream(TRContent.SmallDusts.values()).forEach(
				value -> getOrCreateTagBuilder(TRContent.SMALL_DUSTS_TAG).add(value.asItem()))
		Arrays.stream(TRContent.Gems.values()).forEach(
				value -> getOrCreateTagBuilder(TRContent.GEMS_TAG).add(value.asItem()))
		Arrays.stream(TRContent.Ingots.values()).forEach(
				value -> getOrCreateTagBuilder(TRContent.INGOTS_TAG).add(value.asItem()))
		Arrays.stream(TRContent.Nuggets.values()).forEach(
				value -> getOrCreateTagBuilder(TRContent.NUGGETS_TAG).add(value.asItem()))*/
	}
}
