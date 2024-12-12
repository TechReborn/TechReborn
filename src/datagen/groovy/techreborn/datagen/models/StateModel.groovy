/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
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

import net.minecraft.block.Block
import net.minecraft.client.data.BlockStateVariant
import net.minecraft.client.data.BlockStateVariantMap
import net.minecraft.client.data.MultipartBlockStateSupplier
import net.minecraft.client.data.VariantsBlockStateSupplier
import net.minecraft.client.data.When
import org.jetbrains.annotations.Nullable

class StateModel {
	List<BlockStateVariant> variants = new ArrayList<>()
	List<BlockStateVariantMap> variantMaps = new ArrayList<>()
	@Nullable
	List<When> conditions

	StateModel multipart() {
		conditions = new ArrayList<>()
		return this
	}

	StateModel add(BlockStateVariant variant) {
		variants.add(variant)
		if (conditions != null) conditions.add(null)
		return this
	}

	StateModel add(When condition, BlockStateVariant variant) {
		variants.add(variant)
		conditions.add(condition)
		return this
	}

	StateModel add(BlockStateVariantMap map) {
		variantMaps.add(map)
		return this
	}

	StateModel add(StateModel state) {
		variants.addAll(state.variants)
		variantMaps.addAll(state.variantMaps)
		return this
	}

	void upload(Block block) {
		if (conditions == null) {
			VariantsBlockStateSupplier supplier = variants.size() == 0
				? VariantsBlockStateSupplier.create(block)
				: VariantsBlockStateSupplier.create(block, variants.toArray() as BlockStateVariant[])
			variantMaps.forEach(supplier::coordinate)
			ModelProvider.stateGenerator.blockStateCollector.accept(supplier)
		} else {
			MultipartBlockStateSupplier supplier = MultipartBlockStateSupplier.create(block)
			for (int i = 0, len = conditions.size(); i < len; i++) {
				When condition = conditions.get(i)
				if (condition == null) {
					supplier.with(variants.get(i))
				} else {
					supplier.with(condition, variants.get(i))
				}
			}
			ModelProvider.stateGenerator.blockStateCollector.accept(supplier)
		}
	}
}
