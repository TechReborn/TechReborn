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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.BlockEntityScreenHandlerBuilder;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.tier0.block.blockbreaker.BlockBreakerProcessor;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

/**
 * <b>A machine that can break the block physically located in front of it</b>
 * <br>
 * May not break blocks with hardness < 0, may not break blocks that has no item.
 * Shares most functionality with other GenericMachineBlockEntities, however does not make use of the {@code RecipeCrafter}
 *
 * @author SimonFlapse
 */
public class BlockBreakerBlockEntity extends AbstractBlockBlockEntity implements BuiltScreenHandlerProvider, BlockProcessable {

	public static final int ENERGY_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int FAKE_INPUT_SLOT = 2;

	public BlockBreakerBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.BLOCK_BREAKER, pos, state, "Block Breaker", TechRebornConfig.blockBreakerMaxInput, TechRebornConfig.blockBreakerMaxEnergy, TRContent.Machine.BLOCK_BREAKER.block, ENERGY_SLOT);
		processor = new BlockBreakerProcessor(this, OUTPUT_SLOT, FAKE_INPUT_SLOT, TechRebornConfig.blockBreakerBaseBreakTime, TechRebornConfig.blockBreakerEnergyPerTick);
		inventory = new RebornInventory<>(3, "BlockBreakerBlockEntity", 64, this){
			@Override
			public ItemStack getStack(int i) {
				if (i == FAKE_INPUT_SLOT) {
					return ItemStack.EMPTY;
				}
				return super.getStack(i);
			}
		};
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		BlockEntityScreenHandlerBuilder builder = new ScreenHandlerBuilder("blockbreaker")
			.player(player.getInventory())
			.inventory()
			.hotbar()
			.addInventory()
			.blockEntity(this)
			.energySlot(ENERGY_SLOT, 8 , 72).syncEnergyValue()
			.outputSlot(OUTPUT_SLOT, 80, 60);

			processor.syncNbt(builder);

			return builder.addInventory().create(this, syncID);
	}

	public BlockBreakerProcessor getProcessor() {
		return (BlockBreakerProcessor) this.processor;
	}
}
