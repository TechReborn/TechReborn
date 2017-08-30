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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.IListInfoProvider;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.items.DynamicCell;
import techreborn.lib.ModInfo;

import java.util.List;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileIndustrialCentrifuge extends TilePowerAcceptor
	implements IToolDrop, IInventoryProvider, IListInfoProvider, IRecipeCrafterProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "centrifuge", key = "CentrifugeMaxInput", comment = "Centrifuge Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "centrifuge", key = "CentrifugeMaxEnergy", comment = "Centrifuge Max Energy (Value in EU)")
	public static int maxEnergy = 10000;
	//  @ConfigRegistry(config = "machines", category = "centrifuge", key = "CentrifugeWrenchDropRate", comment = "Centrifuge Wrench Drop Rate")
	public static float wrenchDropRate = 1.0F;

	public int tickTime;
	public Inventory inventory = new Inventory(11, "TileIndustrialCentrifuge", 64, this);
	public RecipeCrafter crafter;

	public TileIndustrialCentrifuge() {
		super();
		// Input slots
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[4];
		outputs[0] = 2;
		outputs[1] = 3;
		outputs[2] = 4;
		outputs[3] = 5;

		this.crafter = new RecipeCrafter(Reference.centrifugeRecipe, this, 2, 4, this.inventory, inputs, outputs);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		this.crafter.updateEntity();
		this.charge(6);
		if (this.inventory.getStackInSlot(6) != ItemStack.EMPTY) {
			final ItemStack stack = this.inventory.getStackInSlot(6);
			if (stack.getItem() instanceof IEnergyItemInfo) {
				final IEnergyItemInfo item = (IEnergyItemInfo) stack.getItem();
				if (item.canProvideEnergy(stack)) {
					if (this.getEnergy() != this.getMaxPower()) {
						this.addEnergy(item.getMaxTransfer(stack));
						PoweredItem.setEnergy(PoweredItem.getEnergy(stack) - item.getMaxTransfer(stack), stack);
					}
				}
			}
		}
	}

	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.INDUSTRIAL_CENTRIFUGE, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.crafter.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		this.crafter.writeToNBT(tagCompound);
		return tagCompound;
	}

	// ISidedInventory

	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[] { 0, 1, 2, 3, 4, 5 } : new int[] { 0, 1, 2, 3, 4, 5 };
	}

	@Override
	public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
		return itemStackIn.isItemEqual(DynamicCell.getEmptyCell(1).copy()) ? index == 1 : index == 0;
	}

	@Override
	public boolean canExtractItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		return slotIndex >= 2 && slotIndex <= 5;
	}

	@Override
	public void addInfo(final List<String> info, final boolean isRealTile) {
		super.addInfo(info, isRealTile);
		info.add("Round and round it goes");
	}

	public int getProgressScaled(final int scale) {
		if (this.crafter.currentTickTime != 0) {
			return this.crafter.currentTickTime * scale / this.crafter.currentNeededTicks;
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

	@Override
	public RecipeCrafter getRecipeCrafter() {
		return this.crafter;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("centrifuge").player(player.inventory).inventory().hotbar()
			.addInventory().tile(this).slot(0, 40, 34).slot(1, 40, 54).outputSlot(2, 82, 44).outputSlot(3, 101, 25)
			.outputSlot(4, 120, 44).outputSlot(5, 101, 63).energySlot(6, 8, 72).syncEnergyValue()
			.syncCrafterValue().addInventory().create();
	}

}
