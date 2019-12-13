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

package techreborn.blockentity.machine.tier3;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;
import techreborn.utils.FluidUtils;

import javax.annotation.Nullable;
import java.util.List;

public class QuantumTankBlockEntity extends MachineBaseBlockEntity
	implements InventoryProvider, IToolDrop, IListInfoProvider, IContainerProvider {

	public Tank tank = new Tank("QuantumTankBlockEntity", FluidValue.INFINITE, this);
	public RebornInventory<QuantumTankBlockEntity> inventory = new RebornInventory<>(3, "QuantumTankBlockEntity", 64, this);

	public QuantumTankBlockEntity(){
		this(TRBlockEntities.QUANTUM_TANK);
	}

	public QuantumTankBlockEntity(BlockEntityType<?> blockEntityTypeIn) {
		super(blockEntityTypeIn);
	}

	public void readWithoutCoords(final CompoundTag tagCompound) {
		tank.read(tagCompound);
	}
	
	public CompoundTag writeWithoutCoords(final CompoundTag tagCompound) {
		tank.write(tagCompound);
		return tagCompound;
	}
	
	public ItemStack getDropWithNBT() {
		final CompoundTag blockEntity = new CompoundTag();
		final ItemStack dropStack = TRContent.Machine.QUANTUM_TANK.getStack();
		this.writeWithoutCoords(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getTag().put("blockEntity", blockEntity);
		return dropStack;
	}
	
	// TileMachineBase
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			if (FluidUtils.drainContainers(tank, inventory, 0, 1)
					|| FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluid())) {
				this.syncWithAll();
			}
				
		}
	}
	
	@Override
	public boolean canBeUpgraded() {
		return false;
	}
	
	@Override
	public void fromTag(final CompoundTag tagCompound) {
		super.fromTag(tagCompound);
		readWithoutCoords(tagCompound);
	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		writeWithoutCoords(tagCompound);
		return tagCompound;
	}
	
	// ItemHandlerProvider
	@Override
	public RebornInventory<QuantumTankBlockEntity> getInventory() {
		return this.inventory;
	}
	
	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return this.getDropWithNBT();
	}
	
	// IListInfoProvider
	@Override
	public void addInfo(final List<Text> info, final boolean isReal, boolean hasData) {
		if (isReal | hasData) {
			if (!this.tank.getFluidInstance().isEmpty()) {
				info.add(new LiteralText(this.tank.getFluidAmount() + " of " + this.tank.getFluid()));
			} else {
				info.add(new LiteralText("Empty"));
			}
		}
		info.add(new LiteralText("Capacity " + this.tank.getCapacity()));
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("quantumtank").player(player.inventory).inventory().hotbar()
			.addInventory().blockEntity(this).fluidSlot(0, 80, 17).outputSlot(1, 80, 53)
			.sync(tank).addInventory().create(this, syncID);
	}

	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}
}
