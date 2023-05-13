/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2023 TechReborn
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

package techreborn.datagen.advancement

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.advancement.criterion.ItemCriterion
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class TRAdvancementProvider extends FabricAdvancementProvider {
	private Consumer<Advancement> consumer

	public TRAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output)
	}

	@Override
	void generateAdvancement(Consumer<Advancement> consumer) {
		this.consumer = consumer

		def root = create {
			name "root"
			icon TRContent.MANUAL
			background new Identifier("techreborn:textures/block/storage/steel_storage_block.png")
			condition inventoryChanged(TRContent.ItemTags.ORES)
			condition inventoryChanged(TRContent.ItemTags.RAW_METALS)
			condition inventoryChanged(TRContent.ItemTags.GEMS)
		}

		refinedIronTree(root)
	}

	private void refinedIronTree(Advancement root) {
		def refinediron = create {
			parent root
			name "refinediron"
			icon TRContent.Ingots.REFINED_IRON
			condition inventoryChanged(TRContent.Ingots.REFINED_IRON)
		}

		def alarm = create {
			parent refinediron
			name "alarm"
			icon TRContent.Machine.ALARM
			condition inventoryChanged(TRContent.Machine.ALARM)
		}

		def machineBlock = create {
			parent refinediron
			name "machineblock"
			icon TRContent.MachineBlocks.BASIC.frame
			condition inventoryChanged(TRContent.MachineBlocks.BASIC.frame)
		}

		def generator = create {
			parent machineBlock
			name "generator"
			icon TRContent.Machine.SOLID_FUEL_GENERATOR
			condition placedBlock(TRContent.Machine.SOLID_FUEL_GENERATOR.block)
		}

		def windmill = create {
			parent generator
			name "windmill"
			frame AdvancementFrame.GOAL
			icon TRContent.Machine.WIND_MILL
			condition placedBlock(TRContent.Machine.WIND_MILL.block)
		}
	}

	private static CriterionConditions placedBlock(Block block) {
		return ItemCriterion.Conditions.createPlacedBlock(block)
	}

	private static CriterionConditions inventoryChanged(ItemConvertible... items) {
		return InventoryChangedCriterion.Conditions.items(items)
	}

	private static CriterionConditions inventoryChanged(TagKey<Item> tag) {
		return InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(tag).build())
	}

	private Advancement create(@DelegatesTo(value = AdvancementFactory.class) Closure closure) {
		def factory = new AdvancementFactory()
		closure.setDelegate(factory)
		closure.call(factory)
		def advancement = factory.build()
		consumer.accept(advancement)
		return advancement
	}
}
