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

package reborncore.common.powerSystem;

import reborncore.RebornCore;

import java.text.DecimalFormat;

public class PowerSystem {
	public static final String ABBREVIATION = "E";

	private static final char[] magnitude = new char[] { 'k', 'M', 'G', 'T' };

	public static String getLocalizedPower(double power) {

		return getRoundedString(power, ABBREVIATION, true);
	}

	public static String getLocalizedPowerNoSuffix(double power) {
		return getRoundedString(power, "", true);
	}

	public static String getLocalizedPowerNoFormat(double power){
		return getRoundedString(power, ABBREVIATION, false);
	}

	public static String getLocalizedPowerNoSuffixNoFormat(double power){
		return getRoundedString(power, "", false);
	}

	public static String getLocalizedPowerFull(double power){
		return getFullPower(power, ABBREVIATION);
	}

	public static String getLocalizedPowerFullNoSuffix(double power){
		return getFullPower(power, "");
	}

	private static String getFullPower(double power, String units){
		DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(RebornCore.locale);
		return formatter.format(power) + " " + units;
	}

	private static String getRoundedString(double originalValue, String units, boolean doFormat) {
		String ret = "";
		double value = 0f;
		int i = 0;
		boolean showMagnitude = true;
		double euValue = originalValue;
		if (euValue < 0) {
			ret = "-";
			euValue = -euValue;
		}

		if (euValue < 1000) {
			doFormat = false;
			showMagnitude = false;
			value = euValue;
		} else if (euValue >= 1000) {
			for (i = 0; ; i++) {
				if (euValue < 10000 && euValue % 1000 >= 100) {
					value = Math.floor(euValue / 1000);
					value += ((float) euValue % 1000) / 1000;
					break;
				}
				euValue /= 1000;
				if (euValue < 1000) {
					value = euValue;
					break;
				}
			}
		}

		if (i > 10) {
			doFormat = false;
			showMagnitude = false;
		} else if (i > 3) {
			value = originalValue;
			showMagnitude = false;
		}

		if (doFormat){
			DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(RebornCore.locale);
			ret += formatter.format(value);
			int idx = ret.lastIndexOf(formatter.getDecimalFormatSymbols().getDecimalSeparator());
			if (idx > 0){
				ret = ret.substring(0, idx + 2);
			}
		}
		else {
			if (i>10){
				ret += "∞";
			}
			else {
				ret += value;
			}
		}

		if (showMagnitude) {
			ret += magnitude[i];
		}

		if (!units.equals("")) {
			ret += " " + units;
		}

		return ret;
	}
}
