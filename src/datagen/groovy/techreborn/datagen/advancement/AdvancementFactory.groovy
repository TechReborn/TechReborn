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

import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class AdvancementFactory {
	private String name
	private ItemStack icon
	private AdvancementFrame frame = AdvancementFrame.TASK
	private List<CriterionConditions> conditionsList = []
	private Advancement parent
	private Identifier background
	private boolean hidden = false

	void name(String name) {
		this.name = name
	}

	void icon(ItemStack icon) {
		this.icon = icon
	}

	void icon(ItemConvertible item) {
		icon new ItemStack(item)
	}

	void frame(AdvancementFrame frame) {
		this.frame = frame
	}

	void condition(CriterionConditions condition) {
		this.conditionsList << condition
	}

	void parent(Advancement advancement) {
		this.parent = advancement
	}

	void background(Identifier identifier) {
		this.background = identifier
	}

	void hidden(boolean hidden) {
		this.hidden = hidden
	}

	Advancement build() {
		Objects.requireNonNull(name, "No name set")
		assert conditionsList.size() > 0

		def builder = Advancement.Builder.createUntelemetered()

		builder.display(
			icon,
			Text.translatable("advancements.techreborn.${name}"),
			Text.translatable("advancements.techreborn.${name}.desc"),
			background,
			frame,
			true,
			true,
			hidden
		)

		int i = 0
		conditionsList.forEach {
			builder.criterion("crit_${i++}", it)
		}

		if (parent != null) {
			builder.parent(parent)
		}

		return builder.build(new Identifier("techreborn:${name}"))
	}
}
