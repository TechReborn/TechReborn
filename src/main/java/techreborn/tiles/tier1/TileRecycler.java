/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.tiles.tier1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.items.ItemParts;

public class TileRecycler extends TilePowerAcceptor 
		implements IToolDrop, IInventoryProvider, IContainerProvider {
	
	@ConfigRegistry(config = "machines", category = "recycler", key = "RecyclerInput", comment = "Recycler Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "recycler", key = "RecyclerMaxEnergy", comment = "Recycler Max Energy (Value in EU)")
	public static int maxEnergy = 1000;

	private final Inventory inventory = new Inventory(3, "TileRecycler", 64, this);
	private final int cost = 2;
	private final int time = 15;
	private final int chance = 6;
	private boolean isBurning;
	private int progress;

	public TileRecycler() {
		super();
	}

	public int gaugeProgressScaled(final int scale) {
		return this.progress * scale / this.time;
	}
	
	public int getProgress() {
		return this.progress;
	}

	public void setProgress(final int progress) {
		this.progress = progress;
	}
	
	public void recycleItems() {
		final ItemStack itemstack = ItemParts.getPartByName("scrap");
		final int randomchance = this.world.rand.nextInt(this.chance);

		if (randomchance == 1) {
			if (this.getStackInSlot(1).isEmpty())
				this.setInventorySlotContents(1, itemstack.copy());
			else
				this.getStackInSlot(1).grow(itemstack.getCount());
		}
		this.decrStackSize(0, 1);
	}

	public boolean canRecycle() {
		return this.getStackInSlot(0) != ItemStack.EMPTY && this.hasSlotGotSpace(1);
	}

	public boolean hasSlotGotSpace(final int slot) {
		if (this.getStackInSlot(slot) == ItemStack.EMPTY) {
			return true;
		} else if (this.getStackInSlot(slot).getCount() < this.getStackInSlot(slot).getMaxStackSize()) {
			return true;
		}
		return false;
	}

	public boolean isBurning() {
		return this.isBurning;
	}

	public void setBurning(final boolean burning) {
		this.isBurning = burning;
	}

	public void updateState() {
		final IBlockState BlockStateContainer = this.world.getBlockState(this.pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != this.isBurning)
				blockMachineBase.setActive(this.isBurning, this.world, this.pos);
		}
	}

	// TilePowerAcceptor
	@Override
	public void update() {
		super.update();
		if (this.world.isRemote)
			return;

		boolean updateInventory = false;
		if (this.canRecycle() && !this.isBurning() && this.getEnergy() != 0)
			this.setBurning(true);
		else if (this.isBurning()) {
			if (this.useEnergy(getEuPerTick(this.cost)) != getEuPerTick(this.cost))
				this.setBurning(false);
			this.progress++;
			if (this.progress >= Math.max((int) (this.time* (1.0 - getSpeedMultiplier())), 1)) {
				this.progress = 0;
				this.recycleItems();
				updateInventory = true;
				this.setBurning(false);
			}
		}

		this.updateState();
		this.charge(2);

		if (updateInventory) {
			this.markDirty();
		}
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}
	
	// TileLegacyMachineBase
	@Override
	public boolean canBeUpgraded() {
		return true;
	}
	
	// IToolDrop
	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.RECYCLER, 1);
	}

	// IInventoryProvider
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("recycler").player(player.inventory).inventory().hotbar().addInventory()
			.tile(this).slot(0, 55, 45).outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
			.syncIntegerValue(this::getProgress, this::setProgress).addInventory().create(this);
	}
}
