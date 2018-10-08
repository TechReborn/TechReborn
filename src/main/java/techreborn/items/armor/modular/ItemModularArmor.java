package techreborn.items.armor.modular;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import techreborn.api.armor.ArmorSlot;
import techreborn.items.armor.ItemTRArmor;

import javax.annotation.Nullable;

public class ItemModularArmor extends ItemTRArmor {

	public ItemModularArmor(ArmorMaterial material, ArmorSlot slot) {
		super(material, slot.getEntityEquipmentSlot());
	}

	public ModularArmorManager getManager(ItemStack stack) {
		return new ModularArmorManager(stack);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            NBTTagCompound nbt) {
		return getManager(stack);
	}
}
