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

package techreborn.init;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

// Class containing definitions of burnable materials
public class FuelRecipes {
	public static void init() {
		FuelRegistryEvents.BUILD.register( (builder, context) -> {
			// Basing it off https://minecraft.wiki/w/Template:Smelting_table
			// one and a half smelt time
			final int oneAndAHalf = context.baseSmeltTime() * 3 / 2;
			// Rubber spam
			builder.add(TRContent.RUBBER_BUTTON, oneAndAHalf);
			builder.add(TRContent.RUBBER_LOG, oneAndAHalf);
			builder.add(TRContent.RUBBER_LOG_STRIPPED, oneAndAHalf);
			builder.add(TRContent.RUBBER_WOOD, oneAndAHalf);
			builder.add(TRContent.STRIPPED_RUBBER_WOOD, oneAndAHalf);
			builder.add(TRContent.RUBBER_PLANKS, oneAndAHalf);
			builder.add(TRContent.RUBBER_SLAB, oneAndAHalf / 2);
			builder.add(TRContent.RUBBER_FENCE, oneAndAHalf);
			builder.add(TRContent.RUBBER_FENCE_GATE, oneAndAHalf);
			builder.add(TRContent.RUBBER_STAIR, oneAndAHalf);
			builder.add(TRContent.RUBBER_TRAPDOOR, oneAndAHalf);
			builder.add(TRContent.RUBBER_PRESSURE_PLATE, oneAndAHalf);
			builder.add(TRContent.RUBBER_DOOR, context.baseSmeltTime());
			builder.add(TRContent.RUBBER_SAPLING, context.baseSmeltTime() / 2);
			// Other stuff
			builder.add(TRContent.Machine.RESIN_BASIN, oneAndAHalf);
			builder.add(TRContent.Plates.WOOD, oneAndAHalf);
			builder.add(TRContent.TREE_TAP, context.baseSmeltTime());
			}
		);
	}
}
