package techreborn.items;

import net.minecraft.item.Item;

public class DrillHeadItem extends Item {
	public int range;

	public DrillHeadItem(Settings settings, int range) {
		super(settings);
		this.range = range;
	}


}
