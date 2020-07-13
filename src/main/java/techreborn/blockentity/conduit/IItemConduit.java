package techreborn.blockentity.conduit;

import net.minecraft.util.math.Direction;

public interface IItemConduit extends IConduit {
	boolean transferItem(ItemTransfer itemTransfer, Direction origin);
}
