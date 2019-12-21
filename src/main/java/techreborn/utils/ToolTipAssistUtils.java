package techreborn.utils;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.List;

public class ToolTipAssistUtils {


	public static List<Text> getUpgradeAssist(TRContent.Upgrades upgradeType, int count, boolean shiftHeld) {
		List<Text> tips = new ArrayList<>();
		boolean shouldStackCalculate = true;

		switch (upgradeType) {
			case OVERCLOCKER:
				tips.add(getDescription("techreborn.tooltip.upgrades.overclocker"));
				tips.add(getPositive("Speed increase", calculateValue(TechRebornConfig.overclockerSpeed * 100, count, shiftHeld), "%"));
				tips.add(getNegative("Energy increase", calculateValue(TechRebornConfig.overclockerPower * 100, count, shiftHeld), "%"));
				break;
			case TRANSFORMER:
				tips.add(getDescription("techreborn.tooltip.upgrades.transformer"));
				shouldStackCalculate = false;
				break;
			case ENERGY_STORAGE:
				tips.add(getDescription("techreborn.tooltip.upgrades.energystorage"));
				tips.add(getPositive("Storage increase", calculateValue(TechRebornConfig.energyStoragePower, count, shiftHeld), " E"));
				break;
			case SUPERCONDUCTOR:
				tips.add(getDescription("techreborn.tooltip.upgrades.superconductor"));
				tips.add(getPositive("Increased flow by: ", calculateValue(Math.pow(2, (TechRebornConfig.superConductorCount + 2)) * 100, count, shiftHeld), "%"));
				break;
		}

		// Add reminder that they can use shift to calculate the entire stack
		if(shouldStackCalculate && !shiftHeld){
			tips.add(new LiteralText(Formatting.GOLD + "Hold shift for stack calculation"));
		}

		return tips;
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

	private static Text getDescription(String key) {
		return new TranslatableText(key).formatted(Formatting.GRAY);
	}

	private static Text getPositive(String text, int value, String unit) {
		return new LiteralText(Formatting.GREEN + getStatStringUnit(text, value, unit));
	}

	private static Text getNegative(String text, int value, String unit) {
		return new LiteralText(Formatting.RED + getStatStringUnit(text, value, unit));
	}

	private static String getStatStringUnit(String text, int value, String unit) {
		return text + ": " + Formatting.WHITE + value + unit;
	}
}
