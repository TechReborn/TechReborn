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
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.screen.Syncable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"deprecation", "UnstableApiUsage"})
public class Tank extends SnapshotParticipant<FluidInstance> implements Syncable, SingleSlotStorage<FluidVariant> {

	private final String name;
	@NotNull
	private FluidInstance fluidInstance = new FluidInstance();
	private final FluidValue capacity;

	private final MachineBaseBlockEntity blockEntity;

	public Tank(String name, FluidValue capacity, MachineBaseBlockEntity blockEntity) {
		super();
		this.name = name;
		this.capacity = capacity;
		this.blockEntity = blockEntity;
	}

	@NotNull
	public FluidInstance getFluidInstance() {
		return fluidInstance;
	}

	@NotNull
	public Fluid getFluid() {
		return getFluidInstance().getFluid();
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

	public final NbtCompound write(NbtCompound nbt) {
		NbtCompound tankData = fluidInstance.write();
		nbt.put(name, tankData);
		return nbt;
	}

	public void setFluidAmount(FluidValue amount) {
		if (!fluidInstance.isEmptyFluid()) {
			fluidInstance.setAmount(amount);
		}
	}

	public final Tank read(NbtCompound nbt) {
		if (nbt.contains(name)) {
			// allow reading empty tanks
			setFluid(Fluids.EMPTY);

			NbtCompound tankData = nbt.getCompound(name);
			fluidInstance = new FluidInstance(tankData);
		}
		return this;
	}

	public void setFluid(@NotNull Fluid f) {
		Validate.notNull(f);
		fluidInstance.setFluid(f);
	}

	@Override
	public void getSyncPair(List<Pair<Supplier<?>, Consumer<?>>> pairList) {
		pairList.add(Pair.of(() -> Registries.FLUID.getId(fluidInstance.getFluid()).toString(), (Consumer<String>) o -> fluidInstance.setFluid(Registries.FLUID.get(new Identifier(o)))));
		pairList.add(Pair.of(() -> fluidInstance.getAmount(), o -> fluidInstance.setAmount((FluidValue) o)));
	}

	public FluidValue getFluidAmount() {
		return getFluidInstance().getAmount();
	}

	public void setFluid(@NotNull FluidInstance instance) {
		fluidInstance = instance;
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
				if (currentVariant.isBlank()) fluidInstance.setAmount(FluidValue.EMPTY);

				fluidInstance.setFluid(insertedVariant.getFluid());
				fluidInstance.setTag(insertedVariant.getNbt());
				fluidInstance.addAmount(FluidValue.fromRaw(insertedAmount));
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

				fluidInstance.subtractAmount(FluidValue.fromRaw(extractedAmount));
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
		return fluidInstance.getVariant();
	}

	@Override
	public long getAmount() {
		return fluidInstance.getAmount().getRawValue();
	}

	@Override
	public long getCapacity() {
		return getFluidValueCapacity().getRawValue();
	}

	@Override
	protected FluidInstance createSnapshot() {
		return fluidInstance.copy();
	}

	@Override
	protected void readSnapshot(FluidInstance snapshot) {
		setFluidInstance(snapshot);
	}
}
