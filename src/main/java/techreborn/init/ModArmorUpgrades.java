package techreborn.init;

import reborncore.RebornRegistry;
import techreborn.api.armor.IArmorUpgrade;
import techreborn.items.armor.modular.upgrades.ItemArmorUpgrade;
import techreborn.items.armor.modular.upgrades.TestUpgrade;

import java.util.Arrays;

public enum  ModArmorUpgrades {
	TEST(new TestUpgrade());

	IArmorUpgrade upgrade;
	ItemArmorUpgrade item;

	ModArmorUpgrades(IArmorUpgrade upgrade) {
		this.upgrade = upgrade;
		item = new ItemArmorUpgrade(upgrade);
	}

	public static void init(){
		Arrays.stream(values()).forEach(upgrade -> {
			upgrade.item.setUnlocalizedName(upgrade.upgrade.getName().toString());
			upgrade.item.setRegistryName(upgrade.upgrade.getName());
			RebornRegistry.registerItem(upgrade.item);
		});
	}
}
