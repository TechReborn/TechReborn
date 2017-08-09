/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.IC2Duplicates;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.ItemParts;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileMatterFabricator extends TilePowerAcceptor
	implements IWrenchable, IInventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "matter_fabricator", key = "MatterFabricatorMaxInput", comment = "Matter Fabricator Max Input (Value in EU)")
	public static int maxInput = 8192;
	@ConfigRegistry(config = "machines", category = "matter_fabricator", key = "MatterFabricatorMaxEnergy", comment = "Matter Fabricator Max Energy (Value in EU)")
	public static int maxEnergy = 100000000;
	@ConfigRegistry(config = "machines", category = "matter_fabricator", key = "MatterFabricatorFabricationRate", comment = "Matter Fabricator Fabrication Rate, amount of amplifier units per UUM")
	public static int fabricationRate = 10000;
	@ConfigRegistry(config = "machines", category = "matter_fabricator", key = "MatterFabricatorEnergyPerAmp", comment = "Matter Fabricator EU per amplifier unit, multiply this with the rate for total EU")
	public static int energyPerAmp = 1666;
	//  @ConfigRegistry(config = "machines", category = "matter_fabricator", key = "MatterFabricatorWrenchDropRate", comment = "Matter Fabricator Wrench Drop Rate")
	public static float wrenchDropRate = 1.0F;

	public Inventory inventory = new Inventory(11, "TileMatterFabricator", 64, this);
	private int amplifier = 0;

	public TileMatterFabricator() {
		super();
		// TODO configs
	}

	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return this.getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return wrenchDropRate;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.MATTER_FABRICATOR, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[] { 0, 1, 2, 3, 4, 5, 6 } : new int[] { 0, 1, 2, 3, 4, 5, 6 };
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
		if (slotIndex >= 6)
			return false;
		return isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(int slotIndex, ItemStack itemStack, EnumFacing side) {
		return slotIndex >= 6 && slotIndex <= 10;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!super.world.isRemote) {
			for (int i = 0; i < 6; i++) {
				final ItemStack stack = this.inventory.getStackInSlot(i);
				if (!stack.isEmpty() && spaceForOutput()) {
					final int amp = this.getValue(stack);
					final int euNeeded = amp * energyPerAmp;
					if (amp != 0 && this.canUseEnergy(euNeeded)) {
						this.useEnergy(euNeeded);
						this.amplifier += amp;
						this.inventory.decrStackSize(i, 1);
					}
				}
			}

			if (amplifier >= fabricationRate) {
				if (spaceForOutput()) {
					this.addOutputProducts();
					amplifier -= fabricationRate;
				}
			}
		}

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
		return this.inventory.getStackInSlot(slot).isEmpty()
			|| ItemUtils.isItemEqual(this.inventory.getStackInSlot(slot), new ItemStack(ModItems.UU_MATTER), true, true)
			&& this.inventory.getStackInSlot(slot).getCount() < 64;
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

		if (this.inventory.getStackInSlot(slot).isEmpty()) {
			this.inventory.setInventorySlotContents(slot, new ItemStack(ModItems.UU_MATTER));
		} else if (ItemUtils.isItemEqual(this.inventory.getStackInSlot(slot), new ItemStack(ModItems.UU_MATTER), true, true)) {
			this.inventory.getStackInSlot(slot).setCount((Math.min(64, 1 + this.inventory.getStackInSlot(slot).getCount())));
		}
	}

	public boolean decreaseStoredEnergy(final double aEnergy, final boolean aIgnoreTooLessEnergy) {
		if (this.getEnergy() - aEnergy < 0 && !aIgnoreTooLessEnergy) {
			return false;
		} else {
			this.setEnergy(this.getEnergy() - aEnergy);
			if (this.getEnergy() < 0) {
				this.setEnergy(0);
				return false;
			} else {
				return true;
			}
		}

	}

	public int getValue(final ItemStack itemStack) {
		if (itemStack.getItem() == ModItems.PARTS && itemStack.getItemDamage() == ItemParts.getPartByName("scrap").getItemDamage()) {
			return 200;
		} else if (itemStack.getItem() == ModItems.SCRAP_BOX) {
			return 2000;
		}
		if (IC2Duplicates.SCRAP.hasIC2Stack()) {
			if (ItemUtils.isInputEqual(itemStack, IC2Duplicates.SCRAP.getIc2Stack(), true, true, true)) {
				return 200;
			}
		}
		return 0;
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

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	public int getProgress() {
		return this.amplifier;
	}

	public void setProgress(final int progress) {
		this.amplifier = progress;
	}

	public int getProgressScaled(final int scale) {
		if (this.amplifier != 0) {
			return Math.min(this.amplifier * scale / fabricationRate, 100);
		}
		return 0;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("matterfabricator").player(player.inventory).inventory().hotbar()
			.addInventory().tile(this).slot(0, 30, 20).slot(1, 50, 20).slot(2, 70, 20).slot(3, 90, 20)
			.slot(4, 110, 20).slot(5, 130, 20).outputSlot(6, 40, 66).outputSlot(7, 60, 66).outputSlot(8, 80, 66)
			.outputSlot(9, 100, 66).outputSlot(10, 120, 66).syncEnergyValue()
			.syncIntegerValue(this::getProgress, this::setProgress).addInventory().create();
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
