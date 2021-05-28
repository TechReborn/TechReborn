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

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.client.screen.builder.Syncable;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.fluid.container.GenericFluidContainer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Tank implements GenericFluidContainer<Direction>, Syncable {

	private final String name;
	@NotNull
	private FluidInstance fluidInstance = new FluidInstance();
	private final FluidValue capacity;

	@Nullable
	private Direction side = null;

	private final MachineBaseBlockEntity blockEntity;

	public Tank(String name, FluidValue capacity, MachineBaseBlockEntity blockEntity) {
		super();
		this.name = name;
		this.capacity = capacity;
		this.blockEntity = blockEntity;
	}

	@NotNull
	public FluidInstance getFluidInstance() {
		return getFluidInstance(side);
	}

	@NotNull
	public Fluid getFluid() {
		return getFluidInstance().getFluid();
	}

	public FluidValue getCapacity() {
		return capacity;
	}

	public FluidValue getFreeSpace() {
		return getCapacity().subtract(getFluidAmount());
	}

	public boolean canFit(Fluid fluid, FluidValue amount) {
		return (isEmpty() || getFluid() == fluid) && getFreeSpace().equalOrMoreThan(amount);
	}

	public boolean isEmpty() {
		return getFluidInstance().isEmpty();
	}

	public boolean isFull() {
		return !getFluidInstance().isEmpty() && getFluidInstance().getAmount().equalOrMoreThan(getCapacity());
	}

	public final CompoundTag write(CompoundTag nbt) {
		CompoundTag tankData = fluidInstance.write();
		nbt.put(name, tankData);
		return nbt;
	}

	public void setFluidAmount(FluidValue amount) {
		if (!fluidInstance.isEmptyFluid()) {
			fluidInstance.setAmount(amount);
		}
	}

	public final Tank read(CompoundTag nbt) {
		if (nbt.contains(name)) {
			// allow to read empty tanks
			setFluid(Fluids.EMPTY);

			CompoundTag tankData = nbt.getCompound(name);
			fluidInstance = new FluidInstance(tankData);
		}
		return this;
	}

	public void setFluid(@NotNull Fluid f) {
		Validate.notNull(f);
		fluidInstance.setFluid(f);
	}

	@Nullable
	public Direction getSide() {
		return side;
	}

	public void setSide(
			@Nullable
					Direction side) {
		this.side = side;
	}

	@Override
	public void getSyncPair(List<Pair<Supplier, Consumer>> pairList) {
		pairList.add(Pair.of(() -> Registry.FLUID.getId(fluidInstance.getFluid()).toString(), (Consumer<String>) o -> fluidInstance.setFluid(Registry.FLUID.get(new Identifier(o)))));
		pairList.add(Pair.of(() -> fluidInstance.getAmount(), o -> fluidInstance.setAmount((FluidValue) o)));
	}

	public FluidValue getFluidAmount() {
		return getFluidInstance().getAmount();
	}

	@Override
	public void setFluid(@Nullable Direction type, @NotNull FluidInstance instance) {
		fluidInstance = instance;
	}

	@NotNull
	@Override
	public FluidInstance getFluidInstance(@Nullable Direction type) {
		return fluidInstance;
	}

	public void setFluidInstance(@NotNull FluidInstance fluidInstance) {
		this.fluidInstance = fluidInstance;
	}

	@Override
	public FluidValue getCapacity(@Nullable Direction type) {
		return capacity;
	}


}
