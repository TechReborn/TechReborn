package techreborn.api.armor;

import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.Arrays;

public enum ArmorSlot {
	HEAD(EntityEquipmentSlot.HEAD),
	CHEST(EntityEquipmentSlot.CHEST),
	LEGS(EntityEquipmentSlot.LEGS),
	FEET(EntityEquipmentSlot.FEET);

	EntityEquipmentSlot entityEquipmentSlot;

	ArmorSlot(EntityEquipmentSlot entityEquipmentSlot) {
		this.entityEquipmentSlot = entityEquipmentSlot;
	}

	public EntityEquipmentSlot getEntityEquipmentSlot() {
		return entityEquipmentSlot;
	}

	public static ArmorSlot fromEntityEquipmentSlot(EntityEquipmentSlot entityEquipmentSlot) {
		return Arrays.stream(values())
			.filter(armorSlot -> armorSlot.entityEquipmentSlot == entityEquipmentSlot)
			.findFirst()
			.orElse(null);
	}
}
