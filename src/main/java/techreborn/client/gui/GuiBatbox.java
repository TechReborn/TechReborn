package techreborn.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.client.gui.widget.GuiButtonPowerBar;
import techreborn.tiles.storage.TileBatBox;

public class GuiBatbox extends GuiBase {

	TileBatBox tile;

	public GuiBatbox(final EntityPlayer player, final TileBatBox tile) {
		super(player, tile, new ContainerBuilder("batbox").player(player.inventory).inventory(8, 84).hotbar(8, 142).addInventory().tile(tile).energySlot(0, 62, 45).energySlot(1, 98, 45).syncEnergyValue().addInventory().create());
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);

		this.buttonList.clear();
		drawSlotBackground(62, 45);
		drawSlotBackground(98, 45);
		this.buttonList.add(new GuiButtonPowerBar(0, guiLeft + 82, guiTop + 29));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		builder.drawMultiEnergyBar(this, 81, 28, (int) tile.getEnergy(), (int) tile.getMaxPower(), mouseX - guiLeft, mouseY - guiTop);
		GlStateManager.scale(0.6, 0.6, 5);
		drawCentredString(PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) (tile.getEnergy())) + "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) (tile.getMaxPower())) + " " + PowerSystem.getDisplayPower().abbreviation, 35, 0, 58);
		GlStateManager.scale(1, 1, 1);
	}
}
