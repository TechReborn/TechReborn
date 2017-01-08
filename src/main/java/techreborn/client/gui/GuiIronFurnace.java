package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileIronFurnace;

public class GuiIronFurnace extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("minecraft",
			"textures/gui/container/furnace.png");

	TileIronFurnace furnace;

	public GuiIronFurnace(final EntityPlayer player, final TileIronFurnace furnace) {
		super(new ContainerBuilder("ironfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(furnace).slot(0, 56, 17).outputSlot(1, 116, 34).fuelSlot(2, 56, 53)
				.syncIntegerValue(furnace::getBurnTime, furnace::setBurnTime)
				.syncIntegerValue(furnace::getTotalBurnTime, furnace::setTotalBurnTime).addInventory()
				.create());
		this.xSize = 176;
		this.ySize = 167;
		this.furnace = furnace;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiIronFurnace.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = this.furnace.gaugeProgressScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 78, l + 35, 176, 14, j + 1, 16);
		}

		j = this.furnace.gaugeFuelScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 57, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.ironfurnace.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}

}
