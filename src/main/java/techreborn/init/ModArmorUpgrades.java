package techreborn.init;

import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import reborncore.RebornRegistry;
import techreborn.api.armor.ArmorSlot;
import techreborn.api.armor.IArmorUpgrade;
import techreborn.items.armor.modular.upgrades.*;

import java.util.Arrays;
import java.util.Collections;

public enum ModArmorUpgrades {
	TEST(new TestUpgrade()),
	PROTECTION(new ProtectionUprgade("general", damageSource -> {
		if (!damageSource.isUnblockable()) {
			return 0d;
		}
		return 1.5D;
	})),
	HEAT_PROTECTION(new ProtectionUprgade("heat", damageSource -> {
		if (!damageSource.isUnblockable() || !damageSource.isFireDamage()) {
			return 0d;
		}
		return 2D;
	})),
	BLAST_PROTECTION(new ProtectionUprgade("blast", damageSource -> {
		if (damageSource.getDamageType().contains("explosion")) {
			return 3d;
		}
		return 0D;
	})),
	PROJECTILE_PROTECTION(new ProtectionUprgade("projectile", damageSource -> {
		if (damageSource.isProjectile()) {
			return 3d;
		}
		return 0D;
	})),
	MAGIC_PROTECTION(new ProtectionUprgade("magic", damageSource -> {
		if (damageSource.isMagicDamage()) {
			return 3d;
		}
		return 0D;
	})),
	FALL_PROTECTION(new ProtectionUprgade("fall", damageSource -> {
		if (damageSource == DamageSource.FALL) {
			return 4d;
		}
		return 0D;
	}, Collections.singletonList(ArmorSlot.FEET))),
	FLIGHT(new FlightUpgrade()),
	BREATHING(new BreathingUpgrade()),
	NIGHT_VISION(new NightVisionUpgrade());

	IArmorUpgrade upgrade;
	ItemArmorUpgrade item;

	ModArmorUpgrades(IArmorUpgrade upgrade) {
		this.upgrade = upgrade;
		item = new ItemArmorUpgrade(upgrade);
	}

	//This is a simple way to attach uprgades to other items
	ModArmorUpgrades(IArmorUpgrade upgrade, Item item) {
		this.upgrade = upgrade;
		ModCapabilities.upgradeMap.put(item, upgrade);
	}

	public boolean hasItem() {
		return item != null;
	}

	public static void init() {
		Arrays.stream(values())
			.filter(ModArmorUpgrades::hasItem)
			.forEach(upgrade -> {
				upgrade.item.setUnlocalizedName(upgrade.upgrade.getName().toString());
				upgrade.item.setRegistryName(upgrade.upgrade.getName());
				RebornRegistry.registerItem(upgrade.item);
			});
	}
}
