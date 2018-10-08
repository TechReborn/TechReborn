package techreborn.api.armour;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;

//TODO think of a better name
public class UpgradeHolder {

	ItemStack upgradeStack;
	IArmourUpgrade upgrade;

	ItemStack armourStack;
	IModularArmourManager armourManager;

	public UpgradeHolder(ItemStack upgradeStack, IModularArmourManager armourManager) {
		Validate.isTrue(ModularArmourUtils.isUprgade(upgradeStack));

		this.upgradeStack = upgradeStack;
		this.upgrade = ModularArmourUtils.getArmourUprgade(upgradeStack);
		this.armourManager = armourManager;
		this.armourStack = armourManager.getArmourStack();
	}

	public ItemStack getUpgradeStack() {
		return upgradeStack;
	}

	public IArmourUpgrade getUpgrade() {
		return upgrade;
	}

	public ItemStack getArmourStack() {
		return armourStack;
	}

	public IModularArmourManager getArmourManager() {
		return armourManager;
	}
}
