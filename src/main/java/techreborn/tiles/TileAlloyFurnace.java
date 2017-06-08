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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.api.recipe.IBaseRecipeType;
import reborncore.api.recipe.RecipeHandler;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.recipes.RecipeTranslator;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileAlloyFurnace extends TileLegacyMachineBase
	implements IWrenchable, IInventoryProvider, IContainerProvider {

//	@ConfigRegistry(config = "machines", category = "alloy_furnace", key = "AlloyFurnaceWrenchDropRate", comment = "Alloy Furnace Wrench Drop Rate")
	public static float WRENCH_DROP_RATE = 1.0F;

	public int tickTime;
	public Inventory inventory = new Inventory(4, "TileAlloyFurnace", 64, this);
	public int burnTime;
	public int currentItemBurnTime;
	public int cookTime;
	int input1 = 0;
	int input2 = 1;
	int output = 2;
	int fuel = 3;

	public TileAlloyFurnace() {

	}

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the
	 * furnace burning, or 0 if the item isn't fuel
	 */
	public static int getItemBurnTime(final ItemStack stack) {
		if (stack == null) {
			return 0;
		} else {
			final Item item = stack.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {
				final Block block = Block.getBlockFromItem(item);

				if (block == Blocks.WOODEN_SLAB) {
					return 150;
				}

				if (block.getMaterial(block.getDefaultState()) == Material.WOOD) {
					return 300;
				}

				if (block == Blocks.COAL_BLOCK) {
					return 16000;
				}
			}

			if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD"))
				return 200;
			if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD"))
				return 200;
			// if (item instanceof ItemHoe && ((ItemHoe)
			// item).getToolMaterialName().equals("WOOD")) return 200;
			if (item == Items.STICK)
				return 100;
			if (item == Items.COAL)
				return 1600;
			if (item == Items.LAVA_BUCKET)
				return 20000;
			if (item == Item.getItemFromBlock(Blocks.SAPLING))
				return 100;
			if (item == Items.BLAZE_ROD)
				return 2400;
			return GameRegistry.getFuelValue(stack);
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		final boolean flag = this.burnTime > 0;
		boolean flag1 = false;
		if (this.burnTime > 0) {
			--this.burnTime;
		}
		if (!this.world.isRemote) {
			if (this.burnTime != 0 || this.getStackInSlot(this.input1) != ItemStack.EMPTY && this.getStackInSlot(this.fuel) != ItemStack.EMPTY) {
				if (this.burnTime == 0 && this.canSmelt()) {
					this.currentItemBurnTime = this.burnTime = TileAlloyFurnace.getItemBurnTime(this.getStackInSlot(this.fuel));
					if (this.burnTime > 0) {
						flag1 = true;
						if (this.getStackInSlot(this.fuel) != ItemStack.EMPTY) {
							this.decrStackSize(this.fuel, 1);
						}
					}
				}
				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;
					if (this.cookTime == 200) {
						this.cookTime = 0;
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			}
			if (flag != this.burnTime > 0) {
				flag1 = true;
				// TODO sync on/off
			}
		}
		if (flag1) {
			this.markDirty();
		}
	}

	public boolean hasAllInputs(final IBaseRecipeType recipeType) {
		if (recipeType == null) {
			return false;
		}
		for (final Object input : recipeType.getInputs()) {
			boolean hasItem = false;
			boolean useOreDict = input instanceof String || recipeType.useOreDic();
			boolean checkSize = input instanceof ItemStack;
			for (int inputslot = 0; inputslot < 2; inputslot++) {
				if (ItemUtils.isInputEqual(input, inventory.getStackInSlot(inputslot), true, true,
					useOreDict)) {
					ItemStack stack = RecipeTranslator.getStackFromObject(input);
					if (!checkSize || inventory.getStackInSlot(inputslot).getCount() >= stack.getCount()) {
						hasItem = true;
					}
				}
			}
			if (!hasItem)
				return false;
		}
		return true;
	}

	private boolean canSmelt() {
		if (this.getStackInSlot(this.input1) == ItemStack.EMPTY || this.getStackInSlot(this.input2) == ItemStack.EMPTY) {
			return false;
		} else {
			ItemStack itemstack = null;
			for (final IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.alloySmelterRecipe)) {
				if (this.hasAllInputs(recipeType)) {
					itemstack = recipeType.getOutput(0);
					break;
				}
			}

			if (itemstack == null)
				return false;
			if (this.getStackInSlot(this.output) == ItemStack.EMPTY)
				return true;
			if (!this.getStackInSlot(this.output).isItemEqual(itemstack))
				return false;
			final int result = this.getStackInSlot(this.output).getCount() + itemstack.getCount();
			return result <= this.getInventoryStackLimit() && result <= this.getStackInSlot(this.output).getMaxStackSize(); // Forge
			// BugFix:
			// Make
			// it
			// respect
			// stack
			// sizes
			// properly.
		}
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted
	 * item in the furnace result stack
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = ItemStack.EMPTY;
			for (final IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.alloySmelterRecipe)) {
				if (this.hasAllInputs(recipeType)) {
					itemstack = recipeType.getOutput(0);
					break;
				}
				if (itemstack != ItemStack.EMPTY) {
					break;
				}
			}

			if (this.getStackInSlot(this.output) == ItemStack.EMPTY) {
				this.setInventorySlotContents(this.output, itemstack.copy());
			} else if (this.getStackInSlot(this.output).getItem() == itemstack.getItem()) {
				this.decrStackSize(this.output, -itemstack.getCount());
			}

			for (final IBaseRecipeType recipeType : RecipeHandler.getRecipeClassFromName(Reference.alloySmelterRecipe)) {
				boolean hasAllRecipes = true;
				if (this.hasAllInputs(recipeType)) {

				} else {
					hasAllRecipes = false;
				}
				if (hasAllRecipes) {
					for (Object input : recipeType.getInputs()) {
						boolean useOreDict = input instanceof String || recipeType.useOreDic();
						for (int inputSlot = 0; inputSlot < 2; inputSlot++) {
							if (ItemUtils.isInputEqual(input, this.inventory.getStackInSlot(inputSlot), true, true,
								recipeType.useOreDic())) {
								int count = 1;
								if (input instanceof ItemStack) {
									count = RecipeTranslator.getStackFromObject(input).getCount();
								}
								inventory.decrStackSize(inputSlot, count);
								break;
							}
						}
					}
				}
			}

		}
	}

	/**
	 * Furnace isBurning
	 */
	public boolean isBurning() {
		return this.burnTime > 0;
	}

	public int getBurnTimeRemainingScaled(final int scale) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = 200;
		}

		return this.burnTime * scale / this.currentItemBurnTime;
	}

	public int getCookProgressScaled(final int scale) {
		return this.cookTime * scale / 200;
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
		return WRENCH_DROP_RATE;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.IRON_ALLOY_FURNACE, 1);
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return new int[] { 0, 1, 2 };
	}

	@Override
	public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
		return index == 0 || index == 1;
	}

	@Override
	public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
		return index == 2;
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	public void setBurnTime(final int burnTime) {
		this.burnTime = burnTime;
	}

	public int getCurrentItemBurnTime() {
		return this.currentItemBurnTime;
	}

	public void setCurrentItemBurnTime(final int currentItemBurnTime) {
		this.currentItemBurnTime = currentItemBurnTime;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	public void setCookTime(final int cookTime) {
		this.cookTime = cookTime;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("alloyfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
			.addInventory().tile(this)
			.filterSlot(0, 47, 17,
				stack -> RecipeHandler.recipeList.stream()
					.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
						&& ItemUtils.isInputEqual(recipe.getInputs().get(0), stack, true, true, true)))
			.filterSlot(1, 65, 17,
				stack -> RecipeHandler.recipeList.stream()
					.anyMatch(recipe -> recipe instanceof AlloySmelterRecipe
						&& ItemUtils.isInputEqual(recipe.getInputs().get(1), stack, true, true, true)))
			.outputSlot(2, 116, 35).fuelSlot(3, 56, 53).syncIntegerValue(this::getBurnTime, this::setBurnTime)
			.syncIntegerValue(this::getCookTime, this::setCookTime)
			.syncIntegerValue(this::getCurrentItemBurnTime, this::setCurrentItemBurnTime).addInventory().create();
	}
}
