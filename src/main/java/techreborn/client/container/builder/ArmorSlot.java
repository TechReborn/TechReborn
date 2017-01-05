package techreborn.client.container.builder;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

import techreborn.lib.ModInfo;

public class ArmorSlot extends FilteredSlot {

	public ArmorSlot(final EntityEquipmentSlot armorType, final InventoryPlayer inventory, final int index,
			final int xPosition, final int yPosition) {

		super(inventory, index, xPosition, yPosition);

		this.setFilter(stack -> stack != null && stack.getItem().isValidArmor(stack, armorType, inventory.player));

		this.setBackgroundLocation(
				new ResourceLocation(ModInfo.MOD_ID, "textures/gui/armor_" + armorType.getName() + ".png"));
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
