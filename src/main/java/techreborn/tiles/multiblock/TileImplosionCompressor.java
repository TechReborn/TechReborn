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

package techreborn.tiles.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.Inventory;
import techreborn.api.Reference;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

public class TileImplosionCompressor extends TilePowerAcceptor
	implements IWrenchable, IInventoryProvider, IRecipeCrafterProvider, IContainerProvider {

	public Inventory inventory = new Inventory(4, "TileImplosionCompressor", 64, this);
	public MultiblockChecker multiblockChecker;
	public RecipeCrafter crafter;

	public TileImplosionCompressor() {
		super();
		final int[] inputs = new int[] { 0, 1 };
		final int[] outputs = new int[] { 2, 3 };
		this.crafter = new RecipeCrafter(Reference.implosionCompressorRecipe, this, 2, 2, this.inventory, inputs, outputs);
	}

	@Override
	public void validate() {
		super.validate();
		this.multiblockChecker = new MultiblockChecker(this.world, this.getPos().down(3));
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
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.IMPLOSION_COMPRESSOR, 1);
	}

	public boolean getMutliBlock() {
		final boolean down = this.multiblockChecker.checkRectY(1, 1, MultiblockChecker.REINFORCED_CASING, MultiblockChecker.ZERO_OFFSET);
		final boolean up = this.multiblockChecker.checkRectY(1, 1, MultiblockChecker.REINFORCED_CASING, new BlockPos(0, 2, 0));
		final boolean chamber = this.multiblockChecker.checkRingYHollow(1, 1, MultiblockChecker.REINFORCED_CASING, new BlockPos(0, 1, 0));
		return down && chamber && up;
	}

	@Override
	public void update() {
		super.update();
		if (this.getMutliBlock()) {
			this.crafter.updateEntity();
		}
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

	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return new int[] { 0, 1, 2, 3 };
	}

	@Override
	public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
		return index == 0 || index == 1;
	}

	@Override
	public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
		return index == 2 || index == 3;
	}

	public int getProgressScaled(final int scale) {
		if (this.crafter.currentTickTime != 0) {
			return this.crafter.currentTickTime * scale / this.crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getBaseMaxPower() {
		return 64000;
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
		return 64;
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
		return new ContainerBuilder("implosioncompressor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
			.addInventory().tile(this).slot(0, 37, 26).slot(1, 37, 44).outputSlot(2, 93, 35).outputSlot(3, 111, 35)
			.syncEnergyValue().syncCrafterValue().addInventory().create();
	}

}
