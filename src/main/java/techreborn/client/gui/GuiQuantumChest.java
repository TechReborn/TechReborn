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
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);

		drawSlotBackground(80, 24);
		drawSlotBackground(80, 64);
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (tile.storedItem != ItemStack.EMPTY && tile.getStackInSlot(1) != null) {
			builder.drawBigBlueBar(this, 31, 43, tile.storedItem.getCount() + tile.getStackInSlot(1).getCount(), tile.maxCapacity, mouseX - guiLeft, mouseY - guiTop, "Stored");
		}
		if (tile.storedItem == ItemStack.EMPTY && tile.getStackInSlot(1) != null) {
			builder.drawBigBlueBar(this, 31, 43, tile.getStackInSlot(1).getCount(), tile.maxCapacity, mouseX - guiLeft, mouseY - guiTop, "Stored");
		}
	}
}
