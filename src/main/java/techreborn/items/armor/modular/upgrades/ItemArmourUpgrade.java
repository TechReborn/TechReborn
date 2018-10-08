package techreborn.items.armor.modular.upgrades;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import techreborn.api.armour.ArmorUpgradeCapabilityProvider;
import techreborn.api.armour.IArmourUpgrade;
import techreborn.items.ItemTR;

import javax.annotation.Nullable;

public class ItemArmourUpgrade extends ItemTR {

	IArmourUpgrade armourUpgrade;

	public ItemArmourUpgrade(IArmourUpgrade armourUpgrade) {
		this.armourUpgrade = armourUpgrade;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack,
	                                            @Nullable
		                                            NBTTagCompound nbt) {
		return ArmorUpgradeCapabilityProvider.getUpgradeProvider(stack, armourUpgrade);
	}
}
