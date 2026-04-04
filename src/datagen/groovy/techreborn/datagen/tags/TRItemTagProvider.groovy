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
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import reborncore.common.misc.RebornCoreTags
import techreborn.datagen.TRConventionalTags
import techreborn.init.ModFluids
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture

class TRItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
	TRItemTagProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(dataOutput, registriesFuture)
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		TRContent.Ores.values().each { ore ->
			valueLookupBuilder(ore.asTag()).add(ore.asItem())
			valueLookupBuilder(TRContent.ItemTags.ORES).add(ore.asItem())
		}
		TRContent.StorageBlocks.values().each { block ->
			valueLookupBuilder(block.asTag()).add(block.asItem())
			valueLookupBuilder(TRContent.ItemTags.STORAGE_BLOCK).add(block.asItem())
		}
		TRContent.Dusts.values().each { dust ->
			valueLookupBuilder(dust.asTag()).add(dust.asItem())
			valueLookupBuilder(TRContent.ItemTags.DUSTS).add(dust.asItem())
		}
		TRContent.RawMetals.values().each { raw ->
			valueLookupBuilder(raw.asTag()).add(raw.asItem())
			valueLookupBuilder(TRContent.ItemTags.RAW_METALS).add(raw.asItem())
		}
		TRContent.SmallDusts.values().each { smallDust ->
			valueLookupBuilder(smallDust.asTag()).add(smallDust.asItem())
			valueLookupBuilder(TRContent.ItemTags.SMALL_DUSTS).add(smallDust.asItem())
		}
		TRContent.Gems.values().each { gem ->
			valueLookupBuilder(gem.asTag()).add(gem.asItem())
			valueLookupBuilder(TRContent.ItemTags.GEMS).add(gem.asItem())
		}
		TRContent.Ingots.values().each { ingot ->
			valueLookupBuilder(ingot.asTag()).add(ingot.asItem())
			valueLookupBuilder(TRContent.ItemTags.INGOTS).add(ingot.asItem())
		}
		valueLookupBuilder(ConventionalItemTags.INGOTS)
			.addTag(TRContent.ItemTags.INGOTS)
		TRContent.Nuggets.values().each { nugget ->
			valueLookupBuilder(nugget.asTag()).add(nugget.asItem())
			valueLookupBuilder(TRContent.ItemTags.NUGGETS).add(nugget.asItem())
		}
		TRContent.Plates.values().each { plate ->
			valueLookupBuilder(plate.asTag()).add(plate.asItem())
			valueLookupBuilder(TRContent.ItemTags.PLATES).add(plate.asItem())
		}
		TRContent.StorageUnit.values().each {unit ->
			valueLookupBuilder(TRContent.ItemTags.STORAGE_UNITS).add(unit.asItem())
		}

		valueLookupBuilder(TRContent.ItemTags.RUBBER_LOGS)
			.add(TRContent.RUBBER_LOG.asItem())
			.add(TRContent.RUBBER_LOG_STRIPPED.asItem())
			.add(TRContent.RUBBER_WOOD.asItem())
			.add(TRContent.STRIPPED_RUBBER_WOOD.asItem())

		valueLookupBuilder(TRContent.ItemTags.BRONZE_TOOL_MATERIALS)
			.addTag(TRContent.Ingots.BRONZE.asTag())
		valueLookupBuilder(TRContent.ItemTags.RUBY_TOOL_MATERIALS)
			.addTag(TRContent.Gems.RUBY.asTag())
		valueLookupBuilder(TRContent.ItemTags.SAPPHIRE_TOOL_MATERIALS)
			.addTag(TRContent.Gems.SAPPHIRE.asTag())
		valueLookupBuilder(TRContent.ItemTags.PERIDOT_TOOL_MATERIALS)
			.addTag(TRContent.Gems.PERIDOT.asTag())

		valueLookupBuilder(TRConventionalTags.TUFF)
			.add(Items.TUFF)
			.add(Items.CHISELED_TUFF)
			.add(Items.TUFF_BRICKS)
			.add(Items.POLISHED_TUFF)
			.add(Items.CHISELED_TUFF_BRICKS)

		valueLookupBuilder(ItemTags.BEACON_PAYMENT_ITEMS)
			.addTag(TRContent.ItemTags.INGOTS)

		valueLookupBuilder(ItemTags.CLUSTER_MAX_HARVESTABLES)
			.add(TRContent.BASIC_DRILL)
			.add(TRContent.ADVANCED_DRILL)
			.add(TRContent.INDUSTRIAL_DRILL)
			.add(TRContent.OMNI_TOOL)

		valueLookupBuilder(ItemTags.FENCES)
			.add(TRContent.RUBBER_FENCE.asItem())
			.add(TRContent.REFINED_IRON_FENCE.asItem())

		valueLookupBuilder(ItemTags.FREEZE_IMMUNE_WEARABLES)
			.add(TRContent.QUANTUM_BOOTS)
			.add(TRContent.QUANTUM_CHESTPLATE)
			.add(TRContent.QUANTUM_LEGGINGS)
			.add(TRContent.QUANTUM_BOOTS)

		valueLookupBuilder(ItemTags.IGNORED_BY_PIGLIN_BABIES)
			.add(TRContent.Nuggets.ELECTRUM.asItem())
			.add(TRContent.Ingots.ELECTRUM.asItem())

		valueLookupBuilder(ItemTags.LEAVES)
			.add(TRContent.RUBBER_LEAVES.asItem())

		valueLookupBuilder(ItemTags.LOGS_THAT_BURN)
			.forceAddTag(TRContent.ItemTags.RUBBER_LOGS)

		valueLookupBuilder(ItemTags.PIGLIN_LOVED)
			.add(TRContent.Plates.GOLD.asItem())
			.add(TRContent.Cables.GOLD.asItem())
			.add(TRContent.Cables.INSULATED_GOLD.asItem())
			.add(TRContent.Ingots.ELECTRUM.asItem())
			.add(TRContent.Plates.ELECTRUM.asItem())
			.add(TRContent.StorageBlocks.ELECTRUM.asItem())

		valueLookupBuilder(ItemTags.PLANKS)
			.add(TRContent.RUBBER_PLANKS.asItem())

		valueLookupBuilder(ItemTags.SAPLINGS)
			.add(TRContent.RUBBER_SAPLING.asItem())

		valueLookupBuilder(ItemTags.SLABS)
			.add(TRContent.RUBBER_SLAB.asItem())

		valueLookupBuilder(ItemTags.SLABS)
			.add(TRContent.RUBBER_SLAB.asItem())

		TRContent.StorageBlocks.values().each {
			valueLookupBuilder(ItemTags.SLABS)
				.add(it.slabBlock.asItem())
		}

		valueLookupBuilder(ItemTags.STAIRS)
			.add(TRContent.RUBBER_STAIR.asItem())

		TRContent.StorageBlocks.values().each {
			valueLookupBuilder(ItemTags.STAIRS)
				.add(it.stairsBlock.asItem())
		}

		TRContent.StorageBlocks.values().each {
			valueLookupBuilder(ItemTags.WALLS)
				.add(it.wallBlock.asItem())
		}

		valueLookupBuilder(ItemTags.WALLS)
			.add(TRContent.COPPER_WALL.asItem())

		valueLookupBuilder(ItemTags.WOODEN_BUTTONS)
			.add(TRContent.RUBBER_BUTTON.asItem())

		valueLookupBuilder(ItemTags.WOODEN_DOORS)
			.add(TRContent.RUBBER_DOOR.asItem())

		valueLookupBuilder(ItemTags.WOODEN_FENCES)
			.add(TRContent.RUBBER_FENCE.asItem())

		valueLookupBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
			.add(TRContent.RUBBER_PRESSURE_PLATE.asItem())

		valueLookupBuilder(ItemTags.WOODEN_SLABS)
			.add(TRContent.RUBBER_SLAB.asItem())

		valueLookupBuilder(ItemTags.WOODEN_STAIRS)
			.add(TRContent.RUBBER_STAIR.asItem())

		valueLookupBuilder(ItemTags.WOODEN_TRAPDOORS)
			.add(TRContent.RUBBER_TRAPDOOR.asItem())

		valueLookupBuilder(RebornCoreTags.WATER_EXPLOSION_ITEM)
			.add(ModFluids.SODIUM.getBucket())

		valueLookupBuilder(ItemTags.DURABILITY_ENCHANTABLE)
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

		valueLookupBuilder(ItemTags.MINING_ENCHANTABLE)
			.add(TRContent.OMNI_TOOL)

		valueLookupBuilder(ItemTags.MINING_LOOT_ENCHANTABLE)
			.add(TRContent.OMNI_TOOL)

		valueLookupBuilder(ItemTags.WEAPON_ENCHANTABLE)
			.add(TRContent.OMNI_TOOL)

		valueLookupBuilder(TRConventionalTags.FROGLIGHTS)
			.add(Items.OCHRE_FROGLIGHT)
			.add(Items.VERDANT_FROGLIGHT)
			.add(Items.PEARLESCENT_FROGLIGHT)

		valueLookupBuilder(TRContent.ItemTags.TRIM_TEMPLATES)
			.add(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE)
			.add(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE)

		valueLookupBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE)
			.add(TRContent.BRONZE_HELMET)
			.add(TRContent.RUBY_HELMET)
			.add(TRContent.SAPPHIRE_HELMET)
			.add(TRContent.PERIDOT_HELMET)
			.add(TRContent.SILVER_HELMET)
			.add(TRContent.STEEL_HELMET)
			.add(TRContent.NANO_HELMET)
			.add(TRContent.QUANTUM_HELMET)

		valueLookupBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
			.add(TRContent.BRONZE_CHESTPLATE)
			.add(TRContent.RUBY_CHESTPLATE)
			.add(TRContent.SAPPHIRE_CHESTPLATE)
			.add(TRContent.PERIDOT_CHESTPLATE)
			.add(TRContent.SILVER_CHESTPLATE)
			.add(TRContent.STEEL_CHESTPLATE)
			.add(TRContent.NANO_CHESTPLATE)
			.add(TRContent.QUANTUM_CHESTPLATE)

		valueLookupBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE)
			.add(TRContent.BRONZE_LEGGINGS)
			.add(TRContent.RUBY_LEGGINGS)
			.add(TRContent.SAPPHIRE_LEGGINGS)
			.add(TRContent.PERIDOT_LEGGINGS)
			.add(TRContent.SILVER_LEGGINGS)
			.add(TRContent.STEEL_LEGGINGS)
			.add(TRContent.NANO_LEGGINGS)
			.add(TRContent.QUANTUM_LEGGINGS)

		valueLookupBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
			.add(TRContent.BRONZE_BOOTS)
			.add(TRContent.RUBY_BOOTS)
			.add(TRContent.SAPPHIRE_BOOTS)
			.add(TRContent.PERIDOT_BOOTS)
			.add(TRContent.SILVER_BOOTS)
			.add(TRContent.STEEL_BOOTS)
			.add(TRContent.NANO_BOOTS)
			.add(TRContent.QUANTUM_BOOTS)
	}
}
