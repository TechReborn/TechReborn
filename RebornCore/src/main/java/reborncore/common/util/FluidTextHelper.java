/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TeamReborn
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

import com.google.common.math.LongMath;
import reborncore.common.fluid.FluidValue;

/**
 * A few helpers to display fluids.
 */
public class FluidTextHelper {
	public static String getValueDisplay(FluidValue value) {
		return getUnicodeMillibuckets(value.getRawValue(), false);
	}

	/**
	 * Return a unicode string representing a fraction, like ¹⁄₈₁.
	 *
	 * @param numerator   {@code long} The numerator of the fraction.
	 * @param denominator {@code long} The denominator of the fraction.
	 * @param simplify    {@code boolean} Whether to simplify the fraction.
	 * @return {@link String} representing the fraction.
	 * @throws IllegalArgumentException if numerator or denominator is negative.
	 */
	public static String getUnicodeFraction(long numerator, long denominator, boolean simplify) {
		if (numerator < 0 || denominator < 0)
			throw new IllegalArgumentException("Numerator and denominator must be non negative.");

		if (simplify && denominator != 0) {
			long g = LongMath.gcd(numerator, denominator);
			numerator /= g;
			denominator /= g;
		}

		StringBuilder numString = new StringBuilder();

		while (numerator > 0) {
			numString.append(SUPERSCRIPT[(int) (numerator % 10)]);
			numerator /= 10;
		}

		StringBuilder denomString = new StringBuilder();

		while (denominator > 0) {
			denomString.append(SUBSCRIPT[(int) (denominator % 10)]);
			denominator /= 10;
		}

		return numString.reverse().toString() + FRACTION_BAR + denomString.reverse().toString();
	}

	/**
	 * Convert a non-negative fluid amount in droplets to a unicode string
	 * representing the amount in millibuckets. For example, passing 163 will result
	 * in
	 *
	 * <pre>
	 *  2 ¹⁄₈₁
	 * </pre>
	 */
	public static String getUnicodeMillibuckets(long droplets, boolean simplify) {
		String result = "" + droplets / 81;

		if (droplets % 81 != 0) {
			result += " " + getUnicodeFraction(droplets % 81, 81, simplify);
		}

		return result;
	}

	private static final char[] SUPERSCRIPT = new char[] { '\u2070', '\u00b9', '\u00b2', '\u00b3', '\u2074', '\u2075', '\u2076', '\u2077', '\u2078',
			'\u2079' };
	private static final char FRACTION_BAR = '\u2044';
	private static final char[] SUBSCRIPT = new char[] { '\u2080', '\u2081', '\u2082', '\u2083', '\u2084', '\u2085', '\u2086', '\u2087', '\u2088',
			'\u2089' };

	private FluidTextHelper() {
	}
}
