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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import reborncore.common.fluid.FluidValue;

public record FluidInstance(FluidVariant fluidVariant, FluidValue amount) {
	public static final FluidInstance EMPTY = new FluidInstance(Fluids.EMPTY, FluidValue.EMPTY);

	public static final Codec<FluidInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		FluidVariant.CODEC.fieldOf("fluid").forGetter(FluidInstance::fluidVariant),
		FluidValue.CODEC.fieldOf("amount").forGetter(FluidInstance::getAmount)
	).apply(instance, FluidInstance::new));
	public static final PacketCodec<RegistryByteBuf, FluidInstance> PACKET_CODEC = PacketCodec.tuple(
		FluidVariant.PACKET_CODEC, FluidInstance::fluidVariant,
		FluidValue.PACKET_CODEC, FluidInstance::getAmount,
		FluidInstance::new
	);

	public FluidInstance(Fluid fluid) {
		this(FluidVariant.of(fluid), FluidValue.EMPTY);
	}

	public FluidInstance(Fluid fluid, FluidValue fluidValue) {
		this(FluidVariant.of(fluid), fluidValue);
	}

	public FluidInstance() {
		this(Fluids.EMPTY);
	}

	public FluidVariant fluidVariant() {
		return fluidVariant;
	}

	public Fluid fluid() {
		return fluidVariant.getFluid();
	}

	public FluidValue getAmount() {
		return amount;
	}

	public FluidInstance withFluid(Fluid fluid) {
		return new FluidInstance(fluid, this.amount);
	}

	public FluidInstance withAmount(FluidValue amount) {
		return new FluidInstance(this.fluidVariant, amount);
	}

	public FluidInstance subtractAmount(FluidValue amount) {
		return new FluidInstance(this.fluidVariant, this.amount.subtract(amount));
	}

	public FluidInstance addAmount(FluidValue amount) {
		return new FluidInstance(this.fluidVariant, this.amount.add(amount));
	}

	public boolean isEmpty() {
		return isEmptyFluid() || this.getAmount().isEmpty();
	}

	public boolean isEmptyFluid() {
		return this.fluid() == Fluids.EMPTY;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof FluidInstance && fluidVariant == ((FluidInstance) obj).fluid() && amount.equals(((FluidInstance) obj).getAmount());
	}

}
