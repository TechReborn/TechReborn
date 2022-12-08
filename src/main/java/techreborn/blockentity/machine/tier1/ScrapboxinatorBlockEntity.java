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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class ScrapboxinatorBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	private final RebornInventory<ScrapboxinatorBlockEntity> inventory = new RebornInventory<>(3, "ScrapboxinatorBlockEntity", 64, this);

	public ScrapboxinatorBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.SCRAPBOXINATOR, pos, state);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
		super.tick(world, pos, state, blockEntity2);
		if (world == null || world.isClient) {
			return;
		}

	}

	// TileMachineBase
	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("scrapboxinator").player(player.getInventory()).inventory().hotbar().addInventory()
				.blockEntity(this).filterSlot(0, 55, 45, stack -> stack.getItem() == TRContent.SCRAP_BOX).outputSlot(1, 101, 45)
				.energySlot(2, 8, 72).syncEnergyValue().syncCrafterValue().addInventory().create(this, syncID);
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity p0) {
		return TRContent.Machine.SCRAPBOXINATOR.getStack();
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.scrapboxinatorMaxEnergy;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return TechRebornConfig.scrapboxinatorMaxInput;
	}
}