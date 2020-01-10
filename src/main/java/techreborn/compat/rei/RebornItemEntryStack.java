package techreborn.compat.rei;

import me.shedaniel.rei.impl.ItemEntryStack;
import net.minecraft.item.ItemStack;

public class RebornItemEntryStack extends ItemEntryStack {

	public RebornItemEntryStack(ItemStack itemStack) {
		super(itemStack);
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		return RebornEntryStack.compareFluids(this, obj);
	}

}
