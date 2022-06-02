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
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider
import techreborn.init.TRContent

class TRItemTagProvider extends ItemTagProvider {
	TRItemTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator)
	}

	@Override
	protected void generateTags() {
		TRContent.Ores.values().each { ore ->
			getOrCreateTagBuilder(ore.asTag()).add(ore.asItem())
			getOrCreateTagBuilder(TRContent.ORES_TAG).add(ore.asItem())
		}
		getOrCreateTagBuilder(TRContent.Ores.RUTILE.asTag()).add(TRContent.Ores.RUTILE_END.asItem())
		TRContent.StorageBlocks.values().each { block ->
			getOrCreateTagBuilder(block.asTag()).add(block.asItem())
			getOrCreateTagBuilder(TRContent.STORAGE_BLOCK_TAG).add(block.asItem())
		}
		TRContent.Dusts.values().each { dust ->
			getOrCreateTagBuilder(dust.asTag()).add(dust.asItem())
			getOrCreateTagBuilder(TRContent.DUSTS_TAG).add(dust.asItem())
		}
		TRContent.RawMetals.values().each { raw ->
			getOrCreateTagBuilder(raw.asTag()).add(raw.asItem())
			getOrCreateTagBuilder(TRContent.RAW_METALS_TAG).add(raw.asItem())
		}
		TRContent.SmallDusts.values().each { smallDust ->
			getOrCreateTagBuilder(smallDust.asTag()).add(smallDust.asItem())
			getOrCreateTagBuilder(TRContent.SMALL_DUSTS_TAG).add(smallDust.asItem())
		}
		TRContent.Gems.values().each { gem ->
			getOrCreateTagBuilder(gem.asTag()).add(gem.asItem())
			getOrCreateTagBuilder(TRContent.GEMS_TAG).add(gem.asItem())
		}
		TRContent.Ingots.values().each { ingot ->
			getOrCreateTagBuilder(ingot.asTag()).add(ingot.asItem())
			getOrCreateTagBuilder(TRContent.INGOTS_TAG).add(ingot.asItem())
		}
		TRContent.Nuggets.values().each { nugget ->
			getOrCreateTagBuilder(nugget.asTag()).add(nugget.asItem())
			getOrCreateTagBuilder(TRContent.NUGGETS_TAG).add(nugget.asItem())
		}
		TRContent.Plates.values().each { plate ->
			getOrCreateTagBuilder(plate.asTag()).add(plate.asItem())
			getOrCreateTagBuilder(TRContent.PLATES_TAG).add(plate.asItem())
		}
	}
}
