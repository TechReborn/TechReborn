package techreborn.api.armour;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public interface IArmourUpgrade {

	/**
	 * @return a unique registry name for this uprgade
	 */
	public ResourceLocation getName();

	/**
	 * @return the armourStack slots that this upgradeStack is valid for, default all
	 */
	public default List<ArmourSlot> getValidSlots() {
		return Arrays.asList(ArmourSlot.values());
	}

	public default void tick(UpgradeHolder holder, EntityPlayer player) {

	}

	/**
	 * Used to get data storage for the upgradeStack
	 */
	public default NBTTagCompound getUpragdeData(UpgradeHolder holder) {
		ItemStack stack = holder.getUpgradeStack();
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		if (!stack.getTagCompound().hasKey("upgrade_" + getName())) {
			stack.getTagCompound().setTag("upgrade_" + getName(), new NBTTagCompound());
		}
		return stack.getTagCompound().getCompoundTag("upgrade_" + getName());
	}

}
