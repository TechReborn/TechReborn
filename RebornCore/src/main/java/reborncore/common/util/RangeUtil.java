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

import org.apache.commons.lang3.Range;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public final class RangeUtil {
	private RangeUtil() {
	}

	public static List<Range<Integer>> joinAdjacent(List<Range<Integer>> list) {
		var in = list.stream().sorted(Comparator.comparing(Range::getMinimum)).toList();
		var out = new LinkedList<Range<Integer>>();

		for (var range : in) {
			if (out.isEmpty()) {
				out.add(range);
				continue;
			}

			var last = out.getLast();
			if (last.getMaximum() >= range.getMinimum() -1) {
				out.removeLast();
				out.add(Range.between(last.getMinimum(), Math.max(last.getMaximum(), range.getMaximum())));
			} else {
				out.add(range);
			}
		}

		return Collections.unmodifiableList(out);
	}
}
