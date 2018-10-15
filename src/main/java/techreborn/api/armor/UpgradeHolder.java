package techreborn.api.armor;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

//TODO think of a better name
@ParametersAreNonnullByDefault
public class UpgradeHolder {

	@Nonnull
	ItemStack upgradeStack;
	@Nonnull
	IArmorUpgrade upgrade;

	@Nonnull
	ItemStack armorStack;
	@Nonnull
	IModularArmorManager armorManager;

	public UpgradeHolder(ItemStack upgradeStack, IModularArmorManager armorManager) {
		Validate.notNull(armorManager);
		Validate.notNull(upgradeStack);
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
