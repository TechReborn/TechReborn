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

import net.minecraft.advancements.Advancement
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.world.level.ItemLike
import net.minecraft.world.item.ItemStack
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

class AdvancementFactory {
	private String name
	private ItemStack icon
	private AdvancementType frame = AdvancementType.TASK
	private List<Criterion<? extends CriterionTriggerInstance>> conditionsList = []
	private AdvancementHolder parent
	private Identifier background
	private boolean hidden = false

	void name(String name) {
		this.name = name
	}

	void icon(ItemStack icon) {
		this.icon = icon
	}

	void icon(ItemLike item) {
		icon new ItemStack(item)
	}

	void frame(AdvancementType frame) {
		this.frame = frame
	}

	void condition(Criterion<? extends CriterionTriggerInstance> condition) {
		this.conditionsList << condition
	}

	void parent(AdvancementHolder advancement) {
		this.parent = advancement
	}

	void background(Identifier identifier) {
		this.background = identifier
	}

	void hidden(boolean hidden) {
		this.hidden = hidden
	}

	AdvancementHolder build() {
		Objects.requireNonNull(name, "No name set")
		assert conditionsList.size() > 0

		def builder = Advancement.Builder.recipeAdvancement()

		builder.display(
			icon,
			Component.translatable("advancements.techreborn.${name}"),
			Component.translatable("advancements.techreborn.${name}.desc"),
			background,
			frame,
			true,
			true,
			hidden
		)

		int i = 0
		conditionsList.forEach {
			builder.addCriterion("crit_${i++}", it)
		}

		if (parent != null) {
			builder.parent(parent)
		}

		return builder.build(Identifier.parse("techreborn:${name}"))
	}
}
