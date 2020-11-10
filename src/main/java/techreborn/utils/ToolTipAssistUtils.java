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

package techreborn.utils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.List;

public class ToolTipAssistUtils {

	// Colour constants
	private static final Formatting instructColour = Formatting.BLUE;

	private static final Formatting infoColour = Formatting.GOLD;
	private static final Formatting statColour = Formatting.GOLD;

	private static final Formatting posColour = Formatting.GREEN;
	private static final Formatting negColour = Formatting.RED;


	public static List<Text> getUpgradeStats(TRContent.Upgrades upgradeType, int count, boolean shiftHeld) {
		List<Text> tips = new ArrayList<>();
		boolean shouldStackCalculate = count > 1;

		switch (upgradeType) {
			case OVERCLOCKER:
				tips.add(getPositive(I18n.translate("techreborn.tooltip.upgrade.speed_increase"), calculateValue(TechRebornConfig.overclockerSpeed * 100, count, shiftHeld), "%"));
				tips.add(getNegative(I18n.translate("techreborn.tooltip.upgrade.energy_increase"), calculateValue(TechRebornConfig.overclockerPower * 100, count, shiftHeld), "%"));
				break;
			case TRANSFORMER:
				shouldStackCalculate = false;
				break;
			case ENERGY_STORAGE:
				tips.add(getPositive(I18n.translate("techreborn.tooltip.upgrade.storage_increase"), calculateValue(TechRebornConfig.energyStoragePower, count, shiftHeld), " E"));
				break;
			case SUPERCONDUCTOR:
				tips.add(getPositive(I18n.translate("techreborn.tooltip.upgrade.flow_increase"), calculateValue(Math.pow(2, (TechRebornConfig.superConductorCount + 2)) * 100, count, shiftHeld), "%"));
				break;
		}

		// Add reminder that they can use shift to calculate the entire stack
		if (shouldStackCalculate && !shiftHeld) {
			tips.add(new LiteralText(instructColour + I18n.translate("techreborn.tooltip.stack_info")));
		}

		return tips;
	}

	public static void addInfo(String inKey, List<Text> list) {
		addInfo(inKey, list, true);
	}

	public static void addInfo(String inKey, List<Text> list, boolean hidden) {
		String key = ("techreborn.message.info." + inKey);

		if (I18n.hasTranslation(key)) {
			if (!hidden || Screen.hasShiftDown()) {
				String info = I18n.translate(key);
				String[] infoLines = info.split("\\r?\\n");

				for (String infoLine : infoLines) {
					list.add(1, new LiteralText(infoColour + infoLine));
				}
			} else {
				list.add(new LiteralText(instructColour + I18n.translate("techreborn.tooltip.more_info")));
			}
		}
	}


	private static int calculateValue(double value, int count, boolean shiftHeld) {
		int calculatedVal;

		if (shiftHeld) {
			calculatedVal = (int) value * count;
		} else {
			calculatedVal = (int) value;
		}

		return calculatedVal;
	}

	private static Text getPositive(String text, int value, String unit) {
		return new LiteralText(posColour + getStatStringUnit(text, value, unit));
	}

	private static Text getNegative(String text, int value, String unit) {
		return new LiteralText(negColour + getStatStringUnit(text, value, unit));
	}

	private static String getStatStringUnit(String text, int value, String unit) {
		return text + ": " + statColour + value + unit;
	}
}
