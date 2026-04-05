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

package techreborn.blockentity.storage.energy;

import net.minecraft.world.item.component.TypedEntityData;
import org.jspecify.annotations.Nullable;
import reborncore.api.blockentity.IUpgrade;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import static techreborn.TechReborn.LOGGER;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class AdjustableSUBlockEntity extends EnergyStorageBlockEntity implements BuiltScreenHandlerProvider {

	public RebornInventory<AdjustableSUBlockEntity> inventory = new RebornInventory<>(4, "AdjustableSUBlockEntity", 64, this);
	private int OUTPUT = 64; // The current output
	public int superconductors = 0;

	public AdjustableSUBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.ADJUSTABLE_SU, pos, state, "ADJUSTABLE_SU", 4, TRContent.Machine.ADJUSTABLE_SU.block, RcEnergyTier.INSANE, TechRebornConfig.aesuMaxEnergy);
	}

	public int getMaxConfigOutput() {
		int extra = 0;
		if (superconductors > 0) {
			extra = (int) Math.pow(2, (superconductors + 2)) * maxOutput;
		}
		return maxOutput + extra;
	}

	public void handleGuiInputFromClient(int id, boolean shift, boolean ctrl) {
		if (shift) {
			id *= 4;
		}
		if (ctrl) {
			id *= 8;
		}

		OUTPUT += id;

		if (OUTPUT > getMaxConfigOutput()) {
			OUTPUT = getMaxConfigOutput();
		}
		if (OUTPUT <= 0) {
			OUTPUT = 0;
		}
	}

	// EnergyStorageBlockEntity
	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClientSide()) {
			return;
		}

		if (OUTPUT > getMaxConfigOutput()) {
			OUTPUT = getMaxConfigOutput();
		}
		if (world.getGameTime() % 20 == 0) {
			checkTier();
		}
	}

	@Override
	public boolean canBeUpgraded() {
		return true;
	}

	@Override
	public ItemStack getToolDrop(Player entityPlayer) {
		ItemStack dropStack = TRContent.Machine.ADJUSTABLE_SU.getStack();
		if (level != null){
			try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(problemPath(), LOGGER)) {
				TagValueOutput view = TagValueOutput.createWithContext(logging, level.registryAccess());
				saveAdditional(view);
				dropStack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(getType(), view.buildResult()));
			}
		}

		return dropStack;
	}

	@Override
	public long getBaseMaxOutput() {
		return OUTPUT;
	}

	@Override
	public long getBaseMaxInput() {
		// If we have super conductors increase the max input of the machine
		if (getMaxConfigOutput() > maxOutput) {
			return getMaxConfigOutput();
		}
		return maxInput;
	}

	// PowerAcceptorBlockEntity
	@Override
	public void resetUpgrades() {
		super.resetUpgrades();
		superconductors = 0;
	}

	@Override
	public long getMaxOutput(@Nullable Direction side) {
		return OUTPUT;
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putInt("output", OUTPUT);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		this.OUTPUT = view.getIntOr("output", 0);
	}

	// MachineBaseBlockEntity
	@Override
	public boolean isUpgradeValid(IUpgrade upgrade, ItemStack stack) {
		return stack.is(TRContent.Upgrades.SUPERCONDUCTOR.item);
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
		return new ScreenHandlerBuilder("aesu").player(player.getInventory()).inventory().hotbar().armor()
			.complete(8, 18).addArmor().addInventory().blockEntity(this).energySlot(0, 62, 45).energySlot(1, 98, 45)
			.syncEnergyValue().sync(ByteBufCodecs.INT, this::getCurrentOutput, this::setCurrentOutput).addInventory().create(this, syncID);
	}

	public int getCurrentOutput() {
		return OUTPUT;
	}

	public void setCurrentOutput(int output) {
		this.OUTPUT = output;
	}
}
