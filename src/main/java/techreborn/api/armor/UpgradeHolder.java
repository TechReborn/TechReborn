package techreborn.api.armor;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;

//TODO think of a better name
public class UpgradeHolder {

	ItemStack upgradeStack;
	IArmorUpgrade upgrade;

	ItemStack armorStack;
	IModularArmorManager armorManager;

	public UpgradeHolder(ItemStack upgradeStack, IModularArmorManager armorManager) {
		Validate.isTrue(ModularArmorUtils.isUprgade(upgradeStack));

		this.upgradeStack = upgradeStack;
		this.upgrade = ModularArmorUtils.getArmorUprgade(upgradeStack);
		this.armorManager = armorManager;
		this.armorStack = armorManager.getArmorStack();
	}

	public ItemStack getUpgradeStack() {
		return upgradeStack;
	}

	public IArmorUpgrade getUpgrade() {
		return upgrade;
	}

	public ItemStack getArmorStack() {
		return armorStack;
	}

	public IModularArmorManager getArmorManager() {
		return armorManager;
	}
}
