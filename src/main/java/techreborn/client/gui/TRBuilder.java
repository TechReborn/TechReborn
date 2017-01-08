package techreborn.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import reborncore.client.guibuilder.GuiBuilder;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.lib.ModInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prospector
 */
public class TRBuilder extends GuiBuilder {
	public static final ResourceLocation GUI_SHEET = new ResourceLocation(ModInfo.MOD_ID.toLowerCase() + ":" + "textures/gui/gui_sheet.png");

	public TRBuilder() {
		super(GUI_SHEET);
	}

	public void drawMultiEnergyBar(GuiBase gui, int x, int y, int energyStored, int maxEnergyStored, int mouseX, int mouseY, int buttonID, GuiBase.Layer layer) {
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		if (layer == GuiBase.Layer.FOREGROUND) {
			mouseX -= gui.getGuiLeft();
			mouseY -= gui.getGuiTop();
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);

		gui.drawTexturedModalRect(x, y, PowerSystem.getDisplayPower().xBar - 15, PowerSystem.getDisplayPower().yBar - 1, 14, 50);

		int draw = (int) ((double) energyStored / (double) maxEnergyStored * (48));
		if (energyStored > maxEnergyStored) {
			draw = (int) ((double) maxEnergyStored / (double) maxEnergyStored * (48));
		}
		gui.drawTexturedModalRect(x + 1, y + 49 - draw, PowerSystem.getDisplayPower().xBar, 48 + PowerSystem.getDisplayPower().yBar - draw, 12, draw);
		int percentage = percentage(maxEnergyStored, energyStored);
		if (isInRect(x + 1, y + 1, 11, 48, mouseX, mouseY)) {
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.colorMask(true, true, true, false);
			GuiUtils.drawGradientRect(0, x + 1, y + 1, x + 13, y + 49, 0x80FFFFFF, 0x80FFFFFF);
			GlStateManager.colorMask(true, true, true, true);
			GlStateManager.enableDepth();

			List<String> list = new ArrayList<>();
			TextFormatting powerColour = TextFormatting.GOLD;
			list.add(powerColour + PowerSystem.getLocaliszedPowerFormattedNoSuffix(energyStored) + "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix(maxEnergyStored) + " " + PowerSystem.getDisplayPower().abbreviation);
			list.add(getPercentageColour(percentage) + "" + percentage + "%" + TextFormatting.GRAY + " Charged");
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mouseX, mouseY, gui.width, gui.height, -1, gui.mc.fontRendererObj);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);
		}
		gui.addPowerButton(x, y, buttonID, layer);
	}

	public void drawBigBlueBar(GuiBase gui, int x, int y, int value, int max, int mouseX, int mouseY, String suffix, GuiBase.Layer layer) {
		if (layer == GuiBase.Layer.BACKGROUND) {
			x += gui.getGuiLeft();
			y += gui.getGuiTop();
		}
		gui.mc.getTextureManager().bindTexture(GUI_SHEET);
		if (!suffix.equals("")) {
			suffix = " " + suffix;
		}
		gui.drawTexturedModalRect(x, y, 0, 218, 114, 18);
		int j = (int) ((double) value / (double) max * 106);
		if (j < 0)
			j = 0;
		gui.drawTexturedModalRect(x + 4, y + 4, 0, 236, j, 10);
		gui.drawCentredString(value + suffix, y + 5, 0xFFFFFF, layer);
		if (isInRect(x, y, 114, 18, mouseX, mouseY)) {
			int percentage = percentage(max, value);
			List<String> list = new ArrayList<>();
			list.add("" + TextFormatting.GOLD + value + "/" + max + suffix);
			list.add(getPercentageColour(percentage) + "" + percentage + "%" + TextFormatting.GRAY + " Full");

			if (value > max) {
				list.add(TextFormatting.GRAY + "Yo this is storing more than it should be able to");
				list.add(TextFormatting.GRAY + "prolly a bug");
				list.add(TextFormatting.GRAY + "pls report and tell how tf you did this");
			}
			net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(list, mouseX, mouseY, gui.width, gui.height, -1, gui.mc.fontRendererObj);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);
		}
	}

	public void drawBigBlueBar(GuiBase gui, int x, int y, int value, int max, int mouseX, int mouseY, GuiBase.Layer layer) {
		drawBigBlueBar(gui, x, y, value, max, mouseX, mouseY, "", layer);
	}

	public void drawSelectedStack(GuiBase gui, int x, int y) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_SHEET);
		gui.drawTexturedModalRect(x - 4, y - 4, 202, 44, 24, 24);
	}

	public TextFormatting getPercentageColour(int percentage) {
		if (percentage <= 10) {
			return TextFormatting.RED;
		} else if (percentage >= 75) {
			return TextFormatting.GREEN;
		} else {
			return TextFormatting.YELLOW;
		}
	}

	public int percentage(int MaxValue, int CurrentValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}
}
