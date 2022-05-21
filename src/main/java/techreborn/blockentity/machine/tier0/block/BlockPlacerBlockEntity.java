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

package techreborn.blockentity.machine.tier0.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.BlockEntityScreenHandlerBuilder;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.tier0.block.blockplacer.BlockPlacerProcessor;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

/**
 * <b>A machine that can break place blocks in front of it</b>
 * <br>
 * May not place blocks with hardness < 0.
 * Shares most functionality with other GenericMachineBlockEntities, however does not make use of the {@code RecipeCrafter}
 *
 * @author SimonFlapse
 */
public class BlockPlacerBlockEntity extends AbstractBlockBlockEntity implements BuiltScreenHandlerProvider, BlockProcessable {

	public static final int ENERGY_SLOT = 0;
	public static final int INPUT_SLOT = 1;
	public static final int FAKE_OUTPUT_SLOT = 2;

	public BlockPlacerBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.BLOCK_PLACER, pos, state, "Block Placer", TechRebornConfig.blockPlacerMaxInput, TechRebornConfig.blockPlacerMaxEnergy, TRContent.Machine.BLOCK_PLACER.block, ENERGY_SLOT);
		processor = new BlockPlacerProcessor(this, INPUT_SLOT, FAKE_OUTPUT_SLOT, TechRebornConfig.blockPlacerBaseBreakTime, TechRebornConfig.blockPlacerEnergyPerTick);
		inventory = new RebornInventory<>(3, "BlockPlacerBlockEntity", 64, this);
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		BlockEntityScreenHandlerBuilder builder = new ScreenHandlerBuilder("blockplacer")
			.player(player.getInventory())
			.inventory()
			.hotbar()
			.addInventory()
			.blockEntity(this)
			.energySlot(ENERGY_SLOT, 8 , 72).syncEnergyValue()
			.slot(INPUT_SLOT, 80, 60);

			processor.syncNbt(builder);

			return builder.addInventory().create(this, syncID);
	}

	public BlockPlacerProcessor getProcessor() {
		return (BlockPlacerProcessor) this.processor;
	}
}
