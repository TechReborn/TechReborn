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

package reborncore.client.gui.guibuilder;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import reborncore.api.IListInfoProvider;
import reborncore.client.RenderUtil;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.slot.GuiTab;
import reborncore.common.fluid.FluidUtil;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PowerSystem.EnergySystem;
import reborncore.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gigabit101 on 08/08/2016.
 */
public class GuiBuilder {
	public static final Identifier defaultTextureSheet = new Identifier("reborncore", "textures/gui/guielements.png");
	private static final Text SPACE_TEXT = new LiteralText(" ");
	static Identifier resourceLocation;

	public GuiBuilder() {
		GuiBuilder.resourceLocation = defaultTextureSheet;
	}

	public GuiBuilder(Identifier resourceLocation) {
		GuiBuilder.resourceLocation = resourceLocation;
	}

	public Identifier getResourceLocation() {
		return resourceLocation;
	}

	public void drawDefaultBackground(MatrixStack matrixStack, Screen gui, int x, int y, int width, int height) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		MinecraftClient.getInstance().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 0, 0, width / 2, height / 2);
		gui.drawTexture(matrixStack, x + width / 2, y, 150 - width / 2, 0, width / 2, height / 2);
		gui.drawTexture(matrixStack, x, y + height / 2, 0, 150 - height / 2, width / 2, height / 2);
		gui.drawTexture(matrixStack, x + width / 2, y + height / 2, 150 - width / 2, 150 - height / 2, width / 2,
				height / 2);
	}

	public void drawPlayerSlots(MatrixStack matrixStack, Screen gui, int posX, int posY, boolean center) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(resourceLocation);

		if (center) {
			posX -= 81;
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				gui.drawTexture(matrixStack, posX + x * 18, posY + y * 18, 150, 0, 18, 18);
			}
		}

		for (int x = 0; x < 9; x++) {
			gui.drawTexture(matrixStack, posX + x * 18, posY + 58, 150, 0, 18, 18);
		}
	}

	public void drawSlot(MatrixStack matrixStack, Screen gui, int posX, int posY) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, posX, posY, 150, 0, 18, 18);
	}

	public void drawText(MatrixStack matrixStack, GuiBase<?> gui, Text text, int x, int y, int color) {
		gui.getTextRenderer().draw(matrixStack, text, x, y, color);
	}

	public void drawProgressBar(MatrixStack matrixStack, GuiBase<?> gui, double progress, int x, int y) {
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 150, 18, 22, 15);
		int j = (int) (progress);
		if (j > 0) {
			gui.drawTexture(matrixStack, x, y, 150, 34, j + 1, 15);
		}
	}

	public void drawOutputSlot(MatrixStack matrixStack, GuiBase<?> gui, int x, int y) {
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 174, 0, 26, 26);
	}

	/**
	 * Draws button with JEI icon in the given coords.
	 *
	 * @param gui   GuiBase GUI to draw on
	 * @param x     int Top left corner where to place button
	 * @param y     int Top left corner where to place button
	 * @param layer Layer Layer to draw on
	 */
	public void drawJEIButton(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (FabricLoader.getInstance().isModLoaded("jei")) {
			if (layer == GuiBase.Layer.BACKGROUND) {
				x += gui.getGuiLeft();
				y += gui.getGuiTop();
			}
			gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
			gui.drawTexture(matrixStack, x, y, 202, 0, 12, 12);
		}
	}

	/**
	 * Draws lock button in either locked or unlocked state
	 *
	 * @param gui    GuiBase GUI to draw on
	 * @param x      int Top left corner where to place button
	 * @param y      int Top left corner where to place button
	 * @param mouseX int Mouse cursor position to check for tooltip
	 * @param mouseY int Mouse cursor position to check for tooltip
	 * @param layer  Layer Layer to draw on
	 * @param locked boolean Set to true if it is in locked state
	 */
	public void drawLockButton(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int mouseX, int mouseY, GuiBase.Layer layer, boolean locked) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 174, 26 + (locked ? 12 : 0), 20, 12);
		if (gui.isPointInRect(x, y, 20, 12, mouseX, mouseY)) {
			List<Text> list = new ArrayList<>();
			if (locked) {
				list.add(new TranslatableText("reborncore.gui.tooltip.unlock_items"));
			} else {
				list.add(new TranslatableText("reborncore.gui.tooltip.lock_items"));
			}
			RenderSystem.pushMatrix();
			gui.renderTooltip(matrixStack, list, mouseX, mouseY);
			RenderSystem.popMatrix();
		}
	}

	/**
	 * Draws hologram toggle button
	 *
	 * @param gui    GuiBase GUI to draw on
	 * @param x      int Top left corner where to place button
	 * @param y      int Top left corner where to place button
	 * @param mouseX int Mouse cursor position to check for tooltip
	 * @param mouseY int Mouse cursor position to check for tooltip
	 * @param layer  Layer Layer to draw on
	 */
	public void drawHologramButton(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int mouseX, int mouseY, GuiBase.Layer layer) {
		if (gui.isTabOpen()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		if (gui.getMachine().renderMultiblock) {
			gui.drawTexture(matrixStack, x, y, 174, 62, 20, 12);
		} else {
			gui.drawTexture(matrixStack, x, y, 174, 50, 20, 12);
		}
		if (gui.isPointInRect(x, y, 20, 12, mouseX, mouseY)) {
			List<Text> list = new ArrayList<>();
			list.add(new TranslatableText("reborncore.gui.tooltip.hologram"));
			RenderSystem.pushMatrix();
			if (layer == GuiBase.Layer.FOREGROUND) {
				mouseX -= gui.getGuiLeft();
				mouseY -= gui.getGuiTop();
			}
			gui.renderTooltip(matrixStack, list, mouseX, mouseY);
			RenderSystem.popMatrix();
		}
	}

	/**
	 * Draws big horizontal bar for heat value
	 *
	 * @param gui   GuiBase GUI to draw on
	 * @param x     int Top left corner where to place bar
	 * @param y     int Top left corner where to place bar
	 * @param value int Current heat value
	 * @param max   int Maximum heat value
	 * @param layer Layer Layer to draw on
	 */
	public void drawBigHeatBar(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int value, int max, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 26, 218, 114, 18);
		if (value != 0) {
			int j = (int) ((double) value / (double) max * 106);
			if (j < 0) {
				j = 0;
			}
			gui.drawTexture(matrixStack, x + 4, y + 4, 26, 246, j, 10);

			Text text = new LiteralText(String.valueOf(value))
					.append(new TranslatableText("reborncore.gui.heat"));

			gui.drawCentredText(matrixStack, text, y + 5, 0xFFFFFF, layer);
		}
	}

	/**
	 * Draws big horizontal blue bar
	 *
	 * @param gui    GuiBase GUI to draw on
	 * @param x      int Top left corner where to place bar
	 * @param y      int Top left corner where to place bar
	 * @param value  int Current value
	 * @param max    int Maximum value
	 * @param mouseX int Mouse cursor position to check for tooltip
	 * @param mouseY int Mouse cursor position to check for tooltip
	 * @param suffix String String to put on the bar and tooltip after percentage value
	 * @param line2  String String to put into tooltip as a second line
	 * @param format String Formatted value to put on the bar
	 * @param layer  Layer Layer to draw on
	 */
	public void drawBigBlueBar(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int value, int max, int mouseX, int mouseY, String suffix, Text line2, String format, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		int j = (int) ((double) value / (double) max * 106);
		if (j < 0) {
			j = 0;
		}
		gui.drawTexture(matrixStack, x + 4, y + 4, 0, 236, j, 10);
		if (!suffix.equals("")) {
			suffix = " " + suffix;
		}
		gui.drawCentredText(matrixStack, new LiteralText(format).append(suffix), y + 5, 0xFFFFFF, layer);
		if (gui.isPointInRect(x, y, 114, 18, mouseX, mouseY)) {
			int percentage = percentage(max, value);
			List<Text> list = new ArrayList<>();

			list.add(
					new LiteralText(String.valueOf(value))
							.formatted(Formatting.GOLD)
							.append("/")
							.append(String.valueOf(max))
							.append(suffix)
			);

			list.add(
					new LiteralText(String.valueOf(percentage))
							.formatted(StringUtils.getPercentageColour(percentage))
							.append("%")
							.append(
									new TranslatableText("reborncore.gui.tooltip.dsu_fullness")
											.formatted(Formatting.GRAY)
							)
			);

			list.add(line2);

			if (value > max) {
				list.add(
						new LiteralText("Yo this is storing more than it should be able to")
								.formatted(Formatting.GRAY)
				);
				list.add(
						new LiteralText("prolly a bug")
								.formatted(Formatting.GRAY)
				);
				list.add(
						new LiteralText("pls report and tell how tf you did this")
								.formatted(Formatting.GRAY)
				);
			}
			if (layer == GuiBase.Layer.FOREGROUND) {
				mouseX -= gui.getGuiLeft();
				mouseY -= gui.getGuiTop();
			}
			gui.renderTooltip(matrixStack, list, mouseX, mouseY);
			RenderSystem.disableLighting();
			RenderSystem.color4f(1, 1, 1, 1);
		}
	}

	public void drawBigBlueBar(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int value, int max, int mouseX, int mouseY, String suffix, GuiBase.Layer layer) {
		drawBigBlueBar(matrixStack, gui, x, y, value, max, mouseX, mouseY, suffix, LiteralText.EMPTY, Integer.toString(value), layer);

	}

	public void drawBigBlueBar(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int value, int max, int mouseX, int mouseY, GuiBase.Layer layer) {
		drawBigBlueBar(matrixStack, gui, x, y, value, max, mouseX, mouseY, "", LiteralText.EMPTY, "", layer);
	}

	/**
	 * Shades GUI and draw gray bar on top of GUI
	 *
	 * @param gui   GuiBase GUI to draw on
	 * @param layer Layer Layer to draw on
	 */
	public void drawMultiblockMissingBar(MatrixStack matrixStack, GuiBase<?> gui, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		int x = 0;
		int y = 4;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		RenderSystem.disableLighting();
		RenderSystem.enableDepthTest();
		RenderSystem.colorMask(true, true, true, false);
		RenderUtil.drawGradientRect(0, x, y, x + 176, y + 20, 0x000000, 0xC0000000);
		RenderUtil.drawGradientRect(0, x, y + 20, x + 176, y + 20 + 48, 0xC0000000, 0xC0000000);
		RenderUtil.drawGradientRect(0, x, y + 68, x + 176, y + 70 + 20, 0xC0000000, 0x00000000);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.disableDepthTest();
		gui.drawCentredText(matrixStack, new TranslatableText("reborncore.gui.missingmultiblock"), 43, 0xFFFFFF, layer);
	}

	/**
	 * Draws upgrade slots on the left side of machine GUI. Draws on the background
	 * level.
	 *
	 * @param gui GuiBase GUI to draw on
	 * @param x   int Top left corner where to place slots
	 * @param y   int Top left corner where to place slots
	 */
	public void drawUpgrades(MatrixStack matrixStack, GuiBase<?> gui, int x, int y) {
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 217, 0, 24, 81);
	}

	/**
	 * Draws tab on the left side of machine GUI. Draws on the background level.
	 *
	 * @param gui   GuiBase GUI to draw on
	 * @param x     int Top left corner where to place tab
	 * @param y     int Top left corner where to place tab
	 * @param stack ItemStack Item to show as tab icon
	 */
	public void drawSlotTab(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, ItemStack stack) {
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 217, 82, 24, 24);
		gui.getMinecraft().getItemRenderer().renderInGuiWithOverrides(stack, x + 5, y + 4);
	}


	/**
	 * Draws Slot Configuration tips instead of player inventory
	 *
	 * @param gui    GuiBase GUI to draw on
	 * @param x      int Top left corner where to place tips list
	 * @param y      int Top left corner where to place tips list
	 * @param mouseX int Mouse cursor position
	 * @param mouseY int Mouse cursor position
	 */
	public void drawSlotConfigTips(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int mouseX, int mouseY, GuiTab guiTab) {
		List<Text> tips = guiTab.getTips().stream()
				.map(TranslatableText::new)
				.collect(Collectors.toList());

		TipsListWidget explanation = new TipsListWidget(gui, gui.getScreenWidth() - 14, 54, y, y + 76, 9 + 2, tips);
		explanation.setLeftPos(x - 81);
		explanation.render(matrixStack, mouseX, mouseY, 1.0f);
		RenderSystem.color4f(1, 1, 1, 1);
	}


	private class TipsListWidget extends EntryListWidget<TipsListWidget.TipsListEntry> {

		public TipsListWidget(GuiBase<?> gui, int width, int height, int top, int bottom, int entryHeight, List<Text> tips) {
			super(gui.getMinecraft(), width, height, top, bottom, entryHeight);
			for (Text tip : tips) {
				this.addEntry(new TipsListEntry(tip));
			}
		}

		@Override
		public int getRowWidth() {
			return 162;
		}

		@Override
		protected void renderBackground(MatrixStack matrixStack) {

		}

		@Override
		public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuffer();
			this.client.getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
			bufferBuilder.vertex(this.left, this.bottom, 0.0D).texture((float) this.left / 32.0F, (float) (this.bottom + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
			bufferBuilder.vertex(this.right, this.bottom, 0.0D).texture((float) this.right / 32.0F, (float) (this.bottom + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
			bufferBuilder.vertex(this.right, this.top, 0.0D).texture((float) this.right / 32.0F, (float) (this.top + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
			bufferBuilder.vertex(this.left, this.top, 0.0D).texture((float) this.left / 32.0F, (float) (this.top + (int) this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
			tessellator.draw();

			super.renderList(matrices, this.getRowLeft(), this.top, mouseX, mouseY, delta);
		}

		private class TipsListEntry extends EntryListWidget.Entry<TipsListWidget.TipsListEntry> {
			private final Text tip;

			public TipsListEntry(Text tip) {
				this.tip = tip;
			}

			@Override
			public void render(MatrixStack matrixStack, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {
				MinecraftClient.getInstance().textRenderer.drawTrimmed(tip, x, y, width, 11184810);
			}
		}
	}

	//TODO: change to double
	/**
	 * Draws energy output value and icon
	 *
	 * @param gui       GuiBase GUI to draw on
	 * @param x         int Top left corner where to place energy output
	 * @param y         int Top left corner where to place energy output
	 * @param maxOutput int Energy output value
	 * @param layer     Layer Layer to draw on
	 */
	public void drawEnergyOutput(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int maxOutput, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		Text text = new LiteralText(PowerSystem.getLocalizedPowerNoSuffix(maxOutput))
				.append(SPACE_TEXT)
				.append(PowerSystem.getDisplayPower().abbreviation)
				.append("\t");

		int width = gui.getTextRenderer().getWidth(text);
		gui.drawText(matrixStack, text, x - width - 2, y + 5, 0, layer);
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 150, 91, 16, 16);
	}

	/**
	 * Draws progress arrow in direction specified.
	 *
	 * @param gui         GuiBase GUI to draw on
	 * @param progress    int Current progress
	 * @param maxProgress int Maximum progress
	 * @param x           int Top left corner where to place progress arrow
	 * @param y           int Top left corner where to place progress arrow
	 * @param mouseX      int Mouse cursor position to check for tooltip
	 * @param mouseY      int Mouse cursor position to check for tooltip
	 * @param direction   ProgressDirection Direction of progress arrow
	 * @param layer       Layer Layer to draw on
	 */
	public void drawProgressBar(MatrixStack matrixStack, GuiBase<?> gui, int progress, int maxProgress, int x, int y, int mouseX, int mouseY, ProgressDirection direction, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}

		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, direction.x, direction.y, direction.width, direction.height);
		int j = (int) ((double) progress / (double) maxProgress * 16);
		if (j < 0) {
			j = 0;
		}

		switch (direction) {
			case RIGHT:
				gui.drawTexture(matrixStack, x, y, direction.xActive, direction.yActive, j, 10);
				break;
			case LEFT:
				gui.drawTexture(matrixStack, x + 16 - j, y, direction.xActive + 16 - j, direction.yActive, j, 10);
				break;
			case UP:
				gui.drawTexture(matrixStack, x, y + 16 - j, direction.xActive, direction.yActive + 16 - j, 10, j);
				break;
			case DOWN:
				gui.drawTexture(matrixStack, x, y, direction.xActive, direction.yActive, 10, j);
				break;
			default:
				return;
		}

		if (gui.isPointInRect(x, y, direction.width, direction.height, mouseX, mouseY)) {
			int percentage = percentage(maxProgress, progress);
			List<Text> list = new ArrayList<>();
			list.add(
					new LiteralText(String.valueOf(percentage))
							.formatted(StringUtils.getPercentageColour(percentage))
							.append("%")
			);
			if (layer == GuiBase.Layer.FOREGROUND) {
				mouseX -= gui.getGuiLeft();
				mouseY -= gui.getGuiTop();
			}
			gui.renderTooltip(matrixStack, list, mouseX, mouseY);
			RenderSystem.disableLighting();
			RenderSystem.color4f(1, 1, 1, 1);
		}
	}

	/**
	 * Draws multi-energy bar
	 *
	 * @param gui             GuiBase GUI to draw on
	 * @param x               int Top left corner where to place energy bar
	 * @param y               int Top left corner where to place energy bar
	 * @param energyStored    int Current amount of energy
	 * @param maxEnergyStored int Maximum amount of energy
	 * @param mouseX          int Mouse cursor position to check for tooltip
	 * @param mouseY          int Mouse cursor position to check for tooltip
	 * @param buttonID        int Button ID used to switch energy systems
	 * @param layer           Layer Layer to draw on
	 */
	public void drawMultiEnergyBar(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int energyStored, int maxEnergyStored, int mouseX,
								   int mouseY, int buttonID, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}

		EnergySystem displayPower = PowerSystem.getDisplayPower();
		MinecraftClient.getInstance().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, displayPower.xBar - 15, displayPower.yBar - 1, 14, 50);
		int draw = (int) ((double) energyStored / (double) maxEnergyStored * (48));
		if (energyStored > maxEnergyStored) {
			draw = 48;
		}
		gui.drawTexture(matrixStack, x + 1, y + 49 - draw, displayPower.xBar, 48 + displayPower.yBar - draw, 12, draw);
		int percentage = percentage(maxEnergyStored, energyStored);
		if (gui.isPointInRect(x + 1, y + 1, 11, 48, mouseX, mouseY)) {
			List<Text> list = Lists.newArrayList();
			if (Screen.hasShiftDown()) {
				list.add(
						new LiteralText(PowerSystem.getLocalizedPowerFullNoSuffix(energyStored))
								.formatted(Formatting.GOLD)
								.append("/")
								.append(PowerSystem.getLocalizedPowerFull(maxEnergyStored))
				);
			} else {
				list.add(
						new LiteralText(PowerSystem.getLocalizedPowerNoSuffix(energyStored))
								.formatted(Formatting.GOLD)
								.append("/")
								.append(PowerSystem.getLocalizedPower(maxEnergyStored))
				);
			}
			list.add(
					StringUtils.getPercentageText(percentage)
							.append(SPACE_TEXT)
							.append(
									new TranslatableText("reborncore.gui.tooltip.power_charged")
											.formatted(Formatting.GRAY)
							)
			);

			if (gui.be instanceof IListInfoProvider) {
				if (Screen.hasShiftDown()) {
					((IListInfoProvider) gui.be).addInfo(list, true, true);
				} else {
					list.add(LiteralText.EMPTY);

					list.add(
							new LiteralText("Shift")
									.formatted(Formatting.BLUE)
									.append(SPACE_TEXT)
									.formatted(Formatting.GRAY)
									.append(new TranslatableText("reborncore.gui.tooltip.power_moreinfo"))
					);
				}
			}
			if (layer == GuiBase.Layer.FOREGROUND) {
				mouseX -= gui.getGuiLeft();
				mouseY -= gui.getGuiTop();
			}
			gui.renderTooltip(matrixStack, list, mouseX, mouseY);
			RenderSystem.disableLighting();
			RenderSystem.color4f(1, 1, 1, 1);
		}
	}

	/**
	 * Draws tank and fluid inside it
	 *
	 * @param gui         GuiBase GUI to draw on
	 * @param x           int Top left corner of tank
	 * @param y           int Top left corner of tank
	 * @param mouseX      int Mouse cursor position to check for tooltip
	 * @param mouseY      int Mouse cursor position to check for tooltip
	 * @param fluid       FluidStack Fluid to draw in tank
	 * @param maxCapacity int Maximum tank capacity
	 * @param isTankEmpty boolean True if tank is empty
	 * @param layer       Layer Layer to draw on
	 */
	public void drawTank(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int mouseX, int mouseY, FluidInstance fluid, FluidValue maxCapacity, boolean isTankEmpty, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}

		int percentage = 0;
		FluidValue amount = FluidValue.EMPTY;
		if (!isTankEmpty) {
			amount = fluid.getAmount();
			percentage = percentage(maxCapacity.getRawValue(), amount.getRawValue());
		}
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 194, 26, 22, 56);
		if (!isTankEmpty) {
			drawFluid(matrixStack, gui, fluid, x + 4, y + 4, 14, 48, maxCapacity.getRawValue());
		}
		gui.drawTexture(matrixStack, x + 3, y + 3, 194, 82, 16, 50);

		if (gui.isPointInRect(x, y, 22, 56, mouseX, mouseY)) {
			List<Text> list = new ArrayList<>();
			if (isTankEmpty) {
				list.add(new TranslatableText("reborncore.gui.tooltip.tank_empty").formatted(Formatting.GOLD));
			} else {
				list.add(
						new LiteralText(String.format("%s / %s", amount, maxCapacity))
								.formatted(Formatting.GOLD)
								.append(SPACE_TEXT)
								.append(FluidUtil.getFluidName(fluid))
				);
			}

			list.add(
					StringUtils.getPercentageText(percentage)
							.formatted(Formatting.GRAY)
							.append(SPACE_TEXT)
							.append(new TranslatableText("reborncore.gui.tooltip.tank_fullness"))
			);

			if (layer == GuiBase.Layer.FOREGROUND) {
				mouseX -= gui.getGuiLeft();
				mouseY -= gui.getGuiTop();
			}
			gui.renderTooltip(matrixStack, list, mouseX, mouseY);
			RenderSystem.disableLighting();
			RenderSystem.color4f(1, 1, 1, 1);
		}
	}

	/**
	 * Draws fluid in tank
	 *
	 * @param gui         GuiBase GUI to draw on
	 * @param fluid       FluidStack Fluid to draw
	 * @param x           int Top left corner of fluid
	 * @param y           int Top left corner of fluid
	 * @param width       int Width of fluid to draw
	 * @param height      int Height of fluid to draw
	 * @param maxCapacity int Maximum capacity of tank
	 */
	public void drawFluid(MatrixStack matrixStack, GuiBase<?> gui, FluidInstance fluid, int x, int y, int width, int height, int maxCapacity) {
		if (fluid.getFluid() == Fluids.EMPTY) {
			return;
		}
		gui.getMinecraft().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
		y += height;
		final Sprite sprite = FluidRenderHandlerRegistry.INSTANCE.get(fluid.getFluid()).getFluidSprites(gui.getMachine().getWorld(), gui.getMachine().getPos(), fluid.getFluid().getDefaultState())[0];
		int color = FluidRenderHandlerRegistry.INSTANCE.get(fluid.getFluid()).getFluidColor(gui.getMachine().getWorld(), gui.getMachine().getPos(), fluid.getFluid().getDefaultState());

		final int drawHeight = (int) (fluid.getAmount().getRawValue() / (maxCapacity * 1F) * height);
		final int iconHeight = sprite.getHeight();
		int offsetHeight = drawHeight;

		RenderSystem.color3f((color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F);

		int iteration = 0;
		while (offsetHeight != 0) {
			final int curHeight = offsetHeight < iconHeight ? offsetHeight : iconHeight;

			DrawableHelper.drawSprite(matrixStack, x, y - offsetHeight, 0, width, curHeight, sprite);
			offsetHeight -= curHeight;
			iteration++;
			if (iteration > 50) {
				break;
			}
		}
		RenderSystem.color3f(1F, 1F, 1F);

		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
	}

	/**
	 * Draws burning progress, similar to vanilla furnace
	 *
	 * @param gui         GuiBase GUI to draw on
	 * @param progress    int Current progress
	 * @param maxProgress int Maximum progress
	 * @param x           int Top left corner where to place burn bar
	 * @param y           int Top left corner where to place burn bar
	 * @param mouseX      int Mouse cursor position to check for tooltip
	 * @param mouseY      int Mouse cursor position to check for tooltip
	 * @param layer       Layer Layer to draw on
	 */
	public void drawBurnBar(MatrixStack matrixStack, GuiBase<?> gui, int progress, int maxProgress, int x, int y, int mouseX, int mouseY, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 150, 64, 13, 13);
		int j = 13 - (int) ((double) progress / (double) maxProgress * 13);
		if (j > 0) {
			gui.drawTexture(matrixStack, x, y + j, 150, 51 + j, 13, 13 - j);

		}
		if (gui.isPointInRect(x, y, 12, 12, mouseX, mouseY)) {
			int percentage = percentage(maxProgress, progress);
			List<Text> list = new ArrayList<>();
			list.add(StringUtils.getPercentageText(percentage));
			if (layer == GuiBase.Layer.FOREGROUND) {
				mouseX -= gui.getGuiLeft();
				mouseY -= gui.getGuiTop();
			}
			gui.renderTooltip(matrixStack, list, mouseX, mouseY);
			RenderSystem.disableLighting();
			RenderSystem.color4f(1, 1, 1, 1);
		}
	}

	/**
	 * Draws bar containing output slots
	 *
	 * @param gui   GuiBase GUI to draw on
	 * @param x     int Top left corner where to place slots bar
	 * @param y     int Top left corner where to place slots bar
	 * @param count int Number of output slots
	 */
	public void drawOutputSlotBar(MatrixStack matrixStack, GuiBase<?> gui, int x, int y, int count) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(resourceLocation);
		gui.drawTexture(matrixStack, x, y, 150, 122, 3, 26);
		x += 3;
		for (int i = 1; i <= count; i++) {
			gui.drawTexture(matrixStack, x, y, 150 + 3, 122, 20, 26);
			x += 20;
		}
		gui.drawTexture(matrixStack, x, y, 150 + 23, 122, 3, 26);
	}

	protected int percentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0) {
			return 0;
		}
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}

	public enum ProgressDirection {
		RIGHT(58, 150, 74, 150, 16, 10),
		LEFT(74, 160, 58, 160, 16, 10),
		DOWN(78, 170, 88, 170, 10, 16),
		UP(58, 170, 68, 170, 10, 16);
		public int x;
		public int y;
		public int xActive;
		public int yActive;
		public int width;
		public int height;

		ProgressDirection(int x, int y, int xActive, int yActive, int width, int height) {
			this.x = x;
			this.y = y;
			this.xActive = xActive;
			this.yActive = yActive;
			this.width = width;
			this.height = height;
		}
	}
}
