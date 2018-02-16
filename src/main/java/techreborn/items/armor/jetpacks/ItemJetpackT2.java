package techreborn.items.armor.jetpacks;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import techreborn.client.TechRebornCreativeTab;

public class ItemJetpackT2 extends ItemArmor{

	public ItemJetpackT2() {
		super(ArmorMaterial.IRON, 7, EntityEquipmentSlot.CHEST);
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.jetpackt2");
		setMaxStackSize(1);
	}
}
