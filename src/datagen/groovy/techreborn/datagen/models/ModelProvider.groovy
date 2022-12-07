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

package techreborn.datagen.models

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.family.BlockFamilies
import net.minecraft.registry.RegistryWrapper
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class ModelProvider extends FabricModelProvider {
	ModelProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output)
	}

	@Override
	void generateBlockStateModels(BlockStateModelGenerator generator) {
		generator.registerDoor(TRContent.RUBBER_DOOR)

		TRContent.Ores.values().each {
			generator.registerSimpleCubeAll(it.block)
		}

		TRContent.StorageBlocks.values().each {
			def family = BlockFamilies.register(it.block)
				.slab(it.slabBlock)
				.stairs(it.stairsBlock)
				.wall(it.wallBlock)
				.build()

			generator.registerCubeAllModelTexturePool(it.block).family(family)
		}
	}

	@Override
	void generateItemModels(ItemModelGenerator generator) {
		TRContent.Dusts.values().each {
			generator.register(it.asItem(), Models.GENERATED)
		}

		TRContent.SmallDusts.values().each {
			generator.register(it.asItem(), Models.GENERATED)
		}

		TRContent.Ingots.values().each {
			generator.register(it.asItem(), Models.GENERATED)
		}

		TRContent.Plates.values().each {
			generator.register(it.asItem(), Models.GENERATED)
		}

		TRContent.Nuggets.values().each {
			generator.register(it.asItem(), Models.GENERATED)
		}
	}
}
