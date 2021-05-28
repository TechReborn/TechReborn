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

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import reborncore.common.RebornCoreConfig;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;

public class PowerSystem {
	private static EnergySystem selectedSystem = EnergySystem.values()[0];

	private static final char[] magnitude = new char[] { 'k', 'M', 'G', 'T' };

	private static Locale locale = Locale.ROOT;

	public static String getLocalizedPower(double power) {

		return getRoundedString(power, selectedSystem.abbreviation, true);
	}

	public static String getLocalizedPowerNoSuffix(double power) {
		return getRoundedString(power, "", true);
	}

	public static String getLocalizedPowerNoFormat(double power){
		return getRoundedString(power, selectedSystem.abbreviation, false);
	}

	public static String getLocalizedPowerNoSuffixNoFormat(double power){
		return getRoundedString(power, "", false);
	}

	public static String getLocalizedPowerFull(double power){
		return getFullPower(power, selectedSystem.abbreviation);
	}

	public static String getLocalizedPowerFullNoSuffix(double power){
		return getFullPower(power, "");
	}

	private static String getFullPower(double power, String units){
		checkLocale();
		DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(locale);
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
			checkLocale();
			DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(locale);
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

	public static EnergySystem getDisplayPower() {
		if(!selectedSystem.enabled.get()){
			bumpPowerConfig();
		}
		return selectedSystem;
	}

	public static void bumpPowerConfig() {
		int value = selectedSystem.ordinal() + 1;
		if (value == EnergySystem.values().length) {
			value = 0;
		}
		selectedSystem = EnergySystem.values()[value];
	}

	public static void init(){
		selectedSystem = Arrays.stream(EnergySystem.values()).filter(energySystem -> energySystem.abbreviation.equalsIgnoreCase(RebornCoreConfig.selectedSystem)).findFirst().orElse(EnergySystem.values()[0]);
		if(!selectedSystem.enabled.get()){
			bumpPowerConfig();
		}
	}

	private static void checkLocale() {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) { return; }
		MinecraftClient instance = MinecraftClient.getInstance();
		if (instance == null) { return; }
		String strangeMcLang = instance.getLanguageManager().getLanguage().getCode();
		locale = Locale.forLanguageTag(strangeMcLang.substring(0, 2));
	}

	public enum EnergySystem {
		EU(0xFF800600, "E", 141, 151, 0xFF670000);

		public int colour;
		public int altColour;
		public String abbreviation;
		public int xBar;
		public int yBar;
		public Supplier<Boolean> enabled = () -> true;

		EnergySystem(int colour, String abbreviation, int xBar, int yBar, int altColour) {
			this.colour = colour;
			this.abbreviation = abbreviation;
			this.xBar = xBar;
			this.yBar = yBar;
			this.altColour = altColour;
		}
	}
}
