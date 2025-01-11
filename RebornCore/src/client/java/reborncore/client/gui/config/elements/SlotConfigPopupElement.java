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

package reborncore.client.gui.config.elements;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.ColorCode;
import net.minecraft.util.math.Direction;
import reborncore.RebornCore;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiSprites;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.network.serverbound.IoSavePayload;
import reborncore.common.network.serverbound.SlotSavePayload;

import java.util.Arrays;
import java.util.stream.Stream;

public class SlotConfigPopupElement extends AbstractConfigPopupElement {
	private final int id;
	private final boolean allowInput;

	public SlotConfigPopupElement(int slotId, int x, int y, int height, boolean allowInput) {
		super(
			x, y, height, GuiSprites.SLOT_CONFIG_POPUP,
			Stream.concat(
				Arrays.stream(SlotConfiguration.ExtractConfig.values())
					.filter(config -> allowInput || config != SlotConfiguration.ExtractConfig.INPUT)
					.map(Enum::name),
				Stream.of("FIRST", "LAST")
			).toArray(String[]::new)
		);
		this.id = slotId;
		this.allowInput = allowInput;
	}

	@Override
	public int getPencilColor(String pencil) {
		return switch (pencil) {
			case "INPUT" -> theme.ioInputColor().rgba();
			case "OUTPUT" -> theme.ioOutputColor().rgba();
			case "FIRST", "LAST" -> (allowInput ? theme.ioInputColor() : theme.ioOutputColor()).rgba();
			default -> 0x80000000 | theme.warningTextColor().rgba();
		};
	}

	@Override
	public void cycleConfig(Direction side, GuiBase<?> guiBase) {
		SlotConfiguration.ExtractConfig nextConfig;
		if (pencil != null) {
			switch (pencil) {
				case "INPUT":
					nextConfig = SlotConfiguration.ExtractConfig.INPUT;
					break;
				case "OUTPUT":
					nextConfig = SlotConfiguration.ExtractConfig.OUTPUT;
					break;
				case "FIRST":
				case "LAST": {
					SlotConfiguration.SlotConfigHolder configHolder = guiBase.getMachine().getSlotConfiguration().getSlotDetails(id);
					if (configHolder != null) {
						if (pencil.equals("FIRST")) {
							configHolder.first = side;
							if (configHolder.last == side) {
								configHolder.last = null;
							}
						} else {
							configHolder.last = side;
							if (configHolder.first == side) {
								configHolder.first = null;
							}
						}
						ClientPlayNetworking.send(new IoSavePayload(guiBase.be.getPos(), id, configHolder));

						if (configHolder.getSideDetail(side).getSlotIO().getIoConfig() != SlotConfiguration.ExtractConfig.NONE) {
							return;
						}
					} else {
						int priority = pencil.equals("FIRST") ? side.ordinal() * 10 + 6 : 60 + side.ordinal();
						ClientPlayNetworking.send(new IoSavePayload(guiBase.be.getPos(), id, true, true, false, priority));
					}
					nextConfig = allowInput ? SlotConfiguration.ExtractConfig.INPUT : SlotConfiguration.ExtractConfig.OUTPUT;
					break;
				}
				default: {
					SlotConfiguration.SlotConfigHolder configHolder = guiBase.getMachine().getSlotConfiguration().getSlotDetails(id);

					if (configHolder != null) {
						if (configHolder.first == side) {
							configHolder.first = null;
							ClientPlayNetworking.send(new IoSavePayload(guiBase.be.getPos(), id, configHolder));
						} else if (configHolder.last == side) {
							configHolder.last = null;
							ClientPlayNetworking.send(new IoSavePayload(guiBase.be.getPos(), id, configHolder));
						}
					}

					nextConfig = SlotConfiguration.ExtractConfig.NONE;
				}
			}
		} else {
			SlotConfiguration.SlotConfig currentSlot = guiBase.getMachine().getSlotConfiguration().getSlotDetails(id).getSideDetail(side);

			// A bit of a mess, in the future have a way to remove config options from this list
			nextConfig = currentSlot.getSlotIO().getIoConfig().getNext();
			if (!allowInput && nextConfig == SlotConfiguration.ExtractConfig.INPUT) {
				nextConfig = SlotConfiguration.ExtractConfig.OUTPUT;
			}
		}

		SlotConfiguration.SlotIO slotIO = new SlotConfiguration.SlotIO(nextConfig);
		SlotConfiguration.SlotConfig newConfig = new SlotConfiguration.SlotConfig(side, slotIO, id);
		ClientPlayNetworking.send(new SlotSavePayload(guiBase.be.getPos(), newConfig));
	}

	public void updateCheckBox(String type, GuiBase<?> guiBase) {
		SlotConfiguration.SlotConfigHolder configHolder = guiBase.getMachine().getSlotConfiguration().getSlotDetails(id);
		if (configHolder == null) {
			return;
		}

		if (type.equalsIgnoreCase("input")) {
			configHolder.setInput(!configHolder.autoInput());
		}
		if (type.equalsIgnoreCase("output")) {
			configHolder.setOutput(!configHolder.autoOutput());
		}
		if (type.equalsIgnoreCase("filter")) {
			configHolder.setFilter(!configHolder.filter());
		}

		ClientPlayNetworking.send(new IoSavePayload(guiBase.be.getPos(), id, configHolder));
	}

	@Override
	protected void drawSateColor(DrawContext drawContext, GuiBase<?> gui, Direction side, int inx, int iny) {
		iny += 4;
		int sx = inx + getX() + gui.getGuiLeft();
		int sy = iny + getY() + gui.getGuiTop();
		SlotConfiguration.SlotConfigHolder slotConfigHolder = gui.getMachine().getSlotConfiguration().getSlotDetails(id);
		if (slotConfigHolder == null) {
			RebornCore.LOGGER.debug("Hmm, this isn't supposed to happen");
			return;
		}
		SlotConfiguration.SlotConfig slotConfig = slotConfigHolder.getSideDetail(side);
		ColorCode color = switch (slotConfig.getSlotIO().getIoConfig()) {
			case INPUT -> theme.ioInputColor();
			case OUTPUT -> theme.ioOutputColor();
			default -> new ColorCode(0);
		};
		drawContext.fill(sx, sy, sx + 18, sy + 18, color.rgba());
		if (side == slotConfigHolder.first) {
			drawTag(drawContext, gui, sx, sy, "F");
		} else if (side == slotConfigHolder.last) {
			drawTag(drawContext, gui, sx, sy, "L");
		}
	}

	protected void drawTag(DrawContext drawContext, GuiBase<?> gui, int sx, int sy, String tag) {
		TextRenderer textRenderer = gui.getTextRenderer();
		Text text = Text.of(tag);
		drawContext.drawText(textRenderer, text, sx + 10 - textRenderer.getWidth(tag) / 2, sy + 6, -1, false);
	}
}
