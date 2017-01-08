package techreborn.client.container.builder.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import techreborn.lib.ModInfo;

public class SpriteSlot extends FilteredSlot {

	int stacksize;

	public SpriteSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition, final String sprite, final int stacksize) {
		super(inventory, index, xPosition, yPosition);
		this.setBackgroundLocation(
				new ResourceLocation(ModInfo.MOD_ID + ":textures/gui/slot_sprites/" + sprite + ".png"));
		this.stacksize = stacksize;
	}

	public SpriteSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition, final String sprite) {
		this(inventory, index, xPosition, yPosition, sprite, 64);
	}

	@Override
	public int getSlotStackLimit() {
		return this.stacksize;
	}
}
