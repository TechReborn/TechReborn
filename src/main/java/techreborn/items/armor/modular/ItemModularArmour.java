package techreborn.items.armor.modular;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import techreborn.api.armour.ArmourSlot;
import techreborn.items.armor.ItemTRArmour;

import javax.annotation.Nullable;

public class ItemModularArmour extends ItemTRArmour {

	public ItemModularArmour(ArmorMaterial material, ArmourSlot slot) {
		super(material, slot.getEntityEquipmentSlot());
	}

	public ModularArmourManager getManager(ItemStack stack) {
		return new ModularArmourManager(stack);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            NBTTagCompound nbt) {
		return getManager(stack);
	}
}
