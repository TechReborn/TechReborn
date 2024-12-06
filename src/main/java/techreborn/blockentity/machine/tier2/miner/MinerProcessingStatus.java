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

package techreborn.blockentity.machine.tier2.miner;

import net.minecraft.text.Text;

public enum MinerProcessingStatus {
	NO_ENERGY(Text.translatable("gui.techreborn.block.miner.no_energy")),
	NO_TOOL(Text.translatable("gui.techreborn.block.miner.no_tool")),
	NO_PIPE(Text.translatable("gui.techreborn.block.miner.no_pipe")),
	READY(Text.translatable("gui.techreborn.block.miner.ready")),
	PROBING(Text.translatable("gui.techreborn.block.miner.probing")),
	DRILLING(Text.translatable("gui.techreborn.block.miner.drilling")),
	PROSPECTING(Text.translatable("gui.techreborn.block.miner.prospecting")),
	DRILLING_ORE(Text.translatable("gui.techreborn.block.miner.drilling_ore")),
	RETRACTING(Text.translatable("gui.techreborn.block.miner.retracting")),
	DONE(Text.translatable("gui.techreborn.block.miner.done"));

	private final Text displayText;

	MinerProcessingStatus(Text displayText){
		this.displayText = displayText;
	}

	public Text getDisplayText() {
		return displayText;
	}
}
