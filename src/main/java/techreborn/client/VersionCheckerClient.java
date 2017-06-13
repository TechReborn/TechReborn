/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.client.gui.GuiUtil;
import techreborn.Core;

import java.awt.*;
import java.util.ArrayList;

public class VersionCheckerClient {
	ResourceLocation texture;

	public VersionCheckerClient() {
		texture = new ResourceLocation("textures/gui/demo_background.png");
	}

	@SubscribeEvent
	public void drawGui(GuiScreenEvent.DrawScreenEvent event) {
		if (event.getGui() instanceof GuiModList) {
			ArrayList<String> changeLog = Core.INSTANCE.versionChecker.getChangeLogSinceCurrentVersion();
			String s = "";
			if (Core.INSTANCE.versionChecker.isChecking) {
				s = "Checking for update...";
			} else if (Core.INSTANCE.versionChecker.isLatestVersion()) {
				s = "You have the latest version of TechReborn";
			} else {
				s = "There is an update for TechReborn with " + changeLog.size() + " changes.";
			}
			event.getGui().drawString(event.getGui().mc.fontRenderer, s, 10, 5, Color.white.getRGB());
			if (!Core.INSTANCE.versionChecker.isLatestVersion()) {
				if (event.getMouseY() < 20) {
					GuiUtil.drawTooltipBox(5, 15, 330, changeLog.size() * 10 + 5);
					int y = 20;
					for (String change : changeLog) {
						event.getGui().drawString(event.getGui().mc.fontRenderer, change, 10, y, Color.white.getRGB());
						y += 10;
					}
				}
			}
		}
	}
}
