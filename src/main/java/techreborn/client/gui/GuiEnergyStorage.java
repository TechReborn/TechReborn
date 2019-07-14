package techreborn.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import reborncore.api.power.EnumPowerTier;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.gui.builder.GuiBase;
import reborncore.common.network.NetworkManager;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.packets.PacketRedstoneMode;
import techreborn.tiles.storage.TileEnergyStorage;

import java.io.IOException;

public class GuiEnergyStorage<T extends TileEnergyStorage> extends GuiBase {
	// Fields >>
	T tile;
	// << Fields

	public GuiEnergyStorage(final EntityPlayer player, final T tile) {
		super(player, tile, ((IContainerProvider) tile).createContainer(player));

		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		drawSlot(62, 45, layer);
		drawSlot(98, 45, layer);

		if (tile.tier != EnumPowerTier.MICRO && tile.tier != EnumPowerTier.LOW) drawArmourSlots(8, 18, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		if(GuiBase.slotConfigType == SlotConfigType.NONE){
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.6, 0.6, 5);
			drawCentredString(PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) tile.getEnergy()) + "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) tile.getMaxPower()) + " " + PowerSystem.getDisplayPower().abbreviation, 35, 0, 58, layer);
			GlStateManager.popMatrix();
		}

		builder.drawMultiEnergyBar(this, 81, 28, (int) tile.getEnergy(), (int) tile.getMaxPower(), mouseX, mouseY, 0, layer);
		builder.drawEnergyStorageRedstoneModeButton(this, 150, 5, mouseX, mouseY, layer, tile.redstoneMode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (isPointInRect(150, 5, 20, 20, mouseX, mouseY)) {
			byte currentMode = tile.redstoneMode;
			NetworkManager.sendToServer(new PacketRedstoneMode(tile, (++currentMode >= TileEnergyStorage.redstoneModes ? 0 : currentMode)));
			return;
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
