/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2023 TeamReborn
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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import reborncore.client.gui.config.elements.GuiSpriteAtlasHolder;

public final class GuiSprites {
	public static final SpriteIdentifier CHARGE_SLOT_ICON = create("charge_slot_icon");
	public static final SpriteIdentifier DISCHARGE_SLOT_ICON = create("discharge_slot_icon");
	public static final SpriteIdentifier ENERGY_BAR = create("energy_bar");
	public static final SpriteIdentifier ENERGY_BAR_BACKGROUND = create("energy_bar_background");
	public static final SpriteIdentifier TOP_ENERGY_BAR = create("top_energy_bar");
	public static final SpriteIdentifier TOP_ENERGY_BAR_BACKGROUND = create("top_energy_bar_background");
	public static final SpriteIdentifier LEFT_TAB = create("left_tab");
	public static final SpriteIdentifier LEFT_TAB_SELECTED = create("left_tab_selected");
	public static final SpriteIdentifier CONFIGURE_ICON = create("configure_icon");
	public static final SpriteIdentifier UPGRADE_ICON = create("upgrade_icon");
	public static final SpriteIdentifier ENERGY_ICON = create("energy_icon");
	public static final SpriteIdentifier ENERGY_ICON_EMPTY = create("energy_icon_empty");
	public static final SpriteIdentifier JEI_ICON = create("jei_icon");
	public static final SpriteIdentifier BUTTON_SLOT_NORMAL = create("button_slot_normal");
	public static final SpriteIdentifier FAKE_SLOT = create("fake_slot");
	public static final SpriteIdentifier BUTTON_HOVER_OVERLAY_SLOT_NORMAL = create("button_hover_overlay_slot_normal");
	public static final SpriteIdentifier SLOT_CONFIG_POPUP = create("slot_config_popup");
	public static final SpriteIdentifier FAST_FORWARD = create("fast_forward");
	public static final SpriteIdentifier FORWARD = create("forward");
	public static final SpriteIdentifier REWIND = create("rewind");
	public static final SpriteIdentifier FAST_REWIND = create("fast_rewind");
	public static final SpriteIdentifier SLOT = create("slot");
	public static final SpriteIdentifier OUTPUT_SLOT = create("output_slot");
	public static final SpriteIdentifier BUTTON_UNLOCKED = create("button_unlocked");
	public static final SpriteIdentifier BUTTON_LOCKED = create("button_locked");
	public static final SpriteIdentifier BUTTON_HOLOGRAM_ENABLED = create("button_hologram_enabled");
	public static final SpriteIdentifier BUTTON_HOLOGRAM_DISABLED = create("button_hologram_disabled");
	public static final SpriteIdentifier SLOT_TAB = create("slot_tab");
	public static final SpriteIdentifier UPGRADES = create("upgrades");
	public static final SpriteIdentifier TANK_BACKGROUND = create("tank_background");
	public static final SpriteIdentifier TANK_FOREGROUND = create("tank_foreground");
	public static final SpriteIdentifier SLOT_BAR_RIGHT = create("slot_bar_right");
	public static final SpriteIdentifier SLOT_BAR_CENTER = create("slot_bar_center");
	public static final SpriteIdentifier SLOT_BAR_LEFT = create("slot_bar_left");
	public static final SpriteIdentifier POWER_BAR_BASE = create("power_bar_base");
	public static final SpriteIdentifier POWER_BAR_OVERLAY = create("power_bar_overlay");

	public static final SpriteIdentifier BACKGROUND_BODY = create("background_body");
	public static final SpriteIdentifier BACKGROUND_EDGE_BOTTOM = create("background_edge_bottom");
	public static final SpriteIdentifier BACKGROUND_EDGE_LEFT = create("background_edge_left");
	public static final SpriteIdentifier BACKGROUND_EDGE_RIGHT = create("background_edge_right");
	public static final SpriteIdentifier BACKGROUND_EDGE_TOP = create("background_edge_top");
	public static final SpriteIdentifier BACKGROUND_CORNER_TOP_LEFT= create("background_corner_top_left");
	public static final SpriteIdentifier BACKGROUND_CORNER_TOP_RIGHT = create("background_corner_top_right");
	public static final SpriteIdentifier BACKGROUND_CORNER_BOTTOM_LEFT= create("background_corner_bottom_left");
	public static final SpriteIdentifier BACKGROUND_CORNER_BOTTOM_RIGHT = create("background_corner_bottom_right");

	public static final SpriteIdentifier EXIT_BUTTON_NORMAL = create("exit_button_normal");
	public static final SpriteIdentifier EXIT_BUTTON_HOVERED = create("exit_button_hovered");
	public static final Button EXIT_BUTTON = new Button(EXIT_BUTTON_NORMAL, EXIT_BUTTON_HOVERED);

	public static final SpriteIdentifier DARK_CHECK_BOX_NORMAL = create("dark_check_box_normal");
	public static final SpriteIdentifier DARK_CHECK_BOX_TICKED = create("dark_check_box_ticked");
	public static final CheckBox DARK_CHECK_BOX = new CheckBox(DARK_CHECK_BOX_NORMAL, DARK_CHECK_BOX_TICKED);

	public static final SpriteIdentifier LIGHT_CHECK_BOX_NORMAL = create("light_check_box_normal");
	public static final SpriteIdentifier LIGHT_CHECK_BOX_TICKED = create("light_check_box_ticked");
	public static final CheckBox LIGHT_CHECK_BOX = new CheckBox(LIGHT_CHECK_BOX_NORMAL, LIGHT_CHECK_BOX_TICKED);

	public static SpriteIdentifier create(String name) {
		return new SpriteIdentifier(GuiSpriteAtlasHolder.ATLAS_ID, new Identifier("reborncore", name));
	}

	public static void drawSprite(DrawContext drawContext, SpriteIdentifier spriteIdentifier, int x, int y) {
		final Sprite sprite = GuiBase.getSprite(spriteIdentifier);

		drawContext.drawSprite(
			x,
			y,
			0,
			sprite.getContents().getWidth(),
			sprite.getContents().getHeight(),
			sprite
		);
	}

	public static void drawSprite(DrawContext drawContext, SpriteIdentifier spriteIdentifier, int x, int y, int width, int height) {
		drawSprite(drawContext, spriteIdentifier, x, y, width, height, 0, 0);
	}

	public static void drawSprite(DrawContext drawContext, SpriteIdentifier spriteIdentifier, int x, int y, int width, int height, GuiBase<?> gui) {
		drawSprite(drawContext, spriteIdentifier, x, y, width, height, gui.getGuiLeft(), gui.getGuiTop());
	}

	public static void drawSprite(DrawContext drawContext, SpriteIdentifier spriteIdentifier, int x, int y, int width, int height, int sx, int sy) {
		drawContext.enableScissor(x + sx, y + sy,  x + width+ sx, y + height+ sy);
		drawSprite(
			drawContext,
			spriteIdentifier,
			x,
			y
		);
		drawContext.disableScissor();
	}

	public record Button(SpriteIdentifier normal, SpriteIdentifier hovered) {
	}

	public record CheckBox(SpriteIdentifier normal, SpriteIdentifier ticked) {
	}
}
