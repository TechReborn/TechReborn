package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.tiles.TileChemicalReactor;

public class GuiChemicalReactor extends GuiBase {

	TileChemicalReactor tile;

	public GuiChemicalReactor(final EntityPlayer player, final TileChemicalReactor tile) {
		super(player, tile, tile.createContainer(player));
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		this.drawSlot(8, 72, layer);

		this.drawSlot(34, 47, layer);
		this.drawSlot(126, 47, layer);
		this.drawOutputSlot(80, 47, layer);

		this.builder.drawJEIButton(this, 150, 4, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		this.builder.drawProgressBar(this, this.tile.getProgressScaled(100), 100, 55, 51, mouseX, mouseY, TRBuilder.ProgressDirection.RIGHT, layer);
		this.builder.drawProgressBar(this, this.tile.getProgressScaled(100), 100, 105, 51, mouseX, mouseY, TRBuilder.ProgressDirection.LEFT, layer);
		this.builder.drawMultiEnergyBar(this, 9, 18, (int) this.tile.getEnergy(), (int) this.tile.getMaxPower(), mouseX, mouseY, 0, layer);
	}
}
