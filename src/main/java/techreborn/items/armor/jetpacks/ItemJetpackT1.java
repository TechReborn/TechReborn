package techreborn.items.armor.jetpacks;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import techreborn.client.TechRebornCreativeTab;

public class ItemJetpackT1 extends ItemArmor {

	public ItemJetpackT1() {
		super(ArmorMaterial.LEATHER, 7, EntityEquipmentSlot.FEET);
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.jetpackt1");
		setMaxStackSize(1);
	}
}
