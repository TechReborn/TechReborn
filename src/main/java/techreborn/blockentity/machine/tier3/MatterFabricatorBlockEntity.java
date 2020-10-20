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

package techreborn.blockentity.machine.tier3;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import team.reborn.energy.EnergySide;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class MatterFabricatorBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public RebornInventory<MatterFabricatorBlockEntity> inventory = new RebornInventory<>(12, "MatterFabricatorBlockEntity", 64, this);
	private int amplifier = 0;

	public MatterFabricatorBlockEntity() {
		super(TRBlockEntities.MATTER_FABRICATOR);
	}

	private boolean spaceForOutput() {
		for (int i = 6; i < 11; i++) {
			if (spaceForOutput(i)) {
				return true;
			}
		}
		return false;
	}

	private boolean spaceForOutput(int slot) {
		return inventory.getStack(slot).isEmpty()
				|| ItemUtils.isItemEqual(inventory.getStack(slot), TRContent.Parts.UU_MATTER.getStack(), true, true)
				&& inventory.getStack(slot).getCount() < 64;
	}

	private void addOutputProducts() {
		for (int i = 6; i < 11; i++) {
			if (spaceForOutput(i)) {
				addOutputProducts(i);
				break;
			}
		}
	}

	private void addOutputProducts(int slot) {
		if (inventory.getStack(slot).isEmpty()) {
			inventory.setStack(slot, TRContent.Parts.UU_MATTER.getStack());
		} else if (ItemUtils.isItemEqual(this.inventory.getStack(slot), TRContent.Parts.UU_MATTER.getStack(), true, true)) {
			inventory.getStack(slot).setCount((Math.min(64, 1 + inventory.getStack(slot).getCount())));
		}
	}

	public boolean decreaseStoredEnergy(double aEnergy, boolean aIgnoreTooLessEnergy) {
		if (getEnergy() - aEnergy < 0 && !aIgnoreTooLessEnergy) {
			return false;
		} else {
			setEnergy(getEnergy() - aEnergy);
			if (getEnergy() < 0) {
				setEnergy(0);
				return false;
			} else {
				return true;
			}
		}
	}

	public int getValue(ItemStack itemStack) {
		if (itemStack.isItemEqualIgnoreDamage(TRContent.Parts.SCRAP.getStack())) {
			return 200;
		} else if (itemStack.getItem() == TRContent.SCRAP_BOX) {
			return 2000;
		}
		return 0;
	}

	public int getProgress() {
		return amplifier;
	}

	public void setProgress(int progress) {
		amplifier = progress;
	}

	public int getProgressScaled(int scale) {
		if (amplifier != 0) {
			return Math.min(amplifier * scale / TechRebornConfig.matterFabricatorFabricationRate, 100);
		}
		return 0;
	}

	// TilePowerAcceptor
	@Override
	public void tick() {
		super.tick();

		if (world.isClient) {
			return;
		}
		this.charge(11);

		for (int i = 0; i < 6; i++) {
			final ItemStack stack = inventory.getStack(i);
			if (!stack.isEmpty() && spaceForOutput()) {
				final int amp = getValue(stack);
				final int euNeeded = amp * TechRebornConfig.matterFabricatorEnergyPerAmp;
				if (amp != 0 && getStored(EnergySide.UNKNOWN) > euNeeded) {
					useEnergy(euNeeded);
					amplifier += amp;
					inventory.shrinkSlot(i, 1);
				}
			}
		}

		if (amplifier >= TechRebornConfig.matterFabricatorFabricationRate) {
			if (spaceForOutput()) {
				addOutputProducts();
				amplifier -= TechRebornConfig.matterFabricatorFabricationRate;
			}
		}
	}

	@Override
	public double getBaseMaxPower() {
		return TechRebornConfig.matterFabricatorMaxEnergy;
	}

	@Override
	public boolean canProvideEnergy(EnergySide side) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return TechRebornConfig.matterFabricatorMaxInput;
	}

	// TileMachineBase
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return TRContent.Machine.MATTER_FABRICATOR.getStack();
	}

	// ItemHandlerProvider
	@Override
	public RebornInventory<MatterFabricatorBlockEntity> getInventory() {
		return inventory;
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("matterfabricator").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 30, 20).slot(1, 50, 20).slot(2, 70, 20).slot(3, 90, 20).slot(4, 110, 20)
				.slot(5, 130, 20).outputSlot(6, 40, 66).outputSlot(7, 60, 66).outputSlot(8, 80, 66)
				.outputSlot(9, 100, 66).outputSlot(10, 120, 66).energySlot(11, 8, 72).syncEnergyValue()
				.sync(this::getProgress, this::setProgress).addInventory().create(this, syncID);
	}
}
