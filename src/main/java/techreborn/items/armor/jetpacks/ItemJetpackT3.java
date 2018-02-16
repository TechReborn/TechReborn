package techreborn.items.armor.jetpacks;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import techreborn.client.TechRebornCreativeTab;

public class ItemJetpackT3 extends ItemArmor {

	public ItemJetpackT3() {
		super(ArmorMaterial.DIAMOND, 7, EntityEquipmentSlot.CHEST);
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.jetpackt3");
		setMaxStackSize(1);
	}
}
