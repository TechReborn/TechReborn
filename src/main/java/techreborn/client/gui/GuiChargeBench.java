package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.tiles.TileChargeBench;

public class GuiChargeBench extends GuiBase {

	TileChargeBench tile;

	public GuiChargeBench(final EntityPlayer player, final TileChargeBench tile) {
		super(player, tile, tile.createContainer(player));
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(62, 25, layer);
		this.drawSlot(98, 25, layer);
		this.drawSlot(62, 45, layer);
		this.drawSlot(98, 45, layer);
		this.drawSlot(62, 65, layer);
		this.drawSlot(98, 65, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		this.builder.drawMultiEnergyBar(this, 81, 28, (int) this.tile.getEnergy(), (int) this.tile.getMaxPower(), mouseX, mouseY, 0, layer);
	}
}
