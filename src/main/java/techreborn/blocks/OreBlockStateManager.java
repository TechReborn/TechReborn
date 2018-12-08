/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.blocks;

import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.lib.ModInfo;


//This is in its own class as not to tick the block class before
@RebornRegistry(modID = ModInfo.MOD_ID, priority = 1, earlyReg = true)
public class OreBlockStateManager {
	//This is a one off config, dont worry about it
	@ConfigRegistry(config = "misc", category = "misc", key = "endOreStone", comment = "Set to true to render the end ores with a stone background")
	public static boolean endOreStone = false;

	public static String convert(String name){
		if (OreBlockStateManager.endOreStone && name.equals("tungsten")) {
			name = "tungsten_stone";
		}
		if (OreBlockStateManager.endOreStone && name.equals("peridot")) {
			name = "peridot_stone";
		}
		if (OreBlockStateManager.endOreStone && name.equals("sheldonite")) {
			name = "sheldonite_stone";
		}
		if (OreBlockStateManager.endOreStone && name.equals("sodalite")) {
			name = "sodalite_stone";
		}
		return name;
	}

	public static String invert(String name){
		if (name.equals("tungsten_stone")) {
			name = "tungsten";
		}
		if (name.equals("peridot_stone")) {
			name = "peridot";
		}
		if (name.equals("sheldonite_stone")) {
			name = "sheldonite";
		}
		if (name.equals("sodalite_stone")) {
			name = "sodalite";
		}
		return name;
	}
}
