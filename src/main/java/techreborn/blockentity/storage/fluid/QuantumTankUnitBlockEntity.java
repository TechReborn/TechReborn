package techreborn.blockentity.storage.fluid;

import reborncore.common.fluid.FluidValue;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;

public class QuantumTankUnitBlockEntity extends TankUnitBaseBlockEntity {

	public QuantumTankUnitBlockEntity(){
		super(TRBlockEntities.QUANTUM_TANK_UNIT, FluidValue.BUCKET.multiply(TechRebornConfig.quantumTankCapacity  ));
	}
}
