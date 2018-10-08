package techreborn.api.armour;

import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.Arrays;

public enum ArmourSlot {
	HEAD(EntityEquipmentSlot.HEAD),
	CHEST(EntityEquipmentSlot.CHEST),
	LEGS(EntityEquipmentSlot.LEGS),
	FEET(EntityEquipmentSlot.FEET);

	EntityEquipmentSlot entityEquipmentSlot;

	ArmourSlot(EntityEquipmentSlot entityEquipmentSlot) {
		this.entityEquipmentSlot = entityEquipmentSlot;
	}

	public EntityEquipmentSlot getEntityEquipmentSlot() {
		return entityEquipmentSlot;
	}

	public static ArmourSlot fromEntityEquipmentSlot(EntityEquipmentSlot entityEquipmentSlot) {
		return Arrays.stream(values())
			.filter(armourSlot -> armourSlot.entityEquipmentSlot == entityEquipmentSlot)
			.findFirst()
			.orElse(null);
	}
}
