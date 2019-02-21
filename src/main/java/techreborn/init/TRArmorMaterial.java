package techreborn.init;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;


/* //TODO move values over
	public static ArmorMaterial BRONZE_ARMOUR = EnumHelper.addEnum(ArmorMaterial.class, "BRONZE", ARMOR_PARAMETERS, "techreborn:bronze", 17, new int[] { 3, 6, 5,
		2 }, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial RUBY_ARMOUR = EnumHelper.addEnum(ArmorMaterial.class, "RUBY", ARMOR_PARAMETERS, "techreborn:ruby", 16, new int[] { 2, 7, 5,
		2 }, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial SAPPHIRE_ARMOUR = EnumHelper.addEnum(ArmorMaterial.class, "SAPPHIRE", ARMOR_PARAMETERS, "techreborn:sapphire", 19, new int[] { 4, 4, 4,
		4 }, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial PERIDOT_ARMOUR = EnumHelper.addEnum(ArmorMaterial.class, "PERIDOT", ARMOR_PARAMETERS, "techreborn:peridot", 17, new int[] { 3, 8, 3,
		2 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F);
	public static ArmorMaterial CLOAKING_ARMOR = EnumHelper.addEnum(ArmorMaterial.class, "CLOAKING", ARMOR_PARAMETERS, "techreborn:cloaking", 5, new int[] { 1, 2, 3,
			1 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F);

 */
public enum  TRArmorMaterial implements IArmorMaterial {

	BRONZE,
	RUBY,
	SAPPHIRE,
	PERIDOT,
	CLOAKING;

	@Override
	public int getDurability(EntityEquipmentSlot entityEquipmentSlot) {
		return 0;
	}

	@Override
	public int getDamageReductionAmount(EntityEquipmentSlot entityEquipmentSlot) {
		return 0;
	}

	@Override
	public int getEnchantability() {
		return 0;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return null;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public float getToughness() {
		return 0;
	}
}
