package techreborn.blockentity.conduit;

import net.minecraft.util.math.Direction;

public interface IConduit  {
	boolean canConnect(Direction face);
}
