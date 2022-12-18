/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.utils;

import techreborn.init.TRContent;

import java.util.Comparator;
import java.util.List;

public class MaterialTypeComparator implements Comparator<Enum<?>> {

	private static final List<String> ORDER = List.of(
		TRContent.Ores.class.getName(),
		TRContent.Nuggets.class.getName(),
		TRContent.SmallDusts.class.getName(),
		TRContent.Dusts.class.getName(),
		TRContent.RawMetals.class.getName(),
		TRContent.Ingots.class.getName(),
		TRContent.Gems.class.getName(),
		TRContent.Plates.class.getName(),
		TRContent.StorageBlocks.class.getName()
	);

	@Override
	public int compare(Enum<?> o1, Enum<?> o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		if (o1.getClass().getName().equals(o2.getClass().getName())) {
			return 0;
		}
		return ORDER.indexOf(o1.getClass().getName()) - ORDER.indexOf(o2.getClass().getName());
	}
}
