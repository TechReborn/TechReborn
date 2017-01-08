package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import techreborn.tiles.TileQuantumChest;

public class GuiQuantumChest extends GuiBase {

	TileQuantumChest quantumChest;

	public GuiQuantumChest(final EntityPlayer player, final TileQuantumChest quantumChest) {
		super(player, quantumChest, quantumChest.createContainer(player));
		this.quantumChest = quantumChest;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;

		this.drawSlot(80, 24, layer);
		this.drawSlot(80, 64, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;

		if (this.quantumChest.storedItem != ItemStack.EMPTY && this.quantumChest.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.quantumChest.storedItem.getCount() + this.quantumChest.getStackInSlot(1).getCount(), this.quantumChest.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
		if (this.quantumChest.storedItem == ItemStack.EMPTY && this.quantumChest.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.quantumChest.getStackInSlot(1).getCount(), this.quantumChest.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
	}
}
