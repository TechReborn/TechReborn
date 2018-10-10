package techreborn.init;

import net.minecraft.item.Item;
import reborncore.RebornRegistry;
import techreborn.api.armor.IArmorUpgrade;
import techreborn.items.armor.modular.upgrades.*;

import java.util.Arrays;

public enum  ModArmorUpgrades {
	TEST(new TestUpgrade()),
	PROTECTION(new ProtectionUprgade(), ModItems.PLATES),
	FLIGHT(new FlightUpgrade()),
	BREATHING(new BreathingUpgrade());

	IArmorUpgrade upgrade;
	ItemArmorUpgrade item;

	ModArmorUpgrades(IArmorUpgrade upgrade) {
		this.upgrade = upgrade;
		item = new ItemArmorUpgrade(upgrade);
	}

	//This is a simple way to attach uprgades to other items
	ModArmorUpgrades(IArmorUpgrade upgrade, Item item){
		this.upgrade = upgrade;
		ModCapabilities.upgradeMap.put(item, upgrade);
	}

	public boolean hasItem(){
		return item != null;
	}

	public static void init(){
		Arrays.stream(values())
			.filter(ModArmorUpgrades::hasItem)
			.forEach(upgrade -> {
			upgrade.item.setUnlocalizedName(upgrade.upgrade.getName().toString());
			upgrade.item.setRegistryName(upgrade.upgrade.getName());
			RebornRegistry.registerItem(upgrade.item);
		});
	}
}
