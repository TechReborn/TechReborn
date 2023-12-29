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

package techreborn.blockentity.machine.tier2;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.List;

public class FishingStationBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	private final RebornInventory<FishingStationBlockEntity> inventory = new RebornInventory<>(7, "FishingStationBlockEntity", 64, this);

	public FishingStationBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.FISHING_STATION, pos, state);
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
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient){
			return;
		}

		charge(6);

		long useRequirement = getEuPerTick(TechRebornConfig.fishingStationEnergyPerCatch);

		if (getStored() < useRequirement) {
			return;
		}

		int speed = (int) Math.round(getSpeedMultiplier() / TechRebornConfig.overclockerSpeed) + 1;

		if (world.getTime() % (TechRebornConfig.fishingStationInterval/speed) != 0) {
			return;
		}

		BlockPos frontPos = pos.offset(getFacing());
		FluidState frontFluid = world.getFluidState(frontPos);
		if (!frontFluid.isEqualAndStill(Fluids.WATER)) {
			return;
		}


		final LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder((ServerWorld) world)
			.add(LootContextParameters.ORIGIN, Vec3d.ofCenter(frontPos))
			.add(LootContextParameters.TOOL, TRContent.Machine.FISHING_STATION.getStack())
			.build(LootContextTypes.FISHING);

		final LootTable lootTable = world.getServer().getLootManager().getLootTable(LootTables.FISHING_GAMEPLAY);
		final ObjectArrayList<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);
		if (insertIntoInv(list)){
			useEnergy(useRequirement);
		}
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.fishingStationMaxEnergy;
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return TechRebornConfig.fishingStationMaxInput;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity p0) {
		return TRContent.Machine.FISHING_STATION.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<FishingStationBlockEntity> getInventory() {
		return this.inventory;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("fishing_station")
				.player(player.getInventory())
				.inventory().hotbar().addInventory()
				.blockEntity(this)
				.outputSlot(0, 30, 22).outputSlot(1, 48, 22)
				.outputSlot(2, 30, 40).outputSlot(3, 48, 40)
				.outputSlot(4, 30, 58).outputSlot(5, 48, 58)
				.energySlot(6, 8, 72).syncEnergyValue()
				.addInventory().create(this, syncID);
	}
}
