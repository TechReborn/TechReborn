package techreborn.blockentity.cable;

import net.minecraft.util.math.Direction;
import team.reborn.energy.api.EnergyStorage;

/**
 * EnergyStorage adjacent to an energy cable, with some additional info.
 */
class OfferedEnergyStorage {
	final CableBlockEntity sourceCable;
	final Direction direction;
	final EnergyStorage storage;

	OfferedEnergyStorage(CableBlockEntity sourceCable, Direction direction, EnergyStorage storage) {
		this.sourceCable = sourceCable;
		this.direction = direction;
		this.storage = storage;
	}

	void afterTransfer() {
		// Block insertions from this side.
		sourceCable.blockedSides |= 1 << direction.ordinal();
	}
}
