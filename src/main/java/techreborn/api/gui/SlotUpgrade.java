package techreborn.api.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import techreborn.utils.upgrade.IMachineUpgrade;

public class SlotUpgrade extends Slot {

    public SlotUpgrade(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof IMachineUpgrade;
    }
}
