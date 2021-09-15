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

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.Locale;

/**
 * @author Prospector on 11/05/16
 */
public class StringUtils {

	public static String toFirstCapital(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}

		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	public static String toFirstCapitalAllLowercase(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		String output = input.toLowerCase(Locale.ROOT);
		return output.substring(0, 1).toUpperCase() + output.substring(1);
	}

	/**
	 * Returns red-yellow-green text formatting depending on percentage
	 *
	 * @param percentage int percentage amount
	 * @return TextFormatting Red or Yellow or Green
	 */
	public static Formatting getPercentageColour(int percentage) {
		if (percentage <= 10) {
			return Formatting.RED;
		} else if (percentage >= 75) {
			return Formatting.GREEN;
		} else {
			return Formatting.YELLOW;
		}
	}

	public static MutableText getPercentageText(int percentage) {
		return new LiteralText(String.valueOf(percentage))
				.formatted(getPercentageColour(percentage))
				.append("%");
	}

}
