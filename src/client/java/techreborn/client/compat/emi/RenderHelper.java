package techreborn.client.compat.emi;

import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;
import reborncore.client.gui.GuiBuilder;

import java.util.List;

public class RenderHelper {
	private static final EmiTexture POWER_BAR_BASE = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 126, 150, 14, 50);
	private static final EmiTexture POWER_BAR_OVERLAY = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 140, 150, 14, 50);

	private static final EmiTexture TANK_BASE = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 194, 26, 22, 56);
	private static final EmiTexture TANK_OVERLAY = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 194, 82, 16, 50);

	private static final EmiTexture ARROW_RIGHT_BASE = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 58, 150, 16, 10);
	private static final EmiTexture ARROW_RIGHT_OVERLAY = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 74, 150, 16, 10);
	private static final EmiTexture ARROW_LEFT_BASE = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 74, 160, 16, 10);
	private static final EmiTexture ARROW_LEFT_OVERLAY = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 58, 160, 16, 10);
	private static final EmiTexture ARROW_UP_BASE = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 58, 170, 10, 16);
	private static final EmiTexture ARROW_UP_OVERLAY = new EmiTexture(GuiBuilder.GUI_ELEMENTS, 68, 170, 10, 16);

	public static void createProgressBar(WidgetHolder widgetHolder, int x, int y, int animDuration, GuiBuilder.ProgressDirection direction) {
		switch (direction) {
			case RIGHT -> {
				widgetHolder.addTexture(ARROW_RIGHT_BASE, x, y);
				widgetHolder.addAnimatedTexture(ARROW_RIGHT_OVERLAY, x, y, animDuration, true, false, false);
			}
			case LEFT -> {
				widgetHolder.addTexture(ARROW_LEFT_BASE, x, y);
				widgetHolder.addAnimatedTexture(ARROW_LEFT_OVERLAY, x, y, animDuration, true, true, false);
			}
			case UP -> {
				widgetHolder.addTexture(ARROW_UP_BASE, x, y);
				widgetHolder.addAnimatedTexture(ARROW_UP_OVERLAY, x, y, animDuration, false, true, false);
			}
		}
	}

	public static void createEnergyDisplay(WidgetHolder widgetHolder, int x, int y, int recipeCost, int animDuration) {
		widgetHolder.addTexture(POWER_BAR_BASE, x, y).tooltip((mx, my) -> List.of(
			TooltipComponent.of(Text.translatable("techreborn.jei.recipe.running.cost", "E", recipeCost).asOrderedText())));

		widgetHolder.addAnimatedTexture(POWER_BAR_OVERLAY, x, y, animDuration, false, true, true);
	}

	public static void createFluidDisplay(WidgetHolder widgetHolder, int x, int y) {}
}
