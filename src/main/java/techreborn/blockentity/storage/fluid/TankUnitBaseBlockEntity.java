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

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.component.TypedEntityData;
import org.apache.commons.lang3.text.WordUtils;
import org.jspecify.annotations.Nullable;
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
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import static techreborn.TechReborn.LOGGER;

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
		this.tank = new Tank("TankStorage", serverMaxCapacity == -1 ? type.capacity : FluidValue.fromRaw(serverMaxCapacity));
	}

	protected boolean canDrainTransfer(){
		if (inventory == null || inventory.getContainerSize() < 2){
			return false;
		}
		ItemStack firstStack = inventory.getItem(0);
		if (firstStack.isEmpty()){
			return false;
		}
		ItemStack secondStack = inventory.getItem(1);
		return secondStack.getCount() < secondStack.getMaxStackSize();
	}

	// MachineBaseBlockEntity
	@Override
	public void tick(Level level, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(level, pos, state, blockEntity);

		if (!(level instanceof ServerLevel)){
			return;
		}

		if (canDrainTransfer() && FluidUtils.isContainer(inventory.getItem(0))) {
			boolean didSomething = FluidUtils.drainContainers(tank, inventory, 0, 1);
			if(!didSomething && FluidUtils.fillContainers(tank, inventory, 0, 1)){
				didSomething = true;
			}
			if(didSomething){
				if(inventory.getItem(1).isEmpty() && !inventory.getItem(0).isEmpty() && inventory.getItem(0).getCount() == 1){
					inventory.setItem(1, inventory.getItem(0));
					inventory.setItem(0, ItemStack.EMPTY);
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
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		view.getString("unitType").ifPresent(name -> {
			this.type = TRContent.TankUnit.valueOf(name);
			configureEntity(type);
			tank.read(view);
		});
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putString("unitType", this.type.name());
		tank.write(view);
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
	public ItemStack getToolDrop(Player playerEntity) {
		ItemStack dropStack = new ItemStack(getBlockType(), 1);
		if (level != null){
			try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(problemPath(), LOGGER)) {
				TagValueOutput view = TagValueOutput.createWithContext(logging, level.registryAccess());
				saveAdditional(view);
				dropStack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(getType(), view.buildResult()));
			}
		}

		return dropStack;
	}

	// IListInfoProvider
	@Override
	public void addInfo(final List<Component> info, final boolean isReal, boolean hasData) {
		if (isReal || hasData) {
			if (!this.tank.getFluidInstance().isEmpty()) {
				info.add(
						Component.literal(String.valueOf(this.tank.getFluidAmount()))
								.append(Component.translatable("techreborn.tooltip.unit.divider"))
								.append(WordUtils.capitalize(FluidUtils.getFluidName(this.tank.getFluid())))
				);
			} else {
				info.add(Component.translatable("techreborn.tooltip.unit.empty"));
			}
		}
		info.add(
				Component.translatable("techreborn.tooltip.unit.capacity")
						.withStyle(ChatFormatting.GRAY)
						.append(Component.literal(String.valueOf(this.tank.getFluidValueCapacity()))
								.withStyle(ChatFormatting.GOLD))
		);
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final Player player) {
		return new ScreenHandlerBuilder("tank").player(player.getInventory()).inventory().hotbar()
				.addInventory().blockEntity(this).fluidSlot(0, 100, 53).outputSlot(1, 140, 53)
				.sync(tank)
				.sync(ByteBufCodecs.VAR_LONG, this::getMaxCapacity, this::setMaxCapacity)

				.addInventory().create(this, syncID);
	}

	// Sync between server/client if configs are mis-matched.
	public long getMaxCapacity() {
		return this.tank.getFluidValueCapacity().getRawValue();
	}

	public void setMaxCapacity(long maxCapacity) {
		FluidInstance instance = tank.getFluidInstance();
		this.tank = new Tank("TankStorage", FluidValue.fromRaw(maxCapacity));
		this.tank.setFluidInstance(instance);
		this.serverMaxCapacity = maxCapacity;
	}

	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}
}
