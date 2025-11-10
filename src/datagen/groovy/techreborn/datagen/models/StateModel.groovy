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

import net.minecraft.world.level.block.Block
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.blockstates.MultiPartGenerator
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.renderer.block.model.VariantMutator
import net.minecraft.client.data.models.blockstates.ConditionBuilder
import net.minecraft.client.data.models.MultiVariant
import org.jetbrains.annotations.Nullable

class StateModel {
	List<MultiVariant> variants = new ArrayList<>()
	List<PropertyDispatch<?>> variantMaps = new ArrayList<>()
	@Nullable
	List<ConditionBuilder> conditions

	StateModel multipart() {
		conditions = new ArrayList<>()
		return this
	}

	StateModel add(MultiVariant variant) {
		variants.add(variant)
		if (conditions != null) conditions.add(null)
		return this
	}

	StateModel add(ConditionBuilder condition, MultiVariant variant) {
		variants.add(variant)
		conditions.add(condition)
		return this
	}

	StateModel add(PropertyDispatch<?> map) {
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
			MultiVariantGenerator supplier
			if (variants.isEmpty()) {
				MultiVariantGenerator.Empty empty = MultiVariantGenerator.dispatch(block)
				if (variantMaps.isEmpty()) {
					throw new IllegalStateException("No target specified")
				}
				supplier = empty.with(variantMaps.get(0) as PropertyDispatch<MultiVariant>)
				for (int i = 1, len = variantMaps.size(); i < len; i++) {
					supplier = supplier.with(variantMaps.get(i) as PropertyDispatch<VariantMutator>)
				}
			} else {
				supplier = MultiVariantGenerator.dispatch(block, variants.get(0))
				for (PropertyDispatch<?> map : variantMaps) {
					supplier = supplier.with(map as PropertyDispatch<VariantMutator>)
				}
			}
			ModelProvider.stateGenerator.blockStateOutput.accept(supplier)
		} else {
			MultiPartGenerator supplier = MultiPartGenerator.multiPart(block)
			for (int i = 0, len = conditions.size(); i < len; i++) {
				ConditionBuilder condition = conditions.get(i)
				if (condition == null) {
					supplier.with(variants.get(i))
				} else {
					supplier.with(condition, variants.get(i))
				}
			}
			ModelProvider.stateGenerator.blockStateOutput.accept(supplier)
		}
	}
}
