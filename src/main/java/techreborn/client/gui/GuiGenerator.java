package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import reborncore.common.powerSystem.PowerSystem;

import techreborn.tiles.generator.TileGenerator;

public class GuiGenerator extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/generator.png");

	TileGenerator generator;

	public GuiGenerator(final EntityPlayer player, final TileGenerator generator) {
		super(generator.createContainer(player));
		this.xSize = 176;
		this.ySize = 167;
		this.generator = generator;
	}

	@Override
	public void initGui() {
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		this.mc.getTextureManager().bindTexture(GuiGenerator.texture);
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = this.generator.getEnergyScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 109, l + 21 + 12, 176, 0, j + 1, 16);
		}

		if (this.generator.burnTime != 0) {
			j = this.generator.getScaledBurnTime(13);
			this.drawTexturedModalRect(k + 80, l + 38 + 12 - j, 176, 30 - j, 14, j + 1);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		final String name = I18n.translateToLocal("tile.techreborn.generator.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
				4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
				this.ySize - 96 + 2, 4210752);

		this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(this.generator.getEnergy()), 25,
				this.ySize - 150, 4210752);
	}
}
