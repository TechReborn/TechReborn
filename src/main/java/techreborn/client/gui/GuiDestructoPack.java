package techreborn.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiDestructoPack extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("techreborn",
		"textures/gui/destructopack.png");

	public GuiDestructoPack(Container container) {
		super(container);
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 166);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int arg0, int arg1) {
		String name = I18n.translateToLocal("item.techreborn.part.destructoPack.name");
		fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2, 5, 4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory"), 8, this.ySize - 96 + 2,
			4210752);
		super.drawGuiContainerForegroundLayer(arg0, arg1);
	}
}
