package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.tiles.teir1.TileRecycler;

public class GuiRecycler extends GuiBase {

	TileRecycler tile;

	public GuiRecycler(final EntityPlayer player, final TileRecycler tile) {
		super(player, tile, tile.createContainer(player));
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.BACKGROUND;

		//this.drawSlot(8, 72, layer);

		this.drawSlot(55, 45, layer);
		this.drawOutputSlot(101, 45, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final GuiBase.Layer layer = GuiBase.Layer.FOREGROUND;

		this.builder.drawProgressBar(this, this.tile.gaugeProgressScaled(100), 100, 76, 48, mouseX, mouseY, TRBuilder.ProgressDirection.RIGHT, layer);
		this.builder.drawMultiEnergyBar(this, 9, 18, (int) this.tile.getEnergy(), (int) this.tile.getMaxPower(), mouseX, mouseY, 0, layer);
	}
}
