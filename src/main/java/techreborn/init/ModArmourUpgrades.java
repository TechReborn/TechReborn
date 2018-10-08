package techreborn.init;

import reborncore.RebornRegistry;
import techreborn.api.armour.IArmourUpgrade;
import techreborn.items.armor.modular.upgrades.ItemArmourUpgrade;
import techreborn.items.armor.modular.upgrades.TestUpgrade;

import java.util.Arrays;

public enum  ModArmourUpgrades {
	TEST(new TestUpgrade());

	IArmourUpgrade upgrade;
	ItemArmourUpgrade item;

	ModArmourUpgrades(IArmourUpgrade upgrade) {
		this.upgrade = upgrade;
		item = new ItemArmourUpgrade(upgrade);
	}

	public static void init(){
		Arrays.stream(values()).forEach(upgrade -> {
			upgrade.item.setUnlocalizedName(upgrade.upgrade.getName().toString());
			upgrade.item.setRegistryName(upgrade.upgrade.getName());
			RebornRegistry.registerItem(upgrade.item);
		});
	}
}
