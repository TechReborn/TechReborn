package techreborn.manual.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ButtonUtil {
	@SideOnly(Side.CLIENT)
	public static void addBackButton(int ID, int X, int Y, List buttonList) {
		buttonList.add(new GuiButtonCustomTexture(ID, X, Y, 50, 60, 17, 12, "button", "", "", 0, 11, 10, 16));
	}

	@SideOnly(Side.CLIENT)
	public static void addNextButton(int ID, int X, int Y, List buttonList) {
		buttonList.add(new GuiButtonCustomTexture(ID, X, Y, 50, 60, 17, 12, "button", "", "", 0, 1, 10, 17));
	}
}
