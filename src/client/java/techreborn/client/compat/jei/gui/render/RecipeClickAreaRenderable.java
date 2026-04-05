/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.client.compat.jei.gui.render;


import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiSprites;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public record RecipeClickAreaRenderable(GuiBase<?> guiBase, int x, int y) implements Renderable {
	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
		if(!guiBase.hideGuiElements()) {
			GuiSprites.drawSpriteStretched(guiGraphics, GuiSprites.JEI_ICON, guiBase.getGuiLeft() + x, guiBase.getGuiTop() + y, 12, 12, 16, 16);
		}
	}

	static final List<Entry> ENTRIES = new ArrayList<>();

	public static void addEntry(Predicate<GuiBase<?>> predicate, int x, int y) {
		ENTRIES.add(new Entry(predicate, x, y));
	}

	public static void addEntry(Predicate<GuiBase<?>> predicate) {
		ENTRIES.add(new Entry(predicate, 158, 5));
	}

	public static void addEntry(Class<? extends GuiBase<?>> guiClass, int x, int y) {
		addEntry(guiClass::isInstance, x, y);
	}

	public static void addEntry(Class<? extends GuiBase<?>> guiClass) {
		addEntry(guiClass::isInstance, 158, 5);
	}

	public static void clearEntries() {
		ENTRIES.clear();
	}

	static {
		ScreenEvents.AFTER_INIT.register(RecipeClickAreaRenderable::onAfterScreenInit);
	}

	static void onAfterScreenInit(Minecraft minecraft, Screen screen, int scaledWidth, int scaledHeight) {
		if(screen instanceof GuiBase<?> guiBase) {
			for(Entry entry : ENTRIES) {
				if(entry.predicate.test(guiBase)) {
					guiBase.addRenderableOnly(new RecipeClickAreaRenderable(guiBase, entry.x, entry.y));
					return;
				}
			}
		}
	}

	record Entry(Predicate<GuiBase<?>> predicate, int x, int y) {}
}
