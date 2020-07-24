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

package techreborn.blockentity.data;

import reborncore.client.screen.builder.BlockEntityScreenHandlerBuilder;

import java.util.Arrays;
import java.util.function.BiConsumer;

public enum SlotType {
	//Im really not a fan of the way I add the slots to the builder here
	INPUT((builder, slot) -> {
		builder.slot(slot.getId(), slot.getX(), slot.getY());
	}),
	OUTPUT((builder, slot) -> {
		builder.outputSlot(slot.getId(), slot.getX(), slot.getY());
	}),
	ENERGY((builder, slot) -> {
		builder.energySlot(slot.getId(), slot.getX(), slot.getY());
	});

	public static SlotType fromString(String string) {
		return Arrays.stream(values())
				.filter(slotType -> slotType.name().equalsIgnoreCase(string))
				.findFirst()
				.orElse(null);
	}

	private final BiConsumer<BlockEntityScreenHandlerBuilder, DataDrivenSlot> slotBiConsumer;

	SlotType(BiConsumer<BlockEntityScreenHandlerBuilder, DataDrivenSlot> slotBiConsumer) {
		this.slotBiConsumer = slotBiConsumer;
	}

	public BiConsumer<BlockEntityScreenHandlerBuilder, DataDrivenSlot> getSlotBiConsumer() {
		return slotBiConsumer;
	}
}
