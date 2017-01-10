package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.tiles.TileQuantumTank;

public class GuiQuantumTank extends GuiContainer {

	private static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/thermal_generator.png");

	TileQuantumTank quantumTank;

	public GuiQuantumTank(final EntityPlayer player, final TileQuantumTank quantumTank) {
		super(quantumTank.createContainer(player));
		this.xSize = 176;
		this.ySize = 167;
		this.quantumTank = quantumTank;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiQuantumTank.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.quantumTank.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
		this.fontRendererObj.drawString("Liquid Amount", 10, 20, 16448255);
		this.fontRendererObj.drawString(this.quantumTank.tank.getFluidAmount() + "", 10, 30, 16448255);
	}
}
