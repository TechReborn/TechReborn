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

package techreborn.blockentity.machine.tier1;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.HashMap;
import java.util.Map;

public class TapperBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {

	private static final int ENERGY_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;

	//TODO LIST
	// Check tree structure
	// Move off to own functions
	// Run every like 5 seconds or something slow
	// GUI
	// Textures
	// Orientable
	// Use energy (check before)
	// Ensure storage before sapping

	public TapperBlockEntity() {
		super(TRBlockEntities.TAPPER, "Tapper", TechRebornConfig.tapperMaxInput, TechRebornConfig.tapperMaxEnergy, TRContent.Machine.EXTRACTOR.block, ENERGY_SLOT);
		this.inventory = new RebornInventory<>(2, "TapperBlockEntity", 64, this);
	}

	@Override
	public void tick() {
		super.tick();

		if(world == null || world.isClient) return;

		// TODO cleanup and refactor (MVP)
		HashMap<BlockPos, BlockState> rubberLogs = new HashMap<>();

		BlockPos originPos = this.pos.offset(Direction.NORTH);
		BlockState originState = world.getBlockState(originPos);
		if(originState.getBlock() != TRContent.RUBBER_LOG) return; // TODO CHECK STRUCTURE

		rubberLogs.put(originPos, originState);

		boolean shouldExit = false;
		BlockPos current = originPos;
		// Progress Up
		while(!shouldExit){
			current = current.offset(Direction.UP);

			BlockState state = world.getBlockState(current);
			if(state.getBlock() == TRContent.RUBBER_LOG){
				rubberLogs.put(current, state);
			}else{
				shouldExit = true;
			}
		}

		// Progress Down
		shouldExit = false;
		current = originPos;
		while(!shouldExit){
			current = current.offset(Direction.DOWN);

			BlockState state = world.getBlockState(current);
			if(state.getBlock() == TRContent.RUBBER_LOG){
				rubberLogs.put(current, state);
			}else{
				shouldExit = true;
			}
		}
		
		int yield = 0;

		// Harvest sap
		for (Map.Entry<BlockPos, BlockState> entry : rubberLogs.entrySet()) {
			BlockPos pos = entry.getKey();
			BlockState state = entry.getValue();
			if(state.get(BlockRubberLog.HAS_SAP)){
				world.setBlockState(pos, state.with(BlockRubberLog.HAS_SAP, false).with(BlockRubberLog.SAP_SIDE, Direction.fromHorizontal(0)));
				yield++;
			}
		}

		if(yield > 0){
			world.playSound(pos.getX(),pos.getY(),pos.getZ(), ModSounds.SAP_EXTRACT, SoundCategory.BLOCKS, 0.6F, 1F, false);


			if(inventory.getStack(OUTPUT_SLOT).isEmpty()){
				ItemStack yieldStack = TRContent.Parts.SAP.getStack();
				yieldStack.setCount(yield);
				this.inventory.setStack(OUTPUT_SLOT,yieldStack);
			}else{
				ItemStack currentStack = inventory.getStack(OUTPUT_SLOT);
				if(currentStack.getCount() + yield <= inventory.getStackLimit()){
					currentStack.increment(yield);
					// TODO check room before harvesting
					inventory.setStack(OUTPUT_SLOT, currentStack);
				}
			}

		}
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("tapper").player(player.inventory).inventory().hotbar().addInventory().blockEntity(this)
				.energySlot(0, 8, 72).outputSlot(1, 101, 45).syncEnergyValue()
				.addInventory().create(this, syncID);
	}
}
