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

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.Identifier
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class TRBlockTagProvider extends FabricTagProvider.BlockTagProvider {

	TRBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup lookup) {
		getOrCreateTagBuilder(TRContent.BlockTags.DRILL_MINEABLE)
			.addOptionalTag(BlockTags.PICKAXE_MINEABLE.id())
			.addOptionalTag(BlockTags.SHOVEL_MINEABLE.id())

		getOrCreateTagBuilder(TRContent.BlockTags.JACKHAMMER_MINEABLE)
			.addOptionalTag(BlockTags.BASE_STONE_NETHER.id())
			.addOptionalTag(BlockTags.BASE_STONE_OVERWORLD.id())
			.addOptionalTag(BlockTags.DIRT.id())
			.addOptionalTag(BlockTags.ICE.id())
			.addOptionalTag(BlockTags.SNOW.id())
			.addOptionalTag(BlockTags.NYLIUM.id())
			.addOptionalTag(BlockTags.WART_BLOCKS.id())
			.addOptionalTag(new Identifier("c","stone"))
			.addOptional(new Identifier("minecraft", "end_stone"))
			.addOptional(new Identifier("minecraft", "sand"))
			.addOptional(new Identifier("minecraft", "red_sand"))
			.addOptional(new Identifier("minecraft", "sandstone"))
			.addOptional(new Identifier("minecraft", "red_sandstone"))
			.addOptional(new Identifier("minecraft", "gravel"))
			.addOptional(new Identifier("minecraft", "calcite"))
			.addOptional(new Identifier("minecraft", "snow"))
			.addOptional(new Identifier("minecraft", "soul_sand"))
			.addOptional(new Identifier("minecraft", "soul_soil"))

		getOrCreateTagBuilder(TRContent.BlockTags.OMNI_TOOL_MINEABLE)
			.addTag(TRContent.BlockTags.DRILL_MINEABLE)
			.addOptionalTag(BlockTags.AXE_MINEABLE.id())
			.addOptionalTag(FabricMineableTags.SHEARS_MINEABLE.id())
			.addOptionalTag(FabricMineableTags.SWORD_MINEABLE.id())

		getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
			.add(TRContent.RUBBER_LEAVES)

		TRContent.Ores.values().each {
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(it.block)
		}

		TRContent.Ores.values().each {
			getOrCreateTagBuilder(ConventionalBlockTags.ORES)
				.add(it.block)
		}

		TRContent.StorageBlocks.values().each {
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(it.block, it.stairsBlock, it.slabBlock, it.wallBlock)
		}

		TRContent.MachineBlocks.values().each {
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(it.casing)
		}

		getOrCreateTagBuilder(BlockTags.FENCES)
			.add(TRContent.RUBBER_FENCE)
			.add(TRContent.REFINED_IRON_FENCE)

		getOrCreateTagBuilder(BlockTags.GUARDED_BY_PIGLINS)
			.add(TRContent.StorageBlocks.ELECTRUM.block)

		getOrCreateTagBuilder(BlockTags.LEAVES)
			.add(TRContent.RUBBER_LEAVES)

		getOrCreateTagBuilder(TRContent.BlockTags.RUBBER_LOGS)
			.add(TRContent.RUBBER_LOG)
			.add(TRContent.RUBBER_LOG_STRIPPED)
			.add(TRContent.RUBBER_WOOD)
			.add(TRContent.STRIPPED_RUBBER_WOOD)

		getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
			.addTag(TRContent.BlockTags.RUBBER_LOGS)

		getOrCreateTagBuilder(BlockTags.PLANKS)
			.add(TRContent.RUBBER_PLANKS)

		getOrCreateTagBuilder(BlockTags.SAPLINGS)
			.add(TRContent.RUBBER_SAPLING)

		getOrCreateTagBuilder(BlockTags.SLABS)
			.add(TRContent.RUBBER_SLAB)

		TRContent.StorageBlocks.values().each {
			getOrCreateTagBuilder(BlockTags.SLABS)
				.add(it.slabBlock)
		}

		getOrCreateTagBuilder(BlockTags.STAIRS)
			.add(TRContent.RUBBER_STAIR)

		TRContent.StorageBlocks.values().each {
			getOrCreateTagBuilder(BlockTags.STAIRS)
				.add(it.stairsBlock)
		}

		TRContent.StorageBlocks.values().each {
			getOrCreateTagBuilder(BlockTags.WALLS)
				.add(it.wallBlock)
		}

		getOrCreateTagBuilder(BlockTags.WALLS)
			.add(TRContent.COPPER_WALL)

		getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
			.add(TRContent.RUBBER_BUTTON)

		getOrCreateTagBuilder(BlockTags.WOODEN_DOORS)
			.add(TRContent.RUBBER_DOOR)

		getOrCreateTagBuilder(BlockTags.WOODEN_FENCES)
			.add(TRContent.RUBBER_FENCE)

		getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES)
			.add(TRContent.RUBBER_PRESSURE_PLATE)

		getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
			.add(TRContent.RUBBER_SLAB)

		getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
			.add(TRContent.RUBBER_STAIR)

		getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS)
			.add(TRContent.RUBBER_TRAPDOOR)

		ModFluids.values().each {
			getOrCreateTagBuilder(BlockTags.REPLACEABLE)
			.add(it.block)
		}

		getOrCreateTagBuilder(TRContent.BlockTags.NONE_SOLID_COVERS)
			.addOptionalTag(new Identifier("ae2", "whitelisted/facades"))
			.forceAddTag(ConventionalBlockTags.GLASS_BLOCKS)
	}
}
