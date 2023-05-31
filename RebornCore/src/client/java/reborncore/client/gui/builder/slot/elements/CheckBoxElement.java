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

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.text.Text;
import reborncore.client.gui.builder.GuiBase;

import java.util.function.Predicate;

public class CheckBoxElement extends ElementBase {
	private final Text label;
	private final Predicate<CheckBoxElement> ticked;
	private final Runnable onChange;
	private final GuiSprites.CheckBox checkBoxSprite;

	public CheckBoxElement(Text label, int x, int y,
						Predicate<CheckBoxElement> ticked,
						Runnable onChange) {
		super(x, y, GuiSprites.LIGHT_CHECK_BOX.normal());
		this.checkBoxSprite = GuiSprites.LIGHT_CHECK_BOX;
		this.label = label;
		this.ticked = ticked;
		this.onChange = onChange;
		updateSprites();
	}

	private void updateSprites() {
		if (ticked.test(this)) {
			setSprite(checkBoxSprite.ticked());
		} else {
			setSprite(checkBoxSprite.normal());
		}
	}

	@Override
	public boolean onClick(GuiBase<?> gui, double mouseX, double mouseY) {
		onChange.run();
		updateSprites();
		return true;
	}

	@Override
	public void draw(DrawContext drawContext, GuiBase<?> gui, int mouseX, int mouseY) {
		SpriteIdentifier sprite = checkBoxSprite.normal();
		if (ticked.test(this)) {
			sprite = checkBoxSprite.ticked();
		}
		drawSprite(drawContext, gui, sprite, getX(), getY()	);
		drawText(drawContext, gui, label, getX() + getWidth() + 5, ((getY() + getHeight() / 2) - (gui.getTextRenderer().fontHeight / 2)), 0xFFFFFFFF);
	}

}
