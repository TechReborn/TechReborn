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
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class TRBlockTagProvider extends BlockTagProvider {

	TRBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup lookup) {
		valueLookupBuilder(TRContent.BlockTags.DRILL_MINEABLE)
			.addOptionalTag(BlockTags.PICKAXE_MINEABLE)
			.addOptionalTag(BlockTags.SHOVEL_MINEABLE)

		valueLookupBuilder(TRContent.BlockTags.JACKHAMMER_MINEABLE)
			.addOptionalTag(BlockTags.BASE_STONE_NETHER)
			.addOptionalTag(BlockTags.BASE_STONE_OVERWORLD)
			.addOptionalTag(BlockTags.DIRT)
			.addOptionalTag(BlockTags.ICE)
			.addOptionalTag(BlockTags.SNOW)
			.addOptionalTag(BlockTags.NYLIUM)
			.addOptionalTag(BlockTags.WART_BLOCKS)
			.addOptionalTag(TagKey.of(RegistryKeys.BLOCK, Identifier.of("c","stone")))
			.addOptional(Blocks.END_STONE)
			.addOptional(Blocks.SAND)
			.addOptional(Blocks.RED_SAND)
			.addOptional(Blocks.SANDSTONE)
			.addOptional(Blocks.RED_SANDSTONE)
			.addOptional(Blocks.GRAVEL)
			.addOptional(Blocks.CALCITE)
			.addOptional(Blocks.SNOW)
			.addOptional(Blocks.SOUL_SAND)
			.addOptional(Blocks.SOUL_SOIL)

		valueLookupBuilder(TRContent.BlockTags.OMNI_TOOL_MINEABLE)
			.addTag(TRContent.BlockTags.DRILL_MINEABLE)
			.addOptionalTag(BlockTags.AXE_MINEABLE)
		// TODO 1.20.5
//			.addOptionalTag(FabricMineableTags.SHEARS_MINEABLE.id())
//			.addOptionalTag(FabricMineableTags.SWORD_MINEABLE.id())

		valueLookupBuilder(BlockTags.HOE_MINEABLE)
			.add(TRContent.RUBBER_LEAVES)

		TRContent.Ores.values().each {
			valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(it.block)
		}

		TRContent.Ores.values().each {
			valueLookupBuilder(ConventionalBlockTags.ORES)
				.add(it.block)
		}

		TRContent.StorageBlocks.values().each {
			valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(it.block, it.stairsBlock, it.slabBlock, it.wallBlock)
		}

		TRContent.MachineBlocks.values().each {
			valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(it.casing)
		}

		valueLookupBuilder(BlockTags.FENCES)
			.add(TRContent.RUBBER_FENCE)
			.add(TRContent.REFINED_IRON_FENCE)

		valueLookupBuilder(BlockTags.GUARDED_BY_PIGLINS)
			.add(TRContent.StorageBlocks.ELECTRUM.block)

		valueLookupBuilder(BlockTags.LEAVES)
			.add(TRContent.RUBBER_LEAVES)

		valueLookupBuilder(TRContent.BlockTags.RUBBER_LOGS)
			.add(TRContent.RUBBER_LOG)
			.add(TRContent.RUBBER_LOG_STRIPPED)
			.add(TRContent.RUBBER_WOOD)
			.add(TRContent.STRIPPED_RUBBER_WOOD)

		valueLookupBuilder(BlockTags.LOGS_THAT_BURN)
			.addTag(TRContent.BlockTags.RUBBER_LOGS)

		valueLookupBuilder(BlockTags.PLANKS)
			.add(TRContent.RUBBER_PLANKS)

		valueLookupBuilder(BlockTags.SAPLINGS)
			.add(TRContent.RUBBER_SAPLING)

		valueLookupBuilder(BlockTags.SLABS)
			.add(TRContent.RUBBER_SLAB)

		TRContent.StorageBlocks.values().each {
			valueLookupBuilder(BlockTags.SLABS)
				.add(it.slabBlock)
		}

		valueLookupBuilder(BlockTags.STAIRS)
			.add(TRContent.RUBBER_STAIR)

		TRContent.StorageBlocks.values().each {
			valueLookupBuilder(BlockTags.STAIRS)
				.add(it.stairsBlock)
		}

		TRContent.StorageBlocks.values().each {
			valueLookupBuilder(BlockTags.WALLS)
				.add(it.wallBlock)
		}

		valueLookupBuilder(BlockTags.WALLS)
			.add(TRContent.COPPER_WALL)

		valueLookupBuilder(BlockTags.WOODEN_BUTTONS)
			.add(TRContent.RUBBER_BUTTON)

		valueLookupBuilder(BlockTags.WOODEN_DOORS)
			.add(TRContent.RUBBER_DOOR)

		valueLookupBuilder(BlockTags.WOODEN_FENCES)
			.add(TRContent.RUBBER_FENCE)

		valueLookupBuilder(BlockTags.WOODEN_PRESSURE_PLATES)
			.add(TRContent.RUBBER_PRESSURE_PLATE)

		valueLookupBuilder(BlockTags.WOODEN_SLABS)
			.add(TRContent.RUBBER_SLAB)

		valueLookupBuilder(BlockTags.WOODEN_STAIRS)
			.add(TRContent.RUBBER_STAIR)

		valueLookupBuilder(BlockTags.WOODEN_TRAPDOORS)
			.add(TRContent.RUBBER_TRAPDOOR)

		ModFluids.values().each {
			valueLookupBuilder(BlockTags.REPLACEABLE)
			.add(it.block)
		}

		valueLookupBuilder(TRContent.BlockTags.NONE_SOLID_COVERS)
			.addOptionalTag(TagKey.of(RegistryKeys.BLOCK, Identifier.of("ae2", "whitelisted/facades")))
			.forceAddTag(ConventionalBlockTags.GLASS_BLOCKS)
	}
}
