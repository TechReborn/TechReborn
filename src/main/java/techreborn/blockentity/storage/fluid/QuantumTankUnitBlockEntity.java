package techreborn.blockentity.storage.fluid;

import reborncore.common.fluid.FluidValue;
import reborncore.common.util.Tank;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;

public class QuantumTankUnitBlockEntity extends TankUnitBaseBlockEntity {

	// Save compatibility TODO remove next MC release
	public Tank tank = new Tank("QuantumTankBlockEntity", FluidValue.INFINITE, this);

	// Save
	public QuantumTankUnitBlockEntity(){
		super(TRBlockEntities.QUANTUM_TANK_UNIT);
		super.tank = tank;
	}
}
