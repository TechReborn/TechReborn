/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.datagen.loottables

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import net.minecraft.core.registries.Registries
import net.minecraft.core.HolderLookup
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture
import java.util.function.Function

class BlockLootTableProvider extends FabricBlockLootSubProvider {

	BlockLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generate() {
		TRContent.StorageBlocks.values().each {
			dropSelf(it.getBlock())
			dropSelf(it.getSlabBlock())
			dropSelf(it.getStairsBlock())
			dropSelf(it.getWallBlock())
		}
		TRContent.Cables.values().each {
			dropSelf(it.block)
		}
		TRContent.Machine.values().each {
			dropSelf(it.block)
		}
		TRContent.SolarPanels.values().each {
			dropSelf(it.block)
		}
		TRContent.StorageUnit.values().each {
			dropSelf(it.block)
		}
		TRContent.TankUnit.values().each {
			dropSelf(it.block)
		}
		TRContent.MachineBlocks.values().each {
			dropSelf(it.getFrame())
			dropSelf(it.getCasing())
		}
		dropSelf(TRContent.RUBBER_BUTTON)
		add(TRContent.RUBBER_DOOR, createDoorTable(TRContent.RUBBER_DOOR))
		dropSelf(TRContent.RUBBER_FENCE)
		dropSelf(TRContent.RUBBER_FENCE_GATE)
		dropSelf(TRContent.RUBBER_LOG)
		dropSelf(TRContent.RUBBER_LOG_STRIPPED)
		dropSelf(TRContent.RUBBER_PLANKS)
		dropSelf(TRContent.RUBBER_PRESSURE_PLATE)
		dropSelf(TRContent.RUBBER_SAPLING)
		dropSelf(TRContent.RUBBER_SLAB)
		dropSelf(TRContent.RUBBER_STAIR)
		dropSelf(TRContent.RUBBER_TRAPDOOR)
		dropSelf(TRContent.RUBBER_WOOD)
		dropSelf(TRContent.RUBBER_LOG_STRIPPED)
		add(TRContent.RUBBER_LEAVES, createLeavesDrops(
			TRContent.RUBBER_LEAVES,
			TRContent.RUBBER_SAPLING,
			0.05,
			0.0625,
			0.083333336,
			0.1)
		)
		add(TRContent.POTTED_RUBBER_SAPLING, createPotFlowerItemTable(TRContent.RUBBER_SAPLING))
		dropSelf(TRContent.NUKE)
		dropSelf(TRContent.REFINED_IRON_FENCE)
		dropSelf(TRContent.REINFORCED_GLASS)

		addOreDrop(TRContent.Ores.BAUXITE, block -> createOreDrop(block, TRContent.Dusts.BAUXITE.asItem()))
		addOreDrop(TRContent.Ores.GALENA, block -> createOreDrop(block, TRContent.Dusts.GALENA.asItem()))
		addOreDrop(TRContent.Ores.SHELDONITE, block -> createOreDrop(block, TRContent.Dusts.PLATINUM.asItem()))
		addOreDrop(TRContent.Ores.IRIDIUM, block -> createOreDrop(block, TRContent.RawMetals.IRIDIUM.asItem()))
		addOreDrop(TRContent.Ores.LEAD, block -> createOreDrop(block, TRContent.RawMetals.LEAD.asItem()))
		addOreDrop(TRContent.Ores.SILVER, block -> createOreDrop(block, TRContent.RawMetals.SILVER.asItem()))
		addOreDrop(TRContent.Ores.TIN, block -> createOreDrop(block, TRContent.RawMetals.TIN.asItem()))
		addOreDrop(TRContent.Ores.TUNGSTEN, block -> createOreDrop(block, TRContent.RawMetals.TUNGSTEN.asItem()))
		addOreDrop(TRContent.Ores.URANIUM, block -> createOreDrop(block, TRContent.RawMetals.URANIUM.asItem()))
		addOreDrop(TRContent.Ores.CINNABAR, this::cinnabarOreDrops)
		addOreDrop(TRContent.Ores.RUBY, this::rubyOreDrops)
		addOreDrop(TRContent.Ores.SAPPHIRE, this::sapphireOreDrops)
		addOreDrop(TRContent.Ores.SODALITE, this::sodaliteOreDrops)
		addOreDrop(TRContent.Ores.SPHALERITE, this::sphaleriteOreDrops)
		addOreDrop(TRContent.Ores.PYRITE, block -> createOreDrop(block, TRContent.Dusts.PYRITE.asItem()))
		addOreDrop(TRContent.Ores.PERIDOT, block -> createSingleItemTableWithSilkTouch(block, TRContent.Gems.PERIDOT.asItem(), UniformGenerator.between(1.0F, 2.0F)))
	}

	private void addOreDrop(TRContent.Ores ore) {
		dropSelf(ore.block)
		def deepslate = ore.getDeepslate()
		if (deepslate != null) {
			dropSelf(deepslate.block)
		}
	}

	private void addOreDrop(TRContent.Ores ore, Function<Block, LootTable.Builder> lootTableFunction) {
		add(ore.block, lootTableFunction)
		def deepslate = ore.getDeepslate()
		if (deepslate != null) {
			add(deepslate.block, lootTableFunction)
		}
	}

	private LootTable.Builder cinnabarOreDrops(Block drop) {
		HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TRContent.Dusts.CINNABAR.asItem()))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(UniformGenerator.between(0.0F, 1.0F))
						.add(LootItem.lootTableItem(Items.REDSTONE))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(drop))
						.when(this.hasSilkTouch())
				)
		)
	}

	private LootTable.Builder rubyOreDrops(Block drop) {
		HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 2.0F))
						.add(LootItem.lootTableItem(TRContent.Gems.RUBY.asItem()))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(UniformGenerator.between(0.0F, 1.0F))
						.add(LootItem.lootTableItem(TRContent.Gems.RED_GARNET.asItem()))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(drop))
						.when(this.hasSilkTouch())
				)
		)
	}

	private LootTable.Builder sapphireOreDrops(Block drop) {
		HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 2.0F))
						.add(LootItem.lootTableItem(TRContent.Gems.SAPPHIRE.asItem()))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(UniformGenerator.between(0.0F, 1.0F))
						.add(LootItem.lootTableItem(TRContent.Gems.PERIDOT.asItem()))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(drop))
						.when(this.hasSilkTouch())
				)
		)
	}

	private LootTable.Builder sodaliteOreDrops(Block drop) {
		HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TRContent.Dusts.SODALITE.asItem()))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(UniformGenerator.between(0.0F, 1.0F))
						.add(LootItem.lootTableItem(TRContent.Dusts.ALUMINUM.asItem()))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(drop))
						.when(this.hasSilkTouch())
				)
		)
	}

	private LootTable.Builder sphaleriteOreDrops(Block drop) {
		HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TRContent.Dusts.SPHALERITE.asItem()))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(UniformGenerator.between(0.0F, 1.0F))
						.add(LootItem.lootTableItem(TRContent.Gems.YELLOW_GARNET.asItem()))
						.apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.when(this.hasSilkTouch().invert())
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(drop))
						.when(this.hasSilkTouch())
				)
		)
	}
}
