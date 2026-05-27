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

package techreborn.client.events;

import net.minecraft.client.Minecraft;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;


public class ToolTipAssistUtils {

	// Colour constants
	private static final ChatFormatting instructColour = ChatFormatting.BLUE;

	private static final ChatFormatting infoColour = ChatFormatting.GOLD;
	private static final ChatFormatting statColour = ChatFormatting.GOLD;

	private static final ChatFormatting posColour = ChatFormatting.GREEN;
	private static final ChatFormatting negColour = ChatFormatting.RED;

	public static List<Component> getUpgradeStats(TRContent.Upgrades upgradeType, int count, boolean shiftHeld) {
		List<Component> tips = new ArrayList<>();
		boolean shouldStackCalculate = count > 1;

		switch (upgradeType) {
			case OVERCLOCKER -> {
				tips.add(getStatStringUnit(I18n.get("techreborn.tooltip.upgrade.speed_increase"), calculateSpeed(TechRebornConfig.overclockerSpeed * 100, count, shiftHeld), "%", true));
				tips.add(getStatStringUnit(I18n.get("techreborn.tooltip.upgrade.energy_increase"), calculateEnergyIncrease(TechRebornConfig.overclockerPower + 1, count, shiftHeld), "x", false));
			}
			case TRANSFORMER -> shouldStackCalculate = false;
			case ENERGY_STORAGE -> tips.add(getStatStringUnit(I18n.get("techreborn.tooltip.upgrade.storage_increase"), calculateValue(TechRebornConfig.energyStoragePower, count, shiftHeld), " E", true));
			case SUPERCONDUCTOR -> tips.add(getStatStringUnit(I18n.get("techreborn.tooltip.upgrade.flow_increase"), calculateValue(Math.pow(2, (TechRebornConfig.superConductorCount + 2)) * 100, count, shiftHeld), "%", true));
		}

		// Add reminder that they can use shift to calculate the entire stack
		if (shouldStackCalculate && !shiftHeld) {
			tips.add(Component.literal(instructColour + I18n.get("techreborn.tooltip.stack_info")));
		}

		return tips;
	}

	public static void addInfo(String inKey, List<Component> list) {
		addInfo(inKey, list, true);
	}

	public static void addInfo(String inKey, List<Component> list, boolean hidden) {
		String key = ("techreborn.message.info." + inKey);

		if (!Language.getInstance().getOrDefault(key, key).equals(key)) {
			if (!hidden || Minecraft.getInstance().hasShiftDown()) {
				String info = I18n.get(key);
				List<MutableComponent> infoLines = Arrays.stream(info.split("\\r?\\n"))
					.map(infoLine -> Component.literal(infoColour + infoLine)).toList();
				list.addAll(1, infoLines);
			} else {
				list.add(Component.literal(instructColour + I18n.get("techreborn.tooltip.more_info")));
			}
		}
	}


	private static int calculateValue(double value, int count, boolean shiftHeld) {
		int calculatedVal;

		if (shiftHeld) {
			calculatedVal = (int) value * Math.min(count, 4);
		} else {
			calculatedVal = (int) value;
		}

		return calculatedVal;
	}

	private static double calculateEnergyIncrease(double value, int count, boolean shiftHeld) {
		double calculatedVal;

		if (shiftHeld) {
			calculatedVal = Math.pow(value, Math.min(count, 4));
		} else {
			calculatedVal = value;
		}

		return calculatedVal;
	}

	private static double calculateSpeed(double value, int count, boolean shiftHeld) {
		double calculatedVal;

		if (shiftHeld) {
			calculatedVal = Math.min(value * Math.min(count, 4), MachineBaseBlockEntity.SPEED_CAP * 100);
		} else {
			calculatedVal = value;
		}

		return calculatedVal;
	}

	private static Component getStatStringUnit(String text, double value, String unit, boolean isPositive) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US); // Always use dot
		NumberFormat formatter = new DecimalFormat("##.##", symbols); // Round to 2 decimal places
		return Component.literal(statColour + text + ": " + ((isPositive) ? posColour : negColour) + formatter.format(value) + unit);
	}
}
