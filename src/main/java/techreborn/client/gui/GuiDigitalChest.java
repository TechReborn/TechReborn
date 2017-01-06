package techreborn.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.TileDigitalChest;

public class GuiDigitalChest extends GuiBase {

	TileDigitalChest tile;

	public GuiDigitalChest(final EntityPlayer player, final TileDigitalChest tile) {
		super(player, tile, new ContainerBuilder().player(player.inventory).inventory().hotbar().addInventory().tile(tile).slot(0, 80, 20).output(1, 80, 70).fake(2, 80, 45).addInventory().create());
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);

		drawSlotBackground(80, 20);
		drawSelectedStackBackground(80, 45);
		drawSlotBackground(80, 70);
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.fontRendererObj.drawString("Amount", 105, 43, 0);
		if (tile.storedItem != ItemStack.EMPTY && tile.getStackInSlot(1) != null) {
			drawString(tile.storedItem.getCount() + tile.getStackInSlot(1).getCount() + "", 105, 53, 0);
		}
		if (tile.storedItem == ItemStack.EMPTY && tile.getStackInSlot(1) != null)
			drawString(tile.getStackInSlot(1).getCount() + "", 105, 53, 0);
	}
}
