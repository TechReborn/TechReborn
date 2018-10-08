package techreborn.items.armor.modular.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import techreborn.api.armor.ArmorUpgradeCapabilityProvider;
import techreborn.api.armor.IArmorUpgrade;
import techreborn.items.ItemTR;

import javax.annotation.Nullable;

public class ItemArmorUpgrade extends ItemTR {

	IArmorUpgrade armorUpgrade;

	public ItemArmorUpgrade(IArmorUpgrade armorUpgrade) {
		this.armorUpgrade = armorUpgrade;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            NBTTagCompound nbt) {
		return ArmorUpgradeCapabilityProvider.getUpgradeProvider(stack, armorUpgrade);
	}
}
