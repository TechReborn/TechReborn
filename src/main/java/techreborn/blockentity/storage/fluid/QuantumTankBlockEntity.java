package techreborn.blockentity.storage.fluid;

import reborncore.common.fluid.FluidValue;
import techreborn.blockentity.bases.TankStorageBaseBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class QuantumTankBlockEntity extends TankStorageBaseBlockEntity {

	public QuantumTankBlockEntity(){
		super(TRBlockEntities.QUANTUM_TANK, TRContent.Machine.QUANTUM_TANK.getStack(), FluidValue.BUCKET.multiply(TechRebornConfig.quantumTankCapacity  ));
	}
}
