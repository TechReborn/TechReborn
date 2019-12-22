package techreborn.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.List;

public class DocumentAssistUtils {

	// Colour constants
	private static final Formatting instructColour = Formatting.BLUE;

	private static final Formatting infoColour = Formatting.GOLD;
	private static final Formatting statColour = Formatting.GOLD;

	private static final Formatting posColour = Formatting.GREEN;
	private static final Formatting negColour = Formatting.RED;


	public static List<Text> getUpgradeStats(TRContent.Upgrades upgradeType, int count, boolean shiftHeld) {
		List<Text> tips = new ArrayList<>();
		boolean shouldStackCalculate = true;

		switch (upgradeType) {
			case OVERCLOCKER:
				tips.add(getPositive("Speed increase", calculateValue(TechRebornConfig.overclockerSpeed * 100, count, shiftHeld), "%"));
				tips.add(getNegative("Energy increase", calculateValue(TechRebornConfig.overclockerPower * 100, count, shiftHeld), "%"));
				break;
			case TRANSFORMER:
				shouldStackCalculate = false;
				break;
			case ENERGY_STORAGE:
				tips.add(getPositive("Storage increase", calculateValue(TechRebornConfig.energyStoragePower, count, shiftHeld), " E"));
				break;
			case SUPERCONDUCTOR:
				tips.add(getPositive("Increased flow by: ", calculateValue(Math.pow(2, (TechRebornConfig.superConductorCount + 2)) * 100, count, shiftHeld), "%"));
				break;
		}

		// Add reminder that they can use shift to calculate the entire stack
		if(shouldStackCalculate && !shiftHeld){
			tips.add(new LiteralText(instructColour + "Hold shift for stack calculation"));
		}

		return tips;
	}

	public static void addInfo(String inKey, List<Text> list){
		addInfo(inKey, list, true);
	}

	public static void addInfo(String inKey, List<Text> list, boolean hidden){
		String key = ("techreborn.message.info." + inKey);

		if(I18n.hasTranslation(key)){
			if(!hidden || Screen.hasShiftDown()){
				list.add(new TranslatableText(key).formatted(infoColour));
			}else{
				list.add(new LiteralText(instructColour + "Hold shift for info"));
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
