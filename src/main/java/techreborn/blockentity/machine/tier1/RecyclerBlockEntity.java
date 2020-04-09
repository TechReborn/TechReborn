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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.IUpgrade;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class RecyclerBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider, SlotConfiguration.SlotFilter {

	private final RebornInventory<RecyclerBlockEntity> inventory = new RebornInventory<>(3, "RecyclerBlockEntity", 64, this);
	private final int cost = 2;
	private final int time = 15;
	private final int chance = 6;
	private boolean isBurning;
	private int progress;

	public RecyclerBlockEntity() {
		super(TRBlockEntities.RECYCLER);
	}

	public int gaugeProgressScaled(int scale) {
		return progress * scale / time;
	}
	
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public void recycleItems() {
		ItemStack itemstack = TRContent.Parts.SCRAP.getStack();
		final int randomchance = this.world.random.nextInt(chance);

		if (randomchance == 1) {
			if (inventory.getStack(1).isEmpty()) {
				inventory.setStack(1, itemstack.copy());
			}
			else {
				inventory.getStack(1).increment(itemstack.getCount());
			}
		}
		inventory.shrinkSlot(0, 1);
	}

	public boolean canRecycle() {
		return !inventory.getStack(0) .isEmpty() && hasSlotGotSpace(1);
	}

	public boolean hasSlotGotSpace(int slot) {
		if (inventory.getStack(slot).isEmpty()) {
			return true;
		} else if (inventory.getStack(slot).getCount() < inventory.getStack(slot).getMaxCount()) {
			return true;
		}
		return false;
	}

	public boolean isBurning() {
		return isBurning;
	}

	public void setBurning(boolean burning) {
		this.isBurning = burning;
	}

	public void updateState() {
		final BlockState BlockStateContainer = world.getBlockState(pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			boolean shouldBurn = isBurning || (canRecycle() && canUseEnergy(getEuPerTick(cost)));
			if (BlockStateContainer.get(BlockMachineBase.ACTIVE) != shouldBurn) {
				blockMachineBase.setActive(isBurning, world, pos);
			}
		}
	}

	// TilePowerAcceptor
	@Override
	public void tick() {
		super.tick();
		if (world.isClient) {
			return;
		}
		charge(2);

		boolean updateInventory = false;
		if (canRecycle() && !isBurning() && getEnergy() != 0) {
			setBurning(true);
		}
		else if (isBurning()) {
			if (useEnergy(getEuPerTick(cost)) != getEuPerTick(cost)) {
				this.setBurning(false);
			}
			progress++;
			if (progress >= Math.max((int) (time* (1.0 - getSpeedMultiplier())), 1)) {
				progress = 0;
				recycleItems();
				updateInventory = true;
				setBurning(false);
			}
		}

		updateState();
		
		if (updateInventory) {
			markDirty();
		}
	}

	@Override
	public double getBaseMaxPower() {
		return TechRebornConfig.recyclerMaxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(Direction direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(Direction direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return TechRebornConfig.recyclerMaxInput;
	}
	
	// TileMachineBase
	@Override
	public boolean canBeUpgraded() {
		return true;
	}
	
	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return TRContent.Machine.RECYCLER.getStack();
	}

	@Override
	public boolean isStackValid(int slotID, ItemStack stack) {
		if (slotID == 0) {
			return canRecycle(stack);
		}
		return false;
	}

	@Override
	public int[] getInputSlots() {
		return new int[]{0};
	}

	// ItemHandlerProvider
	@Override
	public RebornInventory<RecyclerBlockEntity> getInventory() {
		return this.inventory;
	}

	public static boolean canRecycle(ItemStack stack) {
		Item item = stack.getItem();
		if ((item instanceof IUpgrade)) {
			return false;
		}
		return !TechRebornConfig.recyclerBlackList.contains(Registry.ITEM.getId(item).toString());
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("recycler").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 55, 45, RecyclerBlockEntity::canRecycle)
				.outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
				.sync(this::getProgress, this::setProgress).addInventory().create(this, syncID);
	}

}
