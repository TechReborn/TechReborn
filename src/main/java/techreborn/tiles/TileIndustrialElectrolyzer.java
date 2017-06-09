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
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.Reference;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.items.DynamicCell;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileIndustrialElectrolyzer extends TilePowerAcceptor
	implements IWrenchable, IInventoryProvider, ISidedInventory, IRecipeCrafterProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "industrial_electrolyzer", key = "IndustrialElectrolyzerMaxInput", comment = "Industrial Electrolyzer Max Input (Value in EU)")
	public static int maxInput = 128;
	@ConfigRegistry(config = "machines", category = "industrial_electrolyzer", key = "IndustrialElectrolyzerMaxEnergy", comment = "Industrial Electrolyzer Max Energy (Value in EU)")
	public static int maxEnergy = 1000;
	@ConfigRegistry(config = "machines", category = "industrial_electrolyzer", key = "IndustrialElectrolyzerTier", comment = "Industrial Electrolyzer Tier")
	public static int tier = 2;
	//  @ConfigRegistry(config = "machines", category = "industrial_electrolyzer", key = "IndustrialElectrolyzerWrenchDropRate", comment = "Industrial Electrolyzer Wrench Drop Rate")
	public static float wrenchDropRate = 1.0F;

	public int tickTime;
	public Inventory inventory = new Inventory(8, "TileIndustrialElectrolyzer", 64, this);
	public RecipeCrafter crafter;

	public TileIndustrialElectrolyzer() {
		super(tier);
		// Input slots
		final int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		final int[] outputs = new int[4];
		outputs[0] = 2;
		outputs[1] = 3;
		outputs[2] = 4;
		outputs[3] = 5;
		this.crafter = new RecipeCrafter(Reference.industrialElectrolyzerRecipe, this, 2, 4, this.inventory, inputs, outputs);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		this.crafter.updateEntity();
		this.charge(6);
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
		return new ItemStack(ModBlocks.INDUSTRIAL_ELECTROLYZER, 1);
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

	// @Override
	// public void addWailaInfo(List<String> info)
	// {
	// super.addWailaInfo(info);
	// info.add("Power Stored " + energy.getEnergyStored() +" EU");
	// if(crafter.currentRecipe !=null){
	// info.add("Power Usage " + crafter.currentRecipe.euPerTick() + " EU/t");
	// }
	// }

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return side == EnumFacing.DOWN ? new int[] { 0, 1, 2, 3, 4, 5 } : new int[] { 0, 1, 2, 3, 4, 5 };
	}

	@Override
	public boolean canInsertItem(final int slotIndex, final ItemStack stack, final EnumFacing side) {
		if (slotIndex > 1)
			return false;
		if (slotIndex == 1)
			return ItemUtils.isItemEqual(stack, DynamicCell.getEmptyCell(1), true, true);
		return !ItemUtils.isItemEqual(stack, DynamicCell.getEmptyCell(1), true, true);
	}

	@Override
	public boolean canExtractItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		return slotIndex == 2 || slotIndex == 3 || slotIndex == 4 || slotIndex == 5;
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
		return new ContainerBuilder("industrialelectrolyzer").player(player.inventory).inventory(8, 84).hotbar(8, 142)
			.addInventory().tile(this)
			.filterSlot(1, 50, 51, stack -> ItemUtils.isItemEqual(stack, DynamicCell.getEmptyCell(1), true, true))
			.filterSlot(0, 80, 51, stack -> !ItemUtils.isItemEqual(stack, DynamicCell.getEmptyCell(1), true, true))
			.outputSlot(2, 50, 19).outputSlot(3, 70, 19).outputSlot(4, 90, 19).outputSlot(5, 110, 19)
			.energySlot(6, 18, 51).syncEnergyValue().syncCrafterValue().addInventory().create();
	}
}
