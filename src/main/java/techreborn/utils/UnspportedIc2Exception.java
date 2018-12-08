package techreborn.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.client.IDisplayableError;
import net.minecraftforge.fml.common.Loader;
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
		errorScreen.drawCenteredString(fontRenderer, "This can happen due to unoffical versions of ic2.", errorScreen.width / 2, 50, 0xEEEEEE);
		errorScreen.drawCenteredString(fontRenderer, "If you are using offical ic2 please open an issue @ https://github.com/TechReborn/TechReborn/issues", errorScreen.width / 2, 60, 0xEEEEEE);

		if(Loader.isModLoaded("ic2-classic-spmod")){
			errorScreen.drawCenteredString(fontRenderer, "If you are using IC2 Classic please contact the mod author and ask for compatbility to be fixed", errorScreen.width / 2, 100, Color.GREEN.getRGB());
		}
	}
}
