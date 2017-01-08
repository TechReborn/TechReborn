package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.tiles.TileCentrifuge;

public class GuiCentrifuge extends GuiBase {

	TileCentrifuge tile;

	public GuiCentrifuge(final EntityPlayer player, final TileCentrifuge tile) {
		super(player, tile, tile.createContainer(player));
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(8, 72, layer);

		this.drawSlot(40, 34, layer);
		this.drawSlot(40, 54, layer);

		this.drawSlot(82, 44, layer);
		this.drawSlot(101, 25, layer);
		this.drawSlot(120, 44, layer);
		this.drawSlot(101, 63, layer);

		this.builder.drawJEIButton(this, 150, 4, mouseX, mouseY, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		this.builder.drawProgressBar(this, this.tile.getProgressScaled(100), 100, 61, 47, mouseX, mouseY, TRBuilder.ProgressDirection.RIGHT, layer);
		this.builder.drawMultiEnergyBar(this, 9, 18, (int) this.tile.getEnergy(), (int) this.tile.getMaxPower(), mouseX, mouseY, 0, layer);
	}
}
