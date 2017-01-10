package techreborn.client.container.builder.slot;

import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import techreborn.lib.ModInfo;

public class SpriteSlot extends FilteredSlot {

	private final String sprite;
	int stacksize;

	public SpriteSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition, final String sprite, final int stacksize) {
		super(inventory, index, xPosition, yPosition);
		this.sprite = ModInfo.MOD_ID + ":textures/gui/slot_sprites/" + sprite;
		this.stacksize = stacksize;
	}

	public SpriteSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition, final String sprite) {
		this(inventory, index, xPosition, yPosition, sprite, 64);
	}

	@Override
	public int getSlotStackLimit() {
		return this.stacksize;
	}

	@Override
	@Nullable
	@SideOnly(Side.CLIENT)
	public String getSlotTexture() {
		return this.sprite;
	}
}
