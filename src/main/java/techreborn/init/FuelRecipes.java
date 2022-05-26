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

import net.fabricmc.fabric.api.registry.FuelRegistry;

// Class containing definitions of burnable materials
public class FuelRecipes {
	public static void init() {
		FuelRegistry registry = FuelRegistry.INSTANCE;

		// Basing it off https://minecraft.gamepedia.com/Furnace/table

		// Rubber spam
		registry.add(TRContent.RUBBER_BUTTON, 300);
		registry.add(TRContent.RUBBER_LOG, 300);
		registry.add(TRContent.RUBBER_LOG_STRIPPED, 300);
		registry.add(TRContent.RUBBER_WOOD, 300);
		registry.add(TRContent.STRIPPED_RUBBER_WOOD, 300);
		registry.add(TRContent.RUBBER_PLANKS, 300);
		registry.add(TRContent.RUBBER_SLAB, 150);
		registry.add(TRContent.RUBBER_FENCE, 300);
		registry.add(TRContent.RUBBER_FENCE_GATE, 300);
		registry.add(TRContent.RUBBER_STAIR, 300);
		registry.add(TRContent.RUBBER_TRAPDOOR, 300);
		registry.add(TRContent.RUBBER_PRESSURE_PLATE, 300);
		registry.add(TRContent.RUBBER_DOOR, 200);
		registry.add(TRContent.RUBBER_SAPLING, 100);


		// Other stuff
		registry.add(TRContent.Machine.RESIN_BASIN, 300);
		registry.add(TRContent.Plates.WOOD, 300);
		registry.add(TRContent.TREE_TAP, 200);
	}
}
