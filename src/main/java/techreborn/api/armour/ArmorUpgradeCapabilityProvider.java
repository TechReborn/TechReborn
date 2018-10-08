package techreborn.api.armour;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArmorUpgradeCapabilityProvider implements ICapabilityProvider {

	public static ICapabilityProvider getUpgradeProvider(ItemStack stack, IArmourUpgrade upgrade) {
		Validate.notNull(upgrade);
		Validate.notNull(stack);
		return new ArmorUpgradeCapabilityProvider(stack, upgrade);
	}

	ItemStack stack;
	IArmourUpgrade armorUpgrade;

	public ArmorUpgradeCapabilityProvider(ItemStack stack, IArmourUpgrade upgrade) {
		this.stack = stack;
		this.armorUpgrade = upgrade;
	}

	@Override
	public boolean hasCapability(
		@Nonnull
			Capability<?> capability,
		@Nullable
			EnumFacing facing) {
		return capability == CapabilityArmourUpgrade.ARMOUR_UPRGRADE_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(
		@Nonnull
			Capability<T> capability,
		@Nullable
			EnumFacing facing) {
		if(capability == CapabilityArmourUpgrade.ARMOUR_UPRGRADE_CAPABILITY){
			return CapabilityArmourUpgrade.ARMOUR_UPRGRADE_CAPABILITY.cast(armorUpgrade);
		}
		return null;
	}
}
