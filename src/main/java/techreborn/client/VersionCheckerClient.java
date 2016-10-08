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
			event.getGui().drawString(event.getGui().mc.fontRendererObj, s, 10, 5, Color.white.getRGB());
			if (!Core.INSTANCE.versionChecker.isLatestVersion()) {
				if (event.getMouseY() < 20) {
					GuiUtil.drawTooltipBox(5, 15, 330, changeLog.size() * 10 + 5);
					int y = 20;
					for (String change : changeLog) {
						event.getGui().drawString(event.getGui().mc.fontRendererObj, change, 10, y, Color.white.getRGB());
						y += 10;
					}
				}
			}
		}
	}
}
