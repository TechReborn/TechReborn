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

package reborncore.client.gui.builder.slot.elements;

import net.minecraft.util.Identifier;

public record Sprite(Identifier textureLocation, int x, int y, int width, int height) {
	public static final Sprite EMPTY = new Sprite(ElementBase.MECH_ELEMENTS, 0, 0, 0, 0);
	public static final Sprite SLOT_NORMAL = new Sprite(ElementBase.MECH_ELEMENTS, 0, 0, 18, 18);
	public static final Sprite CHARGE_SLOT_ICON = new Sprite(ElementBase.MECH_ELEMENTS, 18, 0, 18, 18);
	public static final Sprite DISCHARGE_SLOT_ICON = new Sprite(ElementBase.MECH_ELEMENTS, 36, 0, 18, 18);
	public static final Sprite ENERGY_BAR = new Sprite(ElementBase.MECH_ELEMENTS, 0, 18, 12, 40);
	public static final Sprite ENERGY_BAR_BACKGROUND = new Sprite(ElementBase.MECH_ELEMENTS, 12, 18, 14, 42);
	public static final Sprite TOP_ENERGY_BAR = new Sprite(ElementBase.MECH_ELEMENTS, 0, 215, 167, 2);
	public static final Sprite TOP_ENERGY_BAR_BACKGROUND = new Sprite(ElementBase.MECH_ELEMENTS, 0, 217, 169, 3);
	public static final Sprite LEFT_TAB = new Sprite(ElementBase.MECH_ELEMENTS, 0, 86, 23, 26);
	public static final Sprite LEFT_TAB_SELECTED = new Sprite(ElementBase.MECH_ELEMENTS, 0, 60, 29, 26);
	public static final Sprite CONFIGURE_ICON = new Sprite(ElementBase.MECH_ELEMENTS, 26, 18, 16, 16);
	public static final Sprite UPGRADE_ICON = new Sprite(ElementBase.MECH_ELEMENTS, 26, 34, 16, 16);
	public static final Sprite ENERGY_ICON = new Sprite(ElementBase.MECH_ELEMENTS, 46, 19, 9, 13);
	public static final Sprite ENERGY_ICON_EMPTY = new Sprite(ElementBase.MECH_ELEMENTS, 62, 19, 9, 13);
	public static final Sprite JEI_ICON = new Sprite(ElementBase.MECH_ELEMENTS, 42, 34, 16, 16);
	public static final Sprite BUTTON_SLOT_NORMAL = new Sprite(ElementBase.MECH_ELEMENTS, 54, 0, 18, 18);
	public static final Sprite FAKE_SLOT = new Sprite(ElementBase.MECH_ELEMENTS, 72, 0, 18, 18);
	public static final Sprite BUTTON_HOVER_OVERLAY_SLOT_NORMAL = new Sprite(ElementBase.MECH_ELEMENTS, 90, 0, 18, 18);
	public static final Sprite SLOT_CONFIG_POPUP = new Sprite(ElementBase.MECH_ELEMENTS, 29, 60, 62, 62);
	public static final Sprite.Button EXIT_BUTTON = new Sprite.Button(new Sprite(ElementBase.MECH_ELEMENTS, 26, 122, 13, 13), new Sprite(ElementBase.MECH_ELEMENTS, 39, 122, 13, 13));
	public static final Sprite.CheckBox DARK_CHECK_BOX = new Sprite.CheckBox(new Sprite(ElementBase.MECH_ELEMENTS, 74, 18, 13, 13), new Sprite(ElementBase.MECH_ELEMENTS, 87, 18, 16, 13));
	public static final Sprite.CheckBox LIGHT_CHECK_BOX = new Sprite.CheckBox(new Sprite(ElementBase.MECH_ELEMENTS, 74, 31, 13, 13), new Sprite(ElementBase.MECH_ELEMENTS, 87, 31, 16, 13));

	public Sprite {
		assert x >= 0 && y >= 0 && width >= 0 && height >= 0;
	}

	public record Button(Sprite normal,
						Sprite hovered) {
	}

	public record CheckBox(Sprite normal,
						Sprite ticked) {
	}
}
