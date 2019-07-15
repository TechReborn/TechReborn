package techreborn.items;

import net.minecraft.item.ItemStack;

/**
 * Interface for coils that can be clicked into the blast furnace
 */
public interface IBlastFurnaceCoil {
	boolean isValid(ItemStack stack);

	int getHeat(ItemStack stack);
}
