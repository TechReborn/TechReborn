package techreborn.client;

import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.opengl.GL11;
import techreborn.Core;

import java.awt.*;

public class VersionCheckerClient {
	ResourceLocation texture;

	public VersionCheckerClient() {
			texture = new ResourceLocation("textures/gui/demo_background.png");
	}

	@SubscribeEvent
	public void drawGui(GuiScreenEvent.DrawScreenEvent event){
		if(event.gui instanceof GuiModList){
			String s = "";
			if(Core.INSTANCE.versionChecker.isChecking){
				s = "Checking for update...";
			} else if(Core.INSTANCE.versionChecker.isLatestVersion()){
				s = "You have the latest version of TechReborn";
			} else{
				s = "There is an update for TechReborn with " + Core.INSTANCE.versionChecker.getChangeLogSinceCurrentVersion().size() + " changes.";
			}
			event.gui.drawString(event.gui.mc.fontRenderer, s, 10, 5, Color.white.getRGB());
		}
		if(!Core.INSTANCE.versionChecker.isLatestVersion()){
			if(event.mouseY < 20){
				GuiUtil.drawTooltipBox(5, 15, 330, Core.INSTANCE.versionChecker.getChangeLogSinceCurrentVersion().size() * 10 + 5);
				int y = 20;
				for(String change : Core.INSTANCE.versionChecker.getChangeLogSinceCurrentVersion()){
					event.gui.drawString(event.gui.mc.fontRenderer, change, 10, y, Color.white.getRGB());
					y+= 10;
				}
			}
		}

	}
}
