package techreborn.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import techreborn.client.container.ContainerIronFurnace;
import techreborn.tiles.TileIronFurnace;

public class GuiIronFurnace extends GuiContainer {

	public static final ResourceLocation texture = new ResourceLocation("minecraft",
		"textures/gui/container/furnace.png");

	TileIronFurnace furnace;
	ContainerIronFurnace containerGrinder;

	public GuiIronFurnace(EntityPlayer player, TileIronFurnace tilegrinder) {
		super(new ContainerIronFurnace(tilegrinder, player));
		this.xSize = 176;
		this.ySize = 167;
		furnace = tilegrinder;
		containerGrinder = (ContainerIronFurnace) this.inventorySlots;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		int j = 0;

		j = furnace.gaugeProgressScaled(24);
		if (j > 0) {
			this.drawTexturedModalRect(k + 78, l + 35, 176, 14, j + 1, 16);
		}

		j = furnace.gaugeFuelScaled(12);
		if (j > 0) {
			this.drawTexturedModalRect(k + 57, l + 36 + 12 - j, 176, 12 - j, 14, j + 2);
		}
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String name = I18n.translateToLocal("tile.techreborn.ironfurnace.name");
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6,
			4210752);
		this.fontRendererObj.drawString(I18n.translateToLocalFormatted("container.inventory", new Object[0]), 8,
			this.ySize - 96 + 2, 4210752);
	}

}
