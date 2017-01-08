package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileCentrifuge;

public class GuiCentrifuge extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_centrifuge.png");

	TileCentrifuge centrifuge;

	public GuiCentrifuge(final EntityPlayer player, final TileCentrifuge tileCentrifuge) {
		super(new ContainerBuilder("centrifuge").player(player.inventory).inventory(8, 84).hotbar(8, 142).addInventory()
				.tile(tileCentrifuge).slot(0, 80, 35).slot(1, 50, 5).outputSlot(2, 80, 5).outputSlot(3, 110, 35)
				.outputSlot(4, 80, 65).outputSlot(5, 50, 35).energySlot(6, 8, 51).upgradeSlot(7, 152, 8)
				.upgradeSlot(8, 152, 26).upgradeSlot(9, 152, 44).upgradeSlot(10, 152, 62).syncEnergyValue()
				.syncCrafterValue().addInventory().create());
		this.xSize = 176;
		this.ySize = 167;
		this.centrifuge = tileCentrifuge;
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
		this.mc.getTextureManager().bindTexture(GuiCentrifuge.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = this.centrifuge.getProgressScaled(11);
		if (j > 0) {
			this.drawTexturedModalRect(k + 83, l + 23 + 10 - j, 177, 15 + 10 - j, 10, j);
			this.drawTexturedModalRect(k + 98, l + 38, 177, 51, j, 10);
			this.drawTexturedModalRect(k + 83, l + 53, 177, 39, 10, j);
			this.drawTexturedModalRect(k + 68 + 10 - j, l + 38, 177 + 10 - j, 27, j, 10);
		}

		j = this.centrifuge.getEnergyScaled(12);

		if (j > 0) {
			this.drawTexturedModalRect(k + 9, l + 32 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String namePt1 = I18n.translateToLocal("tile.techreborn.industrialBlock.name");
		final String namePt2 = I18n.translateToLocal("tile.techreborn.centrifuge.name").replace(namePt1 + " ", "");
		this.fontRendererObj.drawString(namePt1, 98, 6, 4210752);
		this.fontRendererObj.drawString(namePt2, 98, 14, 4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
		this.fontRendererObj.drawString(this.centrifuge.getProgressScaled(100) + "%", 98, this.ySize - 96 + 2, 4210752);
	}
}
