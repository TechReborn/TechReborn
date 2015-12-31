package techreborn.compat.jei;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.text.NumberFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StatCollector;

public class RecipeInfoUtil {
	private static final int color = Color.darkGray.getRGB();

	private RecipeInfoUtil() {
	}

	public static void drawInfo(@Nonnull Minecraft minecraft, int x, int y, final double startCost, final double euPerTick, final int tickTime) {
		FontRenderer fontRendererObj = minecraft.fontRendererObj;
		int lineSpacing = fontRendererObj.FONT_HEIGHT + 1;

		NumberFormat formatter = NumberFormat.getInstance();
		String startCostEU = formatter.format(startCost);
		String startCostString = StatCollector.translateToLocalFormatted("techreborn.jei.recipe.start.cost", startCostEU);
		fontRendererObj.drawString(startCostString, x, y, color);
		y += lineSpacing;

		drawInfo(minecraft, x, y, euPerTick, tickTime);
	}

	public static void drawInfo(@Nonnull Minecraft minecraft, int x, int y, final double euPerTick, final int tickTime) {
		FontRenderer fontRendererObj = minecraft.fontRendererObj;
		int lineSpacing = fontRendererObj.FONT_HEIGHT + 1;

		String runningCostString = StatCollector.translateToLocalFormatted("techreborn.jei.recipe.running.cost", euPerTick);
		fontRendererObj.drawString(runningCostString, x, y, color);
		y += lineSpacing;

		String processingTimeString1 = StatCollector.translateToLocalFormatted("techreborn.jei.recipe.processing.time.1", tickTime);
		fontRendererObj.drawString(processingTimeString1, x, y, color);
		y += lineSpacing;

		int seconds = tickTime / 20;
		String processingTimeString2 = StatCollector.translateToLocalFormatted("techreborn.jei.recipe.processing.time.2", seconds);
		fontRendererObj.drawString(processingTimeString2, x + 10, y, color);
	}
}
