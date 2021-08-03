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

package reborncore.common.fluid;

import com.google.common.base.Objects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.util.JsonHelper;
import reborncore.common.util.FluidTextHelper;

public final class FluidValue {

	public static final FluidValue EMPTY = new FluidValue(0);
	public static final FluidValue BUCKET_QUARTER = new FluidValue(FluidConstants.BUCKET / 4);
	public static final FluidValue BUCKET = new FluidValue(FluidConstants.BUCKET);
	public static final FluidValue INFINITE = new FluidValue(Long.MAX_VALUE);

	private final long rawValue;

	private static FluidValue fromMillibuckets(long millibuckets) {
		return new FluidValue(millibuckets * 81);
	}

	private FluidValue(long rawValue) {
		this.rawValue = rawValue;
	}

	public FluidValue multiply(long value) {
		return fromRaw(rawValue * value);
	}

	public FluidValue fraction(long divider) {return fromRaw(rawValue / divider);}

	public FluidValue add(FluidValue fluidValue) {
		return fromRaw(rawValue + fluidValue.rawValue);
	}

	public FluidValue subtract(FluidValue fluidValue) {
		return fromRaw(rawValue - fluidValue.rawValue);
	}

	public FluidValue min(FluidValue fluidValue) {
		return fromRaw(Math.min(rawValue, fluidValue.rawValue));
	}

	public boolean isEmpty() {
		return rawValue == 0;
	}

	public boolean moreThan(FluidValue value) {
		return rawValue > value.rawValue;
	}

	public boolean equalOrMoreThan(FluidValue value) {
		return rawValue >= value.rawValue;
	}

	public boolean lessThan(FluidValue value) {
		return rawValue < value.rawValue;
	}

	public boolean lessThanOrEqual(FluidValue value) {
		return rawValue <= value.rawValue;
	}

	@Override
	public String toString() {
		return FluidTextHelper.getValueDisplay(this) + " Mb";
	}

	//TODO move away from using this
	@Deprecated
	public long getRawValue() {
		return rawValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FluidValue that = (FluidValue) o;
		return rawValue == that.rawValue;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(rawValue);
	}

	@Deprecated
	public static FluidValue fromRaw(long rawValue) {
		if (rawValue < 0) {
			rawValue = 0;
		}
		return new FluidValue(rawValue);
	}

	public static FluidValue parseFluidValue(JsonElement jsonElement) {
		if (jsonElement.isJsonObject()) {
			final JsonObject jsonObject = jsonElement.getAsJsonObject();
			if (jsonObject.has("buckets")) {
				int buckets = JsonHelper.getInt(jsonObject, "buckets");
				return BUCKET.multiply(buckets);
			} else if (jsonObject.has("droplets")) {
				long droplets = JsonHelper.getLong(jsonObject, "droplets");
				return fromRaw(droplets);
			}
		} else if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
			//TODO add a warning here
			return fromMillibuckets(jsonElement.getAsJsonPrimitive().getAsInt());
		}
		throw new JsonSyntaxException("Could not parse fluid value");
	}

}
