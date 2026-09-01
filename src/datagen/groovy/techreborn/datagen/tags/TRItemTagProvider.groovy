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
import net.minecraft.core.registries.Registries
import net.minecraft.data.tags.BlockItemTagAppender
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockItemTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.ItemLike
import net.minecraft.world.item.Item
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

	protected ItemAppender builder(TagKey<Item> tag) {
		return new ItemAppender(super.builder(tag))
	}

	private static ResourceKey<Item> key(ItemLike item) {
		return item.asItem().builtInRegistryHolder().key()
	}

	private static final class ItemAppender extends BlockItemTagAppender<Item> {
		ItemAppender(BlockItemTagAppender<Item> original) {
			super(original)
		}

		@Override
		protected ResourceKey<Item> convertElement(net.minecraft.references.BlockItemId element) {
			return element.item()
		}

		ItemAppender add(ItemLike... items) {
			items.each { add(TRItemTagProvider.key(it)) }
			return this
		}

		ItemAppender forceAddTag(TagKey<Item> tag) {
			addOptionalTag(tag)
			return this
		}
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		addConventionalTags()
		addToolTags()
		addMachineMaterialTags()

		TRContent.Ores.values().each { ore ->
			builder(ore.asTag()).add(ore.asItem())
			builder(TRContent.ItemTags.ORES).add(ore.asItem())
		}
		TRContent.StorageBlocks.values().each { block ->
			builder(block.asTag()).add(block.asItem())
			builder(TRContent.ItemTags.STORAGE_BLOCK).add(block.asItem())
		}
		TRContent.Dusts.values().each { dust ->
			builder(dust.asTag()).add(dust.asItem())
			builder(TRContent.ItemTags.DUSTS).add(dust.asItem())
		}
		TRContent.RawMetals.values().each { raw ->
			builder(raw.asTag()).add(raw.asItem())
			builder(TRContent.ItemTags.RAW_METALS).add(raw.asItem())
		}
		TRContent.SmallDusts.values().each { smallDust ->
			builder(smallDust.asTag()).add(smallDust.asItem())
			builder(TRContent.ItemTags.SMALL_DUSTS).add(smallDust.asItem())
		}
		TRContent.Gems.values().each { gem ->
			builder(gem.asTag()).add(gem.asItem())
			builder(TRContent.ItemTags.GEMS).add(gem.asItem())
		}
		TRContent.Ingots.values().each { ingot ->
			builder(ingot.asTag()).add(ingot.asItem())
			builder(TRContent.ItemTags.INGOTS).add(ingot.asItem())
		}
		builder(ConventionalItemTags.INGOTS)
			.addTag(TRContent.ItemTags.INGOTS)
		TRContent.Nuggets.values().each { nugget ->
			builder(nugget.asTag()).add(nugget.asItem())
			builder(TRContent.ItemTags.NUGGETS).add(nugget.asItem())
		}
		TRContent.Plates.values().each { plate ->
			builder(plate.asTag()).add(plate.asItem())
			builder(TRContent.ItemTags.PLATES).add(plate.asItem())
		}
		TRContent.StorageUnit.values().each {unit ->
			builder(TRContent.ItemTags.STORAGE_UNITS).add(unit.asItem())
		}

		builder(TRContent.ItemTags.RUBBER_LOGS)
			.add(TRContent.RUBBER_LOG.asItem())
			.add(TRContent.RUBBER_LOG_STRIPPED.asItem())
			.add(TRContent.RUBBER_WOOD.asItem())
			.add(TRContent.STRIPPED_RUBBER_WOOD.asItem())

		builder(TRContent.ItemTags.BRONZE_TOOL_MATERIALS)
			.addTag(TRContent.Ingots.BRONZE.asTag())
		builder(TRContent.ItemTags.RUBY_TOOL_MATERIALS)
			.addTag(TRContent.Gems.RUBY.asTag())
		builder(TRContent.ItemTags.SAPPHIRE_TOOL_MATERIALS)
			.addTag(TRContent.Gems.SAPPHIRE.asTag())
		builder(TRContent.ItemTags.PERIDOT_TOOL_MATERIALS)
			.addTag(TRContent.Gems.PERIDOT.asTag())

		builder(TRConventionalTags.TUFF)
			.add(Items.TUFF)
			.add(Items.CHISELED_TUFF)
			.add(Items.TUFF_BRICKS)
			.add(Items.POLISHED_TUFF)
			.add(Items.CHISELED_TUFF_BRICKS)

		builder(ItemTags.BEACON_PAYMENT_ITEMS)
			.addTag(TRContent.ItemTags.INGOTS)

		builder(ItemTags.CLUSTER_MAX_HARVESTABLES)
			.add(TRContent.BASIC_DRILL)
			.add(TRContent.ADVANCED_DRILL)
			.add(TRContent.INDUSTRIAL_DRILL)
			.add(TRContent.OMNI_TOOL)

		builder(BlockItemTags.FENCES.item())
			.add(TRContent.RUBBER_FENCE.asItem())
			.add(TRContent.REFINED_IRON_FENCE.asItem())

		builder(ItemTags.FREEZE_IMMUNE_WEARABLES)
			.add(TRContent.QUANTUM_BOOTS)
			.add(TRContent.QUANTUM_CHESTPLATE)
			.add(TRContent.QUANTUM_LEGGINGS)
			.add(TRContent.QUANTUM_BOOTS)

		builder(ItemTags.IGNORED_BY_PIGLIN_BABIES)
			.add(TRContent.Nuggets.ELECTRUM.asItem())
			.add(TRContent.Ingots.ELECTRUM.asItem())

		builder(ItemTags.LEAVES)
			.add(TRContent.RUBBER_LEAVES.asItem())

		builder(ItemTags.LOGS_THAT_BURN)
			.forceAddTag(TRContent.ItemTags.RUBBER_LOGS)

		builder(ItemTags.PIGLIN_LOVED)
			.add(TRContent.Plates.GOLD.asItem())
			.add(TRContent.Cables.GOLD.asItem())
			.add(TRContent.Cables.INSULATED_GOLD.asItem())
			.add(TRContent.Ingots.ELECTRUM.asItem())
			.add(TRContent.Plates.ELECTRUM.asItem())
			.add(TRContent.StorageBlocks.ELECTRUM.asItem())

		builder(ItemTags.PLANKS)
			.add(TRContent.RUBBER_PLANKS.asItem())

		builder(ItemTags.SAPLINGS)
			.add(TRContent.RUBBER_SAPLING.asItem())

		builder(BlockItemTags.SLABS.item())
			.add(TRContent.RUBBER_SLAB.asItem())

		builder(BlockItemTags.SLABS.item())
			.add(TRContent.RUBBER_SLAB.asItem())

		TRContent.StorageBlocks.values().each {
			builder(BlockItemTags.SLABS.item())
				.add(it.slabBlock.asItem())
		}

		builder(BlockItemTags.STAIRS.item())
			.add(TRContent.RUBBER_STAIR.asItem())

		TRContent.StorageBlocks.values().each {
			builder(BlockItemTags.STAIRS.item())
				.add(it.stairsBlock.asItem())
		}

		TRContent.StorageBlocks.values().each {
			builder(ItemTags.WALLS)
				.add(it.wallBlock.asItem())
		}

		builder(ItemTags.WALLS)
			.add(TRContent.COPPER_WALL.asItem())

		builder(ItemTags.WOODEN_BUTTONS)
			.add(TRContent.RUBBER_BUTTON.asItem())

		builder(ItemTags.WOODEN_DOORS)
			.add(TRContent.RUBBER_DOOR.asItem())

		builder(ItemTags.WOODEN_FENCES)
			.add(TRContent.RUBBER_FENCE.asItem())

		builder(ItemTags.WOODEN_PRESSURE_PLATES)
			.add(TRContent.RUBBER_PRESSURE_PLATE.asItem())

		builder(ItemTags.WOODEN_SLABS)
			.add(TRContent.RUBBER_SLAB.asItem())

		builder(ItemTags.WOODEN_STAIRS)
			.add(TRContent.RUBBER_STAIR.asItem())

		builder(ItemTags.WOODEN_TRAPDOORS)
			.add(TRContent.RUBBER_TRAPDOOR.asItem())

		builder(RebornCoreTags.WATER_EXPLOSION_ITEM)
			.add(ModFluids.SODIUM.getBucket())

		builder(ItemTags.DURABILITY_ENCHANTABLE)
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

		builder(ItemTags.MINING_ENCHANTABLE)
			.add(TRContent.OMNI_TOOL)

		builder(ItemTags.MINING_LOOT_ENCHANTABLE)
			.add(TRContent.OMNI_TOOL)

		builder(ItemTags.WEAPON_ENCHANTABLE)
			.add(TRContent.OMNI_TOOL)

		builder(TRConventionalTags.FROGLIGHTS)
			.add(Items.OCHRE_FROGLIGHT)
			.add(Items.VERDANT_FROGLIGHT)
			.add(Items.PEARLESCENT_FROGLIGHT)

		builder(TRContent.ItemTags.TRIM_TEMPLATES)
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

		builder(ItemTags.HEAD_ARMOR_ENCHANTABLE)
			.add(TRContent.BRONZE_HELMET)
			.add(TRContent.RUBY_HELMET)
			.add(TRContent.SAPPHIRE_HELMET)
			.add(TRContent.PERIDOT_HELMET)
			.add(TRContent.SILVER_HELMET)
			.add(TRContent.STEEL_HELMET)
			.add(TRContent.NANO_HELMET)
			.add(TRContent.QUANTUM_HELMET)

		builder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
			.add(TRContent.BRONZE_CHESTPLATE)
			.add(TRContent.RUBY_CHESTPLATE)
			.add(TRContent.SAPPHIRE_CHESTPLATE)
			.add(TRContent.PERIDOT_CHESTPLATE)
			.add(TRContent.SILVER_CHESTPLATE)
			.add(TRContent.STEEL_CHESTPLATE)
			.add(TRContent.NANO_CHESTPLATE)
			.add(TRContent.QUANTUM_CHESTPLATE)

		builder(ItemTags.LEG_ARMOR_ENCHANTABLE)
			.add(TRContent.BRONZE_LEGGINGS)
			.add(TRContent.RUBY_LEGGINGS)
			.add(TRContent.SAPPHIRE_LEGGINGS)
			.add(TRContent.PERIDOT_LEGGINGS)
			.add(TRContent.SILVER_LEGGINGS)
			.add(TRContent.STEEL_LEGGINGS)
			.add(TRContent.NANO_LEGGINGS)
			.add(TRContent.QUANTUM_LEGGINGS)

		builder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
			.add(TRContent.BRONZE_BOOTS)
			.add(TRContent.RUBY_BOOTS)
			.add(TRContent.SAPPHIRE_BOOTS)
			.add(TRContent.PERIDOT_BOOTS)
			.add(TRContent.SILVER_BOOTS)
			.add(TRContent.STEEL_BOOTS)
			.add(TRContent.NANO_BOOTS)
			.add(TRContent.QUANTUM_BOOTS)
	}

	private void addConventionalTags() {
		builder(TRConventionalTags.BARRELS_WOODEN).add(Items.BARREL)
		builder(TRConventionalTags.BASALT).add(Items.BASALT, Items.POLISHED_BASALT, Items.SMOOTH_BASALT)

		builder(TRConventionalTags.LIVING_CORAL_BLOCKS)
			.add(Items.TUBE_CORAL_BLOCK, Items.BRAIN_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK, Items.FIRE_CORAL_BLOCK, Items.HORN_CORAL_BLOCK)
		builder(TRConventionalTags.DEAD_CORAL_BLOCKS)
			.add(Items.DEAD_TUBE_CORAL_BLOCK, Items.DEAD_BRAIN_CORAL_BLOCK, Items.DEAD_BUBBLE_CORAL_BLOCK, Items.DEAD_FIRE_CORAL_BLOCK, Items.DEAD_HORN_CORAL_BLOCK)
		builder(TRConventionalTags.CORAL_BLOCKS)
			.addTag(TRConventionalTags.LIVING_CORAL_BLOCKS)
			.addTag(TRConventionalTags.DEAD_CORAL_BLOCKS)

		builder(TRConventionalTags.LIVING_CORAL_FANS)
			.add(Items.TUBE_CORAL_FAN, Items.BRAIN_CORAL_FAN, Items.BUBBLE_CORAL_FAN, Items.FIRE_CORAL_FAN, Items.HORN_CORAL_FAN)
		builder(TRConventionalTags.DEAD_CORAL_FANS)
			.add(Items.DEAD_TUBE_CORAL_FAN, Items.DEAD_BRAIN_CORAL_FAN, Items.DEAD_BUBBLE_CORAL_FAN, Items.DEAD_FIRE_CORAL_FAN, Items.DEAD_HORN_CORAL_FAN)
		builder(TRConventionalTags.CORAL_FANS)
			.addTag(TRConventionalTags.LIVING_CORAL_FANS)
			.addTag(TRConventionalTags.DEAD_CORAL_FANS)

		builder(TRConventionalTags.LIVING_CORAL_PLANTS)
			.add(Items.TUBE_CORAL, Items.BRAIN_CORAL, Items.BUBBLE_CORAL, Items.FIRE_CORAL, Items.HORN_CORAL)
		builder(TRConventionalTags.DEAD_CORAL_PLANTS)
			.add(Items.DEAD_TUBE_CORAL, Items.DEAD_BRAIN_CORAL, Items.DEAD_BUBBLE_CORAL, Items.DEAD_FIRE_CORAL, Items.DEAD_HORN_CORAL)
		builder(TRConventionalTags.CORAL_PLANTS)
			.addTag(TRConventionalTags.LIVING_CORAL_PLANTS)
			.addTag(TRConventionalTags.DEAD_CORAL_PLANTS)

		builder(TRContent.Dusts.COAL.asTag()).add(TRContent.Dusts.CHARCOAL)
		builder(TRConventionalTags.COOKED_MEAT)
			.add(Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_COD, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT, Items.COOKED_SALMON)
		builder(TRConventionalTags.RAW_MEAT)
			.add(Items.BEEF, Items.CHICKEN, Items.COD, Items.MUTTON, Items.PORKCHOP, Items.RABBIT, Items.SALMON)
		builder(TRConventionalTags.GRASS_VARIANTS)
			.add(Items.SHORT_GRASS, Items.TALL_GRASS, Items.FERN, Items.LARGE_FERN, Items.MOSS_CARPET, Items.SEAGRASS)

		[
			coal: [Items.COAL_ORE, Items.DEEPSLATE_COAL_ORE],
			copper: [Items.COPPER_ORE, Items.DEEPSLATE_COPPER_ORE],
			diamond: [Items.DIAMOND_ORE, Items.DEEPSLATE_DIAMOND_ORE],
			emerald: [Items.EMERALD_ORE, Items.DEEPSLATE_EMERALD_ORE],
			gold: [Items.GOLD_ORE, Items.DEEPSLATE_GOLD_ORE],
			iron: [Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE],
			lapis: [Items.LAPIS_ORE, Items.DEEPSLATE_LAPIS_ORE],
			redstone: [Items.REDSTONE_ORE, Items.DEEPSLATE_REDSTONE_ORE],
		].each { material, ores -> builder(itemTag('c', "ores/$material")).add(*ores) }

		builder(TRConventionalTags.PLANKS_THAT_BURN).add(TRContent.RUBBER_PLANKS)
		builder(TRConventionalTags.SMALL_CORALS_BLUE).add(Items.TUBE_CORAL, Items.TUBE_CORAL_FAN)
		builder(TRConventionalTags.SMALL_CORALS_DEAD)
			.addTag(TRConventionalTags.DEAD_CORAL_PLANTS)
			.addTag(TRConventionalTags.DEAD_CORAL_FANS)
		builder(TRConventionalTags.SMALL_CORALS_PINK).add(Items.BRAIN_CORAL, Items.BRAIN_CORAL_FAN)
		builder(TRConventionalTags.SMALL_CORALS_PURPLE).add(Items.BUBBLE_CORAL, Items.BUBBLE_CORAL_FAN)
		builder(TRConventionalTags.SMALL_CORALS_RED).add(Items.FIRE_CORAL, Items.FIRE_CORAL_FAN)
		builder(TRConventionalTags.SMALL_CORALS_YELLOW).add(Items.HORN_CORAL, Items.HORN_CORAL_FAN)
		builder(TRConventionalTags.SPONGES).add(Items.SPONGE, Items.WET_SPONGE)
		builder(TRConventionalTags.WORKBENCH).add(Items.CRAFTING_TABLE)
	}

	private void addToolTags() {
		builder(ItemTags.AXES)
			.add(TRContent.BRONZE_AXE, TRContent.RUBY_AXE, TRContent.SAPPHIRE_AXE, TRContent.PERIDOT_AXE,
				TRContent.BASIC_CHAINSAW, TRContent.ADVANCED_CHAINSAW, TRContent.INDUSTRIAL_CHAINSAW, TRContent.OMNI_TOOL)
		builder(ItemTags.HOES)
			.add(TRContent.BRONZE_HOE, TRContent.RUBY_HOE, TRContent.SAPPHIRE_HOE, TRContent.PERIDOT_HOE)
		builder(ItemTags.PICKAXES)
			.add(TRContent.BRONZE_PICKAXE, TRContent.RUBY_PICKAXE, TRContent.SAPPHIRE_PICKAXE, TRContent.PERIDOT_PICKAXE,
				TRContent.BASIC_DRILL, TRContent.ADVANCED_DRILL, TRContent.INDUSTRIAL_DRILL,
				TRContent.BASIC_JACKHAMMER, TRContent.ADVANCED_JACKHAMMER, TRContent.INDUSTRIAL_JACKHAMMER,
				TRContent.ROCK_CUTTER, TRContent.OMNI_TOOL)
		builder(ItemTags.SHOVELS)
			.add(TRContent.BRONZE_SPADE, TRContent.RUBY_SPADE, TRContent.SAPPHIRE_SPADE, TRContent.PERIDOT_SPADE,
				TRContent.BASIC_DRILL, TRContent.ADVANCED_DRILL, TRContent.INDUSTRIAL_DRILL, TRContent.OMNI_TOOL)
		builder(ItemTags.SWORDS)
			.add(TRContent.BRONZE_SWORD, TRContent.RUBY_SWORD, TRContent.SAPPHIRE_SWORD, TRContent.PERIDOT_SWORD,
				TRContent.NANOSABER, TRContent.BASIC_CHAINSAW, TRContent.ADVANCED_CHAINSAW, TRContent.INDUSTRIAL_CHAINSAW, TRContent.OMNI_TOOL)
	}

	private void addMachineMaterialTags() {
		builder(TRContent.ItemTags.CALCITE_DUST_MATERIAL)
			.add(Items.CALCITE, Items.DRIPSTONE_BLOCK)
			.addTag(TRConventionalTags.CORAL_BLOCKS)
		builder(TRContent.ItemTags.CALCITE_SMALL_DUST_MATERIAL)
			.add(Items.BONE_MEAL, Items.POINTED_DRIPSTONE, Items.NAUTILUS_SHELL)
			.addTag(TRConventionalTags.CORAL_FANS)
			.addTag(TRConventionalTags.CORAL_PLANTS)
		builder(TRContent.ItemTags.GRAVEL_MATERIAL)
			.add(Items.STONE, Items.SMOOTH_STONE, Items.COBBLESTONE, Items.DEEPSLATE, Items.DEEPSLATE_BRICKS,
				Items.CRACKED_DEEPSLATE_BRICKS, Items.DEEPSLATE_TILES, Items.CRACKED_DEEPSLATE_TILES,
				Items.POLISHED_DEEPSLATE, Items.CHISELED_DEEPSLATE, Items.COBBLED_DEEPSLATE, Items.BLACKSTONE,
				Items.POLISHED_BLACKSTONE, Items.CHISELED_POLISHED_BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICKS,
				Items.CRACKED_POLISHED_BLACKSTONE_BRICKS)
			.forceAddTag(ItemTags.STONE_BRICKS)
		builder(TRContent.ItemTags.PLANTBALL_MATERIAL)
			.add(Items.BEETROOT, Items.CARROT, Items.POTATO, Items.WHEAT, Items.MELON, Items.SUGAR_CANE,
				Items.CACTUS, Items.APPLE, Items.PUMPKIN, Items.KELP, Items.SWEET_BERRIES)
			.forceAddTag(ItemTags.LEAVES)
			.forceAddTag(ItemTags.SAPLINGS)
	}

	private static TagKey<Item> itemTag(String namespace, String path) {
		return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(namespace, path))
	}
}
