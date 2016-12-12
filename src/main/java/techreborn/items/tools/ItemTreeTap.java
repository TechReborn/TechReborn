package techreborn.items.tools;

import net.minecraft.item.ItemStack;
import techreborn.client.TechRebornCreativeTab;
import techreborn.items.ItemTRNoDestroy;

public class ItemTreeTap extends ItemTRNoDestroy {

	public ItemTreeTap() {
		setMaxStackSize(1);
		setMaxDamage(20);
		setUnlocalizedName("techreborn.treetap");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getMetadata() != 0;
	}
}
