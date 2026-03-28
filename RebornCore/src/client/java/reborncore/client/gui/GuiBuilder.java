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

package reborncore.client.gui;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import reborncore.api.IListInfoProvider;
import reborncore.client.gui.config.GuiTab;
import reborncore.common.fluid.FluidUtils;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static reborncore.client.gui.GuiSprites.drawSpriteStretched;

public class GuiBuilder {
	private static final Component SPACE_TEXT = Component.literal(" ");
	@Deprecated
	public static final Identifier GUI_ELEMENTS = Identifier.fromNamespaceAndPath("reborncore", "textures/gui/guielements.png");
	private static final boolean EXPERIMENTAL_PROGRESS_BAR = false;

	public void drawDefaultBackground(GuiGraphicsExtractor drawContext, int x, int y, int width, int height) {
		drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, GuiSprites.BACKGROUND.texture(), x, y, width, height);
	}

	public void drawPlayerSlots(GuiGraphicsExtractor drawContext, Screen gui, int posX, int posY, boolean center) {
		if (center) {
			posX -= 81;
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				drawSlot(drawContext,posX + x * 18, posY + y * 18);
			}
		}

		for (int x = 0; x < 9; x++) {
			drawSlot(drawContext, posX + x * 18, posY + 58);
		}
	}

	public void drawSlot(GuiGraphicsExtractor drawContext,int posX, int posY) {
		drawSpriteStretched(drawContext, GuiSprites.SLOT, posX, posY, 18, 18);
	}

	public void drawText(GuiGraphicsExtractor drawContext, GuiBase<?> gui, Component text, int x, int y, int color) {
		drawContext.text(gui.getFont(), text, x, y, color, false);
	}

	public void drawProgressBar(GuiGraphicsExtractor drawContext, GuiBase<?> gui, double progress, int x, int y) {
		drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x, y, 150, 18, 22, 15, 256, 256);
		int j = (int) (progress);
		if (j > 0) {
			drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x, y, 150, 34, j + 1, 15, 256, 256);
		}
	}

	public void drawOutputSlot(GuiGraphicsExtractor drawContext, int x, int y) {
		drawSpriteStretched(drawContext, GuiSprites.OUTPUT_SLOT, x, y, 26, 26);
	}

	/**
	 * Draws lock button in either locked or unlocked state
	 *
	 * @param gui    {@link GuiBase} The GUI to draw on
	 * @param x      {@code int} Top left corner where to place button
	 * @param y      {@code int} Top left corner where to place button
	 * @param mouseX {@code int} Mouse cursor position to check for tooltip
	 * @param mouseY {@code int} Mouse cursor position to check for tooltip
	 * @param layer  {@link GuiBase.Layer} The layer to draw on
	 * @param locked {@code boolean} Set to true if it is in locked state
	 */
	public void drawLockButton(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, int mouseX, int mouseY, GuiBase.Layer layer, boolean locked) {
		if (gui.hideGuiElements()) return;
		int x2 = x, y2 = y;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x2 += gui.getGuiLeft();
			y2 += gui.getGuiTop();
		}

		drawSpriteStretched(drawContext, locked ? GuiSprites.BUTTON_LOCKED : GuiSprites.BUTTON_UNLOCKED, x2, y2, 20, 12);
		if (gui.isPointInRect(x, y, 20, 12, mouseX, mouseY)) {
			List<Component> list = new ArrayList<>();
			if (locked) {
				list.add(Component.translatable("reborncore.gui.tooltip.unlock_items"));
			} else {
				list.add(Component.translatable("reborncore.gui.tooltip.lock_items"));
			}
			drawContext.setComponentTooltipForNextFrame(gui.getFont(), list, mouseX, mouseY);
		}
	}

	/**
	 * Draws hologram toggle button
	 *
	 * @param gui    {@link GuiBase} The GUI to draw on
	 * @param x      {@code int} Top left corner where to place button
	 * @param y      {@code int} Top left corner where to place button
	 * @param mouseX {@code int} Mouse cursor position to check for tooltip
	 * @param mouseY {@code int} Mouse cursor position to check for tooltip
	 * @param layer  {@link GuiBase.Layer} The layer to draw on
	 */
	public void drawHologramButton(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, int mouseX, int mouseY, GuiBase.Layer layer) {
		if (gui.isTabOpen()) return;
		boolean hasTooltip = gui.isPointInRect(x, y, 20, 12, mouseX, mouseY);
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		if (gui.getMachine().renderMultiblock) {
			drawSpriteStretched(drawContext, GuiSprites.BUTTON_HOLOGRAM_ENABLED, x, y, 20, 12);
		} else {
			drawSpriteStretched(drawContext, GuiSprites.BUTTON_HOLOGRAM_DISABLED, x, y, 20, 12);
		}
		if (hasTooltip) {
			List<Component> list = new ArrayList<>();
			list.add(Component.translatable("reborncore.gui.tooltip.hologram"));
			drawContext.setComponentTooltipForNextFrame(gui.getFont(), list, mouseX, mouseY);
		}
	}

	/**
	 * Draws big horizontal bar for heat value
	 *
	 * @param gui   {@link GuiBase} The GUI to draw on
	 * @param x     {@code int} Top left corner where to place bar
	 * @param y     {@code int} Top left corner where to place bar
	 * @param value {@code int} Current heat value
	 * @param max   {@code int} Maximum heat value
	 * @param layer {@link GuiBase.Layer} The layer to draw on
	 */
	public void drawBigHeatBar(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, int value, int max, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x, y, 26, 218, 114, 18, 256, 256);
		if (value != 0) {
			int j = (int) ((double) value / (double) max * 106);
			if (j < 0) {
				j = 0;
			}
			drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x + 4, y + 4, 26, 246, j, 10, 256, 256);

			Component text = Component.literal(String.valueOf(value))
					.append(Component.translatable("reborncore.gui.heat"));

			gui.drawCentredText(drawContext, text, y + 5, 0xFFFFFFFF, layer);
		}
	}

	/**
	 * Draws big horizontal blue bar
	 *
	 * @param gui    {@link GuiBase} The GUI to draw on
	 * @param x      {@code int} Top left corner where to place bar
	 * @param y      {@code int} Top left corner where to place bar
	 * @param value  {@code int} Current value
	 * @param max    {@code int} Maximum value
	 * @param mouseX {@code int} Mouse cursor position to check for tooltip
	 * @param mouseY {@code int} Mouse cursor position to check for tooltip
	 * @param suffix {@link String} String to put on the bar and tooltip after percentage value
	 * @param line2  {@link String} String to put into tooltip as a second line
	 * @param format {@link String} Formatted value to put on the bar
	 * @param layer  {@link GuiBase.Layer} The layer to draw on
	 */
	public void drawBigBlueBar(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, int value, int max, int mouseX, int mouseY, String suffix, Component line2, String format, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		int j = (int) ((double) value / (double) max * 106);
		if (j < 0) {
			j = 0;
		}
		drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x + 4, y + 4, 0, 236, j, 10, 256, 256);
		if (!suffix.equals("")) {
			suffix = " " + suffix;
		}
		gui.drawCentredText(drawContext, Component.literal(format).append(suffix), y + 5, 0xFFFFFFFF, layer);
		if (gui.isPointInRect(x, y, 114, 18, mouseX, mouseY)) {
			int percentage = percentage(max, value);
			List<Component> list = new ArrayList<>();

			list.add(
					Component.literal(String.valueOf(value))
							.withStyle(ChatFormatting.GOLD)
							.append("/")
							.append(String.valueOf(max))
							.append(suffix)
			);

			list.add(
					Component.literal(String.valueOf(percentage))
							.withStyle(StringUtils.getPercentageColour(percentage))
							.append("%")
							.append(
									Component.translatable("reborncore.gui.tooltip.dsu_fullness")
											.withStyle(ChatFormatting.GRAY)
							)
			);

			list.add(line2);

			if (value > max) {
				list.add(
						Component.literal("Yo this is storing more than it should be able to")
								.withStyle(ChatFormatting.GRAY)
				);
				list.add(
						Component.literal("prolly a bug")
								.withStyle(ChatFormatting.GRAY)
				);
				list.add(
						Component.literal("pls report and tell how tf you did this")
								.withStyle(ChatFormatting.GRAY)
				);
			}
			if (layer == GuiBase.Layer.FOREGROUND) {
				mouseX -= gui.getGuiLeft();
				mouseY -= gui.getGuiTop();
			}
			drawContext.setComponentTooltipForNextFrame(gui.getFont(), list, mouseX, mouseY);
		}
	}

	public void drawBigBlueBar(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, int value, int max, int mouseX, int mouseY, String suffix, GuiBase.Layer layer) {
		drawBigBlueBar(drawContext, gui, x, y, value, max, mouseX, mouseY, suffix, Component.empty(), Integer.toString(value), layer);

	}

	public void drawBigBlueBar(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, int value, int max, int mouseX, int mouseY, GuiBase.Layer layer) {
		drawBigBlueBar(drawContext, gui, x, y, value, max, mouseX, mouseY, "", Component.empty(), "", layer);
	}

	/**
	 * Shades GUI and draw gray bar on top of GUI
	 *
	 * @param gui   {@link GuiBase} The GUI to draw on
	 * @param layer {@link GuiBase.Layer} The layer to draw on
	 */
	public void drawMultiblockMissingBar(GuiGraphicsExtractor drawContext, GuiBase<?> gui, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		int x = 0;
		int y = 4;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}

		drawContext.fillGradient(x, y, x + 176, y + 20, 0x000000, 0xC0000000);
		drawContext.fillGradient(x, y + 20, x + 176, y + 20 + 48, 0xC0000000, 0xC0000000);
		drawContext.fillGradient(x, y + 68, x + 176, y + 70 + 20, 0xC0000000, 0x00000000);

		gui.drawCentredText(drawContext, Component.translatable("reborncore.gui.missingmultiblock"), 43, 0xFFFFFFFF, layer);
	}

	/**
	 * Draws upgrade slots on the left side of machine GUI. Draws on the background
	 * level.
	 *
	 * @param gui {@link GuiBase} The GUI to draw on
	 * @param x   {@code int} Top left corner where to place slots
	 * @param y   {@code int} Top left corner where to place slots
	 */
	public void drawUpgrades(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y) {
		drawSpriteStretched(drawContext, GuiSprites.UPGRADES, x, y, 24, 81);
	}

	/**
	 * Draws tab on the left side of machine GUI. Draws on the background level.
	 *
	 * @param gui   {@link GuiBase} The GUI to draw on
	 * @param x     {@code int} Top left corner where to place tab
	 * @param y     {@code int} Top left corner where to place tab
	 * @param stack {@link ItemStack} Item to show as tab icon
	 */
	public void drawSlotTab(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, ItemStack stack) {
		drawSpriteStretched(drawContext, GuiSprites.SLOT_TAB, x, y, 24, 24);
		drawContext.item(stack, x + 5, y + 4);
	}


	/**
	 * Draws Slot Configuration tips instead of player inventory
	 *
	 * @param gui    {@link GuiBase} The GUI to draw on
	 * @param x      {@code int} Top left corner where to place tips list
	 * @param y      {@code int} Top left corner where to place tips list
	 * @param mouseX {@code int} Mouse cursor position
	 * @param mouseY {@code int} Mouse cursor position
	 */
	public void drawSlotConfigTips(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, int mouseX, int mouseY, GuiTab guiTab) {
		List<Component> tips = guiTab.getTips().stream()
				.map(Component::translatable)
				.collect(Collectors.toList());

		TipsListWidget explanation = new TipsListWidget(gui, gui.getScreenWidth() - 14, 76, y, 9 + 2, tips);
		explanation.setX(x - 81);
		explanation.setScrollAmount(0);
		explanation.extractRenderState(drawContext, mouseX, mouseY, 1.0f);
	}

	private static class TipsListWidget extends AbstractSelectionList<TipsListWidget.TipsListEntry> {
		private final Theme theme;

		public TipsListWidget(GuiBase<?> gui, int width, int height, int top, int entryHeight, List<Component> tips) {
			super(gui.getMinecraft(), width, height, top, entryHeight);
			for (Component tip : tips) {
				this.addEntry(new TipsListEntry(tip));
			}
			theme = gui.theme;
		}

		@Override
		public int getRowWidth() {
			return 162;
		}

		@Override
		public void extractListItems(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float delta) {
			drawContext.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xff202020);
			super.extractListItems(drawContext, mouseX, mouseY, delta);
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput builder) {
		}

		private class TipsListEntry extends AbstractSelectionList.Entry<TipsListWidget.TipsListEntry> {
			private final Component tip;

			public TipsListEntry(Component tip) {
				this.tip = tip;
			}

			@Override
			public void extractContent(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
				drawContext.textWithWordWrap(Minecraft.getInstance().font, tip, getContentX(), getContentY(), getWidth(), theme.subtitleColor().rgba());
			}
		}
	}

	// TODO: change to double

	/**
	 * Draws energy output value and icon
	 *
	 * @param gui       {@link GuiBase} The GUI to draw on
	 * @param x         {@code int} Top left corner where to place energy output
	 * @param y         {@code int} Top left corner where to place energy output
	 * @param maxOutput {@code int} Energy output value
	 * @param layer     {@link GuiBase.Layer} The layer to draw on
	 */
	public void drawEnergyOutput(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, int maxOutput, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		Component text = Component.literal(PowerSystem.getLocalizedPowerNoSuffix(maxOutput))
				.append(SPACE_TEXT)
				.append(PowerSystem.ABBREVIATION)
				.append(" ");

		int width = gui.getFont().width(text);
		gui.drawText(drawContext, text, x - width - 2, y + 5, 0xff000000, layer);
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x, y, 150, 91, 16, 16, 256, 256);
	}

	/**
	 * Draws progress arrow in direction specified.
	 *
	 * @param gui         {@link GuiBase} The GUI to draw on
	 * @param progress    {@code int} Current progress
	 * @param maxProgress {@code int} Maximum progress
	 * @param x           {@code int} Top left corner where to place progress arrow
	 * @param y           {@code int} Top left corner where to place progress arrow
	 * @param mouseX      {@code int} Mouse cursor position to check for tooltip
	 * @param mouseY      {@code int} Mouse cursor position to check for tooltip
	 * @param direction   {@link ProgressDirection} Direction of the progress arrow
	 * @param layer       {@link GuiBase.Layer} The layer to draw on
	 */
	public void drawProgressBar(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int progress, int maxProgress, int x, int y, int mouseX, int mouseY, ProgressDirection direction, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}

		drawSpriteStretched(drawContext, direction.baseSprite, x, y, direction.width, direction.height);
		int j = (int) ((double) progress / (double) maxProgress * 16);
		if (j < 0) {
			j = 0;
		}

		if (EXPERIMENTAL_PROGRESS_BAR) {
			switch (direction) {
				case RIGHT, LEFT -> drawSpriteStretched(drawContext, direction.overlaySprite, x, y, j, 10, direction.width, direction.height, gui);
				case UP, DOWN -> drawSpriteStretched(drawContext, direction.overlaySprite, x, y, 10, j, direction.width, direction.height, gui);
			}
		} else {
			switch (direction) {
				case RIGHT -> drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x, y, direction.xActive, direction.yActive, j, 10, 256, 256);
				case LEFT -> drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x + 16 - j, y, direction.xActive + 16 - j, direction.yActive, j, 10, 256, 256);
				case UP -> drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x, y + 16 - j, direction.xActive, direction.yActive + 16 - j, 10, j, 256, 256);
				case DOWN -> drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x, y, direction.xActive, direction.yActive, 10, j, 256, 256);
			}
		}

		final TextureAtlasSprite sprite = GuiBase.getSprite(direction.baseSprite);

		if (gui.isPointInRect(x, y, direction.width, direction.height, mouseX, mouseY)) {
			int percentage = percentage(maxProgress, progress);
			List<Component> list = new ArrayList<>();
			list.add(
					Component.literal(String.valueOf(percentage))
							.withStyle(StringUtils.getPercentageColour(percentage))
							.append("%")
			);
			drawContext.setComponentTooltipForNextFrame(gui.getFont(), list, mouseX, mouseY);
		}
	}

	/**
	 * Draws multi-energy bar
	 *
	 * @param gui             {@link GuiBase} The GUI to draw on
	 * @param x               {@code int} Top left corner where to place energy bar
	 * @param y               {@code int} Top left corner where to place energy bar
	 * @param energyStored    {@code long} Current amount of energy
	 * @param maxEnergyStored {@code long} Maximum amount of energy
	 * @param mouseX          {@code int} Mouse cursor position to check for tooltip
	 * @param mouseY          {@code int} Mouse cursor position to check for tooltip
	 * @param buttonID        {@code int} Button ID used to switch energy systems
	 * @param layer           {@link GuiBase.Layer} The layer to draw on
	 */
	public void drawMultiEnergyBar(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, long energyStored, long maxEnergyStored, int mouseX,
								int mouseY, int buttonID, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}

		drawSpriteStretched(drawContext, GuiSprites.POWER_BAR_BASE, x, y, 14, 50);

		int barHeight = 48;
		int draw = (int) ((double) energyStored / (double) maxEnergyStored * (barHeight));
		if (energyStored > maxEnergyStored) {
			draw = barHeight;
		}
		drawSpriteStretched(drawContext, GuiSprites.POWER_BAR_OVERLAY, x + 1, y + 49 - draw, 12, draw, 12, 48);

		int percentage = percentage(maxEnergyStored, energyStored);
		if (gui.isPointInRect(x + 1, y + 1, 11, 48, mouseX, mouseY)) {
			List<Component> list = Lists.newArrayList();
			boolean hasShift = Minecraft.getInstance().hasShiftDown();
			if (hasShift) {
				list.add(
						Component.literal(PowerSystem.getLocalizedPowerFullNoSuffix(energyStored))
								.withStyle(ChatFormatting.GOLD)
								.append("/")
								.append(PowerSystem.getLocalizedPowerFull(maxEnergyStored))
				);
			} else {
				list.add(
						Component.literal(PowerSystem.getLocalizedPowerNoSuffix(energyStored))
								.withStyle(ChatFormatting.GOLD)
								.append("/")
								.append(PowerSystem.getLocalizedPower(maxEnergyStored))
				);
			}
			list.add(
					StringUtils.getPercentageText(percentage)
							.append(SPACE_TEXT)
							.append(
									Component.translatable("reborncore.gui.tooltip.power_charged")
											.withStyle(ChatFormatting.GRAY)
							)
			);

			if (gui.be instanceof IListInfoProvider) {
				if (hasShift) {
					((IListInfoProvider) gui.be).addInfo(list, true, true);
				} else {
					list.add(Component.empty());

					list.add(
							Component.literal("Shift")
									.withStyle(ChatFormatting.BLUE)
									.append(SPACE_TEXT)
									.withStyle(ChatFormatting.GRAY)
									.append(Component.translatable("reborncore.gui.tooltip.power_moreinfo"))
					);
				}
			}
			drawContext.setComponentTooltipForNextFrame(gui.getFont(), list, mouseX, mouseY);
		}
	}

	/**
	 * Draws tank and fluid inside it
	 *
	 * @param gui         {@link GuiBase} The GUI to draw on
	 * @param x           {@code int} Top left corner of tank
	 * @param y           {@code int} Top left corner of tank
	 * @param mouseX      {@code int} Mouse cursor position to check for tooltip
	 * @param mouseY      {@code int} Mouse cursor position to check for tooltip
	 * @param fluid       {@link FluidInstance} to draw in tank
	 * @param maxCapacity {@code int} Maximum tank capacity
	 * @param isTankEmpty {@code boolean} True if tank is empty
	 * @param layer       {@link GuiBase.Layer} The layer to draw on
	 */
	public void drawTank(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int x, int y, int mouseX, int mouseY, FluidInstance fluid, FluidValue maxCapacity, boolean isTankEmpty, GuiBase.Layer layer) {
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
		drawSpriteStretched(drawContext, GuiSprites.TANK_BACKGROUND, x, y, 22, 56);
		if (!isTankEmpty) {
			drawFluid(drawContext, gui, fluid, x + 4, y + 4, 14, 48, maxCapacity.getRawValue());
		}
		drawSpriteStretched(drawContext, GuiSprites.TANK_FOREGROUND, x + 3, y + 3, 16, 50);

		if (gui.isPointInRect(x, y, 22, 56, mouseX, mouseY)) {
			List<Component> list = new ArrayList<>();
			if (isTankEmpty) {
				list.add(Component.translatable("reborncore.gui.tooltip.tank_empty").withStyle(ChatFormatting.GOLD));
			} else {
				list.add(
						Component.literal(String.format("%s / %s", amount, maxCapacity))
								.withStyle(ChatFormatting.GOLD)
								.append(SPACE_TEXT)
								.append(FluidUtils.getFluidName(fluid))
				);
			}

			list.add(
					StringUtils.getPercentageText(percentage)
							.withStyle(ChatFormatting.GRAY)
							.append(SPACE_TEXT)
							.append(Component.translatable("reborncore.gui.tooltip.tank_fullness"))
			);

			drawContext.setComponentTooltipForNextFrame(gui.getFont(), list, mouseX, mouseY);
		}
	}

	/**
	 * Draws fluid in tank
	 *
	 * @param gui         {@link GuiBase} The GUI to draw on
	 * @param fluid       {@link FluidInstance} Fluid to draw
	 * @param x           {@code int} Top left corner of fluid
	 * @param y           {@code int} Top left corner of fluid
	 * @param width       {@code int} Width of fluid to draw
	 * @param height      {@code int} Height of fluid to draw
	 * @param maxCapacity {@code int} Maximum capacity of tank
	 */
	public void drawFluid(GuiGraphicsExtractor drawContext, GuiBase<?> gui, FluidInstance fluid, int x, int y, int width, int height, long maxCapacity) {
		if (fluid.fluid() == Fluids.EMPTY) {
			return;
		}
		// Get sprite from vanilla FluidModel instead of Fabric's FluidVariantRendering
		final TextureAtlasSprite sprite = Minecraft.getInstance()
			.getModelManager()
			.getFluidStateModelSet()
			.get(fluid.fluid().defaultFluidState())
			.stillMaterial()
			.sprite();
		int color = FluidVariantRendering.getColor(fluid.fluidVariant());

		final int drawHeight = (int) (fluid.getAmount().getRawValue() / (maxCapacity * 1F) * height);
		y += height - drawHeight;
		int count = drawHeight / width;
		int remainder = drawHeight % width;
		for (int i = 0; i < count; i++) {
			drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, width, width, color);
			y += width;
		}
		if (remainder != 0) {
			drawContext.enableScissor(x, y, x + width, y + remainder);
			drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, width, width, color);
			drawContext.disableScissor();
		}
	}

	/**
	 * Draws burning progress, similar to vanilla furnace
	 *
	 * @param gui         {@link GuiBase} The GUI to draw on
	 * @param progress    {@code int} Current progress
	 * @param maxProgress {@code int} Maximum progress
	 * @param x           {@code int} Top left corner where to place burn bar
	 * @param y           {@code int} Top left corner where to place burn bar
	 * @param mouseX      {@code int} Mouse cursor position to check for tooltip
	 * @param mouseY      {@code int} Mouse cursor position to check for tooltip
	 * @param layer       {@link GuiBase.Layer} The layer to draw on
	 */
	public void drawBurnBar(GuiGraphicsExtractor drawContext, GuiBase<?> gui, int progress, int maxProgress, int x, int y, int mouseX, int mouseY, GuiBase.Layer layer) {
		if (gui.hideGuiElements()) return;
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x, y, 150, 64, 13, 13, 256, 256);
		int j = 13 - (int) ((double) progress / (double) maxProgress * 13);
		if (j > 0) {
			drawContext.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, x, y + j, 150, 51 + j, 13, 13 - j, 256, 256);

		}
		if (gui.isPointInRect(x, y, 12, 12, mouseX, mouseY)) {
			int percentage = percentage(maxProgress, progress);
			List<Component> list = new ArrayList<>();
			list.add(StringUtils.getPercentageText(percentage));
			drawContext.setComponentTooltipForNextFrame(gui.getFont(), list, mouseX, mouseY);
		}
	}

	/**
	 * Draws bar containing output slots
	 *
	 * @param x     {@code int} Top left corner where to place slots bar
	 * @param y     {@code int} Top left corner where to place slots bar
	 * @param count {@code int} Number of output slots
	 */
	public void drawOutputSlotBar(GuiGraphicsExtractor drawContext, int x, int y, int count) {
		drawSpriteStretched(drawContext, GuiSprites.SLOT_BAR_RIGHT, x, y, 3, 26);
		x += 3;
		for (int i = 1; i <= count; i++) {
			drawSpriteStretched(drawContext, GuiSprites.SLOT_BAR_CENTER, x, y, 20, 26);
			x += 20;
		}
		drawSpriteStretched(drawContext, GuiSprites.SLOT_BAR_LEFT, x, y, 3, 26);
	}

	protected int percentage(long MaxValue, long CurrentValue) {
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
		public final SpriteId baseSprite;
		public final SpriteId overlaySprite;
		public final int x;
		public final int y;
		public final int xActive;
		public final int yActive;
		public final int width;
		public final int height;

		ProgressDirection(int x, int y, int xActive, int yActive, int width, int height) {
			this.baseSprite = GuiSprites.create("progress_%s_base".formatted(name().toLowerCase(Locale.ROOT)));
			this.overlaySprite = GuiSprites.create("progress_%s_overlay".formatted(name().toLowerCase(Locale.ROOT)));
			this.x = x;
			this.y = y;
			this.xActive = xActive;
			this.yActive = yActive;
			this.width = width;
			this.height = height;
		}
	}
}
