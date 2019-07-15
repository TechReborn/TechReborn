package techreborn.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackUtils {
	/**
	 * Get or create an ItemStack NBT data
	 *
	 * @param stack the itemstack
	 * @return the existing NBT or a new one if it does not exist
	 */
	public static NBTTagCompound getStackNbtData(ItemStack stack) {
		NBTTagCompound ret = stack.getTagCompound();

		if (ret == null) { // does not exist so we need to create it
			ret = new NBTTagCompound();
			stack.setTagCompound(ret);
		}

		return ret;
	}
}
