package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.container.ContainerGenerator;
import techreborn.tiles.generator.TileGenerator;

public class GuiGenerator extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("techreborn", "textures/gui/generator.png");

	TileGenerator generator;

	ContainerGenerator containerGenerator;

	public GuiGenerator(EntityPlayer player, TileGenerator generator) {
		super(new ContainerGenerator(generator, player));
		this.xSize = 176;
		this.ySize = 167;
		this.generator = generator;
		this.containerGenerator = (ContainerGenerator) this.inventorySlots;
	}

	@Override
	public void initGui() {
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = generator.getEnergyScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 109, l + 21 + 12, 176, 0, j + 1, 16);
		}

		if (containerGenerator.burnTime != 0) {
			j = containerGenerator.getScaledBurnTime(13);
			this.drawTexturedModalRect(k + 80, l + 38 + 12 - j, 176, 30 - j, 14, j + 1);
		}
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String name = I18n.translateToLocal("tile.techreborn.generator.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
			4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
			this.ySize - 96 + 2, 4210752);

		this.fontRendererObj.drawString(PowerSystem.getLocaliszedPower(containerGenerator.energy), 25, this.ySize - 150,
			4210752);
	}
}
