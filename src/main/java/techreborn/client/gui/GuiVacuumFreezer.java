package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.tiles.multiblock.TileVacuumFreezer;

public class GuiVacuumFreezer extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/vacuum_freezer.png");

	TileVacuumFreezer freezer;

	public GuiVacuumFreezer(final EntityPlayer player, final TileVacuumFreezer freezer) {
		super(freezer.createContainer(player));
		this.xSize = 176;
		this.ySize = 167;
		this.freezer = freezer;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiVacuumFreezer.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = this.freezer.getProgressScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 80, l + 37, 176, 14, j + 1, 16);
		}

		j = (int) (this.freezer.getEnergy() * 12f / this.freezer.getMaxPower());
		if (j > 0) {
			this.drawTexturedModalRect(k + 26, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}

		if (!this.freezer.getMultiBlock()) {
			this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38, l + 52 + 12, -1);
		}

		j = this.freezer.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 26, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		final String name = I18n.translateToLocal("tile.techreborn.vacuumfreezer.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

}
