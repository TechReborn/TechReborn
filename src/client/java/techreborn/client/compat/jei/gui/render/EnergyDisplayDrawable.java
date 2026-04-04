package techreborn.client.compat.jei.gui.render;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.client.gui.GuiSprites;
import reborncore.common.crafting.RebornRecipe;

import static reborncore.client.gui.GuiSprites.drawSpriteStretched;

public class EnergyDisplayDrawable implements IDrawable {
	private final RebornRecipe recipe;
	public EnergyDisplayDrawable(RebornRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public int getWidth() {
		return 14;
	}

	@Override
	public int getHeight() {
		return 50;
	}

	@Override
	public void draw(GuiGraphicsExtractor guiGraphics, int x, int y) {
		GuiBuilder.INSTANCE.drawMultiEnergyBar(guiGraphics, JeiRenderTarget.INSTANCE, x, y, recipe.power(), recipe.power(), -1, -1, 0, GuiBase.Layer.FOREGROUND);
	}

	public static boolean isMouseOver(int x, int y, double mouseX, double mouseY) {
		return mouseX >= x && mouseX < x + 14 && mouseY >= y && mouseY < y + 50;
	}

}
