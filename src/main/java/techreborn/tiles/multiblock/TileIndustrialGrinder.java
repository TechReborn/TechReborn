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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.api.Reference;
import techreborn.api.recipe.ITileRecipeHandler;
import techreborn.api.recipe.machines.IndustrialGrinderRecipe;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileIndustrialGrinder extends TilePowerAcceptor implements IWrenchable, IInventoryProvider,
	ITileRecipeHandler<IndustrialGrinderRecipe>, IRecipeCrafterProvider, IContainerProvider {
	public static final int TANK_CAPACITY = 16000;

	public Inventory inventory = new Inventory(7, "TileGrinder", 64, this);
	public Tank tank = new Tank("TileGrinder", TileIndustrialGrinder.TANK_CAPACITY, this);
	public RecipeCrafter crafter;
	public MultiblockChecker multiblockChecker;

	public TileIndustrialGrinder() {
		super(ConfigTechReborn.CentrifugeTier);
		// TODO configs

		final int[] inputs = new int[2];
		inputs[0] = 0;
		inputs[1] = 1;
		final int[] outputs = new int[4];
		outputs[0] = 2;
		outputs[1] = 3;
		outputs[2] = 4;
		outputs[3] = 5;
		this.crafter = new RecipeCrafter(Reference.industrialGrinderRecipe, this, 1, 4, this.inventory, inputs,
			outputs);
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
		return new ItemStack(ModBlocks.INDUSTRIAL_GRINDER, 1);
	}

	public boolean getMutliBlock() {
		if(multiblockChecker == null){
			return false;
		}
		final boolean down = this.multiblockChecker.checkRectY(1, 1, MultiblockChecker.CASING_NORMAL,
			MultiblockChecker.ZERO_OFFSET);
		final boolean up = this.multiblockChecker.checkRectY(1, 1, MultiblockChecker.CASING_NORMAL,
			new BlockPos(0, 2, 0));
		final boolean blade = this.multiblockChecker.checkRingY(1, 1, MultiblockChecker.CASING_REINFORCED,
			new BlockPos(0, 1, 0));
		final IBlockState centerBlock = this.multiblockChecker.getBlock(0, 1, 0);
		final boolean center = centerBlock.getBlock() == Blocks.WATER;
		return down && center && blade && up;
	}

	@Override
	public void update() {
		super.update();

		if (this.multiblockChecker == null) {
			final BlockPos pos = this.getPos().offset(this.getFacing().getOpposite(), 2).down();
			this.multiblockChecker = new MultiblockChecker(this.world, pos);
		}

		if (this.getMutliBlock()) {
			this.crafter.updateEntity();
		}
		FluidUtils.drainContainers(this.tank, this.inventory, 1, 6);
	}

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.tank.readFromNBT(tagCompound);
		this.crafter.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		this.tank.writeToNBT(tagCompound);
		this.crafter.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
	}

	@Override
	public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.tank;
		}
		return super.getCapability(capability, facing);
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return new int[] { 0, 1, 2, 3, 4, 5, 6 };
	}

	@Override
	public boolean canInsertItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		if (slotIndex >= 2)
			return false;
		return this.isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		return slotIndex == 2 || slotIndex == 3 || slotIndex == 4 || slotIndex == 5 || slotIndex == 6;
	}

	public int getProgressScaled(final int scale) {
		if (this.crafter.currentTickTime != 0) {
			return this.crafter.currentTickTime * scale / this.crafter.currentNeededTicks;
		}
		return 0;
	}

	@Override
	public double getBaseMaxPower() {
		return 10000;
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
	public boolean canCraft(final TileEntity tile, final IndustrialGrinderRecipe recipe) {
		if(!getMutliBlock()){
			return false;
		}
		final FluidStack recipeFluid = recipe.fluidStack;
		final FluidStack tankFluid = this.tank.getFluid();
		if (recipe.fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.isFluidEqual(recipeFluid)) {
			if (tankFluid.amount >= recipeFluid.amount) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(final TileEntity tile, final IndustrialGrinderRecipe recipe) {
		final FluidStack recipeFluid = recipe.fluidStack;
		final FluidStack tankFluid = this.tank.getFluid();
		if (recipe.fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.isFluidEqual(recipeFluid)) {
			if (tankFluid.amount >= recipeFluid.amount) {
				if (tankFluid.amount == recipeFluid.amount)
					this.tank.setFluid(null);
				else
					tankFluid.amount -= recipeFluid.amount;
				return true;
			}
		}
		return false;
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
		return new ContainerBuilder("industrialgrinder").player(player.inventory).inventory().hotbar()
			.addInventory().tile(this).slot(1, 34, 35).slot(0, 84, 43).outputSlot(2, 126, 18).outputSlot(3, 126, 36)
			.outputSlot(4, 126, 54).outputSlot(5, 126, 72).outputSlot(6, 34, 55).syncEnergyValue().syncCrafterValue().addInventory()
			.create();
	}

}
