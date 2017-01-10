package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.tiles.generator.TileGenerator;

public class GuiGenerator extends GuiBase {

	TileGenerator tile;

	public GuiGenerator(final EntityPlayer player, final TileGenerator tile) {
		super(player, tile, tile.createContainer(player));
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(8, 72, layer);

		this.drawSlot(80, 54, layer);

		this.builder.drawJEIButton(this, 150, 4, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		this.builder.drawBurnBar(this, this.tile.getScaledBurnTime(100), 100, 81, 38, mouseX, mouseY, layer);
		this.builder.drawMultiEnergyBar(this, 9, 18, (int) this.tile.getEnergy(), (int) this.tile.getMaxPower(), mouseX, mouseY, 0, layer);
	}
}
