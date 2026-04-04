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

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

public final class GuiSprites {
	public static final SpriteId CHARGE_SLOT_ICON = create("charge_slot_icon");
	public static final SpriteId DISCHARGE_SLOT_ICON = create("discharge_slot_icon");
	public static final SpriteId ENERGY_BAR = create("energy_bar");
	public static final SpriteId ENERGY_BAR_BACKGROUND = create("energy_bar_background");
	public static final SpriteId TOP_ENERGY_BAR = create("top_energy_bar");
	public static final SpriteId TOP_ENERGY_BAR_BACKGROUND = create("top_energy_bar_background");
	public static final SpriteId LEFT_TAB = create("left_tab");
	public static final SpriteId LEFT_TAB_SELECTED = create("left_tab_selected");
	public static final SpriteId CONFIGURE_ICON = create("configure_icon");
	public static final SpriteId UPGRADE_ICON = create("upgrade_icon");
	public static final SpriteId ENERGY_ICON = create("energy_icon");
	public static final SpriteId ENERGY_ICON_EMPTY = create("energy_icon_empty");
	public static final SpriteId JEI_ICON = create("jei_icon");
	public static final SpriteId BUTTON_SLOT_NORMAL = create("button_slot_normal");
	public static final SpriteId FAKE_SLOT = create("fake_slot");
	public static final SpriteId BUTTON_HOVER_OVERLAY_SLOT_NORMAL = create("button_hover_overlay_slot_normal");
	public static final SpriteId SLOT_CONFIG_POPUP = create("slot_config_popup");
	public static final SpriteId FAST_FORWARD = create("fast_forward");
	public static final SpriteId FORWARD = create("forward");
	public static final SpriteId REWIND = create("rewind");
	public static final SpriteId FAST_REWIND = create("fast_rewind");
	public static final SpriteId SLOT = create("slot");
	public static final SpriteId OUTPUT_SLOT = create("output_slot");
	public static final SpriteId BUTTON_UNLOCKED = create("button_unlocked");
	public static final SpriteId BUTTON_LOCKED = create("button_locked");
	public static final SpriteId BUTTON_HOLOGRAM_ENABLED = create("button_hologram_enabled");
	public static final SpriteId BUTTON_HOLOGRAM_DISABLED = create("button_hologram_disabled");
	public static final SpriteId SLOT_TAB = create("slot_tab");
	public static final SpriteId UPGRADES = create("upgrades");
	public static final SpriteId TANK_BACKGROUND = create("tank_background");
	public static final SpriteId TANK_FOREGROUND = create("tank_foreground");
	public static final SpriteId SLOT_BAR_RIGHT = create("slot_bar_right");
	public static final SpriteId SLOT_BAR_CENTER = create("slot_bar_center");
	public static final SpriteId SLOT_BAR_LEFT = create("slot_bar_left");
	public static final SpriteId POWER_BAR_BASE = create("power_bar_base");
	public static final SpriteId POWER_BAR_OVERLAY = create("power_bar_overlay");

	public static final SpriteId BACKGROUND = create("background");

	public static final SpriteId EXIT_BUTTON_NORMAL = create("exit_button_normal");
	public static final SpriteId EXIT_BUTTON_HOVERED = create("exit_button_hovered");
	public static final Button EXIT_BUTTON = new Button(EXIT_BUTTON_NORMAL, EXIT_BUTTON_HOVERED);

	public static final SpriteId DARK_CHECK_BOX_NORMAL = create("dark_check_box_normal");
	public static final SpriteId DARK_CHECK_BOX_TICKED = create("dark_check_box_ticked");
	public static final CheckBox DARK_CHECK_BOX = new CheckBox(DARK_CHECK_BOX_NORMAL, DARK_CHECK_BOX_TICKED);

	public static final SpriteId LIGHT_CHECK_BOX_NORMAL = create("light_check_box_normal");
	public static final SpriteId LIGHT_CHECK_BOX_TICKED = create("light_check_box_ticked");
	public static final CheckBox LIGHT_CHECK_BOX = new CheckBox(LIGHT_CHECK_BOX_NORMAL, LIGHT_CHECK_BOX_TICKED);

	public static SpriteId create(String name) {
		return new SpriteId(Identifier.parse("gui"), Identifier.fromNamespaceAndPath("reborncore", name));
	}

	public static void drawSpriteStretched(GuiGraphicsExtractor drawContext, SpriteId spriteIdentifier, int x, int y, int textureWidth, int textureHeight) {
		final TextureAtlasSprite sprite = GuiBase.getSprite(spriteIdentifier);

		drawContext.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			sprite,
			x,
			y,
			textureWidth,
			textureHeight
		);
	}

	public static void drawSpriteStretched(GuiGraphicsExtractor drawContext, SpriteId spriteIdentifier, int x, int y, int width, int height, int textureWidth, int textureHeight, GuiBase<?> gui) {
		drawSpriteStretched(drawContext, spriteIdentifier, x, y, width, height, textureWidth, textureHeight, gui.getGuiLeft(), gui.getGuiTop());
	}

	public static void drawSpriteStretched(GuiGraphicsExtractor drawContext, SpriteId spriteIdentifier, int x, int y, int width, int height, int textureWidth, int textureHeight) {
		drawSpriteStretched(drawContext, spriteIdentifier, x, y, width, height, textureWidth, textureHeight, 0, 0);
	}

	public static void drawSpriteStretched(GuiGraphicsExtractor drawContext, SpriteId spriteIdentifier, int x, int y, int width, int height, int textureWidth, int textureHeight, int sx, int sy) {
		drawContext.enableScissor(x + sx, y + sy,  x + width+ sx, y + height+ sy);
		drawSpriteStretched(
			drawContext,
			spriteIdentifier,
			x,
			y,
			textureWidth,
			textureHeight
		);
		drawContext.disableScissor();
	}

	public record Button(SpriteId normal, SpriteId hovered) {
	}

	public record CheckBox(SpriteId normal, SpriteId ticked) {
	}
}
