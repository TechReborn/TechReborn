package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileQuantumChest;

public class GuiQuantumChest extends GuiBase {

	TileQuantumChest tile;

	public GuiQuantumChest(final EntityPlayer player, final TileQuantumChest tile) {
		super(player, tile, new ContainerBuilder("quantumchest").player(player.inventory).inventory().hotbar().addInventory().tile(tile).slot(0, 80, 24).outputSlot(1, 80, 64).addInventory().create());
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		Layer layer = Layer.BACKGROUND;

		this.drawSlot(80, 24, layer);
		this.drawSlot(80, 64, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		Layer layer = Layer.FOREGROUND;

		if (this.tile.storedItem != ItemStack.EMPTY && this.tile.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.tile.storedItem.getCount() + this.tile.getStackInSlot(1).getCount(), this.tile.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
		if (this.tile.storedItem == ItemStack.EMPTY && this.tile.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.tile.getStackInSlot(1).getCount(), this.tile.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
	}
}
