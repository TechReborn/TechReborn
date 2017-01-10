package techreborn.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.tiles.storage.TileBatBox;

public class GuiBatbox extends GuiBase {

	TileBatBox tile;

	public GuiBatbox(final EntityPlayer player, final TileBatBox tile) {
		super(player, tile, tile.createContainer(player));
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(62, 45, layer);
		this.drawSlot(98, 45, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.6, 0.6, 5);
		this.drawCentredString(PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) this.tile.getEnergy()) + "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) this.tile.getMaxPower()) + " " + PowerSystem.getDisplayPower().abbreviation, 35, 0, 58, layer);
		GlStateManager.popMatrix();

		this.builder.drawMultiEnergyBar(this, 81, 28, (int) this.tile.getEnergy(), (int) this.tile.getMaxPower(), mouseX, mouseY, 0, layer);
	}
}
