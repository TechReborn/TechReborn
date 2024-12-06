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
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import reborncore.common.misc.RebornCoreTags
import techreborn.datagen.TRConventionalTags
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class TRItemTagProvider extends ItemTagProvider {
	TRItemTagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, registriesFuture)
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg) {
		TRContent.Ores.values().each { ore ->
			getOrCreateTagBuilder(ore.asTag()).add(ore.asItem())
			getOrCreateTagBuilder(TRContent.ItemTags.ORES).add(ore.asItem())
		}
		TRContent.StorageBlocks.values().each { block ->
			getOrCreateTagBuilder(block.asTag()).add(block.asItem())
			getOrCreateTagBuilder(TRContent.ItemTags.STORAGE_BLOCK).add(block.asItem())
		}
		TRContent.Dusts.values().each { dust ->
			getOrCreateTagBuilder(dust.asTag()).add(dust.asItem())
			getOrCreateTagBuilder(TRContent.ItemTags.DUSTS).add(dust.asItem())
		}
		TRContent.RawMetals.values().each { raw ->
			getOrCreateTagBuilder(raw.asTag()).add(raw.asItem())
			getOrCreateTagBuilder(TRContent.ItemTags.RAW_METALS).add(raw.asItem())
		}
		TRContent.SmallDusts.values().each { smallDust ->
			getOrCreateTagBuilder(smallDust.asTag()).add(smallDust.asItem())
			getOrCreateTagBuilder(TRContent.ItemTags.SMALL_DUSTS).add(smallDust.asItem())
		}
		TRContent.Gems.values().each { gem ->
			getOrCreateTagBuilder(gem.asTag()).add(gem.asItem())
			getOrCreateTagBuilder(TRContent.ItemTags.GEMS).add(gem.asItem())
		}
		TRContent.Ingots.values().each { ingot ->
			getOrCreateTagBuilder(ingot.asTag()).add(ingot.asItem())
			getOrCreateTagBuilder(TRContent.ItemTags.INGOTS).add(ingot.asItem())
		}
		getOrCreateTagBuilder(ConventionalItemTags.INGOTS)
			.addTag(TRContent.ItemTags.INGOTS)
		TRContent.Nuggets.values().each { nugget ->
			getOrCreateTagBuilder(nugget.asTag()).add(nugget.asItem())
			getOrCreateTagBuilder(TRContent.ItemTags.NUGGETS).add(nugget.asItem())
		}
		TRContent.Plates.values().each { plate ->
			getOrCreateTagBuilder(plate.asTag()).add(plate.asItem())
			getOrCreateTagBuilder(TRContent.ItemTags.PLATES).add(plate.asItem())
		}
		TRContent.StorageUnit.values().each {unit ->
			getOrCreateTagBuilder(TRContent.ItemTags.STORAGE_UNITS).add(unit.asItem())
		}
		getOrCreateTagBuilder(TRContent.ItemTags.MINER_ACCEPTED_TOOLS)
			.add(TRContent.BASIC_DRILL.asItem())
			.add(TRContent.ADVANCED_DRILL.asItem())
			.add(TRContent.INDUSTRIAL_DRILL.asItem())
			.add(TRContent.BASIC_JACKHAMMER.asItem())
			.add(TRContent.ADVANCED_JACKHAMMER.asItem())
			.add(TRContent.INDUSTRIAL_JACKHAMMER.asItem())
			.add(TRContent.ROCK_CUTTER.asItem())

		getOrCreateTagBuilder(TRContent.ItemTags.RUBBER_LOGS)
			.add(TRContent.RUBBER_LOG.asItem())
			.add(TRContent.RUBBER_LOG_STRIPPED.asItem())
			.add(TRContent.RUBBER_WOOD.asItem())
			.add(TRContent.STRIPPED_RUBBER_WOOD.asItem())

		getOrCreateTagBuilder(TRContent.ItemTags.RUBBER_LOGS)
			.add(TRContent.RUBBER_LOG.asItem())

		getOrCreateTagBuilder(ItemTags.BEACON_PAYMENT_ITEMS)
			.addTag(TRContent.ItemTags.INGOTS)

		getOrCreateTagBuilder(ItemTags.CLUSTER_MAX_HARVESTABLES)
			.add(TRContent.BASIC_DRILL)
			.add(TRContent.ADVANCED_DRILL)
			.add(TRContent.INDUSTRIAL_DRILL)
			.add(TRContent.OMNI_TOOL)

		getOrCreateTagBuilder(ItemTags.FENCES)
			.add(TRContent.RUBBER_FENCE.asItem())
			.add(TRContent.REFINED_IRON_FENCE.asItem())

		getOrCreateTagBuilder(ItemTags.FREEZE_IMMUNE_WEARABLES)
			.add(TRContent.QUANTUM_BOOTS)
			.add(TRContent.QUANTUM_CHESTPLATE)
			.add(TRContent.QUANTUM_LEGGINGS)
			.add(TRContent.QUANTUM_BOOTS)

		getOrCreateTagBuilder(ItemTags.IGNORED_BY_PIGLIN_BABIES)
			.add(TRContent.Nuggets.ELECTRUM.asItem())
			.add(TRContent.Ingots.ELECTRUM.asItem())

		getOrCreateTagBuilder(ItemTags.LEAVES)
			.add(TRContent.RUBBER_LEAVES.asItem())

		getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
			.forceAddTag(TRContent.ItemTags.RUBBER_LOGS)

		getOrCreateTagBuilder(ItemTags.PIGLIN_LOVED)
			.add(TRContent.Plates.GOLD.asItem())
			.add(TRContent.Cables.GOLD.asItem())
			.add(TRContent.Cables.INSULATED_GOLD.asItem())
			.add(TRContent.Ingots.ELECTRUM.asItem())
			.add(TRContent.Plates.ELECTRUM.asItem())
			.add(TRContent.StorageBlocks.ELECTRUM.asItem())

		getOrCreateTagBuilder(ItemTags.PLANKS)
			.add(TRContent.RUBBER_PLANKS.asItem())

		getOrCreateTagBuilder(ItemTags.SAPLINGS)
			.add(TRContent.RUBBER_SAPLING.asItem())

		getOrCreateTagBuilder(ItemTags.SLABS)
			.add(TRContent.RUBBER_SLAB.asItem())

		getOrCreateTagBuilder(ItemTags.SLABS)
			.add(TRContent.RUBBER_SLAB.asItem())

		TRContent.StorageBlocks.values().each {
			getOrCreateTagBuilder(ItemTags.SLABS)
				.add(it.slabBlock.asItem())
		}

		getOrCreateTagBuilder(ItemTags.STAIRS)
			.add(TRContent.RUBBER_STAIR.asItem())

		TRContent.StorageBlocks.values().each {
			getOrCreateTagBuilder(ItemTags.STAIRS)
				.add(it.stairsBlock.asItem())
		}

		TRContent.StorageBlocks.values().each {
			getOrCreateTagBuilder(ItemTags.WALLS)
				.add(it.wallBlock.asItem())
		}

		getOrCreateTagBuilder(ItemTags.WALLS)
			.add(TRContent.COPPER_WALL.asItem())

		getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS)
			.add(TRContent.RUBBER_BUTTON.asItem())

		getOrCreateTagBuilder(ItemTags.WOODEN_DOORS)
			.add(TRContent.RUBBER_DOOR.asItem())

		getOrCreateTagBuilder(ItemTags.WOODEN_FENCES)
			.add(TRContent.RUBBER_FENCE.asItem())

		getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
			.add(TRContent.RUBBER_PRESSURE_PLATE.asItem())

		getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
			.add(TRContent.RUBBER_SLAB.asItem())

		getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
			.add(TRContent.RUBBER_STAIR.asItem())

		getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS)
			.add(TRContent.RUBBER_TRAPDOOR.asItem())

		getOrCreateTagBuilder(RebornCoreTags.WATER_EXPLOSION_ITEM)
			.add(ModFluids.SODIUM.getBucket())

		getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
			.add(TRContent.RED_CELL_BATTERY)
			.add(TRContent.LITHIUM_ION_BATTERY)
			.add(TRContent.ENERGY_CRYSTAL)
			.add(TRContent.LAPOTRON_CRYSTAL)
			.add(TRContent.LAPOTRONIC_ORB)
			.add(TRContent.BASIC_CHAINSAW)
			.add(TRContent.ADVANCED_CHAINSAW)
			.add(TRContent.INDUSTRIAL_CHAINSAW)
			.add(TRContent.BASIC_DRILL)
			.add(TRContent.ADVANCED_DRILL)
			.add(TRContent.INDUSTRIAL_DRILL)
			.add(TRContent.ELECTRIC_TREE_TAP)
			.add(TRContent.BASIC_JACKHAMMER)
			.add(TRContent.ADVANCED_JACKHAMMER)
			.add(TRContent.INDUSTRIAL_JACKHAMMER)
			.add(TRContent.ROCK_CUTTER)
			.add(TRContent.NANOSABER)
			.add(TRContent.OMNI_TOOL)
			.add(TRContent.LITHIUM_ION_BATPACK)
			.add(TRContent.LAPOTRONIC_ORBPACK)
			.add(TRContent.CLOAKING_DEVICE)
			.add(TRContent.NANO_HELMET)
			.add(TRContent.NANO_CHESTPLATE)
			.add(TRContent.NANO_LEGGINGS)
			.add(TRContent.NANO_BOOTS)
			.add(TRContent.QUANTUM_HELMET)
			.add(TRContent.QUANTUM_CHESTPLATE)
			.add(TRContent.QUANTUM_LEGGINGS)
			.add(TRContent.QUANTUM_BOOTS)

		getOrCreateTagBuilder(ItemTags.MINING_ENCHANTABLE)
			.add(TRContent.OMNI_TOOL)

		getOrCreateTagBuilder(ItemTags.MINING_LOOT_ENCHANTABLE)
			.add(TRContent.OMNI_TOOL)

		getOrCreateTagBuilder(ItemTags.SWORD_ENCHANTABLE)
			.add(TRContent.OMNI_TOOL)

		getOrCreateTagBuilder(TRConventionalTags.FROGLIGHTS)
			.add(Items.OCHRE_FROGLIGHT)
			.add(Items.VERDANT_FROGLIGHT)
			.add(Items.PEARLESCENT_FROGLIGHT)
	}
}
