package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.multiblock.TileImplosionCompressor;

public class GuiImplosionCompressor extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/implosion_compressor.png");

	TileImplosionCompressor compresser;

	public GuiImplosionCompressor(final EntityPlayer player, final TileImplosionCompressor tilecompressor) {
		super(new ContainerBuilder("implosioncompressor").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(tilecompressor).slot(0, 37, 26).slot(1, 37, 44).outputSlot(2, 93, 35)
				.outputSlot(3, 111, 35).syncEnergyValue().syncCrafterValue().addInventory().create());
		this.xSize = 176;
		this.ySize = 167;
		this.compresser = tilecompressor;
	}

	@Override
	public void initGui() {
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiImplosionCompressor.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		if (!this.compresser.getMutliBlock()) {
			// GuiDraw.drawTooltipBox(k + 30, l + 50 + 12 - 0, 114, 10);
			this.fontRendererObj.drawString(I18n.translateToLocal("techreborn.message.missingmultiblock"), k + 38, l + 52 + 12, -1);
		}

		this.mc.getTextureManager().bindTexture(GuiImplosionCompressor.texture);
		int j = this.compresser.getProgressScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 61, l + 37, 176, 14, j + 1, 16);
		}

		j = this.compresser.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 14, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.implosioncompressor.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}

}
