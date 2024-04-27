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

package techreborn.blockentity.storage.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidUtils;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.List;

public class TankUnitBaseBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, IToolDrop, IListInfoProvider, BuiltScreenHandlerProvider {
	protected Tank tank;
	private long serverMaxCapacity = -1;

	protected final RebornInventory<TankUnitBaseBlockEntity> inventory = new RebornInventory<>(2, "TankInventory", 64, this);

	private TRContent.TankUnit type;

	public TankUnitBaseBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.TANK_UNIT, pos, state);
	}

	public TankUnitBaseBlockEntity(BlockPos pos, BlockState state, TRContent.TankUnit type) {
		super(TRBlockEntities.TANK_UNIT, pos, state);
		configureEntity(type);
	}

	private void configureEntity(TRContent.TankUnit type) {
		this.type = type;
		this.tank = new Tank("TankStorage", serverMaxCapacity == -1 ? type.capacity : FluidValue.fromRaw(serverMaxCapacity), this);
	}

	protected boolean canDrainTransfer(){
		if (inventory == null || inventory.size() < 2){
			return false;
		}
		ItemStack firstStack = inventory.getStack(0);
		if (firstStack.isEmpty()){
			return false;
		}
		ItemStack secondStack = inventory.getStack(1);
		return secondStack.getCount() < secondStack.getMaxCount();
	}

	// MachineBaseBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);

		if (world == null || world.isClient()){
			return;
		}

		if (canDrainTransfer() && FluidUtils.isContainer(inventory.getStack(0))) {
			boolean didSomething = FluidUtils.drainContainers(tank, inventory, 0, 1);
			if(!didSomething && FluidUtils.fillContainers(tank, inventory, 0, 1)){
				didSomething = true;
			}
			if(didSomething){
				if(inventory.getStack(1).isEmpty() && !inventory.getStack(0).isEmpty() && inventory.getStack(0).getCount() == 1){
					inventory.setStack(1, inventory.getStack(0));
					inventory.setStack(0, ItemStack.EMPTY);
				}
				syncWithAll();
			}
		}
		// allow infinite fluid input for creative tank
		if (type == TRContent.TankUnit.CREATIVE) {
			if (!tank.isEmpty() && !tank.getFluidAmount().equals(tank.getFluidValueCapacity().fraction(2))) {
				tank.setFluidAmount(tank.getFluidValueCapacity().fraction(2));
			}
		}
		// Void excessive fluid in creative tank (#2205)
		if (type == TRContent.TankUnit.CREATIVE && tank.isFull()) {
			FluidUtils.drainContainers(tank, inventory, 0, 1, true);
		}
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public void readNbt(final NbtCompound tagCompound, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(tagCompound, registryLookup);
		if (tagCompound.contains("unitType")) {
			this.type = TRContent.TankUnit.valueOf(tagCompound.getString("unitType"));
			configureEntity(type);
			tank.read(tagCompound);
		}
	}

	@Override
	public void writeNbt(final NbtCompound tagCompound, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(tagCompound, registryLookup);
		tagCompound.putString("unitType", this.type.name());
		tank.write(tagCompound);
	}

	@Override
	public FluidValue fluidTransferAmount() {
		// Full capacity should be filled in four minutes (4 minutes * 20 ticks per second / slotTransferSpeed equals 4)
		return type.capacity.fraction(1200);
	}

	// InventoryProvider
	@Override
	public RebornInventory<TankUnitBaseBlockEntity> getInventory() {
		return this.inventory;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity playerEntity) {
		ItemStack dropStack = new ItemStack(getBlockType(), 1);
		final NbtCompound nbt = new NbtCompound();
		if (world != null){
			writeNbt(nbt, world.getRegistryManager());
			dropStack.set(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(nbt));
		}

		return dropStack;
	}

	// IListInfoProvider
	@Override
	public void addInfo(final List<Text> info, final boolean isReal, boolean hasData) {
		if (isReal || hasData) {
			if (!this.tank.getFluidInstance().isEmpty()) {
				info.add(
						Text.literal(String.valueOf(this.tank.getFluidAmount()))
								.append(Text.translatable("techreborn.tooltip.unit.divider"))
								.append(WordUtils.capitalize(FluidUtils.getFluidName(this.tank.getFluid())))
				);
			} else {
				info.add(Text.translatable("techreborn.tooltip.unit.empty"));
			}
		}
		info.add(
				Text.translatable("techreborn.tooltip.unit.capacity")
						.formatted(Formatting.GRAY)
						.append(Text.literal(String.valueOf(this.tank.getFluidValueCapacity()))
								.formatted(Formatting.GOLD))
		);
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("tank").player(player.getInventory()).inventory().hotbar()
				.addInventory().blockEntity(this).fluidSlot(0, 100, 53).outputSlot(1, 140, 53)
				.sync(tank)
				.sync(PacketCodecs.VAR_LONG, this::getMaxCapacity, this::setMaxCapacity)

				.addInventory().create(this, syncID);
	}

	// Sync between server/client if configs are mis-matched.
	public long getMaxCapacity() {
		return this.tank.getFluidValueCapacity().getRawValue();
	}

	public void setMaxCapacity(long maxCapacity) {
		FluidInstance instance = tank.getFluidInstance();
		this.tank = new Tank("TankStorage", FluidValue.fromRaw(maxCapacity), this);
		this.tank.setFluidInstance(instance);
		this.serverMaxCapacity = maxCapacity;
	}

	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}
}
