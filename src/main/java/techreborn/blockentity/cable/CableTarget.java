package techreborn.blockentity.cable;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

final class CableTarget {
	private final Direction directionTo;
	private final BlockApiCache<EnergyStorage, Direction> cache;
	// Set to true when the cable inserts into us. This prevents us from extracting again from it.
	boolean insertedIntoUs = false;

	public CableTarget(Direction directionTo, BlockApiCache<EnergyStorage, Direction> cache) {
		this.directionTo = directionTo;
		this.cache = cache;
	}

	@Nullable
	EnergyStorage find() {
		return cache.find(directionTo.getOpposite());
	}
}
