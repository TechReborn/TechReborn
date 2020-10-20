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

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import team.reborn.energy.EnergySide;
import techreborn.blocks.lighting.LampBlock;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.Collections;
import java.util.List;

public class GreenhouseControllerBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	private final RebornInventory<GreenhouseControllerBlockEntity> inventory = new RebornInventory<>(7, "GreenhouseControllerBlockEntity", 64, this);
	private BlockPos multiblockCenter;
	private int ticksToNextMultiblockCheck = 0;
	private boolean growthBoost = false;

	public GreenhouseControllerBlockEntity() {
		super(TRBlockEntities.GREENHOUSE_CONTROLLER);
	}

	private void workCycle() {
		if (world == null){
			return;
		}
		BlockPos blockPos = multiblockCenter.add(world.random.nextInt(9) - 4, 0, world.random.nextInt(9) - 4);
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();

		if (growthBoost) {
			if (block instanceof Fertilizable
					|| block instanceof PlantBlock
					|| block instanceof SugarCaneBlock
					|| block instanceof CactusBlock
			) {
				if (getStored(EnergySide.UNKNOWN) > TechRebornConfig.greenhouseControllerEnergyPerBonemeal) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerBonemeal);
					blockState.scheduledTick((ServerWorld) world, blockPos, world.random);
				}
			}
		}

		if (block instanceof CropBlock) {
			processAgedCrop(blockState, blockPos, ((CropBlock) block).getAgeProperty(), ((CropBlock) block).getMaxAge(), 0);
		} else if (block instanceof NetherWartBlock) {
			processAgedCrop(blockState, blockPos, NetherWartBlock.AGE, 3, 0);
		} else if (block instanceof SweetBerryBushBlock) {
			processAgedCrop(blockState, blockPos, SweetBerryBushBlock.AGE, 3, 1);
		} else if (block instanceof CocoaBlock) {
			processAgedCrop(blockState, blockPos, CocoaBlock.AGE, 2, 0);
		} else if (block instanceof GourdBlock) {
			if (tryHarvestBlock(blockState, blockPos)) {
				world.breakBlock(blockPos, false);
			}
		} else if (block instanceof SugarCaneBlock
				|| block instanceof CactusBlock
				|| block instanceof BambooBlock
		) {
			// If we can break bottom block we should at least remove all of them up to top so they don't break automatically
			boolean breakBlocks = false;
			for (int y = 1; (blockState = world.getBlockState(blockPos.up(y))).getBlock() == block; y++) {
				if (y == 1) {
					breakBlocks = tryHarvestBlock(blockState, blockPos.up(y));
				} else {
					tryHarvestBlock(blockState, blockPos.up(y));
				}
				if (breakBlocks) world.breakBlock(blockPos.up(y), false);
			}
		} else if (block instanceof BlockRubberLog) {
			for (int y = 0; (blockState = world.getBlockState(blockPos.up(y))).getBlock() == block && y < 10; y++) {
				if (blockState.get(BlockRubberLog.HAS_SAP)
						&& (getStored(EnergySide.UNKNOWN) > TechRebornConfig.greenhouseControllerEnergyPerHarvest)
						&& insertIntoInv(Collections.singletonList(TRContent.Parts.SAP.getStack()))
				) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
					world.setBlockState(blockPos.up(y), blockState.with(BlockRubberLog.HAS_SAP, false).with(BlockRubberLog.SAP_SIDE, Direction.fromHorizontal(0)));
				}
			}
		}
	}

	private void processAgedCrop(BlockState blockState, BlockPos blockPos, IntProperty ageProperty, int maxAge, int newAge) {
		if (world == null) {
			return;
		}
		if (blockState.get(ageProperty) < maxAge) {
			return;
		}
		if (tryHarvestBlock(blockState, blockPos)) {
			world.setBlockState(blockPos, blockState.with(ageProperty, newAge), 2);
		}
	}

	private boolean tryHarvestBlock(BlockState blockState, BlockPos blockPos) {
		if (world == null) {
			return false;
		}
		if (getStored(EnergySide.UNKNOWN) < TechRebornConfig.greenhouseControllerEnergyPerHarvest){
			return false;
		}
		if (insertIntoInv(Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos, null))) {
			useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
			return true;
		}
		return false;
	}

	private boolean insertIntoInv(List<ItemStack> stacks) {
		boolean result = false;
		for (ItemStack stack : stacks) {
			for (int i = 0; i < 6; i++) {
				if (insertIntoInv(i, stack)) result = true;
				if (stack.isEmpty()) break;
			}
		}
		return result;
	}

	private boolean insertIntoInv(int slot, ItemStack stack) {
		ItemStack targetStack = inventory.getStack(slot);
		if (targetStack.isEmpty()) {
			inventory.setStack(slot, stack.copy());
			stack.decrement(stack.getCount());
			return true;
		} else {
			if (ItemUtils.isItemEqual(stack, targetStack, true, false)) {
				int freeStackSpace = targetStack.getMaxCount() - targetStack.getCount();
				if (freeStackSpace > 0) {
					int transferAmount = Math.min(freeStackSpace, stack.getCount());
					targetStack.increment(transferAmount);
					stack.decrement(transferAmount);
					return true;
				}
			}
		}
		return false;
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick() {
		if (world == null){
			return;
		}
		if (multiblockCenter == null) {
			multiblockCenter = pos.offset(getFacing().getOpposite(), 5);
		}
		charge(6);
		super.tick();

		if (world.isClient) {
			return;
		}

		if (getStored(EnergySide.UNKNOWN) < getEuPerTick(TechRebornConfig.greenhouseControllerEnergyPerTick)) {
			return;
		}

		if (--ticksToNextMultiblockCheck < 0) {
			growthBoost = isMultiblockValid();
			ticksToNextMultiblockCheck = 200;
		}

		if (world.getTime() % 20 == 0) {
			double cyclesLimit = getSpeedMultiplier() * 4 + 1;
			while (cyclesLimit-- > 0) {
				workCycle();
			}
		}
	}

	@Override
	public boolean canProvideEnergy(EnergySide side) {
		return false;
	}

	@Override
	public double getBaseMaxPower() {
		return TechRebornConfig.greenhouseControllerMaxEnergy;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return TechRebornConfig.greenhouseControllerMaxInput;
	}

	// MachineBaseBlockEntity
	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		BlockState lamp = TRContent.Machine.LAMP_INCANDESCENT.block.getDefaultState().with(Properties.FACING, Direction.DOWN);
		BlockState crop = Blocks.CACTUS.getDefaultState();

		for (int i = 0; i < 3; i++) {
			for (int j = -1; j < 2; j++) {
				writer.add(i * 3 + 2, 3, j * 3, (world, pos) -> LampBlock.isActive(world.getBlockState(pos)), lamp);
			}
		}

		for (int i = 1; i <= 9; i++) {
			for (int j = -4; j <= 4; j++) {
				writer.add(i, 0, j, (world, pos) -> true, crop);
			}
		}
	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return TRContent.Machine.GREENHOUSE_CONTROLLER.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<GreenhouseControllerBlockEntity> getInventory() {
		return this.inventory;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("greenhousecontroller").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this)
				.outputSlot(0, 30, 22).outputSlot(1, 48, 22)
				.outputSlot(2, 30, 40).outputSlot(3, 48, 40)
				.outputSlot(4, 30, 58).outputSlot(5, 48, 58)
				.energySlot(6, 8, 72).syncEnergyValue()
				.addInventory().create(this, syncID);
	}

}
