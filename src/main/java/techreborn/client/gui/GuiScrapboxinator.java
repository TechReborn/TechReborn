package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModItems;
import techreborn.tiles.TileScrapboxinator;

public class GuiScrapboxinator extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/scrapboxinator.png");

	TileScrapboxinator tile;

	public GuiScrapboxinator(final EntityPlayer player, final TileScrapboxinator scrapboxinator) {
		super(new ContainerBuilder("scrapboxinator").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(scrapboxinator)
				.filterSlot(0, 56, 34, stack -> stack.getItem() == ModItems.SCRAP_BOX).outputSlot(1, 116, 34)
				.upgradeSlot(2, 152, 8).upgradeSlot(3, 152, 26).upgradeSlot(4, 152, 44).upgradeSlot(5, 152, 62)
				.syncEnergyValue().syncIntegerValue(scrapboxinator::getProgress, scrapboxinator::setProgress)
				.addInventory().create());
		this.xSize = 176;
		this.ySize = 167;
		this.tile = scrapboxinator;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiScrapboxinator.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = this.tile.gaugeProgressScaled(24);
		// System.out.println(compressor.gaugeProgressScaled(10));
		if (j > 0) {
			this.drawTexturedModalRect(k + 78, l + 35, 176, 14, j + 1, 16);
		}

		j = this.tile.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 24, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = net.minecraft.util.text.translation.I18n.translateToLocal("tile.techreborn.scrapboxinator.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2,
				4210752);
	}
}
