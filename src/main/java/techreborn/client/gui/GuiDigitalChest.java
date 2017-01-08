package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileDigitalChest;

public class GuiDigitalChest extends GuiBase {

	TileDigitalChest tile;

	public GuiDigitalChest(final EntityPlayer player, final TileDigitalChest tile) {
		super(player, tile, new ContainerBuilder("digitalchest").player(player.inventory).inventory().hotbar()
				.addInventory().tile(tile).slot(0, 80, 24).outputSlot(1, 80, 64).addInventory().create());
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float f, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);

		this.drawSlotBackground(80, 24);
		this.drawSlotBackground(80, 64);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (this.tile.storedItem != ItemStack.EMPTY && this.tile.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.tile.storedItem.getCount() + this.tile.getStackInSlot(1).getCount(), this.tile.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored");
		}
		if (this.tile.storedItem == ItemStack.EMPTY && this.tile.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.tile.getStackInSlot(1).getCount(), this.tile.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored");
		}
	}
}
