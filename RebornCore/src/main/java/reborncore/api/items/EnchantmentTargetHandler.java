package reborncore.api.items;

import net.minecraft.enchantment.EnchantmentTarget;

public interface EnchantmentTargetHandler {
	/**
	 * Allows to modify conditions for Enchantment application
	 *
	 * @param target Enchantment target to check
	 * @return True if proper target provided
	 */
	boolean modifyEnchantmentApplication(EnchantmentTarget target);
}
