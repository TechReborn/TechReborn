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

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.ApplyBonusLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture
import java.util.function.Function

class BlockLootTableProvider extends FabricBlockLootTableProvider {

	BlockLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generate() {
		TRContent.StorageBlocks.values().each {
			addDrop(it.getBlock())
			addDrop(it.getSlabBlock())
			addDrop(it.getStairsBlock())
			addDrop(it.getWallBlock())
		}
		TRContent.Cables.values().each {
			addDrop(it.block)
		}
		TRContent.Machine.values().each {
			addDrop(it.block)
		}
		TRContent.SolarPanels.values().each {
			addDrop(it.block)
		}
		TRContent.StorageUnit.values().each {
			addDrop(it.block)
		}
		TRContent.TankUnit.values().each {
			addDrop(it.block)
		}
		TRContent.MachineBlocks.values().each {
			addDrop(it.getFrame())
			addDrop(it.getCasing())
		}
		addDrop(TRContent.RUBBER_BUTTON)
		addDrop(TRContent.RUBBER_DOOR, doorDrops(TRContent.RUBBER_DOOR))
		addDrop(TRContent.RUBBER_FENCE)
		addDrop(TRContent.RUBBER_FENCE_GATE)
		addDrop(TRContent.RUBBER_LOG)
		addDrop(TRContent.RUBBER_LOG_STRIPPED)
		addDrop(TRContent.RUBBER_PLANKS)
		addDrop(TRContent.RUBBER_PRESSURE_PLATE)
		addDrop(TRContent.RUBBER_SAPLING)
		addDrop(TRContent.RUBBER_SLAB)
		addDrop(TRContent.RUBBER_STAIR)
		addDrop(TRContent.RUBBER_TRAPDOOR)
		addDrop(TRContent.RUBBER_WOOD)
		addDrop(TRContent.RUBBER_LOG_STRIPPED)
		addDrop(TRContent.RUBBER_LEAVES, leavesDrops(
			TRContent.RUBBER_LEAVES,
			TRContent.RUBBER_SAPLING,
			0.05,
			0.0625,
			0.083333336,
			0.1)
		)
		addDrop(TRContent.POTTED_RUBBER_SAPLING, pottedPlantDrops(TRContent.RUBBER_SAPLING))
		addDrop(TRContent.NUKE)
		addDrop(TRContent.REFINED_IRON_FENCE)
		addDrop(TRContent.REINFORCED_GLASS)

		addOreDrop(TRContent.Ores.BAUXITE)
		addOreDrop(TRContent.Ores.GALENA)
		addOreDrop(TRContent.Ores.SHELDONITE)
		addOreDrop(TRContent.Ores.IRIDIUM, block -> oreDrops(block, TRContent.RawMetals.IRIDIUM.asItem()))
		addOreDrop(TRContent.Ores.LEAD, block -> oreDrops(block, TRContent.RawMetals.LEAD.asItem()))
		addOreDrop(TRContent.Ores.SILVER, block -> oreDrops(block, TRContent.RawMetals.SILVER.asItem()))
		addOreDrop(TRContent.Ores.TIN, block -> oreDrops(block, TRContent.RawMetals.TIN.asItem()))
		addOreDrop(TRContent.Ores.TUNGSTEN, block -> oreDrops(block, TRContent.RawMetals.TUNGSTEN.asItem()))
		addOreDrop(TRContent.Ores.CINNABAR, this::cinnabarOreDrops)
		addOreDrop(TRContent.Ores.RUBY, this::rubyOreDrops)
		addOreDrop(TRContent.Ores.SAPPHIRE, this::sapphireOreDrops)
		addOreDrop(TRContent.Ores.SODALITE, this::sodaliteOreDrops)
		addOreDrop(TRContent.Ores.SPHALERITE, this::sphaleriteOreDrops)
		addOreDrop(TRContent.Ores.PYRITE, block -> oreDrops(block, TRContent.Dusts.PYRITE.asItem()))
		addOreDrop(TRContent.Ores.PERIDOT, block -> drops(block, TRContent.Gems.PERIDOT.asItem(), UniformLootNumberProvider.create(1.0F, 2.0F)))
	}

	private void addOreDrop(TRContent.Ores ore) {
		addDrop(ore.block)
		def deepslate = ore.getDeepslate()
		if (deepslate != null) {
			addDrop(deepslate.block)
		}
	}

	private void addOreDrop(TRContent.Ores ore, Function<Block, LootTable.Builder> lootTableFunction) {
		addDrop(ore.block, lootTableFunction)
		def deepslate = ore.getDeepslate()
		if (deepslate != null) {
			addDrop(deepslate.block, lootTableFunction)
		}
	}

	private LootTable.Builder cinnabarOreDrops(Block drop) {
		RegistryWrapper.Impl<Enchantment> enchantments = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.builder()
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(TRContent.Dusts.CINNABAR.asItem()))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
						.with(ItemEntry.builder(Items.REDSTONE))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(drop))
						.conditionally(this.createSilkTouchCondition())
				)
		)
	}

	private LootTable.Builder rubyOreDrops(Block drop) {
		RegistryWrapper.Impl<Enchantment> enchantments = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.builder()
				.pool(
					LootPool.builder()
						.rolls(UniformLootNumberProvider.create(1.0F, 2.0F))
						.with(ItemEntry.builder(TRContent.Gems.RUBY.asItem()))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
						.with(ItemEntry.builder(TRContent.Gems.RED_GARNET.asItem()))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(drop))
						.conditionally(this.createSilkTouchCondition())
				)
		)
	}

	private LootTable.Builder sapphireOreDrops(Block drop) {
		RegistryWrapper.Impl<Enchantment> enchantments = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.builder()
				.pool(
					LootPool.builder()
						.rolls(UniformLootNumberProvider.create(1.0F, 2.0F))
						.with(ItemEntry.builder(TRContent.Gems.SAPPHIRE.asItem()))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
						.with(ItemEntry.builder(TRContent.Gems.PERIDOT.asItem()))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(drop))
						.conditionally(this.createSilkTouchCondition())
				)
		)
	}

	private LootTable.Builder sodaliteOreDrops(Block drop) {
		RegistryWrapper.Impl<Enchantment> enchantments = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.builder()
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(TRContent.Dusts.SODALITE.asItem()))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
						.with(ItemEntry.builder(TRContent.Dusts.ALUMINUM.asItem()))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(drop))
						.conditionally(this.createSilkTouchCondition())
				)
		)
	}

	private LootTable.Builder sphaleriteOreDrops(Block drop) {
		RegistryWrapper.Impl<Enchantment> enchantments = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
		return this.applyExplosionDecay(
			drop,
			LootTable.builder()
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(TRContent.Dusts.SPHALERITE.asItem()))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
						.with(ItemEntry.builder(TRContent.Gems.YELLOW_GARNET.asItem()))
						.apply(ApplyBonusLootFunction.uniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
						.conditionally(this.createSilkTouchCondition().invert())
				)
				.pool(
					LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1.0F))
						.with(ItemEntry.builder(drop))
						.conditionally(this.createSilkTouchCondition())
				)
		)
	}
}
