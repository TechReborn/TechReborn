package techreborn.blockentity.storage.fluid;

import reborncore.common.fluid.FluidValue;
import reborncore.common.util.Tank;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class QuantumTankBlockEntity extends TankUnitBaseBlockEntity {

	// Save compatibility TODO remove next MC release
	public Tank tank = new Tank("QuantumTankBlockEntity", FluidValue.BUCKET.multiply(TechRebornConfig.quantumTankUnitCapacity), this);

	// Save
	public QuantumTankBlockEntity() {
		super(TRContent.TankUnit.QUANTUM);
		super.tank = tank;
	}
}
