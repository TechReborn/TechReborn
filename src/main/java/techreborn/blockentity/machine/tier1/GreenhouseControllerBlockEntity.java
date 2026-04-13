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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jspecify.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
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
	private int workingIndex = 0;
	// number of blocks from center
	private final int range = 4;


	public GreenhouseControllerBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.GREENHOUSE_CONTROLLER, pos, state);
	}

	private void workCycle() {
		if (level == null){
			return;
		}

		int size = range * 2 + 1;
		int offsetX = workingIndex % size;
		int offsetZ = workingIndex / size;
		BlockPos corner = multiblockCenter.offset(-range, 0, -range);
		BlockPos blockPos = corner.offset(offsetX, 0, offsetZ);

		workingIndex = (workingIndex + 1) % (size * size);
		BlockState blockState = level.getBlockState(blockPos);
		Block block = blockState.getBlock();

		if (growthBoost) {
			if (block instanceof BonemealableBlock || block instanceof VegetationBlock
					|| block instanceof SugarCaneBlock	|| block instanceof CactusBlock
			) {
				if (getStored() > TechRebornConfig.greenhouseControllerEnergyPerBonemeal) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerBonemeal);
					blockState.randomTick((ServerLevel) level, blockPos, level.getRandom());
				}
			}
		}

		if (getStored() < TechRebornConfig.greenhouseControllerEnergyPerHarvest){
			return;
		}

		if (block instanceof CropBlock cropBlock) {
			processAgedCrop(blockState, blockPos, cropBlock.getAgeProperty(), cropBlock.getMaxAge(), 0);
		} else if (block instanceof NetherWartBlock) {
			processAgedCrop(blockState, blockPos, NetherWartBlock.AGE, 3, 0);
		} else if (block instanceof SweetBerryBushBlock) {
			processAgedCrop(blockState, blockPos, SweetBerryBushBlock.AGE, 3, 1);
		} else if (block instanceof CocoaBlock) {
			processAgedCrop(blockState, blockPos, CocoaBlock.AGE, 2, 0);
		} else if (block instanceof PumpkinBlock) {
			if (tryHarvestBlock(blockState, blockPos)) {
				level.destroyBlock(blockPos, false);
			}
		} else if (block instanceof SugarCaneBlock
				|| block instanceof CactusBlock
				|| block instanceof BambooStalkBlock
		) {
			// If we can break bottom block we should at least remove all of them up to top, so they don't break automatically
			boolean breakBlocks = false;
			for (int y = 1; (blockState = level.getBlockState(blockPos.above(y))).getBlock() == block; y++) {
				if (y == 1) {
					breakBlocks = tryHarvestBlock(blockState, blockPos.above(y));
				} else {
					tryHarvestBlock(blockState, blockPos.above(y));
				}
				if (breakBlocks) level.destroyBlock(blockPos.above(y), false);
			}
		} else if (block instanceof BlockRubberLog) {
			for (int y = 0; (blockState = level.getBlockState(blockPos.above(y))).getBlock() == block && y < 10; y++) {
				if (blockState.getValue(BlockRubberLog.HAS_SAP)
						&& insertIntoInv(Collections.singletonList(TRContent.Parts.SAP.getStack()))
				) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
					level.setBlockAndUpdate(blockPos.above(y), blockState.setValue(BlockRubberLog.HAS_SAP, false).setValue(BlockRubberLog.SAP_SIDE, Direction.from2DDataValue(0)));
				}
			}
		} else if (block instanceof CaveVines){
			for (int y=0; (blockState = level.getBlockState(blockPos.above(y))).getBlock() instanceof CaveVines; y++){
				if (blockState.getValue(BlockStateProperties.BERRIES)
					&& insertIntoInv(Collections.singletonList(new ItemStack(Items.GLOW_BERRIES, 1)))
				){
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
					level.setBlockAndUpdate(blockPos.above(y), blockState.setValue(BlockStateProperties.BERRIES, false));
				}
			}
		}
		else if (blockState.is(Blocks.MELON)) {
			if (tryHarvestBlock(blockState, blockPos)) {
				level.destroyBlock(blockPos, false);
			}
		}
	}

	private void processAgedCrop(BlockState blockState, BlockPos blockPos, IntegerProperty ageProperty, int maxAge, int newAge) {
		if (level == null) {
			return;
		}
		if (blockState.getValue(ageProperty) < maxAge) {
			return;
		}
		if (tryHarvestBlock(blockState, blockPos)) {
			level.setBlock(blockPos, blockState.setValue(ageProperty, newAge), 2);
		}
	}

	private boolean tryHarvestBlock(BlockState blockState, BlockPos blockPos) {
		if (insertIntoInv(Block.getDrops(blockState, (ServerLevel) level, blockPos, null))) {
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
		ItemStack targetStack = inventory.getItem(slot);
		if (targetStack.isEmpty()) {
			inventory.setItem(slot, stack.copy());
			stack.shrink(stack.getCount());
			return true;
		} else {
			if (ItemUtils.isItemEqual(stack, targetStack, true, false)) {
				int freeStackSpace = targetStack.getMaxStackSize() - targetStack.getCount();
				if (freeStackSpace > 0) {
					int transferAmount = Math.min(freeStackSpace, stack.getCount());
					targetStack.grow(transferAmount);
					stack.shrink(transferAmount);
					return true;
				}
			}
		}
		return false;
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClientSide()){
			return;
		}
		if (multiblockCenter == null) {
			multiblockCenter = pos.relative(getFacing().getOpposite(), range + 1);
		}

		charge(6);

		if (getStored() < getEuPerTick(TechRebornConfig.greenhouseControllerEnergyPerTick)) {
			return;
		}

		if (--ticksToNextMultiblockCheck < 0) {
			growthBoost = isShapeValid();
			ticksToNextMultiblockCheck = 200;
		}

		if (world.getGameTime() % 20 == 0) {
			double cyclesLimit = getSpeedMultiplier() * 4 + 1;
			while (cyclesLimit-- > 0) {
				workCycle();
			}
		}
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.greenhouseControllerMaxEnergy;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return TechRebornConfig.greenhouseControllerMaxInput;
	}

	// MachineBaseBlockEntity
	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		BlockState lamp = TRContent.Machine.LAMP_INCANDESCENT.block.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.DOWN);
		BlockState crop = Blocks.CACTUS.defaultBlockState();

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

	// IToolDrop
	@Override
	public ItemStack getToolDrop(Player entityPlayer) {
		return TRContent.Machine.GREENHOUSE_CONTROLLER.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<GreenhouseControllerBlockEntity> getInventory() {
		return this.inventory;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
		return new ScreenHandlerBuilder("greenhousecontroller").player(player.getInventory()).inventory().hotbar().addInventory()
				.blockEntity(this)
				.outputSlot(0, 30, 22).outputSlot(1, 48, 22)
				.outputSlot(2, 30, 40).outputSlot(3, 48, 40)
				.outputSlot(4, 30, 58).outputSlot(5, 48, 58)
				.energySlot(6, 8, 72).syncEnergyValue()
				.addInventory().create(this, syncID);
	}

}
