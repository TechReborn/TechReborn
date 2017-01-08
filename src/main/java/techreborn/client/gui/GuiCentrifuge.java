package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileCentrifuge;

public class GuiCentrifuge extends GuiBase {

	TileCentrifuge tile;

	public GuiCentrifuge(final EntityPlayer player, final TileCentrifuge tile) {
		super(player, tile, new ContainerBuilder("centrifuge").player(player.inventory).inventory().hotbar().addInventory()
			.tile(tile).slot(0, 40, 34).slot(1, 40, 54).outputSlot(2, 82, 44).outputSlot(3, 101, 25)
			.outputSlot(4, 120, 44).outputSlot(5, 101, 63).energySlot(6, 8, 72).syncEnergyValue()
			.syncCrafterValue().addInventory().create());
		this.tile = tile;
	}

	@Override
	public void initGui() {
		final int k = (this.width - this.xSize) / 2;
		final int l = (this.height - this.ySize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		Layer layer = Layer.BACKGROUND;

		drawSlot(8, 72, layer);

		drawSlot(40, 34, layer);
		drawSlot(40, 54, layer);

		drawSlot(82, 44, layer);
		drawSlot(101, 25, layer);
		drawSlot(120, 44, layer);
		drawSlot(101, 63, layer);

		builder.drawJEIButton(this, 150, 4, mouseX, mouseY, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		Layer layer = Layer.FOREGROUND;

		builder.drawProgressBar(this, tile.getProgressScaled(100), 100, 61, 47, mouseX, mouseY, TRBuilder.ProgressDirection.RIGHT, layer);
		builder.drawMultiEnergyBar(this, 9, 18, (int) tile.getEnergy(), (int) tile.getMaxPower(), mouseX, mouseY, 0, layer);
	}
}
