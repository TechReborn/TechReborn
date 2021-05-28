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

import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;
import reborncore.common.fluid.container.ItemFluidInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RebornFluidManager {

	private static final HashMap<Identifier, RebornFluid> fluids = new HashMap<>();

	private static Lazy<Map<Fluid, BucketItem>> bucketMap;

	public static void register(RebornFluid rebornFluid, Identifier identifier) {
		fluids.put(identifier, rebornFluid);
		Registry.register(Registry.FLUID, identifier, rebornFluid);
	}

	public static void setupBucketMap() {
		bucketMap = new Lazy<>(() -> {
			Map<Fluid, BucketItem> map = new HashMap<>();
			Registry.ITEM.stream().filter(item -> item instanceof BucketItem).forEach(item -> {
				BucketItem bucketItem = (BucketItem) item;
				//We can be sure of this as we add this via a mixin
				ItemFluidInfo fluidInfo = (ItemFluidInfo) bucketItem;
				Fluid fluid = fluidInfo.getFluid(new ItemStack(item));
				if (!map.containsKey(fluid)) {
					map.put(fluid, bucketItem);
				}
			});
			return map;
		});
	}

	public static Map<Fluid, BucketItem> getBucketMap() {
		return bucketMap.get();
	}

	public static HashMap<Identifier, RebornFluid> getFluids() {
		return fluids;
	}

	public static Stream<RebornFluid> getFluidStream() {
		return fluids.values().stream();
	}
}
