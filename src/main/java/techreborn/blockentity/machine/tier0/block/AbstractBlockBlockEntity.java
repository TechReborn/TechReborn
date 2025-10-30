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

package techreborn.blockentity.machine.tier0.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.BlockEntityScreenHandlerBuilder;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

public class AbstractBlockBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider, BlockProcessable {
	private static final int ENERGY_SLOT = 0;
	private static final int INPUT_SLOT = 1;

	protected BlockProcessor processor;

	/**
	 * @param blockEntityType
	 * @param pos
	 * @param state
	 * @param name            {@link String} Name for a {@link BlockEntity}.
	 * @param maxInput        {@code int} Maximum energy input, value in EU
	 * @param maxEnergy       {@code int} Maximum energy buffer, value in EU
	 * @param toolDrop        {@link Block} Block to drop with wrench
	 * @param energySlot      {@code int} Energy slot to use to charge machine from battery
	 */
	public AbstractBlockBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, String name, int maxInput, int maxEnergy, Block toolDrop, int energySlot) {
		super(blockEntityType, pos, state, name, maxInput, maxEnergy, toolDrop, energySlot);
	}


	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
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

	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClientSide) {
			return;
		}

		processor.onTick(world, BlockProcessorUtils.getFrontBlockPosition(blockEntity, pos));
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		processor.writeData(view);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		processor.readData(view);
	}

	public BlockProcessor getProcessor() {
		return processor;
	}

	//BlockBreakerProcessable
	@Override
	public boolean consumeEnergy(int amount) {
		return tryUseExact(getEuPerTick(amount));
	}

	//BlockBreakerProcessable
	@Override
	public void playSound() {
		if (RecipeCrafter.soundHandler != null && canPlaySound()) {
			RecipeCrafter.soundHandler.playSound(false, this);
		}
	}

	@Override
	public boolean canPlaySound() {
		return !isMuffled();
	}
}
