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

package techreborn.blockentity.machine.tier0.block.blockbreaker;

import net.minecraft.network.chat.Component;
import reborncore.common.util.Color;
import techreborn.blockentity.machine.tier0.block.ProcessingStatus;

/**
 * An enumeration of different statuses the Block Breaker can have
 *
 * Includes {@code TranslatableText} describing the status along with a text color
 *
 * @author SimonFlapse
 */
enum BlockBreakerStatus implements ProcessingStatus {
	IDLE(Component.translatable("gui.techreborn.block_breaker.idle"), Color.BLUE),
	IDLE_PAUSED(Component.translatable("gui.techreborn.block.idle_redstone"), Color.BLUE),
	OUTPUT_FULL(Component.translatable("gui.techreborn.block.output_full"), Color.RED),
	NO_ENERGY(Component.translatable("gui.techreborn.block.no_energy"), Color.RED),
	INTERRUPTED(Component.translatable("gui.techreborn.block.interrupted"), Color.RED),
	OUTPUT_BLOCKED(Component.translatable("gui.techreborn.block.output_blocked"), Color.RED),
	PROCESSING(Component.translatable("gui.techreborn.block_breaker.processing"), Color.DARK_GREEN);

	private final Component text;
	private final int color;

	BlockBreakerStatus(Component text, Color color) {
		this.text = text;
		this.color = color.getColor();
	}

	@Override
	public Component getText() {
		return text;
	}

	@Override
	public Component getProgressText(int progress) {
		progress = Math.max(progress, 0);

		if (this == PROCESSING) {
			return Component.translatable("gui.techreborn.block.progress.active", Component.literal("" + progress + "%"));
		} else if (this == IDLE || this == INTERRUPTED) {
			return Component.translatable("gui.techreborn.block.progress.stopped");
		}

		return Component.translatable("gui.techreborn.block.progress.paused", Component.literal("" + progress + "%"));
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public int getStatusCode() {
		return this.ordinal();
	}
}
