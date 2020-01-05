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

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.text.WordUtils;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidUtil;
import reborncore.common.fluid.FluidValue;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.StringUtils;
import reborncore.common.util.Tank;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.utils.FluidUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class TankUnitBaseBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, IToolDrop, IListInfoProvider, IContainerProvider {
	protected Tank tank;
	protected RebornInventory<TankUnitBaseBlockEntity> inventory = new RebornInventory<>(2, "TankInventory", 64, this);

	private TRContent.TankUnit type;

	public TankUnitBaseBlockEntity() {
		super(TRBlockEntities.TANK_UNIT);
	}

	public TankUnitBaseBlockEntity(TRContent.TankUnit type) {
		super(TRBlockEntities.TANK_UNIT);
		configureEntity(type);
	}


	public TankUnitBaseBlockEntity(BlockEntityType<?> blockEntityTypeIn, TRContent.TankUnit type) {
		super(blockEntityTypeIn);
		this.type = type;
	}

	private void configureEntity(TRContent.TankUnit type) {
		this.type = type;
		this.tank = new Tank("TankStorage", type.capacity, this);
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isClient()) {
			return;
		}

		if (FluidUtils.drainContainers(tank, inventory, 0, 1)
			|| FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluid())) {

			if (type == TRContent.TankUnit.CREATIVE) {
				if (!tank.isEmpty() && !tank.isFull()) {
					tank.setFluidAmount(FluidValue.INFINITE);
				}
			}
			syncWithAll();
		}
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public void fromTag(final CompoundTag tagCompound) {
		super.fromTag(tagCompound);
		if (tagCompound.contains("unitType")) {
			this.type = TRContent.TankUnit.valueOf(tagCompound.getString("unitType"));
			configureEntity(type);
			tank.read(tagCompound);
		} else {
			// SAVE COMPAT
			if (tagCompound.contains("QuantumTankBlockEntity")) {
				this.type = TRContent.TankUnit.QUANTUM;
				Tank temp = new Tank("QuantumTankBlockEntity", type.capacity, this);
				temp.read(tagCompound);

				// Set tank name to what it should be
				tank = new Tank("TankStorage", type.capacity, this);
				tank.setFluid(null, temp.getFluidInstance().copy());
			}
		}


	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tagCompound.putString("unitType", this.type.name());
		tank.write(tagCompound);
		return tagCompound;
	}

	public ItemStack getDropWithNBT() {
		ItemStack dropStack = new ItemStack(getBlockType(), 1);
		final CompoundTag blockEntity = new CompoundTag();
		this.toTag(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getOrCreateTag().put("blockEntity", blockEntity);
		return dropStack;
	}

	// ItemHandlerProvider
	@Override
	public RebornInventory<TankUnitBaseBlockEntity> getInventory() {
		return this.inventory;
	}

	// IListInfoProvider
	@Override
	public void addInfo(final List<Text> info, final boolean isReal, boolean hasData) {
		if (isReal || hasData) {
			if (!this.tank.getFluidInstance().isEmpty()) {
				info.add(new LiteralText(this.tank.getFluidAmount() + StringUtils.t("techreborn.tooltip.unit.divider") + WordUtils.capitalize(FluidUtil.getFluidName(this.tank.getFluid()))));
			} else {
				info.add(new TranslatableText("techreborn.tooltip.unit.empty"));
			}
		}
		info.add(new LiteralText(Formatting.GRAY + StringUtils.t("techreborn.tooltip.unit.capacity") + Formatting.GOLD + this.tank.getCapacity() + " (" + this.tank.getCapacity().getRawValue() / 1000 + ")"));
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("tank").player(player.inventory).inventory().hotbar()
			.addInventory().blockEntity(this).fluidSlot(0, 100, 53).outputSlot(1, 140, 53)
			.sync(tank).addInventory().create(this, syncID);
	}

	@Nonnull
	@Override
	public Tank getTank() {
		return tank;
	}

	@Deprecated
	public void setTank(Tank tank) {
		this.tank.setFluid(null, tank.getFluidInstance());
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity playerEntity) {
		return getDropWithNBT();
	}
}
