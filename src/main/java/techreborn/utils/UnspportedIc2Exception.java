package techreborn.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.client.IDisplayableError;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class UnspportedIc2Exception extends CustomModLoadingErrorDisplayException implements IDisplayableError {

	@Override
	public String getMessage() {
		return "IC2 is installed, but no supported helper was setup";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime) {
		errorScreen.drawCenteredString(fontRenderer, "IC2 is installed, but no supported helper was setup", errorScreen.width / 2, 10, Color.RED.getRGB());
		errorScreen.drawCenteredString(fontRenderer, "This can happen due to using forked versions of ic2.", errorScreen.width / 2, 50, 0xEEEEEE);
		errorScreen.drawCenteredString(fontRenderer, "If you are using offical ic2 please open an issue @https://github.com/TechReborn/TechReborn/issues", errorScreen.width / 2, 60, 0xEEEEEE);

		errorScreen.drawCenteredString(fontRenderer, "If you are using a fork of IC2 please contact the developer and ask for TechReborn support", errorScreen.width / 2, 100, Color.GREEN.getRGB());
	}
}
