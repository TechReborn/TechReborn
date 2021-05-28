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

package reborncore.common.fluid.container;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import reborncore.common.fluid.FluidValue;
import reborncore.common.util.NBTSerializable;

public class FluidInstance implements NBTSerializable {
	public static final String FLUID_KEY = "Fluid";
	public static final String AMOUNT_KEY = "Amount";
	public static final String TAG_KEY = "Tag";

	public static final FluidInstance EMPTY = new FluidInstance(Fluids.EMPTY, FluidValue.EMPTY);

	protected Fluid fluid;
	protected FluidValue amount;
	protected NbtCompound tag;

	public FluidInstance(Fluid fluid, FluidValue amount) {
		this.fluid = fluid;
		this.amount = amount;
	}

	public FluidInstance(Fluid fluid) {
		this(fluid, FluidValue.EMPTY);
	}

	public FluidInstance() {
		this(Fluids.EMPTY);
	}

	public FluidInstance(NbtCompound tag) {
		this();
		read(tag);
	}

	public Fluid getFluid() {
		return fluid;
	}

	public FluidValue getAmount() {
		return amount;
	}

	public NbtCompound getTag() {
		return tag;
	}

	public FluidInstance setFluid(Fluid fluid) {
		this.fluid = fluid;
		return this;
	}

	public FluidInstance setAmount(FluidValue value) {
		this.amount = value;
		return this;
	}

	public FluidInstance subtractAmount(FluidValue amount) {
		this.amount = this.amount.subtract(amount);
		return this;
	}

	public FluidInstance addAmount(FluidValue amount) {
		this.amount = this.amount.add(amount);
		return this;
	}

	public void setTag(NbtCompound tag) {
		this.tag = tag;
	}

	public boolean isEmpty() {
		return isEmptyFluid() || this.getAmount().isEmpty();
	}

	public boolean isEmptyFluid() {
		return this.getFluid() == Fluids.EMPTY;
	}

	public FluidInstance copy() {
		return new FluidInstance().setFluid(fluid).setAmount(amount);
	}

	@Override
	public NbtCompound write() {
		NbtCompound tag = new NbtCompound();
		tag.putString(FLUID_KEY, Registry.FLUID.getId(fluid).toString());
		tag.putInt(AMOUNT_KEY, amount.getRawValue());
		if (this.tag != null && !this.tag.isEmpty()) {
			tag.put(TAG_KEY, this.tag);
		}
		return tag;
	}

	@Override
	public void read(NbtCompound tag) {
		fluid = Registry.FLUID.get(new Identifier(tag.getString(FLUID_KEY)));
		amount = FluidValue.fromRaw(tag.getInt(AMOUNT_KEY));
		if (tag.contains(TAG_KEY)) {
			this.tag = tag.getCompound(TAG_KEY);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof FluidInstance && fluid == ((FluidInstance) obj).getFluid() && amount.equals(((FluidInstance) obj).getAmount());
	}

	public boolean isFluidEqual(FluidInstance instance) {
		return (isEmpty() && instance.isEmpty()) || fluid.equals(instance.getFluid());
	}
}