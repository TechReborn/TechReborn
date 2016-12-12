package techreborn.items.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import techreborn.client.TechRebornCreativeTabMisc;

/**
 * Created by modmuss50 on 26/02/2016.
 */
public class ItemTRArmour extends ItemArmor {

	private ArmorMaterial material = ArmorMaterial.LEATHER;
	private EntityEquipmentSlot slot = EntityEquipmentSlot.HEAD;

	public ItemTRArmour(ArmorMaterial material, EntityEquipmentSlot slot) {
		super(material, material.getDamageReductionAmount(slot), slot);
		if (slot == EntityEquipmentSlot.HEAD)
			setUnlocalizedName(material.name().toLowerCase() + "Helmet");
		if (slot == EntityEquipmentSlot.CHEST)
			setUnlocalizedName(material.name().toLowerCase() + "Chestplate");
		if (slot == EntityEquipmentSlot.LEGS)
			setUnlocalizedName(material.name().toLowerCase() + "Leggings");
		if (slot == EntityEquipmentSlot.FEET)
			setUnlocalizedName(material.name().toLowerCase() + "Boots");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.material = material;
		this.slot = slot;
	}
}
