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

package techreborn.datagen.tags

import net.fabricmc.fabric.api.tag.TagFactory
import net.minecraft.util.Identifier

class CommonTags {
	static class Items {
		public static zincIngots = create("zinc_ingots")

		public static brassDusts = create("brass_dusts")
		public static electrumDusts = create("electrum_dusts")
		public static platinumDusts = create("platinum_dusts")
		public static zincDusts = create("zinc_dusts")

		public static leadOres = create("lead_ores")
		public static sheldoniteOres = create("sheldonite_ores")
		public static silverOres = create("silver_ores")
		public static tinOres = create("tin_ores")

		public static rawLeadOres = create("raw_lead_ores")
		public static rawSilverOres = create("raw_silver_ores")
		public static rawTinOres = create("raw_tin_ores")

		private static def create(String path) {
			return TagFactory.ITEM.create(new Identifier("c", path))
		}
	}
}
