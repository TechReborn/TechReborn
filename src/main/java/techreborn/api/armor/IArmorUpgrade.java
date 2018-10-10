package techreborn.api.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Arrays;
import java.util.List;

public interface IArmorUpgrade {

	/**
	 * @return a unique registry name for this uprgade
	 */
	public ResourceLocation getName();

	/**
	 * @return the armorStack slots that this upgradeStack is valid for, default all
	 */
	public default List<ArmorSlot> getValidSlots() {
		return Arrays.asList(ArmorSlot.values());
	}

	public default void tick(UpgradeHolder holder, EntityPlayer player) {

	}

	/**
	 *
	 * A method to handle LivingHurtEvent
	 *
	 * @return true to canel the event
	 */
	public default boolean hurt(UpgradeHolder holder, LivingHurtEvent event) {
		return false;
	}

	public default double getSpeed(UpgradeHolder holder){
		return 0F;
	}

	/**
	 * Add tool tips to the armor item
	 * @param list
	 */
	public default void tooltip(UpgradeHolder holder, List<String> list){

	}

	public default Multimap<String, AttributeModifier> getAttributeModifiers(UpgradeHolder holder){
		return HashMultimap.create();
	}

	//Called when the uprgade is added to the armor stack
	public default void onAdded(ItemStack stack, TileEntity workstation){

	}

	//Called when the uprgade is removed from the armor stack
	public default void onRemoved(ItemStack stack, TileEntity workstation){

	}

	public default int getPowerStorage(UpgradeHolder holder){
		return 0;
	}

	public default boolean allowMultiple(){
		return true;
	}

	public default void equip(UpgradeHolder holder, EntityPlayer player){

	}

	public default void unequip(UpgradeHolder holder, EntityPlayer player){

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
