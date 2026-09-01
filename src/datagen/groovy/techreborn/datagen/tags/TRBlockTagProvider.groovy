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

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider.BlockTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.tags.BlockItemTagAppender
import net.minecraft.tags.BlockItemTags
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class TRBlockTagProvider extends BlockTagsProvider {
	private static final TagKey<Block> COMMON_ORES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"))
	private static final TagKey<Block> COMMON_GLASS_BLOCKS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "glass_blocks"))

	TRBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture)
	}

	protected BlockAppender builder(TagKey<Block> tag) {
		return new BlockAppender(super.builder(tag))
	}

	private static ResourceKey<Block> key(Block block) {
		return block.builtInRegistryHolder().key()
	}

	private static final class BlockAppender extends BlockItemTagAppender<Block> {
		BlockAppender(BlockItemTagAppender<Block> original) {
			super(original)
		}

		@Override
		protected ResourceKey<Block> convertElement(net.minecraft.references.BlockItemId element) {
			return element.block()
		}

		BlockAppender add(Block... blocks) {
			blocks.each { add(TRBlockTagProvider.key(it)) }
			return this
		}

		BlockAppender addOptional(Block block) {
			addOptional(TRBlockTagProvider.key(block))
			return this
		}

		BlockAppender forceAddTag(TagKey<Block> tag) {
			addOptionalTag(tag)
			return this
		}
	}

	@Override
	protected void addTags(HolderLookup.Provider lookup) {
		builder(TRContent.BlockTags.DRILL_MINEABLE)
			.addOptionalTag(BlockTags.MINEABLE_WITH_PICKAXE)
			.addOptionalTag(BlockTags.MINEABLE_WITH_SHOVEL)

		builder(TRContent.BlockTags.JACKHAMMER_MINEABLE)
			.addOptionalTag(BlockTags.BASE_STONE_NETHER)
			.addOptionalTag(BlockTags.BASE_STONE_OVERWORLD)
			.addOptionalTag(BlockTags.DIRT)
			.addOptionalTag(BlockTags.ICE)
			.addOptionalTag(BlockTags.SNOW)
			.addOptionalTag(BlockTags.NYLIUM)
			.addOptionalTag(BlockTags.WART_BLOCKS)
			.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c","stone")))
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

		builder(TRContent.BlockTags.OMNI_TOOL_MINEABLE)
			.addTag(TRContent.BlockTags.DRILL_MINEABLE)
			.addOptionalTag(BlockTags.MINEABLE_WITH_AXE)
		// TODO 1.20.5
//			.addOptionalTag(FabricMineableTags.SHEARS_MINEABLE.id())
//			.addOptionalTag(FabricMineableTags.SWORD_MINEABLE.id())

		builder(BlockTags.MINEABLE_WITH_HOE)
			.add(TRContent.RUBBER_LEAVES)

		TRContent.Ores.values().each {
			builder(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(it.block)
		}

		TRContent.Ores.values().each {
			builder(COMMON_ORES)
				.add(it.block)
		}

		TRContent.StorageBlocks.values().each {
			builder(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(it.block, it.stairsBlock, it.slabBlock, it.wallBlock)
		}

		TRContent.MachineBlocks.values().each {
			builder(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(it.casing)
		}

		builder(BlockTags.FENCES)
			.add(TRContent.RUBBER_FENCE)
			.add(TRContent.REFINED_IRON_FENCE)

		builder(BlockTags.GUARDED_BY_PIGLINS)
			.add(TRContent.StorageBlocks.ELECTRUM.block)

		builder(BlockTags.LEAVES)
			.add(TRContent.RUBBER_LEAVES)

		builder(TRContent.BlockTags.RUBBER_LOGS)
			.add(TRContent.RUBBER_LOG)
			.add(TRContent.RUBBER_LOG_STRIPPED)
			.add(TRContent.RUBBER_WOOD)
			.add(TRContent.STRIPPED_RUBBER_WOOD)

		builder(BlockItemTags.LOGS_THAT_BURN.block())
			.addTag(TRContent.BlockTags.RUBBER_LOGS)

		builder(BlockTags.PLANKS)
			.add(TRContent.RUBBER_PLANKS)

		builder(BlockItemTags.SAPLINGS.block())
			.add(TRContent.RUBBER_SAPLING)

		builder(BlockTags.SLABS)
			.add(TRContent.RUBBER_SLAB)

		TRContent.StorageBlocks.values().each {
			builder(BlockTags.SLABS)
				.add(it.slabBlock)
		}

		builder(BlockTags.STAIRS)
			.add(TRContent.RUBBER_STAIR)

		TRContent.StorageBlocks.values().each {
			builder(BlockTags.STAIRS)
				.add(it.stairsBlock)
		}

		TRContent.StorageBlocks.values().each {
			builder(BlockTags.WALLS)
				.add(it.wallBlock)
		}

		builder(BlockTags.WALLS)
			.add(TRContent.COPPER_WALL)

		builder(BlockTags.WOODEN_BUTTONS)
			.add(TRContent.RUBBER_BUTTON)

		builder(BlockTags.WOODEN_DOORS)
			.add(TRContent.RUBBER_DOOR)

		builder(BlockTags.WOODEN_FENCES)
			.add(TRContent.RUBBER_FENCE)

		builder(BlockTags.WOODEN_PRESSURE_PLATES)
			.add(TRContent.RUBBER_PRESSURE_PLATE)

		builder(BlockTags.WOODEN_SLABS)
			.add(TRContent.RUBBER_SLAB)

		builder(BlockTags.WOODEN_STAIRS)
			.add(TRContent.RUBBER_STAIR)

		builder(BlockTags.WOODEN_TRAPDOORS)
			.add(TRContent.RUBBER_TRAPDOOR)

		ModFluids.values().each {
			builder(BlockTags.REPLACEABLE)
			.add(it.block)
		}

		builder(TRContent.BlockTags.NONE_SOLID_COVERS)
			.addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("ae2", "whitelisted/facades")))
			.forceAddTag(COMMON_GLASS_BLOCKS)
	}
}
