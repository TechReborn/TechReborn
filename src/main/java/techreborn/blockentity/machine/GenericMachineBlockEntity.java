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

package techreborn.blockentity.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.api.recipe.IRecipeCrafterProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.util.RebornInventory;


/**
 * @author drcrazy
 */
public abstract class GenericMachineBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, IRecipeCrafterProvider {

	public String name;
	public int maxInput;
	public int maxEnergy;
	public Block toolDrop;
	public int energySlot;
	public RebornInventory<?> inventory;
	public RecipeCrafter crafter;

	/**
	 * @param name       {@link String} Name for a {@link BlockEntity}. Do we need it at all?
	 * @param maxInput   {@code int} Maximum energy input, value in EU
	 * @param maxEnergy  {@code int} Maximum energy buffer, value in EU
	 * @param toolDrop   {@link Block} Block to drop with wrench
	 * @param energySlot {@code int} Energy slot to use to charge machine from battery
	 */
	public GenericMachineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, String name, int maxInput, int maxEnergy, Block toolDrop, int energySlot) {
		super(blockEntityType, pos, state);
		this.name = "BlockEntity" + name;
		this.maxInput = maxInput;
		this.maxEnergy = maxEnergy;
		this.toolDrop = toolDrop;
		this.energySlot = energySlot;
		checkTier();
	}

	/**
	 * Returns progress scaled to input value
	 *
	 * @param scale {@code int} Maximum value for progress
	 * @return {@code int} Scale of progress
	 */
	public int getProgressScaled(int scale) {
		if (crafter != null && crafter.currentTickTime != 0 && crafter.currentNeededTicks != 0) {
			return crafter.currentTickTime * scale / crafter.currentNeededTicks;
		}
		return 0;
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) {
			return;
		}
		if (energySlot != -1) {
			charge(energySlot);
		}
	}

	@Override
	public long getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
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
