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

package techreborn.blockentity.bases;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;

/**
 * @author drcrazy
 *
 */
public abstract class GenericMachineBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, IRecipeCrafterProvider{

	public String name;
	public int maxInput;
	public int maxEnergy;
	public Block toolDrop;
	public int energySlot;
	public RebornInventory<?> inventory;
	public RecipeCrafter crafter;
	
	/**
	 * @param name String Name for a blockEntity. Do we need it at all?
	 * @param maxInput int Maximum energy input, value in EU
	 * @param maxEnergy int Maximum energy buffer, value in EU
	 * @param toolDrop Block Block to drop with wrench
	 * @param energySlot int Energy slot to use to charge machine from battery
	 */
	public GenericMachineBlockEntity(BlockEntityType<?> blockEntityType, String name, int maxInput, int maxEnergy, Block toolDrop, int energySlot) {
		super(blockEntityType);
		this.name = "BlockEntity" + name;
		this.maxInput = maxInput;
		this.maxEnergy = maxEnergy;
		this.toolDrop = toolDrop;
		this.energySlot = energySlot;
		checkTier();
	}
	
	public int getProgressScaled(int scale) {
		if (crafter != null && crafter.currentTickTime != 0) {
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			charge(energySlot);
		}
	}
	
	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(final Direction direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final Direction direction) {
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
	
	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity p0) {
		return new ItemStack(toolDrop, 1);
	}
	
	// InventoryProvider
	@Override
	public RebornInventory<?> getInventory() {
		return inventory;
	}
	
	// IRecipeCrafterProvider
	@Override
	public RecipeCrafter getRecipeCrafter() {
		return crafter;
	}
}
