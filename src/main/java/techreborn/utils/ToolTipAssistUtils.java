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

		switch (upgradeType) {
			case OVERCLOCKER:
				tips.add(getDescription("techreborn.tooltip.upgrades.transformer.overclocker"));
				tips.add(getPositive("Speed increase", (int)(TechRebornConfig.overclockerSpeed * 100 * count), "%"));
				tips.add(getNegative("Energy increase", (int)(TechRebornConfig.overclockerPower * 100 * count) , "%"));
				break;
			case TRANSFORMER:
                tips.add(getDescription("techreborn.tooltip.upgrades.transformer"));
				break;
			case ENERGY_STORAGE:
                tips.add(getDescription("techreborn.tooltip.upgrades.energystorage"));
                tips.add(getPositive("Storage increase", (int)TechRebornConfig.energyStoragePower, " E"));
				break;
			case SUPERCONDUCTOR:

				tips.add(getDescription("techreborn.tooltip.upgrades.superconductor"));

				// Was in existing tooltip, don't understand but I presume it's an easter egg
				if (shiftHeld) {
					tips.add(new LiteralText(Formatting.GOLD + "Blame obstinate_3 for this").formatted(Formatting.GRAY));
				}
				break;
		}

		return tips;
	}

	private static Text getDescription(String key){
        return new TranslatableText("techreborn.tooltip.upgrades.overclocker").formatted(Formatting.GRAY);
    }

    private static Text getPositive(String text, int value, String unit){
	  return new LiteralText(Formatting.GREEN + getStatString(text, value, unit));
    }

	private static Text getNegative(String text, int value, String unit){
		return new LiteralText(Formatting.RED + getStatString(text, value, unit));
	}

	private static String getStatString(String text, int value, String unit){
		return text + ": " + Formatting.WHITE + value + unit;
	}
}
