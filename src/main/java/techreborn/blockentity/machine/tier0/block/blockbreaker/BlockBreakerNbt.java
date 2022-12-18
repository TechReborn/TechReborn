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

import net.minecraft.nbt.NbtCompound;
import reborncore.common.screen.builder.BlockEntityScreenHandlerBuilder;
import techreborn.blockentity.machine.tier0.block.ProcessingStatus;

/**
 * <b>Class handling Nbt values of the Block Breaker</b>
 * <br>
 * Inherited by the {@link BlockBreakerProcessor} for keeping its values in sync when saving/loading a map
 *
 * @author SimonFlapse
 * @see BlockBreakerProcessor
 */
class BlockBreakerNbt {
	protected int breakTime;
	protected int currentBreakTime;
	protected ProcessingStatus status = BlockBreakerStatus.IDLE;

	public void writeNbt(NbtCompound tag) {
		tag.putInt("breakTime", this.breakTime);
		tag.putInt("currentBreakTime", this.currentBreakTime);
		tag.putInt("blockBreakerStatus", getStatus());
	}

	public void readNbt(NbtCompound tag) {
		this.breakTime = tag.getInt("breakTime");
		this.currentBreakTime = tag.getInt("currentBreakTime");
		setStatus(tag.getInt("blockBreakerStatus"));
	}

	public BlockEntityScreenHandlerBuilder syncNbt(BlockEntityScreenHandlerBuilder builder) {
		return builder.sync(this::getBreakTime, this::setBreakTime)
			.sync(this::getCurrentBreakTime, this::setCurrentBreakTime)
			.sync(this::getStatus, this::setStatus);
	}

	protected int getBreakTime() {
		return breakTime;
	}

	protected void setBreakTime(int breakTime) {
		this.breakTime = breakTime;
	}

	protected int getCurrentBreakTime() {
		return currentBreakTime;
	}

	protected void setCurrentBreakTime(int currentBreakTime) {
		this.currentBreakTime = currentBreakTime;
	}

	protected int getStatus() {
		return status.getStatusCode();
	}

	protected void setStatus(int status) {
		this.status = BlockBreakerStatus.values()[status];
	}
}
