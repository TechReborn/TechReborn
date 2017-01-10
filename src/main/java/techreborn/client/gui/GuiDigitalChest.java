package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import techreborn.tiles.TileDigitalChest;

public class GuiDigitalChest extends GuiBase {

	TileDigitalChest digitalChest;

	public GuiDigitalChest(final EntityPlayer player, final TileDigitalChest digitalChest) {
		super(player, digitalChest, digitalChest.createContainer(player));
		this.digitalChest = digitalChest;
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

		if (this.digitalChest.storedItem != ItemStack.EMPTY && this.digitalChest.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43,
					this.digitalChest.storedItem.getCount() + this.digitalChest.getStackInSlot(1).getCount(),
					this.digitalChest.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
		if (this.digitalChest.storedItem == ItemStack.EMPTY && this.digitalChest.getStackInSlot(1) != null) {
			this.builder.drawBigBlueBar(this, 31, 43, this.digitalChest.getStackInSlot(1).getCount(),
					this.digitalChest.maxCapacity, mouseX - this.guiLeft, mouseY - this.guiTop, "Stored", layer);
		}
	}
}
