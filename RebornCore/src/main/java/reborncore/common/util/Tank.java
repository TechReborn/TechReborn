/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.screen.Syncable;
import reborncore.common.util.serialization.SerializationUtil;

import java.util.function.UnaryOperator;

public class Tank extends SnapshotParticipant<FluidInstance> implements Syncable, SingleSlotStorage<FluidVariant> {
	private final String name;
	private FluidInstance fluidInstance = new FluidInstance();
	private final FluidValue capacity;

	public Tank(String name, FluidValue capacity) {
		super();
		this.name = name;
		this.capacity = capacity;
	}

	@NotNull
	public FluidInstance getFluidInstance() {
		return fluidInstance;
	}

	@NotNull
	public Fluid getFluid() {
		return getFluidInstance().fluid();
	}

	public FluidValue getFluidValueCapacity() {
		return capacity;
	}

	public FluidValue getFreeSpace() {
		return getFluidValueCapacity().subtract(getFluidAmount());
	}

	public boolean canFit(Fluid fluid, FluidValue amount) {
		return (isEmpty() || getFluid() == fluid) && getFreeSpace().equalOrMoreThan(amount);
	}

	public boolean isEmpty() {
		return getFluidInstance().isEmpty();
	}

	public boolean isFull() {
		return !getFluidInstance().isEmpty() && getFluidInstance().getAmount().equalOrMoreThan(getFluidValueCapacity());
	}

	public final NbtCompound write(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
		NbtCompound tankData = SerializationUtil.writeNbt(FluidInstance.CODEC, fluidInstance, wrapperLookup);
		nbt.put(name, tankData);
		return nbt;
	}

	public void setFluidAmount(FluidValue amount) {
		if (!fluidInstance.isEmptyFluid()) {
			modifyFluid(fluidInstance -> fluidInstance.withAmount(amount));
		}
	}

	public final Tank read(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
		if (nbt.contains(name)) {
			// allow reading empty tanks
			setFluid(Fluids.EMPTY);

			NbtCompound tankData = nbt.getCompound(name);
			fluidInstance = SerializationUtil.parseNbt(FluidInstance.CODEC, tankData, wrapperLookup, () -> FluidInstance.EMPTY, "tank data");
		}
		return this;
	}

	public void setFluid(@NotNull Fluid f) {
		modifyFluid(fluidInstance -> fluidInstance.withFluid(f));
	}

	@Override
	public void configureSync(Context context) {
		context.sync(FluidInstance.PACKET_CODEC, this::getFluidInstance, this::setFluidInstance);
	}

	public FluidValue getFluidAmount() {
		return getFluidInstance().getAmount();
	}

	public void modifyFluid(UnaryOperator<FluidInstance> operator) {
		setFluidInstance(operator.apply(fluidInstance));
	}

	public void setFluidInstance(@NotNull FluidInstance fluidInstance) {
		this.fluidInstance = fluidInstance;
	}

	@Override
	public long insert(FluidVariant insertedVariant, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(insertedVariant, maxAmount);
		FluidVariant currentVariant = getResource();

		if (currentVariant.equals(insertedVariant) || currentVariant.isBlank()) {
			long insertedAmount = Math.min(maxAmount, getCapacity() - getAmount());

			if (insertedAmount > 0) {
				updateSnapshots(transaction);

				// Just in case.
				if (currentVariant.isBlank()) {
					modifyFluid(fluidInstance -> fluidInstance.withAmount(FluidValue.EMPTY));
				}

				modifyFluid(fluidInstance -> fluidInstance.withFluid(insertedVariant.getFluid()).addAmount(FluidValue.fromRaw(insertedAmount)));
			}

			return insertedAmount;
		}

		return 0;
	}

	@Override
	public long extract(FluidVariant extractedVariant, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(extractedVariant, maxAmount);
		FluidVariant currentVariant = getResource();

		if (extractedVariant.equals(currentVariant)) {
			long extractedAmount = Math.min(maxAmount, getAmount());

			if (extractedAmount > 0) {
				updateSnapshots(transaction);

				modifyFluid(fluidInstance -> fluidInstance.subtractAmount(FluidValue.fromRaw(extractedAmount)));
			}

			return extractedAmount;
		}

		return 0;
	}

	@Override
	public boolean isResourceBlank() {
		return getResource().isBlank();
	}

	@Override
	public FluidVariant getResource() {
		return fluidInstance.fluidVariant();
	}

	@Override
	@SuppressWarnings({"deprecation"})
	public long getAmount() {
		return fluidInstance.getAmount().getRawValue();
	}

	@Override
	@SuppressWarnings({"deprecation"})
	public long getCapacity() {
		return getFluidValueCapacity().getRawValue();
	}

	@Override
	protected FluidInstance createSnapshot() {
		return fluidInstance;
	}

	@Override
	protected void readSnapshot(FluidInstance snapshot) {
		setFluidInstance(snapshot);
	}
}
