package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileIndustrialElectrolyzer;

public class GuiIndustrialElectrolyzer extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn",
			"textures/gui/industrial_electrolyzer.png");

	TileIndustrialElectrolyzer eletrolyzer;


	public GuiIndustrialElectrolyzer(final EntityPlayer player, final TileIndustrialElectrolyzer tileeletrolyzer) {
		super(new ContainerBuilder("industrialelectrolyzer").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(tileeletrolyzer).slot(0, 80, 51).slot(1, 50, 51).outputSlot(2, 50, 19)
				.outputSlot(3, 70, 19).outputSlot(4, 90, 19).outputSlot(5, 110, 19).energySlot(6, 18, 51)
				.syncEnergyValue().syncCrafterValue().addInventory().create());
		this.xSize = 176;
		this.ySize = 167;
		this.eletrolyzer = tileeletrolyzer;
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
		this.mc.getTextureManager().bindTexture(GuiIndustrialElectrolyzer.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = this.eletrolyzer.getProgressScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 72, l + 38, 176, 14, j + 1, 16);
		}

		j = this.eletrolyzer.getEnergyScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 134, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.industrialelectrolyzer.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);
	}
}
