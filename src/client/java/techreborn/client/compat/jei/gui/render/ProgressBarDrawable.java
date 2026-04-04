package techreborn.client.compat.jei.gui.render;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.crafting.RebornRecipe;

import static reborncore.client.gui.GuiSprites.drawSpriteStretched;

public class ProgressBarDrawable implements IDrawable {
	private final GuiBuilder.ProgressDirection direction;
	private final int millisPerCycle;

	public ProgressBarDrawable(GuiBuilder.ProgressDirection direction, int millisPerCycle) {
		this.direction = direction;
		this.millisPerCycle = millisPerCycle;
	}

	public static ProgressBarDrawable right(int duration) {
		return new ProgressBarDrawable(GuiBuilder.ProgressDirection.RIGHT, duration);
	}

	public static ProgressBarDrawable right(RebornRecipe recipe) {
		return right(recipe.time() * 50);
	}

	public static ProgressBarDrawable left(int duration) {
		return new ProgressBarDrawable(GuiBuilder.ProgressDirection.LEFT, duration);
	}

	public static ProgressBarDrawable left(RebornRecipe recipe) {
		return left(recipe.time() * 50);
	}

	public static ProgressBarDrawable down(int duration) {
		return new ProgressBarDrawable(GuiBuilder.ProgressDirection.DOWN, duration);
	}

	public static ProgressBarDrawable down(RebornRecipe recipe) {
		return down(recipe.time() * 50);
	}

	public static ProgressBarDrawable up(int duration) {
		return new ProgressBarDrawable(GuiBuilder.ProgressDirection.UP, duration);
	}

	public static ProgressBarDrawable up(RebornRecipe recipe) {
		return up(recipe.time() * 50);
	}

	@Override
	public int getWidth() {
		return direction.width;
	}

	@Override
	public int getHeight() {
		return direction.height;
	}

	@Override
	public void draw(GuiGraphicsExtractor drawContext, int x, int y) {
		int progress = Math.toIntExact(System.currentTimeMillis() % millisPerCycle);
		int maxProgress = millisPerCycle;
		GuiBuilder.INSTANCE.drawProgressBar(drawContext, JeiRenderTarget.INSTANCE, progress, maxProgress, x, y, -1, -1, direction, GuiBase.Layer.FOREGROUND);
	}
}
