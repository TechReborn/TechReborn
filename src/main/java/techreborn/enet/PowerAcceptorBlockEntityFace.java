package techreborn.enet;

import net.minecraft.util.math.Direction;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;

public class PowerAcceptorBlockEntityFace {
	private final PowerAcceptorBlockEntity entity;
	private final Direction face;

	public PowerAcceptorBlockEntityFace(PowerAcceptorBlockEntity entity, Direction face) {
		this.entity = entity;
		this.face = face;
	}

	public boolean canAcceptEnergy() {
		return entity.canAcceptEnergy(face);
	}

	public boolean canProvideEnergy() {
		return entity.canProvideEnergy(face);
	}

	public double getEnergy() {
		return entity.getEnergy();
	}

	public double getFreeSpace() { return entity.getFreeSpace(); }

	public boolean hasFreeSpace() { return getFreeSpace() > 0; }

	public double useEnergy(double amount) { return entity.useEnergy(amount); }

	public double addEnergy(double amount) { return entity.addEnergy(amount); }

	public boolean isSameEntity(PowerAcceptorBlockEntityFace other) { return entity == other.entity; }
}
